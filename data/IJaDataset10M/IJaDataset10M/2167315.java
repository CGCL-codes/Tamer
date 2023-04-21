package com.worldware.web;

import java.net.*;
import java.io.*;
import java.util.*;
import java.net.HttpURLConnection;
import javax.servlet.http.*;
import com.worldware.mail.*;
import com.worldware.misc.*;
import com.worldware.interfaces.*;
import com.worldware.servlet.http.*;
import com.worldware.tcp.*;

/** Basic servlet engine. In the process of being converted from a different architecture to be servlet based. 
 * One instance is created per request.
 **/
public class Trellis extends Thread {

    static boolean verbose = false;

    /** The file the access log will be written to. May be null, which means no logging.
	 */
    static LogFile m_accessLog;

    Socket m_connection;

    /** True means examine the HOST parameter in the HTTP request, to distinguish between
	 * multiple hosts.
	 */
    static boolean m_useHostParam = false;

    /** Title for all documents returned. Probably want to be able to vary this by host */
    String m_title = "Trellis<sup>TM</sup> Web Server";

    /** Title for all documents returned. Probably want to be able to vary this by host */
    String m_headInfo = "";

    /** true means echo all query parameters back along with the response */
    boolean m_debugPost = true;

    /** Base64 decode object */
    static Base64 m_b64 = new Base64();

    /** The path to the servlet. Generally only used for debugging
	 * @see setServletPath
	 */
    static String m_servletPath = "/";

    /** the file to serve .htm & .jpgs from
	 */
    File m_baseDir;

    /** Constructor */
    public Trellis(Socket s, TCPServerInterface tcpif, File baseDir) {
        m_connection = s;
        m_baseDir = baseDir;
    }

    String getTitle() {
        return m_title;
    }

    protected void setTitle(String newTitle) {
        m_title = newTitle;
    }

    void setHeadInfo(String newData) {
        m_headInfo = newData;
    }

    public void run() {
        Date now = new MyDate();
        Hashtable headers = new Hashtable();
        String method;
        String protocolVersion = "";
        InetAddress remoteAddress = m_connection.getInetAddress();
        InetAddress thisHost = null;
        try {
            thisHost = InetAddress.getLocalHost();
        } catch (UnknownHostException uhe) {
        }
        ;
        try {
            PrintStream os = new PrintStream(m_connection.getOutputStream());
            DataInputStream is = new DataInputStream(m_connection.getInputStream());
            String get = is.readLine();
            if (verbose) System.out.println("REQ->" + get);
            if ((get == null) || (get.trim().length() == 0)) return;
            StringTokenizer st = new StringTokenizer(get);
            method = st.nextToken();
            if (method.equals("GET") || method.equals("POST")) {
                String fullURL = st.nextToken();
                if (st.hasMoreTokens()) {
                    protocolVersion = st.nextToken();
                }
                while ((get = is.readLine()) != null) {
                    int i = get.indexOf(':');
                    if (i != -1) {
                        String s = get.substring(0, i).toLowerCase();
                        if (get.length() > s.length() + 2) {
                            String q = new String(get.substring(i + 2));
                            headers.put(s, q);
                            if (verbose) System.out.println("HEAD: " + s + ": " + q);
                        } else {
                            Log.error("Trellis.java: Header with no value '" + get + "'");
                            headers.put(s, "{null}");
                        }
                    }
                    if (get.trim().equals("")) break;
                }
                HttpServletRequest req;
                try {
                    req = createRequest(protocolVersion, method, headers, fullURL, is, remoteAddress);
                } catch (Exception e) {
                    sendError(os, 501, e);
                    return;
                } catch (java.lang.LinkageError le) {
                    sendError(os, 501, le);
                    return;
                }
                String URI = req.getRequestURI();
                log(req.getRemoteAddr(), now, req.getMethod(), URI, req.getQueryString(), req.getHeader("User-Agent"));
                if (URI.endsWith(".jpg") || URI.endsWith(".gif") || URI.endsWith(".htm") || URI.endsWith(".css")) {
                    serveFile(URI, os, protocolVersion, req);
                    return;
                }
                if (URI.endsWith(".php")) {
                    serveTemplate(URI, os, protocolVersion, req);
                    return;
                }
                HttpServletResponse res = createResponse(os);
                service(req, res);
            } else {
                if (protocolVersion.startsWith("HTTP/")) {
                    os.print("HTTP/1.0 501 Not Implemented\r\n");
                    os.print("Date: " + now + "\r\n");
                    os.print("Server: Trellis 1.0\r\n");
                    os.print("Content-type: text/html" + "\r\n\r\n");
                }
                os.println("<HTML><HEAD><TITLE>Not Implemented</TITLE></HEAD>");
                os.println("<BODY><H1>HTTP Error 501: Method '" + method + "' has not been implemented for this URL.</H1></BODY></HTML>");
                os.close();
            }
        } catch (IOException ioe) {
            Log.error("Trellis.java: IOexception: " + ioe);
            ioe.printStackTrace();
        } catch (Exception e) {
            Log.error("Trellis.java: Exception: " + e);
            e.printStackTrace();
        }
        try {
            m_connection.close();
        } catch (IOException e) {
        }
    }

