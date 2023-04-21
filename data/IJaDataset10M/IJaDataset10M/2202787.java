package sdloader.http;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.http.Cookie;
import sdloader.constants.HttpConstants;
import sdloader.util.CollectionsUtil;
import sdloader.util.WebUtil;

/**
 * HTTPリクエストのヘッダー部分
 * 
 * @author c9katayama
 * @author shot
 */
public class HttpHeader {

    private String requestHeader;

    private String method;

    private String requestURI;

    private String queryString;

    private int statusCode = HttpConstants.SC_OK;

    private String status = HttpConstants.findStatus(HttpConstants.SC_OK);

    private String version = HttpConstants.HTTP_1_1;

    private List<HeaderData> headerList = CollectionsUtil.newArrayList();

    private List<Cookie> cookieList = CollectionsUtil.newArrayList();

    public String getMethod() {
        return method;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getVersion() {
        return version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public HttpHeader() {
    }

    public HttpHeader(String requestHeader) {
        if (requestHeader == null) {
            throw new IllegalArgumentException("Http header is null.");
        }
        this.requestHeader = requestHeader;
        parseHttpRequest(requestHeader);
    }

    private void parseHttpRequest(String httpRequest) {
        StringTokenizer token = new StringTokenizer(httpRequest, HttpConstants.CRLF_STRING, false);
        if (token.hasMoreTokens()) {
            String requestLine = token.nextToken();
            parseRequestPathLine(requestLine);
        } else {
            throw new IllegalArgumentException("Invalid http request.");
        }
        while (token.hasMoreTokens()) {
            String line = token.nextToken();
            if (line.trim().length() <= 0) {
                break;
            }
            int nameEnd = line.indexOf(HttpConstants.COLON_STRING);
            String name = line.substring(0, nameEnd).trim().toLowerCase();
            String value = null;
            int valueStart = nameEnd + HttpConstants.COLON_STRING.length();
            if (line.length() > valueStart) {
                value = line.substring(valueStart, line.length());
            }
            if (name.equalsIgnoreCase(HttpConstants.COOKIE)) {
                parseCookie(value);
            } else {
                addHeader(name, value);
            }
        }
    }

    private void parseRequestPathLine(String requestLine) {
        StringTokenizer token = new StringTokenizer(requestLine, " ", false);
        if (token.hasMoreTokens()) {
            method = token.nextToken().trim();
        } else {
            throw new IllegalArgumentException("Invalid http request. method not found.");
        }
        if (token.hasMoreTokens()) {
            String request = token.nextToken().trim();
            int paramDelim = request.indexOf("?");
            if (paramDelim > 0) {
                requestURI = request.substring(0, paramDelim);
                queryString = request.substring(paramDelim + 1, request.length());
            } else {
                requestURI = request;
            }
        } else {
            throw new IllegalArgumentException("Invalid http request. requestURI not found.");
        }
        if (token.hasMoreTokens()) {
            version = token.nextToken().trim();
        } else {
            throw new IllegalArgumentException("Invalid http request. version not found.");
        }
    }

    private void parseCookie(String cookieValue) {
        if (cookieValue == null || cookieValue.length() <= 0) {
            return;
        }
        StringTokenizer token = new StringTokenizer(cookieValue, HttpConstants.SEMI_COLON_STRING, false);
        while (token.hasMoreTokens()) {
            String keyValue = token.nextToken();
            int delimIndex = keyValue.indexOf("=");
            String key = keyValue.substring(0, delimIndex);
            String value = keyValue.substring(delimIndex + 1, keyValue.length());
            Cookie cookie = new Cookie(key, value);
            cookieList.add(cookie);
        }
    }

    public void addHeader(String name, String value) {
        headerList.add(new HeaderData(name, value));
    }

    public void setHeader(String name, String value) {
        boolean exist = false;
        for (HeaderData data : headerList) {
            if (data.getName().equalsIgnoreCase(name)) {
                data.value = value;
                exist = true;
            }
        }
        if (!exist) {
            addHeader(name, value);
        }
    }

    public List<String> getHeaderNameList() {
        List<String> headerFieldNameList = CollectionsUtil.newArrayList();
        for (HeaderData data : headerList) {
            headerFieldNameList.add(data.getName());
        }
        return headerFieldNameList;
    }

    public String getHeaderValue(String headerName) {
        for (HeaderData data : headerList) {
            if (data.getName().equalsIgnoreCase(headerName)) {
                return data.getValue();
            }
        }
        return null;
    }

    public List<String> getHeaderValueList(String headerName) {
        List<String> headerValueList = CollectionsUtil.newArrayList();
        for (HeaderData data : headerList) {
            if (data.getName().equalsIgnoreCase(headerName)) {
                headerValueList.add(data.getValue());
            }
        }
        return headerValueList;
    }

    public void addCookie(Cookie newCookie) {
        cookieList.add(newCookie);
    }

    public Cookie getCookie(String name) {
        for (Cookie cookie : cookieList) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    public List<Cookie> getCookieList() {
        return cookieList;
    }

    public boolean isKeepAlive() {
        String keepAliveHeader = getHeaderValue(HttpConstants.KEEPALIVE);
        if (keepAliveHeader != null && keepAliveHeader.equalsIgnoreCase(HttpConstants.CLOSE)) {
            return false;
        }
        if (version.equals(HttpConstants.HTTP_1_1)) {
            return true;
        }
        String connection = getHeaderValue(HttpConstants.CONNECTION);
        if (connection != null && connection.equalsIgnoreCase(HttpConstants.KEEPALIVE)) {
            return true;
        }
        return false;
    }

    public String buildResponseHeader() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.version + " " + statusCode + " " + status + HttpConstants.CRLF_STRING);
        for (HeaderData headerData : headerList) {
            String headerName = headerData.getName();
            String headerValue = headerData.getValue();
            buf.append(headerName + HttpConstants.COLON_STRING + headerValue + HttpConstants.CRLF_STRING);
        }
        for (Cookie cookie : cookieList) {
            if (cookie.getValue() != null) {
                buf.append(HttpConstants.SETCOOKIE + HttpConstants.COLON_STRING);
                buf.append(cookie.getName() + "=" + cookie.getValue());
                if (cookie.getMaxAge() > 0) {
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.SECOND, cookie.getMaxAge());
                    buf.append("; expires=" + WebUtil.formatCookieDate(now.getTime()));
                }
                if (cookie.getPath() != null) {
                    buf.append("; path=" + cookie.getPath());
                }
                if (cookie.getDomain() != null) {
                    buf.append("; domain=" + cookie.getDomain());
                }
                if (cookie.getSecure()) {
                    buf.append("; secure");
                }
                buf.append(HttpConstants.CRLF_STRING);
            }
        }
        return buf.toString();
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    protected static class HeaderData {

        private String name;

        private String value;

        public HeaderData(String aName, String aValue) {
            name = aName;
            value = aValue;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
