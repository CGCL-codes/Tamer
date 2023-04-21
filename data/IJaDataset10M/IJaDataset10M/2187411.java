package ejb.bprocess.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Encoder;
import sun.misc.CharacterEncoder;

/**
 *
 * @author root
 */
public class EncryptionPassword {

    private static EncryptionPassword instance;

    /** Creates a new instance of EncryptionPassword */
    public EncryptionPassword() {
    }

    public synchronized String encrypt(String plaintext) throws Exception {
        return plaintext;
    }

    public static synchronized EncryptionPassword getInstance() {
        if (instance == null) {
            instance = new EncryptionPassword();
        }
        return instance;
    }
}
