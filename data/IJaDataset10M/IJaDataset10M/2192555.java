package util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import util.InitServlet;

public class MultipartRequest implements HttpServletRequest {

    private static final String TEMP_PATH = InitServlet.CONTENT_REALPATH + "/temp";

    private long userId_;

    private HttpServletRequest req_;

    private File dir_ = new File(TEMP_PATH);

    private HashMap<String, ArrayList<String>> parameters_ = new HashMap<String, ArrayList<String>>();

    private HashMap<String, UploadedFile> files_ = new HashMap<String, UploadedFile>();

    private HashMap<File, String> exactFileNameTable_ = new HashMap<File, String>();

    public static boolean isMultipart(HttpServletRequest req) {
        String type = null;
        String type1 = req.getHeader("Content-Type");
        String type2 = req.getContentType();
        if (type1 == null && type2 != null) {
            type = type2;
        } else if (type2 == null && type1 != null) {
            type = type1;
        } else if (type1 != null && type2 != null) {
            type = (type1.length() > type2.length() ? type1 : type2);
        }
        if (type == null || !type.toLowerCase().startsWith("multipart/form-data")) {
            return false;
        } else {
            return true;
        }
    }

    public MultipartRequest(HttpServletRequest req, long userId) throws IOException {
        if (req == null) throw new IllegalArgumentException("request cannot be null");
        if (!dir_.isDirectory()) dir_.mkdir();
        if (!dir_.canWrite()) throw new IllegalArgumentException("服务器上 " + TEMP_PATH + " 这个目录没有写的权限");
        req_ = req;
        userId_ = userId;
        readRequest();
    }

    protected void readRequest() throws IOException {
        int length = req_.getContentLength();
        String type = null;
        String type1 = req_.getHeader("Content-Type");
        String type2 = req_.getContentType();
        if (type1 == null && type2 != null) {
            type = type2;
        } else if (type2 == null && type1 != null) {
            type = type1;
        } else if (type1 != null && type2 != null) {
            type = (type1.length() > type2.length() ? type1 : type2);
        }
        if (type == null || !type.toLowerCase().startsWith("multipart/form-data")) {
            throw new IOException("Posted content type isn't multipart/form-data");
        }
        String boundary = extractBoundary(type);
        if (boundary == null) {
            throw new IOException("Separation boundary was not specified");
        }
        MultipartInputStreamHandler in = new MultipartInputStreamHandler(req_.getInputStream(), length);
        String line = in.readLine();
        if (line == null) {
            throw new IOException("Corrupt form data: premature ending");
        }
        if (!line.startsWith(boundary)) {
            throw new IOException("Corrupt form data: no leading boundary");
        }
        boolean done = false;
        while (!done) {
            done = readNextPart(in, boundary);
        }
    }

    private String extractBoundary(String line) {
        int index = line.lastIndexOf("boundary=");
        if (index == -1) {
            return null;
        }
        String boundary = line.substring(index + 9);
        boundary = "--" + boundary;
        return boundary;
    }

    protected boolean readNextPart(MultipartInputStreamHandler in, String boundary) throws IOException {
        String line = in.readLine();
        if (line == null) {
            return true;
        } else if (line.length() == 0) {
            return true;
        }
        String[] dispInfo = extractDispositionInfo(line);
        String strHttpParameterName = dispInfo[1];
        String strExactFileName = dispInfo[2];
        line = in.readLine();
        if (line == null) {
            return true;
        }
        String contentType = extractContentType(line);
        if (contentType != null) {
            line = in.readLine();
            if (line == null || line.length() > 0) {
                throw new IOException("Malformed line after content type: " + line);
            }
        } else {
            contentType = "application/octet-stream";
        }
        if (strExactFileName == null) {
            String value = readParameter(in, boundary);
            ArrayList<String> existingValues = (ArrayList<String>) parameters_.get(strHttpParameterName);
            if (existingValues == null) {
                existingValues = new ArrayList<String>();
                parameters_.put(strHttpParameterName, existingValues);
            }
            existingValues.add(value);
        } else {
            File temporaryFile;
            if (!strExactFileName.equals("unknown")) {
                String tempfilename = userId_ + strHttpParameterName + System.currentTimeMillis() + ".bin";
                temporaryFile = new File(dir_, tempfilename);
            } else {
                temporaryFile = null;
            }
            readAndSaveFile(in, boundary, temporaryFile, contentType);
            if (!strExactFileName.equals("unknown")) {
                files_.put(strHttpParameterName, new UploadedFile(temporaryFile, contentType));
            }
            exactFileNameTable_.put(temporaryFile, strExactFileName);
        }
        return false;
    }

