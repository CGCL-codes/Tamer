package org.xaware.server.engine.instruction.bizcomps.multiformat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This class provides a look ahead feature where the caller may request a number of bytes from the file. These bytes
 * are stored in the lookAheadBuffer. Subsequent lookAhead request will either use the bytes in the lookAhead buffer or
 * perhaps add to the lookAheadBuffer because of a larger byte count request. When an actual read is requested the
 * lookAheadBuffer will be used first to fullfill the request. With the read the lookAheadBuffer will be reduced by the
 * number of bytes in the read.
 * 
 * @author hcurtis
 * 
 */
public class RecordStreamHandler {

    private static final String className = "RecordStreamHandler";

    public static final int FIXED_LENGTH = 0;

    public static final int CUSTOM_DELIMITED_RECORD = 1;

    public static final int NEW_LINE_TERMINATED = 2;

    private static final int BUFFER_INIT_SIZE = 500;

    private static final int MAX_RETRIES = 20;

    private int readMode = FIXED_LENGTH;

    private InputStreamReader input;

    private BufferedReader newLineReader;

    private String encoding = null;

    private char endOfRecord;

    private long recordCount = 0;

    /**
     * Only the delimited read methods will turn this flag off. When set false the lookAhead will read no further. The
     * readData will always turn this to true, setting the condition for another newline or delimited record read.
     */
    private boolean fetchAnotherLine = true;

    /**
     * Indicates the input data source is closed. This occurs after the last readRecord method is completed. The close(0
     * method is public so it is possible for an external shut down of this handler.
     */
    private boolean closed = false;

    /**
     * Indicates the end of file was reached by either the lookAhead or readRecord methods. It indicates that no further
     * processing of the file will take place. The lookAhead method will permit subsequent call while at the end of
     * file, however it will only return the number of bytes already in the lookAheadBuffer
     */
    private boolean eof = false;

    /**
     * Indicates that the delimiter for end of record was detected. This prevents the lookAhead and readRecord methods
     * from reading additional bytes from the input source.
     */
    private boolean detectedDelimiter = false;

    /**
     * The shortBolck signal is set true when the requested number of bytes has exceeded the file limits. ShortBlock is
     * only set true if the read also encountered an EOF condition.
     */
    private boolean shortBlock = false;

    /**
     * The look ahead buffer contains the characters requested by the lookAhead method. It is used by the readRecord
     * method as bytes already read to apply to the requested record.
     */
    private StringBuffer lookAheadBuffer = new StringBuffer(BUFFER_INIT_SIZE);

    private static final XAwareLogger lf = XAwareLogger.getXAwareLogger(RecordStreamHandler.class.getName());

    /**
     * Initialize the input stream handler with setting up the InputStream, optional, encoding, and custom read
     * processes.
     * 
     * @param in -
     *            InputStream that specifies the input source
     * @param recordSeperator -
     *            The xa:record_seperator value
     * @param encoding -
     *            This parameter can be null indicating the xa:record_seperator wasn't detected. Otherwise it should be
     *            that attribute's value
     * @param fileStart -
     *            This is the number of characters to read from the file before actual record processing starts
     * @throws Exception -
     *             thrown when the xa:record_seperator is invalid
     */
    public RecordStreamHandler(final InputStream in, final String recordSeperator, final String encoding, final int fileStart) throws XAwareException {
        final String methodName = "RecordStreamHandler";
        lf.entering(className, methodName);
        this.encoding = encoding;
        if (recordSeperator != null) {
            if (XAwareConstants.BIZCOMPONENT_ATTR_VALUE_NEWLINE.equals(recordSeperator)) {
                readMode = NEW_LINE_TERMINATED;
            } else if (XAwareConstants.BIZCOMPONENT_ATTR_VALUE_NOCOLSEP.equals(recordSeperator)) {
                readMode = FIXED_LENGTH;
            } else {
                readMode = CUSTOM_DELIMITED_RECORD;
                if (recordSeperator.length() > 0) {
                    endOfRecord = recordSeperator.charAt(0);
                    lf.info("Record separator character is: " + Integer.toHexString((int) endOfRecord), className, methodName);
                } else {
                    throw new XAwareException("Invalid record seperator character");
                }
            }
        }
        switch(readMode) {
            case FIXED_LENGTH:
            case CUSTOM_DELIMITED_RECORD:
                if (encoding == null) {
                    input = new InputStreamReader(in);
                } else {
                    try {
                        input = new InputStreamReader(in, encoding);
                    } catch (final UnsupportedEncodingException e) {
                        throw new XAwareException("Invalid Encoding: " + encoding);
                    }
                }
                stepOverFileStart(input, fileStart);
                break;
            case NEW_LINE_TERMINATED:
                input = new InputStreamReader(in);
                newLineReader = new BufferedReader(input);
                stepOverFileStart(newLineReader, fileStart);
                break;
            default:
                throw new XAwareException("Invalid readMode: " + readMode);
        }
        lf.exiting(className, methodName);
    }

