package jolie.net.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpMessage {

    public enum Type {

        RESPONSE, POST, GET, UNSUPPORTED, ERROR
    }

    public enum Version {

        HTTP_1_0, HTTP_1_1
    }

    public static class Cookie {

        private final String name, value, domain, path, expirationDate;

        private final boolean secure;

        public Cookie(String name, String value, String domain, String path, String expirationDate, boolean secure) {
            this.name = name;
            this.value = value;
            this.domain = domain;
            this.path = path;
            this.expirationDate = expirationDate;
            this.secure = secure;
        }

        @Override
        public String toString() {
            return (name + "=" + value + "; " + "expires=" + expirationDate + "; " + "domain=" + domain + "; " + "path=" + path + ((secure) ? ("; secure") : ""));
        }

        public String name() {
            return name;
        }

        public String value() {
            return value;
        }

        public String path() {
            return path;
        }

        public String domain() {
            return domain;
        }

        public String expirationDate() {
            return expirationDate;
        }

        public boolean secure() {
            return secure;
        }
    }

    private Version version;

    private Type type;

    private byte[] content = null;

    private final Map<String, String> propMap = new HashMap<String, String>();

    private final List<Cookie> setCookies = new ArrayList<Cookie>();

    private final Map<String, String> cookies = new HashMap<String, String>();

    private int httpCode;

    private String requestPath;

    private String reason;

    private String userAgent;

    public boolean isSupported() {
        return type != Type.UNSUPPORTED;
    }

    public boolean isGet() {
        return type == Type.GET;
    }

    public void addCookie(String name, String value) {
        cookies.put(name, value);
    }

    public Map<String, String> cookies() {
        return cookies;
    }

    public void addSetCookie(Cookie cookie) {
        setCookies.add(cookie);
    }

    public List<Cookie> setCookies() {
        return setCookies;
    }

    public HttpMessage(Type type) {
        this.type = type;
    }

    protected void setVersion(Version version) {
        this.version = version;
    }

    public Version version() {
        return version;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Collection<Entry<String, String>> properties() {
        return propMap.entrySet();
    }

    public void setRequestPath(String path) {
        requestPath = path;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setProperty(String name, String value) {
        propMap.put(name, value);
    }

    public String getProperty(String name) {
        return propMap.get(name.toLowerCase());
    }

    public String getPropertyOrEmptyString(String name) {
        String ret = propMap.get(name);
        return (ret == null) ? "" : ret;
    }

    public String reason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int size() {
        if (content == null) return 0;
        return content.length;
    }

    public String requestPath() {
        return requestPath;
    }

    public String userAgent() {
        return userAgent;
    }

    public boolean isResponse() {
        return type == Type.RESPONSE;
    }

    public boolean isError() {
        return type == Type.ERROR;
    }

    public int httpCode() {
        return httpCode;
    }

    public void setHttpCode(int code) {
        httpCode = code;
    }

    public byte[] content() {
        return content;
    }
}
