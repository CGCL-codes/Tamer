package com.enerjy.index.impl;

import com.enerjy.index.IMetricsElement;

/**
 * A collection of utilities for dealing with Index objects.
 */
public class IndexUtils {

    /**
     * Determine the full path (from the Index root) of an element.
     * 
     * @param element Element to query; null elements will yield an empty string.
     * @return The element's full path.
     */
    public static String getFullPath(IMetricsElement element) {
        if (null == element) {
            return "";
        }
        return getFullPath(element.getParent()) + "/" + element.getName();
    }

    private IndexUtils() {
    }
}
