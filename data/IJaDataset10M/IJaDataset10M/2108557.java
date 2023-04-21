package com.ss4o.core.model;

import com.ss4o.core.exception.ValidateException;

/**
 * 行範囲クラス
 * 
 * @author kakusuke
 * 
 */
public class RowRange implements Validatable {

    /** MSG_VALIDATION_ERROR */
    private static final String MSG_VALIDATION_ERROR = "行番号が不正です。";

    /** 開始行 */
    private int fromRow = 0;

    /** 終了行 */
    private int toRow = Integer.MAX_VALUE;

    /**
	 * デフォルトコンストラクタ
	 */
    public RowRange() {
    }

    public RowRange createClone() {
        RowRange rowRange = null;
        try {
            rowRange = (RowRange) this.clone();
        } catch (CloneNotSupportedException e) {
        }
        return rowRange;
    }

    /**
	 * コンストラクタ
	 * 
	 * @param fromRow
	 *            開始行
	 * @param toRow
	 *            終了行
	 */
    public RowRange(int fromRow, int toRow) {
        this.fromRow = fromRow;
        this.toRow = toRow;
    }

    @Override
    public void validate() throws ValidateException {
        if (fromRow < 0 || fromRow > toRow) throw new ValidateException(MSG_VALIDATION_ERROR);
    }

    /**
	 * @return 開始行
	 */
    public int getFromRow() {
        return fromRow;
    }

    /**
	 * @param fromRow
	 *            セットする 開始行
	 */
    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    /**
	 * @return 終了行
	 */
    public int getToRow() {
        return toRow;
    }

    /**
	 * @param toRow
	 *            セットする 終了行
	 */
    public void setToRow(int toRow) {
        this.toRow = toRow;
    }
}