    /** Creates the HttpServletRequest structure that gets passed to the servlet.
	 */
    public static HttpServletRequest createRequest(String protocolVersion, String method, Hashtable headers, String orgURL, InputStream is, InetAddress remoteAddress) throws HTTPException, MalformedURLException, IOException {
        String localURL;
        if (orgURL.startsWith(m_servletPath)) {
            localURL = orgURL.substring(m_servletPath.length());
            if (!localURL.startsWith("/")) localURL = "/" + localURL;
        } else localURL = orgURL;
        return new wwHttpServletRequest(method, headers, localURL, is, m_servletPath, remoteAddress);
    }

    /** Creates the HttpServletResponse structure that gets passed to the servlet.
	 */
    public HttpServletResponse createResponse(OutputStream os) throws HTTPException, MalformedURLException {
        return new wwHttpServletResponse(this, os);
    }

    /** Gets a file from the current directory of the server process
	  */
    void serveFile(String localURL, PrintStream os, String version, HttpServletRequest req) {
        File imageFile = serveFile2(localURL, os, version, req);
        if (imageFile != null) copyFile(imageFile, os);
        os.close();
    }

    /** Gets a file from the current directory of the server process
	  */
    void serveTemplate(String localURL, PrintStream os, String version, HttpServletRequest req) {
        File imageFile = serveFile2(localURL, os, version, req);
        String s = loadFile(imageFile);
        com.worldware.misc.StringTemplate st = new com.worldware.misc.StringTemplate("<%", "%>");
        Hashtable h = new Hashtable();
        h.put("progname", "Ichabod");
        s = st.replace(s, h);
        os.print(s);
        os.close();
    }

    File serveFile2(String localURL, PrintStream os, String version, HttpServletRequest req) {
        File baseDir = m_baseDir;
        if (m_useHostParam) {
            String hostName = req.getHeader("HOST");
            if (hostName != null) baseDir = new File(baseDir, hostName);
        }
        String contentType = WebTools.guessContentType(localURL);
        String fname = localURL;
        int index = fname.indexOf("/");
        if ((index != -1) && (index < fname.length() - 1)) fname = fname.substring(index + 1);
        File imageFile = new File(baseDir, fname);
        if (!accessible(imageFile, baseDir)) {
            sendError(os, java.net.HttpURLConnection.HTTP_NOT_FOUND, "File not found", "File " + imageFile + " is not accessible.");
            return null;
        }
        if (!imageFile.exists()) {
            sendError(os, java.net.HttpURLConnection.HTTP_NOT_FOUND, "File not found", "File " + imageFile + " Not Found");
            return null;
        }
        Date lastMod = new Date(imageFile.lastModified());
        long ifmodlong = -1;
        try {
            ifmodlong = req.getDateHeader(wwHttpServletRequest.H_IF_MODIFIED_SINCE);
        } catch (IllegalArgumentException iae) {
            System.out.println(req.toString());
            ifmodlong = -1;
        }
        if (ifmodlong != -1) {
            Date ifMod = new Date(ifmodlong);
            if (verbose) System.out.println("Req: " + ifMod + " mod: " + lastMod);
            if (lastMod.getTime() < ifMod.getTime()) {
                this.sendResponseCode(os, java.net.HttpURLConnection.HTTP_NOT_MODIFIED, "Unchanged");
                return null;
            }
        }
        os.print("HTTP/1.0 " + java.net.HttpURLConnection.HTTP_ACCEPTED + " OK\r\n");
        Date now = new MyDate();
        Date expires = new MyDate(now.getTime() + 1000 * 60 * 60 * 3);
        String expiresString = W3CDate.format(expires);
        String nowString = W3CDate.format(now);
        String lastModString = W3CDate.format(lastMod);
        os.print("Date: " + nowString + "\r\n");
        os.print("Last-Modified: " + lastModString + "\r\n");
        os.print("Expires: " + expiresString + "\r\n");
        os.print("Server: Trellis 1.0\r\n");
        os.print("Content-length: " + imageFile.length() + "\r\n");
        os.print("Weird-Header: Just testing\r\n");
        os.print("Content-type: " + contentType + "\r\n\r\n");
        return imageFile;
    }

