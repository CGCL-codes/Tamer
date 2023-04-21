package at.jps.proxy;

import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * Class declaration
 *
 *
 * 
 * @version %I%, %G%
 */
public class HTTPRequestHeader {

    /**
     * Constructor declaration
     *
     *
     */
    HTTPRequestHeader() {
        primeheader = "";
        headerfields = new Hashtable();
        MalFormedHeader = false;
    }

    /**
     * Constructor declaration
     *
     *
     * @param in
     *
     *
     */
    HTTPRequestHeader(String in) {
        primeheader = "";
        headerfields = new Hashtable();
        MalFormedHeader = false;
        if (!parseHeader(in)) {
            MalFormedHeader = true;
        }
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     */
    public String getPrimeHeader() {
        return primeheader;
    }

    /**
     * Method declaration
     *
     *
     * @param name
     *
     *
     * @return
     *
     */
    public String getHeader(String name) {
        return (String) headerfields.get(name);
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     */
    public String getMethod() {
        return Method;
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     */
    public String getURI() {
        return URI;
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     */
    public String getVersion() {
        return Version;
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     */
    public int getContentLength() {
        return content_length;
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     */
    public Hashtable getHeaderFields() {
        return headerfields;
    }

    /**
     * Method declaration
     *
     *
     * @param input
     *
     *
     * @return
     *
     */
    public boolean parseHeader(String input) {
        if (input == null || input.equals("")) {
            return MalFormedHeader = true;
        }
        MalFormedHeader = false;
        String delimiter;
        if (input.endsWith("\r\n")) {
            delimiter = "\r\n";
        } else {
            delimiter = "\n";
        }
        int pos;
        if ((pos = input.indexOf(delimiter)) < 0) {
            MalFormedHeader = true;
            return false;
        }
        primeheader = input.substring(0, pos);
        StringTokenizer st = new StringTokenizer(primeheader, " ");
        for (int i = 0; st.hasMoreTokens(); i++) {
            switch(i) {
                case 0:
                    Method = st.nextToken();
                    break;
                case 1:
                    URI = st.nextToken();
                    break;
                case 2:
                    Version = st.nextToken();
                    break;
                default:
                    MalFormedHeader = true;
                    return false;
            }
        }
        if (Method == null || URI == null || Version == null) {
            MalFormedHeader = true;
            return false;
        }
        String name;
        String value;
        for (st = new StringTokenizer(input.substring(pos), delimiter); st.hasMoreTokens(); headerfields.put(name, value)) {
            String token = st.nextToken();
            if ((pos = token.indexOf(": ")) < 0) {
                MalFormedHeader = true;
                return false;
            }
            name = token.substring(0, pos);
            value = token.substring(pos + 2);
            if (name.equalsIgnoreCase("Content-Length")) {
                content_length = Integer.parseInt(value);
            }
        }
        return true;
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     */
    public boolean isMalFormedHeader() {
        return MalFormedHeader;
    }

    public String Method;

    public String URI;

    public String Version;

    public String primeheader;

    public Hashtable headerfields;

    private boolean MalFormedHeader;

    private int content_length;
}
