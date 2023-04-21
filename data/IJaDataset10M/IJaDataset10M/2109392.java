package net.firstpartners.spreadsheet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Convenience Class for Output of Spreadhsheets
 * @author paul
 *
 */
public class SpreadSheetOutputter {

    private static Log log = LogFactory.getLog(SpreadSheetOutputter.class);

    /**
	 * Outputs an Apache POI Workbook to a file
	 * @param wb - Apache POI Workbook (excel)
	 * @param fileName
	 * @throws IOException
	 */
    public static void outputToFile(HSSFWorkbook wb, String fileName) throws IOException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            outputToStream(wb, fileOutputStream);
            fileOutputStream.close();
        } catch (java.security.AccessControlException ace) {
            log.error("Unable to output to file - logging to console instead");
            outputToConsole(wb);
        }
    }

    /**
	 * Outputs an Apache POI Workbook to a Stream (e.g Servlet response)
	 * @param wb
	 * @param stream
	 * @throws IOException
	 */
    public static void outputToStream(HSSFWorkbook wb, OutputStream stream) throws IOException {
        wb.write(stream);
    }

    /**
	 * Outputs an Apache POI Workbook to a Logging Console
	 * @param wb
	 * @throws IOException
	 */
    public static void outputToConsole(HSSFWorkbook wb) throws IOException {
        RangeHolder ranges = RangeConvertor.convertExcelToCells(wb);
        outputToConsole(ranges);
    }

    /**
	 * Outputs Red-Piranha's own internal format to a Logging Console
	 * @param ranges
	 */
    public static void outputToConsole(RangeHolder ranges) {
        for (Range r : ranges) {
            log.info(r);
        }
    }
}