    protected void readAndSaveFile(MultipartInputStreamHandler in, String boundary, File temporaryFile, String contentType) throws IOException {
        OutputStream out = null;
        if (temporaryFile == null) {
            out = new ByteArrayOutputStream();
        } else {
            if (contentType.equals("application/x-macbinary")) {
                out = new MacBinaryDecoderOutputStream(new BufferedOutputStream(new FileOutputStream(temporaryFile), 8 * 1024));
            } else {
                out = new BufferedOutputStream(new FileOutputStream(temporaryFile), 8 * 1024);
            }
        }
        byte[] bbuf = new byte[100 * 1024];
        int result;
        String line;
        boolean rnflag = false;
        while ((result = in.readLine(bbuf, 0, bbuf.length)) != -1) {
            if (result > 2 && bbuf[0] == '-' && bbuf[1] == '-') {
                line = new String(bbuf, 0, result, "utf-8");
                if (line.startsWith(boundary)) break;
            }
            if (rnflag) {
                out.write('\r');
                out.write('\n');
                rnflag = false;
            }
            if (result >= 2 && bbuf[result - 2] == '\r' && bbuf[result - 1] == '\n') {
                out.write(bbuf, 0, result - 2);
                rnflag = true;
            } else {
                out.write(bbuf, 0, result);
            }
        }
        out.close();
    }

    private String[] extractDispositionInfo(String line) throws IOException {
        String[] retval = new String[3];
        String origline = line;
        line = origline.toLowerCase();
        int start = line.indexOf("content-disposition: ");
        int end = line.indexOf(";");
        if (start == -1 || end == -1) {
            throw new IOException("Content disposition corrupt: " + origline);
        }
        String disposition = line.substring(start + 21, end);
        if (!disposition.equals("form-data")) {
            throw new IOException("Invalid content disposition: " + disposition);
        }
        start = line.indexOf("name=\"", end);
        end = line.indexOf("\"", start + 7);
        if (start == -1 || end == -1) {
            throw new IOException("Content disposition corrupt: " + origline);
        }
        String name = origline.substring(start + 6, end);
        String filename = null;
        start = line.indexOf("filename=\"", end + 2);
        end = line.indexOf("\"", start + 10);
        if (start != -1 && end != -1) {
            filename = origline.substring(start + 10, end);
            int slash = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
            if (slash > -1) {
                filename = filename.substring(slash + 1);
            }
            if (filename.equals("")) filename = "unknown";
        }
        retval[0] = disposition;
        retval[1] = name;
        retval[2] = filename;
        return retval;
    }

    private String extractContentType(String line) throws IOException {
        String contentType = null;
        String origline = line;
        line = origline.toLowerCase();
        if (line.startsWith("content-type")) {
            int start = line.indexOf(" ");
            if (start == -1) {
                throw new IOException("Content type corrupt: " + origline);
            }
            contentType = line.substring(start + 1);
        } else if (line.length() != 0) {
            throw new IOException("Malformed line after disposition: " + origline);
        }
        return contentType;
    }

