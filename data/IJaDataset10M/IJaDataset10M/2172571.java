package net.sf.ipm.baza;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.util.StringTokenizer;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class IPMKeys {

    private static final String KEY_STRING = "178-153-276-18-550-55-089-230";

    public static Key getKey() {
        try {
            byte[] bytes = getBytes(KEY_STRING);
            DESKeySpec pass = new DESKeySpec(bytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey s = skf.generateSecret(pass);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getBytes(String str) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StringTokenizer st = new StringTokenizer(str, "-", false);
        System.out.println("Tokenizer: " + st);
        while (st.hasMoreTokens()) {
            int i = Integer.parseInt(st.nextToken());
            bos.write((byte) i);
        }
        return bos.toByteArray();
    }
}
