package pspdash;

import pspdash.data.DataRepository;
import pspdash.data.SimpleData;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

public class TinyCGIBase implements TinyCGI {

    static String DEFAULT_CHARSET = "ISO-8859-1";

    protected InputStream inStream = null;

    protected OutputStream outStream = null;

    protected PrintWriter out = null;

    protected Map env = null;

    protected Map parameters = new HashMap();

    protected String charset = DEFAULT_CHARSET;

    public void service(InputStream in, OutputStream out, Map env) throws IOException {
        this.inStream = in;
        this.outStream = out;
        this.out = new PrintWriter(new OutputStreamWriter(outStream, charset));
        this.env = env;
        parameters.clear();
        parseInput((String) env.get("QUERY_STRING"));
        if ("POST".equalsIgnoreCase((String) env.get("REQUEST_METHOD"))) doPost(); else doGet();
        this.out.flush();
    }

    /** Parse CGI query parameters, and store them in the Map
     *  <code>parameters</code>.
     *
     * Single valued parameters can be fetched directly from the map.
     * Multivalued parameters are stored in the map as String arrays,
     * with "_ALL" appended to the name.  (So a query string
     * "name=foo&name=bar" would result in a 2-element string array
     * being placed in the map under the key "name_ALL".)
     */
    protected void parseInput(String query) throws IOException {
        if (query == null || query.length() == 0) return;
        String delim = (query.indexOf('\n') == -1) ? "&" : "\r\n";
        StringTokenizer params = new StringTokenizer(query, delim);
        String param, name, val;
        int equalsPos;
        while (params.hasMoreTokens()) {
            param = params.nextToken();
            equalsPos = param.indexOf('=');
            if (equalsPos == 0 || param.length() == 0) continue; else if (equalsPos == -1) parameters.put(URLDecoder.decode(param), Boolean.TRUE); else try {
                name = URLDecoder.decode(param.substring(0, equalsPos));
                val = param.substring(equalsPos + 1);
                if (val.startsWith("=")) val = val.substring(1); else val = URLDecoder.decode(val);
                if (supportQueryFiles() && QUERY_FILE_PARAM.equals(name)) parseInputFile(val); else putParam(name, val);
            } catch (Exception e) {
                System.err.println("Malformed query parameter: " + param);
            }
        }
    }

    public static final String QUERY_FILE_PARAM = "qf";

    private void putParam(String name, String val) {
        parameters.put(name, val);
        name = name + "_ALL";
        parameters.put(name, append((String[]) parameters.get(name), val));
    }

    protected void parseFormData() throws IOException {
        int length;
        try {
            length = Integer.parseInt((String) env.get("CONTENT_LENGTH"));
        } catch (Exception e) {
            return;
        }
        byte[] messageBody = new byte[length];
        int bytesRead = inStream.read(messageBody);
        parseInput(new String(messageBody, 0, bytesRead));
    }

