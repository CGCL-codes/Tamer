package de.uni_hamburg.golem.target;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class generates an MD5 hash using the data algorithim specified by WebCT
 *
 */
public class WebCTDigest {

    private static Log log = LogFactory.getLog(WebCTDigest.class);

    public String getDigest(String secret, String[] data) {
        int count = 0;
        int size = data.length;
        StringBuffer tempBuffer = new StringBuffer();
        for (int i = 0; i < size; i++) {
            tempBuffer.append(data[i]);
        }
        byte[] data_array = tempBuffer.toString().getBytes();
        size = data_array.length;
        for (int z = 0; z < size; z++) {
            count += data_array[z];
        }
        StringBuffer sb = new StringBuffer();
        sb.append(count);
        sb.append(secret);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            log.warn(e.toString());
            return "";
        }
        String digest_data = sb.toString();
        byte[] data_bytes = md5.digest(digest_data.getBytes());
        StringBuffer hexString = new StringBuffer();
        String hex = null;
        for (int i = 0; i < data_bytes.length; i++) {
            hex = Integer.toHexString(0xFF & data_bytes[i]);
            if (hex.length() < 2) {
                hexString.append("0");
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public int getChecksum(File f) throws java.io.IOException {
        int i = 0;
        int count = 0;
        BufferedReader r = new BufferedReader(new FileReader(f));
        while (true) {
            i = r.read();
            if (i == -1) break;
            count += i;
        }
        return count;
    }

    public int getChecksum(String f) throws java.io.IOException {
        int i = 0;
        int count = 0;
        BufferedReader r = new BufferedReader(new StringReader(f));
        while (true) {
            i = r.read();
            if (i == -1) break;
            count += i;
        }
        return count;
    }
}
