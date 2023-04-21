package phex.http;

import java.util.*;
import phex.common.Environment;

public class HTTPHeaders {

    public static final HTTPHeader[] EMPTY_HTTPHEADER_ARRAY = new HTTPHeader[0];

    /**
     * A empty HTTPHeaders object. Dont modify this object!
     */
    public static final HTTPHeaders EMPTY_HTTPHEADERS = new EmptyHTTPHeaders();

    public static final String COLON_SEPARATOR = ": ";

    public static final String HEADER_ACCEPT = "Accept";

    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";

    public static final String HEADER_HOST = "Host";

    public static final String HEADER_RANGE = "Range";

    public static final String HEADER_USER_AGENT = "User-Agent";

    public static final String HEADER_SERVER = "Server";

    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";

    public static final String HEADER_CONNECTION = "Connection";

    public static final String HEADER_CONTENT_LENGTH = "Content-Length";

    public static final String HEADER_CONTENT_RANGE = "Content-Range";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    public static final String HEADER_RETRY_AFTER = "Retry-After";

    public static final String HEADER_LISTEN_IP = "Listen-IP";

    public static final String HEADER_VENDOR_MESSAGE = "Vendor-Message";

    public static final String HEADER_X_MY_ADDRESS = "X-My-Address";

    public static final String HEADER_REMOTE_IP = "Remote-IP";

    public static final String HEADER_ALT_LOC = "Alt-Location";

    public static final String HEADER_X_ALT_LOC = "X-Gnutella-Alternate-Location";

    public static final String HEADER_X_AVAILABLE_RANGES = "X-Available-Ranges";

    public static final String HEADER_X_GNUTELLA_CONTENT_URN = "X-Gnutella-Content-URN";

    public static final String HEADER_X_QUEUE = "X-Queue";

    public static final String HEADER_CHAT = "Chat";

    public static final String HEADER_X_ULTRAPEER = "X-Ultrapeer";

    public static final String HEADER_X_ULTRAPEER_NEEDED = "X-Ultrapeer-Needed";

    public static final String HEADER_X_QUERY_ROUTING = "X-Query-Routing";

    public static final String HEADER_X_UP_QUERY_ROUTING = "X-Ultrapeer-Query-Routing";

    public static final String HEADER_GGEP = "GGEP";

    public static final String HEADER_X_TRY = "X-Try";

    public static final String HEADER_X_TRY_ULTRAPEERS = "X-Try-Ultrapeers";

    public static final String HEADER_X_DYNAMIC_QUERY = "X-Dynamic-Querying";

    public static final String HEADER_X_DEGREE = "X-Degree";

    public static final String HEADER_X_MAX_TTL = "X-Max-TTL";

    public static final HTTPHeaders COMMON_HANDSHAKE_HEADERS;

    public static final HTTPHeaders ACCEPT_HANDSHAKE_HEADERS;

    static {
        COMMON_HANDSHAKE_HEADERS = new HTTPHeaders(false);
        COMMON_HANDSHAKE_HEADERS.addHeader(new HTTPHeader(HEADER_USER_AGENT, Environment.getPhexVendor()));
        ACCEPT_HANDSHAKE_HEADERS = new HTTPHeaders(COMMON_HANDSHAKE_HEADERS);
        ACCEPT_HANDSHAKE_HEADERS.addHeader(new HTTPHeader(HEADER_GGEP, "0.5"));
        ACCEPT_HANDSHAKE_HEADERS.addHeader(new HTTPHeader(HEADER_VENDOR_MESSAGE, "0.1"));
    }

    private Map headerFields;

    /**
     * States if the header names are converted to lower case to ensure a lenient
     * header field retrival.
     */
    private boolean lenient;

    /**
     *
     * @param lenient States if the header names are converted to lower case to
     * ensure a lenient header field retrival.
     */
    public HTTPHeaders(boolean lenient) {
        this.lenient = lenient;
        headerFields = new HashMap();
    }

