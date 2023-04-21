package gate.cloud.io.arc;

import static gate.cloud.io.IOConstants.*;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;
import org.archive.io.arc.ARCReader;
import org.archive.io.arc.ARCReaderFactory;
import org.archive.io.arc.ARCRecord;
import org.archive.io.arc.ARCRecordMetaData;
import org.archive.util.ArchiveUtils;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.cloud.batch.PooledDocumentProcessor;
import gate.cloud.io.InputHandler;
import gate.cloud.util.ByteArrayURLStreamHandler;
import gate.util.GateException;

/**
 * An input handler that reads from an ARC file.
 */
public class ARCInputHandler implements InputHandler {

    private static final Logger logger = Logger.getLogger(ARCInputHandler.class);

    private static final String ARC_HEADER_PREFIX = "arc_header_";

    private static final String HTTP_HEADER_PREFIX = "http_header_";

    private static final String HTTP_CONTENT_TYPE_HEADER_NAME = "Content-Type";

    /**
   * The ARC file we are loading documents from.
   */
    private File arcFile;

    /**
   * The directory containing the batch specification file, or
   * <code>null</code> if the batch specification did not come from a
   * file.
   */
    protected File batchDir;

    /**
   * The encoding to assume for ARC records that don't specify
   * one in their Content-Type header.  Windows-1252 is used if
   * not specified in the batch file.
   */
    protected String defaultEncoding;

    /**
   * Optional mime type override.  If specified, all entries to
   * be processed will be parsed by GATE using this mime type.
   * By default, we respect the mime type given in the ARC
   * metadata for each entry.
   */
    protected String mimeType;

    /**
   * Pool of archive readers.
   */
    protected BlockingQueue<ARCReader> readers;

    /**
   * The index from archive record number to offset.
   */
    protected long[] index;

    /**
   * Regular expression pattern matching any sequence of digits
   * at the start of a string.
   */
    protected static final Pattern STARTING_DIGITS_PATTERN = Pattern.compile("^(\\d+)");

    /**
   * Regular expression pattern matching the "charset" from an HTTP
   * Content-type header.
   */
    protected static final Pattern CHARSET_PATTERN = Pattern.compile("charset=(\\S*)");

    public void config(Map<String, String> configData) throws IOException, GateException {
        String arcFileStr = configData.get(PARAM_ARC_FILE_LOCATION);
        if (arcFileStr == null || arcFileStr.trim().length() == 0) {
            throw new IllegalArgumentException("No value was provided for the required parameter \"" + PARAM_ARC_FILE_LOCATION + "\"!");
        }
        String batchFileStr = configData.get(PARAM_BATCH_FILE_LOCATION);
        if (batchFileStr != null) {
            batchDir = new File(batchFileStr).getParentFile();
        }
        arcFile = new File(arcFileStr);
        if (!arcFile.isAbsolute()) {
            arcFile = new File(batchDir, arcFileStr);
        }
        if (!arcFile.exists()) {
            throw new IllegalArgumentException("File \"" + arcFile + "\", provided as value for required parameter \"" + PARAM_ARC_FILE_LOCATION + "\", does not exist!");
        }
        if (!arcFile.isFile()) {
            throw new IllegalArgumentException("File \"" + arcFile + "\", provided as value for required parameter \"" + PARAM_ARC_FILE_LOCATION + "\", is not a file!");
        }
        defaultEncoding = configData.get(PARAM_DEFAULT_ENCODING);
        if (defaultEncoding == null) {
            defaultEncoding = "Windows-1252";
        }
        mimeType = configData.get(PARAM_MIME_TYPE);
    }

    public void init() throws IOException, GateException {
        logger.info("Building index for ARC file " + arcFile.getAbsolutePath() + " - this may take some time...");
        int recordNum = 0;
        ARCReader reader = ARCReaderFactory.get(arcFile);
        LongArrayList indexList = new LongArrayList();
        try {
            Iterator<ArchiveRecord> iter = reader.iterator();
            while (iter.hasNext()) {
                ARCRecord record = (ARCRecord) iter.next();
                indexList.add(record.getMetaData().getOffset());
                if (recordNum++ % 1000 == 0) {
                    logger.debug("Processed " + (recordNum - 1) + " records");
                }
            }
        } finally {
            reader.close();
        }
        indexList.trim();
        index = indexList.elements();
        logger.info("Finished indexing ARC file " + arcFile.getAbsolutePath() + ", " + index.length + " records total.");
        readers = new LinkedBlockingQueue<ARCReader>();
    }

