package net.sf.maple.data.file.processors;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import net.sf.maple.data.file.ItemDesc;
import net.sf.maple.data.file.TypeProcessor;
import net.sf.maple.misc.Contracts;
import net.sf.maple.misc.StrMap;

/**
 * An abstract subclass of TypeProcessor specialized for handling collections
 * of objects 
 * @author imaman
 * <br>Written at: Jul 18, 2007
 */
public abstract class CollectionProcessor implements TypeProcessor {

    protected static final String EMPTY = "empty";

    public boolean allowsEncryption() {
        return false;
    }

    /**
    * Save the elements of the given collection
    * @param f Processor factory
    * @param m Map to store elements at 
    * @param value The collection holding the elements
    * @param desc The field holding the <code>value</code> object
    * @return Number of elements saved
    */
    protected abstract int saveElementsToMap(ProcessorFactory f, StrMap m, Object value, ItemDesc desc);

    /**
    * {@inheritDoc}
    */
    public void saveToMap(ProcessorFactory f, StrMap m, Object value, ItemDesc desc) {
        saveToMap(f, m, desc.name, value, desc.type, desc);
    }

    private void saveToMap(ProcessorFactory f, StrMap m, String key, Object value, Type t, ItemDesc desc) {
        if (value == null) {
            m.set(key, null);
            return;
        }
        int n = saveElementsToMap(f, m, value, desc);
        if (n == 0) m.set(key, EMPTY);
    }

    protected static Type[] getTypeParams(ProcessorFactory f, Type t, int typeParamCount) {
        Contracts.x_assert(t instanceof ParameterizedType);
        ParameterizedType pt = (ParameterizedType) t;
        Type[] es = pt.getActualTypeArguments();
        Contracts.x_assert(es.length == typeParamCount);
        for (Type curr : es) {
            TypeProcessor c = f.lookup(curr);
            Contracts.x_assert(c != null);
        }
        return es;
    }

    protected static Object resultOrNull(StrMap m, String key, int size, Object t) {
        return resultOrNull(m, key, size, t, t);
    }

    protected static Object resultOrNull(StrMap m, String key, int size, Object t, Object r) {
        if (size != 0) return r;
        boolean b = m.containsKey(key);
        if (!b) return TypeProcessor.NONE;
        String s = m.get(key);
        if (EMPTY.equals(s)) return r;
        return null;
    }
}
