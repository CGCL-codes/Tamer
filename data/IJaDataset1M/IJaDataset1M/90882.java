package org.citycarshare.util;

import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class with static utility methods.
 *
 * @author Brian "Dexter" Allen, vector@acm.org
 * @version $Id: Utils.java,v 1.1 2002/06/14 18:26:10 dexter_allen Exp $
 */
public class Utils {

    public static void ensureNotNull(Object argObj, String argObjName) {
        if (argObj == null) {
            String errMsg = argObjName + " can not be null.";
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static String dumpMap(Map argMap) {
        if (argMap == null) {
            return "null\n";
        }
        String dump = "key  ->  value\n";
        Set keys = argMap.keySet();
        Iterator i = keys.iterator();
        while (i.hasNext()) {
            Object key = i.next();
            dump += key + " -> " + (argMap.get(key)).toString() + "\n";
        }
        return dump;
    }

    public static String dumpCollection(Collection argCol) {
        if (argCol == null) {
            return "null\n";
        }
        String retVal = "Collection Dump (" + argCol.size() + "):\n";
        Iterator i = argCol.iterator();
        while (i.hasNext()) {
            retVal += i.next() + "\n";
        }
        retVal += "-----------------\n";
        return retVal;
    }
}
