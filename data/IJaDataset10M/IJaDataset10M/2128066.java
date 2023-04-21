package gov.usda.gdpc.test;

import java.io.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.*;

/**
 *
 * @author  terryc
 */
public class TestPOI {

    /** Creates a new instance of TestPOI */
    public TestPOI() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "D:/terry/NewPanzeaSchema/NEXT/UScultivars_since_1971.xls";
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
            HSSFWorkbook hssfworkbook = new HSSFWorkbook(fs);
            for (int i = 0, n = hssfworkbook.getNumberOfSheets(); i < n; i++) {
                HSSFSheet sheet = hssfworkbook.getSheetAt(i);
                int rows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < rows; j++) {
                    HSSFRow row = sheet.getRow(j);
                    if (row != null) {
                        int cells = row.getPhysicalNumberOfCells();
                        for (int k = 0; k < cells; k++) {
                            HSSFCell cell = row.getCell((short) k);
                            if (cell != null) {
                                String value = null;
                                switch(cell.getCellType()) {
                                    case HSSFCell.CELL_TYPE_FORMULA:
                                        value = "FORMULA";
                                        break;
                                    case HSSFCell.CELL_TYPE_NUMERIC:
                                        value = Double.toString(cell.getNumericCellValue());
                                        break;
                                    case HSSFCell.CELL_TYPE_STRING:
                                        value = cell.getStringCellValue();
                                        break;
                                    default:
                                }
                                System.out.print(value + "  ");
                            } else {
                                System.out.print("NULL  ");
                            }
                        }
                        System.out.println("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("finished");
    }
}
