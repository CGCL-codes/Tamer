package com.loribel.commons.util.collection;

import java.util.Map;

/**
 * Map ayant comme valeurs des Objets de type Integer.
 *
 * @author Gregory Borelli
 */
public class GB_MapCount2 extends GB_MapInt {

    public GB_MapCount2() {
        super();
    }

    public GB_MapCount2(Map a_map) {
        super(a_map);
    }

    public int addCount(Object a_key) {
        Integer l_value = (Integer) get(a_key);
        if (l_value == null) {
            l_value = new Integer(1);
        } else {
            l_value = new Integer(l_value.intValue() + 1);
        }
        put(a_key, l_value);
        return l_value.intValue();
    }
}
