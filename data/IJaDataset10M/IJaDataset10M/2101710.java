package org.myrobotlab.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.myrobotlab.framework.Service;

/**
 * Client HTTP Request class This class helps to send POST HTTP requests with
 * various form data, including files. Cookies can be added to be included in
 * the request.
 * 
 * @author Vlad Patryshev
 * @version 1.0
 * 
 *          Modified by grog - added buffered output to increase performance in
 *          larger POSTs. In file operations buffered output is 10x faster.
 *          Currently, a POST of a large file takes ~6 seconds TODO - fill in
 *          details and result
 * 
 *          References:
 *          http://www.javabeat.net/tips/36-file-upload-and-download-
 *          using-java.html http://www.java2s.com/Code/Java/File-Input-Output/
 *          ComparingBufferedandUnbufferedWritingPerformance.htm
 *          
 *          The big beautiful kahuna from stack overflow
 *          http://stackoverflow.com/questions/2793150/how-to-use-java-net-urlconnection-to-fire-and-handle-http-requests
 *          
 *          
 */
public class HTTPRequest {

    public static final Logger LOG = Logger.getLogger(HTTPRequest.class.getCanonicalName());

    URLConnection connection;

    OutputStream osstr = null;

    BufferedOutputStream os = null;

    Map<String, String> cookies = new HashMap<String, String>();

    String boundary = "---------------------------";

    String error = null;

    protected void connect() throws IOException {
        if (os == null) os = new BufferedOutputStream(connection.getOutputStream());
    }

    protected void write(char c) throws IOException {
        connect();
        os.write(c);
    }

    protected void write(String s) throws IOException {
        Service.logTime("t1", "write-connect");
        connect();
        Service.logTime("t1", "write-post connect");
        os.write(s.getBytes());
        Service.logTime("t1", "post write s.getBytes");
    }

    protected void newline() throws IOException {
        connect();
        write("\r\n");
    }

    protected void writeln(String s) throws IOException {
        connect();
        write(s);
        newline();
    }

    private void boundary() throws IOException {
        write("--");
        write(boundary);
    }

    /**
	 * Creates a new multipart POST HTTP request on a freshly opened
	 * URLConnection
	 * 
	 * @param connection
	 *            an already open URL connection
	 * @throws IOException
	 */
    public HTTPRequest(URLConnection connection) throws IOException {
        LOG.info("http request for " + connection.getURL());
        this.connection = connection;
        connection.setDoOutput(true);
    }

    /**
	 * Creates a new multipart POST HTTP request for a specified URL
	 * 
	 * @param url
	 *            the URL to send request to
	 * @throws IOException
	 */
    public HTTPRequest(URL url) throws IOException {
        this(url.openConnection());
    }

    /**
	 * Creates a new multipart POST HTTP request for a specified URL string
	 * 
	 * @param urlString
	 *            the string representation of the URL to send request to
	 * @throws IOException
	 */
    public HTTPRequest(String urlString) throws IOException {
        this(new URL(urlString));
    }

    public void setRequestProperty(String key, String value) {
        if (connection != null) {
            connection.setRequestProperty(key, value);
        }
    }

    public void postCookies() {
        StringBuffer cookieList = new StringBuffer();
        for (Iterator<Entry<String, String>> i = cookies.entrySet().iterator(); i.hasNext(); ) {
            Entry<String, String> entry = (i.next());
            cookieList.append(entry.getKey().toString() + "=" + entry.getValue());
            if (i.hasNext()) {
                cookieList.append("; ");
            }
        }
        if (cookieList.length() > 0) {
            connection.setRequestProperty("Cookie", cookieList.toString());
        }
    }

    /**
	 * adds a cookie to the requst
	 * 
	 * @param name
	 *            cookie name
	 * @param value
	 *            cookie value
	 * @throws IOException
	 */
    public void setCookie(String name, String value) throws IOException {
        cookies.put(name, value);
    }

    /**
	 * adds cookies to the request
	 * 
	 * @param cookies
	 *            the cookie "name-to-value" map
	 * @throws IOException
	 */
    public void setCookies(Map<String, String> cookies) throws IOException {
        if (cookies == null) return;
        this.cookies.putAll(cookies);
    }

