package com.oneandone.sushi.metadata.simpletypes;

import com.oneandone.sushi.metadata.Schema;
import com.oneandone.sushi.metadata.SimpleType;
import com.oneandone.sushi.metadata.SimpleTypeException;

public class BooleanType extends SimpleType {

    public BooleanType(Schema schema) {
        super(schema, Boolean.class, "boolean");
    }

    @Override
    public Object newInstance() {
        return Boolean.FALSE;
    }

    @Override
    public String valueToString(Object obj) {
        return obj.toString();
    }

    @Override
    public Object stringToValue(String str) throws SimpleTypeException {
        str = str.toLowerCase();
        if ("true".equals(str)) {
            return Boolean.TRUE;
        } else if ("false".equals(str)) {
            return Boolean.FALSE;
        } else {
            throw new SimpleTypeException("expected true or false, got " + str + ".");
        }
    }
}
