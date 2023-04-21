package rabbit.http;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.Externalizable;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/** A class to handle a general Header.
 */
public class GeneralHeader implements Externalizable {

    private static final long serialVersionUID = 20050430;

    /** The headers of this Header in order.
     * @serial 
     */
    protected List<Header> headers = new ArrayList<Header>();

    /** The String consisting of \r and \n */
    public static final String CRLF = "\r\n";

    /** If we should allow single \n to mean end of header*/
    protected static boolean strictHTTP = true;

    /** A cache of strings to keep the different strings down. */
    protected static Map<String, String> headerCache = new WeakHashMap<String, String>();

    /** Should headers be parsed with strict HTTP or not?
     */
    public static void setStrictHTTP(boolean b) {
        strictHTTP = b;
    }

    /** This class holds a header value, that is a &quot;type: some text&quot;
     */
    protected static class Header implements Externalizable {

        private static final long serialVersionUID = 20050430;

        String type;

        String value;

        /** Used for externalization */
        public Header() {
        }

        Header(String type, String value) {
            type = getCachedString(type);
            value = getCachedString(value);
            this.type = type;
            this.value = value;
        }

        public boolean equals(Object o) {
            if (o instanceof Header) {
                return (((Header) o).type.equalsIgnoreCase(type));
            }
            return false;
        }

        public int hashCode() {
            return type.hashCode();
        }

        public void append(String s) {
            value += CRLF + s;
            value = getCachedString(value);
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(type);
            out.writeObject(value);
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            type = getCachedString((String) in.readObject());
            value = getCachedString((String) in.readObject());
        }
    }

    /** Create a new HTTPHeader from scratch
     */
    public GeneralHeader() {
    }

    /** Create a new HTTPHeader from scratch
     */
    public GeneralHeader(DataInputStream in) throws IOException {
        readHeader(in);
    }

    protected static String getCachedString(String s) {
        String t = headerCache.get(s);
        if (t != null) return t;
        StringBuilder sb = new StringBuilder(s.length());
        sb.append(s);
        String ss = sb.toString();
        String ss2 = new String(ss);
        headerCache.put(ss, ss2);
        return ss2;
    }

    /** Read in this header from the given Stream.
     *  This actually merges the current header with the one beeing read.
     * @param in the Stream to read the header from.
     */
    public void readHeader(DataInputStream in) throws IOException {
        String msg = readLine(in);
        readHeader(in, msg);
    }

    public static String readLine(DataInputStream in) throws IOException {
        StringBuilder sb = new StringBuilder(50);
        int l = -1;
        int count = 0;
        while (true) {
            int c = in.read();
            if (c == -1) break;
            count++;
            if (c == '\n') {
                if (l == '\r') {
                    sb.setLength(sb.length() - 1);
                    break;
                }
                if (!strictHTTP) break;
            }
            sb.append((char) c);
            l = c;
        }
        if (count == 0) return null;
        return sb.toString();
    }

    /** Read in this header from the given Stream.
     *  This actually merges the current header with the one beeing read.
     *  This method is a convinience to have the abbility to read past the initial data
     *  before the header is read.
     * @param in the Stream to read the header from.
     * @param firstline the first line of this header.
     */
    public void readHeader(DataInputStream in, String firstline) throws IOException {
        String msg = firstline;
        Header head = null;
        boolean append = false;
        while (true) {
            if (msg == null) throw (new IOException("Couldnt read headers, connection must be closed"));
            if (msg.length() == 0) {
                if (!append) break;
                head.append(msg);
                msg = readLine(in);
                continue;
            }
            char c = msg.charAt(0);
            if (c == ' ' || c == '\t' || append) {
                if (head != null) {
                    head.append(msg);
                    append = checkQuotes(head.value);
                } else {
                    throw (new IOException("Malformed header:" + msg));
                }
                msg = readLine(in);
                continue;
            }
            int i = msg.indexOf(':');
            if (i < 0) {
                switch(msg.charAt(0)) {
                    case 'h':
                    case 'H':
                        if (msg.toLowerCase().startsWith("http/")) {
                            msg = readLine(in);
                            continue;
                        }
                    default:
                        throw (new IOException("Malformed header:" + msg));
                }
            }
            int j = i;
            while (j > 0 && ((c = msg.charAt(j - 1)) == ' ' || c == '\t')) j--;
            String value = msg.substring(i + 1);
            if (strictHTTP) append = checkQuotes(value);
            if (!append) value = value.trim();
            head = new Header(msg.substring(0, j), value);
            headers.add(head);
            msg = readLine(in);
        }
    }

