package org.gbif.portal.web.download;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.io.DataResourceAuditor;
import org.gbif.portal.io.DelimitedResultsOutputter;
import org.gbif.portal.io.OutputProperty;
import org.gbif.portal.io.ResultsOutputter;

/**
 * Runnable that outputs a delimited file.
 * 
 * @author dmartin
 */
public class DelimitedFileWriter extends FileWriter {

    protected static Log logger = LogFactory.getLog(DelimitedFileWriter.class);

    /** The locale to use */
    private Locale locale;

    /** The mappings from field names to Output properties */
    protected Map<String, OutputProperty> downloadFieldMappings;

    /** The host url */
    protected String hostUrl;

    /** Field delimiter */
    protected String delimiter = "\t";

    /** End of record marker */
    protected String endOfRecord = "\n";

    /** The secondary outputs to run */
    protected List<SecondaryOutput> secondaryDownloadOutputs;

    /** The characters to replace */
    protected Map<String, String> replaceChars = new HashMap<String, String>();

    /**
	 * Write out the delimited file.
	 * 
	 * @throws IOException 
	 */
    public void writeFile() throws Exception {
        logger.debug("#######Delimited File Writer starting writeFile...");
        DelimitedFieldFormatter dff = new DelimitedFieldFormatter(downloadFields, messageSource, locale, hostUrl);
        dff.setDelimiter(delimiter);
        dff.setReplaceChars(replaceChars);
        List<String> requestedFieldNames = new ArrayList<String>();
        for (Field field : downloadFields) {
            requestedFieldNames.add(field.getFieldName());
            if (logger.isDebugEnabled()) {
                logger.debug("Requested field = " + field.getFieldName());
            }
        }
        ResultsOutputter resultsOutputter = new DelimitedResultsOutputter(outputStream, downloadFieldMappings, requestedFieldNames, dff, delimiter, endOfRecord);
        if (addCitation && zipped) {
            DataResourceAuditor cro = new DataResourceAuditor();
            cro.setNextResultsOutputter(resultsOutputter);
            resultsOutputter = cro;
        }
        byte[] delimiterInBytes = delimiter.getBytes();
        for (Field field : downloadFields) {
            outputStream.write(messageSource.getMessage(field.getFieldI18nNameKey(), null, field.getFieldI18nNameKey(), locale).getBytes());
            outputStream.write(delimiterInBytes);
        }
        outputStream.write(endOfRecord.getBytes());
        outputProcess.process(resultsOutputter);
        if (zipped) ((ZipOutputStream) outputStream).closeEntry();
        if (addCitation && zipped) {
            downloadUtils.outputCitation(outputStream, (DataResourceAuditor) resultsOutputter, citationFileName, locale, hostUrl);
        }
        if (addRights && zipped) {
            downloadUtils.outputRights(outputStream, (DataResourceAuditor) resultsOutputter, rightsFileName, locale, hostUrl);
        }
        if (logEventId != null && resultsOutputter instanceof DataResourceAuditor) {
            downloadUtils.logDownloadUsage((DataResourceAuditor) resultsOutputter, logEventId);
        }
        if (zipped && secondaryDownloadOutputs != null) {
            downloadUtils.addSecondaryOutputs((ZipOutputStream) outputStream, secondaryDownloadOutputs);
        }
        signalFileWriteComplete();
    }

    /**
	 * @return the downloadFieldMappings
	 */
    public Map<String, OutputProperty> getDownloadFieldMappings() {
        return downloadFieldMappings;
    }

    /**
	 * @param downloadFieldMappings the downloadFieldMappings to set
	 */
    public void setDownloadFieldMappings(Map<String, OutputProperty> downloadFieldMappings) {
        this.downloadFieldMappings = downloadFieldMappings;
    }

    /**
	 * @return the hostUrl
	 */
    public String getHostUrl() {
        return hostUrl;
    }

    /**
	 * @param hostUrl the hostUrl to set
	 */
    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    /**
	 * @return the locale
	 */
    public Locale getLocale() {
        return locale;
    }

    /**
	 * @param locale the locale to set
	 */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
	 * @return the delimiter
	 */
    public String getDelimiter() {
        return delimiter;
    }

    /**
	 * @param delimiter the delimiter to set
	 */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
	 * @return the endOfRecord
	 */
    public String getEndOfRecord() {
        return endOfRecord;
    }

    /**
	 * @param endOfRecord the endOfRecord to set
	 */
    public void setEndOfRecord(String endOfRecord) {
        this.endOfRecord = endOfRecord;
    }

    /**
	 * @return the secondaryDownloadOutputs
	 */
    public List<SecondaryOutput> getSecondaryDownloadOutputs() {
        return secondaryDownloadOutputs;
    }

    /**
	 * @param secondaryDownloadOutputs the secondaryDownloadOutputs to set
	 */
    public void setSecondaryDownloadOutputs(List<SecondaryOutput> secondaryDownloadOutputs) {
        this.secondaryDownloadOutputs = secondaryDownloadOutputs;
    }

    /**
	 * @return the replaceChars
	 */
    public Map<String, String> getReplaceChars() {
        return replaceChars;
    }

    /**
	 * @param replaceChars the replaceChars to set
	 */
    public void setReplaceChars(Map<String, String> replaceChars) {
        this.replaceChars = replaceChars;
    }
}
