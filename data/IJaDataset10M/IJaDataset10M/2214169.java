package com.lubq.lm.util;

import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

public class CollectionUtil {

    /**
     * Null-safe check if the specified collection is empty.
     * <p>
     * Null returns true.
     * 
     * @param coll  the collection to check, may be null
     * @return true if empty or null
     * @since Commons Collections 3.2
     */
    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * Null-safe check if the specified collection is not empty.
     * <p>
     * Null returns false.
     * 
     * @param coll  the collection to check, may be null
     * @return true if non-null and non-empty
     * @since Commons Collections 3.2
     */
    public static boolean isNotEmpty(Collection coll) {
        return !CollectionUtils.isEmpty(coll);
    }
}
