package org.orbeon.oxf.cache;

import org.apache.commons.lang.StringUtils;
import org.orbeon.oxf.common.OXFException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CacheUtils {

    private static final int INDENTATION = 4;

    public static String validityToString(Object validity) {
        return validityToString(validity, 0);
    }

    private static String validityToString(Object validity, int level) {
        if (validity instanceof Long) {
            return StringUtils.repeat(" ", INDENTATION * level) + new Date(((Long) validity).longValue()).toString();
        } else if (validity instanceof List) {
            StringBuffer result = new StringBuffer();
            result.append(StringUtils.repeat(" ", INDENTATION * level));
            result.append("[\n");
            for (Iterator i = ((List) validity).iterator(); i.hasNext(); ) {
                Object childValidity = i.next();
                result.append(validityToString(childValidity, level + 1));
                result.append("\n");
            }
            result.append(StringUtils.repeat(" ", INDENTATION * level));
            result.append("]");
            return result.toString();
        } else {
            throw new OXFException("Unsupported validity type: '" + validity.getClass().getName() + "'");
        }
    }

    public static String getShortClassName(Class clazz) {
        int i = clazz.getName().lastIndexOf('.');
        if (i == -1) return clazz.getName().substring(i); else return clazz.getName();
    }
}
