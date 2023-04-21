package org.archive.net;

import gnu.inet.encoding.IDNA;
import gnu.inet.encoding.IDNAException;
import it.unimi.dsi.mg4j.util.MutableString;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.archive.util.TextUtils;

/**
 * Factory that returns UURIs.
 * 
 * Does escaping and fixup on URIs massaging in accordance with RFC2396
 * and to match browser practice. For example, it removes any
 * '..' if first thing in the path as per IE,  converts backslashes to forward
 * slashes, and discards any 'fragment'/anchor portion of the URI. This
 * class will also fail URIs if they are longer than IE's allowed maximum
 * length.
 * 
 * <p>TODO: Test logging.
 * 
 * @author stack
 */
public class UURIFactory extends URI {

    private static final long serialVersionUID = -6146295130382209042L;

    /**
     * Logging instance.
     */
    private static Logger logger = Logger.getLogger(UURIFactory.class.getName());

    /**
     * The single instance of this factory.
     */
    private static final UURIFactory factory = new UURIFactory();

    /**
     * RFC 2396-inspired regex.
     *
     * From the RFC Appendix B:
     * <pre>
     * URI Generic Syntax                August 1998
     *
     * B. Parsing a URI Reference with a Regular Expression
     *
     * As described in Section 4.3, the generic URI syntax is not sufficient
     * to disambiguate the components of some forms of URI.  Since the
     * "greedy algorithm" described in that section is identical to the
     * disambiguation method used by POSIX regular expressions, it is
     * natural and commonplace to use a regular expression for parsing the
     * potential four components and fragment identifier of a URI reference.
     *
     * The following line is the regular expression for breaking-down a URI
     * reference into its components.
     *
     * ^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?
     * 12            3  4          5       6  7        8 9
     *
     * The numbers in the second line above are only to assist readability;
     * they indicate the reference points for each subexpression (i.e., each
     * paired parenthesis).  We refer to the value matched for subexpression
     * <n> as $<n>.  For example, matching the above expression to
     *
     * http://www.ics.uci.edu/pub/ietf/uri/#Related
     *
     * results in the following subexpression matches:
     *
     * $1 = http:
     * $2 = http
     * $3 = //www.ics.uci.edu
     * $4 = www.ics.uci.edu
     * $5 = /pub/ietf/uri/
     * $6 = <undefined>
     * $7 = <undefined>
     * $8 = #Related
     * $9 = Related
     *
     * where <undefined> indicates that the component is not present, as is
     * the case for the query component in the above example.  Therefore, we
     * can determine the value of the four components and fragment as
     *
     * scheme    = $2
     * authority = $4
     * path      = $5
     * query     = $7
     * fragment  = $9
     * </pre>
     *
     * -- 
     * <p>Below differs from the rfc regex in that it has java escaping of
     * regex characters and we allow a URI made of a fragment only (Added extra
     * group so indexing is off by one after scheme).
     */
    static final Pattern RFC2396REGEX = Pattern.compile("^(([^:/?#]+):)?((//([^/?#]*))?([^?#]*)(\\?([^#]*))?)?(#(.*))?");

    public static final String SLASHDOTDOTSLASH = "^(/\\.\\./)+";

    public static final String SLASH = "/";

    public static final String HTTP = "http";

    public static final String HTTP_PORT = ":80";

    public static final String HTTPS = "https";

    public static final String HTTPS_PORT = ":443";

    public static final String DOT = ".";

    public static final String EMPTY_STRING = "";

    public static final String NBSP = " ";

    public static final String SPACE = " ";

    public static final String ESCAPED_SPACE = "%20";

    public static final String TRAILING_ESCAPED_SPACE = "^(.*)(%20)+$";

    public static final String PIPE = "|";

    public static final String PIPE_PATTERN = "\\|";

    public static final String ESCAPED_PIPE = "%7C";

    public static final String CIRCUMFLEX = "^";

    public static final String CIRCUMFLEX_PATTERN = "\\^";

    public static final String ESCAPED_CIRCUMFLEX = "%5E";

    public static final String QUOT = "\"";

