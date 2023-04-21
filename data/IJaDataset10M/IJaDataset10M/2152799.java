package com.sptci.rwt;

import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

/**
 * An Excel workbook generator for data contained in a {@link Rows}
 * object.  This uses the
 * <a href='http://poi.apache.org/hssf/index.html'>Apache POI HSSF</a>
 * library for creating Excel workbooks.
 *
 * <p>&copy; Copyright 2007 Sans Pareil Technologies, Inc.</p>
 * @author Rakesh Vidyadharan 2007-10-09
 * @version $Id: ExcelGenerator.java 20 2007-11-10 00:40:51Z rakesh.vidyadharan $
 */
class ExcelGenerator {

    /**
   * Create a new workbook that holds the data in {@link Rows}.  Also
   * display <code>query</code> to indicate the statement that was used to
   * generate the results.
   *
   * @see #createQuery
   * @see #createHeader
   * @see #createRow
   * @param query The SQL query to execute.
   * @param rows The list of rows data set to export.
   * @return The work book that contains the data in <code>rows</code>.
   */
    HSSFWorkbook generate(final String query, final List<Rows> rows) {
        final HSSFWorkbook workbook = new HSSFWorkbook();
        int count = 0;
        for (Rows result : rows) {
            final HSSFSheet sheet = workbook.createSheet("Query Results: " + ++count);
            boolean first = true;
            short index = 0;
            for (Row row : result.getRows()) {
                if (first) {
                    createQuery(query, workbook, sheet, index++, (short) row.getColumns().size());
                    createHeader(row, workbook, sheet, index++);
                    first = false;
                }
                createRow(row, workbook, sheet, index++);
            }
        }
        return workbook;
    }

    /**
   * Create the cells used to display the SQL statement that was executed
   * to generate the results.
   *
   * @param query The SQL statement used to generate the results.
   * @param workbook The excel workbook.
   * @param sheet The excel work sheet.
   * @param index The row number for the header.
   * @param columns The number of columns this row spans.
   */
    private void createQuery(final String query, final HSSFWorkbook workbook, final HSSFSheet sheet, final short index, final short columns) {
        HSSFFont font = workbook.createFont();
        font.setFontName("Helvetica");
        font.setItalic(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        HSSFRow sheetRow = sheet.createRow(index);
        HSSFCell cell = sheetRow.createCell((short) 0);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(query));
        sheet.addMergedRegion(new Region(index, (short) 0, index, columns));
    }

    /**
   * Create the header row for the excel sheet using the {@link
   * com.sptci.rwt.Column#name} field.
   *
   * @param row The row instance from which column names are extracted.
   * @param workbook The excel workbook.
   * @param sheet The sheet in which the header row is to be inserted.
   * @param index The row number for the header.
   */
    private void createHeader(final Row row, final HSSFWorkbook workbook, final HSSFSheet sheet, final short index) {
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("Helvetica");
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        HSSFRow sheetRow = sheet.createRow(index);
        short columnIndex = 0;
        for (Column column : row.getColumns()) {
            HSSFCell cell = sheetRow.createCell(columnIndex++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(column.getName()));
        }
    }

    /**
   * Create a header row for the excel sheet using the {@link
   * com.sptci.rwt.Column#name} field.
   *
   * @param row The row instance from which column names are extracted.
   * @param workbook The excel workbook.
   * @param sheet The sheet in which the header row is to be inserted.
   * @param index The row index to set.
   */
    private void createRow(final Row row, final HSSFWorkbook workbook, final HSSFSheet sheet, final short index) {
        HSSFFont font = workbook.createFont();
        font.setFontName("Helvetica");
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        HSSFRow sheetRow = sheet.createRow(index);
        short columnIndex = 0;
        for (Column column : row.getColumns()) {
            HSSFCell cell = sheetRow.createCell(columnIndex++);
            cell.setCellStyle(style);
            if (column.getContent() != null) {
                cell.setCellValue(new HSSFRichTextString(column.getContent().toString()));
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }
        }
    }
}
