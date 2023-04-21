package de.grogra.pf.registry;

import de.grogra.util.Described;
import de.grogra.reflect.*;
import de.grogra.graph.impl.GraphManager;
import de.grogra.graph.impl.SharedObjectNode;
import de.grogra.persistence.*;

public abstract class ObjectItem extends Item implements SharedObjectProvider {

    private static final int OBJ_DESCRIBES_MASK = 1 << Item.USED_BITS;

    public static final int USED_BITS = Item.USED_BITS + 1;

    String type;

    private Type objectType;

    public static ObjectItem createReference(RegistryContext c, Object o, String name) {
        if (o instanceof Shareable) {
            SharedObjectNode n = new SharedObjectNode((Shareable) o);
            n.setExtentIndex(LAST_EXTENT_INDEX);
            GraphManager g = c.getRegistry().getProjectGraph();
            g.addMetaNode(n, c.getRegistry().isActive() ? g.getActiveTransaction() : null);
            return new SONodeReference(name, n);
        }
        return new Value(name, o);
    }

    public void addToRegistry(Registry r, String directory) {
        setObjDescribes(true);
        r.getItem(directory).addUserItemWithUniqueName(this, "obj");
    }

    public ObjectItem(String key) {
        super(key);
    }

    protected void setType(String type) {
        this.type = type;
    }

    public abstract Object getObject();

    public abstract boolean isObjectFetched();

    protected boolean getTypeFromObject() {
        return type == null;
    }

    public Type getObjectType() {
        if (getTypeFromObject() && ((objectType == null) || !Reflection.isPrimitive(objectType))) {
            Object o = getObject();
            return (o == null) ? Type.NULL : (o instanceof Manageable) ? (Type) ((Manageable) o).getManageableType() : ClassAdapter.wrap(o.getClass());
        } else {
            return getObjectTypeImpl();
        }
    }

    Type getObjectTypeImpl() {
        if (objectType == null) {
            if (type.indexOf('.') < 0) {
                if ("null".equals(type)) {
                    objectType = Type.NULL;
                } else if ("boolean".equals(type)) {
                    objectType = ClassAdapter.wrap(Boolean.class);
                } else if ("byte".equals(type)) {
                    objectType = ClassAdapter.wrap(Byte.class);
                } else if ("short".equals(type)) {
                    objectType = ClassAdapter.wrap(Short.class);
                } else if ("char".equals(type)) {
                    objectType = ClassAdapter.wrap(Character.class);
                } else if ("int".equals(type)) {
                    objectType = ClassAdapter.wrap(Integer.class);
                } else if ("long".equals(type)) {
                    objectType = ClassAdapter.wrap(Long.class);
                } else if ("float".equals(type)) {
                    objectType = ClassAdapter.wrap(Float.class);
                } else if ("double".equals(type)) {
                    objectType = ClassAdapter.wrap(Double.class);
                }
            }
            if (objectType == null) {
                objectType = Reflection.getType(type, getClassLoader());
            }
        }
        return objectType;
    }

    public boolean isInstance(String type) {
        return Reflection.isSupertypeOrSame(type, getObjectType());
    }

    public boolean isInstance(Type type) {
        return Reflection.isAssignableFrom(type, getObjectType());
    }

    public boolean isInstance(Class cls) {
        return cls.isAssignableFrom(getObjectType().getImplementationClass());
    }

    @Override
    protected Object getDerivedDescription(String type) {
        if ((bits & OBJ_DESCRIBES_MASK) == 0) {
            return null;
        }
        Object o;
        return ((o = getObject()) instanceof Described) ? ((Described) o).getDescription(type) : (ICON.equals(type) && (o instanceof de.grogra.icon.IconSource)) ? o : null;
    }

    public String getProviderName() {
        return getRegistry().getProviderName();
    }

    public ResolvableReference readReference(PersistenceInput in) throws java.io.IOException {
        return getRegistry().readReference(in);
    }

    public void writeObject(Shareable object, PersistenceOutput out) throws java.io.IOException {
        if (object == getObject()) {
            out.writeString(getAbsoluteName());
        } else {
            getRegistry().writeObject(object, out);
        }
    }

    public static final NType $TYPE;

    public static final NType.Field objDescribes$FIELD;

    public static final NType.Field type$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(ObjectItem.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        protected void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 0:
                    ((ObjectItem) o).type = (String) value;
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object o) {
            switch(id) {
                case 0:
                    return ((ObjectItem) o).type;
            }
            return super.getObject(o);
        }
    }

    static {
        $TYPE = new NType(ObjectItem.class);
        $TYPE.addManagedField(objDescribes$FIELD = new NType.BitField($TYPE, "objDescribes", 0 | NType.BitField.SCO, de.grogra.reflect.Type.BOOLEAN, OBJ_DESCRIBES_MASK));
        $TYPE.addManagedField(type$FIELD = new _Field("type", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(String.class), null, 0));
        $TYPE.validate();
    }

    public void setObjDescribes(boolean v) {
        if (v) bits |= OBJ_DESCRIBES_MASK; else bits &= ~OBJ_DESCRIBES_MASK;
    }
}