    public static final String ESCAPED_QUOT = "%22";

    public static final String SQUOT = "'";

    public static final String ESCAPED_SQUOT = "%27";

    public static final String APOSTROPH = "`";

    public static final String ESCAPED_APOSTROPH = "%60";

    public static final String LSQRBRACKET = "[";

    public static final String LSQRBRACKET_PATTERN = "\\[";

    public static final String ESCAPED_LSQRBRACKET = "%5B";

    public static final String RSQRBRACKET = "]";

    public static final String RSQRBRACKET_PATTERN = "\\]";

    public static final String ESCAPED_RSQRBRACKET = "%5D";

    public static final String LCURBRACKET = "{";

    public static final String LCURBRACKET_PATTERN = "\\{";

    public static final String ESCAPED_LCURBRACKET = "%7B";

    public static final String RCURBRACKET = "}";

    public static final String RCURBRACKET_PATTERN = "\\}";

    public static final String ESCAPED_RCURBRACKET = "%7D";

    public static final String BACKSLASH = "\\";

    public static final String BACKSLASH_PATTERN = "\\\\";

    public static final String ESCAPED_BACKSLASH = "%5C";

    public static final String NEWLINE = "\n+|\r+";

    public static final String IMPROPERESC_REPLACE = "%25$1";

    public static final String IMPROPERESC = "%((?:[^\\p{XDigit}])|(?:.[^\\p{XDigit}])|(?:\\z))";

    public static final String COMMERCIAL_AT = "@";

    public static final char PERCENT_SIGN = '%';

    public static final char COLON = ':';

    /**
     * First percent sign in string followed by two hex chars.
     */
    public static final String URI_HEX_ENCODING = "^[^%]*%[\\p{XDigit}][\\p{XDigit}].*";

    /**
     * Authority port number regex.
     */
    static final Pattern PORTREGEX = Pattern.compile("(.*:)([0-9]+)$");

    /**
     * Characters we'll accept in the domain label part of a URI
     * authority: ASCII letters-digits-hyphen (LDH) plus underscore,
     * with single intervening '.' characters.
     * 
     * (We accept '_' because DNS servers have tolerated for many
     * years counter to spec; we also accept dash patterns and ACE
     * prefixes that will be rejected by IDN-punycoding attempt.)
     */
    static final String ACCEPTABLE_ASCII_DOMAIN = "^(?:[a-zA-Z0-9_-]++(?:\\.)?)++$";

    /**
     * Pattern that looks for case of three or more slashes after the 
     * scheme.  If found, we replace them with two only as mozilla does.
     */
    static final Pattern HTTP_SCHEME_SLASHES = Pattern.compile("^(https?://)/+(.*)");

    /**
     * Pattern that looks for case of two or more slashes in a path.
     */
    static final Pattern MULTIPLE_SLASHES = Pattern.compile("//+");

    /**
     * System property key for list of supported schemes.
     */
    private static final String SCHEMES_KEY = ".schemes";

    /**
     * System property key for list of purposefully-ignored schemes.
     */
    private static final String IGNORED_SCHEMES_KEY = ".ignored-schemes";

    private String[] schemes = null;

    private String[] ignoredSchemes = null;

    public static final int IGNORED_SCHEME = 9999999;

    /**
     * Protected constructor.
     */
    private UURIFactory() {
        super();
        String s = System.getProperty(this.getClass().getName() + SCHEMES_KEY);
        if (s != null && s.length() > 0) {
            schemes = s.split("[, ]+");
            Arrays.sort(schemes);
        }
        String ignored = System.getProperty(this.getClass().getName() + IGNORED_SCHEMES_KEY);
        if (ignored != null && ignored.length() > 0) {
            ignoredSchemes = ignored.split("[, ]+");
            Arrays.sort(ignoredSchemes);
        }
    }

    /**
     * @param uri URI as string.
     * @return An instance of UURI
     * @throws URIException
     */
    public static UURI getInstance(String uri) throws URIException {
        return UURIFactory.factory.create(uri);
    }

