package dk.pervasive.jcaf.scap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author Jakob E. Bardram
 *  
 */
public class SCAPRequest {

    private static final String UNDEFINED_PROTOCOL = "UNDEFINED";

    private Socket socket = null;

    private String request = null;

    private String header = null;

    private String content = null;

    private String protocol = UNDEFINED_PROTOCOL;

    private String version = null;

    private String method = null;

    private String argument = null;

    private Hashtable params = new Hashtable();

    private Hashtable headerFields = new Hashtable();

    private BufferedReader dataReader = null;

    public SCAPRequest(Socket socket) {
        super();
        this.socket = socket;
    }

    private BufferedReader getBufferedReader() throws IOException {
        if (dataReader == null) {
            InputStream is = socket.getInputStream();
            dataReader = new BufferedReader(new InputStreamReader(is));
        }
        return dataReader;
    }

    private void connect() throws IOException {
        String line;
        StringBuffer sbuff = new StringBuffer();
        Vector lines = new Vector();
        line = getBufferedReader().readLine();
        if (line == null) {
            throw new IOException("End of Stream");
        }
        while (line.length() != 0) {
            lines.add(line);
            sbuff.append(line + "\r\n");
            line = getBufferedReader().readLine();
        }
        request = (new String(sbuff)).trim();
        if (!request.equalsIgnoreCase("")) {
            header = (String) lines.get(0);
            System.out.println(header);
            StringTokenizer st = new StringTokenizer(header, " /&?=", false);
            protocol = st.nextToken().trim();
            version = st.nextToken().trim();
            method = st.nextToken().trim();
            argument = st.nextToken().trim();
            for (int i = 0; st.hasMoreElements(); i++) {
                String key = st.nextToken().trim();
                String param = st.nextToken().trim();
                params.put(key, param);
            }
            for (int i = 1; i < lines.size(); i++) {
                System.out.println((String) lines.get(i));
                st = new StringTokenizer((String) lines.get(i), ":");
                String header = st.nextToken().trim();
                String field = st.nextToken().trim();
                headerFields.put(header, field);
            }
        }
    }

    /**
     * Returns the header line in the request, e.g. <code>SAP/1.0 GET /getAllContacts</code>
     * @return
     */
    public String getHeader() {
        return header;
    }

    /**
     * Returns the argument.
     * 
     * @return String
     */
    public String getArgument() throws IOException {
        if (request == null) connect();
        return argument;
    }

    /**
     * Returns the method.
     * 
     * @return String
     */
    public String getMethod() throws IOException {
        if (request == null) connect();
        return method;
    }

    /**
     * Returns the protocol.
     * 
     * @return String
     */
    public String getProtocol() throws IOException {
        if (request == null) connect();
        return protocol;
    }

    /**
     * Returns the request.
     * 
     * @return String
     */
    public String getRequest() throws IOException {
        if (request == null) connect();
        return request;
    }

    /**
     * Returns the version.
     * 
     * @return String
     */
    public String getVersion() throws IOException {
        if (request == null) connect();
        return version;
    }

    /**
     * Returns the parameters.
     * 
     * @return Map
     */
    public Hashtable getParameters() throws IOException {
        if (request == null) connect();
        return params;
    }

    /**
     * Returns the number of parameters.
     * 
     * @return int
     */
    public int getParameterCount() throws IOException {
        if (request == null) connect();
        return params.size();
    }

    /**
     * Returns the params.
     * 
     * @return Map
     */
    public String getParameter(String key) throws IOException {
        return (String) getParameters().get(key);
    }

    /**
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns an unmodifyable Map of the header fields.
     * 
     * @return Map
     */
    public Map getHeaderFields() {
        return headerFields;
    }

    public String getHeaderField(String name) {
        return (String) headerFields.get(name);
    }
}
