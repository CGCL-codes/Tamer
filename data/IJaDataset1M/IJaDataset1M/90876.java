package com.ideo.sweetdevria.taglib.grid.export.formatter.cellFormatter;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class DefaultCellFormatter implements CellFormatter {

    protected HSSFFont fontTitle;

    protected HSSFFont fontHeader;

    protected HSSFFont fontLine;

    private short black = (new HSSFColor.BLACK()).getIndex();

    public DefaultCellFormatter() {
        fontTitle = fontHeader = fontLine = null;
    }

    public void applyTitleStyle(HSSFWorkbook workbook, HSSFCellStyle cellStyle, int x, int y, Object value) {
        if (fontTitle == null) {
            fontTitle = createFont(workbook, fontTitle, black, (short) 14, HSSFFont.BOLDWEIGHT_BOLD);
        }
        setCellStyle(cellStyle, HSSFCellStyle.BORDER_NONE, fontTitle);
        doAfterTitleStyle(workbook, cellStyle, x, y, value);
    }

    protected void doAfterTitleStyle(HSSFWorkbook workbook, HSSFCellStyle cellStyle, int x, int y, Object value) {
    }

    public void applyHeaderStyle(HSSFWorkbook workbook, HSSFCellStyle cellStyle, int x, int y, Object value) {
        if (fontHeader == null) {
            fontHeader = createFont(workbook, fontHeader, black, (short) 12, HSSFFont.BOLDWEIGHT_BOLD);
        }
        setCellStyle(cellStyle, HSSFCellStyle.BORDER_MEDIUM, fontHeader);
        doAfterHeaderStyle(workbook, cellStyle, x, y, value);
    }

    protected void doAfterHeaderStyle(HSSFWorkbook workbook, HSSFCellStyle cellStyle, int x, int y, Object value) {
    }

    public void applyDataStyle(HSSFWorkbook workbook, HSSFCellStyle cellStyle, int x, int y, Object value) {
        if (fontLine == null) {
            fontLine = createFont(workbook, fontLine, black, (short) 10, HSSFFont.BOLDWEIGHT_NORMAL);
        }
        setCellStyle(cellStyle, HSSFCellStyle.BORDER_THIN, fontLine);
        doAfterDataStyle(workbook, cellStyle, x, y, value);
    }

    protected void doAfterDataStyle(HSSFWorkbook workbook, HSSFCellStyle cellStyle, int x, int y, Object value) {
    }

    protected HSSFCellStyle setCellStyle(HSSFCellStyle cellStyle, short borderStyle, HSSFFont font) {
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderRight(borderStyle);
        return cellStyle;
    }

    protected HSSFFont createFont(HSSFWorkbook workbook, HSSFFont font, short fontColor, short fontSize, short fontWeight) {
        if (font == null) {
            font = workbook.createFont();
            font = workbook.createFont();
            font.setFontHeightInPoints(fontSize);
            font.setBoldweight(fontWeight);
            font.setColor(fontColor);
        }
        return font;
    }
}
