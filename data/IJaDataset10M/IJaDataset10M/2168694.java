package com.atmosphere.util;

import com.atmosphere.interfaces.IComponentKey;

/**
 * <code>GenericComponentKey</code> defines the implementation
 * of a data structure that can be used as a generic component
 * key with <code>String</code> value.
 *
 * @author Yi Wang (Neakor)
 * @version Creation date: 05-19-2009 17:11 EST
 * @version Modified date: 05-19-2009 17:12 EST
 */
public class GenericComponentKey implements IComponentKey {

    /**
	 * The <code>String</code> key.
	 */
    private final String key;

    /**
	 * Constructor of <code>GenericComponentKey</code>.
	 * @param key The <code>String</code> key.
	 */
    public GenericComponentKey(String key) {
        this.key = key;
    }

    @Override
    public String getValue() {
        return this.key;
    }
}
