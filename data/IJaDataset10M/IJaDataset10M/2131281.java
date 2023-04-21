package org.ardverk.daap.bio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EncodingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class for parsing http header values according to RFC-2616 Section
 * 4 and 19.3.
 * 
 * @author Michael Becke
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 * 
 * @since 2.0beta1
 */
public class HttpParser {

    /** Log object for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(HttpParser.class);

    /**
     * Constructor for HttpParser.
     */
    private HttpParser() {
    }

    /**
     * Return byte array from an (unchunked) input stream. Stop reading when
     * <tt>"\n"</tt> terminator encountered If the stream ends before the line
     * terminator is found, the last part of the string will still be returned.
     * If no input data available, <code>null</code> is returned.
     * 
     * @param inputStream
     *            the stream to read from
     * 
     * @throws IOException
     *             if an I/O problem occurs
     * @return a byte array from the stream
     */
    public static byte[] readRawLine(InputStream inputStream) throws IOException {
        LOG.trace("enter HttpParser.readRawLine()");
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int ch;
        while ((ch = inputStream.read()) >= 0) {
            buf.write(ch);
            if (ch == '\n') {
                break;
            }
        }
        if (buf.size() == 0) {
            return null;
        }
        return buf.toByteArray();
    }

    /**
     * Read up to <tt>"\n"</tt> from an (unchunked) input stream. If the stream
     * ends before the line terminator is found, the last part of the string
     * will still be returned. If no input data available, <code>null</code> is
     * returned.
     * 
     * @param inputStream
     *            the stream to read from
     * @param charset
     *            charset of HTTP protocol elements
     * 
     * @throws IOException
     *             if an I/O problem occurs
     * @return a line from the stream
     * 
     * @since 3.0
     */
    public static String readLine(InputStream inputStream, String charset) throws IOException {
        LOG.trace("enter HttpParser.readLine(InputStream, String)");
        byte[] rawdata = readRawLine(inputStream);
        if (rawdata == null) {
            return null;
        }
        int len = rawdata.length;
        int offset = 0;
        if (len > 0) {
            if (rawdata[len - 1] == '\n') {
                offset++;
                if (len > 1) {
                    if (rawdata[len - 2] == '\r') {
                        offset++;
                    }
                }
            }
        }
        final String result = EncodingUtils.getString(rawdata, 0, len - offset, charset);
        return result;
    }

    /**
     * Read up to <tt>"\n"</tt> from an (unchunked) input stream. If the stream
     * ends before the line terminator is found, the last part of the string
     * will still be returned. If no input data available, <code>null</code> is
     * returned
     * 
     * @param inputStream
     *            the stream to read from
     * 
     * @throws IOException
     *             if an I/O problem occurs
     * @return a line from the stream
     * 
     * @deprecated use #readLine(InputStream, String)
     */
    public static String readLine(InputStream inputStream) throws IOException {
        LOG.trace("enter HttpParser.readLine(InputStream)");
        return readLine(inputStream, "US-ASCII");
    }

    /**
     * Parses headers from the given stream. Headers with the same name are not
     * combined.
     * 
     * @param is
     *            the stream to read headers from
     * @param charset
     *            the charset to use for reading the data
     * 
     * @return an array of headers in the order in which they were parsed
     * 
     * @throws IOException
     *             if an IO error occurs while reading from the stream
     * @throws HttpException
     *             if there is an error parsing a header value
     * 
     * @since 3.0
     */
    public static Header[] parseHeaders(InputStream is, String charset) throws IOException, HttpException {
        LOG.trace("enter HeaderParser.parseHeaders(InputStream, String)");
        List<Header> headers = new ArrayList<Header>();
        String name = null;
        StringBuffer value = null;
        for (; ; ) {
            String line = HttpParser.readLine(is, charset);
            if ((line == null) || (line.trim().length() < 1)) {
                break;
            }
            if ((line.charAt(0) == ' ') || (line.charAt(0) == '\t')) {
                if (value != null) {
                    value.append(' ');
                    value.append(line.trim());
                }
            } else {
                if (name != null) {
                    headers.add(new BasicHeader(name, value.toString()));
                }
                int colon = line.indexOf(":");
                if (colon < 0) {
                    throw new ProtocolException("Unable to parse header: " + line);
                }
                name = line.substring(0, colon).trim();
                value = new StringBuffer(line.substring(colon + 1).trim());
            }
        }
        if (name != null) {
            headers.add(new BasicHeader(name, value.toString()));
        }
        return (Header[]) headers.toArray(new Header[headers.size()]);
    }

    /**
     * Parses headers from the given stream. Headers with the same name are not
     * combined.
     * 
     * @param is
     *            the stream to read headers from
     * 
     * @return an array of headers in the order in which they were parsed
     * 
     * @throws IOException
     *             if an IO error occurs while reading from the stream
     * @throws HttpException
     *             if there is an error parsing a header value
     * 
     * @deprecated use #parseHeaders(InputStream, String)
     */
    public static Header[] parseHeaders(InputStream is) throws IOException, HttpException {
        LOG.trace("enter HeaderParser.parseHeaders(InputStream, String)");
        return parseHeaders(is, "US-ASCII");
    }
}