    /**
     * Read the specified number of bytes at the begining of the file before processing the file for records
     * 
     * @param input -
     *            InputStreamReader file
     * @param stepOverChars -
     *            Numer of characters to read from the file
     * @throws XAwareException
     *             when EOF encountered before end of record reached
     */
    private void stepOverFileStart(final Reader input, final int stepOverChars) throws XAwareException {
        final String methodName = "stepOverFileStart";
        lf.entering(className, methodName);
        if (stepOverChars > 0) {
            final char cbuf[] = new char[stepOverChars];
            int sts;
            try {
                sts = input.read(cbuf);
            } catch (final IOException e) {
                throw new XAwareException("I/O Exception " + e.getMessage() + " while steping over xa:record_start=\"" + stepOverChars + "\"");
            }
            if (sts < stepOverChars) {
                eof = true;
                close();
                throw new XAwareException("EOF encountered while processing xa:record_start=\"" + stepOverChars + "\"");
            }
        }
        lf.exiting(className, methodName);
    }

    /**
     * This method will read the record's bytes until the delimiter is encountered. It will use the bytes already
     * collected in the look ahead buffer before reading more data from the input source. If more bytes are required
     * (the End of Record flag isn't set) this method will read them from the input source.
     * 
     * Upon return of this method the look ahead buffer will be empty.
     * 
     * @return Returns the next record string or has the EOF set on
     */
    public String readRecordDelimited() throws Exception {
        final String methodName = "readRecordDelimited";
        lf.entering(className, methodName);
        recordCount++;
        fetchAnotherLine = true;
        String ret = null;
        if (closed) {
            lf.severe("Read record #" + recordCount + " request after file closed", className, methodName);
            return "";
        }
        final int currentSize = lookAheadBuffer.length();
        if ((currentSize == 0) && eof) {
            lf.severe("Read record #" + recordCount + " request after EOF signaled", className, methodName);
            return "";
        }
        switch(readMode) {
            case FIXED_LENGTH:
                throw new Exception("Invalid delimited readMode");
            case CUSTOM_DELIMITED_RECORD:
            case NEW_LINE_TERMINATED:
                if (!detectedDelimiter || currentSize == 0) {
                    while (!detectedDelimiter && !eof) {
                        try {
                            lookAheadBuffer.append(readInput(1));
                        } catch (final IOException e) {
                            lf.severe("Input data exception, " + e.getMessage(), className, methodName);
                        } catch (final Exception e) {
                            lf.severe("Process exception, " + e.getMessage(), className, methodName);
                        }
                    }
                }
                ret = lookAheadBuffer.toString();
                lookAheadBuffer = new StringBuffer(BUFFER_INIT_SIZE);
                break;
            default:
                throw new Exception("Invalid readMode: " + readMode);
        }
        if (eof) {
            close();
        }
        detectedDelimiter = false;
        shortBlock = false;
        lf.exiting(className, methodName);
        return ret;
    }

