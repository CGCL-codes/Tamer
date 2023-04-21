package org.moviereport.core.export.pdf;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.moviereport.core.export.pdf.reports.DVDCover2Sided;
import org.moviereport.core.export.pdf.reports.FloatingTableReport;
import org.moviereport.core.export.pdf.reports.MovieReport;
import org.moviereport.core.export.pdf.reports.MovieReportInfo;
import org.moviereport.core.model.MovieCollection;

public class MovieDescriptionToPdf {

    private static Logger logger = Logger.getLogger(MovieDescriptionToPdf.class.getName());

    private final List<MovieReport> movieReports = new ArrayList<MovieReport>();

    public MovieDescriptionToPdf() {
        movieReports.add(new DVDCover2Sided("Jewelcase 130mm x 185mm x 20mm", "DVD Cover for 2 DVDs, normal", 365f, 30f));
        movieReports.add(new DVDCover2Sided("Jewelcase 130mm x 185mm x 10mm", "DVD Cover for 2 DVDs, thin", 365f, 30f));
        movieReports.add(new FloatingTableReport());
    }

    public List<MovieReportInfo> getAllMovieReportInfos() {
        List<MovieReportInfo> list = new ArrayList<MovieReportInfo>();
        for (MovieReport movieReport : movieReports) {
            list.add(movieReport);
        }
        return list;
    }

    public InputStream exportPdf(MovieCollection movieCollection, ExportPdfConfiguration exportConfiguration) throws MDFToPdfException {
        int index = movieReports.indexOf(exportConfiguration.getMovieReportInfo());
        if (index != -1) {
            MovieReport movieReport = movieReports.get(index);
            logger.log(Level.INFO, "Creating report '" + exportConfiguration.getMovieReportInfo().getReportName());
            return movieReport.createReport(movieCollection, exportConfiguration);
        }
        return null;
    }

    /**
	 * Business Exception that occurs in the useCase MovieDescriptionToPdf
	 * 
	 * @author mblock
	 * 
	 */
    public static class MDFToPdfException extends Exception {

        public static enum ERROR_TYPE {

            UNDEFINED, ERROR_WRITING_PDF_FILE
        }

        private ERROR_TYPE type;

        /**
		 * Constructor
		 */
        public MDFToPdfException(ERROR_TYPE type) {
            super();
            this.type = type;
        }

        /**
		 * Constructor
		 */
        public MDFToPdfException(ERROR_TYPE type, String msg) {
            super(msg);
            this.type = type;
        }

        /**
		 * Constructor
		 * 
		 * @param cause
		 *            the original exception causing this exception
		 */
        public MDFToPdfException(ERROR_TYPE type, Throwable cause) {
            super(cause);
            this.type = type;
        }

        /**
		 * Constructor
		 * 
		 * @param msg
		 * @param cause
		 */
        public MDFToPdfException(ERROR_TYPE type, String msg, Throwable cause) {
            super(msg, cause);
            this.type = type;
        }

        public ERROR_TYPE getType() {
            return type;
        }
    }
}
