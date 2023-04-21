package org.clouddreamwork.fetcher;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.util.zip.*;
import org.clouddreamwork.*;

public class FetchService {

    private boolean gzip = false;

    private String method;

    private String parameters = "";

    private String cookie;

    private String content;

    private String url;

    private Map<String, List<String>> header = null;

    public FetchService(String url) throws MalformedURLException {
        this(url, "GET");
    }

    public FetchService(String url, String method) throws MalformedURLException {
        this.url = url;
        this.method = method;
    }

    public String getContent() throws IOException {
        BufferedReader reader = null;
        try {
            HttpURLConnection conn = getConnection();
            header = conn.getHeaderFields();
            cookie = conn.getHeaderField("Set-Cookie");
            if (cookie != null) cookie = cookie.substring(0, cookie.indexOf(";"));
            reader = getBufferedReader(getEncoding(conn));
            String line;
            StringBuilder contentBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) contentBuilder.append(line).append('\n');
            content = contentBuilder.toString();
            return content;
        } catch (IOException e) {
        } finally {
            if (reader != null) reader.close();
        }
        return null;
    }

    public boolean isGzip() {
        return gzip;
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

    public void setURL(String url) throws MalformedURLException {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public void addParameter(String parameter) {
        if (parameters == null) parameters = "";
        if (parameters != "") parameters += "&";
        parameters += parameter;
    }

    public String getParameters() {
        return parameters;
    }

    public String getStoredContent() {
        return content;
    }

    private String getEncoding(HttpURLConnection conn) throws IOException {
        BufferedReader reader = null;
        String encoding = null;
        String ctype = null;
        try {
            ctype = conn.getContentType();
        } catch (Exception e) {
        }
        if (ctype != null) {
            String[] params = ctype.split(";");
            for (String param : params) {
                if (param.toLowerCase().contains("charset=")) return extractEncoding(param.replaceFirst("charset=", "").trim(), true);
            }
        }
        reader = getBufferedReader();
        String line, mat;
        while ((line = reader.readLine()) != null) {
            line = line.toLowerCase();
            mat = matchPattern(line, "(?<=charset=).*?(?=\")");
            if (mat != null) return extractEncoding(mat, true);
            mat = matchPattern(line, "(?<=charset=\").*?(?=\")");
            if (mat != null) return extractEncoding(mat, true);
            mat = matchPattern(line, "(?<=encoding=\").*?(?=\")");
            if (mat != null) return extractEncoding(mat, true);
            if (line.contains("charset") || line.contains("encoding")) {
                encoding = extractEncoding(line, false);
                if (encoding != null) return encoding;
            }
        }
        return "UTF-8";
    }

    private String matchPattern(String str, String regex) {
        Matcher mat = Pattern.compile(regex).matcher(str);
        if (mat.find()) return mat.group(); else return null;
    }

    private BufferedReader getBufferedReader() throws IOException {
        return getBufferedReader(null);
    }

    private BufferedReader getBufferedReader(String encoding) throws IOException {
        HttpURLConnection conn = getConnection();
        return getBufferedReader(conn, encoding);
    }

    private BufferedReader getBufferedReader(HttpURLConnection conn, String encoding) throws IOException {
        BufferedReader reader = null;
        String gzip = null;
        try {
            gzip = conn.getContentEncoding();
        } catch (Exception e) {
        }
        InputStream in = conn.getInputStream();
        if (gzip != null && gzip.toLowerCase().equals("gzip")) {
            this.gzip = true;
            GZIPInputStream gzipin = new GZIPInputStream(in);
            if (encoding == null) reader = new BufferedReader(new InputStreamReader(gzipin)); else reader = new BufferedReader(new InputStreamReader(gzipin, encoding));
        } else {
            if (encoding == null) reader = new BufferedReader(new InputStreamReader(in)); else reader = new BufferedReader(new InputStreamReader(in, encoding));
        }
        return reader;
    }

    private HttpURLConnection getConnection() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        if (cookie != null) conn.setRequestProperty("Cookie", cookie);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("User-Agent", Constants.USER_AGENT());
        conn.connect();
        if (!parameters.equals("")) {
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(parameters);
            out.flush();
            out.close();
        }
        return conn;
    }

    private String extractEncoding(String source, boolean strict) {
        if (source.contains("gb2312") || source.contains("GB2312") || source.contains("gbk") || source.contains("GBK")) return "GBK";
        if (source.contains("big5") || source.contains("big-5") || source.contains("BIG5") || source.contains("BIG-5")) return "Big5-HKSCS";
        if (source.contains("utf-8") || source.contains("utf8") || source.contains("UTF-8") || source.contains("UTF8")) return "UTF-8";
        if (strict) return source; else return null;
    }
}
