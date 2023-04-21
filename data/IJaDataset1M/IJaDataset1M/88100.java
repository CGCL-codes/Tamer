package net.pepperbytes.plaf.database.base;

import org.hibernate.type.Type;

public class ParamValueTypeMap {

    Object value = null;

    Type type = null;

    public ParamValueTypeMap(Object value, Type t) {
        this.value = value;
        this.type = t;
    }
}