    /**
     * This method will read the record's bytes as specified in the size parameter. It will attempt to use the bytes in
     * the look ahead buffer before reading more data from the input source. The bytes from the look ahead buffer that
     * are applicable to the record will be removed from the look ahead buffer. If more bytes are required this method
     * will read them from the input source.
     * 
     * Upon return of this method the look ahead buffer will either be empty or have bytes that can be applied to the
     * next record.
     * 
     * @param size
     * @return
     */
    public String readRecord(final int size) {
        final String methodName = "readRecord(" + size + ")";
        lf.entering(className, methodName);
        recordCount++;
        lf.fine("Read record #" + recordCount + " from input file", className, methodName);
        String ret = null;
        if (closed) {
            lf.severe("Read record #" + recordCount + " request after file closed", className, methodName);
            return "";
        }
        int currentSize = lookAheadBuffer.length();
        if ((currentSize == 0) && eof) {
            lf.severe("Read record #" + recordCount + " request after EOF signaled", className, methodName);
            return "";
        }
        switch(readMode) {
            case CUSTOM_DELIMITED_RECORD:
                lf.finer("reading CUSTOM_DELIMITED_RECORD");
                while (!detectedDelimiter && !eof) {
                    try {
                        lookAheadBuffer.append(readUntilTheDelimiter(1));
                    } catch (final IOException e) {
                        lf.severe("Encountered exception " + e.getMessage() + " while reading input file", className, methodName);
                    }
                }
                currentSize = lookAheadBuffer.length();
            case NEW_LINE_TERMINATED:
                if (readMode == NEW_LINE_TERMINATED) {
                    lf.finer("reading new line delimited record");
                }
                if (currentSize < size) {
                    lf.warning("Read " + recordCount + " requested size " + size + " is greater than actual record record size " + currentSize, className, methodName);
                } else if (currentSize > size) {
                    lf.warning("Read " + recordCount + " requested size " + size + " is less than actual record record size " + currentSize, className, methodName);
                }
                fetchAnotherLine = true;
                ret = lookAheadBuffer.toString();
                lookAheadBuffer = new StringBuffer(BUFFER_INIT_SIZE);
                if (eof) {
                    close();
                    lf.fine("EOF detected on delimited input file", className, methodName);
                }
                detectedDelimiter = false;
                shortBlock = false;
                lf.finest("Delimited record #" + recordCount + " image: " + ret, className, methodName);
                lf.exiting(className, methodName);
                return ret;
        }
        fetchAnotherLine = true;
        if (currentSize >= size) {
            ret = lookAheadBuffer.substring(0, size);
            if (currentSize == size) {
                lookAheadBuffer = new StringBuffer(BUFFER_INIT_SIZE);
                if (eof) {
                    close();
                    lf.fine("EOF detected on fixed length input file", className, methodName);
                }
            } else {
                lookAheadBuffer.delete(0, size);
            }
            lf.finest("Fixed length record #" + recordCount + " image: [" + ret + "]", className, methodName);
            detectedDelimiter = false;
            shortBlock = false;
            lf.exiting(className, methodName);
            return ret;
        }
        if (detectedDelimiter) {
            lf.severe("Request a record read beyond delimited record boundary", className, methodName);
            ret = lookAheadBuffer.toString();
        } else {
            final int readSize = size - currentSize;
            try {
                lookAheadBuffer.append(readInput(readSize));
            } catch (final IOException e) {
                lf.severe("Input data exception, " + e.getMessage(), className, methodName);
            } catch (final Exception e) {
                lf.severe("Process exception, " + e.getMessage(), className, methodName);
            }
            if (shortBlock) {
                lf.severe("Last record is shorter than expected by " + (size - lookAheadBuffer.length()) + " bytes", className, methodName);
            }
            ret = lookAheadBuffer.toString();
        }
        lookAheadBuffer = new StringBuffer(BUFFER_INIT_SIZE);
        detectedDelimiter = false;
        if (eof) {
            close();
        }
        lf.exiting(RecordStreamHandler.class.getName(), "readRecord");
        return ret;
    }

    /**
     * Provide a look ahead into the next record. These characters will be kept in a look ahead buffer. In the case of
     * records delimited by a character or new_line, the lookAhead method will no go beyond the current record boundary.
     * Fixed length records with abutted records do not support this feature. When the actual reacord read is requested,
     * the look ahead buffer will be provided before reading more characters from the file.
     * 
     * @param size -
     *            the number of characters required for the look a head
     * @return Return the number of characters requested or those available using the record delimiter as a termination
     *         to the string
     */
    public String lookAhead(final int size) {
        final String methodName = "lookAhead(" + size + ")";
        lf.entering(className, methodName);
        if (closed) {
            lf.severe("lookAhead request after file closed", className, methodName);
            lf.exiting(className, methodName);
            return "";
        }
        if (detectedDelimiter || eof) {
            lf.exiting(className, methodName);
            return getRequestedLookAhead(size);
        }
        final int currentSize = lookAheadBuffer.length();
        if (currentSize >= size) {
            lf.exiting(className, methodName);
            return getRequestedLookAhead(size);
        }
        final int readSize = size - currentSize;
        try {
            if (fetchAnotherLine) {
                lookAheadBuffer.append(readInput(readSize));
            }
        } catch (final IOException e) {
            lf.warning("Input data exception, " + e.getMessage(), className, methodName);
        } catch (final Exception e) {
            lf.warning("process exception, " + e.getMessage(), className, methodName);
        }
        if (isEof()) {
            close();
        }
        lf.exiting(className, methodName);
        return getRequestedLookAhead(size);
    }