    public HTTPHeaders(HTTPHeaders headers) {
        headerFields = new HashMap(headers.headerFields);
        lenient = headers.lenient;
    }

    public HTTPHeaders(int size, boolean lenient) {
        this.lenient = lenient;
        headerFields = new HashMap(size);
    }

    /**
     * Adds a header field. If lenient is set to true
     * the header name is lower cased for easier and lenient retrival with
     * getHeaderField()
     *
     * @param header the header to add.
     */
    public void addHeader(HTTPHeader header) {
        String name;
        if (lenient) {
            name = header.getName().toLowerCase();
        } else {
            name = header.getName();
        }
        ArrayList values = (ArrayList) headerFields.get(name);
        if (values == null) {
            values = new ArrayList();
            headerFields.put(name, values);
        }
        values.add(header);
    }

    /**
     * Adds a header array. If lenient is set to true
     * the header name is lower cased for easier and lenient retrival with
     * getHeaderField()
     *
     * @param headers the header array to add.
     */
    public void addHeaders(HTTPHeader[] headers) {
        String name;
        ArrayList values;
        for (int i = 0; i < headers.length; i++) {
            if (lenient) {
                name = headers[i].getName().toLowerCase();
            } else {
                name = headers[i].getName();
            }
            values = (ArrayList) headerFields.get(name);
            if (values == null) {
                values = new ArrayList();
                headerFields.put(name, values);
            }
            values.add(headers[i]);
        }
    }

