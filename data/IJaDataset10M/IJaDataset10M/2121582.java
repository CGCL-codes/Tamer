package de.opus5.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * A class for handling multipart form uploads.
 * Unlike com.oreilly.servlet.MultipartRequest this
 * class returns a InputStream for reading the
 * file's content, so you can also write the data
 * into magic crystals instead of writing it to
 * the file system. ;) 
 * There is also the option to save files to
 * disk and return a java.io.File object.
 * You can also access the file data via the 
 * getUploadedFile() method, which returns an
 * file object implementing the 
 * de.opus5.servlet.UploadedFile interface.
 * The class can decide based on the total request size
 * if the file data is temporary stored in memory
 * or in the file system.
 * <p>
 * Example:
 * <blockquote><pre>
 * MulipartRequest req;
 * ..
 * try {
 *    req = new MultipartRequest(servletRequest,maxMemoryStoredRequestSize,
 *			tmpdir,fileprefix);
 * } catch (Exception e) {
 *   ...
 * }
 *
 * ....
 * filename = req.getUploadedFile("Document").getName();
 * is = req.getInputStreamForFile("Document");
 * while (is.read()...) {
 * ...
 * }
 * login = req.getParameter("Login");
 * ...
 * UploadedFile adressFile = mReq.getUploadedFile(Global.MPADRESSF_KEY);
 * UploadedFile docFile = mReq.getUploadedFile(Global.MPDOCFILE_KEY);
 * myObject.setAdressFile(adressFile); 
 * ....
 * ....
 * </pre></blockquote>
 * </p>
 * @author Frederik Dahlke, opus 5 interaktive medien gmbh
 * @version $Id: HttpMultipartRequest.java,v 1.2 2000/06/04 20:08:46 ege Exp $
 */
public class HttpMultipartRequest implements HttpServletRequest {

    private static final int DEFAULT_MAX_REQUEST_SIZE = 67108864;

    private static final int DEFAULT_MAX_MEMORY_STORED_REQUEST_SIZE = 1048576;

    private static final Object isUpload = new Object();

    private String boundary;

    int bytesRead = 0;

    private Hashtable files = new Hashtable();

    private boolean keepFiles = false;

    private int maxMemoryStoredRequestSize;

    private int maxRequestSize;

    private BucketHash parameterValues = new BucketHash();

    int requestSize;

    boolean endMarkFound;

    private ServletInputStream servletInputStream;

    private HttpServletRequest servletRequest;

    private String tmpDir;

    private String uniquePrefix;

    /**
   * This constructor is intentionaly private to prevent
   * developers calling the class with obscure settings.
   * All desired behaviours can be obtained by calling
   * one of the public constructors.
   * @param sR - the ServletRequest
   */
    private HttpMultipartRequest(HttpServletRequest sR, int maxMemStoredReqSiz, int maxReqSiz, String tmpDir, String uniquePrefix, boolean keepFiles) throws IllegalArgumentException, IOException {
        this.tmpDir = tmpDir;
        this.uniquePrefix = uniquePrefix;
        this.maxMemoryStoredRequestSize = maxMemStoredReqSiz;
        this.maxRequestSize = maxReqSiz;
        this.keepFiles = keepFiles;
        this.checkServletRequest(sR);
        this.servletRequest = sR;
        this.readRequest();
    }

    /**
   * Create a MultipartRequest, 
   * temporary files will be deleted.
   * @param sR the ServletRequest
   * @param maxMemStoredReqSiz If the request size in bytes exceeds
   * this value, the uploaded files will be stored on disk.
   * @param maxReqSiz Maximum size of request.
   * @param tmpDir Directory where temporary files are saved.
   * @param uniquePrefix Prefix added to the filenames to prevent
   * overwriting of files with the same name.
   */
    public HttpMultipartRequest(HttpServletRequest sR, int maxMemStoredReqSiz, int maxReqSiz, String tmpDir, String uniquePrefix) throws IllegalArgumentException, IOException {
        this(sR, maxMemStoredReqSiz, maxReqSiz, tmpDir, uniquePrefix, false);
    }

    /**
   * Create a MultipartRequest,
   * temporary files will be deleted, the maximum request size
   * is set to 1MB.
   * @param sR the ServletRequest
   * @param maxMemStoredReqSiz If the request size in bytes exceeds
   * this value, the uploaded files will be stored on disk.
   * @param tmpDir Directory where temporary files are saved.
   * @param uniquePrefix Prefix added to the filenames to prevent
   * overwriting of files with the same name.
   */
    public HttpMultipartRequest(HttpServletRequest sR, int maxMemStoredReqSiz, String tmpDir, String uniquePrefix) throws IllegalArgumentException, IOException {
        this(sR, maxMemStoredReqSiz, DEFAULT_MAX_REQUEST_SIZE, tmpDir, uniquePrefix, false);
    }