    protected String readParameter(MultipartInputStreamHandler in, String boundary) throws IOException {
        StringBuffer sbuf = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            if (line.startsWith(boundary)) break;
            sbuf.append(line + "\r\n");
        }
        if (sbuf.length() == 0) {
            return "";
        }
        sbuf.setLength(sbuf.length() - 2);
        return sbuf.toString();
    }

    public File getFile(String strParameterName) {
        UploadedFile file = (UploadedFile) files_.get(strParameterName);
        if (file != null) {
            return file.getFile();
        } else {
            return null;
        }
    }

    public String getAuthType() {
        return req_.getAuthType();
    }

    public Cookie[] getCookies() {
        return req_.getCookies();
    }

    public long getDateHeader(String arg0) {
        return req_.getDateHeader(arg0);
    }

    public String getHeader(String arg0) {
        return req_.getHeader(arg0);
    }

    public int getIntHeader(String arg0) {
        return 0;
    }

    public String getMethod() {
        return null;
    }

    public String getPathInfo() {
        return null;
    }

    public String getPathTranslated() {
        return null;
    }

    public String getContextPath() {
        return null;
    }

    public String getQueryString() {
        return null;
    }

    public String getRemoteUser() {
        return req_.getRemoteUser();
    }

    public boolean isUserInRole(String arg0) {
        return req_.isUserInRole(arg0);
    }

    public Principal getUserPrincipal() {
        return req_.getUserPrincipal();
    }

    public String getRequestedSessionId() {
        return req_.getRequestedSessionId();
    }

    public String getRequestURI() {
        return req_.getRequestURI();
    }

    public String getServletPath() {
        return req_.getServletPath();
    }

    public HttpSession getSession(boolean arg0) {
        return req_.getSession(arg0);
    }

    public HttpSession getSession() {
        return req_.getSession();
    }

    public boolean isRequestedSessionIdValid() {
        return req_.isRequestedSessionIdValid();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return req_.isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromURL() {
        return req_.isRequestedSessionIdFromURL();
    }

    public Object getAttribute(String arg0) {
        return req_.getAttribute(arg0);
    }

    public String getCharacterEncoding() {
        return req_.getCharacterEncoding();
    }

    public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
    }

    public int getContentLength() {
        return req_.getContentLength();
    }

    public String getContentType() {
        return req_.getContentType();
    }

    public ServletInputStream getInputStream() throws IOException {
        return req_.getInputStream();
    }

    public String getParameter(String arg0) {
        try {
            ArrayList<String> values = (ArrayList<String>) parameters_.get(arg0);
            if (values == null || values.size() == 0) {
                return req_.getParameter(arg0);
            }
            String value = (String) values.get(values.size() - 1);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getParameterValues(String arg0) {
        return req_.getParameterValues(arg0);
    }

    public String getProtocol() {
        return req_.getProtocol();
    }

    public String getScheme() {
        return req_.getScheme();
    }

    public String getServerName() {
        return req_.getServerName();
    }

    public int getServerPort() {
        return req_.getServerPort();
    }

    public BufferedReader getReader() throws IOException {
        return req_.getReader();
    }

    public String getRemoteAddr() {
        return req_.getRemoteAddr();
    }

    public String getRemoteHost() {
        return req_.getRemoteHost();
    }

    public void setAttribute(String arg0, Object arg1) {
        req_.setAttribute(arg0, arg1);
    }

    public void removeAttribute(String arg0) {
    }

    public Locale getLocale() {
        return req_.getLocale();
    }

    public boolean isSecure() {
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String arg0) {
        return req_.getRequestDispatcher(arg0);
    }

    public void deleteTemporaryFile() {
        Set<File> files = exactFileNameTable_.keySet();
        Iterator<File> iteratorOfFiles = files.iterator();
        while (iteratorOfFiles.hasNext()) {
            File fileToDel = (File) iteratorOfFiles.next();
            if (fileToDel != null) {
                fileToDel.delete();
            }
        }
    }

    public String getFileName(File file) throws UnsupportedEncodingException {
        return (String) exactFileNameTable_.get(file);
    }

    public StringBuffer getRequestURL() {
        return req_.getRequestURL();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    public String getRealPath(String arg0) {
        return null;
    }

    public int getRemotePort() {
        return req_.getRemotePort();
    }

    public String getLocalName() {
        return req_.getLocalName();
    }

    public String getLocalAddr() {
        return req_.getLocalAddr();
    }

    public int getLocalPort() {
        return req_.getLocalPort();
    }

    public AsyncContext getAsyncContext() {
        return null;
    }

    public Enumeration<String> getAttributeNames() {
        return null;
    }

    public DispatcherType getDispatcherType() {
        return null;
    }

    public Enumeration<Locale> getLocales() {
        return null;
    }

    public Map<String, String[]> getParameterMap() {
        return null;
    }

    public Enumeration<String> getParameterNames() {
        return null;
    }

    public ServletContext getServletContext() {
        return null;
    }

    public boolean isAsyncStarted() {
        return false;
    }

    public boolean isAsyncSupported() {
        return false;
    }

    public AsyncContext startAsync() {
        return null;
    }

    public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) {
        return null;
    }

    public boolean authenticate(HttpServletResponse arg0) throws IOException, ServletException {
        return false;
    }

    public Enumeration<String> getHeaderNames() {
        return null;
    }

    public Enumeration<String> getHeaders(String arg0) {
        return null;
    }

    public Part getPart(String arg0) throws IOException, IllegalStateException, ServletException {
        return null;
    }

    public Collection<Part> getParts() throws IOException, IllegalStateException, ServletException {
        return null;
    }

    public void login(String arg0, String arg1) throws ServletException {
    }

    public void logout() throws ServletException {
    }
}

class MultipartInputStreamHandler {

    ServletInputStream in;

    int totalExpected;

    int totalRead = 0;

    byte[] buf = new byte[8 * 1024];

    MultipartInputStreamHandler(ServletInputStream in, int totalExpected) {
        this.in = in;
        this.totalExpected = totalExpected;
    }

    String readLine() throws IOException {
        StringBuffer sbuf = new StringBuffer();
        int result;
        do {
            result = this.readLine(buf, 0, buf.length);
            if (result != -1) {
                sbuf.append(new String(buf, 0, result, "utf-8"));
            }
        } while (result == buf.length);
        if (sbuf.length() == 0) {
            return null;
        }
        sbuf.setLength(sbuf.length() - 2);
        return sbuf.toString();
    }

    int readLine(byte b[], int off, int len) throws IOException {
        if (totalRead >= totalExpected) {
            return -1;
        } else {
            if (len > (totalExpected - totalRead)) {
                len = totalExpected - totalRead;
            }
            int result = in.readLine(b, off, len);
            if (result > 0) {
                totalRead += result;
            }
            return result;
        }
    }
}

class UploadedFile {

    private File temporaryFile_;

    private String type_;

    UploadedFile(File temporaryFile, String type) {
        temporaryFile_ = temporaryFile;
        type_ = type;
    }

    String getContentType() {
        return type_;
    }

    File getFile() {
        return temporaryFile_;
    }
}

class MacBinaryDecoderOutputStream extends FilterOutputStream {

    private int bytesFiltered_ = 0;

    private int dataForkLength_ = 0;

    MacBinaryDecoderOutputStream(OutputStream out) {
        super(out);
    }

    public void write(int b) throws IOException {
        if (bytesFiltered_ <= 86 && bytesFiltered_ >= 83) {
            int leftShift = (86 - bytesFiltered_) * 8;
            dataForkLength_ = dataForkLength_ | (b & 0xff) << leftShift;
        } else if (bytesFiltered_ < (128 + dataForkLength_) && bytesFiltered_ >= 128) {
            out.write(b);
        }
        bytesFiltered_++;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (bytesFiltered_ >= (128 + dataForkLength_)) {
            bytesFiltered_ += len;
        } else if (bytesFiltered_ >= 128 && (bytesFiltered_ + len) <= (128 + dataForkLength_)) {
            out.write(b, off, len);
            bytesFiltered_ += len;
        } else {
            for (int i = 0; i < len; i++) {
                write(b[off + i]);
            }
        }
    }
}
