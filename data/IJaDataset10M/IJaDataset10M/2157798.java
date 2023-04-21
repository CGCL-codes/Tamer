package sun.net.www.protocol.http;

import java.io.IOException;
import java.net.URL;
import java.net.ProtocolException;
import java.net.PasswordAuthentication;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Random;
import sun.net.www.HeaderParser;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class DigestAuthentication extends AuthenticationInfo {

    static final char DIGEST_AUTH = 'D';

    PasswordAuthentication pw;

    private String authMethod;

    static class Parameters {

        private boolean serverQop;

        private String opaque;

        private String cnonce;

        private String nonce;

        private String algorithm;

        private int NCcount;

        private String cachedHA1;

        private boolean redoCachedHA1 = true;

        private static int defaultCnonceRepeat = 5;

        private static int cnonceRepeat;

        private static final int cnoncelen = 40;

        private static Random random;

        static {
            cnonceRepeat = ((Integer) java.security.AccessController.doPrivileged(new sun.security.action.GetIntegerAction("http.auth.digest.cnonceRepeat", defaultCnonceRepeat))).intValue();
            random = new Random();
        }

        Parameters() {
            serverQop = false;
            opaque = null;
            algorithm = null;
            cachedHA1 = null;
            nonce = null;
            setNewCnonce();
        }

        boolean authQop() {
            return serverQop;
        }

        synchronized int getNCCount() {
            return NCcount;
        }

        synchronized String getCnonce() {
            if (NCcount >= cnonceRepeat) {
                setNewCnonce();
            }
            NCcount++;
            return cnonce;
        }

        synchronized void setNewCnonce() {
            byte bb[] = new byte[cnoncelen / 2];
            char cc[] = new char[cnoncelen];
            random.nextBytes(bb);
            for (int i = 0; i < (cnoncelen / 2); i++) {
                int x = bb[i] + 128;
                cc[i * 2] = (char) ('A' + x / 16);
                cc[i * 2 + 1] = (char) ('A' + x % 16);
            }
            cnonce = new String(cc, 0, cnoncelen);
            NCcount = 0;
            redoCachedHA1 = true;
        }

        synchronized void setQop(String qop) {
            if (qop != null) {
                StringTokenizer st = new StringTokenizer(qop, " ");
                while (st.hasMoreTokens()) {
                    if (st.nextToken().equalsIgnoreCase("auth")) {
                        serverQop = true;
                        return;
                    }
                }
            }
            serverQop = false;
        }

        synchronized String getOpaque() {
            return opaque;
        }

        synchronized void setOpaque(String s) {
            opaque = s;
        }

        synchronized String getNonce() {
            return nonce;
        }

        synchronized void setNonce(String s) {
            nonce = s;
            redoCachedHA1 = true;
        }

        synchronized String getCachedHA1() {
            if (redoCachedHA1) {
                return null;
            } else {
                return cachedHA1;
            }
        }

        synchronized void setCachedHA1(String s) {
            cachedHA1 = s;
            redoCachedHA1 = false;
        }

        synchronized String getAlgorithm() {
            return algorithm;
        }

        synchronized void setAlgorithm(String s) {
            algorithm = s;
        }
    }

    Parameters params;

    /**
     * Create a DigestAuthentication
     */
    public DigestAuthentication(boolean isProxy, URL url, String realm, String authMethod, PasswordAuthentication pw, Parameters params) {
        super(isProxy ? PROXY_AUTHENTICATION : SERVER_AUTHENTICATION, DIGEST_AUTH, url, realm);
        this.authMethod = authMethod;
        this.pw = pw;
        this.params = params;
    }

    public DigestAuthentication(boolean isProxy, String host, int port, String realm, String authMethod, PasswordAuthentication pw, Parameters params) {
        super(isProxy ? PROXY_AUTHENTICATION : SERVER_AUTHENTICATION, DIGEST_AUTH, host, port, realm);
        this.authMethod = authMethod;
        this.pw = pw;
        this.params = params;
    }

    /**
     * @return true if this authentication supports preemptive authorization
     */
    boolean supportsPreemptiveAuthorization() {
        return true;
    }

    /**
     * @return the name of the HTTP header this authentication wants set
     */
    String getHeaderName() {
        if (type == SERVER_AUTHENTICATION) {
            return "Authorization";
        } else {
            return "Proxy-Authorization";
        }
    }

    /**
     * Reclaculates the request-digest and returns it.
     * @return the value of the HTTP header this authentication wants set
     */
    String getHeaderValue(URL url, String method) {
        return getHeaderValueImpl(url.getFile(), method);
    }

    /**
     * Check if the header indicates that the current auth. parameters are stale.
     * If so, then replace the relevant field with the new value
     * and return true. Otherwise return false.
     * returning true means the request can be retried with the same userid/password
     * returning false means we have to go back to the user to ask for a new
     * username password.
     */
    boolean isAuthorizationStale(String header) {
        HeaderParser p = new HeaderParser(header);
        String s = p.findValue("stale");
        if (s == null || !s.equals("true")) return false;
        String newNonce = p.findValue("nonce");
        if (newNonce == null || "".equals(newNonce)) {
            return false;
        }
        params.setNonce(newNonce);
        return true;
    }

    /**
     * Set header(s) on the given connection.
     * @param conn The connection to apply the header(s) to
     * @param p A source of header values for this connection, if needed.
     * @param raw Raw header values for this connection, if needed.
     * @return true if all goes well, false if no headers were set.
     */
    boolean setHeaders(HttpURLConnection conn, HeaderParser p, String raw) {
        params.setNonce(p.findValue("nonce"));
        params.setOpaque(p.findValue("opaque"));
        params.setQop(p.findValue("qop"));
        String uri = conn.getURL().getFile();
        if (params.nonce == null || authMethod == null || pw == null || realm == null) {
            return false;
        }
        if (authMethod.length() >= 1) {
            authMethod = Character.toUpperCase(authMethod.charAt(0)) + authMethod.substring(1).toLowerCase();
        }
        String algorithm = p.findValue("algorithm");
        if (algorithm == null || "".equals(algorithm)) {
            algorithm = "MD5";
        }
        params.setAlgorithm(algorithm);
        if (params.authQop()) {
            params.setNewCnonce();
        }
        String value = getHeaderValueImpl(uri, conn.getMethod());
        if (value != null) {
            conn.setAuthenticationProperty(getHeaderName(), value);
            return true;
        } else {
            return false;
        }
    }

    private String getHeaderValueImpl(String uri, String method) {
        String response;
        char[] passwd = pw.getPassword();
        boolean qop = params.authQop();
        String opaque = params.getOpaque();
        String cnonce = params.getCnonce();
        String nonce = params.getNonce();
        String algorithm = params.getAlgorithm();
        int cncount = params.getNCCount();
        String cnstring = null;
        if (cncount != -1) {
            cnstring = Integer.toHexString(cncount).toLowerCase();
            int len = cnstring.length();
            if (len < 8) cnstring = zeroPad[len] + cnstring;
        }
        try {
            response = computeDigest(true, pw.getUserName(), passwd, realm, method, uri, nonce, cnonce, cnstring);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
        String value = authMethod + " username=\"" + pw.getUserName() + "\", realm=\"" + realm + "\", nonce=\"" + nonce + "\", uri=\"" + uri + "\", response=\"" + response + "\", algorithm=\"" + algorithm;
        if (opaque != null) {
            value = value + "\", opaque=\"" + opaque;
        }
        if (cnonce != null) {
            value = value + "\", nc=" + cnstring;
            value = value + ", cnonce=\"" + cnonce;
        }
        if (qop) {
            value = value + "\", qop=\"auth";
        }
        value = value + "\"";
        return value;
    }

    public void checkResponse(String header, String method, URL url) throws IOException {
        String uri = url.getFile();
        char[] passwd = pw.getPassword();
        String username = pw.getUserName();
        boolean qop = params.authQop();
        String opaque = params.getOpaque();
        String cnonce = params.cnonce;
        String nonce = params.getNonce();
        String algorithm = params.getAlgorithm();
        int cncount = params.getNCCount();
        String cnstring = null;
        if (header == null) {
            throw new ProtocolException("No authentication information in response");
        }
        if (cncount != -1) {
            cnstring = Integer.toHexString(cncount).toUpperCase();
            int len = cnstring.length();
            if (len < 8) cnstring = zeroPad[len] + cnstring;
        }
        try {
            String expected = computeDigest(false, username, passwd, realm, method, uri, nonce, cnonce, cnstring);
            HeaderParser p = new HeaderParser(header);
            String rspauth = p.findValue("rspauth");
            if (rspauth == null) {
                throw new ProtocolException("No digest in response");
            }
            if (!rspauth.equals(expected)) {
                throw new ProtocolException("Response digest invalid");
            }
            String nextnonce = p.findValue("nextnonce");
            if (nextnonce != null && !"".equals(nextnonce)) {
                params.setNonce(nextnonce);
            }
        } catch (NoSuchAlgorithmException ex) {
            throw new ProtocolException("Unsupported algorithm in response");
        }
    }

    private String computeDigest(boolean isRequest, String userName, char[] password, String realm, String connMethod, String requestURI, String nonceString, String cnonce, String ncValue) throws NoSuchAlgorithmException {
        String A1, HashA1;
        String algorithm = params.getAlgorithm();
        boolean md5sess = algorithm.equalsIgnoreCase("MD5-sess");
        MessageDigest md = MessageDigest.getInstance(md5sess ? "MD5" : algorithm);
        if (md5sess) {
            if ((HashA1 = params.getCachedHA1()) == null) {
                String s = userName + ":" + realm + ":";
                String s1 = encode(s, password, md);
                A1 = s1 + ":" + nonceString + ":" + cnonce;
                HashA1 = encode(A1, null, md);
                params.setCachedHA1(HashA1);
            }
        } else {
            A1 = userName + ":" + realm + ":";
            HashA1 = encode(A1, password, md);
        }
        String A2;
        if (isRequest) {
            A2 = connMethod + ":" + requestURI;
        } else {
            A2 = ":" + requestURI;
        }
        String HashA2 = encode(A2, null, md);
        String combo, finalHash;
        if (params.authQop()) {
            combo = HashA1 + ":" + nonceString + ":" + ncValue + ":" + cnonce + ":auth:" + HashA2;
        } else {
            combo = HashA1 + ":" + nonceString + ":" + HashA2;
        }
        finalHash = encode(combo, null, md);
        return finalHash;
    }

    private static final char charArray[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static final String zeroPad[] = { "00000000", "0000000", "000000", "00000", "0000", "000", "00", "0" };

    private String encode(String src, char[] passwd, MessageDigest md) {
        md.update(src.getBytes());
        if (passwd != null) {
            byte[] passwdBytes = new byte[passwd.length];
            for (int i = 0; i < passwd.length; i++) passwdBytes[i] = (byte) passwd[i];
            md.update(passwdBytes);
            Arrays.fill(passwdBytes, (byte) 0x00);
        }
        byte[] digest = md.digest();
        StringBuffer res = new StringBuffer(digest.length * 2);
        for (int i = 0; i < digest.length; i++) {
            int hashchar = ((digest[i] >>> 4) & 0xf);
            res.append(charArray[hashchar]);
            hashchar = (digest[i] & 0xf);
            res.append(charArray[hashchar]);
        }
        return res.toString();
    }
}
