package org.vardb.util.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.vardb.util.CDateHelper;
import org.vardb.util.CException;
import org.vardb.util.CFileHelper;
import org.vardb.util.CStringHelper;

public class CExcelHelper {

    public CTable extractTable(String filename) {
        CTable table = new CTable();
        table.setIdentifier(CFileHelper.getIdentifierFromFilename(filename));
        Workbook workbook = openSpreadsheet(filename);
        Sheet sheet = workbook.getSheetAt(0);
        boolean isHeader = true;
        for (Row row : sheet) {
            if (isHeader) {
                extractHeaderRow(row, table);
                isHeader = false;
            } else extractRow(row, table);
        }
        return table;
    }

    private void extractHeaderRow(Row row, CTable table) {
        CTable.Row trow = table.getHeader();
        for (Cell cell : row) {
            String value = cell.getStringCellValue();
            if (!CStringHelper.hasContent(value)) return;
            trow.add(value);
        }
    }

    private void extractRow(Row row, CTable table) {
        if (!CStringHelper.hasContent(getCellValue(row.getCell(0)))) {
            System.out.println("first column is empty - skipping");
            return;
        }
        CTable.Row trow = table.addRow();
        for (int colnum = 0; colnum < table.getHeader().size(); colnum++) {
            Cell cell = row.getCell(colnum);
            if (colnum == 0) trow.add(getIdentifierCellValue(cell)); else trow.add(getCellValue(cell));
        }
    }

