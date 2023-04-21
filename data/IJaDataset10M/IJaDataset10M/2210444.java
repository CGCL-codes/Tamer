package org.zxframework.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>
 * 	This helps in tracing allow for enterMethod() to work without having to set the method name etc.
 * </p>
 * 
 * NOTE : Thanks Apache :) <br>
 * 
 * The internal representation of caller location information.
 * 
 * @author Apache People
 * @since 0.8.3 of Apache Commons Logging api
 * @version 0.01
 */
public class LocationInfo implements java.io.Serializable {

    /**
	 * <code>LINE_SEP</code> - The line seperator
	 */
    public static final String LINE_SEP = System.getProperty("line.separator");

    /**
	 * <code>LINE_SEP_LEN</code> - The length of the seperator
	 */
    public static final int LINE_SEP_LEN = LINE_SEP.length();

    /**
	 * Caller's line number.
	 */
    transient String lineNumber;

    /**
	 * Caller's file name.
	 */
    transient String fileName;

    /**
	 * Caller's fully qualified class name.
	 */
    transient String className;

    /**
	 * Caller's method name.
	 */
    transient String methodName;

    /**
	 * All available caller information, in the format <code>fully.qualified.classname.of.caller.methodName(Filename.java:line)</code>
	 */
    public String fullInfo;

    private static StringWriter sw = new StringWriter();

    private static PrintWriter pw = new PrintWriter(sw);

    /**
	 * When location information is not available the constant <code>NA</code>
	 * is returned. Current value of this string
	 */
    public static final String NA = "?";

    static final long serialVersionUID = -1325822038990805636L;

    static boolean inVisualAge = false;

    static {
        try {
            Class dummy = Class.forName("com.ibm.uvm.tools.DebugSupport");
            if (dummy != null) inVisualAge = true;
        } catch (Throwable e) {
        }
    }

    /**
	 * Instantiate location information based on a Throwable. We expect the
	 * Throwable <code>t</code>, to be in the format
	 * 
	 * <pre>
	 *  java.lang.Throwable ... at org.apache.log4j.PatternLayout.format(PatternLayout.java:413) at org.apache.log4j.FileAppender.doAppend(FileAppender.java:183) at org.apache.log4j.Category.callAppenders(Category.java:131) at org.apache.log4j.Category.log(Category.java:512) at callers.fully.qualified.className.methodName(FileName.java:74) ...
	 *  	 </pre>
	 * 
	 * 
	 * <p>
	 * However, we can also deal with JIT compilers that "lose" the location
	 * information, especially between the parentheses.
	 * 
	 * @param t
	 * @param fqnOfCallingClass
	 *  
	 */
    public LocationInfo(Throwable t, String fqnOfCallingClass) {
        if (t == null) return;
        String s;
        synchronized (sw) {
            t.printStackTrace(pw);
            s = sw.toString();
            sw.getBuffer().setLength(0);
        }
        int ibegin, iend;
        ibegin = s.lastIndexOf(fqnOfCallingClass);
        if (ibegin == -1) return;
        ibegin = s.indexOf(LINE_SEP, ibegin);
        if (ibegin == -1) return;
        ibegin += LINE_SEP_LEN;
        iend = s.indexOf(LINE_SEP, ibegin);
        if (iend == -1) return;
        if (!inVisualAge) {
            ibegin = s.lastIndexOf("at ", iend);
            if (ibegin == -1) return;
            ibegin += 3;
        }
        this.fullInfo = s.substring(ibegin, iend);
    }

    /**
	 * Return the fully qualified class name of the caller making the logging
	 * request.
	 * 
	 * @return Returns the fully qualified class name
	 */
    public String getClassName() {
        if (fullInfo == null) return NA;
        if (className == null) {
            int iend = fullInfo.lastIndexOf('(');
            if (iend == -1) className = NA; else {
                iend = fullInfo.lastIndexOf('.', iend);
                int ibegin = 0;
                if (inVisualAge) {
                    ibegin = fullInfo.lastIndexOf(' ', iend) + 1;
                }
                if (iend == -1) className = NA; else className = this.fullInfo.substring(ibegin, iend);
            }
        }
        return className;
    }

    /**
	 * Return the file name of the caller.
	 * 
	 * <p>
	 * This information is not always available.
	 * 
	 * @return Returns the file name of the caller.
	 */
    public String getFileName() {
        if (fullInfo == null) return NA;
        if (fileName == null) {
            int iend = fullInfo.lastIndexOf(':');
            if (iend == -1) fileName = NA; else {
                int ibegin = fullInfo.lastIndexOf('(', iend - 1);
                fileName = this.fullInfo.substring(ibegin + 1, iend);
            }
        }
        return fileName;
    }

    /**
	 * Returns the line number of the caller.
	 * 
	 * <p>
	 * This information is not always available.
	 * 
	 * @return Returns the line number of the caller.
	 */
    public String getLineNumber() {
        if (fullInfo == null) return NA;
        if (lineNumber == null) {
            int iend = fullInfo.lastIndexOf(')');
            int ibegin = fullInfo.lastIndexOf(':', iend - 1);
            if (ibegin == -1) lineNumber = NA; else lineNumber = this.fullInfo.substring(ibegin + 1, iend);
        }
        return lineNumber;
    }

    /**
	 * Returns the method name of the caller.
	 * 
	 * @return Returns the method name of the caller.
	 */
    public String getMethodName() {
        if (fullInfo == null) return NA;
        if (methodName == null) {
            int iend = fullInfo.lastIndexOf('(');
            int ibegin = fullInfo.lastIndexOf('.', iend);
            if (ibegin == -1) methodName = NA; else methodName = this.fullInfo.substring(ibegin + 1, iend);
        }
        return methodName;
    }
}
