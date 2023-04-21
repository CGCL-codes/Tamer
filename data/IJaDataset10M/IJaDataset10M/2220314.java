package com.blommersit.httpd;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Use this class to generate the webserver response and to set cookies
 * @author Rick Blommers
 */
public class HttpResponse {

    /** the printwriter use for writing */
    DataOutputStream writer;

    /**
	 * the result message (first line)
	 * Usual HTTP/1.1 200 Ok"
	 */
    String resultHeader;

    /**
	 * the result header code
	 * Usual 200
	 * @since V1.03
	 */
    int resultHeaderCode;

    /**
     * this boolean remembers if the headers have been send. This is required
     * to know, because it isn't possible to change the headers after they
     * have been send.
     */
    boolean headersSend = false;

    /**
     * the http-headers we need to send. The key contains the headername
     * (incluseive ':'). The value is the header value
     */
    HashMap headers = new HashMap();

    /**
     * the result buffer (here the data is queud till it's flushed
     * Yep we do output buffering.
     */
    StringBuffer buffer = new StringBuffer(512);

    /**
     * the http cookies we need to send. Cookies can result in multiple HTTP
     * headers with the same key. (The cannot be placed in the header hasmap)
     * key=cookieid, value=cookievalue
     */
    HashMap cookies = new HashMap();

    /**
	 * The server thread for accessing the server settings
	 * @since V1.03
	 */
    HttpServerThread serverThread;

    /**
	 * Should the connection be kept alive
	 * @since V1.03?
	 */
    boolean keepAlive = true;

    /**
	 * The output encoding to use
	 * @since V1.04
	 */
    String outputEncoding;

    /**
	 * Simle date formatter to format
	 * @since V1.04
	 */
    public static SimpleDateFormat dateFormatterGMT;

