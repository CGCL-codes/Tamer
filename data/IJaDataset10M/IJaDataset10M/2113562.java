package de.grogra.xl.qnp;

import de.grogra.xl.util.*;
import de.grogra.reflect.*;

public class FieldListPredicate extends AttributeListPredicate {

    private final Field[] fields;

    public FieldListPredicate(Type cls, Type nodeType, Field[] fields) {
        super(cls, fields.length);
        if ((nodeType != null) && !Reflection.equal(nodeType, getParameterType(0))) {
            throw new IllegalArgumentException("Illegal node type");
        }
        for (int i = 0; i < fields.length; i++) {
            if ((fields[i] != null) && !Reflection.equal(getParameterType(i + 1), fields[i].getType())) {
                throw new IllegalArgumentException("Illegal field type " + fields[i].getType() + ", expected " + getParameterType(i + 1));
            }
        }
        this.fields = fields;
    }

    public FieldListPredicate(Type cls, Type nodeType, Field field) {
        this(cls, nodeType, new Field[] { field });
    }

    public FieldListPredicate(Type nodeType, Field field) {
        this(null, nodeType, new Field[] { field });
    }

    @Override
    protected boolean getBoolean(Object o, int index) {
        try {
            return fields[index].getBoolean(o);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    @Override
    protected byte getByte(Object o, int index) {
        try {
            return fields[index].getByte(o);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    @Override
    protected short getShort(Object o, int index) {
        try {
            return fields[index].getShort(o);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    @Override
    protected char getChar(Object o, int index) {
        try {
            return fields[index].getChar(o);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    @Override
    protected int getInt(Object o, int index) {
        try {
            return fields[index].getInt(o);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    @Override
    protected long getLong(Object o, int index) {
        try {
            return fields[index].getLong(o);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    @Override
    protected float getFloat(Object o, int index) {
        try {
            return fields[index].getFloat(o);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    @Override
    protected double getDouble(Object o, int index) {
        try {
            return fields[index].getDouble(o);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    @Override
    protected Object getObject(Object o, int index) {
        try {
            return fields[index].getObject(o);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        }
    }

    @Override
    protected String paramString() {
        return XLUtils.arrayToString(fields);
    }
}
