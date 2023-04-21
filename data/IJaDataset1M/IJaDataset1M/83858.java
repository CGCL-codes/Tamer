package bioinfo.comaWebServer.dataManagement;

import java.security.MessageDigest;

public class PasswordManager {

    private PasswordManager() {
    }

    public static synchronized String encrypt(String x) throws Exception {
        MessageDigest d = MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(String.valueOf(x).getBytes());
        return byteArrayToHexString(d.digest());
    }

    private static synchronized String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }
}
