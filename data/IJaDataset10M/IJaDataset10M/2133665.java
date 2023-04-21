package com.googlecode.bdoc.testsupport.excel;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelExampleTables {

    private String xlsFilePath;

    private HSSFWorkbook workbook;

    public ExcelExampleTables(String xlsFilePath) {
        this.xlsFilePath = xlsFilePath;
        try {
            workbook = new HSSFWorkbook(new FileInputStream(xlsFilePath));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error reading xls from: " + xlsFilePath, e);
        }
    }

    public List<String> sheetNames() {
        List<String> result = new ArrayList<String>();
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int index = 0; index < numberOfSheets; index++) {
            result.add(workbook.getSheetName(index));
        }
        return result;
    }

    public ExcelExampleTable getTable(String tableDescription) {
        HSSFSheet sheet = workbook.getSheetAt(0);
        int rowIndex = -1;
        int cellnum = -1;
        HSSFCell cell = null;
        try {
            for (rowIndex = 0; rowIndex < sheet.getLastRowNum(); rowIndex++) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (null == row) {
                    continue;
                }
                for (cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
                    cell = row.getCell(cellnum);
                    if ((HSSFCell.CELL_TYPE_STRING == cell.getCellType()) && tableDescription.equals(cell.getRichStringCellValue().getString())) {
                        return new ExcelExampleTable(sheet, rowIndex, cellnum);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Problem with cell (row=" + rowIndex + ",cellnum=" + cellnum + ",val=" + cell + ") getting table [" + tableDescription + "] from [" + xlsFilePath + "]", e);
        }
        throw new IllegalArgumentException("Can't find [" + tableDescription + "] in [" + xlsFilePath + "]");
    }
}
