package org.elogistics.util;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Static class to find Objects in lists or sets using
 * equals.
 * 
 * @author Jurkschat, Oliver
 *
 */
public class Finder {

    public static Object find(Set source, Object equal) {
        if (source.contains(equal)) {
            Object result = null;
            Iterator<Object> i = source.iterator();
            while (i.hasNext()) {
                result = i.next();
                if (result.equals(equal)) {
                    return result;
                }
            }
        }
        return null;
    }

    public static Object find(List source, Object equal) {
        if (source.contains(equal)) {
            return source.get(source.indexOf(equal));
        }
        return null;
    }
}
