package test.xlsx;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.sf.excompcel.spreadsheet.impl.poixssf.BasePoixECTest;
import org.apache.log4j.Logger;
import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.extractor.XSSFExportToXml;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

/**
 * 
 * @author Detlev Struebig
 *
 */
public class ExtractXml {

    /** Logger. */
    private static Logger log = Logger.getLogger(BasePoixECTest.class);

    /** XLS File with Test Data */
    protected static String pathTestDataMasterXlsx = "app/configtest/testdata/xlsWithData1.xlsx";

    @Test
    public void testExtractXml() throws Exception {
        FileInputStream in = new FileInputStream(pathTestDataMasterXlsx);
        POITextExtractor ex = ExtractorFactory.createExtractor(in);
        log.info(ex.getText());
    }

    private String extractToXml(InputStream in) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook(in);
        XSSFExportToXml ex = null;
        return "";
    }

    public String extractText(InputStream in) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook(in);
        XSSFExcelExtractor ex = new XSSFExcelExtractor(wb);
        String text = ex.getText();
        return text;
    }
}
