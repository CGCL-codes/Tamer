package com.ibm.wala.util;

import com.ibm.wala.util.collections.Filter;

/**
 * 
 * A filter that accepts everything.
 * 
 * @author sfink
 */
public class IndiscriminateFilter implements Filter {

    private static final IndiscriminateFilter INSTANCE = new IndiscriminateFilter();

    public static IndiscriminateFilter singleton() {
        return INSTANCE;
    }

    public boolean accepts(Object o) {
        return true;
    }
}