    /**
     * @param uri URI as string.
     * @param charset Character encoding of the passed uri string.
     * @return An instance of UURI
     * @throws URIException
     */
    public static UURI getInstance(String uri, String charset) throws URIException {
        return UURIFactory.factory.create(uri, charset);
    }

    /**
     * @param base Base uri to use resolving passed relative uri.
     * @param relative URI as string.
     * @return An instance of UURI
     * @throws URIException
     */
    public static UURI getInstance(UURI base, String relative) throws URIException {
        return UURIFactory.factory.create(base, relative);
    }

    /**
     * Test of whether passed String has an allowed URI scheme.
     * First tests if likely scheme suffix.  If so, we then test if its one of
     * the supported schemes.
     * @param possibleUrl URL string to examine.
     * @return True if passed string looks like it could be an URL.
     */
    public static boolean hasSupportedScheme(String possibleUrl) {
        boolean hasScheme = UURI.hasScheme(possibleUrl);
        if (!hasScheme || UURIFactory.factory.schemes == null) {
            return hasScheme;
        }
        String tmpStr = possibleUrl.substring(0, possibleUrl.indexOf(':'));
        return Arrays.binarySearch(UURIFactory.factory.schemes, tmpStr) >= 0;
    }

    /**
     * @param uri URI as string.
     * @return Instance of UURI.
     * @throws URIException
     */
    private UURI create(String uri) throws URIException {
        return create(uri, UURI.getDefaultProtocolCharset());
    }

