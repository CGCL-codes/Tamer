package net.sourceforge.processdash.util;

import java.io.IOException;
import java.net.URLConnection;

public class HTTPUtils {

    public static final String DEFAULT_CHARSET = "iso-8859-1";

    /** Extract the charset from a mime content type
     */
    public static String getCharset(String contentType) {
        String upType = contentType.toUpperCase();
        int pos = upType.indexOf("CHARSET=");
        if (pos == -1) return DEFAULT_CHARSET;
        int beg = pos + 8;
        return contentType.substring(beg);
    }

    /** Change the charset in a mime content type string; return the new
     * content type string
     */
    public static String setCharset(String contentType, String newCharset) {
        int pos = contentType.toLowerCase().indexOf("charset=");
        if (pos == -1) return contentType + "; charset=" + newCharset; else return contentType.substring(0, pos + 8) + newCharset;
    }

    /** Extract the content type from the given HTTP response headers.
     */
    public static String getContentType(String header) {
        String upHeader = CRLF + header.toUpperCase();
        int pos = upHeader.indexOf(CRLF + "CONTENT-TYPE:");
        if (pos == -1) return null;
        int beg = pos + 15;
        int end = upHeader.indexOf(CRLF, beg);
        if (end == -1) end = upHeader.length();
        return header.substring(beg - 2, end - 2).trim();
    }

    /** Determine the length (in bytes) of the header in an HTTP response.
     */
    public static int getHeaderLength(byte[] result) {
        int i = 0;
        int max = result.length - 3;
        do {
            if ((result[i] == '\r') && (result[i + 1] == '\n') && (result[i + 2] == '\r') && (result[i + 3] == '\n')) return (i + 4);
            i++;
        } while (i < max);
        return result.length;
    }

    /** Read the response from a connection and interpret it as a String,
     * using the content type charset from the response headers.
     */
    public static String getResponseAsString(URLConnection conn) throws IOException {
        String contentType = conn.getContentType();
        if (contentType == null) throw new IOException("No reponse, or no Content-Type");
        String charset = getCharset(contentType);
        byte[] rawData = FileUtils.slurpContents(conn.getInputStream(), true);
        return new String(rawData, charset);
    }

    protected static final String CRLF = "\r\n";
}