    protected void parseMultipartFormData() throws IOException {
        String contentType = (String) env.get("CONTENT_TYPE");
        int contentLength;
        try {
            contentLength = Integer.parseInt((String) env.get("CONTENT_LENGTH"));
        } catch (Exception e) {
            return;
        }
        try {
            MultipartRequest req = new MultipartRequest(new PrintWriter(System.out), contentType, contentLength, inStream, MultipartRequest.MAX_READ_BYTES);
            Enumeration parameterNames = req.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = (String) parameterNames.nextElement();
                Enumeration values = req.getURLParameters(name);
                while (values.hasMoreElements()) putParam(name, (String) values.nextElement());
            }
            parameterNames = req.getFileParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = (String) parameterNames.nextElement();
                parameters.put(name, req.getFileSystemName(name));
                parameters.put(name + "_SIZE", req.getFileParameter(name, req.SIZE));
                parameters.put(name + "_TYPE", req.getFileParameter(name, req.CONTENT_TYPE));
                parameters.put(name + "_CONTENTS", req.getFileParameter(name, req.CONTENTS));
            }
        } catch (IllegalArgumentException iae) {
            parseFormData();
        }
    }

    protected void parseInputFile(String filename) throws IOException {
        if (filename == null || filename.length() == 0) return;
        TinyWebServer t = getTinyWebServer();
        String origFilename = filename;
        String scriptPath = (String) env.get("SCRIPT_PATH");
        try {
            if (!filename.startsWith("/")) {
                URL context = new URL("http://unimportant" + scriptPath);
                URL file = new URL(context, filename);
                filename = file.getFile();
            }
            env.put("SCRIPT_PATH", filename);
            parseInput(new String(t.getRequest(filename, true), "UTF-8"));
        } catch (IOException ioe) {
            System.out.println("Couldn't read file: " + filename);
            System.out.println("(Specified as '" + origFilename + "' from '" + scriptPath + "')");
        } finally {
            env.put("SCRIPT_PATH", scriptPath);
        }
    }

    public String resolveRelativeURI(String context, String uri) {
        if (uri == null || uri.startsWith("/") || uri.startsWith("http:")) return uri;
        try {
            if (!context.startsWith("http:")) context = "http://unimportant" + context;
            URL cntxt = new URL(context);
            URL file = new URL(cntxt, uri);
            return file.getFile();
        } catch (MalformedURLException mue) {
            return uri;
        }
    }

    public String resolveRelativeURI(String uri) {
        return resolveRelativeURI((String) env.get("REQUEST_URI"), uri);
    }

    private String[] append(String[] array, String element) {
        String[] result;
        result = new String[array == null ? 1 : array.length + 1];
        if (array != null) System.arraycopy(array, 0, result, 0, array.length);
        result[result.length - 1] = element;
        return result;
    }

    protected String cssLinkHTML() {
        String style = (String) parameters.get("style");
        if (style == null) style = "/style.css";
        return "<LINK REL='stylesheet' TYPE='text/css' HREF='" + style + "'>";
    }

    /** Get the data repository servicing this request. */
    protected DataRepository getDataRepository() {
        return (DataRepository) env.get(DATA_REPOSITORY);
    }

    /** Get the tiny web server that is running this request. */
    protected TinyWebServer getTinyWebServer() {
        return (TinyWebServer) env.get(TINY_WEB_SERVER);
    }

    /** Get the PSPProperties object */
    protected PSPProperties getPSPProperties() {
        return (PSPProperties) env.get(PSP_PROPERTIES);
    }

    protected ObjectCache getObjectCache() {
        return (ObjectCache) env.get(OBJECT_CACHE);
    }

    /** Perform an internal http request. */
    protected byte[] getRequest(String uri, boolean skipHeaders) throws IOException {
        return getTinyWebServer().getRequest(uri, skipHeaders);
    }

    /** Fetch a named query parameter */
    protected String getParameter(String name) {
        return (String) parameters.get(name);
    }

    /** get the effective prefix, set via the URL */
    protected String getPrefix() {
        String result = (String) parameters.get("hierarchyPath");
        if (result == null) result = (String) env.get("PATH_TRANSLATED"); else if (!result.startsWith("/")) {
            String prefix = (String) env.get("PATH_TRANSLATED");
            if (prefix == null) prefix = "";
            result = prefix + "/" + result;
        }
        return result;
    }

    /** get the name of the person who owns the data in the repository */
    protected String getOwner() {
        DataRepository data = getDataRepository();
        SimpleData val = data.getSimpleValue("/Owner");
        if (val == null) return null;
        String result = val.format();
        if ("Enter your name".equals(result)) return null; else return result;
    }

    /** Does this CGI script want to support query parameter files?
     * child classes that DO NOT want query parameter support should
     * override this method to return false. */
    protected boolean supportQueryFiles() {
        return true;
    }

    /** Handle an HTTP POST request */
    protected void doPost() throws IOException {
        writeHeader();
        writeContents();
    }

    /** Handle an HTTP GET request */
    protected void doGet() throws IOException {
        writeHeader();
        writeContents();
    }

    /** Write a standard CGI header.
     *
     * This method can be overridden by children that might need to generate
     * a special header, or might need to vary the header on the fly.
     */
    protected void writeHeader() {
        out.print("Content-type: text/html; charset=" + charset + "\r\n\r\n");
        out.flush();
    }

    /** Generate CGI script output.
     *
     * This method should be overridden by child classes to generate
     * the contents of the script.
     */
    protected void writeContents() throws IOException {
        out.println("<HTML><BODY>");
        out.println("This space intentionally left blank.");
        out.println("</BODY></HTML>");
    }

    /** Set the default character set to be used for CGI output.
     *
     * If the parameter does not name a valid charset, this method
     * will do nothing.
     *
     * @param charsetName The name of a supported character encoding
     */
    static void setDefaultCharset(String charsetName) {
        if (charsetName != null && charsetName.length() > 0) try {
            "test".getBytes(charsetName);
            DEFAULT_CHARSET = charsetName;
        } catch (UnsupportedEncodingException uee) {
        }
    }

    /** Get the name of the default character set to be used for CGI output.
     */
    public static String getDefaultCharset() {
        return DEFAULT_CHARSET;
    }
}
