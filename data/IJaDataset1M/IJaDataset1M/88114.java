package de.grogra.graph;

import de.grogra.reflect.*;

public abstract class AccessorBase<T> implements AttributeAccessor, ObjectAttributeAccessor<T>, BooleanAttributeAccessor, ByteAttributeAccessor, ShortAttributeAccessor, CharAttributeAccessor, IntAttributeAccessor, LongAttributeAccessor, FloatAttributeAccessor, DoubleAttributeAccessor {

    protected final Attribute attribute;

    public AccessorBase(Attribute attribute) {
        this.attribute = attribute;
    }

    public Type getType() {
        return attribute.getType();
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Field getField() {
        return null;
    }

    protected T clone(T orig) {
        return orig;
    }

    public Object setSubfield(Object object, FieldChain field, int[] indices, Object value, GraphState gs) {
        T v = clone(getObject(object, gs));
        Object o = v;
        int i;
        try {
            for (i = 0; i < field.length() - 1; i++) {
                o = field.getField(i).getObject(o);
            }
            Reflection.set(o, field.getField(i), value);
        } catch (IllegalAccessException e) {
            throw new de.grogra.util.WrapException(e);
        }
        setObject(object, v, gs);
        return value;
    }
}
