package org.mitre.jsip.util;

import java.util.StringTokenizer;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.*;

public class DigestAuthentication {

    private String realm;

    private String nonce;

    private boolean stale;

    private String algorithm;

    private byte[] opaque;

    private String qop;

    private byte[] cnonce;

    private int nonceCount;

    /**
     * Create a new instance using the information passed from the server
     * in the WWW-Authenticate field
     */
    public DigestAuthentication(String authRequest) {
        parseAuthRequest(authRequest);
        if (algorithm == null) algorithm = "MD5";
    }

    /**
     * If another WWW-Authenticate field is sent, update our information
     * using this new field
     */
    public void updateAuthRequest(String authRequest) {
        parseAuthRequest(authRequest);
    }

    /**
     * See if a password authentication is required. This is normally the case if 
     * the "stale" variable from the WWW-Authenticate field is false
     */
    public boolean passwordRequired() {
        return !stale;
    }

    /**
     * Create a string to use in a Authorization header field
     */
    public String createAuthorizationHeader(String username, String password, String method, String requestUrl) throws DigestAuthenticationException {
        if (username == null || password == null || method == null || requestUrl == null) {
            throw new DigestAuthenticationException("Can't have a null value");
        }
        String a1 = calculateA1(username, password);
        String a2 = calculateA2(method, requestUrl);
        String response = calculateResponse(a1, a2);
        return "Digest username=\"" + username + "\", realm=\"" + realm + "\", nonce=\"" + nonce + "\",uri=\"" + requestUrl + "\", response=\"" + response + "\"";
    }

    private void parseAuthRequest(String authRequest) {
        StringTokenizer tokenizer = new StringTokenizer(authRequest, ",");
        while (tokenizer.hasMoreTokens()) {
            String nextItem = tokenizer.nextToken();
            int ePos = nextItem.indexOf('=');
            if (ePos < 0) break;
            String itemName = nextItem.substring(0, ePos).trim();
            String itemValue = nextItem.substring(ePos + 1).trim();
            itemValue = unquote(itemValue);
            if (itemName.equals("realm")) {
                realm = itemValue;
            } else if (itemName.equals("nonce")) {
                nonce = itemValue;
            } else if (itemName.equals("stale")) {
                stale = Boolean.getBoolean(itemValue.toLowerCase());
            } else if (itemName.equals("algorithm")) {
                algorithm = itemValue;
            } else {
                System.out.println("DigestAuthentication doesn't handle " + itemName + " right now. ");
            }
        }
    }

    private String calculateA1(String username, String passwd) throws DigestAuthenticationException {
        if (realm == null) throw new DigestAuthenticationException("Realm can't be null");
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException nsae) {
            throw new DigestAuthenticationException(nsae.toString());
        }
        String input = username + ":" + realm + ":" + passwd;
        md.update(getEncodedBytes(input));
        return toHex(md.digest());
    }

    private String calculateA2(String method, String url) throws DigestAuthenticationException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException nsae) {
            throw new DigestAuthenticationException(nsae.toString());
        }
        String input = method + ":" + url;
        md.update(getEncodedBytes(input));
        return toHex(md.digest());
    }

    private String calculateResponse(String a1s, String a2s) throws DigestAuthenticationException {
        if (nonce == null) {
            throw new DigestAuthenticationException("Can't have a null nonce value");
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException nsae) {
            throw new DigestAuthenticationException(nsae.toString());
        }
        String nString = a1s + ":" + nonce + ":" + a2s;
        md.update(getEncodedBytes(nString));
        return toHex(md.digest());
    }

    private static final char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private String toHex(byte[] resp) {
        StringBuffer buf = new StringBuffer(resp.length * 2);
        for (int i = 0; i < resp.length; i++) {
            int h = (resp[i] & 0xf0) >> 4;
            int j = (resp[i] & 0x0f);
            buf.append(new Character((char) ((h > 9) ? 'a' + h - 10 : '0' + h)));
            buf.append(new Character((char) ((j > 9) ? 'a' + j - 10 : '0' + j)));
        }
        return buf.toString();
    }

    private static final String BYTE_ENCODING = "8859_1";

    private byte[] getEncodedBytes(String buffer) throws DigestAuthenticationException {
        try {
            return buffer.getBytes(BYTE_ENCODING);
        } catch (UnsupportedEncodingException uee) {
            throw new DigestAuthenticationException("Bad Encoding");
        }
    }

    private String unquote(String value) {
        int firstIndex = value.indexOf('\"');
        if (firstIndex < 0) return value;
        int lastIndex = value.lastIndexOf('\"');
        if (lastIndex <= firstIndex) {
            return value.substring(firstIndex + 1);
        }
        return value.substring(firstIndex + 1, lastIndex).trim();
    }

    private static String sRequest = "realm=\"mitre.org\",nonce=\"ff39c021e19f510ea665510cdcfbf7f7\",stale=FALSE,algorithm=MD5";

    public static void main(String[] args) {
        DigestAuthentication da = new DigestAuthentication(sRequest);
        String responseString = null;
        try {
            responseString = da.createAuthorizationHeader("srjone@mitre.org", "srjones", "REGISTER", "sip:mitre.org:5060");
        } catch (DigestAuthenticationException dae) {
            System.err.println("We got an exception: " + dae.toString());
            System.exit(-1);
        }
        System.out.println("The response calculated is [" + responseString + "]");
        System.exit(0);
    }
}