    /**
	 * adds cookies to the request
	 * 
	 * @param cookies
	 *            array of cookie names and values (cookies[2*i] is a name,
	 *            cookies[2*i + 1] is a value)
	 * @throws IOException
	 */
    public void setCookies(String[] cookies) throws IOException {
        if (cookies == null) return;
        for (int i = 0; i < cookies.length - 1; i += 2) {
            setCookie(cookies[i], cookies[i + 1]);
        }
    }

    private void writeName(String name) throws IOException {
        newline();
        write("Content-Disposition: form-data; name=\"");
        write(name);
        write('"');
    }

    /**
	 * adds a string parameter to the request
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws IOException
	 */
    public void setParameter(String name, String value) throws IOException {
        boundary();
        writeName(name);
        newline();
        newline();
        writeln(value);
    }

    private void pipe(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[500000];
        int nread;
        synchronized (in) {
            while ((nread = in.read(buf, 0, buf.length)) >= 0) {
                out.write(buf, 0, nread);
            }
        }
        out.flush();
        in.close();
        buf = null;
    }

    /**
	 * adds a file parameter to the request
	 * 
	 * @param name
	 *            parameter name
	 * @param filename
	 *            the name of the file
	 * @param is
	 *            input stream to read the contents of the file from
	 * @throws IOException
	 */
    public void setParameter(String name, String filename, InputStream is) throws IOException {
        Service.logTime("t1", "setParameter begin (after new fileinput)");
        boundary();
        writeName(name);
        write("; filename=\"");
        write(filename);
        write('"');
        newline();
        write("Content-Type: ");
        Service.logTime("t1", "pre guessContentTypeFromName");
        String type = URLConnection.guessContentTypeFromName(filename);
        if (type == null) type = "application/octet-stream";
        writeln(type);
        Service.logTime("t1", "post guessContentTypeFromName");
        newline();
        pipe(is, os);
        newline();
    }

    /**
	 * adds a file parameter to the request
	 * 
	 * @param name
	 *            parameter name
	 * @param file
	 *            the file to upload
	 * @throws IOException
	 */
    public void setParameter(String name, File file) throws IOException {
        Service.logTime("t1", "pre set file");
        setParameter(name, file.getPath(), new FileInputStream(file));
        Service.logTime("t1", "post set file");
    }

    /**
	 * adds a parameter to the request; if the parameter is a File, the file is
	 * uploaded, otherwise the string value of the parameter is passed in the
	 * request
	 * 
	 * @param name
	 *            parameter name
	 * @param object
	 *            parameter value, a File or anything else that can be
	 *            stringified
	 * @throws IOException
	 */
    public void setParameter(String name, Object object) throws IOException {
        if (object instanceof File) {
            setParameter(name, (File) object);
        } else {
            setParameter(name, object.toString());
        }
    }

    /**
	 * adds parameters to the request
	 * 
	 * @param parameters
	 *            "name-to-value" map of parameters; if a value is a file, the
	 *            file is uploaded, otherwise it is stringified and sent in the
	 *            request
	 * @throws IOException
	 */
    public void setParameters(Map parameters) throws IOException {
        if (parameters == null) return;
        for (Iterator i = parameters.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            setParameter(entry.getKey().toString(), entry.getValue());
        }
    }

    /**
	 * adds parameters to the request
	 * 
	 * @param parameters
	 *            array of parameter names and values (parameters[2*i] is a
	 *            name, parameters[2*i + 1] is a value); if a value is a file,
	 *            the file is uploaded, otherwise it is stringified and sent in
	 *            the request
	 * @throws IOException
	 */
    public void setParameters(Object[] parameters) throws IOException {
        if (parameters == null) return;
        for (int i = 0; i < parameters.length - 1; i += 2) {
            setParameter(parameters[i].toString(), parameters[i + 1]);
        }
    }

    /**
	 * posts the requests to the server, with all the cookies and parameters
	 * that were added
	 * 
	 * @return input stream with the server response
	 * @throws IOException
	 */
    public InputStream post() {
        try {
            boundary();
            writeln("--");
            os.close();
            return connection.getInputStream();
        } catch (IOException e) {
        }
        return null;
    }

