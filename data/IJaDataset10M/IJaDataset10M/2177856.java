package com.aimluck.eip.schedule;

/**
 * スケジュールの検索結果を管理するクラスです。
 * 
 */
public class ScheduleOnedayResultData extends ScheduleResultData {

    /** <code>startRow</code> 開始 */
    private int startRow;

    /** <code>endRow</code> 終了 */
    private int endRow;

    /** <code>index</code> インデックス */
    private int index;

    private int dRowCount;

    /**
   * 終了地点を取得します。
   * 
   * @return endRow
   */
    public int getEndRow() {
        return endRow;
    }

    /**
   * 終了地点を設定します。
   * 
   * @param endRow
   */
    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    /**
   * 開始地点を取得します。
   * 
   * @return startRow
   */
    public int getStartRow() {
        return startRow;
    }

    /**
   * 開始地点を設定します。
   * 
   * @param startRow
   */
    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    /**
   * インデックスを取得します。
   * 
   * @return index
   */
    public int getIndex() {
        return index;
    }

    /**
   * インデックスを設定します。
   * 
   * @param index
   */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
   * @param dRowCount
   */
    public void setdRowCount(int dRowCount) {
        this.dRowCount = dRowCount;
    }

    /**
   * @return dRowCount
   */
    public int getdRowCount() {
        return dRowCount;
    }
}
