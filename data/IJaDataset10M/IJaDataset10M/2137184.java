package com.shared.beans;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/** 
 * A utility class to handle <tt>multipart/form-data</tt> requests,
 * the kind of requests that support file uploads.  This class can 
 * receive arbitrarily large files (up to an artificial limit you can set),
 * and fairly efficiently too.  
 * It cannot handle nested data (multipart content within multipart content)
 * or internationalized content (such as non Latin-1 filenames).
 * <p>
 * It's used like this:
 * <blockquote><pre>
 * MultipartRequest multi = new MultipartRequest(req, ".");
 * &nbsp;
 * out.println("Params:");
 * Enumeration params = multi.getParameterNames();
 * while (params.hasMoreElements()) {
 *   String name = (String)params.nextElement();
 *   String value = multi.getParameter(name);
 *   out.println(name + " = " + value);
 * }
 * out.println();
 * &nbsp;
 * out.println("Files:");
 * Enumeration files = multi.getFileNames();
 * while (files.hasMoreElements()) {
 *   String name = (String)files.nextElement();
 *   String filename = multi.getFilesystemName(name);
 *   String type = multi.getContentType(name);
 *   File f = multi.getFile(name);
 *   out.println("name: " + name);
 *   out.println("filename: " + filename);
 *   out.println("type: " + type);
 *   if (f != null) {
 *     out.println("f.toString(): " + f.toString());
 *     out.println("f.getName(): " + f.getName());
 *     out.println("f.exists(): " + f.exists());
 *     out.println("f.length(): " + f.length());
 *     out.println();
 *   }
 * }
 * </pre></blockquote>
 *
 * A client can upload files using an HTML form with the following structure.
 * Note that not all browsers support file uploads.
 * <blockquote><pre>
 * &lt;FORM ACTION="/servlet/Handler" METHOD=POST
 *          ENCTYPE="multipart/form-data"&gt;
 * What is your name? &lt;INPUT TYPE=TEXT NAME=submitter&gt; &lt;BR&gt;
 * Which file to upload? &lt;INPUT TYPE=FILE NAME=file&gt; &lt;BR&gt;
 * &lt;INPUT TYPE=SUBMIT&GT;
 * &lt;/FORM&gt;
 * </pre></blockquote>
 * <p>
 * The full file upload specification is contained in experimental RFC 1867,
 * available at <a href="http://www.ietf.org/rfc/rfc1867.txt">
 * http://www.ietf.org/rfc/rfc1867.txt</a>.
 */
public class MultipartRequest {

    private static final int DEFAULT_MAX_POST_SIZE = 5 * 1024 * 1024;

    private static final String NO_FILE = "unknown";

    private HttpServletRequest req;

    private File dir;

    private int maxSize;

    private Hashtable parameters = new Hashtable();

    private Hashtable files = new Hashtable();

    /**
   * Constructs a new MultipartRequest to handle the specified request, 
   * saving any uploaded files to the given directory, and limiting the 
   * upload size to 1 Megabyte.  If the content is too large, an
   * IOException is thrown.  This constructor actually parses the 
   * <tt>multipart/form-data</tt> and throws an IOException if there's any 
   * problem reading or parsing the request.
   *
   * @param request the servlet request
   * @param saveDirectory the directory in which to save any uploaded files
   * @exception IOException if the uploaded content is larger than 1 Megabyte
   * or there's a problem reading or parsing the request
   */
    public MultipartRequest(HttpServletRequest request, String saveDirectory) throws IOException {
        this(request, saveDirectory, DEFAULT_MAX_POST_SIZE);
    }

