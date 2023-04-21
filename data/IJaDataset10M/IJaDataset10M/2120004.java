package org.orangegears.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class XLSSpreadSheetParser implements SpreadSheetParser {

    private final File source;

    private final File output;

    private final Map<String, Object> dataContext;

    private final String tempOutputPath = this.getClass().getResource("/runtime/upload").getPath() + "/temp.xls";

    public XLSSpreadSheetParser(File input, Map<String, Object> dataContext) {
        source = input;
        this.dataContext = dataContext;
        output = parse();
    }

    private File parse() {
        HSSFSheet sheet = null;
        HSSFWorkbook wb = null;
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(source));
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int row = sheet.getFirstRowNum(); row <= sheet.getLastRowNum(); row++) {
            HSSFRow cRow = sheet.getRow(row);
            if (cRow != null) {
                for (short column = cRow.getFirstCellNum(); column <= cRow.getLastCellNum(); column++) {
                    if (cRow.getCell(column) != null && cRow.getCell(column).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
                        String value = cRow.getCell(column).getStringCellValue();
                        if (value != "" && value.charAt(0) == '&') {
                            value = value.substring(1);
                            value += ";";
                            try {
                                ExpressionTree eTree = new ExpressionTree(value);
                                Operation op = new Operation(eTree, dataContext);
                                sheet.getRow(row).getCell(column).setCellValue(op.getAnswer().toString());
                            } catch (Exception e) {
                                sheet.getRow(row).getCell(column).setCellValue("#ERROR");
                            }
                        }
                    }
                }
            }
        }
        File outFile = new File(tempOutputPath);
        outFile.deleteOnExit();
        try {
            FileOutputStream tempFile = new FileOutputStream(outFile);
            wb.write(tempFile);
            tempFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outFile;
    }

    public File getParsedFile() {
        return output;
    }
}
