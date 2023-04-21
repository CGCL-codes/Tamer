package org.vardb.util.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.vardb.util.CBeanHelper;
import org.vardb.util.CStringHelper;

public final class CJExcelHelper {

    private CJExcelHelper() {
    }

    public static WritableWorkbook createWorkbook(String filename) throws WriteException, IOException {
        return Workbook.createWorkbook(new File(filename));
    }

    public static void writeWorkbook(WritableWorkbook workbook) throws Exception {
        workbook.write();
        workbook.close();
    }

    public static List<String> getProperties(Sheet sheet) {
        List<String> properties = new ArrayList<String>();
        int row = 0;
        for (int col = 0; col < sheet.getColumns(); col++) {
            Cell cell = sheet.getCell(col, row);
            if (!CStringHelper.hasContent(cell.getContents())) break;
            properties.add(cell.getContents().trim());
        }
        return properties;
    }

    public static void createWorksheet(WritableSheet sheet, List<String> properties, List<?> objects) throws WriteException {
        CBeanHelper helper = new CBeanHelper();
        int c = 0;
        int r = 0;
        for (String property : properties) {
            sheet.addCell(new Label(c++, r, property));
        }
        for (Object obj : objects) {
            c = 0;
            r++;
            for (String property : properties) {
                Object value = helper.getProperty(obj, property);
                if (value instanceof String) CJExcelHelper.addCell(sheet, c++, r, (String) value); else if (value instanceof Integer) CJExcelHelper.addCell(sheet, c++, r, (Integer) value); else if (value instanceof Double) CJExcelHelper.addCell(sheet, c++, r, (Double) value); else if (value instanceof Double) CJExcelHelper.addCell(sheet, c++, r, (Float) value); else CJExcelHelper.addCell(sheet, c++, r, value);
            }
        }
    }

    public static WritableSheet createWorksheet(WritableWorkbook wb, CTable table, String sheetname) throws WriteException {
        WritableSheet sheet = wb.createSheet(sheetname, 0);
        int r = 0;
        int c = 0;
        for (CTable.Cell cell : table.getHeader().getCells()) {
            addHeaderCell(sheet, c++, r, cell.getStringValue());
        }
        for (CTable.Row tablerow : table.getRows()) {
            c = 0;
            r++;
            for (CTable.Cell cell : tablerow.getCells()) {
                addCell(sheet, c++, r, cell.getValue());
            }
        }
        return sheet;
    }

    public static void addHeaderCell(WritableSheet sheet, int c, int r, String value) throws WriteException {
        if (value == null) return;
        sheet.addCell(new Label(c, r, value, createHeaderCellFormat()));
    }

    public static void addCell(WritableSheet sheet, int c, int r, String value) throws WriteException {
        if (value == null) return;
        sheet.addCell(new Label(c, r, value));
    }

    public static void addCell(WritableSheet sheet, int c, int r, Integer value) throws WriteException {
        if (value == null) return;
        sheet.addCell(new Number(c, r, value));
    }

    public static void addCell(WritableSheet sheet, int c, int r, Double value) throws WriteException {
        addCell(sheet, c, r, value, null);
    }

    public static void addCell(WritableSheet sheet, int c, int r, Double value, WritableCellFormat format) throws WriteException {
        if (value == null) return;
        sheet.addCell(new Number(c, r, value, format));
    }

    public static void addCell(WritableSheet sheet, int c, int r, Float value) throws WriteException {
        addCell(sheet, c, r, value, null);
    }

    public static void addCell(WritableSheet sheet, int c, int r, Float value, WritableCellFormat format) throws WriteException {
        if (value == null) return;
        sheet.addCell(new Number(c, r, value, format));
    }

    public static void addCell(WritableSheet sheet, int c, int r, Object value) throws WriteException {
        if (value == null) return;
        sheet.addCell(new Label(c, r, value.toString()));
    }

    @SuppressWarnings("unused")
    private static WritableCellFormat createDecimalFormat(int dps) {
        String pattern = "#." + CStringHelper.repeatString("#", dps);
        return createNumberFormat(pattern);
    }

    private static WritableCellFormat createNumberFormat(String pattern) {
        return new WritableCellFormat(new NumberFormat(pattern));
    }

    private static WritableCellFormat createHeaderCellFormat() throws WriteException {
        int fontSize = 10;
        boolean italics = false;
        WritableFont font = new WritableFont(WritableFont.ARIAL, fontSize, WritableFont.BOLD, italics);
        WritableCellFormat format = new WritableCellFormat(font);
        format.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
        return format;
    }
}