    /**
   * Constructs a new MultipartRequest to handle the specified request, 
   * saving any uploaded files to the given directory, and limiting the 
   * upload size to the specified length.  If the content is too large, an 
   * IOException is thrown.  This constructor actually parses the 
   * <tt>multipart/form-data</tt> and throws an IOException if there's any 
   * problem reading or parsing the request.
   *
   * @param request the servlet request
   * @param saveDirectory the directory in which to save any uploaded files
   * @param maxPostSize the maximum size of the POST content
   * @exception IOException if the uploaded content is larger than 
   * <tt>maxPostSize</tt> or there's a problem reading or parsing the request
   */
    public MultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize) throws IOException {
        if (request == null) throw new IllegalArgumentException("request cannot be null");
        if (saveDirectory == null) throw new IllegalArgumentException("saveDirectory cannot be null");
        if (maxPostSize <= 0) {
            throw new IllegalArgumentException("maxPostSize must be positive");
        }
        req = request;
        dir = new File(saveDirectory);
        maxSize = maxPostSize;
        if (!dir.isDirectory()) throw new IllegalArgumentException("Not a directory: " + saveDirectory);
        if (!dir.canWrite()) throw new IllegalArgumentException("Not writable: " + saveDirectory);
        readRequest();
    }

    public File getTempDirectory() {
        return dir;
    }

    /**
   * Constructor with an old signature, kept for backward compatibility.
   * Without this constructor, a servlet compiled against a previous version 
   * of this class (pre 1.4) would have to be recompiled to link with this 
   * version.  This constructor supports the linking via the old signature.
   * Callers must simply be careful to pass in an HttpServletRequest.
   * 
   */
    public MultipartRequest(ServletRequest request, String saveDirectory) throws IOException {
        this((HttpServletRequest) request, saveDirectory);
    }

    /**
   * Constructor with an old signature, kept for backward compatibility.
   * Without this constructor, a servlet compiled against a previous version 
   * of this class (pre 1.4) would have to be recompiled to link with this 
   * version.  This constructor supports the linking via the old signature.
   * Callers must simply be careful to pass in an HttpServletRequest.
   * 
   */
    public MultipartRequest(ServletRequest request, String saveDirectory, int maxPostSize) throws IOException {
        this((HttpServletRequest) request, saveDirectory, maxPostSize);
    }

    public Map getParameterMap() {
        Map paramMap = new HashMap();
        Enumeration keys = getParameterNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = getParameter(key);
            paramMap.put(key, value);
        }
        return paramMap;
    }

    /**
   * Returns the names of all the parameters as an Enumeration of 
   * Strings.  It returns an empty Enumeration if there are no parameters.
   *
   * @return the names of all the parameters as an Enumeration of Strings
   */
    public Enumeration getParameterNames() {
        return parameters.keys();
    }

    /**
   * Returns the names of all the uploaded files as an Enumeration of 
   * Strings.  It returns an empty Enumeration if there are no uploaded 
   * files.  Each file name is the name specified by the form, not by 
   * the user.
   *
   * @return the names of all the uploaded files as an Enumeration of Strings
   */
    public Enumeration getFileNames() {
        return files.keys();
    }

    /**
   * Returns the value of the named parameter as a String, or null if 
   * the parameter was not sent or was sent without a value.  The value 
   * is guaranteed to be in its normal, decoded form.  If the parameter 
   * has multiple values, only the last one is returned (for backward 
   * compatibility).  For parameters with multiple values, it's possible
   * the last "value" may be null.
   *
   * @param name the parameter name
   * @return the parameter value
   */
    public String getParameter(String name) {
        try {
            Vector values = (Vector) parameters.get(name);
            if (values == null || values.size() == 0) {
                return null;
            }
            String value = (String) values.elementAt(values.size() - 1);
            if ("undefined".equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
   * Returns the values of the named parameter as a String array, or null if 
   * the parameter was not sent.  The array has one entry for each parameter 
   * field sent.  If any field was sent without a value that entry is stored 
   * in the array as a null.  The values are guaranteed to be in their 
   * normal, decoded form.  A single value is returned as a one-element array.
   *
   * @param name the parameter name
   * @return the parameter values
   */
    public String[] getParameterValues(String name) {
        try {
            Vector values = (Vector) parameters.get(name);
            if (values == null || values.size() == 0) {
                return null;
            }
            String[] valuesArray = new String[values.size()];
            values.copyInto(valuesArray);
            return valuesArray;
        } catch (Exception e) {
            return null;
        }
    }

    /**
   * Returns the filesystem name of the specified file, or null if the 
   * file was not included in the upload.  A filesystem name is the name 
   * specified by the user.  It is also the name under which the file is 
   * actually saved.
   *
   * @param name the file name
   * @return the filesystem name of the file
   */
    public String getFilesystemName(String name) {
        try {
            UploadedFile file = (UploadedFile) files.get(name);
            return file.getFilesystemName();
        } catch (Exception e) {
            return null;
        }
    }

    public String getFullLocalName(String name) {
        try {
            UploadedFile file = (UploadedFile) files.get(name);
            return file.getFile().toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
   * Returns the content type of the specified file (as supplied by the 
   * client browser), or null if the file was not included in the upload.
   *
   * @param name the file name
   * @return the content type of the file
   */
    public String getContentType(String name) {
        try {
            UploadedFile file = (UploadedFile) files.get(name);
            return file.getContentType();
        } catch (Exception e) {
            return null;
        }
    }

    /**
   * Returns a File object for the specified file saved on the server's 
   * filesystem, or null if the file was not included in the upload.
   *
   * @param name the file name
   * @return a File object for the named file
   */
    public File getFile(String name) {
        try {
            UploadedFile file = (UploadedFile) files.get(name);
            return file.getFile();
        } catch (Exception e) {
            return null;
        }
    }

    /**
   * The workhorse method that actually parses the request.  A subclass 
   * can override this method for a better optimized or differently
   * behaved implementation.
   *
   * @exception IOException if the uploaded content is larger than 
   * <tt>maxSize</tt> or there's a problem parsing the request
   */
    protected void readRequest() throws IOException {
        int length = req.getContentLength();
        if (length > maxSize) {
            throw new IOException("Posted content length of " + length + " exceeds limit of " + maxSize);
        }
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
            throw new IOException("Posted content type isn't multipart/form-data");
        }
        String boundary = extractBoundary(type);
        if (boundary == null) {
            throw new IOException("Separation boundary was not specified");
        }
        MultipartInputStreamHandler in = new MultipartInputStreamHandler(req.getInputStream(), length);
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

    /**
   * A utility method that reads an individual part.  Dispatches to 
   * readParameter() and readAndSaveFile() to do the actual work.  A 
   * subclass can override this method for a better optimized or 
   * differently behaved implementation.
   * 
   * @param in the stream from which to read the part
   * @param boundary the boundary separating parts
   * @return a flag indicating whether this is the last part
   * @exception IOException if there's a problem reading or parsing the
   * request
   *
   * @see readParameter
   * @see readAndSaveFile
   */
    protected boolean readNextPart(MultipartInputStreamHandler in, String boundary) throws IOException {
        String line = in.readLine();
        if (line == null) {
            return true;
        } else if (line.length() == 0) {
            return true;
        }
        String[] dispInfo = extractDispositionInfo(line);
        String disposition = dispInfo[0];
        String name = dispInfo[1];
        String filename = dispInfo[2];
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
        if (filename == null) {
            String value = readParameter(in, boundary);
            if (value.equals("")) {
                value = null;
            }
            Vector existingValues = (Vector) parameters.get(name);
            if (existingValues == null) {
                existingValues = new Vector();
                parameters.put(name, existingValues);
            }
            existingValues.addElement(value);
        } else {
            filename = filename.replaceAll(" ", "");
            readAndSaveFile(in, boundary, filename, contentType);
            if (filename.equals(NO_FILE)) {
                files.put(name, new UploadedFile(null, null, null));
            } else {
                files.put(name, new UploadedFile(dir.toString(), filename, contentType));
            }
        }
        return false;
    }

    /**
   * A utility method that reads a single part of the multipart request 
   * that represents a parameter.  A subclass can override this method 
   * for a better optimized or differently behaved implementation.
   *
   * @param in the stream from which to read the parameter information
   * @param boundary the boundary signifying the end of this part
   * @return the parameter value
   * @exception IOException if there's a problem reading or parsing the 
   * request
   */
    protected String readParameter(MultipartInputStreamHandler in, String boundary) throws IOException {
        StringBuffer sbuf = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            if (line.startsWith(boundary)) break;
            sbuf.append(line + "\r\n");
        }
        if (sbuf.length() == 0) {
            return null;
        }
        sbuf.setLength(sbuf.length() - 2);
        return sbuf.toString();
    }

    /**
   * A utility method that reads a single part of the multipart request 
   * that represents a file, and saves the file to the given directory.
   * A subclass can override this method for a better optimized or 
   * differently behaved implementation.
   *
   * @param in the stream from which to read the file
   * @param boundary the boundary signifying the end of this part
   * @param dir the directory in which to save the uploaded file
   * @param filename the name under which to save the uploaded file
   * @exception IOException if there's a problem reading or parsing the 
   * request
   */
    protected void readAndSaveFile(MultipartInputStreamHandler in, String boundary, String filename, String contentType) throws IOException {
        OutputStream out = null;
        if (filename.equals(NO_FILE)) {
            out = new ByteArrayOutputStream();
        } else if (contentType.equals("application/x-macbinary")) {
            File f = new File(dir + File.separator + filename);
            out = new MacBinaryDecoderOutputStream(new BufferedOutputStream(new FileOutputStream(f), 8 * 1024));
        } else {
            File f = new File(dir + File.separator + filename);
            out = new BufferedOutputStream(new FileOutputStream(f), 8 * 1024);
        }
        byte[] bbuf = new byte[100 * 1024];
        int result;
        String line;
        boolean rnflag = false;
        while ((result = in.readLine(bbuf, 0, bbuf.length)) != -1) {
            if (result > 2 && bbuf[0] == '-' && bbuf[1] == '-') {
                line = new String(bbuf, 0, result, "ISO-8859-1");
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
        out.flush();
        out.close();
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
            if (filename.equals("")) filename = NO_FILE;
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
}

class UploadedFile {

    private String dir;

    private String filename;

    private String type;

    UploadedFile(String dir, String filename, String type) {
        this.dir = dir;
        this.filename = filename;
        this.type = type;
    }

    public String getContentType() {
        return type;
    }

    public String getFilesystemName() {
        return filename;
    }

    public File getFile() {
        if (dir == null || filename == null) {
            return null;
        } else {
            return new File(dir + File.separator + filename);
        }
    }
}

class MultipartInputStreamHandler {

    ServletInputStream in;

    int totalExpected;

    int totalRead = 0;

    byte[] buf = new byte[8 * 1024];

    public MultipartInputStreamHandler(ServletInputStream in, int totalExpected) {
        this.in = in;
        this.totalExpected = totalExpected;
    }

    public String readLine() throws IOException {
        StringBuffer sbuf = new StringBuffer();
        int result;
        String line;
        do {
            result = this.readLine(buf, 0, buf.length);
            if (result != -1) {
                sbuf.append(new String(buf, 0, result, "ISO-8859-1"));
            }
        } while (result == buf.length);
        if (sbuf.length() == 0) {
            return null;
        }
        sbuf.setLength(sbuf.length() - 2);
        return sbuf.toString();
    }

    public int readLine(byte b[], int off, int len) throws IOException {
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

class MacBinaryDecoderOutputStream extends FilterOutputStream {

    int bytesFiltered = 0;

    int dataForkLength = 0;

    public MacBinaryDecoderOutputStream(OutputStream out) {
        super(out);
    }

    public void write(int b) throws IOException {
        if (bytesFiltered <= 86 && bytesFiltered >= 83) {
            int leftShift = (86 - bytesFiltered) * 8;
            dataForkLength = dataForkLength | (b & 0xff) << leftShift;
        } else if (bytesFiltered < (128 + dataForkLength) && bytesFiltered >= 128) {
            out.write(b);
        }
        bytesFiltered++;
    }

    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        if (bytesFiltered >= (128 + dataForkLength)) {
            bytesFiltered += len;
        } else if (bytesFiltered >= 128 && (bytesFiltered + len) <= (128 + dataForkLength)) {
            out.write(b, off, len);
            bytesFiltered += len;
        } else {
            for (int i = 0; i < len; i++) {
                write(b[off + i]);
            }
        }
    }
}