    public Document getInputDocument(String id) throws IOException, GateException {
        ARCReader reader = borrowReader();
        try {
            Matcher idMatcher = STARTING_DIGITS_PATTERN.matcher(id);
            if (!idMatcher.find()) {
                throw new GateException("Document IDs within an ARC must start " + "with one or more digits, \"" + id + "\" does not.");
            }
            int docIndex = Integer.parseInt(idMatcher.group(1));
            if (docIndex >= index.length) {
                throw new GateException("Input document at index " + docIndex + " requested, " + "but this ARC file only contains " + index.length + " entries.");
            }
            ARCRecord record = (ARCRecord) reader.get(index[docIndex]);
            ARCRecordMetaData header = record.getMetaData();
            long recordContentBegin = header.getContentBegin();
            record.skip(recordContentBegin);
            long recordBodySize = record.available();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            record.dump(baos);
            byte[] content = baos.toByteArray();
            String encoding = null;
            Header[] httpHeaders = record.getHttpHeaders();
            Pattern charsetPattern = Pattern.compile("charset=(\\S*)");
            for (Header aHeader : httpHeaders) {
                if (aHeader.getName().equalsIgnoreCase(HTTP_CONTENT_TYPE_HEADER_NAME)) {
                    Matcher m = charsetPattern.matcher(aHeader.getValue());
                    if (m.find()) {
                        encoding = m.group(1);
                        break;
                    }
                }
            }
            if (encoding == null) encoding = defaultEncoding;
            URL docUrl = new URL(null, header.getUrl(), new ByteArrayURLStreamHandler(content));
            FeatureMap docParams = Factory.newFeatureMap();
            docParams.put(Document.DOCUMENT_URL_PARAMETER_NAME, docUrl);
            if (encoding != null && encoding.length() > 0) {
                docParams.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, encoding);
            }
            docParams.put(Document.DOCUMENT_MARKUP_AWARE_PARAMETER_NAME, Boolean.TRUE);
            if (mimeType != null) {
                docParams.put(Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, mimeType);
            } else if (header.getMimetype() != null && header.getMimetype().length() > 0) {
                docParams.put(Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, header.getMimetype());
            }
            FeatureMap docFeatures = Factory.newFeatureMap();
            Object redirect = header.getHeaderValue("location");
            if (redirect != null) {
                docFeatures.put("redirect_to", redirect.toString());
            }
            docFeatures.put("archive_name", arcFile.getName());
            docFeatures.put("archive_position", idMatcher.group(1));
            Date documentDate = getDate(header, record);
            if (documentDate != null) {
                docFeatures.put("retrievedAt", documentDate);
            }
            docFeatures.put("original_size", Long.toString(recordBodySize));
            docFeatures.put(PooledDocumentProcessor.FILE_SIZE_FEATURE, Long.valueOf(content.length));
            Iterator<?> headerKeyIter = header.getHeaderFieldKeys().iterator();
            String headerKey, headerValueString;
            Object headerValue;
            while (headerKeyIter.hasNext()) {
                headerKey = headerKeyIter.next().toString();
                headerValue = header.getHeaderValue(headerKey);
                if (headerValue != null) {
                    headerValueString = headerValue.toString();
                } else {
                    headerValueString = "_null_";
                }
                docFeatures.put(ARC_HEADER_PREFIX + headerKey, headerValueString);
            }
            Header[] httpHeader = record.getHttpHeaders();
            if (httpHeader != null) {
                for (Header h : httpHeader) {
                    headerKey = h.getName();
                    headerValueString = h.getValue();
                    docFeatures.put(HTTP_HEADER_PREFIX + headerKey, headerValueString);
                }
            }
            return (Document) Factory.createResource("gate.corpora.DocumentImpl", docParams, docFeatures, id);
        } finally {
            releaseReader(reader);
        }
    }

    /**
   * Use the HTTP Date header if present, otherwise the archival date.
   * 
   * @return null if no date is found.
   */
    private Date getDate(ArchiveRecordHeader header, ARCRecord record) {
        String dateString = null;
        Header[] httpHeader = record.getHttpHeaders();
        if (httpHeader != null) {
            for (Header h : httpHeader) {
                if (h.getName().equalsIgnoreCase("Date")) {
                    dateString = h.getValue();
                    break;
                }
            }
        }
        if (dateString != null) {
            try {
                return DateUtil.parseDate(dateString);
            } catch (DateParseException e) {
            }
        }
        dateString = header.getDate();
        try {
            return ArchiveUtils.parse14DigitDate(dateString);
        } catch (ParseException e) {
        }
        return null;
    }

    /**
   * Take a spare ARCReader from the pool, or create a new one if there are
   * no spares available.
   */
    protected ARCReader borrowReader() throws IOException {
        ARCReader reader = readers.poll();
        if (reader == null) {
            synchronized (this) {
                reader = ARCReaderFactory.get(arcFile);
                reader.setParseHttpHeaders(true);
            }
        }
        return reader;
    }

    /**
   * Return an ARCReader to the pool.
   */
    protected void releaseReader(ARCReader reader) {
        readers.add(reader);
    }

    /**
   * Close all the ARCReaders in the pool.
   */
    public void close() throws IOException, GateException {
        for (ARCReader r : readers) {
            try {
                r.close();
            } catch (Exception e) {
                logger.warn("Exception while closing ARC reader", e);
            }
        }
    }
}
