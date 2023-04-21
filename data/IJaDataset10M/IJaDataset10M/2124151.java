package wtanaka.praya.msn;

import java.security.MessageDigest;

/**
 * Authentication, a class for authentication
 *        
 * This is a almost copy from msnj.sourceforge.net
 **/
public class Authentication {

    public static String getAuth(String key, String password) {
        String both = key + password;
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            digest = md.digest(both.getBytes("US-ASCII"));
        } catch (Exception e) {
        }
        StringBuffer hash = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int b = 0x00FF & digest[i];
            String str = Integer.toHexString(b);
            if (str.length() == 1) str = "0" + str;
            hash.append(str);
        }
        return "MD5 S " + hash.toString();
    }
}
