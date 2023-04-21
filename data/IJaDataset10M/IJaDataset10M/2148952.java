package rjws.server.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import rjws.exception.RJWSException;
import rjws.server.RJWSDispatcher;

public class HTTPRequest {

    /**
	 * Upper-Case String-definitions
	 */
    private static final String METHOD_GET = "GET";

    private static final String METHOD_POST = "POST";

    private static final String METHOD_HEAD = "HEAD";

    private static final String PROPERTY_HOST = "HOST:";

    private static final String PROPERTY_USERAGENT = "USER-AGENT:";

    private static final String PROPERTY_CONNECTION = "CONNECTION:";

    private static final String PROPERTY_CONNECTION_KEEPALIVE = "KEEP-ALIVE";

    private static final String PROPERTY_CONNECTION_CLOSE = "CLOSE";

    private static final String PROPERTY_CONTENT_TYPE = "CONTENT-TYPE:";

    private static final String PROPERTY_CONTENT_LENGTH = "CONTENT-LENGTH:";

    private static final String PROPERTY_REFERER = "REFERER:";

    /**
	 * Reads the Post-Data from the Reader
	 * 
	 * @param reader
	 * @param postLength
	 * @param request
	 * @throws IOException
	 */
    private static void parsePOST(BufferedReader reader, int postLength, HTTPRequest request) throws IOException {
        char[] buffer = new char[postLength];
        int count = reader.read(buffer);
        if (count != postLength) throw new RJWSException("Length mismatch: " + count + "/" + postLength);
        request.postContent = new String(buffer);
        if (request.isMultipart()) {
        } else {
            parseVariables(new String(buffer), request.postVariables);
        }
    }

    /**
	 * Parses the Path-Parts (GET-Variables)
	 * @param line
	 * @return
	 */
    private static void parseGET(String line, HTTPRequest request) {
        String[] parts = line.split("[ ]");
        if (parts.length < 2) throw new RJWSException("Part-mismatch");
        String path = parts[1];
        String[] pathParts = path.split("[?]");
        if (pathParts.length < 1) throw new RJWSException("Pathpart-mismatch");
        request.path = pathParts[0];
        if (pathParts.length >= 2) {
            String variables = pathParts[1];
            parseVariables(variables, request.getVariables);
        }
    }

    /**
	 * Parses the Variables in a String
	 * 
	 * @param buffer
	 * @param map
	 */
    private static void parseVariables(String buffer, Map<String, String> map) {
        String[] variableParts = buffer.split("[&]");
        for (String valueKeySet : variableParts) {
            if (valueKeySet.contains("=")) {
                String[] valueKeyParts = valueKeySet.split("[=]");
                if (valueKeyParts.length != 2) throw new RJWSException("Field-assignment wrong: " + valueKeySet);
                String key = valueKeyParts[0];
                String value = valueKeyParts[1];
                map.put(key, value);
            } else {
                map.put(valueKeySet, null);
            }
        }
    }

