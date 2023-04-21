package chaski.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Qin Gao
 * 
 * The class can encode / decode binary to strings
 */
public class BinaryToStringCodec {

    private boolean compress = false;

    public BinaryToStringCodec(boolean compress) {
        this.compress = compress;
    }

    /**
	 * Decode the string into binary, in this very implementation, we use two bytes to
	 * encode one byte, using the Hex representations : ie 0D0A etc
	 * @param rep
	 * @return
	 */
    public byte[] decodeString(String rep) {
        byte[] ret = new byte[rep.length() / 2];
        for (int i = 0; i < rep.length(); i += 2) {
            int ss = Integer.parseInt(rep.substring(i, i + 2), 16);
            ret[i / 2] = (byte) ss;
        }
        return ret;
    }

    public String encodeString(byte[] r) {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < r.length; i++) {
            String s = Integer.toHexString(r[i]);
            if (s.length() >= 2) bf.append(s.substring(s.length() - 2)); else {
                bf.append("0");
                bf.append(s);
            }
        }
        return bf.toString();
    }

    public String encodeObject(Object obj) {
        ByteArrayOutputStream str = new ByteArrayOutputStream();
        try {
            if (compress) {
                GZIPOutputStream os = new GZIPOutputStream(str);
                ObjectOutputStream ost = new ObjectOutputStream(os);
                ost.writeObject(obj);
                ost.flush();
                os.finish();
                return encodeString(str.toByteArray());
            } else {
                ObjectOutputStream ost = new ObjectOutputStream(str);
                ost.writeObject(obj);
                ost.flush();
                return encodeString(str.toByteArray());
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object decodeObject(String str) {
        byte[] b = decodeString(str);
        ByteArrayInputStream bi = new ByteArrayInputStream(b);
        try {
            if (compress) {
                GZIPInputStream os = new GZIPInputStream(bi);
                ObjectInputStream ins = new ObjectInputStream(os);
                return ins.readObject();
            } else {
                ObjectInputStream ins = new ObjectInputStream(bi);
                return ins.readObject();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    static class Test implements Serializable {

        /**
		 * 
		 */
        private static final long serialVersionUID = 6385132202699243405L;

        public String s1;

        public String s2;
    }

    public static void main(String[] args) throws IOException {
        String p = "中国啊哈哈ABCDEFG";
        BinaryToStringCodec enc = new BinaryToStringCodec(true);
        byte[] b = p.getBytes();
        String rep = enc.encodeString(b);
        System.err.println(rep);
        byte[] q = enc.decodeString(rep);
        ByteArrayInputStream rd = new ByteArrayInputStream(q);
        BufferedReader rdi = new BufferedReader(new InputStreamReader(rd));
        rep = rdi.readLine();
        System.err.println(rep);
        Test t = new Test();
        t.s1 = "第一个absdagsdagsdgsdg第二个352超3偶来说噶是离开家萨克管揭示了阿大噶少了解到噶阿三多个垃圾书到了噶第二个352超3偶来说噶是离开家萨克管揭示了阿大噶少了解到噶阿三多个垃圾书到了噶第二个352超3偶来说噶是离开家萨克管揭示了阿大噶少了解到噶阿三多个垃圾书到了噶第二个352超3偶来说噶是离开家萨克管揭示了阿大噶少了解到噶阿三多个垃圾书到了噶第二个352超3偶来说噶是离开家萨克管揭示了阿大噶少了解到噶阿三多个垃圾书到了噶";
        t.s2 = "第二个352超3偶来说噶是离开家萨克管揭示了阿大噶少了解到噶阿三多个垃圾书到了噶";
        String r = enc.encodeObject(t);
        System.err.println("" + r.length());
        t = (Test) enc.decodeObject(r);
        byte[] test = new byte[1];
        BinaryToStringCodec es = new CompactB2SCodec(false);
        for (test[0] = -128; test[0] <= 127; test[0]++) {
            String sss = es.encodeString(test);
            byte[] vvv = es.decodeString(sss);
            if (vvv[0] != test[0]) {
                System.err.println("ERROR: " + test[0]);
            }
            if (test[0] == 127) break;
        }
    }
}
