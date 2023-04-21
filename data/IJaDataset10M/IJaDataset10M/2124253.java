package sisc.util;

import java.io.*;
import sisc.ser.*;
import java.util.Map;
import java.util.HashMap;
import sisc.data.Value;
import sisc.data.Symbol;
import sisc.util.Util;

/**
 * This class serves two purposes. Firstly, it contains static members
 * that maintain a bijective map between symbols and values. Secondly,
 * instances of this class are used in Java serialisation as wrappers
 * for interned values.
 *
 * Since the bijective map is static it is shared between app contexts
 * and hence interned values can leak between apps. This is an
 * unavoidable consequence of wanting to fit in with standard Java
 * serialisation in, e.g., J2EE containers, which may happen in
 * threads with no associated app context. In practice this shouldn't
 * cause much of a problem. Firstly, many J2EE containers have
 * separate class loaders for separate web apps etc. Secondly, the
 * main purpose of interned values is to store globally unique types,
 * for which there is little harm in sharing between apps.
 *
 * Access to the bijective map is thread-safe.
 *
 * The map is *not* weak - symbols and values of map entries will not
 * be garbage collected. It would be desirable for map entries to be
 * removed when they are no longer referenced. However, it is
 * impossible to do so reliably, so the current set up is the only
 * safe solution.
 */
public class InternedValue implements Externalizable {

    private static Object sync = new Object();

    private static Map byName = new HashMap(0);

    private static Map byValue = new HashMap(0);

    private Symbol name;

    private Value value;

    public InternedValue() {
    }

    private InternedValue(Symbol name, Value value) {
        this.name = name;
        this.value = value;
    }

    public Symbol getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public static InternedValue lookupByName(Symbol name) {
        synchronized (sync) {
            return (InternedValue) byName.get(name);
        }
    }

    public static InternedValue lookupByValue(Value value) {
        synchronized (sync) {
            return (InternedValue) byValue.get(value);
        }
    }

    public static InternedValue intern(Symbol name, Value value) {
        InternedValue res;
        synchronized (sync) {
            InternedValue bN = lookupByName(name);
            InternedValue bV = lookupByValue(value);
            if (bN != null && bV == null) {
                res = bN;
            } else if (bN == null && bV != null) {
                res = bV;
            } else if (bN == null && bV == null) {
                res = new InternedValue(name, value);
                byName.put(name, res);
                byValue.put(value, res);
            } else if (bN == bV) {
                res = bN;
            } else {
                res = null;
            }
        }
        return res;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        Serializer s = JavaSerializer.create(out);
        s.writeInitializedExpression(name);
        s.writeClass(value.getClass());
        value.serialize(s);
    }

    public static Value deserResolve(Symbol name, Class clazz) throws IOException {
        Value value;
        try {
            value = (Value) clazz.newInstance();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
            throw new IOException(ie.getMessage());
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            throw new IOException(iae.getMessage());
        }
        value = intern(name, value).getValue();
        if (value.getClass() != clazz) {
            throw new IOException(Util.liMessage(Util.SISCB, "interntypemismatch", new Object[] { name, value.getClass(), clazz.getClass() }));
        }
        return value;
    }

    public void readExternal(ObjectInput in) throws IOException {
        Deserializer d = JavaDeserializer.create(in);
        name = (Symbol) d.readInitializedExpression();
        Class clazz = d.readClass();
        value = deserResolve(name, clazz);
        value.deserialize(d);
    }

    public Object readResolve() throws ObjectStreamException {
        return value;
    }
}