    /**
	 * posts the requests to the server, with all the cookies and parameters
	 * that were added before (if any), and with parameters that are passed in
	 * the argument
	 * 
	 * @param parameters
	 *            request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameters
	 */
    public InputStream post(Map parameters) throws IOException {
        setParameters(parameters);
        return post();
    }

    /**
	 * posts the requests to the server, with all the cookies and parameters
	 * that were added before (if any), and with parameters that are passed in
	 * the argument
	 * 
	 * @param parameters
	 *            request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameters
	 */
    public InputStream post(Object[] parameters) throws IOException {
        setParameters(parameters);
        return post();
    }

    /**
	 * posts the requests to the server, with all the cookies and parameters
	 * that were added before (if any), and with cookies and parameters that are
	 * passed in the arguments
	 * 
	 * @param cookies
	 *            request cookies
	 * @param parameters
	 *            request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameters
	 * @see setCookies
	 */
    public InputStream post(Map<String, String> cookies, Map parameters) throws IOException {
        setCookies(cookies);
        setParameters(parameters);
        return post();
    }

    /**
	 * posts the requests to the server, with all the cookies and parameters
	 * that were added before (if any), and with cookies and parameters that are
	 * passed in the arguments
	 * 
	 * @param cookies
	 *            request cookies
	 * @param parameters
	 *            request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameters
	 * @see setCookies
	 */
    public InputStream post(String[] cookies, Object[] parameters) throws IOException {
        setCookies(cookies);
        setParameters(parameters);
        return post();
    }

    /**
	 * post the POST request to the server, with the specified parameter
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameter
	 */
    public InputStream post(String name, Object value) throws IOException {
        setParameter(name, value);
        return post();
    }

    /**
	 * post the POST request to the server, with the specified parameters
	 * 
	 * @param name1
	 *            first parameter name
	 * @param value1
	 *            first parameter value
	 * @param name2
	 *            second parameter name
	 * @param value2
	 *            second parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameter
	 */
    public InputStream post(String name1, Object value1, String name2, Object value2) throws IOException {
        setParameter(name1, value1);
        return post(name2, value2);
    }

    /**
	 * post the POST request to the server, with the specified parameters
	 * 
	 * @param name1
	 *            first parameter name
	 * @param value1
	 *            first parameter value
	 * @param name2
	 *            second parameter name
	 * @param value2
	 *            second parameter value
	 * @param name3
	 *            third parameter name
	 * @param value3
	 *            third parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameter
	 */
    public InputStream post(String name1, Object value1, String name2, Object value2, String name3, Object value3) throws IOException {
        setParameter(name1, value1);
        return post(name2, value2, name3, value3);
    }

    /**
	 * post the POST request to the server, with the specified parameters
	 * 
	 * @param name1
	 *            first parameter name
	 * @param value1
	 *            first parameter value
	 * @param name2
	 *            second parameter name
	 * @param value2
	 *            second parameter value
	 * @param name3
	 *            third parameter name
	 * @param value3
	 *            third parameter value
	 * @param name4
	 *            fourth parameter name
	 * @param value4
	 *            fourth parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameter
	 */
    public InputStream post(String name1, Object value1, String name2, Object value2, String name3, Object value3, String name4, Object value4) throws IOException {
        setParameter(name1, value1);
        return post(name2, value2, name3, value3, name4, value4);
    }

    /**
	 * posts a new request to specified URL, with parameters that are passed in
	 * the argument
	 * 
	 * @param parameters
	 *            request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameters
	 */
    public InputStream post(URL url, Map parameters) throws IOException {
        return new HTTPRequest(url).post(parameters);
    }

    /**
	 * posts a new request to specified URL, with parameters that are passed in
	 * the argument
	 * 
	 * @param parameters
	 *            request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameters
	 */
    public InputStream post(URL url, Object[] parameters) throws IOException {
        return new HTTPRequest(url).post(parameters);
    }

    /**
	 * posts a new request to specified URL, with cookies and parameters that
	 * are passed in the argument
	 * 
	 * @param cookies
	 *            request cookies
	 * @param parameters
	 *            request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setCookies
	 * @see #setParameters
	 */
    public InputStream post(URL url, Map<String, String> cookies, Map parameters) throws IOException {
        return new HTTPRequest(url).post(cookies, parameters);
    }

