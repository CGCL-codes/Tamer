package com.ericdaugherty.mail.server.crypto;

import java.security.AccessController;
import java.security.Provider;

/**
 *
 * @author Andreas Kyrmegalos
 */
public final class JESProvider extends Provider {

    private static final long serialVersionUID = 6812507587804302833L;

    private static final String info = "JES Provider";

    private static final JESProvider instance = new JESProvider();

    public static JESProvider getInstance() {
        return instance;
    }

    private JESProvider() {
        super("JES", 1.0, info);
        AccessController.doPrivileged(new java.security.PrivilegedAction() {

            public Object run() {
                put("MessageDigest.MD5", "com.ericdaugherty.mail.server.crypto.digest.MD5Digest");
                put("MessageDigest.SHA", "com.ericdaugherty.mail.server.crypto.digest.SHA1Digest");
                put("MessageDigest.SHA-256", "com.ericdaugherty.mail.server.crypto.digest.SHA256Digest");
                put("MessageDigest.SHA-384", "com.ericdaugherty.mail.server.crypto.digest.SHA384Digest");
                put("MessageDigest.SHA-512", "com.ericdaugherty.mail.server.crypto.digest.SHA512Digest");
                put("Alg.Alias.MessageDigest.SHA-1", "SHA");
                put("Alg.Alias.MessageDigest.SHA1", "SHA");
                put("JESMac.HmacMD5", "com.ericdaugherty.mail.server.crypto.mac.MacBase$HmacMD5");
                put("JESMac.HmacSHA1", "com.ericdaugherty.mail.server.crypto.mac.MacBase$HmacSHA1");
                put("JESMac.HmacSHA256", "com.ericdaugherty.mail.server.crypto.mac.MacBase$HmacSHA256");
                put("JESMac.HmacSHA384", "com.ericdaugherty.mail.server.crypto.mac.MacBase$HmacSHA384");
                put("JESMac.HmacSHA512", "com.ericdaugherty.mail.server.crypto.mac.MacBase$HmacSHA512");
                put("Alg.Alias.JESMac.HmacSHA-1", "HmacSHA1");
                put("Alg.Alias.JESMac.HmacSHA-256", "HmacSHA256");
                put("Alg.Alias.JESMac.HmacSHA-384", "HmacSHA384");
                put("Alg.Alias.JESMac.HmacSHA-512", "HmacSHA512");
                return null;
            }
        });
    }
}
