package helpers;

import controllers.front.profile.ProfileSettings;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Onno Valkering
 */
public class Converter {

    /**
     * convert a string to hex
     * @param input : byte[]
     * @return hex output : String
     */
    private static String convertToHex(byte[] input) {
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            int halfbyte = (input[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    sBuilder.append((char) ('0' + halfbyte));
                } else {
                    sBuilder.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = input[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return sBuilder.toString();
    }

    /**
     * convert a string to SHA1
     * @param text
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    public static String SHA1(String text) {
        byte[] sha1hash = new byte[40];
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha1hash = md.digest();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return convertToHex(sha1hash);
    }

    public static String date(String orignalDate, String orginalFormat, String newFormat) {
        Date parsedCreationDate = new Date();
        String formattedCreationDate = new String();
        try {
            parsedCreationDate = new SimpleDateFormat(orginalFormat).parse(orignalDate);
            formattedCreationDate = new SimpleDateFormat(newFormat).format(parsedCreationDate);
        } catch (ParseException ex) {
            Logger.getLogger(ProfileSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formattedCreationDate;
    }
}
