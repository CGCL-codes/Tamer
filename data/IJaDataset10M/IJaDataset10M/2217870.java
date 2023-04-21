package fi.hip.gb.disk.transport.jgroups;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Properties;
import org.jgroups.Message;
import org.jgroups.auth.AuthToken;
import org.jgroups.util.Util;
import xjava.security.Cipher;

/**
 * <p>
 * This is an example of using a preshared token that is encrypted using an 
 * X509 certificate for authentication purposes.  All members of the group 
 * have to have the same string value in the JGroups config.
 * <p>
 * This example uses certificates contained within a specified keystore.
 * Keystore can be created with command:
 * <pre>keytool -genkey -keystore .keystore -keyalg rsa -alias disk</pre>
 * <p> 
 * Configuration parameters for this example are shown below:
 * <ul>
 *  <li>keystore_type = JKS(default)/PKCS12 - see http://java.sun.com/j2se/1.4.2/docs/guide/security/CryptoSpec.html#AppA</li>
 *  <li>keystore_path (required) = the location of the keystore</li>
 *  <li>keystore_password (required) =  the password of the keystore</li>
 *  <li>cert_alias (required) = the alias of the certification within the keystore</li>
 *  <li>cert_password = the password of the certification within the keystore</li>
 *  <li>auth_value (required) = the string to encrypt</li>
 *  <li>cert_required = is the certificate required to exist, if false then we are able to 
 *      fallback to plain-text authenication</li>
 *  <li>cipher_type = RSA(default)/AES/Blowfish/DES/DESede/PBEWithMD5AndDES/PBEWithHmacSHA1AndDESede/RC2/RC4/RC5 - see http://java.sun.com/j2se/1.4.2/docs/guide/security/jce/JCERefGuide.html#AppA</li>
 * </ul>
 * <p>
 * Notice! This class is modified to work without certificates. If the keystore 
 * file is not found  and cert_require=false, we compare the auth_value as plain text. 
 * This is to be able to use single config file for all configurations with and without the 
 * certificates. Make sure to keep cert_require always true on production systems.
 * 
 * @see org.jgroups.auth.AuthToken
 * @author Chris Mills
 * @author Juho Karppinen
 */
public class X509Token1_5 extends AuthToken {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String KEYSTORE_TYPE = "keystore_type";

    public static final String KEYSTORE_PATH = "keystore_path";

    public static final String KEYSTORE_PASSWORD = "keystore_password";

    public static final String CERT_ALIAS = "cert_alias";

    public static final String CERT_PASSWORD = "cert_password";

    public static final String CERT_REQUIRED = "cert_required";

    public static final String TOKEN_ATTR = "auth_value";

    public static final String CIPHER_TYPE = "cipher_type";

    private boolean valueSet = false;

    private String keystore_type = null;

    private String cert_alias = null;

    private String keystore_path = null;

    private String token_attr = null;

    private String cipher_type = null;

    private boolean cert_required = true;

    private byte[] encryptedToken = null;

    private char[] cert_password = null;

    private char[] keystore_password = null;

    private Cipher cipher = null;

    private PrivateKey certPrivateKey = null;

    private X509Certificate certificate = null;

    public X509Token1_5() {
    }

    public void setValue(Properties properties) {
        if (log.isDebugEnabled()) {
            log.debug("setting values on X509Token1_5 object");
        }
        if (properties.containsKey(X509Token1_5.TOKEN_ATTR)) {
            this.token_attr = (String) properties.get(X509Token1_5.TOKEN_ATTR);
            properties.remove(X509Token1_5.TOKEN_ATTR);
            if (log.isDebugEnabled()) log.debug("token_attr = " + this.token_attr);
        }
        if (properties.containsKey(X509Token1_5.KEYSTORE_TYPE)) {
            this.keystore_type = (String) properties.get(X509Token1_5.KEYSTORE_TYPE);
            properties.remove(X509Token1_5.KEYSTORE_TYPE);
        } else {
            this.keystore_type = "JKS";
        }
        if (log.isDebugEnabled()) log.debug("keystore_type = " + this.keystore_type);
        if (properties.containsKey(X509Token1_5.KEYSTORE_PATH)) {
            this.keystore_path = (String) properties.get(X509Token1_5.KEYSTORE_PATH);
            properties.remove(X509Token1_5.KEYSTORE_PATH);
            if (log.isDebugEnabled()) log.debug("keystore_path = " + new String(this.keystore_path));
        }
        if (properties.containsKey(X509Token1_5.KEYSTORE_PASSWORD)) {
            this.keystore_password = ((String) properties.get(X509Token1_5.KEYSTORE_PASSWORD)).toCharArray();
            properties.remove(X509Token1_5.KEYSTORE_PASSWORD);
            if (log.isDebugEnabled()) log.debug("keystore_password = " + new String(this.keystore_password));
        }
        if (properties.containsKey(X509Token1_5.CERT_ALIAS)) {
            this.cert_alias = (String) properties.get(X509Token1_5.CERT_ALIAS);
            properties.remove(X509Token1_5.CERT_ALIAS);
            if (log.isDebugEnabled()) log.debug("cert_alias = " + this.cert_alias);
        }
        if (properties.containsKey(X509Token1_5.CERT_PASSWORD)) {
            this.cert_password = ((String) properties.get(X509Token1_5.CERT_PASSWORD)).toCharArray();
            properties.remove(X509Token1_5.CERT_PASSWORD);
        } else {
            this.cert_password = this.keystore_password;
        }
        if (log.isDebugEnabled()) log.debug("cert_password = " + new String(this.cert_password));
        if (properties.containsKey(X509Token1_5.CIPHER_TYPE)) {
            this.cipher_type = (String) properties.get(X509Token1_5.CIPHER_TYPE);
            properties.remove(X509Token1_5.CIPHER_TYPE);
        } else {
            this.cipher_type = "RSA";
        }
        if (log.isDebugEnabled()) log.debug("cipher_type = " + this.cipher_type);
        if (properties.containsKey(X509Token1_5.CERT_REQUIRED)) {
            this.cert_required = Boolean.parseBoolean((String) properties.get(X509Token1_5.CERT_REQUIRED));
            properties.remove(X509Token1_5.CERT_REQUIRED);
        } else {
            this.cert_required = true;
        }
        if (log.isDebugEnabled()) log.debug("cert_required = " + this.cert_required);
        if (getCertificate()) {
            this.valueSet = true;
            if (log.isDebugEnabled()) {
                log.debug("X509Token1_5 created correctly");
            }
        }
    }

