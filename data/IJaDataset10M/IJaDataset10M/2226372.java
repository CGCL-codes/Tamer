package de.schlund.pfixxml.loader;

import java.io.*;

/**
 * AppLoaderConfigException.java 
 * 
 * Created: 29.04.2003
 * 
 * @author mleidig
 */
public class StateTransferException extends Exception {

    public static final int MEMBER_TYPE_CHANGED = 0;

    public static final int MEMBER_TYPE_CONVERSION = 1;

    public static final int MEMBER_REMOVED = 2;

    public static final int MEMBER_ADDED = 3;

    public static final int MEMBER_FINAL = 4;

    public static final int CLASS_REMOVED = 5;

    public static final int UNHANDLED_EXCEPTION = 6;

    public static final int NULLHASH_EXCEPTION = 7;

    int type;

    String msg;

    String className;

    Exception x;

    public StateTransferException(int type, String className, String msg) {
        this.type = type;
        this.className = className;
        this.msg = msg;
    }

    public StateTransferException(int type, String className, Exception x) {
        this.type = type;
        this.className = className;
        this.x = x;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("StateTransferException: Class '" + className + "'");
        if (msg != null) sb.append(": " + msg);
        if (x != null) sb.append(": " + x);
        if (getType() == UNHANDLED_EXCEPTION && x != null) {
            StringWriter sw = new StringWriter();
            x.printStackTrace(new PrintWriter(sw));
            sb.append(": " + sw.getBuffer().toString());
        }
        return sb.toString();
    }

    public String getMessage() {
        return msg;
    }

    public Exception getException() {
        return x;
    }

    public int getType() {
        return type;
    }

    public String getClassName() {
        return className;
    }
}
