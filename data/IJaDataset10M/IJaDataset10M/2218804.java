package traitmap.util;

import java.security.MessageDigest;
import org.apache.log4j.Logger;

/** 
 * TraitMap�f�[�^�x�[�X�y�щ{���V�X�e��, �Q�O�O�R�N�P��<br>
 * �S���ҁF�L�c�N�Y�A�Q�m���m���x�[�X�����J���`�[���E�C���t�H�}�e�B�N�X��Վ{�݁EGSC�ERIKEN
 * 
 * @version 2.2
 * @author moroda
 */
public class Encryption {

    static Logger log = Logger.getLogger(Encryption.class);

    public static final String decryptXOR(String message, String key) {
        if (message == null || message.length() < 8) {
            return null;
        }
        if (key == null || key.length() <= 0) {
            return null;
        }
        String prand = "";
        String decMessage = "";
        byte[] keybytes = new byte[0];
        try {
            keybytes = key.getBytes("ISO-8859-1");
            for (int i = 0; i < keybytes.length; i++) {
                prand += Byte.toString(keybytes[i]);
            }
            int sPos = (int) Math.floor(prand.length() / 5) - 1;
            long mult = Long.parseLong("" + prand.charAt(sPos) + prand.charAt(sPos * 2) + prand.charAt(sPos * 3) + prand.charAt(sPos * 4) + prand.charAt(sPos * 5));
            long incr = Math.round(key.length() / 2.0);
            long modu = (long) Math.pow(2, 31) - 1;
            long salt = Long.parseLong(message.substring(message.length() - 8, message.length()), 16);
            message = message.substring(0, message.length() - 8);
            prand = prand + Long.toString(salt);
            long temprand = 0;
            int times = (int) Math.floor(prand.length() / 10.0);
            for (int i = 0; i < times - 1; i++) {
                temprand += Long.parseLong(prand.substring(10 * i, 10 * (i + 1)));
            }
            String lastvalue = prand.substring(10 * times, prand.length());
            lastvalue = lastvalue.length() > 0 ? lastvalue : "0";
            prand = Long.toString(temprand + Long.parseLong(lastvalue));
            prand = Long.toString((mult * Long.parseLong(prand) + incr) % modu);
            for (int i = 0; i < message.length(); i += 2) {
                int _a = Integer.parseInt(message.substring(i, i + 2), 16);
                int _b = (int) Math.floor((Double.parseDouble(prand) / modu) * 255);
                byte[] decByte = { Byte.parseByte(Integer.toString(_a ^ _b)) };
                decMessage += new String(decByte, "ISO-8859-1");
                prand = Long.toString((mult * Long.parseLong(prand) + incr) % modu);
            }
        } catch (Exception e) {
            log.error("password decrypt error");
        }
        return decMessage;
    }

    public static final String digestMD5(String message) {
        byte[] digest = new byte[0];
        StringBuffer digestMessage = new StringBuffer();
        try {
            digest = MessageDigest.getInstance("MD5").digest(message.getBytes("ISO-8859-1"));
            for (int i = 0; i < digest.length; i++) {
                digestMessage.append(Integer.toString((digest[i] & 0xf0) >> 4, 16));
                digestMessage.append(Integer.toString(digest[i] & 0x0f, 16));
            }
        } catch (Exception e) {
            log.error(e);
        }
        return digestMessage.toString();
    }
}