    public String getName() {
        return this.getClass().getName();
    }

    public boolean authenticate(AuthToken token, Message msg) {
        if (!this.valueSet) {
            if (log.isFatalEnabled()) {
                log.fatal("X509Token1_5 not setup correctly - check token attrs");
            }
            return false;
        }
        if ((token != null) && (token instanceof X509Token1_5)) {
            X509Token1_5 serverToken = (X509Token1_5) token;
            if (!serverToken.valueSet) {
                if (log.isFatalEnabled()) {
                    log.fatal("X509Token1_5 - recieved token not valid");
                }
                return false;
            }
            try {
                if (this.cipher != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("setting cipher to decrypt mode");
                    }
                    this.cipher.initDecrypt(this.certPrivateKey);
                    String serverBytes = new String(this.cipher.doFinal(serverToken.encryptedToken));
                    if ((serverBytes != null) && (serverBytes.equalsIgnoreCase(this.token_attr))) {
                        if (log.isDebugEnabled()) {
                            log.debug("X509 authentication passed");
                        }
                        return true;
                    }
                } else if (this.cert_required == false) {
                    log.debug("using plain-text authentication");
                    return new String(serverToken.encryptedToken).equalsIgnoreCase(this.token_attr);
                }
            } catch (Exception e) {
                if (log.isFatalEnabled()) {
                    log.fatal(e);
                }
            }
        }
        if (log.isWarnEnabled()) {
            log.warn("X509 authentication failed");
        }
        return false;
    }

    public void writeTo(DataOutputStream out) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("X509Token1_5 writeTo() " + this.encryptedToken);
        }
        Util.writeByteBuffer(this.encryptedToken, out);
    }

    public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {
        if (log.isDebugEnabled()) {
            log.debug("X509Token1_5 readFrom()");
        }
        this.encryptedToken = Util.readByteBuffer(in);
        this.valueSet = true;
        log.debug("X509Token1_5 readFrom()= " + this.encryptedToken);
    }

    /**
     * Used during setup to get the certification from the keystore and encrypt the auth_value with the private key
     * @return true if the certificate was found and the string encypted correctly otherwise returns false
     */
    private boolean getCertificate() {
        try {
            KeyStore store = KeyStore.getInstance(this.keystore_type);
            FileInputStream fis = new FileInputStream(this.keystore_path);
            store.load(fis, this.keystore_password);
            this.cipher = Cipher.getInstance(this.cipher_type);
            this.certificate = (X509Certificate) store.getCertificate(this.cert_alias);
            if (log.isDebugEnabled()) {
                log.debug("certificate = " + this.certificate.toString());
            }
            this.cipher.initEncrypt(this.certPrivateKey);
            this.encryptedToken = this.cipher.doFinal(this.token_attr.getBytes());
            if (log.isDebugEnabled()) {
                log.debug("encryptedToken = " + this.encryptedToken);
            }
            KeyStore.PrivateKeyEntry privateKey = (KeyStore.PrivateKeyEntry) store.getEntry(this.cert_alias, new KeyStore.PasswordProtection(this.cert_password));
            this.certPrivateKey = privateKey.getPrivateKey();
            if (log.isDebugEnabled()) {
                log.debug("certPrivateKey = " + this.certPrivateKey.toString());
            }
            return true;
        } catch (FileNotFoundException e) {
            if (!this.cert_required) {
                log.warn("X509 certificate not found for JGroups AUTH, using plain-text authentication instead");
                this.encryptedToken = this.token_attr.getBytes();
                this.cipher = null;
                return true;
            } else {
                log.error(e);
            }
        } catch (Exception e) {
            log.error(e);
        }
        return false;
    }
}