    /** initialize the date formatter */
    static {
        dateFormatterGMT = new SimpleDateFormat("d MMM yyyy HH:mm:ss 'GMT'");
        dateFormatterGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
	 * the default constructor. This method initailises the default response.
	 * The default headers send are:<pre>
	 * HTTP/1.1 200 OK
	 * date: current data
	 * server: blommers-it webserver
	 * connection: close
	 * content-type: text/html; charset=iso=8859-1
	 * cache-control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0
	 * pragma: no-cache
	 * </pre>
	 * @param thread the server thread
	 * @param writer the outputstream
	 */
    public HttpResponse(HttpServerThread thread, DataOutputStream writer) {
        this.writer = writer;
        resultHeader = "HTTP/1.1 200 OK";
        resultHeaderCode = 200;
        outputEncoding = thread.getServer().getSettings().getDefaultResponseCharacterEncoding();
        headers.put("date:", dateFormatterGMT.format(new Date()));
        headers.put("server:", "blommers-it webserver");
        headers.put("connection:", "close");
        headers.put("content-type:", "text/html; charset=" + outputEncoding);
        headers.put("cache-control:", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        headers.put("pragma:", "no-cache");
        this.serverThread = thread;
    }

    /**
	 * change the keepalive flag
	 */
    public void setKeepAlive(boolean alive) {
        this.keepAlive = alive;
    }

    /**
	 * this method sets the header begin. This should be the header result
	 * in the form:  HTTP/1.1 200 OK
	 *
	 * This method still works, it parses the result code from the header
	 *
	 * @param value the header value to set
	 * @deprecated since V1.03 use setHeaderBegin( value, code )	 *
	 */
    public void setHeaderBegin(String value) {
        if (headersSend) throw new java.lang.UnsupportedOperationException("Headers already send....");
        resultHeader = value;
        int pos = value.indexOf(' ');
        int pos2 = value.indexOf(' ', pos + 1);
        if (pos >= 0) {
            String nr = "";
            if (pos2 >= 0) nr = value.substring(pos, pos2).trim(); else nr = value.substring(pos).trim();
            resultHeaderCode = Utils.parseInt(nr, 10, 0);
        }
    }

    /**
	 * this method sets the header begin. This should be the header result
	 * in the form:  HTTP/1.1 200 OK
	 *
	 * @param value the header value to set
	 */
    public void setHeaderBegin(String value, int code) {
        this.resultHeader = value;
        this.resultHeaderCode = code;
    }

    /**
	 * This method sets the header a little smarter. Jou just have to supply
	 * the error number to generate the header.<br />
	 * Can only be called before the headers are sent!<br />
	 * This method modifies the data set with "setHeaderBegin"!
	 * @since V1.03
	 */
    public void setHttpResult(int code) {
        String message = "HTTP/1.1 " + code + " ";
        switch(code) {
            case HttpUtils.HTTP_OK:
                message += "OK";
                break;
            case HttpUtils.HTTP_BAD_REQUEST:
                message += "Bad Request";
                break;
            case HttpUtils.HTTP_NOT_FOUND:
                message += "Not Found";
                break;
            case HttpUtils.HTTP_BAD_METHOD:
                message += "Method Not Allowed";
                break;
            case HttpUtils.HTTP_LENGTH_REQUIRED:
                message += "Length Required";
                break;
            default:
            case HttpUtils.HTTP_INTERNAL_ERROR:
                message += "Internal Server Error";
                break;
        }
        this.resultHeader = message;
        this.resultHeaderCode = code;
    }

    /**
	 * this method returns the begin header currently set
	 * @since V1.03
	 */
    public String getHeaderBegin() {
        return resultHeader;
    }

    /**
	 * this method returns the result code from the begin header
	 * @since V1.03
	 */
    public int getHeaderResultCode() {
        return resultHeaderCode;
    }

    /**
     * this method sets a header
     * @param key the name  of the header (case insensitive). You should you the ':' in the keyname
     * @param value the value of the header
     */
    public void setHeader(String key, String value) {
        key = key.toLowerCase();
        if (headersSend) throw new java.lang.UnsupportedOperationException("Headers already send....");
        headers.put(key, value);
    }

    /**
     * this method removes a header
     * @param key the headername (not case sensitive)
     */
    public void delHeader(String key) {
        key = key.toLowerCase();
        if (headersSend) throw new java.lang.UnsupportedOperationException("Headers already send....");
        headers.remove(key);
    }

    /**
     * this method retrieves a header
     * @param key the headername of the header to retrieve (case insensitive)
     * @param def the default value to return if the header doesn't exist
     * @return the header value
     */
    public String getHeader(String key, String def) {
        key = key.toLowerCase();
        if (!headers.containsKey(key)) return def;
        return (String) headers.get(key);
    }

    /**
     * this method sets a cookie. Deleting a cookie can be done
     * by settings the value to "" and the expires date in the history. (NOT 0
     * because 0 means session expiration, so use 1)
     * @param name the name of the cookie (case sensitive)
     * @param value the value of the cookie
     * @param expires when does the cookie expire ?? ms. since the Unix-epoch
     * @param domain the domain name of the cookie
     * @param path the pathname of the cookie
     * @param secure a secure cookie ?? Note yet supported by the webserver
     */
    public void setCookie(String name, String value, long expires, String domain, String path, boolean secure) {
        Cookie cookie = null;
        if (cookies.containsKey(name)) cookie = (Cookie) cookies.get(name); else {
            cookie = new Cookie();
            cookies.put(name, cookie);
        }
        cookie.value = value;
        cookie.expires = expires;
        cookie.domain = domain;
        cookie.path = path;
        cookie.secure = secure;
    }

    /**
     * this method deletes a cookie. NOTE: this doesn't delete cookie set with a different path or domain
     * @param name the cookie to delete
     */
    public void delCookie(String name) {
        setCookie(name, "", 1, "", "", false);
    }

    /**
     * This method sets a cookie. (A simplified method of the extended method).
     * @param name the name of the cookie
     * @param value the value of the cookie
     */
    public void setCookie(String name, String value) {
        setCookie(name, value, 0, "", "", false);
    }

    /**
     * this method writes an exception in the buffer
     * @param t the throwable object to send
     */
    public void print(Throwable t) {
        StringWriter strWriter = new StringWriter(50);
        PrintWriter pw = new PrintWriter(writer);
        t.printStackTrace(pw);
        buffer.append(strWriter.getBuffer().toString());
    }

    /**
     * this method writes a line of data to the buffer
     * @param data the data to place in the buffer
     */
    public void print(String data) {
        buffer.append(data);
    }

    /**
     * Identical to print
     * for the users who worked with ASP (response.write)
     * @param data the data to write
     */
    public void write(String data) {
        print(data);
    }

    /**
     * this method writes a line with CR-LF to the buffer
     * @param data the data to rptin
     */
    public void println(String data) {
        buffer.append(data);
        buffer.append("\r\n");
    }

    /**
     * Identical to println
     * for the users who worked with ASP (response.writeln)
     * @param data the data to write
     */
    public void writeln(String data) {
        println(data);
    }

    /**
     * this method puts a single character into the buffer
     * @param c the character to put in the buffer
     */
    public void print(char c) {
        buffer.append(c);
    }

    /**
	 * Identical to print
	 * @param c the character to put in the buffer
	 */
    public void write(char c) {
        buffer.append(c);
    }

    /**
	 * Unbuffered writing.
	 * WARNING: You should first manually flush all buffered information. If you
	 * don't do this the output isn't chronological... I promise you, you will
	 * not be happy!
	 * @since V1.03
	 */
    public void writeDirect(byte[] bytes, int offset, int len) throws IOException {
        if (this.serverThread.getHttpRequest().getHttpMethod() != HttpRequest.HTTP_METHOD_HEAD) {
            writer.write(bytes, offset, len);
        }
    }

    /** @since V1.04 shorter method for writing bytes */
    public void writeDirect(byte[] bytes) throws IOException {
        writeDirect(bytes, 0, bytes.length);
    }

    /**
	 * Unbuffered  direct writing
	 * WARNING: You should first manually flush all buffered information. If you
	 * don't do this the output isn't chronological... I promise you, you will
	 * not be happy!
	 * @since V1.04 
	 * @throws java.io.IOException
	 */
    public void writeDirect(String s) throws IOException {
        writeDirect(s.getBytes(outputEncoding));
    }

    /**
	 * Writes a given input stream directly to the response stream
	 * @param stream the input stream to send
	 * @since V1.03
	 */
    public void writeStream(InputStream stream) throws IOException {
        flush();
        if (this.serverThread.getHttpRequest().getHttpMethod() != HttpRequest.HTTP_METHOD_HEAD) {
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int readCount = 0;
            do {
                readCount = stream.read(buffer);
                if (readCount > 0) {
                    writeDirect(buffer, 0, readCount);
                }
            } while (readCount > 0);
        }
    }

    /**
	 * Writes a file directly to the stream, it automaticly detects the
	 * mimetype and sends the correct headers
	 * @param file the file to sent to the stream
	 * @since V1.03
	 */
    public boolean writeFile(File file) throws IOException {
        if (!file.exists()) return false;
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
        if (stream == null) return false;
        try {
            setHeader("content-length:", Long.toString(file.length()));
            setHeader("content-type:", MimeTypeDetector.getMimeType(file.getName(), this, stream));
            flush();
            if (this.serverThread.getHttpRequest().getHttpMethod() != HttpRequest.HTTP_METHOD_HEAD) {
                writeStream(stream);
            }
        } finally {
            stream.close();
        }
        return true;
    }

    /**
	 * this method sends the headers. only if required.
	 * @since V1.03
	 */
    public void sendHeaders() throws IOException {
        if (!headersSend) {
            HttpServerSettings settings = this.serverThread.getServer().getSettings();
            if (settings.getMaxKeepAliveRequestPerConnection() > 0) {
                headers.put("keep-alive:", "timeout=" + settings.getKeepAliveConnectionTimeout() + ",max=" + settings.getMaxKeepAliveRequestPerConnection());
                if (keepAlive) {
                    headers.put("connection:", "keep-alive");
                } else {
                    headers.put("connection:", "close");
                }
            }
            this.serverThread.getServer().getSettings().getHttpInterceptorManager().interceptWriteHeaders(serverThread, serverThread.getHttpRequest(), serverThread.getHttpResponse());
            headersSend = true;
            writeDirect(resultHeader);
            writeDirect("\r\n");
            Iterator it = this.headers.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                writeDirect(key);
                writeDirect((String) headers.get(key));
                writeDirect("\r\n");
            }
            it = this.cookies.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                writeDirect("set-cookie: ");
                writeDirect(key);
                writeDirect("=");
                Cookie cookie = (Cookie) cookies.get(key);
                writeDirect(java.net.URLEncoder.encode(cookie.value, "UTF-8"));
                if (cookie.expires != 0) {
                    writeDirect("; expires=");
                    Date d = new Date(cookie.expires);
                    SimpleDateFormat sf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
                    writeDirect(sf.format(d));
                }
                if (!cookie.path.equals("")) {
                    writeDirect("; path=");
                    writeDirect(cookie.path);
                }
                if (!cookie.domain.equals("")) {
                    writeDirect("; domain=");
                    writeDirect(cookie.domain);
                }
                if (cookie.secure) {
                    writeDirect("; secure");
                }
                writeDirect("\r\n");
            }
            writeDirect("\r\n");
            writer.flush();
        }
    }

    /**
	 * This method flushes the buffer to the client. NOTE: after the first
	 * flush it isn't possible to modify the headers, because they already
	 * have been send.
	 * @throws IOException if data could not be send.
	 */
    public void flush() throws IOException {
        sendHeaders();
        if (this.serverThread.getHttpRequest().getHttpMethod() != HttpRequest.HTTP_METHOD_HEAD) {
            writeDirect(buffer.toString());
            writer.flush();
            buffer.setLength(0);
        }
    }

    /**
	 * private class for storing cookie information
	 */
    class Cookie {

        /** cookie value */
        public String value = "";

        /** expires value, 0=no expiration given -&gt; means session cookie */
        public long expires = 0;

        /** the domainname, "" default */
        public String domain = "";

        /** the path, default is none (most safe='/')*/
        public String path = "";

        /** secure only flag (only transmitted over https) ? Default is false*/
        public boolean secure = false;
    }
}
