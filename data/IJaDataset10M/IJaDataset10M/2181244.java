package org.phnq.babel.types;

import java.util.*;

/**
 * <<Class summary>>
 *
 * @author Patrick Gostovic &lt;&gt;
 * @version $Rev$
 */
public class JSValue {

    protected static final short VAL_TYPE_STRING = 0;

    protected static final short VAL_TYPE_NUMBER = 1;

    protected static final short VAL_TYPE_UNDEFINED = 2;

    protected static final short VAL_TYPE_OBJECT = 3;

    protected static final short VAL_TYPE_ARRAY = 4;

    protected static final short VAL_TYPE_NULL = 5;

    private short type;

    public JSValue(short type) {
        this.type = type;
    }

    public short getType() {
        return type;
    }

    public boolean isObject() {
        return this.type == VAL_TYPE_OBJECT;
    }

    public boolean isArray() {
        return this.type == VAL_TYPE_ARRAY;
    }

    public boolean isString() {
        return this.type == VAL_TYPE_STRING;
    }

    public boolean isNumber() {
        return this.type == VAL_TYPE_NUMBER;
    }

    public boolean isUndefined() {
        return this.type == VAL_TYPE_UNDEFINED;
    }
}
