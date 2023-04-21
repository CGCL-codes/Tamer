package test.creator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * @author detlevs
 *
 */
public class RandomExcelSheetCreator {

    /** Logger. */
    private static Logger log = Logger.getLogger(RandomExcelSheetCreator.class);

    /** XLS Template Filename. */
    private String templateFilename = "_blank.xls";

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        RandomExcelSheetCreator runner = new RandomExcelSheetCreator();
        try {
            runner.createRandomExcelRow1000Col10("ExcelRow1000Col10.xls");
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
	 * Create XLS File with 1000 Rows and 10 Column with Random values.
	 * @param filename Excel Filename
	 * @throws IOException
	 */
    public void createRandomExcelRow1000Col10(String filename) throws IOException {
        int rowDef = 100;
        int colDef = 10;
        HSSFWorkbook wb = createWorkbook(filename);
        HSSFSheet sheet = wb.getSheetAt(0);
        for (int row = 0; row < rowDef; row++) {
            HSSFRow rowXls = sheet.createRow(row);
            for (int col = 0; col < colDef; col++) {
                HSSFCell colXls = rowXls.createCell(col);
            }
        }
    }

    /**
	 * Create a new XLS File an return the {@link HSSFWorkbook}
	 * @param filename Excel Filename
	 * @return {@link HSSFWorkbook}
	 * @throws IOException
	 */
    private HSSFWorkbook createWorkbook(String filename) throws IOException {
        InputStream fIn = RandomExcelSheetCreator.class.getResourceAsStream(templateFilename);
        POIFSFileSystem fPoiFS = new POIFSFileSystem(fIn);
        HSSFWorkbook wb = new HSSFWorkbook(fPoiFS);
        FileOutputStream out = new FileOutputStream(filename);
        wb.write(out);
        return wb;
    }
}
