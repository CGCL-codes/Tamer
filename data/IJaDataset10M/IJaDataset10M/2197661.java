package it.bova.rtmapi;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Md5Helper {

    static final String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) buf.append((char) ('0' + halfbyte)); else buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    static final String md5(String text) throws RtmApiException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] md5hash = new byte[32];
            md.update(text.getBytes("UTF-8"), 0, text.length());
            md5hash = md.digest();
            return convertToHex(md5hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RtmApiException("Md5 error: NoSuchAlgorithmException - " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RtmApiException("Md5 error: UnsupportedEncodingException - " + e.getMessage());
        }
    }
}
