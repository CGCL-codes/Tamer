package malgnsoft.util.multipart;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletInputStream;

/** 
 * A utility class to handle <code>multipart/form-data</code> requests,
 * the kind of requests that support file uploads.  This class uses a 
 * "pull" model where the reading of incoming files and parameters is 
 * controlled by the client code, which allows incoming files to be stored 
 * into any <code>OutputStream</code>.  If you wish to use an API which 
 * resembles <code>HttpServletRequest</code>, use the "push" model 
 * <code>MultipartRequest</code> instead.  It's an easy-to-use wrapper 
 * around this class.
 * <p>
 * This class can receive arbitrarily large files (up to an artificial limit 
 * you can set), and fairly efficiently too.  
 * It cannot handle nested data (multipart content within multipart content).
 * It <b>can</b> now with the latest release handle internationalized content
 * (such as non Latin-1 filenames).
 * <p>
 * It also optionally includes enhanced buffering and Content-Length
 * limitation.  Buffering is only required if your servlet container is 
 * poorly implemented (many are, including Tomcat 3.2),
 * but it is generally recommended because it will make a slow servlet 
 * container a lot faster, and will only make a fast servlet container a 
 * little slower.  Content-Length limiting is usually only required if you find 
 * that your servlet is hanging trying to read the input stram from the POST, 
 * and it is similarly recommended because it only has a minimal impact on 
 * performance.
 * <p>
 * See the included upload.war for an example of how to use this class.
 * <p>
 * The full file upload specification is contained in experimental RFC 1867,
 * available at <a href="http://www.ietf.org/rfc/rfc1867.txt">
 * http://www.ietf.org/rfc/rfc1867.txt</a>.
 * 
 * 
 * @author Jason Hunter
 * @author Geoff Soutter
 * @version 1.11, 2002/11/01, added constructor that takes an encoding, to
 *                            make sure chars are always read correctly
 * @version 1.10, 2002/11/01, added support for a preamble before the first
 *                            boundary marker
 * @version 1.9, 2002/11/01, added support to parse odd Opera Content-Type
 * @version 1.8, 2002/11/01, added support for lynx with unquoted param vals
 * @version 1.7, 2002/04/30, fixed bug if a line was '\n' alone
 * @version 1.6, 2002/04/30, added better internationalization support, thanks
 *                           to Changshin Lee
 * @version 1.5, 2002/04/30, added Opera header fix, thanks to Nic Ferrier
 * @version 1.4, 2001/03/23, added IE5 bug workaround supporting \n as line
 *                           ending, thanks to Michael Alyn Miller
 * @version 1.3, 2001/01/22, added support for boundaries surrounded by quotes
 *                           and content-disposition after content-type,
 *                           thanks to Scott Stark
 * @version 1.2, 2001/01/22, getFilePath() support thanks to Stefan Eissing
 * @version 1.1, 2000/10/29, integrating old WebSphere fix
 * @version 1.0, 2000/10/27, initial revision
 */
public class MultipartParser {

    /** input stream to read parts from */
    private ServletInputStream in;

    /** MIME boundary that delimits parts */
    private String boundary;

    /** reference to the last file part we returned */
    private FilePart lastFilePart;

    /** buffer for readLine method */
    private byte[] buf = new byte[8 * 1024];

    /** default encoding */
    private static String DEFAULT_ENCODING = "ISO-8859-1";

    /** preferred encoding */
    private String encoding = DEFAULT_ENCODING;

    /**
   * Creates a <code>MultipartParser</code> from the specified request,
   * which limits the upload size to the specified length, buffers for 
   * performance and prevent attempts to read past the amount specified 
   * by the Content-Length.
   * 
   * @param req   the servlet request.
   * @param maxSize the maximum size of the POST content.
   */
    public MultipartParser(HttpServletRequest req, int maxSize) throws IOException {
        this(req, maxSize, true, true);
    }