    /**
     * Select the size of the lookAheadBuffer that should be returned to satisfy the lookAhead request
     * 
     * @param size -
     *            requested characters
     * @return Returns The requested portion of the lookAhead buffer
     */
    private String getRequestedLookAhead(final int size) {
        final String methodName = "getRequestedLookAhead(" + size + ")";
        lf.entering(className, methodName);
        String retStr = null;
        if (lookAheadBuffer.length() > size) {
            retStr = lookAheadBuffer.substring(0, size);
        } else {
            retStr = lookAheadBuffer.toString();
        }
        lf.exiting(className, methodName);
        return retStr;
    }

    /**
     * Switch to direct the reading of the next set of bytes to the appropriate reader.
     * 
     * @param size -
     *            the number of bytes requested
     * @return
     * @throws Exception
     * @throws IOException
     */
    private String readInput(final int size) throws Exception, IOException {
        final String methodName = "readInput(" + ")";
        lf.entering(className, methodName);
        String ret = null;
        if (eof) {
            throw new Exception("EOF has been reached");
        }
        switch(readMode) {
            case FIXED_LENGTH:
                ret = readFixedSize(size);
                break;
            case NEW_LINE_TERMINATED:
                ret = readAnotherLine(0);
                break;
            case CUSTOM_DELIMITED_RECORD:
                ret = readUntilTheDelimiter(size);
                break;
            default:
                throw new Exception("Invalid readMode: " + readMode);
        }
        lf.exiting(className, methodName);
        return ret;
    }

    /**
     * External closing of the input data source. This will shut down any further processing of the input data
     */
    public void close() {
        final String methodName = "close";
        lf.entering(className, methodName);
        if (!closed) {
            try {
                switch(readMode) {
                    case FIXED_LENGTH:
                    case CUSTOM_DELIMITED_RECORD:
                        input.close();
                        lf.fine("Closing " + (readMode == FIXED_LENGTH ? "fixed length" : "custom delimited") + " input file", className, methodName);
                        break;
                    case NEW_LINE_TERMINATED:
                        newLineReader.close();
                        lf.fine("Closing new line terminated input file", className, methodName);
                        break;
                }
            } catch (final IOException e) {
                lf.warning("Failed to close input data source", className, methodName);
            }
            closed = true;
        }
        lf.exiting(className, methodName);
    }

    /**
     * Read from the file the specified number of bytes. Since this reader assumes the file is a fixed length format
     * there is no check for a delimiter. A short block could be set erroneously because of a look ahead beyond the
     * actual record.
     * 
     * @param size
     * @return - Returns the number of bytes requested or less if the read was not fully successful
     * @throws IOException
     */
    private String readFixedSize(final int size) throws IOException {
        final String methodName = "readFixedSize(" + size + ")";
        lf.entering(className, methodName);
        final char buf[] = new char[size];
        final int ret = input.read(buf, 0, size);
        if (ret == -1) {
            eof = true;
            lf.finest("Returned no characters: [] EOF=" + eof, className, methodName);
            lf.exiting(className, methodName);
            return new String();
        } else if (ret < size) {
            eof = true;
            shortBlock = true;
        }
        final String retBuf = new String(buf, 0, ret);
        lf.finest("Returned characters: [" + retBuf + "] EOF=" + eof, className, methodName);
        lf.exiting(className, methodName);
        return retBuf;
    }

    /**
     * Read a line terminated by a new line
     * 
     * @return Return the entire read line with the new line character removed. In the case of EOF return an empty
     *         string with the EOF set true
     * @param depth -
     *            Indicates the number of nested call to find a new line with data
     * @throws IOException
     */
    private String readAnotherLine(final int depth) throws IOException {
        final String methodName = "readAnotherLine(" + depth + ")";
        lf.entering(className, methodName);
        if (depth > MAX_RETRIES) {
            lf.exiting(className, methodName);
            return new String();
        }
        String buf = newLineReader.readLine();
        detectedDelimiter = true;
        if (buf == null) {
            eof = true;
            lf.finest("Returned no characters: [] EOF=" + eof, className, methodName);
            lf.exiting(className, methodName);
            return new String();
        }
        if (buf.length() == 0) {
            buf = readAnotherLine(depth + 1);
        }
        fetchAnotherLine = !(buf.length() > 0);
        if (encoding == null) {
            lf.finest("Returned characters: [" + buf + "] EOF=" + eof, className, methodName);
            lf.exiting(className, methodName);
            return buf;
        }
        final String encodedBuf = new String(buf.getBytes(), encoding);
        lf.finest("Returned characters: [" + encodedBuf + "] EOF=" + eof, className, methodName);
        lf.exiting(className, methodName);
        return encodedBuf;
    }