    /**
     * @param uri URI as string.
     * @param charset Original encoding of the string.
     * @return Instance of UURI.
     * @throws URIException
     */
    private UURI create(String uri, String charset) throws URIException {
        UURI uuri = new UURI(fixup(uri, null, charset), true, charset);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("URI " + uri + " PRODUCT " + uuri.toString() + " CHARSET " + charset);
        }
        return validityCheck(uuri);
    }

    /**
     * @param base UURI to use as a base resolving <code>relative</code>.
     * @param relative Relative URI.
     * @return Instance of UURI.
     * @throws URIException
     */
    private UURI create(UURI base, String relative) throws URIException {
        UURI uuri = new UURI(base, new UURI(fixup(relative, base, base.getProtocolCharset()), true, base.getProtocolCharset()));
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(" URI " + relative + " PRODUCT " + uuri.toString() + " CHARSET " + base.getProtocolCharset() + " BASE " + base);
        }
        return validityCheck(uuri);
    }

    /**
     * Check the generated UURI.
     * 
     * At the least look at length of uuri string.  We were seeing case
     * where before escaping, string was &lt; MAX_URL_LENGTH but after was
     * &gt;.  Letting out a too-big message was causing us troubles later
     * down the processing chain.
     * @param uuri Created uuri to check.
     * @return The passed <code>uuri</code> so can easily inline this check.
     * @throws URIException
     */
    protected UURI validityCheck(UURI uuri) throws URIException {
        if (uuri.getRawURI().length > UURI.MAX_URL_LENGTH) {
            throw new URIException("Created (escaped) uuri > " + UURI.MAX_URL_LENGTH + ": " + uuri.toString());
        }
        return uuri;
    }

    /**
     * Do heritrix fix-up on passed uri string.
     *
     * Does heritrix escaping; usually escaping done to make our behavior align
     * with IEs.  This method codifies our experience pulling URIs from the
     * wilds.  Its does all the escaping we want; its output can always be
     * assumed to be 'escaped' (though perhaps to a laxer standard than the 
     * vanilla HttpClient URI class or official specs might suggest). 
     *
     * @param uri URI as string.
     * @param base May be null.
     * @param e True if the uri is already escaped.
     * @return A fixed up URI string.
     * @throws URIException
     */
    private String fixup(String uri, final URI base, final String charset) throws URIException {
        if (uri == null) {
            throw new NullPointerException();
        } else if (uri.length() == 0 && base == null) {
            throw new URIException("URI length is zero (and not relative).");
        }
        if (uri.length() > UURI.MAX_URL_LENGTH) {
            throw new URIException("URI length > " + UURI.MAX_URL_LENGTH + ": " + uri);
        }
        if (uri.indexOf(NBSP) >= 0) {
            uri = TextUtils.replaceAll(NBSP, uri, SPACE);
        }
        uri = uri.trim();
        if (uri.indexOf(BACKSLASH) >= 0) {
            uri = TextUtils.replaceAll(BACKSLASH_PATTERN, uri, SLASH);
        }
        uri = TextUtils.replaceAll(NEWLINE, uri, EMPTY_STRING);
        Matcher matcher = HTTP_SCHEME_SLASHES.matcher(uri);
        if (matcher.matches()) {
            uri = matcher.group(1) + matcher.group(2);
        }
        uri = escapeWhitespace(uri);
        matcher = RFC2396REGEX.matcher(uri);
        if (!matcher.matches()) {
            throw new URIException("Failed parse of " + uri);
        }
        String uriScheme = checkUriElementAndLowerCase(matcher.group(2));
        String uriSchemeSpecificPart = checkUriElement(matcher.group(3));
        String uriAuthority = checkUriElement(matcher.group(5));
        String uriPath = checkUriElement(matcher.group(6));
        String uriQuery = checkUriElement(matcher.group(8));
        if (uriScheme != null && uriScheme.length() > 0 && this.schemes != null) {
            if (!(Arrays.binarySearch(schemes, uriScheme) >= 0)) {
                if ((Arrays.binarySearch(ignoredSchemes, uriScheme) >= 0)) {
                    throw new URIException(IGNORED_SCHEME, "Ignored scheme: " + uriScheme);
                } else {
                    throw new URIException("Unsupported scheme: " + uriScheme);
                }
            }
        }
        if (uriScheme == null || uriScheme.length() <= 0) {
            if (base == null) {
                throw new URIException("Relative URI but no base: " + uri);
            }
        } else {
            checkHttpSchemeSpecificPartSlashPrefix(base, uriScheme, uriSchemeSpecificPart);
        }
        uriAuthority = fixupAuthority(uriAuthority);
        if (uriSchemeSpecificPart != null && uriSchemeSpecificPart.startsWith(SLASH)) {
            if (uriPath != null) {
                uriPath = TextUtils.replaceFirst(SLASHDOTDOTSLASH, uriPath, SLASH);
            }
            if (uriPath == null || EMPTY_STRING.equals(uriPath)) {
                uriPath = SLASH;
            }
        }
        if (uriAuthority != null) {
            if (uriScheme != null && uriScheme.length() > 0 && uriScheme.equals(HTTP)) {
                uriAuthority = checkPort(uriAuthority);
                uriAuthority = stripTail(uriAuthority, HTTP_PORT);
            } else if (uriScheme != null && uriScheme.length() > 0 && uriScheme.equals(HTTPS)) {
                uriAuthority = checkPort(uriAuthority);
                uriAuthority = stripTail(uriAuthority, HTTPS_PORT);
            }
            uriAuthority = stripTail(uriAuthority, DOT);
            uriAuthority = stripPrefix(uriAuthority, DOT);
        } else {
            if (uriScheme != null && base != null && uriScheme.equals(base.getScheme())) {
                uriScheme = null;
            }
        }
        uriPath = ensureMinimalEscaping(uriPath, charset);
        uriQuery = ensureMinimalEscaping(uriQuery, charset, LaxURLCodec.QUERY_SAFE);
        MutableString s = new MutableString(((uriScheme != null) ? uriScheme.length() : 0) + 1 + ((uriAuthority != null) ? uriAuthority.length() : 0) + 2 + ((uriPath != null) ? uriPath.length() : 0) + 1 + ((uriQuery != null) ? uriQuery.length() : 0));
        appendNonNull(s, uriScheme, ":", true);
        appendNonNull(s, uriAuthority, "//", false);
        appendNonNull(s, uriPath, "", false);
        appendNonNull(s, uriQuery, "?", false);
        return s.toString();
    }

    /**
     * If http(s) scheme, check scheme specific part begins '//'.
     * @throws URIException 
     * @see http://www.faqs.org/rfcs/rfc1738.html Section 3.1. Common Internet
     * Scheme Syntax
     */
    protected void checkHttpSchemeSpecificPartSlashPrefix(final URI base, final String scheme, final String schemeSpecificPart) throws URIException {
        if (base != null) {
            return;
        }
        if (scheme == null || scheme.length() <= 0) {
            return;
        }
        if (!scheme.equals("http") && !scheme.equals("https")) {
            return;
        }
        if (!schemeSpecificPart.startsWith("//")) {
            throw new URIException("http scheme specific part must " + "begin '//': " + schemeSpecificPart);
        }
        if (schemeSpecificPart.length() <= 2) {
            throw new URIException("http scheme specific part is " + "too short: " + schemeSpecificPart);
        }
    }

    /**
     * Fixup 'authority' portion of URI, by removing any stray 
     * encoded spaces, lowercasing any domain names, and applying
     * IDN-punycoding to Unicode domains. 
     * 
     * @param uriAuthority the authority string to fix
     * @return fixed version
     * @throws URIException
     */
    private String fixupAuthority(String uriAuthority) throws URIException {
        if (uriAuthority != null) {
            while (uriAuthority.endsWith(ESCAPED_SPACE)) {
                uriAuthority = uriAuthority.substring(0, uriAuthority.length() - 3);
            }
            int atIndex = uriAuthority.indexOf(COMMERCIAL_AT);
            int portColonIndex = uriAuthority.indexOf(COLON, (atIndex < 0) ? 0 : atIndex);
            if (atIndex < 0 && portColonIndex < 0) {
                return fixupDomainlabel(uriAuthority);
            } else if (atIndex < 0 && portColonIndex > -1) {
                String domain = fixupDomainlabel(uriAuthority.substring(0, portColonIndex));
                String port = uriAuthority.substring(portColonIndex);
                return domain + port;
            } else if (atIndex > -1 && portColonIndex < 0) {
                String userinfo = uriAuthority.substring(0, atIndex + 1);
                String domain = fixupDomainlabel(uriAuthority.substring(atIndex + 1));
                return userinfo + domain;
            } else {
                String userinfo = uriAuthority.substring(0, atIndex + 1);
                String domain = fixupDomainlabel(uriAuthority.substring(atIndex + 1, portColonIndex));
                String port = uriAuthority.substring(portColonIndex);
                return userinfo + domain + port;
            }
        }
        return uriAuthority;
    }

    /**
     * Fixup the domain label part of the authority.
     * 
     * We're more lax than the spec. in that we allow underscores.
     * 
     * @param label Domain label to fix.
     * @return Return fixed domain label.
     * @throws URIException
     */
    private String fixupDomainlabel(String label) throws URIException {
        try {
            label = IDNA.toASCII(label);
        } catch (IDNAException e) {
            if (TextUtils.matches(ACCEPTABLE_ASCII_DOMAIN, label)) {
            } else {
                URIException ue = new URIException(e + " " + label);
                ue.initCause(e);
                throw ue;
            }
        }
        label = label.toLowerCase();
        return label;
    }

    /**
     * Ensure that there all characters needing escaping
     * in the passed-in String are escaped. Stray '%' characters
     * are *not* escaped, as per browser behavior. 
     * 
     * @param u String to escape
     * @param charset 
     * @return string with any necessary escaping applied
     */
    private String ensureMinimalEscaping(String u, final String charset) {
        return ensureMinimalEscaping(u, charset, LaxURLCodec.EXPANDED_URI_SAFE);
    }

    /**
     * Ensure that there all characters needing escaping
     * in the passed-in String are escaped. Stray '%' characters
     * are *not* escaped, as per browser behavior. 
     * 
     * @param u String to escape
     * @param charset 
     * @param bitset 
     * @return string with any necessary escaping applied
     */
    private String ensureMinimalEscaping(String u, final String charset, final BitSet bitset) {
        if (u == null) {
            return null;
        }
        for (int i = 0; i < u.length(); i++) {
            char c = u.charAt(i);
            if (!bitset.get(c)) {
                try {
                    u = LaxURLCodec.DEFAULT.encode(bitset, u, charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return u;
    }

    /**
     * Escape any whitespace found.
     * 
     * The parent class takes care of the bulk of escaping.  But if any
     * instance of escaping is found in the URI, then we ask for parent
     * to do NO escaping.  Here we escape any whitespace found irrespective
     * of whether the uri has already been escaped.  We do this for
     * case where uri has been judged already-escaped only, its been
     * incompletly done and whitespace remains.  Spaces, etc., in the URI are
     * a real pain.  Their presence will break log file and ARC parsing.
     * @param uri URI string to check.
     * @return uri with spaces escaped if any found.
     */
    protected String escapeWhitespace(String uri) {
        MutableString buffer = null;
        for (int i = 0; i < uri.length(); i++) {
            char c = uri.charAt(i);
            if (Character.isWhitespace(c)) {
                if (buffer == null) {
                    buffer = new MutableString(uri.length() + 2);
                    buffer.append(uri.substring(0, i));
                }
                buffer.append("%");
                String hexStr = Integer.toHexString(c);
                if ((hexStr.length() % 2) > 0) {
                    buffer.append("0");
                }
                buffer.append(hexStr);
            } else {
                if (buffer != null) {
                    buffer.append(c);
                }
            }
        }
        return (buffer != null) ? buffer.toString() : uri;
    }

    /**
     * Check port on passed http authority.  Make sure the size is not larger
     * than allowed: See the 'port' definition on this
     * page, http://www.kerio.com/manual/wrp/en/418.htm.
     * Also, we've seen port numbers of '0080' whose leading zeros confuse
     * the parent class. Strip the leading zeros.
     *
     * @param uriAuthority
     * @return Null or an amended port number.
     * @throws URIException
     */
    private String checkPort(String uriAuthority) throws URIException {
        Matcher m = PORTREGEX.matcher(uriAuthority);
        if (m.matches()) {
            String no = m.group(2);
            if (no != null && no.length() > 0) {
                while (no.charAt(0) == '0' && no.length() > 1) {
                    no = no.substring(1);
                }
                uriAuthority = m.group(1) + no;
                int portNo = Integer.parseInt(no);
                if (portNo <= 0 || portNo > 65535) {
                    throw new URIException("Port out of bounds: " + uriAuthority);
                }
            }
        }
        return uriAuthority;
    }

    /**
     * @param b Buffer to append to.
     * @param str String to append if not null.
     * @param substr Suffix or prefix to use if <code>str</code> is not null.
     * @param suffix True if <code>substr</code> is a suffix.
     */
    private void appendNonNull(MutableString b, String str, String substr, boolean suffix) {
        if (str != null && str.length() > 0) {
            if (!suffix) {
                b.append(substr);
            }
            b.append(str);
            if (suffix) {
                b.append(substr);
            }
        }
    }

    /**
     * @param str String to work on.
     * @param prefix Prefix to strip if present.
     * @return <code>str</code> w/o <code>prefix</code>.
     */
    private String stripPrefix(String str, String prefix) {
        return str.startsWith(prefix) ? str.substring(prefix.length(), str.length()) : str;
    }

    /**
     * @param str String to work on.
     * @param tail Tail to strip if present.
     * @return <code>str</code> w/o <code>tail</code>.
     */
    private static String stripTail(String str, String tail) {
        return str.endsWith(tail) ? str.substring(0, str.length() - tail.length()) : str;
    }

    /**
     * @param element to examine.
     * @return Null if passed null or an empty string otherwise
     * <code>element</code>.
     */
    private String checkUriElement(String element) {
        return (element == null || element.length() <= 0) ? null : element;
    }

    /**
     * @param element to examine and lowercase if non-null.
     * @return Null if passed null or an empty string otherwise
     * <code>element</code> lowercased.
     */
    private String checkUriElementAndLowerCase(String element) {
        String tmp = checkUriElement(element);
        return (tmp != null) ? tmp.toLowerCase() : tmp;
    }
}