    /**
	 * Parses the Request-Data and returns a Request-Object
	 * @param data
	 * @return
	 * @throws IOException 
	 */
    public static HTTPRequest parse(BufferedReader reader, RJWSDispatcher dispatcher) throws IOException {
        Vector<String> lines = new Vector<String>();
        String inputLine;
        do {
            inputLine = reader.readLine();
            lines.add(inputLine);
        } while (!inputLine.equals(""));
        HTTPRequest ret = new HTTPRequest();
        ret.dispatcher = dispatcher;
        for (String line : lines) {
            String lineUpperCase = line.toUpperCase();
            if (lineUpperCase.startsWith(METHOD_GET)) {
                if (ret.method != null) throw new RJWSException("Multiple HTTP-Methods");
                ret.method = HTTPMethod.GET;
                parseGET(line, ret);
            } else if (lineUpperCase.startsWith(METHOD_POST)) {
                if (ret.method != null) throw new RJWSException("Multiple HTTP-Methods");
                ret.method = HTTPMethod.POST;
                parseGET(line, ret);
            } else if (lineUpperCase.startsWith(METHOD_HEAD)) {
                if (ret.method != null) throw new RJWSException("Multiple HTTP-Methods");
                ret.method = HTTPMethod.HEAD;
                parseGET(line, ret);
            } else if (lineUpperCase.startsWith(PROPERTY_HOST)) {
                ret.host = line.substring(PROPERTY_HOST.length()).trim();
            } else if (lineUpperCase.startsWith(PROPERTY_CONNECTION)) {
                if (lineUpperCase.contains(PROPERTY_CONNECTION_KEEPALIVE)) ret.connection = HTTPConnection.KEEPALIVE; else if (lineUpperCase.contains(PROPERTY_CONNECTION_CLOSE)) ret.connection = HTTPConnection.CLOSE;
            } else if (lineUpperCase.startsWith(PROPERTY_USERAGENT)) {
                ret.userAgent = line.substring(PROPERTY_USERAGENT.length()).trim();
            } else if (lineUpperCase.startsWith(PROPERTY_CONTENT_LENGTH)) {
                ret.contentLength = Integer.parseInt(line.substring(PROPERTY_CONTENT_LENGTH.length()).trim());
            } else if (lineUpperCase.startsWith(PROPERTY_REFERER)) {
                ret.referer = line.substring(PROPERTY_REFERER.length()).trim();
            } else if (lineUpperCase.startsWith(PROPERTY_CONTENT_TYPE)) {
                final String CONTENT_MULTIPART = "MULTIPART/FORM-DATA";
                String[] parts = line.split("[:]");
                if (parts.length != 2) throw new RJWSException("Part-mismatch: " + parts.length + " in: " + line);
                if (lineUpperCase.contains(CONTENT_MULTIPART)) {
                    ret.multipart = true;
                    parts = parts[1].split("[;]");
                    if (parts.length != 2) throw new RJWSException("Part-mismatch: " + parts.length + " in: " + line);
                    ret.setContentType(parts[0].trim());
                    parts = parts[1].split("[=]");
                    if (parts.length != 2) throw new RJWSException("Part-mismatch: " + parts.length + " in: " + line);
                    ret.setContentBoundary(parts[1].trim());
                } else {
                    ret.setContentType(parts[1].trim());
                }
            }
        }
        if (ret.method == HTTPMethod.POST) parsePOST(reader, ret.contentLength, ret);
        return ret;
    }

    private HTTPRequest() {
    }

    private HTTPMethod method;

    private HTTPConnection connection;

    private String host, path, userAgent, accept, referer, contentType, contentBoundary;

    private String postContent;

    private boolean multipart;

    private int contentLength;

    private final Map<String, String> getVariables = new HashMap<String, String>();

    private final Map<String, String> postVariables = new HashMap<String, String>();

    private RJWSDispatcher dispatcher;

    public HTTPMethod getMethod() {
        return method;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAccept() {
        return accept;
    }

    public HTTPConnection getConnection() {
        return connection;
    }

    public void setMethod(HTTPMethod method) {
        this.method = method;
    }

    public void setConnection(HTTPConnection connection) {
        this.connection = connection;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public Map<String, String> getGetVariables() {
        return getVariables;
    }

    public Map<String, String> getPostVariables() {
        return postVariables;
    }

    /**
	 * @param contentLength the contentLength to set
	 */
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    /**
	 * @return the contentLength
	 */
    public int getContentLength() {
        return contentLength;
    }

    /**
	 * @param referer the referer to set
	 */
    public void setReferer(String referer) {
        this.referer = referer;
    }

    /**
	 * @return the referer
	 */
    public String getReferer() {
        return referer;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentBoundary(String contentBoundary) {
        this.contentBoundary = contentBoundary;
    }

    public String getContentBoundary() {
        return contentBoundary;
    }

    public void setMultipart(boolean multipart) {
        this.multipart = multipart;
    }

    public boolean isMultipart() {
        return multipart;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostContent() {
        return postContent;
    }

    public boolean hasPostContent() {
        return (postContent != null);
    }

    /**
	 * @return the dispatcher
	 */
    public RJWSDispatcher getDispatcher() {
        return dispatcher;
    }

    /**
	 * @param dispatcher the dispatcher to set
	 */
    public void setDispatcher(RJWSDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTPRequest [accept=");
        builder.append(accept);
        builder.append(", connection=");
        builder.append(connection);
        builder.append(", contentBoundary=");
        builder.append(contentBoundary);
        builder.append(", contentLength=");
        builder.append(contentLength);
        builder.append(", contentType=");
        builder.append(contentType);
        builder.append(", getVariables=");
        builder.append(getVariables);
        builder.append(", host=");
        builder.append(host);
        builder.append(", method=");
        builder.append(method);
        builder.append(", multipart=");
        builder.append(multipart);
        builder.append(", path=");
        builder.append(path);
        builder.append(", postContent=");
        builder.append(postContent);
        builder.append(", postVariables=");
        builder.append(postVariables);
        builder.append(", referer=");
        builder.append(referer);
        builder.append(", userAgent=");
        builder.append(userAgent);
        builder.append("]");
        return builder.toString();
    }
}