    /**
     * Read the specified number of bytes or less if the record delimiter is encountered.
     * 
     * @param size -
     *            Number of bytes requested.
     * @return
     * @throws IOException
     */
    private String readUntilTheDelimiter(final int size) throws IOException {
        final String methodName = "readUntilTheDelimiter(" + size + ")";
        int c = -1;
        int count = 0;
        lf.entering(className, methodName);
        if (eof) {
            lf.warning("Input file EOF already detected", className, methodName);
            lf.exiting(className, methodName);
            return "";
        }
        final StringBuffer rec = new StringBuffer(size);
        if (!detectedDelimiter) {
            do {
                c = input.read();
                if (c == -1) {
                    eof = true;
                    fetchAnotherLine = false;
                    if (rec.length() > 0) {
                        lf.warning("Record " + (recordCount + 1) + " is not terminated with a delimiter character, EOF detected", className, methodName);
                        lf.finest("Returned characters: [" + rec.toString() + "]", className, methodName);
                    }
                    lf.exiting(className, methodName);
                    return rec.toString();
                }
                detectedDelimiter = ((char) c == endOfRecord);
                if (!detectedDelimiter) {
                    rec.append((char) c);
                    count++;
                } else if ((lookAheadBuffer.length() == 0) && (rec.length() == 0)) {
                    detectedDelimiter = false;
                }
            } while ((count < size) && !detectedDelimiter);
        }
        fetchAnotherLine = !detectedDelimiter;
        lf.finest("Returned characters: [" + rec.toString() + "] EOF=" + eof, className, methodName);
        lf.exiting(className, methodName);
        return rec.toString();
    }

    /**
     * The End of File indicator is set true when the last byte of the input data source is read, otherwise this
     * indicator is false
     * 
     * @return Returns the eof.
     */
    public boolean isEof() {
        return eof && (lookAheadBuffer.length() == 0);
    }

    /**
     * The shortBlock is set true when the number of bytes requested can not be supplied because the end of file is
     * reached.
     * 
     * @return Returns the shortBlock.
     */
    public boolean isShortBlock() {
        return shortBlock;
    }

    /**
     * @return Returns the closed.
     */
    public boolean isClosed() {
        return closed;
    }

    public String getDelimiterCharacter() {
        final StringBuffer desc = new StringBuffer(10);
        switch(readMode) {
            case FIXED_LENGTH:
                desc.append("none(fixed length)");
                break;
            case CUSTOM_DELIMITED_RECORD:
                switch(endOfRecord) {
                    case '\t':
                        desc.append("\\t");
                        break;
                    default:
                        desc.append("0x").append(Integer.toHexString((int) endOfRecord));
                }
                break;
            case NEW_LINE_TERMINATED:
                desc.append("new-line");
                break;
            default:
                desc.append("unknown delimiter mode ").append(readMode);
        }
        return desc.toString();
    }

    public String getFileDelimiterMode() {
        final StringBuffer desc = new StringBuffer("Each Record is ");
        switch(readMode) {
            case FIXED_LENGTH:
                desc.append("a specified fixed length");
                break;
            case CUSTOM_DELIMITED_RECORD:
                desc.append("delimited by a ");
                switch(endOfRecord) {
                    case '\t':
                        desc.append("\\t");
                        break;
                    default:
                        desc.append("0x").append(Integer.toHexString((int) endOfRecord));
                }
                desc.append(" character");
                break;
            case NEW_LINE_TERMINATED:
                desc.append("delimited by a new-line character");
                break;
            default:
                desc.append("unknown delimiter mode ").append(readMode);
        }
        return desc.toString();
    }

    /**
     * @return Returns the readMode.
     */
    public int getReadMode() {
        return readMode;
    }

    public String getCurrentInputData() {
        return lookAheadBuffer.toString();
    }

    /**
     * @return Returns the recordCount.
     */
    public long getRecordCount() {
        return recordCount;
    }
}
