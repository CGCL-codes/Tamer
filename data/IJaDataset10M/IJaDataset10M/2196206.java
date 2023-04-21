package com.amwebexpert.generator;

import java.io.FileOutputStream;
import java.util.Date;
import junit.framework.TestCase;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.amwebexpert.tags.workbook.model.InMemoryWorkbook;
import com.amwebexpert.tags.workbook.model.InMemoryWorkbookBin;

public class WorkbookGeneratorTest extends TestCase {

    public void _testGenerateWorkbook() {
        WorkbookGenerator gen = new WorkbookGenerator();
        gen.setSheetName("Sheet Number 1");
        gen.setTitle("This is my title");
        gen.setNbColumns(4);
        gen.setNbRows(10);
        gen.setHeaderRow(true);
        gen.setHeaderCol(false);
        gen.setDefaultColumnWidth(25);
        try {
            Workbook wkb = gen.generateWorkbook();
            wkb.write(new FileOutputStream("/temp/test.xls"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void _testGenerateWorkbook2() {
        WorkbookGenerator gen = new WorkbookGenerator();
        gen.setSheetName("Sheet Number 1");
        gen.setTitle("This is my title");
        gen.setNbColumns(4);
        gen.setNbRows(10);
        gen.setHeaderRow(true);
        gen.setHeaderCol(false);
        gen.setDefaultColumnWidth(25);
        try {
            Workbook wkb = gen.generateWorkbook();
            CreationHelper helper = wkb.getCreationHelper();
            DataFormat df = helper.createDataFormat();
            Sheet sheet = wkb.getSheetAt(0);
            Row row = sheet.getRow(1);
            if (row == null) {
                row = sheet.createRow(1);
            }
            Cell cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            setDateCell(cell, df);
            System.out.println(cell.getCellStyle().getDataFormat());
            System.out.println(cell.getCellStyle().getDataFormatString());
            wkb.write(new FileOutputStream("/temp/test.xls"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testGenerateWorkbook3() {
        WorkbookGenerator gen = new WorkbookGenerator();
        gen.setSheetName("DataFormat");
        gen.setTitle("Data formats");
        gen.setNbColumns(3);
        gen.setNbRows(100);
        gen.setHeaderRow(true);
        gen.setHeaderCol(false);
        gen.setDefaultColumnWidth(25);
        try {
            Workbook wkb = gen.generateWorkbook();
            Sheet sheet = wkb.getSheetAt(0);
            CreationHelper helper = wkb.getCreationHelper();
            DataFormat df = helper.createDataFormat();
            Date dt = new Date();
            int f = 0;
            for (int r = 3; r < 49; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    row = sheet.createRow(r);
                }
                Cell cell = row.getCell(0);
                if (cell == null) {
                    cell = row.createCell(0);
                }
                cell.setCellValue((double) f);
                cell = row.getCell(1);
                if (cell == null) {
                    cell = row.createCell(1);
                }
                String fmt = df.getFormat((short) f);
                cell.setCellValue(fmt);
                if (fmt.contains("m")) {
                    cell = row.getCell(2);
                    if (cell == null) {
                        cell = row.createCell(2);
                    }
                    CellStyle cellStyle = wkb.createCellStyle();
                    cellStyle.setDataFormat(df.getFormat(fmt));
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(dt);
                }
                f++;
            }
            wkb.write(new FileOutputStream("/temp/test.xls"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private void setNumberCell(Cell cell, DataFormat df) {
        cell.setCellValue(1234567);
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle.setDataFormat(df.getFormat("$ #,##0.00"));
    }

    private void setDateCell(Cell cell, DataFormat df) {
        cell.setCellValue(new Date());
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle.setDataFormat(df.getFormat("dd/MM/yyyy"));
    }

    public void _testFmt() throws Exception {
        InMemoryWorkbook wkb = new InMemoryWorkbookBin("/temp/test.xls");
        Cell cell = wkb.getCellAt(wkb.getWorkbook().getSheetAt(0), 1, 0);
        CellStyle cellStyle = cell.getCellStyle();
        System.out.println(cellStyle.getDataFormatString());
    }
}
