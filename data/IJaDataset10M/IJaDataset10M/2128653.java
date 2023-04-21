package sjtu.llgx.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Serializer {

    private static final byte __Quote = 34;

    private static final byte __0 = 48;

    private static final byte __1 = 49;

    private static final byte __Colon = 58;

    private static final byte __Semicolon = 59;

    private static final byte __N = 78;

    private static final byte __U = 85;

    private static final byte __Slash = 92;

    private static final byte __a = 97;

    private static final byte __b = 98;

    private static final byte __d = 100;

    private static final byte __i = 105;

    private static final byte __s = 115;

    private static final byte __LeftB = 123;

    private static final byte __RightB = 125;

    private static final String __NAN = "NAN";

    private static final String __INF = "INF";

    private static final String __NINF = "-INF";

    public static String serialize(Object obj) {
        return serialize(obj, JavaCenterHome.JCH_CHARSET);
    }

    @SuppressWarnings("unchecked")
    public static String serialize(Object obj, String charset) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        serialize(stream, obj, charset);
        String result = null;
        try {
            result = stream.toString(charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static void serialize(ByteArrayOutputStream stream, Object obj, String charset) {
        if (obj == null) {
            writeNull(stream);
        } else if (obj instanceof Boolean) {
            writeBoolean(stream, ((Boolean) obj).booleanValue() ? __1 : __0);
        } else if ((obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Integer)) {
            writeInteger(stream, getAsciiBytes(obj));
        } else if (obj instanceof Long) {
            writeDouble(stream, getAsciiBytes(obj));
        } else if (obj instanceof Float) {
            Float f = (Float) obj;
            obj = f.isNaN() ? __NAN : (!f.isInfinite() ? obj : (f.floatValue() > 0 ? __INF : __NINF));
            writeDouble(stream, getAsciiBytes(obj));
        } else if (obj instanceof Double) {
            Double d = (Double) obj;
            obj = d.isNaN() ? __NAN : (!d.isInfinite() ? obj : (d.doubleValue() > 0 ? __INF : __NINF));
            writeDouble(stream, getAsciiBytes(obj));
        } else if ((obj instanceof Character) || (obj instanceof String) || (obj instanceof StringBuffer)) {
            writeString(stream, getBytes(obj, charset));
        } else if (obj instanceof Map) {
            writeMap(stream, (Map) obj, charset);
        } else if (obj instanceof List) {
            writeList(stream, (List) obj, charset);
        } else {
            System.out.println("Serializer.serialize: The DataType is invalid");
        }
    }

    private static void writeNull(ByteArrayOutputStream stream) {
        stream.write(__N);
        stream.write(__Semicolon);
    }

    private static void writeBoolean(ByteArrayOutputStream stream, byte b) {
        stream.write(__b);
        stream.write(__Colon);
        stream.write(b);
        stream.write(__Semicolon);
    }

    private static void writeInteger(ByteArrayOutputStream stream, byte[] i) {
        stream.write(__i);
        stream.write(__Colon);
        stream.write(i, 0, i.length);
        stream.write(__Semicolon);
    }

    private static void writeDouble(ByteArrayOutputStream stream, byte[] d) {
        stream.write(__d);
        stream.write(__Colon);
        stream.write(d, 0, d.length);
        stream.write(__Semicolon);
    }

    private static void writeString(ByteArrayOutputStream stream, byte[] s) {
        byte[] slen = getAsciiBytes(new Integer(s.length));
        stream.write(__s);
        stream.write(__Colon);
        stream.write(slen, 0, slen.length);
        stream.write(__Colon);
        stream.write(__Quote);
        stream.write(s, 0, s.length);
        stream.write(__Quote);
        stream.write(__Semicolon);
    }

    @SuppressWarnings("unchecked")
    private static void writeList(ByteArrayOutputStream stream, List a, String charset) {
        int len = a.size();
        byte[] alen = getAsciiBytes(new Integer(len));
        stream.write(__a);
        stream.write(__Colon);
        stream.write(alen, 0, alen.length);
        stream.write(__Colon);
        stream.write(__LeftB);
        for (int i = 0; i < len; i++) {
            writeInteger(stream, getAsciiBytes(new Integer(i)));
            serialize(stream, a.get(i), charset);
        }
        stream.write(__RightB);
    }

    @SuppressWarnings("unchecked")
    private static void writeMap(ByteArrayOutputStream stream, Map h, String charset) {
        int len = h.size();
        byte[] hlen = getAsciiBytes(new Integer(len));
        stream.write(__a);
        stream.write(__Colon);
        stream.write(hlen, 0, hlen.length);
        stream.write(__Colon);
        stream.write(__LeftB);
        for (Iterator keys = h.keySet().iterator(); keys.hasNext(); ) {
            Object key = keys.next();
            if ((key instanceof Byte) || (key instanceof Short) || (key instanceof Integer)) {
                writeInteger(stream, getAsciiBytes(key));
            } else if (key instanceof Boolean) {
                writeInteger(stream, new byte[] { ((Boolean) key).booleanValue() ? __1 : __0 });
            } else {
                writeString(stream, getBytes(key, charset));
            }
            serialize(stream, h.get(key), charset);
        }
        stream.write(__RightB);
    }

    private static byte[] getAsciiBytes(Object obj) {
        try {
            return obj.toString().getBytes("US-ASCII");
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] getBytes(Object obj, String charset) {
        try {
            return obj.toString().getBytes(charset);
        } catch (Exception e) {
            return obj.toString().getBytes();
        }
    }

    @SuppressWarnings("unchecked")
    public static Map unserialize(String targetString, boolean isTreeMap) {
        if (targetString == null || targetString.equals("")) {
            return new HashMap();
        }
        Map resultMap = null;
        try {
            resultMap = (Map) Serializer.unserialize(targetString, isTreeMap ? 1 : 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @SuppressWarnings("unchecked")
    public static List unserialize(String targetString) {
        if (targetString == null || targetString.equals("")) {
            return new ArrayList();
        }
        List result = null;
        try {
            result = (List) Serializer.unserialize(targetString, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object unserialize(String ss, int state) {
        return unserialize(ss, JavaCenterHome.JCH_CHARSET, state);
    }

    @SuppressWarnings("unchecked")
    public static Object unserialize(String ss, String charset, int state) {
        ByteArrayInputStream stream = null;
        try {
            stream = new ByteArrayInputStream(ss.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Object result = unserialize(stream, charset, state);
        return result;
    }

    @SuppressWarnings("unchecked")
    private static Object unserialize(ByteArrayInputStream stream, String charset, int state) {
        switch(stream.read()) {
            case __N:
                return readNull(stream);
            case __b:
                return readBoolean(stream);
            case __i:
                return readInteger(stream);
            case __d:
                return readDouble(stream);
            case __s:
                return readString(stream, charset);
            case __U:
                return readUnicodeString(stream);
            case __a:
                return readArray(stream, charset, state);
            default:
                System.out.println("Serializer.unserialize: The DataType Is invalid,state=" + state);
                return null;
        }
    }

    private static String readNumber(ByteArrayInputStream stream) {
        StringBuffer sb = new StringBuffer();
        int i = stream.read();
        while ((i != __Semicolon) && (i != __Colon)) {
            sb.append((char) i);
            i = stream.read();
        }
        return sb.toString();
    }

    private static Object readNull(ByteArrayInputStream stream) {
        stream.skip(1);
        return null;
    }

    private static Boolean readBoolean(ByteArrayInputStream stream) {
        stream.skip(1);
        boolean b = stream.read() == __1;
        stream.skip(1);
        return b;
    }

    private static Number readInteger(ByteArrayInputStream stream) {
        stream.skip(1);
        String i = readNumber(stream);
        return Integer.parseInt(i);
    }

    private static Number readDouble(ByteArrayInputStream stream) {
        stream.skip(1);
        String d = readNumber(stream);
        if (d.equals(__NAN)) {
            return new Double(Double.NaN);
        }
        if (d.equals(__INF)) {
            return new Double(Double.POSITIVE_INFINITY);
        }
        if (d.equals(__NINF)) {
            return new Double(Double.NEGATIVE_INFINITY);
        }
        if ((d.indexOf('.') > 0) || (d.indexOf('e') > 0) || (d.indexOf('E') > 0)) {
            return new Double(d);
        }
        int len = d.length();
        char c = d.charAt(0);
        if ((len < 19) || ((c == '-') && (len < 20))) {
            return new Long(d);
        }
        if ((len > 20) || ((c != '-') && (len > 19))) {
            return new Double(d);
        }
        try {
            return new Long(d);
        } catch (Exception e) {
            return new Double(d);
        }
    }

    private static String readString(ByteArrayInputStream stream, String charset) {
        stream.skip(1);
        int len = Integer.parseInt(readNumber(stream));
        stream.skip(1);
        byte[] buf = new byte[len];
        stream.read(buf, 0, len);
        stream.skip(2);
        try {
            return new String(buf, charset);
        } catch (Exception e) {
            return new String(buf);
        }
    }

    private static String readUnicodeString(ByteArrayInputStream stream) {
        stream.skip(1);
        int len = Integer.parseInt(readNumber(stream));
        stream.skip(1);
        StringBuffer sb = new StringBuffer(len);
        int c;
        for (int i = 0; i < len; i++) {
            if ((c = stream.read()) == __Slash) {
                char[] chs = new char[4];
                chs[0] = (char) stream.read();
                chs[1] = (char) stream.read();
                chs[2] = (char) stream.read();
                chs[3] = (char) stream.read();
                sb.append((char) (Integer.parseInt(new String(chs), 16)));
            } else {
                sb.append((char) c);
            }
        }
        stream.skip(2);
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static Object readArray(ByteArrayInputStream stream, String charset, int state) {
        stream.skip(1);
        int n = Integer.parseInt(readNumber(stream));
        stream.skip(1);
        Map map = null;
        List list = null;
        if (state == 0) {
            list = new ArrayList(n);
            state = -1;
        } else if (state == 1) {
            map = new TreeMap();
        } else {
            map = new HashMap(n);
        }
        for (int i = 0; i < n; i++) {
            Object key;
            switch(stream.read()) {
                case __i:
                    key = readInteger(stream);
                    break;
                case __s:
                    key = readString(stream, charset);
                    break;
                case __U:
                    key = readUnicodeString(stream);
                    break;
                default:
                    return null;
            }
            Object result = unserialize(stream, charset, state);
            if (list != null) {
                if ((key instanceof Integer) && (((Integer) key).intValue() == i)) {
                    list.add(result);
                } else {
                    list = null;
                }
            } else {
                map.put(key, result);
            }
        }
        stream.skip(1);
        if (list != null) {
            return list;
        } else {
            return map;
        }
    }
}
