package net.sf.contrail.core;

import java.io.DataInput;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;
import net.sf.contrail.core.utils.ExternalizationManager.Serializer;

/**
 * Identifies an item in a hierarchy of items.
 * 
 * The name of an item may not contain the '/' character.
 * Names are separated by '/' characters to form hierarchies. 
 * 
 * @author Ted Stockwell
 */
public final class Identifier implements Comparable<Identifier>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Identifier[] EMPTY_ANCESTORS = new Identifier[0];

    private static final transient Map<String, Reference<Identifier>> __cache = new TreeMap<String, Reference<Identifier>>();

    private static final transient ReferenceQueue<Identifier> __referenceQueue = new ReferenceQueue<Identifier>();

    private static class IdentifierReference extends WeakReference<Identifier> {

        String _path;

        public IdentifierReference(Identifier referent) {
            super(referent, __referenceQueue);
            _path = referent._completePath;
        }
    }

    public static final Serializer<Identifier> SERIALIZER = new Serializer<Identifier>() {

        private final int typeCode = Identifier.class.getName().hashCode();

        public Identifier readExternal(java.io.DataInput in) throws IOException {
            String id = in.readUTF();
            return Identifier.create(id);
        }

        ;

        public void writeExternal(java.io.DataOutput out, Identifier object) throws IOException {
            out.writeUTF(object._completePath);
        }

        ;

        public void readExternal(DataInput in, Identifier object) throws IOException {
            throw new UnsupportedOperationException();
        }

        public int typeCode() {
            return typeCode;
        }
    };

    private transient Identifier[] _ancestors;

    private transient String _name;

    private String _completePath;

    private transient Properties _properties;

    public static Identifier create(String path) {
        Identifier identifier = null;
        synchronized (__cache) {
            Reference<Identifier> ref = __cache.get(path);
            if (ref != null && (identifier = ref.get()) != null) return identifier;
            identifier = new Identifier(path);
            __cache.put(path, new IdentifierReference(identifier));
        }
        return identifier;
    }

    public static Identifier create(Identifier parent, String name) {
        if (parent == null) return create(name);
        return create(parent._completePath + "/" + name);
    }

    private Identifier(String path) {
        while (path.endsWith("/")) path = path.substring(0, path.length() - 1);
        while (path.startsWith("/")) path = path.substring(1);
        int i = path.lastIndexOf('/');
        if (0 <= i) {
            _completePath = path;
            _name = path.substring(i + 1);
            Identifier parent = create(path.substring(0, i));
            Identifier[] ancestors = parent._ancestors;
            _ancestors = new Identifier[ancestors.length + 1];
            System.arraycopy(ancestors, 0, _ancestors, 0, ancestors.length);
            _ancestors[ancestors.length] = parent;
        } else {
            _completePath = _name = path;
            _ancestors = EMPTY_ANCESTORS;
        }
        IdentifierReference ref;
        while ((ref = (IdentifierReference) __referenceQueue.poll()) != null) {
            synchronized (__cache) {
                __cache.remove(ref._path);
            }
        }
    }

    public static Identifier create() {
        return create(UUID.randomUUID().toString());
    }

    public static Identifier create(Identifier parent) {
        return create(parent, UUID.randomUUID().toString());
    }

    public static Identifier create(Item parent, String name) {
        return create(parent.getId(), name);
    }

    private Object readResolve() {
        return create(_completePath);
    }

    public String getName() {
        return _name;
    }

    public Identifier getParent() {
        if (_ancestors.length <= 0) return null;
        return _ancestors[_ancestors.length - 1];
    }

    public boolean isAncestorOf(Identifier identifier) {
        Identifier[] a = identifier._ancestors;
        for (int i = a.length; 0 < i--; ) if (a[i] == this) return true;
        return false;
    }

    @Override
    public String toString() {
        return _completePath;
    }

    public Object getProperty(String propertyName) {
        if (_properties == null) return null;
        return _properties.get(propertyName);
    }

    public synchronized void setProperty(String propertyName, Object value) {
        if (_properties == null) _properties = new Properties();
        _properties.put(propertyName, value);
    }

    @Override
    public int compareTo(Identifier o) {
        if (o == this) return 0;
        Identifier[] a1 = _ancestors;
        Identifier[] a2 = o._ancestors;
        for (int i = 0; i < a1.length && i < a2.length; i++) {
            Identifier i1 = a1[i];
            Identifier i2 = a2[i];
            if (i1 != i2) return i1._name.compareTo(i2._name);
        }
        if (a1.length < a2.length) return -1;
        if (a1.length > a2.length) return 1;
        return _name.compareTo(o._name);
    }
}
