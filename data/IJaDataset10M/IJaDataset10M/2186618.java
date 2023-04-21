package br.org.direto.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public class Base64Utils {

    public static byte[] encode(byte[] b) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream b64os = MimeUtility.encode(baos, "base64");
        b64os.write(b);
        b64os.close();
        return baos.toByteArray();
    }

    public static byte[] decode(byte[] b) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        InputStream b64is = MimeUtility.decode(bais, "base64");
        byte[] tmp = new byte[b.length];
        int n = b64is.read(tmp);
        byte[] res = new byte[n];
        System.arraycopy(tmp, 0, res, 0, n);
        return res;
    }

    public static void main(String[] args) throws Exception {
        String test = "d143t0";
        byte res1[] = Base64Utils.encode(test.getBytes());
        System.out.println(test + " base64 -> " + java.util.Arrays.toString(res1));
        System.out.println(new String(res1));
        String sEnconde = "ZDE0M3Qw";
        byte res2[] = Base64Utils.decode(sEnconde.getBytes());
        System.out.println("");
        System.out.println(java.util.Arrays.toString(res1) + " string --> " + new String(res2));
    }
}
