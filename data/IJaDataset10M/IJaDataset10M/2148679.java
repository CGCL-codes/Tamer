package org.gbif.portal.web.download.log;

import java.util.zip.ZipOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.io.DataResourceAuditor;
import org.gbif.portal.io.LogStatAuditor;
import org.gbif.portal.io.ResultsOutputter;
import org.gbif.portal.io.VelocityResultsOutputter;
import org.gbif.portal.web.controller.dataset.LogQuery;
import org.gbif.portal.web.download.FieldFormatter;
import org.gbif.portal.web.download.VelocityFileWriter;

/**
 * Writes a file out using velocity.
 * 
 * @author dmartin
 */
public class LogFileWriter extends VelocityFileWriter {

    protected static Log logger = LogFactory.getLog(LogFileWriter.class);

    /**
	 * @see org.gbif.portal.web.download.FileWriter#writeFile()
	 */
    public void writeFile() throws Exception {
        writeTemplate(headerTemplatePath, outputStream);
        FieldFormatter ff = new FieldFormatter(downloadFields, messageSource, locale, hostUrl);
        ResultsOutputter resultsOutputter = new VelocityResultsOutputter(outputStream, templatePath, ff);
        LogStatAuditor lsa = new LogStatAuditor();
        DataResourceAuditor dra = new DataResourceAuditor();
        lsa.setNextResultsOutputter(dra);
        dra.setNextResultsOutputter(resultsOutputter);
        resultsOutputter = lsa;
        outputProcess.process(resultsOutputter);
        writeTemplate(footerTemplatePath, outputStream);
        if (zipped) {
            ((ZipOutputStream) outputStream).closeEntry();
        }
        if (logEventId != null && resultsOutputter instanceof DataResourceAuditor) {
            downloadUtils.logDownloadUsage(dra, logEventId);
        }
        if (outputProcess instanceof LogQuery) {
            LogQuery lq = (LogQuery) outputProcess;
            lq.outputLogStats(outputStream, lsa.getLogStats());
        }
        signalFileWriteComplete();
    }
}
