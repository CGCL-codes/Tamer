package org.freedom.ooze.common.exception;

/**
 * @author liangs
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ExceptionInfo {

    private static String getExcDes(StackTraceElement[] ste) {
        String result = "\n";
        for (int i = 0; i < ste.length; i++) {
            result = result + ste[i] + "\n";
        }
        return result;
    }

    public static String getStackTraces(Exception e) {
        return "\n" + e + "\n" + getExcDes(e.getStackTrace());
    }
}
