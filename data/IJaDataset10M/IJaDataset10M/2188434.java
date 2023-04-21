package edu.mit.lcs.haystack.proxy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @version 	1.0
 * @author		Dennis Quan
 */
public class DefaultMarshaller implements IMarshaller {

    public static StringBuffer stringify(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String str = Byte.toString(b[i]);
            int c = 5 - str.length();
            for (int j = 0; j < c; j++) {
                str = str + " ";
            }
            sb.append(str);
        }
        return sb;
    }

    public static byte[] destringify(String s) {
        byte[] out = new byte[s.length() / 5];
        for (int i = 0; i < s.length() / 5; i++) {
            String str = s.substring(i * 5, (i + 1) * 5);
            str = str.substring(0, str.indexOf(" "));
            out[i] = Byte.parseByte(str);
        }
        return out;
    }

    /**
	 * @see IMarshaller#encode(Object)
	 */
    public String encode(Object o) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            return stringify(baos.toByteArray()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * @see IMarshaller#decode(String)
	 */
    public Object decode(String str) {
        try {
            Object o = new ObjectInputStream(new ByteArrayInputStream(destringify(str))).readObject();
            return o;
        } catch (Exception e) {
            return str;
        }
    }
}
