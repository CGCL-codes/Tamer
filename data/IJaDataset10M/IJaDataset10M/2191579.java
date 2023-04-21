package android.net;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@hide}
 *
 * Web Address Parser
 *
 * This is called WebAddress, rather than URL or URI, because it
 * attempts to parse the stuff that a user will actually type into a
 * browser address widget.
 *
 * Unlike java.net.uri, this parser will not choke on URIs missing
 * schemes.  It will only throw a ParseException if the input is
 * really hosed.
 *
 * If given an https scheme but no port, fills in port
 *
 */
public class WebAddress {

    private static final String LOGTAG = "http";

    public String mScheme;

    public String mHost;

    public int mPort;

    public String mPath;

    public String mAuthInfo;

    static final int MATCH_GROUP_SCHEME = 1;

    static final int MATCH_GROUP_AUTHORITY = 2;

    static final int MATCH_GROUP_HOST = 3;

    static final int MATCH_GROUP_PORT = 4;

    static final int MATCH_GROUP_PATH = 5;

    static Pattern sAddressPattern = Pattern.compile("(?:(http|HTTP|https|HTTPS|file|FILE)\\:\\/\\/)?" + "(?:([-A-Za-z0-9$_.+!*'(),;?&=]+(?:\\:[-A-Za-z0-9$_.+!*'(),;?&=]+)?)@)?" + "([-A-Za-z0-9%_]+(?:\\.[-A-Za-z0-9%_]+)*|\\[[0-9a-fA-F:\\.]+\\])?" + "(?:\\:([0-9]+))?" + "(\\/?.*)?");

    /** parses given uriString. */
    public WebAddress(String address) throws ParseException {
        if (address == null) {
            throw new NullPointerException();
        }
        mScheme = "";
        mHost = "";
        mPort = -1;
        mPath = "/";
        mAuthInfo = "";
        Matcher m = sAddressPattern.matcher(address);
        String t;
        if (m.matches()) {
            t = m.group(MATCH_GROUP_SCHEME);
            if (t != null) mScheme = t;
            t = m.group(MATCH_GROUP_AUTHORITY);
            if (t != null) mAuthInfo = t;
            t = m.group(MATCH_GROUP_HOST);
            if (t != null) mHost = t;
            t = m.group(MATCH_GROUP_PORT);
            if (t != null) {
                try {
                    mPort = Integer.parseInt(t);
                } catch (NumberFormatException ex) {
                    throw new ParseException("Bad port");
                }
            }
            t = m.group(MATCH_GROUP_PATH);
            if (t != null && t.length() > 0) {
                if (t.charAt(0) == '/') {
                    mPath = t;
                } else {
                    mPath = "/" + t;
                }
            }
        } else {
            throw new ParseException("Bad address");
        }
        if (mPort == 443 && mScheme.equals("")) {
            mScheme = "https";
        } else if (mPort == -1) {
            if (mScheme.equals("https")) mPort = 443; else mPort = 80;
        }
        if (mScheme.equals("")) mScheme = "http";
    }

    public String toString() {
        String port = "";
        if ((mPort != 443 && mScheme.equals("https")) || (mPort != 80 && mScheme.equals("http"))) {
            port = ":" + Integer.toString(mPort);
        }
        String authInfo = "";
        if (mAuthInfo.length() > 0) {
            authInfo = mAuthInfo + "@";
        }
        return mScheme + "://" + authInfo + mHost + port + mPath;
    }
}
