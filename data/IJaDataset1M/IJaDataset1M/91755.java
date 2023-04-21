package net.sf.ipm.baza;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    private static MD5 md5 = null;

    private static final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
	 * Constructor is private so you must use the getInstance method
	 */
    public MD5() {
    }

    /**
	 * This returns the singleton instance
	 */
    public static MD5 getInstance() {
        if (md5 == null) {
            md5 = new MD5();
        }
        return (md5);
    }

    public static String hashData(final byte[] dataToHash) {
        return hexStringFromBytes((calculateHash(dataToHash)));
    }

    public static String koduj(String tekst) {
        final byte[] dataToHash = tekst.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Wystąpił błąd wynikający z braku pakietu " + "odpowiedzialnego za tworzenie sumy kontrolnej!");
            e.printStackTrace();
        }
        md.update(dataToHash, 0, dataToHash.length);
        final byte[] b = md.digest();
        String hex = "";
        int msb;
        int lsb = 0;
        int i;
        for (i = 0; i < b.length; i++) {
            msb = (b[i] & 0x000000FF) / 16;
            lsb = (b[i] & 0x000000FF) % 16;
            hex = hex + hexChars[msb] + hexChars[lsb];
        }
        return hex;
    }

    private static byte[] calculateHash(final byte[] dataToHash) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Wystąpił błąd wynikający z braku pakietu " + "odpowiedzialnego za tworzenie sumy kontrolnej!");
            e.printStackTrace();
        }
        md.update(dataToHash, 0, dataToHash.length);
        return (md.digest());
    }

    public static String hexStringFromBytes(final byte[] b) {
        String hex = "";
        int msb;
        int lsb = 0;
        int i;
        for (i = 0; i < b.length; i++) {
            msb = (b[i] & 0x000000FF) / 16;
            lsb = (b[i] & 0x000000FF) % 16;
            hex = hex + hexChars[msb] + hexChars[lsb];
        }
        return (hex);
    }
}
