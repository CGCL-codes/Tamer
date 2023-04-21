package de.grogra.pf.registry.expr;

import de.grogra.reflect.*;
import de.grogra.pf.registry.*;
import de.grogra.util.*;

public class Array extends Expression {

    String type;

    private Type objectType;

    public static final NType $TYPE;

    public static final NType.Field type$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(Array.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        protected void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 0:
                    ((Array) o).type = (String) value;
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object o) {
            switch(id) {
                case 0:
                    return ((Array) o).type;
            }
            return super.getObject(o);
        }
    }

    static {
        $TYPE = new NType(new Array());
        $TYPE.addManagedField(type$FIELD = new _Field("type", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(String.class), null, 0));
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new Array();
    }

    public Array() {
        super(null);
    }

    @Override
    public Object evaluate(RegistryContext ctx, StringMap args) {
        Object[] a = getArgs((Item) getBranch(), ctx, args, this);
        Object array = getObjectType().getArrayType().createArray(a.length);
        for (int i = 0; i < a.length; i++) {
            java.lang.reflect.Array.set(array, i, a[i]);
        }
        return array;
    }

    public Type getObjectType() {
        if (objectType == null) {
            objectType = Reflection.getType(type, getClassLoader());
        }
        return objectType;
    }
}