    /**
   * Create a MultipartRequest,
   * files will be saved to disk allways.
   * @param sR the ServletRequest
   * @param maxReqSiz Maximum size of request.
   * @param tmpDir Directory where temporary files are saved.
   * @param uniquePrefix Prefix added to the filenames to prevent
   * overwriting of files with the same name.
   * @param keep If set, the uploaded files will not be deleted.
   */
    public HttpMultipartRequest(HttpServletRequest sR, int maxReqSiz, String tmpDir, String uniquePrefix, boolean keep) throws IllegalArgumentException, IOException {
        this(sR, 0, maxReqSiz, tmpDir, uniquePrefix, keep);
    }

    /**
   * Create a MultipartRequest using the ServletRequest object,
   * temporary files will be deleted.
   * Maximum request size is 1MB, files will be saved to disk.
   * @param sR the ServletRequest
   * @param tmpDir Directory where temporary files are saved.
   * @param uniquePrefix Prefix added to the filenames to prevent
   * overwriting of files with the same name.
   */
    public HttpMultipartRequest(HttpServletRequest sR, String tmpDir, String uniquePrefix) throws IllegalArgumentException, IOException {
        this(sR, DEFAULT_MAX_MEMORY_STORED_REQUEST_SIZE, DEFAULT_MAX_REQUEST_SIZE, tmpDir, uniquePrefix, false);
    }

    /**
   * Check if the request is not null and has the
   * appropriate content type.
   */
    private void checkServletRequest(HttpServletRequest sR) throws IllegalArgumentException, IOException {
        if (sR == null) throw new IllegalArgumentException("Request object is null.");
        this.requestSize = sR.getContentLength();
        if (this.requestSize > maxRequestSize && maxRequestSize != -1) {
            throw new IllegalArgumentException("Request size of " + this.requestSize + " bytes exceeds maximum request size ( " + maxRequestSize + ")");
        }
        String contentType = sR.getContentType();
        if (!(contentType.toLowerCase().startsWith("multipart/form-data"))) throw new IllegalArgumentException("Unknown content type.");
        int index = contentType.indexOf("boundary=");
        if (index == -1) throw new IllegalArgumentException("No boundary specified.");
        index += 9;
        this.boundary = "--" + contentType.substring(index);
        this.servletInputStream = sR.getInputStream();
    }

