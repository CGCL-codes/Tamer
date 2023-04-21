package net.zehrer.vse.common;

import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.BinaryEncoder;

/**
 * Some crypt and date help functions for the S3 Service.
 */
public class Authentication {

    /** Signature algorithm used for S3 authentication: {@value} */
    private static final String SIGNATURE_ALGORITHM = "HmacSHA1";

    private BinaryEncoder encoder = null;

    private Mac mac = null;

    public Authentication() {
        try {
            this.mac = Mac.getInstance(SIGNATURE_ALGORITHM);
        } catch (Exception e) {
        }
    }

    public void setKey(String secretAccessKey) {
        SecretKeySpec key = new SecretKeySpec(secretAccessKey.getBytes(), SIGNATURE_ALGORITHM);
        try {
            mac.init(key);
        } catch (Exception e) {
        }
    }

    public String generateMD5(byte[] data) {
        if (data != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(data);
                return new String(encoder.encode(md.digest()));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
	 * Sign the text with the key and encode the resutl with the encoder.
	 * 
	 * @param data
	 * @return signed and encoded text
	 */
    public String sign(String data) {
        String result = null;
        try {
            result = new String(encoder.encode(this.mac.doFinal(data.getBytes())));
        } catch (Exception e) {
        }
        return result;
    }

    public void setEncoder(BinaryEncoder encoder) {
        this.encoder = encoder;
    }
}