    /** Copies from a file to a stream.
	* Is there a system method to do this?
	*/
    public static boolean copyFile(File oldFile, OutputStream os) {
        if (!oldFile.canRead()) {
            Log.error("Trellis.java: Can't read file: " + oldFile.getAbsolutePath());
            return false;
        }
        FileInputStream fis = null;
        byte[] userData = new byte[16384];
        try {
            fis = new FileInputStream(oldFile);
            int bytesRead = -1;
            while (-1 != (bytesRead = fis.read(userData))) {
                try {
                    os.write(userData, 0, bytesRead);
                } catch (FileNotFoundException fnf) {
                    Log.error("Trellis: Error opening output stream" + fnf);
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            Log.error("Trellis: file " + oldFile.getPath() + " not found");
            return false;
        } catch (IOException e) {
            Log.error("Exception occured when reading file " + oldFile.getPath() + " exception:" + e);
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {
                    Log.error("Trellis.CopyFile: Error closing file");
                }
            }
        }
        return true;
    }

    /** Loads a file into a String
	*/
    public static String loadFile(File oldFile) {
        StringBuffer sb = new StringBuffer(16384);
        if (!oldFile.canRead()) {
            Log.error("Trellis.java: Can't read file: " + oldFile.getAbsolutePath());
            return null;
        }
        FileReader fis = null;
        char[] userData = new char[16384];
        try {
            fis = new FileReader(oldFile);
            int bytesRead = -1;
            while (-1 != (bytesRead = fis.read(userData))) {
                sb.append(userData, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            Log.error("Trellis: file " + oldFile.getPath() + " not found");
            return null;
        } catch (IOException e) {
            Log.error("Exception occured when reading file " + oldFile.getPath() + " exception:" + e);
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {
                    Log.error("Trellis.CopyFile: Error closing file");
                }
            }
        }
        return sb.toString();
    }

    /** Loads a stream into a String.
	 * Caller closes stream.
	*/
    public static String loadStream(InputStream is) {
        StringBuffer sb = new StringBuffer(16384);
        char[] userData = new char[16384];
        try {
            InputStreamReader fis = new InputStreamReader(is);
            int bytesRead = -1;
            while (-1 != (bytesRead = fis.read(userData))) {
                sb.append(userData, 0, bytesRead);
            }
        } catch (IOException e) {
            Log.error("Trellis.java:loadStream: Exception occured when reading. " + e);
            return "Resource not readable.";
        }
        return sb.toString();
    }

    private String getDataHeader(String title) throws IOException {
        String s = new String("<html>\r\n" + "<head>\r\n" + "<title>" + title + "</title>\r\n" + getAdditionalHeadInfo() + "</head>\r\n" + "<body background=\"/background.gif\" bgcolor=#C0C0C0>");
        return s;
    }

    String getAdditionalHeadInfo() throws IOException {
        return m_headInfo;
    }

    String getDataFooter() throws IOException {
        String s = new String("</body>" + "</html>");
        return s;
    }

    /** Takes a vector of strings, and turns in into an HTML Unnumbered list
	  * right now, this is only used for lists of email addresses. To prevent 
	  * spam, put some html tags in the middle of the address
	  */
    public static String getVectorAsList(Vector v) {
        Enumeration e = v.elements();
        return getStringEnumAsList(e);
    }

    /** Takes a vector of strings, and turns in into an HTML Unnumbered list
	  * right now, this is only used for lists of email addresses. To prevent 
	  * spam, put some html tags in the middle of the address
	  */
    public static String getStringEnumAsList(Enumeration e) {
        if (!e.hasMoreElements()) return ("Empty list<P>");
        StringBuffer sb = new StringBuffer(2048);
        sb.append("<UL>\r\n");
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            int ln = name.length();
            if (ln > 3) {
                sb.append("<LI><B>");
                sb.append(name.charAt(0));
                sb.append("</B>");
                sb.append(name.substring(1, ln - 1));
                sb.append("<B>");
                sb.append(name.charAt(ln - 1));
                sb.append("</B>\r\n");
            } else sb.append("<LI>\r\n" + name + "\r\n");
        }
        sb.append("</UL>\r\n");
        return sb.toString();
    }

    /** decode a query string, changing '+' to ' ', change %nn to ascii(nn)
	 * @see encodeQuery
	 **/
    void getData(HttpServletRequest req, PrintStream ps, String version) throws IOException {
    }

    /** Returns the specified Response code, without any body. Used for 304, etc.
			All 1xx (informational), 204 (no content), and 304 (not modified) responses
			MUST NOT include a message-body. All other responses do include a
			message-body, although it MAY be of zero length. 
	*/
    public static void sendResponseCode(PrintStream os, int code, String desc) {
        os.print("HTTP/1.0 " + code + " " + desc + "\r\n");
        Date now = new MyDate();
        os.print("Date: " + now + "\r\n");
        os.print("Server: Trellis 1.0\r\n");
        os.print("WWW-Authenticate: Basic realm=\"MailServerAdmin\"\r\n");
        os.print("\r\n");
        os.close();
    }

    public static void sendRedirect(PrintStream os, int code, String desc, String location) {
        os.print("HTTP/1.0 " + code + " " + desc + "\r\n");
        Date now = new MyDate();
        os.print("Date: " + now + "\r\n");
        os.print("Server: Trellis 1.0\r\n");
        os.print("Location: " + location + "\r\n");
        os.print("WWW-Authenticate: Basic realm=\"MailServerAdmin\"\r\n");
        os.print("\r\n");
        os.close();
    }

    public static void sendError(PrintStream os, int errcode, Throwable t) {
        String stackTrace = "";
        {
            OutputStream eos = new ByteArrayOutputStream(1024);
            t.printStackTrace(new PrintStream(eos));
            stackTrace = "<PRE>\r\n" + eos.toString() + "\r\n</PRE>\r\n";
        }
        sendError(os, errcode, "ERROR", stackTrace);
    }

    /** Returns the specified error code */
    public static void sendError(PrintStream os, int errcode, String errDesc, String longDesc) {
        os.print("HTTP/1.0 " + errcode + " " + errDesc + "\r\n");
        Date now = new MyDate();
        os.print("Date: " + now + "\r\n");
        os.print("Server: Trellis 1.0\r\n");
        os.print("WWW-Authenticate: Basic realm=\"MailServerAdmin\"\r\n");
        os.print("Content-type: text/html" + "\r\n\r\n");
        os.println("<HTML><HEAD><TITLE>File Not Found</TITLE></HEAD>");
        os.println("<BODY><H1>HTTP Error " + errcode + ":" + errDesc + "</H1>" + longDesc + "</BODY></HTML>");
        os.close();
    }

    /** Note: Overridden in TrellisBlack
	 */
    public void service(HttpServletRequest req, HttpServletResponse res) {
        String s = this.getClass().getName();
        PrintStream ps;
        try {
            ps = new PrintStream(m_connection.getOutputStream());
        } catch (IOException ioe) {
            return;
        }
        String url = req.getPathInfo();
        if (url.equals("/")) url = "/index.htm";
        String protocolVersion = req.getProtocol();
        serveFile(url, ps, protocolVersion, req);
    }

    /** Writes the basic HTTP header followed by a blank line.
	 * <P> this is a hack, fix it. ZZZ should handle a hashtable of headers.
	 * Should this be in the servlet response class?
	 */
    public static void writeHTTPHeader(PrintStream os, int rc, String rcText, int contentLength, String contentType) {
        os.print("HTTP/1.0 " + rc + " " + rcText + "\r\n");
        Date now = new MyDate();
        os.print("Date: " + now + "\r\n");
        os.print("Server: Trellis 1.0\r\n");
        if (contentLength != -1) os.print("Content-length: " + contentLength + "\r\n");
        os.print("Content-type: " + contentType + "\r\n\r\n");
    }

    /** Sets the path to the WebUI. This is really only so I can 
	 * more easily simulate the way things will work in apache, while
	 * running under my servlet code (easier to debug).
	 */
    public static void setServletPath(String path) {
        m_servletPath = path;
    }

    /** @deprecated Moved to WebTools.java
	 * @see WebTools
	 */
    public static String appendURL(String URLbase, String URLnew) {
        return WebTools.appendURL(URLbase, URLnew);
    }

    public static void main(String args[]) throws IOException {
        final String IPKey = "ip=";
        final String dirKey = "dir=";
        java.net.InetAddress webIPAddress = null;
        System.out.println("Starting Trellis at " + new Date());
        File baseDir = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("host")) m_useHostParam = true; else if (args[i].startsWith(IPKey)) {
                String ip = args[i].substring(IPKey.length());
                System.out.println("Using IPAddress '" + ip + "'");
                try {
                    InetAddress iaddr = InetAddress.getByName(ip);
                    webIPAddress = iaddr;
                    System.out.println("IPAddress resolves to '" + iaddr.toString() + "'");
                } catch (java.net.UnknownHostException uhe) {
                    System.out.println("ERROR: Could not resolve host ip address '" + ip + "'");
                    return;
                }
            } else if (args[i].startsWith(dirKey)) {
                baseDir = new File(args[i].substring(dirKey.length()));
            } else {
                System.out.println("Unknown command line option ( valid are " + dirKey + " and " + IPKey + ")");
            }
        }
        if (baseDir == null) baseDir = new File((String) System.getProperties().get("user.dir"));
        System.out.println("Using directory '" + baseDir + "'");
        setLog(new File(baseDir, "access.txt"));
        new HTTPServerListenerBase(null, 80, webIPAddress, baseDir, new LogSimple());
    }

    /** Check to see that it is ok to give the user the file. (THey are not allowed to get ../../privatefile.html)
	 */
    boolean accessible(File requested, File baseDir) {
        try {
            String req2 = requested.getCanonicalPath();
            String base2 = baseDir.getCanonicalPath();
            if (req2.length() < base2.length()) {
                System.out.println(req2 + "(" + req2.length() + ")\nis shorter than\n" + base2 + "(" + base2.length() + ")");
                return false;
            }
            if (!(req2.startsWith(base2))) {
                System.out.println(req2 + " does not have " + base2 + " as a prefix");
                return false;
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
            return false;
        }
        return true;
    }

    /** Sets the file to write the access log to. You must call this 
	 * method for any logging to take place, since the default is no log.
	 */
    public static void setLog(File accessLog) throws IOException {
        if (accessLog.exists()) {
            if (!accessLog.canWrite()) throw new IOException("Can't write to access log '" + accessLog + "'");
        } else ;
        m_accessLog = new LogFile(accessLog);
    }

    static void log(String remoteAddress, Date now, String method, String URI, String query, String userAgent) {
        if (m_accessLog == null) return;
        if (remoteAddress == null) remoteAddress = "-";
        String dateString = "-";
        if (now != null) dateString = now.toString();
        if (method == null) method = "-";
        if (URI == null) URI = "-";
        if (query == null) query = "-";
        if (userAgent == null) userAgent = "-";
        String logEntry = remoteAddress + " " + now + "  " + method + " \"" + URI + "\" " + query + " \"" + userAgent + "\"";
        m_accessLog.write(logEntry);
    }
}