    /**
   * Creates a <code>MultipartParser</code> from the specified request,
   * which limits the upload size to the specified length, and optionally 
   * buffers for performance and prevents attempts to read past the amount 
   * specified by the Content-Length. 
   * 
   * @param req   the servlet request.
   * @param maxSize the maximum size of the POST content.
   * @param buffer whether to do internal buffering or let the server buffer,
   *               useful for servers that don't buffer
   * @param limitLength boolean flag to indicate if we need to filter 
   *                    the request's input stream to prevent trying to 
   *                    read past the end of the stream.
   */
    public MultipartParser(HttpServletRequest req, int maxSize, boolean buffer, boolean limitLength) throws IOException {
        this(req, maxSize, buffer, limitLength, null);
    }

    /**
   * Creates a <code>MultipartParser</code> from the specified request,
   * which limits the upload size to the specified length, and optionally 
   * buffers for performance and prevents attempts to read past the amount 
   * specified by the Content-Length, and with a specified encoding. 
   * 
   * @param req   the servlet request.
   * @param maxSize the maximum size of the POST content.
   * @param buffer whether to do internal buffering or let the server buffer,
   *               useful for servers that don't buffer
   * @param limitLength boolean flag to indicate if we need to filter 
   *                    the request's input stream to prevent trying to 
   *                    read past the end of the stream.
   * @param encoding the encoding to use for parsing, default is ISO-8859-1.
   */
    public MultipartParser(HttpServletRequest req, int maxSize, boolean buffer, boolean limitLength, String encoding) throws IOException {
        if (encoding != null) {
            setEncoding(encoding);
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
        int length = req.getContentLength();
        if (length > maxSize) {
            throw new IOException("Posted content length of " + length + " exceeds limit of " + maxSize);
        }
        String boundary = extractBoundary(type);
        if (boundary == null) {
            throw new IOException("Separation boundary was not specified");
        }
        ServletInputStream in = req.getInputStream();
        if (buffer) {
            in = new BufferedServletInputStream(in);
        }
        if (limitLength) {
            in = new LimitedServletInputStream(in, length);
        }
        this.in = in;
        this.boundary = boundary;
        do {
            String line = readLine();
            if (line == null) {
                throw new IOException("Corrupt form data: premature ending");
            }
            if (line.startsWith(boundary)) {
                break;
            }
        } while (true);
    }

    /**
   * Sets the encoding used to parse from here onward.  The default is
   * ISO-8859-1.  Encodings are actually best passed into the contructor,
   * so even the initial line reads are correct.
   *
   * @param encoding The encoding to use for parsing
   */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
   * Read the next part arriving in the stream. Will be either a 
   * <code>FilePart</code> or a <code>ParamPart</code>, or <code>null</code>
   * to indicate there are no more parts to read. The order of arrival 
   * corresponds to the order of the form elements in the submitted form.
   * 
   * @return either a <code>FilePart</code>, a <code>ParamPart</code> or
   *        <code>null</code> if there are no more parts to read.
   * @exception IOException	if an input or output exception has occurred.
   * 
   * @see FilePart
   * @see ParamPart
   */
    public Part readNextPart() throws IOException {
        if (lastFilePart != null) {
            lastFilePart.getInputStream().close();
            lastFilePart = null;
        }
        Vector<String> headers = new Vector<String>();
        String line = readLine();
        if (line == null) {
            return null;
        } else if (line.length() == 0) {
            return null;
        }
        while (line != null && line.length() > 0) {
            String nextLine = null;
            boolean getNextLine = true;
            while (getNextLine) {
                nextLine = readLine();
                if (nextLine != null && (nextLine.startsWith(" ") || nextLine.startsWith("\t"))) {
                    line = line + nextLine;
                } else {
                    getNextLine = false;
                }
            }
            headers.addElement(line);
            line = nextLine;
        }
        if (line == null) {
            return null;
        }
        String name = null;
        String filename = null;
        String origname = null;
        String contentType = "text/plain";
        Enumeration e = headers.elements();
        while (e.hasMoreElements()) {
            String headerline = (String) e.nextElement();
            if (headerline.toLowerCase().startsWith("content-disposition:")) {
                String[] dispInfo = extractDispositionInfo(headerline);
                name = dispInfo[1];
                filename = dispInfo[2];
                origname = dispInfo[3];
            } else if (headerline.toLowerCase().startsWith("content-type:")) {
                String type = extractContentType(headerline);
                if (type != null) {
                    contentType = type;
                }
            }
        }
        if (filename == null) {
            return new ParamPart(name, in, boundary, encoding);
        } else {
            if (filename.equals("")) {
                filename = null;
            }
            lastFilePart = new FilePart(name, in, boundary, contentType, filename, origname);
            return lastFilePart;
        }
    }

    /**
   * Extracts and returns the boundary token from a line.
   * 
   * @return the boundary token.
   */
    private String extractBoundary(String line) {
        int index = line.lastIndexOf("boundary=");
        if (index == -1) {
            return null;
        }
        String boundary = line.substring(index + 9);
        if (boundary.charAt(0) == '"') {
            index = boundary.lastIndexOf('"');
            boundary = boundary.substring(1, index);
        }
        boundary = "--" + boundary;
        return boundary;
    }

    /**
   * Extracts and returns disposition info from a line, as a <code>String<code>
   * array with elements: disposition, name, filename.
   * 
   * @return String[] of elements: disposition, name, filename.
   * @exception  IOException if the line is malformatted.
   */
    private String[] extractDispositionInfo(String line) throws IOException {
        String[] retval = new String[4];
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
        int startOffset = 6;
        if (start == -1 || end == -1) {
            start = line.indexOf("name=", end);
            end = line.indexOf(";", start + 6);
            if (start == -1) {
                throw new IOException("Content disposition corrupt: " + origline);
            } else if (end == -1) {
                end = line.length();
            }
            startOffset = 5;
        }
        String name = origline.substring(start + startOffset, end);
        String filename = null;
        String origname = null;
        start = line.indexOf("filename=\"", end + 2);
        end = line.indexOf("\"", start + 10);
        if (start != -1 && end != -1) {
            filename = origline.substring(start + 10, end);
            origname = filename;
            int slash = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
            if (slash > -1) {
                filename = filename.substring(slash + 1);
            }
        }
        retval[0] = disposition;
        retval[1] = name;
        retval[2] = filename;
        retval[3] = origname;
        return retval;
    }

    /**
   * Extracts and returns the content type from a line, or null if the
   * line was empty.
   * 
   * @return content type, or null if line was empty.
   * @exception  IOException if the line is malformatted.
   */
    private static String extractContentType(String line) throws IOException {
        line = line.toLowerCase();
        int end = line.indexOf(";");
        if (end == -1) {
            end = line.length();
        }
        return line.substring(13, end).trim();
    }

    /**
   * Read the next line of input.
   * 
   * @return     a String containing the next line of input from the stream,
   *        or null to indicate the end of the stream.
   * @exception IOException	if an input or output exception has occurred.
   */
    private String readLine() throws IOException {
        StringBuffer sbuf = new StringBuffer();
        int result;
        String line;
        do {
            result = in.readLine(buf, 0, buf.length);
            if (result != -1) {
                sbuf.append(new String(buf, 0, result, encoding));
            }
        } while (result == buf.length);
        if (sbuf.length() == 0) {
            return null;
        }
        int len = sbuf.length();
        if (len >= 2 && sbuf.charAt(len - 2) == '\r') {
            sbuf.setLength(len - 2);
        } else if (len >= 1 && sbuf.charAt(len - 1) == '\n') {
            sbuf.setLength(len - 1);
        }
        return sbuf.toString();
    }
}