    private String getIdentifierCellValue(Cell cell) {
        Object value = getCellValue(cell);
        if (value instanceof Double) return CStringHelper.formatDecimal((Double) value, 0); else return value.toString();
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) return null;
        switch(cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case Cell.CELL_TYPE_NUMERIC:
                return getNumericValue(cell);
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return getFormulaValue(cell);
            case Cell.CELL_TYPE_ERROR:
                return cell.getErrorCellValue();
            default:
                System.out.println("unhandled cell type: " + cell.getCellType());
                return null;
        }
    }

    private Object getFormulaValue(Cell cell) {
        try {
            if (cell.getCellType() == Cell.CELL_TYPE_BLANK) return null;
            FormulaEvaluator evaluator = getFormulaEvaluator(cell);
            CellValue cellValue = evaluator.evaluate(cell);
            switch(cellValue.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    return null;
                case Cell.CELL_TYPE_STRING:
                    return cellValue.getStringValue();
                case Cell.CELL_TYPE_NUMERIC:
                    return getNumericValue(cell, cellValue);
                case Cell.CELL_TYPE_BOOLEAN:
                    return cellValue.getBooleanValue();
                case Cell.CELL_TYPE_ERROR:
                    return cellValue.formatAsString();
                default:
                    System.out.println("unhandled cellValue type: " + cellValue.getCellType());
                    return null;
            }
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    private Object getNumericValue(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) return cell.getDateCellValue(); else return cell.getNumericCellValue();
    }

    private Object getNumericValue(Cell cell, CellValue cellValue) {
        if (DateUtil.isCellDateFormatted(cell)) return DateUtil.getJavaDate(cellValue.getNumberValue()); else return cellValue.getNumberValue();
    }

    public Workbook openSpreadsheet(String filename) {
        try {
            InputStream instream = new FileInputStream(filename);
            return WorkbookFactory.create(instream);
        } catch (FileNotFoundException e) {
            throw new CException(e);
        } catch (InvalidFormatException e) {
            throw new CException(e);
        } catch (IOException e) {
            throw new CException(e);
        }
    }

    public Object getCellValue(Sheet sheet, int rownum, int colnum) {
        Row row = sheet.getRow(rownum);
        if (row == null) throw new CException("row is null for rownum " + rownum + " in sheet " + sheet.getSheetName());
        Cell cell = row.getCell(colnum);
        if (row == null) throw new CException("cell is null for rownum " + rownum + " and colnum " + colnum + " in sheet " + sheet.getSheetName());
        return getCellValue(cell);
    }

    public Object getCellValue(Sheet sheet, String address) {
        CellReference cellReference = new CellReference(address);
        Row row = sheet.getRow(cellReference.getRow());
        Cell cell = row.getCell(cellReference.getCol());
        return getCellValue(cell);
    }

    private FormulaEvaluator getFormulaEvaluator(Cell cell) {
        Sheet sheet = cell.getSheet();
        Workbook workbook = sheet.getWorkbook();
        return workbook.getCreationHelper().createFormulaEvaluator();
    }

    private CellStyle headerStyle = null;

    public Workbook createWorkbook() {
        return new HSSFWorkbook();
    }

    public Workbook createWorkbook(String filename) {
        Workbook workbook = createWorkbook();
        writeWorkbook(workbook, filename);
        return workbook;
    }

    public void writeWorkbook(Workbook workbook, String filename) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            workbook.write(out);
        } catch (Exception e) {
            throw new CException(e);
        } finally {
            CFileHelper.closeStream(out);
        }
    }

    public Sheet createWorksheet(Workbook workbook, CTable table, String sheetname) {
        Sheet sheet = workbook.createSheet(sheetname);
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

    public void addHeaderCell(Sheet sheet, int c, int r, String value) {
        if (value == null) return;
        Cell cell = createCell(sheet, c, r);
        cell.setCellValue(value);
        cell.setCellStyle(getHeaderCellStyle(sheet.getWorkbook()));
    }

    public void addCell(Sheet sheet, int c, int r, String value) {
        if (value == null) return;
        Cell cell = createCell(sheet, c, r);
        cell.setCellValue(value);
    }

    public void addCell(Sheet sheet, int c, int r, Integer value) {
        if (value == null) return;
        Cell cell = createCell(sheet, c, r);
        cell.setCellValue(value);
    }

    public void addCell(Sheet sheet, int c, int r, Double value) {
        if (value == null) return;
        Cell cell = createCell(sheet, c, r);
        cell.setCellValue(value);
    }

    public void addCell(Sheet sheet, int c, int r, Double value, CellStyle style) {
        if (value == null) return;
        Cell cell = createCell(sheet, c, r);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    public void addCell(Sheet sheet, int c, int r, Float value) {
        if (value == null) return;
        Cell cell = createCell(sheet, c, r);
        cell.setCellValue(value);
    }

    public void addCell(Sheet sheet, int c, int r, Float value, CellStyle style) {
        if (value == null) return;
        Cell cell = createCell(sheet, c, r);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    public void addCell(Sheet sheet, int c, int r, Object value) {
        if (value == null) return;
        Cell cell = createCell(sheet, c, r);
        CDataType type = CDataType.guessDataType(value);
        System.out.println("adding cell at c=" + c + ", r=" + r + ", value=" + value + ", type=" + type);
        switch(type) {
            case BOOLEAN:
                cell.setCellValue(Boolean.valueOf(value.toString()));
                return;
            case DATE:
                cell.setCellValue(CDateHelper.parse(value.toString(), CDateHelper.DATE_PATTERN));
                return;
            case INTEGER:
                cell.setCellValue(Integer.valueOf(value.toString()));
                return;
            case FLOAT:
                cell.setCellValue(Float.valueOf(value.toString()));
                return;
            default:
                cell.setCellValue(value.toString());
                return;
        }
    }

    private Cell createCell(Sheet sheet, int c, int r) {
        Row row = sheet.getRow(r);
        if (row == null) row = sheet.createRow(r);
        Cell cell = row.createCell(c);
        return cell;
    }

    private CellStyle getHeaderCellStyle(Workbook workbook) {
        if (headerStyle == null) {
            headerStyle = workbook.createCellStyle();
            int fontSize = 10;
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short) fontSize);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            headerStyle.setFont(font);
            headerStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        }
        return headerStyle;
    }
}