    private boolean checkQuotes(String v) {
        int q = v.indexOf('"');
        if (q == -1) return false;
        boolean halfquote = false;
        int l = v.length();
        for (; q < l; q++) {
            char c = v.charAt(q);
            switch(c) {
                case '\\':
                    q++;
                    break;
                case '"':
                    halfquote = !halfquote;
                    break;
            }
        }
        return halfquote;
    }

    /** Get the text value of this header 
     * @return a String describing this GeneralHeader.
     */
    public String toString() {
        StringBuilder ret = new StringBuilder();
        int hsize = headers.size();
        for (int i = 0; i < hsize; i++) {
            Header h = headers.get(i);
            ret.append(h.type);
            ret.append(": ");
            ret.append(h.value);
            ret.append(CRLF);
        }
        ret.append(CRLF);
        return ret.toString();
    }

    /** get the value of header type 
     * @param type the Header were intrested in.
     * @return the value of type or null if no value is set.
     */
    public String getHeader(String type) {
        int s = headers.size();
        for (int i = 0; i < s; i++) {
            Header h = headers.get(i);
            if (h.type.equalsIgnoreCase(type)) return h.value;
        }
        return null;
    }

    /** Set or replaces a value for given type.
     * @param type the type or category that we want to set.
     * @param value the value we want to set
     */
    public void setHeader(String type, String value) {
        int s = headers.size();
        for (int i = 0; i < s; i++) {
            Header h = headers.get(i);
            if (h.type.equalsIgnoreCase(type)) {
                h.value = value;
                return;
            }
        }
        Header h = new Header(type, value);
        headers.add(h);
    }

    /** Set a specified header 
     * @param current the type or category that we want to set.
     * @param newValue the value we want to set
     */
    public void setExistingValue(String current, String newValue) {
        int s = headers.size();
        for (int i = 0; i < s; i++) {
            Header h = headers.get(i);
            if (h.value.equals(current)) {
                h.value = newValue;
                return;
            }
        }
    }

    /** Add a new header. Old headers of the same type remain. 
     *  The new header is placed last.
     * @param type the type or category that we want to set.
     * @param value the value we want to set
     */
    public void addHeader(String type, String value) {
        Header h = new Header(type, value);
        headers.add(h);
    }

    /** removes a headerline from this header
     * @param type the type we want to remove
     */
    public void removeHeader(String type) {
        int s = headers.size();
        for (int i = 0; i < s; i++) {
            Header h = headers.get(i);
            if (h.type.equalsIgnoreCase(type)) {
                headers.remove(i);
                i--;
                s--;
            }
        }
    }

    /** removes a header with the specified value 
     * @param value the value of the header we want to remove
     */
    public void removeValue(String value) {
        int s = headers.size();
        for (int i = 0; i < s; i++) {
            Header h = headers.get(i);
            if (h.value.equals(value)) {
                headers.remove(i);
                return;
            }
        }
    }

    /** Get all headers of a specified type...
     * @param type the type of the headers to get, eg. "Cache-Control".
     */
    public List<String> getHeaders(String type) {
        List<String> ret = new ArrayList<String>();
        int s = headers.size();
        for (int i = 0; i < s; i++) {
            Header h = headers.get(i);
            if (h.type.equalsIgnoreCase(type)) {
                ret.add(h.value);
            }
        }
        return ret;
    }

    /** Copy all headers in this header to the given header. 
     * @param to the GeneralHeader to add headers to.
     */
    public void copyHeader(GeneralHeader to) {
        for (int i = 0; i < headers.size(); i++) {
            Header h = headers.get(i);
            to.addHeader(h.type, h.value);
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        headers = (List<Header>) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(headers);
    }
}