    /**
	 * posts a new request to specified URL, with cookies and parameters that
	 * are passed in the argument
	 * 
	 * @param cookies
	 *            request cookies
	 * @param parameters
	 *            request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setCookies
	 * @see #setParameters
	 */
    public InputStream post(URL url, String[] cookies, Object[] parameters) throws IOException {
        return new HTTPRequest(url).post(cookies, parameters);
    }

    /**
	 * post the POST request specified URL, with the specified parameter
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameter
	 */
    public InputStream post(URL url, String name1, Object value1) throws IOException {
        return new HTTPRequest(url).post(name1, value1);
    }

    /**
	 * post the POST request to specified URL, with the specified parameters
	 * 
	 * @param name1
	 *            first parameter name
	 * @param value1
	 *            first parameter value
	 * @param name2
	 *            second parameter name
	 * @param value2
	 *            second parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameter
	 */
    public InputStream post(URL url, String name1, Object value1, String name2, Object value2) throws IOException {
        return new HTTPRequest(url).post(name1, value1, name2, value2);
    }

    /**
	 * post the POST request to specified URL, with the specified parameters
	 * 
	 * @param name1
	 *            first parameter name
	 * @param value1
	 *            first parameter value
	 * @param name2
	 *            second parameter name
	 * @param value2
	 *            second parameter value
	 * @param name3
	 *            third parameter name
	 * @param value3
	 *            third parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameter
	 */
    public InputStream post(URL url, String name1, Object value1, String name2, Object value2, String name3, Object value3) throws IOException {
        return new HTTPRequest(url).post(name1, value1, name2, value2, name3, value3);
    }

    /**
	 * post the POST request to specified URL, with the specified parameters
	 * 
	 * @param name1
	 *            first parameter name
	 * @param value1
	 *            first parameter value
	 * @param name2
	 *            second parameter name
	 * @param value2
	 *            second parameter value
	 * @param name3
	 *            third parameter name
	 * @param value3
	 *            third parameter value
	 * @param name4
	 *            fourth parameter name
	 * @param value4
	 *            fourth parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see #setParameter
	 */
    public InputStream post(URL url, String name1, Object value1, String name2, Object value2, String name3, Object value3, String name4, Object value4) throws IOException {
        return new HTTPRequest(url).post(name1, value1, name2, value2, name3, value3, name4, value4);
    }

    public byte[] getBinary() {
        error = null;
        String contentType = connection.getContentType();
        int contentLength = connection.getContentLength();
        LOG.info("contentType " + contentType + " contentLength " + contentLength);
        InputStream raw;
        byte[] data = null;
        try {
            raw = connection.getInputStream();
            InputStream in = new BufferedInputStream(raw);
            data = new byte[contentLength];
            int bytesRead = 0;
            int offset = 0;
            while (offset < contentLength) {
                bytesRead = in.read(data, offset, data.length - offset);
                if (bytesRead == -1) break;
                offset += bytesRead;
            }
            in.close();
            if (offset != contentLength) {
                throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
            }
        } catch (IOException e1) {
            Service.logException(e1);
            error = e1.getMessage();
        }
        return data;
    }

    public String getString() {
        byte[] b = getBinary();
        if (b != null) return new String(b);
        return null;
    }

    public static void main(String[] args) throws Exception {
        org.apache.log4j.BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
        HTTPRequest http = new HTTPRequest("http://www.mkyong.com/java/how-do-convert-byte-array-to-string-in-java/");
        String s = http.getString();
        LOG.info(s);
        String language = "en";
        String toSpeak = "hello";
        URI uri = new URI("http", null, "translate.google.com", 80, "/translate_tts", "tl=" + language + "&q=" + toSpeak, null);
        URL url = uri.toURL();
        HttpURLConnection.setFollowRedirects(true);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        System.out.println("Response code = " + connection.getResponseCode());
        String header = connection.getHeaderField("location");
        if (header != null) System.out.println("Redirected to " + header);
        HTTPRequest request = new HTTPRequest(uri.toURL());
        request.getBinary();
    }

    public boolean hasError() {
        return error != null;
    }

    public String getError() {
        return error;
    }
}
