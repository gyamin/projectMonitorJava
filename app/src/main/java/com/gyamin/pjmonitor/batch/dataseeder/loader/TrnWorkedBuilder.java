package com.gyamin.pjmonitor.batch.dataseeder.loader;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gyamin.pjmonitor.batch.dataseeder.csvreader.CsvReaderInterface;
import com.gyamin.pjmonitor.batch.dataseeder.csvreader.SagyouJikanShukei;
import com.gyamin.pjmonitor.entity.TrnWorked;
import com.gyamin.pjmonitor.common.AppLogging;
import dataseeder.csvreader.IssueInterface;
import dataseeder.csvreader.FirstDJ;
import dataseeder.csvreader.SecondDJ;

import java.sql.SQLException;

/**
 * issuesテーブルにcsvからマスタを設定する
 * @author Yasumasa
 */
public class TrnWorkedBuilder extends AbstractBuilder {
  // 取り込みデータ配列
  private ArrayList<TrnWorked> trnWorkeds = new ArrayList<>();

  /**
   * データソースからデータを読み込み、データ配列を準備する
   */
  @Override
  void prepareImportBeansFromFile() {
    // 対象CSV Reader設定
    CsvReaderInterface targetCsvs[] = new CsvReaderInterface[1];
    targetCsvs[0] = new SagyouJikanShukei();
    for (IssueInterface elem : targetCsvs) {
      // 読み込んだIssueBeanオブジェクトを結合
      System.out.println(elem.toString());
      this.importBeans.addAll(elem.getImportBeansFromFile());
    }
  }
  
  /**
   * データ配列からテーブルにデータを登録する
   */
  @Override
  public void buildUpData() {
    Logger.getLogger(AppLogging.LOG_NAME).log(Level.INFO, "START: issuesテーブルデータ設定");
    try {
      dao.Issues daoIssues = new dao.Issues(null);
      // データ削除
      daoIssues.truncateIssues();
      // データ登録
      this.prepareImportBeansFromFile();
      daoIssues.insertIssues(this.importBeans);    
    } catch (SQLException ex) {
      Logger.getLogger(AppLogging.LOG_NAME).log(Level.SEVERE, null , ex);
    }
  }
}