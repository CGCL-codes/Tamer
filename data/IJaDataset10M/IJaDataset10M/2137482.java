package org.apache.poi.hssf.usermodel;

import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationSheet;

/**
 * HSSF wrapper for a sheet under evaluation
 * 
 * @author Josh Micich
 */
final class HSSFEvaluationSheet implements EvaluationSheet {

    private final HSSFSheet _hs;

    public HSSFEvaluationSheet(HSSFSheet hs) {
        _hs = hs;
    }

    public HSSFSheet getHSSFSheet() {
        return _hs;
    }

    public EvaluationCell getCell(int rowIndex, int columnIndex) {
        HSSFRow row = _hs.getRow(rowIndex);
        if (row == null) {
            return null;
        }
        HSSFCell cell = row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }
        return new HSSFEvaluationCell(cell, this);
    }
}
