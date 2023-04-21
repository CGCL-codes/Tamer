package fi.hip.gb.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * This class is made to convert RFC2253-style Distinguished Name to
 * the older X.500 style frequently used in Globus, or vice versa.
 * It also has method to remove "/CN=proxy" or "/CN=limited proxy"
 * or "/CN=12345..." suffixes from a DN, where 12345... are digits only.
 * <p>
 * Whitespace is removed in the process, in accordance with RFC2253.
 * Thus, the following code
 * <p>
 * <code>
 * DNConvert c = new DNConvert("CN=proxy, CN = Olle, O= KTH, C=SE");<br>
 * System.out.println("["+c.removeProxy(DNConvert.RFC2253)+"]);<br>
 * System.out.println("["+c.removeProxy(DNConvert.X500)+"]");
 * </code>
 * <p>
 * yields the following result:<p>
 * <code>
 * [CN=Olle,O=KTH,C=SE]<br>
 * [/C=SE/O=KTH/CN=Olle]
 * </code>
 * 
 * @author Ville Nenonen Ville.Nenonen@hut.fi
 * @author Olle Mulmo (rewrite)
 * 
 * @version $Id: DNConvert.java 555 2005-09-28 09:41:40Z jkarppin $
 */
public class DNConvert {

    /**
	 * The style denoting RFC2253 format of the DN
	 */
    public static final int RFC2253 = 0;

    /**
	 * The style denoting the old X.500 format of the DN
	 */
    public static final int X500 = 1;

    private static final String[] PROXY_PATTERN = new String[] { "^CN=((limited )*proxy|[0-9]*),.*", ".*/CN=([0-9]*|(limited )*proxy)$" };

    private static boolean[] PROXY_AT_END = new boolean[] { false, true };

    private static char[] RDN_DELIMITER = new char[] { ',', '/' };

    private static boolean[] RDN_DELIMITER_AT_END = new boolean[] { true, false };

    /**
	 * The regular expression used to reformat the dubious E(mail(Address)) RDN attribute
	 */
    private static String[] EMAIL_RDN_PATTERN;

    /**
	 * The "trimmed" E(mail(Address)) RDN attribute 
	 */
    private static String[] EMAIL_RDN_REPLACE;

    /**
	 * The regular expressions used to remove any whitespace inside a DN
	 */
    private static String[] WHITESPACE_PATTERN;

    /**
	 * The RDN definition of email address
	 */
    private static String[] EMAIL_RDN = new String[] { "Email", "Email" };

    private static String WHITESPACE_REPLACE = "$1$2";

    static {
        WHITESPACE_PATTERN = new String[RDN_DELIMITER.length];
        EMAIL_RDN_PATTERN = new String[RDN_DELIMITER.length];
        EMAIL_RDN_REPLACE = new String[RDN_DELIMITER.length];
        for (int i = 0; i < RDN_DELIMITER.length; i++) {
            EMAIL_RDN_PATTERN[i] = "^(E|e|((E|e|)(mail|mailaddress|mailAddress|MAIL|MAILADDRESS)))=";
            EMAIL_RDN_REPLACE[i] = EMAIL_RDN[i] + "=";
            WHITESPACE_PATTERN[i] = "(\\\\.)|[ ]*([=" + RDN_DELIMITER[i] + "])[ ]*|^[ ]*|[ ]*$";
        }
    }

    /**
	 * Default behavior whether to filter the email addresses or not
	 */
    private static boolean DEFAULT_DO_EMAIL_FORMAT = true;

    /**
	 * Instance variables
	 */
    private String myDn = null;

    private String[] myDnFormatted = new String[RDN_DELIMITER.length];

    private String[] myDnProxyRemoved = new String[RDN_DELIMITER.length];

    private int myFormat = -1;

    private boolean myFormatEmailAttr = DEFAULT_DO_EMAIL_FORMAT;

    private LinkedList myRdns = null;

    /**
	 * @param dn The Distuingished Name, in either X.500 or RFC2253 format.
	 */
    public DNConvert(String dn) {
        super();
        myDn = dn.trim();
        myFormat = (myDn.startsWith("/")) ? X500 : RFC2253;
    }

    /**
	 * @param dn The Distuingished Name, in either X.500 or RFC2253 format.
	 * @param formatEmailAttr If set, will reformat the email attribute
	 */
    public DNConvert(String dn, boolean formatEmailAttr) {
        super();
        myDn = dn.trim();
        myFormat = (myDn.startsWith("/")) ? X500 : RFC2253;
        myFormatEmailAttr = formatEmailAttr;
    }

    /**
	 * Redefines the default behavior whether to reformat the email address portion
	 * of a DN or not.
	 * 
	 * @param enabled
	 * @see #DNConvert(String, boolean)
	 * @see #setFormatEmailAttr(boolean)
	 */
    public static void setDefaultEmailAttrFormatting(boolean enabled) {
        DEFAULT_DO_EMAIL_FORMAT = enabled;
    }

    /**
	 * Removes the proxy extension of the DN. 
	 *
	 * @param style The style to reformat to (one of X500 or RFC2253)
	 * @return DN in preferred format without any 'CN=proxy' RDN
	 * @see #reformat(int)
	 */
    public String removeProxySuffix(int style) {
        if (myDnProxyRemoved[style] != null) {
            return myDnProxyRemoved[style];
        }
        String dummy = reformat(style);
        while (dummy.matches(PROXY_PATTERN[style])) {
            dummy = (PROXY_AT_END[style]) ? dummy.substring(0, dummy.lastIndexOf(RDN_DELIMITER[style])) : dummy.substring(dummy.indexOf(RDN_DELIMITER[style]) + 1);
        }
        return (myDnProxyRemoved[style] = dummy);
    }

    /**
	 * Defines whether to reformat an email attribute in the DN or not
	 *  
	 * @param enabled
	 */
    public void setFormatEmailAttr(boolean enabled) {
        myFormatEmailAttr = enabled;
        myDnFormatted = new String[RDN_DELIMITER.length];
    }

    /**
	 * Removes the proxy extension of the DN. 
	 *
	 * @return DN in the current format without any 'CN=proxy' RDN.
	 */
    public String removeProxySuffix() {
        return removeProxySuffix(myFormat);
    }

    private String trimInternal(String dn, int style) {
        String s = Pattern.compile(WHITESPACE_PATTERN[style]).matcher(dn).replaceAll(WHITESPACE_REPLACE);
        return myFormatEmailAttr ? Pattern.compile(EMAIL_RDN_PATTERN[style]).matcher(s).replaceFirst(EMAIL_RDN_REPLACE[style]) : s;
    }

    /**
	 * Removes any whitespace between type-value and RDN delimiters.
	 * <p>
	 * Example:<code>" CN=John Doe , O =Org"</code> returns <code>"CN=John Doe,O=Org"</code>
	 * <p>
	 * 
	 * @return String the trimmed DN
	 */
    public String trim() {
        String s = myDnFormatted[myFormat];
        if (s == null) s = myDnFormatted[myFormat] = trimInternal(myDn, myFormat);
        return s;
    }

    private void addToList(LinkedList list, boolean reversed, String rdn, int fromStyle, int toStyle) {
        if (rdn.trim().length() == 0) return;
        if (!rdn.matches(".*=.*") && !list.isEmpty()) {
            String s = (String) (reversed ? list.removeFirst() : list.removeLast());
            rdn = s + RDN_DELIMITER[fromStyle] + rdn;
        }
        if (reversed) list.addFirst(trimInternal(rdn, toStyle)); else list.addLast(trimInternal(rdn, toStyle));
    }

    /**
	 * Converts the DN from one style to another. 
	 * @param style The style to reformat to (one of X500 or RFC2253)
	 * @return The DN in the preferred format.
	 * @see #X500
	 * @see #RFC2253
	 */
    public String reformat(int style) {
        if (myDnFormatted[style] != null) return myDnFormatted[style];
        boolean reversed = (style != myFormat);
        LinkedList list = new LinkedList();
        char delim = RDN_DELIMITER[myFormat];
        String delimiters = "\"" + delim;
        StringTokenizer tok = new StringTokenizer(myDn, delimiters, true);
        boolean inQuote = false;
        String rdn = "";
        while (tok.hasMoreTokens()) {
            String token = tok.nextToken();
            if (token.length() == 1) {
                char ch = token.charAt(0);
                if (ch == '\"') {
                    inQuote = !inQuote;
                }
                if (ch == delim && !inQuote) {
                    if (rdn.trim().length() > 0) {
                        if (rdn.endsWith("\\")) {
                            if (myFormat != style) rdn = rdn.replaceFirst(".$", "" + delim); else rdn += delim;
                            continue;
                        }
                        addToList(list, reversed, rdn, myFormat, style);
                    }
                    rdn = "";
                    continue;
                }
            }
            rdn += token;
        }
        addToList(list, reversed, rdn, myFormat, style);
        StringBuffer sb = new StringBuffer();
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            if (!RDN_DELIMITER_AT_END[style]) {
                sb.append(RDN_DELIMITER[style]);
            }
            sb.append((String) i.next());
            if (RDN_DELIMITER_AT_END[style] && i.hasNext()) {
                sb.append(RDN_DELIMITER[style]);
            }
        }
        return myDnFormatted[style] = sb.toString();
    }

    public String toString() {
        String format = "error";
        try {
            format = (new String[] { "RFC", "X500" })[myFormat];
        } catch (Exception e) {
        }
        return "Format: " + format + " (" + myFormat + ")\nDN: [" + myDn + "]";
    }
}
