package com.runninglog.utils;

import javax.servlet.http.*;

/**
 *
 * @author mike
 */
public class StringUtils {

    /** Returns the boolean value of a paramter or the default value if it can't be parsed.
     * 
     * @param request - HttpServletRequest
     * @param parmName - Parameter Name
     * @param dValue - The default value
     * @return
     */
    public static boolean booleanParm(HttpServletRequest request, String parmName, boolean dValue) {
        String s = request.getParameter(parmName);
        if (s == null) {
            return dValue;
        } else {
            return (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("t") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("y") || s.equalsIgnoreCase("on") || s.equals("1"));
        }
    }

    /** Returns the long value of a parameter or the default value if it can't be parsed.
     * 
     * @param request
     * @param parmName
     * @param dValue
     * @return
     */
    public static long longParm(HttpServletRequest request, String parmName, long dValue) {
        String s = request.getParameter(parmName);
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException nfe) {
            return dValue;
        }
    }

    /** Returns the float value of a parameter
     * 
     * @param request
     * @param parmName
     * @param dValue
     * @return
     */
    public static float floatParm(HttpServletRequest request, String parmName, float dValue) {
        String s = request.getParameter(parmName);
        try {
            return Float.valueOf(s);
        } catch (NumberFormatException nfe) {
            return dValue;
        }
    }

    /** Returns the value of a checkbox
     * 
     * @param request
     * @param parmName
     * @return true if the checkbox was checked
     */
    public static boolean checkBox(HttpServletRequest request, String parmName) {
        return request.getParameter(parmName) != null;
    }

    public static int intParm(HttpServletRequest request, String parmName, int dValue) {
        String s = request.getParameter(parmName);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return dValue;
        }
    }

    public static int[] intParms(HttpServletRequest request, String parmName) {
        String[] parms = request.getParameterValues(parmName);
        int[] values = null;
        if (parms != null && parms.length > 0) {
            values = new int[parms.length];
            for (int i = 0; i < parms.length; i++) {
                try {
                    values[i] = Integer.parseInt(parms[i]);
                } catch (NumberFormatException nfe) {
                    values[i] = -1;
                }
            }
        }
        return values;
    }

    public static String stripQuotes(String inStr) {
        String s = inStr.replaceAll("\"", "");
        if (s != null) return s; else return inStr;
    }
}
