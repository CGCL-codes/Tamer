package org.seasar.webhelpers.validator.reader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * Excelファイルの内容を読み込み、DataSetを返すクラスです。<br>
 * {@link org.seasar.extension.dataset.impl.XlsReader}では、
 * ヘッダとそれに対するデータ定義を読み込むのに対して、
 * このクラスでは、任意のデータを読み込むようになっています。<br>
 * そのため、行によって、列数などが変わってもデータを読み込むことが可能です。 
 * 
 * @author takanori
 */
public class ExcelReader {

    private HSSFWorkbook workbook;

    /**
     * インスタンスを生成します。
     * 
     * @param path Excelファイルのパス
     */
    public ExcelReader(String path) {
        this(ResourceUtil.getResourceAsStream(path));
    }

    /**
     * インスタンスを生成します。
     * 
     * @param in Excelファイルの入力ストリーム
     */
    public ExcelReader(InputStream in) {
        try {
            this.workbook = new HSSFWorkbook(in);
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }

    /**
     * Excelに定義されたデータを取得します。
     * 
     * @return データセット
     */
    public DataSet read() {
        DataSet dataSet = new DataSetImpl();
        for (int i = 0; i < this.workbook.getNumberOfSheets(); i++) {
            String sheetName = this.workbook.getSheetName(i);
            DataTable dataTable = dataSet.addTable(sheetName);
            buildTable(dataTable, this.workbook.getSheetAt(i));
        }
        return dataSet;
    }

    private void buildTable(DataTable dataTable, HSSFSheet sheet) {
        int rowSize = sheet.getLastRowNum() + 1;
        int colSizeMax = 0;
        for (int i = 0; i < rowSize; i++) {
            HSSFRow row = sheet.getRow(i);
            if (row != null) {
                int colSize = row.getLastCellNum();
                if (colSize > colSizeMax) {
                    colSizeMax = colSize;
                }
            }
        }
        for (int i = 0; i < colSizeMax; i++) {
            dataTable.addColumn(Integer.toString(i));
        }
        for (int i = 0; i < rowSize; i++) {
            HSSFRow row = sheet.getRow(i);
            DataRow dataRow = dataTable.addRow();
            buildRow(dataRow, row);
        }
    }

    private void buildRow(DataRow dataRow, HSSFRow row) {
        if (row == null) {
            return;
        }
        int colSize = row.getLastCellNum();
        for (int i = 0; i < colSize; i++) {
            HSSFCell cell = row.getCell((short) i);
            Object value = getValue(cell);
            dataRow.setValue(i, value);
        }
    }

    private Object getValue(HSSFCell cell) {
        if (cell == null) {
            return null;
        }
        Object value;
        switch(cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                final double numericCellValue = cell.getNumericCellValue();
                if (numericCellValue == (int) numericCellValue) {
                    value = new BigDecimal((int) numericCellValue);
                } else {
                    value = new BigDecimal(Double.toString(numericCellValue));
                }
                break;
            case HSSFCell.CELL_TYPE_STRING:
                String s = cell.getStringCellValue();
                if (s != null) {
                    s = StringUtil.rtrim(s);
                }
                value = s;
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                boolean b = cell.getBooleanCellValue();
                value = Boolean.valueOf(b);
                break;
            default:
                value = null;
                break;
        }
        return value;
    }
}
