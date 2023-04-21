package org.makumba.commons.formatters;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.commons.lang.ArrayUtils;

/**
 * Formats a specific field. Subclasses of this class are handling the different kind of fields.
 * @author Cristian Bogdan
 * @author Stefan Baebler
 * @author Frederik Habilis
 * @author Rudolf Mayer
 */
public class FieldFormatter {

    static String[] params = { "default", "empty" };

    static String[][] paramValues = { null, null };

    public String[] getAcceptedParams() {
        return params;
    }

    public String[][] getAcceptedValue() {
        return paramValues;
    }

    private static final class SingletonHolder {

        static final FieldFormatter singleton = new FieldFormatter();
    }

    public static FieldFormatter getInstance() {
        return SingletonHolder.singleton;
    }

    /** Don't use this, use getInstance() */
    protected FieldFormatter() {
        for (int i = 0; i < getAcceptedParams().length; i++) {
            Hashtable h = new Hashtable(13);
            if (getAcceptedValue()[i] != null) {
                for (int j = 0; j < getAcceptedValue()[i].length; j++) {
                    h.put(getAcceptedValue()[i][j], dummy);
                }
            }
            validParams.put(getAcceptedParams()[i], h);
        }
    }

    static Object dummy = new Object();

    public String getExpr(RecordFormatter rf, int fieldIndex) {
        if (rf.expr[fieldIndex] != null) {
            return rf.expr[fieldIndex];
        }
        return rf.dd.getFieldDefinition(fieldIndex).getName();
    }

    public void initExpr(RecordFormatter rf, int fieldIndex, String s) {
        rf.expr[fieldIndex] = s;
    }

    Hashtable validParams = new Hashtable(13);

    public void checkParams(RecordFormatter rf, int fieldIndex, Dictionary formatParams) {
        for (Enumeration e = formatParams.keys(); e.hasMoreElements(); ) {
            String s = (String) e.nextElement();
            if (s.startsWith("org.makumba")) {
                continue;
            }
            checkParam(rf, fieldIndex, s, ((String) formatParams.get(s)).toLowerCase());
        }
    }

    public void checkParam(RecordFormatter rf, int fieldIndex, String name, String val) {
        Hashtable h = (Hashtable) validParams.get(name);
        if (h == null) {
            throw new InvalidValueException(rf.expr[fieldIndex], "invalid format parameter \'" + name + "\'. Allowed values are: " + ArrayUtils.toString(validParams.keySet()));
        }
        if (h.size() == 0) {
            return;
        }
        if (h.get(val) == null) {
            throw new InvalidValueException(rf.expr[fieldIndex], "invalid value for format parameter \'" + name + "\': <" + val + ">. Allowed values are: " + ArrayUtils.toString(h.keySet()));
        }
    }

    /**
     * Format the object to pure text. If text-format is blank, try the "empty" replacer value.
     * 
     * @param rf
     *            TODO
     * @param fieldIndex
     *            TODO
     */
    public String format(RecordFormatter rf, int fieldIndex, Object o, Dictionary formatParams) {
        String formatted;
        if (o == null || o.equals(rf.dd.getFieldDefinition(fieldIndex).getNull())) {
            formatted = formatNull(rf, fieldIndex, formatParams);
        } else {
            formatted = formatNotNull(rf, fieldIndex, o, formatParams);
        }
        if ("".equals(formatted)) {
            return getEmptyValueFormat(rf, fieldIndex, formatParams);
        }
        return formatted;
    }

    /**
     * Format the null-object to pure text. Try the "default" format parameter.
     * 
     * @param rf
     *            TODO
     * @param fieldIndex
     *            TODO
     */
    public String formatNull(RecordFormatter rf, int fieldIndex, Dictionary formatParams) {
        return getDefaultValueFormat(rf, fieldIndex, formatParams);
    }

    /**
     * Formats the not-null-object to pure text. To be over-ridden by subclasses.
     */
    public String formatNotNull(RecordFormatter rf, int fieldIndex, Object o, Dictionary formatParams) {
        return o.toString();
    }

    public int getIntParam(RecordFormatter rf, int fieldIndex, Dictionary formatParams, String name) {
        String s = (String) formatParams.get(name);
        if (s == null) {
            return -1;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new InvalidValueException(rf.expr[fieldIndex], "invalid integer for " + name + ": " + s);
        }
    }

    public String getIntParamString(RecordFormatter rf, int fieldIndex, Dictionary formatParams, String name) {
        int n = getIntParam(rf, fieldIndex, formatParams, name);
        if (n == -1) {
            return "";
        }
        return name + "=\"" + n + "\" ";
    }

    /**
     * Gets the formatted default value, used if real value is null. Returns blank if not set.
     */
    public String getDefaultValueFormat(RecordFormatter rf, int fieldIndex, Dictionary formatParams) {
        String s = (String) formatParams.get("default");
        return (s == null) ? "" : s;
    }

    /**
     * Gets the formatted empty value, used if real value is empty. Returns blank if not set.
     */
    public String getEmptyValueFormat(RecordFormatter rf, int fieldIndex, Dictionary formatParams) {
        String s = (String) formatParams.get("empty");
        return (s == null) ? "" : s;
    }

    /**
     * Chooses between the real (formatted) value and possible replacements (default, empty).
     */
    public String resetValueFormat(RecordFormatter rf, int fieldIndex, String s, Dictionary formatParams) {
        if (s == null) {
            s = getDefaultValueFormat(rf, fieldIndex, formatParams);
        }
        if ("".equals(s)) {
            return getEmptyValueFormat(rf, fieldIndex, formatParams);
        }
        return s;
    }

    public boolean equals(String s1, String s2) {
        return s1 != null && s1.equals(s2);
    }
}