    /**
     * Adds or replaces all given HTTPHeaders in this HTTPHeaders collection.
     * If lenient is set to true the header name is lower cased for easier and
     * lenient retrival with getHeaderField()
     *
     * @param headers the HTTPHeaders to add.
     */
    public void replaceHeaders(HTTPHeaders headers) {
        if (lenient == headers.lenient) {
            headerFields.putAll(headers.headerFields);
        } else {
            Iterator iterator = headers.headerFields.keySet().iterator();
            String key;
            String name;
            ArrayList values;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                values = (ArrayList) headerFields.get(key);
                name = ((HTTPHeader) values.get(0)).getName();
                if (lenient) {
                    name = name.toLowerCase();
                }
                headerFields.put(name, values);
            }
        }
    }

    /**
     * Returns the header field value for the given name. If not available it
     * returns null. If the lenient flag is set the header name is converted to
     * lower case for retrival.
     *
     * @param name the header field name.
     * @return the value of the header field.
     */
    public HTTPHeader getHeader(String name) {
        if (lenient) {
            name = name.toLowerCase();
        }
        ArrayList values = null;
        values = (ArrayList) headerFields.get(name);
        if (values != null) {
            return (HTTPHeader) values.get(0);
        } else {
            return null;
        }
    }

    /**
     * Returns all header field values for the given name. If not available it
     * returns null. If the lenient flag is set the header name is converted to
     * lower case for retrival.
     *
     * @param name the header field name.
     * @return the values of the header field.
     */
    public HTTPHeader[] getHeaders(String name) {
        if (lenient) {
            name = name.toLowerCase();
        }
        ArrayList values = null;
        values = (ArrayList) headerFields.get(name);
        if (values == null) {
            return EMPTY_HTTPHEADER_ARRAY;
        } else {
            HTTPHeader results[] = new HTTPHeader[values.size()];
            return ((HTTPHeader[]) values.toArray(results));
        }
    }

    /**
     * Returns if the header value is equal (ignoring case) with the given
     * compareValue.
     * @param name the name of the header to compare the value from
     * @param compareValue the value to compare the header value with
     * @return true if the header value is equal (ignoring case) with the 
     * compareValue, false otherwise.
     */
    public boolean isHeaderValueEqual(String name, String compareValue) {
        HTTPHeader header = getHeader(name);
        if (header == null) {
            return false;
        }
        String value = header.getValue();
        return compareValue.equalsIgnoreCase(value);
    }

    /**
     * Returns if the header is containing a value
     * (values are separated by ',') that is equal (ignoring case) to
     * compareValue.
     * @param name the name of the header to compare the value from
     * @param compareValue the value to compare the header value with
     * @return true is containing a value
     * (values are separated by ',') that is equal (ignoring case) to
     * compareValue. false otherwise.
     */
    public boolean isHeaderValueContaining(String name, String compareValue) {
        HTTPHeader header = getHeader(name);
        if (header == null) {
            return false;
        }
        String value = header.getValue();
        if (value == null) {
            return false;
        }
        if (compareValue.equalsIgnoreCase(value)) {
            return true;
        }
        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (compareValue.equalsIgnoreCase(token)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the header value in byte if available and parseable, 
     * otherwise defaultValue is returned.
     * @param name the name of the header to look for
     * @param defaultValue the default value to return when the header
     * is not available or can not be parsed.
     * @return the header value in byte if available and parseable, 
     * otherwise defaultValue is returned.
     */
    public byte getByteHeaderValue(String name, byte defaultValue) {
        HTTPHeader header = getHeader(name);
        if (header == null) {
            return defaultValue;
        }
        try {
            byte value = header.byteValue();
            return value;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Returns the header value in int if available and parseable, 
     * otherwise defaultValue is returned.
     * @param name the name of the header to look for
     * @param defaultValue the default value to return when the header
     * is not available or can not be parsed.
     * @return the header value in int if available and parseable, 
     * otherwise defaultValue is returned.
     */
    public int getIntHeaderValue(String name, int defaultValue) {
        HTTPHeader header = getHeader(name);
        if (header == null) {
            return defaultValue;
        }
        try {
            int value = header.intValue();
            return value;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String buildHTTPHeaderString() {
        StringBuffer buffer = new StringBuffer(30 * headerFields.size());
        Iterator iterator = headerFields.keySet().iterator();
        String key;
        ArrayList values;
        Iterator items;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            values = (ArrayList) headerFields.get(key);
            items = values.iterator();
            while (items.hasNext()) {
                HTTPHeader header = (HTTPHeader) items.next();
                buffer.append(header.getName());
                buffer.append(COLON_SEPARATOR);
                buffer.append(header.getValue());
                buffer.append(HTTPRequest.CRLF);
            }
        }
        return buffer.toString();
    }

    private static class EmptyHTTPHeaders extends HTTPHeaders {

        EmptyHTTPHeaders() {
            super(0, false);
        }

        /**
         * Operation not supported
         */
        public void addHeader(HTTPHeader header) {
            throw new UnsupportedOperationException("Modification of empty http headers not allowed");
        }

        /**
         * Operation not supported
         */
        public void addHeaders(HTTPHeader[] headers) {
            throw new UnsupportedOperationException("Modification of empty http headers not allowed");
        }

        /**
         * Always returns null.
         */
        public HTTPHeader getHeader(String name) {
            return null;
        }

        /**
         * Always returns empty http header array.
         */
        public HTTPHeader[] getHeaders(String name) {
            return EMPTY_HTTPHEADER_ARRAY;
        }

        /**
         * Returns a empty string.
         * @return a empty string.
         */
        public String buildHTTPHeaderString() {
            return "";
        }
    }

    public static HTTPHeaders createDefaultRequestHeaders() {
        HTTPHeaders headers = new HTTPHeaders(false);
        headers.addHeader(new HTTPHeader(HEADER_USER_AGENT, Environment.getPhexVendor()));
        return headers;
    }

    public static HTTPHeaders createDefaultResponseHeaders() {
        HTTPHeaders headers = new HTTPHeaders(false);
        headers.addHeader(new HTTPHeader(HEADER_SERVER, Environment.getPhexVendor()));
        return headers;
    }
}