    /**
   * Read File Data
   */
    private UploadedFile readFileData(String filename, String fieldname, String contentType) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        boolean ret = false;
        boolean checkBoundary = false;
        UploadedFile upfile;
        OutputStream os;
        BufferedOutputStream bos;
        int result, filesize, buffPos;
        if (requestSize > maxMemoryStoredRequestSize) {
            upfile = new DiskFile(fieldname, filename, tmpDir, uniquePrefix + filename, keepFiles);
        } else {
            upfile = new MemoryFile(fieldname, filename);
        }
        upfile.setContentType(contentType);
        os = upfile.getOutputStream();
        bos = new BufferedOutputStream(os);
        filesize = 0;
        result = 0;
        buffPos = 0;
        while ((bytesRead < requestSize) && ((result = servletInputStream.read()) != -1)) {
            bytesRead++;
            if (checkBoundary) {
                buffer[buffPos] = (byte) result;
                buffPos++;
                if (result == '\r') {
                    bos.write('\r');
                    bos.write('\n');
                    bos.write(buffer, 0, buffPos - 1);
                    filesize += buffPos + 2;
                    buffPos = 0;
                    checkBoundary = false;
                } else if (buffPos == boundary.length()) {
                    String line = new String(buffer, 0, buffPos, "ISO-8859-1");
                    if (line.startsWith(boundary)) {
                        int nc;
                        nc = servletInputStream.read();
                        if (nc != -1) {
                            bytesRead++;
                            if (nc == '-') {
                                endMarkFound = true;
                                if (servletInputStream.read() != -1) bytesRead++;
                                if (servletInputStream.read() != -1) bytesRead++;
                                if (servletInputStream.read() != -1) bytesRead++;
                            } else {
                                if (servletInputStream.read() != -1) bytesRead++;
                            }
                        }
                        break;
                    } else {
                        bos.write('\r');
                        bos.write('\n');
                        bos.write(buffer, 0, buffPos);
                        filesize += buffPos + 2;
                        buffPos = 0;
                        checkBoundary = false;
                        ret = false;
                    }
                }
            } else if (ret) {
                if (result == '\n') {
                    checkBoundary = true;
                } else {
                    bos.write('\r');
                    bos.write(result);
                    filesize += 2;
                    ret = false;
                }
            } else if (result == '\r') {
                ret = true;
            } else {
                bos.write(result);
                filesize++;
            }
        }
        bos.flush();
        bos.close();
        os.close();
        upfile.setSize(filesize);
        return upfile;
    }

    /**
   * Read next line of input.
   * @return String, null at end of stream
   */
    private String readLine() throws IOException {
        String line = this.readLineSimple();
        if (line == null) return null;
        return line.substring(0, line.length() - 2);
    }

    /**
   * Read next line of input, do not chop \r\n .
   * @return String, null at end of stream
   */
    private String readLineSimple() throws IOException {
        byte[] buffer = new byte[8 * 1024];
        StringBuffer stringBuffer = new StringBuffer();
        int result;
        String line;
        if (this.bytesRead >= this.requestSize || endMarkFound) {
            return null;
        }
        do {
            result = servletInputStream.readLine(buffer, 0, buffer.length);
            if (result != -1) {
                this.bytesRead += result;
                stringBuffer.append(new String(buffer, 0, result, "ISO-8859-1"));
            }
        } while (result == buffer.length);
        if (stringBuffer.length() == 0) return null;
        return stringBuffer.toString();
    }

    /**
   * read the parameter's data.
   * @return the parameter data
   */
    private String readParameterData() throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        String end = boundary + "--";
        while ((line = this.readLine()) != null) {
            if (line.startsWith(end)) {
                endMarkFound = true;
            }
            if (line.startsWith(boundary)) break;
            stringBuffer.append(line + "\r\n");
        }
        if (stringBuffer.length() == 0) return null;
        stringBuffer.setLength(stringBuffer.length() - 2);
        return stringBuffer.toString();
    }

    /**
   * read all paramters and filenames from request
   * and store them in the hashes.
   */
    private void readRequest() throws IOException {
        String line = "";
        line = this.readLine();
        while ((line = this.readLine()) != null) {
            if (line.length() == 0) continue;
            String loLine = line.toLowerCase();
            if (!loLine.startsWith("content-disposition: form-data;")) throw new IOException("Unknown or no content-disposition found.");
            int start = loLine.indexOf("name=\"");
            int end = loLine.indexOf("\"", start + 7);
            if (start == -1 || end == -1) throw new IOException("No field name found in content-disposition.");
            String fieldname = line.substring(start + 6, end);
            String filename = null;
            start = loLine.indexOf("filename=\"", end);
            end = loLine.indexOf("\"", start + 10);
            if (start != -1 && end != -1) {
                filename = line.substring(start + 10, end);
                int basename = Math.max(filename.lastIndexOf("/"), filename.lastIndexOf("\\"));
                if (basename != -1) filename = filename.substring(basename + 1);
            }
            String contentType = null;
            if ((line = this.readLine()) == null) {
                throw new IOException("Malformed multipart-request.");
            }
            loLine = line.toLowerCase();
            if (loLine.startsWith("content-type: ")) {
                contentType = line.substring(14, line.length());
                do {
                    line = this.readLine();
                    if (line == null) throw new IOException("Malformed multipart-request.");
                } while (line.length() != 0);
            }
            if (filename != null) {
                UploadedFile file = this.readFileData(filename, fieldname, contentType);
                this.files.put(fieldname, file);
                String name = filename;
                if (name.lastIndexOf('/') >= 0) name = name.substring(name.lastIndexOf('/') + 1);
                if (name.lastIndexOf('\\') >= 0) name = name.substring(name.lastIndexOf('\\') + 1);
                this.parameterValues.put(fieldname, name);
            } else {
                this.parameterValues.put(fieldname, this.readParameterData());
            }
        }
    }

    /**
   * returns a java.io.File object if the file
   * was saved on disk, null otherwise. 
   * This this method should only be called, if
   * keepFiles is set to true.
   * @param fieldname the form parameter field name 
   * @return the java.io.File object
   */
    public File getFile(String fieldname) {
        if (keepFiles) return ((UploadedFile) files.get(fieldname)).getFile(); else return null;
    }

    /**
   * return an InputStream for reading an uploaded
   * file.
   * @param filename the form parameter field name
   * @return InputStream for reading the filedata.
   */
    public InputStream getInputStreamForFile(String fieldname) throws IOException {
        return ((UploadedFile) files.get(fieldname)).getInputStream();
    }

    /**
   * Copies an uploaded file into the specified file.
   * @param fieldname the form parameter field name
   * @param filename the name of the target
   */
    public void copyFile(String fieldname, String filename) throws IOException {
        InputStream in = ((UploadedFile) files.get(fieldname)).getInputStream();
        FileOutputStream out = new FileOutputStream(filename);
        byte[] buffer = new byte[2048];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }

    /**
   * return the names of all parameters as Enumeration
   * @return Enumeration of parameter names
   */
    public Enumeration getParameterNames() {
        return parameterValues.keys();
    }

    /** 
   * return the value of parameter with specified name
   * @param name the parameter name
   * @return the parameter value
   */
    public String getParameter(String name) {
        return (String) parameterValues.getFirst(name);
    }

    /**
   * return the value of the specified parameter as
   * an array of strings.
   */
    public String[] getParameterValues(String name) {
        int size = parameterValues.elementCount(name);
        String[] values = new String[size];
        int c = 0;
        for (Enumeration e = parameterValues.getElements(name); e.hasMoreElements(); c++) {
            values[c] = (String) e.nextElement();
        }
        return values;
    }

    /**
   * Get the UploadedFile object for the
   * uploaded file with given name.
   * @param fieldname the form parameter field name
   */
    public UploadedFile getUploadedFile(String fieldname) {
        return (UploadedFile) files.get(fieldname);
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public Object getAttribute(String name) {
        return servletRequest.getAttribute(name);
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public int getContentLength() {
        return servletRequest.getContentLength();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getContentType() {
        return servletRequest.getContentType();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getProtocol() {
        return servletRequest.getProtocol();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getScheme() {
        return servletRequest.getScheme();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getServerName() {
        return servletRequest.getServerName();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public int getServerPort() {
        return servletRequest.getServerPort();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getRemoteAddr() {
        return servletRequest.getRemoteAddr();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getRemoteHost() {
        return servletRequest.getRemoteHost();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getRealPath(String path) {
        return servletRequest.getRealPath(path);
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public ServletInputStream getInputStream() throws IOException {
        return servletRequest.getInputStream();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public BufferedReader getReader() throws IOException {
        return servletRequest.getReader();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getCharacterEncoding() {
        return servletRequest.getCharacterEncoding();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public Cookie[] getCookies() {
        return servletRequest.getCookies();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getMethod() {
        return servletRequest.getMethod();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getRequestURI() {
        return servletRequest.getRequestURI();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getServletPath() {
        return servletRequest.getServletPath();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getPathInfo() {
        return servletRequest.getPathInfo();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getQueryString() {
        return servletRequest.getQueryString();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getPathTranslated() {
        return servletRequest.getPathTranslated();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getRemoteUser() {
        return servletRequest.getRemoteUser();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getAuthType() {
        return servletRequest.getAuthType();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getHeader(String name) {
        return servletRequest.getHeader(name);
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public int getIntHeader(String name) {
        return servletRequest.getIntHeader(name);
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public long getDateHeader(String name) {
        return servletRequest.getDateHeader(name);
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public Enumeration getHeaderNames() {
        return servletRequest.getHeaderNames();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public HttpSession getSession(boolean create) {
        return servletRequest.getSession(create);
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public String getRequestedSessionId() {
        return servletRequest.getRequestedSessionId();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public boolean isRequestedSessionIdValid() {
        return servletRequest.isRequestedSessionIdValid();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public boolean isRequestedSessionIdFromCookie() {
        return servletRequest.isRequestedSessionIdFromCookie();
    }

    /**
   * Calls the corresponding method in ServletRequest-object.
   */
    public boolean isRequestedSessionIdFromUrl() {
        return servletRequest.isRequestedSessionIdFromUrl();
    }

    /**
   * Simple class for storing parameters
   */
    public class BucketHash extends Hashtable {

        public Object put(Object key, Object value) {
            Vector values;
            if (containsKey(key)) {
                values = (Vector) super.get(key);
            } else {
                values = new Vector();
                super.put(key, values);
            }
            values.addElement(value);
            return null;
        }

        public Object getFirst(Object key) {
            Vector values = (Vector) super.get(key);
            if (values != null) {
                return values.firstElement();
            }
            return null;
        }

        public int elementCount(Object key) {
            Vector values = (Vector) super.get(key);
            if (values != null) {
                return values.size();
            }
            return 0;
        }

        public Enumeration getElements(Object key) {
            Vector values = (Vector) super.get(key);
            if (values != null) {
                return values.elements();
            }
            return null;
        }
    }
}
