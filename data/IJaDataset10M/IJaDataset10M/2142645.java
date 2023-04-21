package org.babbly.core.authorization;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class implements closely the Digest Algorithm definition as specified in
 * rfc2617. 
 * 
 * When an UAC receives a response which is an Authentication challenge from an 
 * UAS, there is an algorithm value within, that is defined by the UAS. 
 * That value defines the algorithm to be used from the UAC to create the 
 * checksum or digest. It can be either "MD5" or "MD5-sess", whereby the 
 * exact way of computing of the response depends on it. 
 * <p>
 * <b>As stated in rfc2617:</b>
 * <br>
 * If this algorithm value is not present it is assumed to be "MD5". 
 * If the algorithm value is not understood, the challenge should be ignored
 * (and a different one used, if there is more than one).
 * <p>
 * The string obtained by applying the Digest Algorithm to the data "data" with 
 * secret "secret" will be denoted by KD(secret, data), 
 * and the string obtained by applying the checksum algorithm to the data "data"
 * will be denoted H(data).
 *  
 * <pre>
 * <code>
 * For the "MD5" and "MD5-sess" algorithms:
 *
 *        H(data) = MD5(data)
 *
 *    and
 *
 *        KD(secret, data) = H(concat(secret, ":", data))
 *</code>
 *</pre>
 * 
 * @author Georgi Dimitrov (MrJ)
 *
 */
public class DigestAlgorithm {

    public enum Algorithm {

        MD5("MD5"), MD5Session("MD5-sess");

        String text = null;

        Algorithm(String text) {
            this.text = text;
        }
    }

    ;

    public enum QOP {

        auth("auth"), authint("auth-int"), unspecified(null);

        String text = null;

        QOP(String text) {
            this.text = text;
        }
    }

    ;

    private Algorithm algorithm = Algorithm.MD5;

    private String method = null;

    private QOP qop = QOP.unspecified;

    private String cnonce = null;

    private String nc = null;

    private String username = null;

    private String password = null;

    private String realm = null;

    private String nonce = null;

    private String body = null;

    private String digestURI = null;

    /**
	 * An array containing all valid values of the Hexadecimal
	 * (hex, base16, etc.) numeral system.
	 */
    private static final char hexTable[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
	 * Fast convert a byte array to a hex string with possible leading zero.
	 * <p>
	 * <b>Note: </b>This code was seen on: 
	 * 
	 * {@link}http://mindprod.com/jgloss/hex.html
	 * 
	 * @param b the byte array that is going to be converted into its hex value.
	 * @return the hex value of the given byte array.
	 */
    public static String toHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexTable[(b[i] & 0xf0) >>> 4]);
            sb.append(hexTable[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
	 * Takes as input parameters that are two types of data. The first one is
	 * the user's credentials and the second is the data received from
	 * the server within the Authentication header of the response. 
	 * Having that data the <code>computeDigest</code> method generates a valid 
	 * digest response that will be included in an Authorization header within a
	 * REGISTER request.
	 * <p>
	 * <b>Note: </b> This method is compliant with rfc2617.
	 * 
	 * @param username
	 * @param password
	 * @param realm
	 * @param nonce
	 * @param method
	 * @param digestURI
	 * @param body
	 */
    public DigestAlgorithm(String username, String password, String realm, String nonce, String method, String digestURI, String body) throws IllegalArgumentException {
        if (method == null) {
            throw new IllegalArgumentException("parameter method can not be null!");
        }
        if (realm == null) {
            throw new IllegalArgumentException("parameter realm can not be null!");
        }
        if (nonce == null) {
            throw new IllegalArgumentException("parameter nonce can not be null!");
        }
        if (digestURI == null) {
            throw new IllegalArgumentException("parameter digestURI can not be null!");
        }
        this.username = username;
        this.password = password;
        this.realm = realm;
        this.nonce = nonce;
        this.method = method;
        this.digestURI = digestURI;
        if (body != null) {
            this.body = body;
        } else {
            this.body = "";
        }
    }

    /**
	 * Calculate the value of the Digest algorithm according to rfc2617.
	 * 
	 * @return the newly computed digest value
	 */
    public String computeDigest() {
        String a1 = a1(), a2 = a2();
        if (qop != QOP.unspecified) {
            return kd(h(a1), nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + h(a2));
        } else {
            return kd(h(a1), nonce + ":" + h(a2));
        }
    }

    /**
	 * @return
	 */
    private String kd(String secret, String data) {
        return h(secret + ":" + data);
    }

    private String h(String data) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm.toString());
        } catch (NoSuchAlgorithmException e) {
        }
        byte[] digestedData = digest.digest(data.getBytes());
        return toHexString(digestedData);
    }

    private String a1() {
        if (algorithm == Algorithm.MD5) {
            return username + ":" + realm + ":" + password;
        } else if (algorithm == Algorithm.MD5Session) {
            if (cnonce == null || cnonce.length() < 1) {
                throw new NullPointerException("cnonce have to be specified for MD5-Sess algorithm.");
            } else {
                return h(username + ":" + realm + ":" + password) + ":" + nonce + ":" + cnonce;
            }
        } else {
            return null;
        }
    }

    private String a2() {
        if (qop == QOP.unspecified || qop == QOP.auth) {
            return method + ":" + digestURI;
        } else {
            return method + ":" + digestURI + ":" + h(body);
        }
    }

    public boolean isSuppliedQop() {
        return qop != null && qop != QOP.unspecified;
    }

    public void setQop(QOP qop, String cnonce, String nc) throws IllegalArgumentException {
        if (cnonce == null) {
            throw new IllegalArgumentException("parameter cnonce can not be null");
        }
        if (nc == null) {
            throw new IllegalArgumentException("parameter nc can not be null");
        }
        this.qop = qop;
        this.cnonce = cnonce;
        this.nc = nc;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public QOP getQop() {
        return qop;
    }

    public String getCnonce() {
        return cnonce;
    }

    public String getNc() {
        return nc;
    }
}
