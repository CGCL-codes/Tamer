package com.jspx.io;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2004-4-1
 * Time: 21:56:22
 * 1.0
 */
public abstract class AbstractWrite {

    protected String resource = null;

    protected String encode = System.getProperty("file.encoding");

    public String getEncode() {
        return encode;
    }

    public boolean setContent(String value, boolean append) {
        boolean result = false;
        if (open(append)) {
            result = writeContent(value);
            close();
        }
        return result;
    }

    public void setFile(String value) {
        resource = value;
    }

    public void setEncode(String value) {
        encode = value;
    }

    protected abstract boolean open(boolean append);

    protected abstract boolean writeContent(String value);

    protected abstract void close();
}
