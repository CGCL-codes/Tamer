package backend;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * As the reports in text or html cannot be flexible enough for some 
 * investigators this report type has been added. Comma Separated Values can be
 * opened in Spreadsheet software or easily piped to other applications for
 * further processing if Hashtables are not enough. As this report is mainly for
 * further processing the information contained is limited to the findings and
 * other information like investigator and used settings is ommitted - these can
 * be found in the other reports.
 * Values are seperated by Commas, Text is encapsulated in double-qoutes (").
 * 
 * @author Rajmund Witt
 * @version 0.5.02
 * 
 * @see Case
 * @see Configuration
 * @see LogWriter
 * @see ReportWriter
 * @see SystemInformation
 */
public class CsvReportWriter extends ReportWriter {

    /**
	 * Handles the full process of Report creation: creating report directory
	 * if non existing, compiling content of report & saving the report
	 * @return if the process was successful
	 */
    public static boolean createReport() {
        REPORT_FILE_EXTENSION = ".csv";
        isSuccessful_ = true;
        fileName_ = null;
        timeStamp_ = Calendar.getInstance();
        checkDirectory();
        checkFileName();
        compileReport();
        writeReport();
        return isSuccessful_;
    }

    /**
	 * Compiles the content of the report from the information contained in
	 * the Case instance
	 */
    protected static void compileReport() {
        ArrayList<CategorizedImage> indexedImages = case_.getIndexedImages();
        toWrite_ = new StringBuilder();
        toWrite_.append("\"Avg Skin %\",\"Canonical Path\",\"MD5\",\"SHA1\",");
        toWrite_.append("\"Last Modified\",\"RGB DetectorValue\",");
        toWrite_.append("\"YCbCr DetectorValue\"");
        toWrite_.append(currentSystem_.getLineSeparator());
        for (int i = 0; i < indexedImages.size(); i++) {
            toWrite_.append(indexedImages.get(i).getPreciseAveragePercentage());
            toWrite_.append(",\"" + indexedImages.get(i).getCanonicalPath());
            toWrite_.append("\",\"" + indexedImages.get(i).getMd5());
            toWrite_.append("\",\"" + indexedImages.get(i).getSha1());
            toWrite_.append("\"," + indexedImages.get(i).getLastModified());
            toWrite_.append(",");
            toWrite_.append(indexedImages.get(i).getPreciseRgbPercentage());
            toWrite_.append(",");
            toWrite_.append(indexedImages.get(i).getPreciseYCbCrPercentage());
            toWrite_.append(currentSystem_.getLineSeparator());
        }
    }
}
