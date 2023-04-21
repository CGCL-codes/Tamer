package com.liferay.portal.kernel.util;

/**
 * <a href="StringMaker.java.html"><b><i>View Source</i></b></a>
 *
 * @author Harry Mark
 *
 */
public class StringMaker {

    static boolean collect = false;

    static {
        String collectString = System.getProperty(MakerStats.class.getName());
        if (collectString != null) {
            if (collectString.equals("true")) {
                collect = true;
            }
        }
    }

    static MakerStats stats = null;

    static {
        if (collect) {
            stats = new MakerStats(StringMaker.class.toString());
        }
    }

    static int defaultInitSize = 128;

    static {
        String defaultInitSizeString = System.getProperty(StringMaker.class.getName() + ".initial.size");
        if (defaultInitSizeString != null) {
            try {
                defaultInitSize = Integer.parseInt(defaultInitSizeString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static MakerStats getStatistics() {
        return stats;
    }

    public StringMaker() {
        _sb = new StringBuffer(defaultInitSize);
        if (collect) {
            _getInfo(new Throwable());
        }
    }

    public StringMaker(int capacity) throws NegativeArraySizeException {
        _sb = new StringBuffer(capacity);
        if (collect) {
            _getInfo(new Throwable());
        }
    }

    public StringMaker(String s) throws NullPointerException {
        if (s == null) {
            throw new NullPointerException();
        }
        _sb = new StringBuffer(s.length() + defaultInitSize);
        if (collect) {
            _getInfo(new Throwable());
        }
        _sb.append(s);
    }

    public StringMaker(StringBuffer sb) throws NullPointerException {
        if (sb == null) {
            _sb = new StringBuffer(defaultInitSize);
        } else {
            _sb = sb;
        }
        if (collect) {
            _getInfo(new Throwable());
        }
    }

    public StringMaker append(Object obj) {
        _sb.append(obj);
        return this;
    }

    public StringMaker append(String s) {
        _sb.append(s);
        return this;
    }

    public StringMaker append(StringBuffer sb) {
        sb.append(sb);
        return this;
    }

    public StringMaker append(boolean b) {
        _sb.append(b);
        return this;
    }

    public StringMaker append(char c) {
        _sb.append(c);
        return this;
    }

    public StringMaker append(char[] array) {
        _sb.append(array);
        return this;
    }

    public StringMaker append(char[] array, int offset, int len) {
        _sb.append(array, offset, len);
        return this;
    }

    public StringMaker append(double d) {
        _sb.append(d);
        return this;
    }

    public StringMaker append(float f) {
        _sb.append(f);
        return this;
    }

    public StringMaker append(int i) {
        _sb.append(i);
        return this;
    }

    public StringMaker append(long l) {
        _sb.append(l);
        return this;
    }

    public int capacity() {
        return _sb.capacity();
    }

    public char charAt(int index) {
        return _sb.charAt(index);
    }

    public StringMaker delete(int start, int end) {
        _sb.delete(start, end);
        return this;
    }

    public StringMaker deleteCharAt(int index) {
        _sb.deleteCharAt(index);
        return this;
    }

    public void ensureCapacity(int minimumCapacity) {
        _sb.ensureCapacity(minimumCapacity);
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        _sb.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public StringBuffer getStringBuffer() {
        return _sb;
    }

    public int indexOf(String s) {
        return _sb.indexOf(s);
    }

    public int indexOf(String s, int fromIndex) {
        return _sb.indexOf(s, fromIndex);
    }

    public StringMaker insert(int offset, boolean b) {
        _sb.insert(offset, b);
        return this;
    }

    public StringMaker insert(int offset, double d) {
        _sb.insert(offset, d);
        return this;
    }

    public StringMaker insert(int offset, float f) {
        _sb.insert(offset, f);
        return this;
    }

    public StringMaker insert(int offset, int i) {
        _sb.insert(offset, i);
        return this;
    }

    public StringMaker insert(int offset, long l) {
        _sb.insert(offset, l);
        return this;
    }

    public StringMaker insert(int index, char[] array, int offset, int len) {
        _sb.insert(index, array, offset, len);
        return this;
    }

    public StringMaker insert(int offset, Object obj) {
        _sb.insert(offset, obj);
        return this;
    }

    public StringMaker insert(int offset, String s) {
        _sb.insert(offset, s);
        return this;
    }

    public StringMaker insert(int offset, char c) {
        _sb.insert(offset, c);
        return this;
    }

    public StringMaker insert(int offset, char[] array) {
        _sb.insert(offset, array);
        return this;
    }

    public int lastIndexOf(String s) {
        return _sb.lastIndexOf(s);
    }

    public int lastIndexOf(String s, int fromIndex) {
        return _sb.lastIndexOf(s, fromIndex);
    }

    public int length() {
        return _sb.length();
    }

    public StringMaker replace(int start, int end, String s) {
        _sb.replace(start, end, s);
        return this;
    }

    public StringMaker reverse() {
        _sb.reverse();
        return this;
    }

    public void setCharAt(int index, char ch) {
        _sb.setCharAt(index, ch);
    }

    public void setLength(int len) {
        _sb.setLength(len);
    }

    public String substring(int start) {
        return _sb.substring(start);
    }

    public String substring(int start, int end) {
        return _sb.substring(start, end);
    }

    public String toString() {
        if (collect) {
            stats.add(_caller, _initSize, _sb.length());
        }
        return _sb.toString();
    }

    private void _getInfo(Throwable t) {
        _initSize = _sb.capacity();
        StackTraceElement[] elements = t.getStackTrace();
        if (elements.length > 1) {
            StackTraceElement el = elements[1];
            _caller = el.getClassName() + StringPool.PERIOD + el.getMethodName() + StringPool.COLON + el.getLineNumber();
        }
    }

    private StringBuffer _sb;

    private int _initSize;

    private String _caller;
}
