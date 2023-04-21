package com.memoire.yapod;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class YapodXmlSerializer implements YapodSerializer {

    public static boolean INDENTATION = true;

    public static final String ENCODING = "UTF-8";

    protected OutputStream out;

    protected Map ref;

    protected Map obj;

    protected Object root;

    protected int indentation_;

    public YapodXmlSerializer() {
    }

    public String extension() {
        return "xml";
    }

    private static final String SPACES = "                                                            " + "                                                            " + "                                                            ";

    protected String indente() {
        return SPACES.substring(0, Math.min(indentation_, SPACES.length()));
    }

    public static void warning(String _s) {
        System.err.println("Yapod XML warning: " + _s);
    }

    protected static void output(OutputStream _w, String _o) throws IOException {
        output0(_w, _o.getBytes(ENCODING));
    }

    protected static void output0(OutputStream _w, byte[] _o) throws IOException {
        _w.write(_o);
    }

    public synchronized void open(OutputStream _out) throws IOException {
        OutputStream tmpOut = _out;
        if (!(tmpOut instanceof BufferedOutputStream)) tmpOut = new BufferedOutputStream(tmpOut, BUFFER_SIZE);
        idbase_ = 1;
        ref = new HashMap();
        obj = new HashMap();
        out = tmpOut;
        indentation_ = 0;
        writeHeader(out);
    }

    public synchronized void close() throws IOException {
        writeFooter(out);
        out.close();
    }

    public synchronized void store(Object _o, Object dbcx_, Hashtable _ref, Hashtable _obj) throws IOException {
        String _dir = (String) dbcx_;
        String k = (String) _ref.get(_o);
        if (k == null) throw new IllegalArgumentException("object not referenced: " + _o);
        Map old_ref = ref;
        Map old_obj = obj;
        ref = _ref;
        obj = _obj;
        root = _o;
        indentation_ = 0;
        out = new BufferedOutputStream(new FileOutputStream(_dir + File.separator + k + ".xml"), 8192);
        output(out, "<?xml version=\"1.0\" encoding=\"" + ENCODING + "\"?>\n");
        output(out, "<?xml-stylesheet href=\"yapod.css\" type=\"text/css\"?>\n");
        output(out, "<!DOCTYPE yapod>\n");
        output(out, "<yapod>\n");
        write(_o);
        output(out, "</yapod>\n");
        out.close();
        root = null;
        ref = old_ref;
        obj = old_obj;
    }

    protected int idbase_ = 1;

    protected String getId(Object _o) {
        if (_o == null) return "null";
        String r = (String) ref.get(_o);
        if (r == null) {
            do {
                r = "RO-" + idbase_;
                idbase_++;
            } while (obj.get(r) != null);
            ref.put(_o, r);
            obj.put(r, _o);
        }
        return r;
    }

    public final synchronized void write(Object _o) throws IOException {
        indentation_++;
        write(_o, out);
        indentation_--;
    }

    protected synchronized void writeHeader(OutputStream _w) throws IOException {
        output(_w, "<?xml version=\"1.0\" encoding=\"" + ENCODING + "\"?>\n");
        output(_w, "<?xml-stylesheet href=\"yapod.css\" type=\"text/css\"?>\n");
        output(_w, "<!DOCTYPE yapod>\n");
        output(_w, "<yapod>\n");
    }

    protected synchronized void writeFooter(OutputStream _w) throws IOException {
        output(_w, "</yapod>\n");
    }

    protected synchronized void writeNull(OutputStream _w) throws IOException {
        if (INDENTATION) output(_w, indente());
        output(_w, "<null/>\n");
    }

    protected synchronized void writeBoolean(Boolean _o, OutputStream _w) throws IOException {
        if (INDENTATION) output(_w, indente());
        output(_w, "<single");
        output(_w, getTypeAttr(_o));
        output(_w, ">");
        output(_w, _o.toString());
        output(_w, "</single>\n");
    }

    protected synchronized void writeNumber(Number _o, OutputStream _w) throws IOException {
        if (INDENTATION) output(_w, indente());
        output(_w, "<single");
        output(_w, getTypeAttr(_o));
        output(_w, ">");
        output(_w, _o.toString());
        output(_w, "</single>\n");
    }

    protected synchronized void writeCharacter(Character _o, OutputStream _w) throws IOException {
        if (INDENTATION) output(_w, indente());
        output(_w, "<single");
        output(_w, getTypeAttr(_o));
        output(_w, ">");
        output(_w, "" + (int) _o.charValue());
        output(_w, "</single>\n");
    }

    protected synchronized void writeString(String _o, OutputStream _w) throws IOException {
        if (INDENTATION) output(_w, indente());
        output(_w, "<single");
        output(_w, getTypeAttr(_o));
        output(_w, ">");
        output(_w, toXmlCharset(_o));
        output(_w, "</single>\n");
    }

    protected synchronized void writeBytes(byte[] _o, OutputStream _w) throws IOException {
        if (INDENTATION) output(_w, indente());
        output(_w, "<array-byte><![CDATA[");
        char[] s = new char[_o.length];
        for (int i = 0; i < _o.length; i++) s[i] = (char) _o[i];
        output(_w, new String(s));
        output(_w, "]]></array-byte>\n");
    }

    protected synchronized void writeArray(Object _o, OutputStream _w) throws IOException {
        int l = Array.getLength(_o);
        if (INDENTATION) output(_w, indente());
        output(_w, "<array");
        output(_w, getTypeAttr(_o));
        output(_w, getDepthAttr(_o));
        output(_w, " id=\"");
        output(_w, getId(_o));
        output(_w, "\" length=\"");
        output(_w, "" + l);
        output(_w, "\">\n");
        indentation_++;
        for (int i = 0; i < l; i++) write(Array.get(_o, i), _w);
        indentation_--;
        if (INDENTATION) output(_w, indente());
        output(_w, "</array>\n");
    }

    protected synchronized void writeObject(Object _o, OutputStream _w) throws IOException {
        Class c = _o.getClass();
        try {
            c.getMethod("writeObject", new Class[] { ObjectOutputStream.class });
            warning("Specific serialization for " + c.getName());
        } catch (NoSuchMethodException ex) {
        }
        synchronized (_o) {
            if (INDENTATION) output(_w, indente());
            output(_w, "<object");
            output(_w, getTypeAttr(_o));
            output(_w, " id=\"");
            output(_w, getId(_o));
            output(_w, "\">\n");
            indentation_++;
            Field[] fields = YapodLib.getAllFields(_o.getClass());
            for (int i = 0; i < fields.length; i++) writeField(_o, fields[i], _w);
            indentation_--;
            if (INDENTATION) output(_w, indente());
            output(_w, "</object>\n");
        }
    }

    protected synchronized void writeReference(Object _o, OutputStream _w) throws IOException {
        if (INDENTATION) output(_w, indente());
        output(_w, "<reference idref=\"");
        output(_w, getId(_o));
        output(_w, "\"/>\n");
    }

    protected void writeField(String _name, Object _value, OutputStream _w) throws IOException {
        if (INDENTATION) output(_w, indente());
        output(_w, "<field name=\"");
        output(_w, _name);
        output(_w, "\">\n");
        indentation_++;
        write(_value, _w);
        indentation_--;
        if (INDENTATION) output(_w, indente());
        output(_w, "</field>\n");
    }

    public synchronized void write(Object _o, OutputStream _w) throws IOException {
        if (_o == null) writeNull(_w); else if (_o instanceof Boolean) writeBoolean((Boolean) _o, _w); else if (_o instanceof Number) writeNumber((Number) _o, _w); else if (_o instanceof Character) writeCharacter((Character) _o, _w); else if (_o instanceof String) writeString((String) _o, _w); else if (_o instanceof byte[]) writeBytes((byte[]) _o, _w); else if (_o.getClass().isArray()) writeArray(_o, _w); else {
            if ((root == _o) || (ref.get(_o) == null)) writeObject(_o, _w);
            writeReference(_o, _w);
        }
    }

    protected void writeField(Object _o, Field _f, OutputStream _w) throws IOException {
        if (!YapodLib.isValid(_f)) return;
        YapodLib.setAccessible(_f, true);
        Object FAKE_VALUE = new Object();
        Object value = FAKE_VALUE;
        try {
            value = _f.get(_o);
        } catch (IllegalAccessException ex1) {
            String fn = _f.getName();
            Method m = null;
            try {
                m = _o.getClass().getMethod(fn, new Class[0]);
            } catch (NoSuchMethodException ex2) {
            }
            if ((m == null) && (fn.endsWith("_"))) {
                fn = fn.substring(0, fn.length() - 1);
                try {
                    m = _o.getClass().getMethod(fn, new Class[0]);
                } catch (NoSuchMethodException ex3) {
                }
            }
            if (m == null) {
                fn = "get" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
                try {
                    m = _o.getClass().getMethod(fn, new Class[0]);
                } catch (NoSuchMethodException ex4) {
                }
            }
            if (m != null) {
                try {
                    value = m.invoke(_o, new Object[0]);
                } catch (IllegalAccessException ex5) {
                } catch (InvocationTargetException ex6) {
                }
            }
        }
        if (value == FAKE_VALUE) {
            warning("No access to field " + _f.getName() + " in " + _o.getClass());
            value = null;
        }
        writeField(_f.getName(), value, _w);
    }

    Map classAttr = new HashMap();

    private String getTypeAttr(Object _value) {
        if (_value == null) return "";
        Class c = _value.getClass();
        if (classAttr.containsKey(c)) {
            return (String) classAttr.get(c);
        }
        String r = getTypeAttr(c);
        classAttr.put(c, r);
        return r;
    }

    private String getTypeAttr(Class _c) {
        Class c = _c;
        StringBuffer r = new StringBuffer(30);
        r.append(" type=\"");
        while (c.isArray()) c = c.getComponentType();
        r.append(c.getName());
        r.append("\"");
        return r.toString();
    }

    private String getDepthAttr(Object _value) {
        if (_value == null) return "";
        Class c = _value.getClass();
        while (!c.isArray()) return "";
        StringBuffer r = new StringBuffer(30);
        r.append(" depth=\"");
        int n = 0;
        while (c.isArray()) {
            c = c.getComponentType();
            n++;
        }
        r.append("" + n);
        r.append("\"");
        return r.toString();
    }

    protected static final String toXmlCharset(char _c) {
        String r;
        if (_c == '<') r = "&lt;"; else if (_c == '>') r = "&gt;"; else if (_c == ' ') r = "&#32;"; else if (_c == '&') r = "&amp;"; else if (_c == '"') r = "&quot;"; else if (Character.isISOControl(_c) || (_c == '\'') || (_c == '\"') || (_c > 254)) r = "&#" + (int) _c + ";"; else r = "" + _c;
        return r;
    }

    protected static final String toXmlCharset(Character _c) {
        return toXmlCharset(_c.charValue());
    }

    protected static final String toXmlCharset(String _s) {
        StringBuffer r = new StringBuffer(2 * _s.length());
        int l = _s.length();
        for (int i = 0; i < l; i++) {
            char c = _s.charAt(i);
            r.append(toXmlCharset(c));
        }
        return r.toString();
    }
}
