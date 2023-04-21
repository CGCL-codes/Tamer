package gnu.trove.impl.sync;

import gnu.trove.iterator.*;
import gnu.trove.procedure.*;
import gnu.trove.set.*;
import gnu.trove.list.*;
import gnu.trove.function.*;
import gnu.trove.map.*;
import gnu.trove.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Random;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class TSynchronizedFloatCharMap implements TFloatCharMap, Serializable {

    private static final long serialVersionUID = 1978198479659022715L;

    private final TFloatCharMap m;

    final Object mutex;

    public TSynchronizedFloatCharMap(TFloatCharMap m) {
        if (m == null) throw new NullPointerException();
        this.m = m;
        mutex = this;
    }

    public TSynchronizedFloatCharMap(TFloatCharMap m, Object mutex) {
        this.m = m;
        this.mutex = mutex;
    }

    public int size() {
        synchronized (mutex) {
            return m.size();
        }
    }

    public boolean isEmpty() {
        synchronized (mutex) {
            return m.isEmpty();
        }
    }

    public boolean containsKey(float key) {
        synchronized (mutex) {
            return m.containsKey(key);
        }
    }

    public boolean containsValue(char value) {
        synchronized (mutex) {
            return m.containsValue(value);
        }
    }

    public char get(float key) {
        synchronized (mutex) {
            return m.get(key);
        }
    }

    public char put(float key, char value) {
        synchronized (mutex) {
            return m.put(key, value);
        }
    }

    public char remove(float key) {
        synchronized (mutex) {
            return m.remove(key);
        }
    }

    public void putAll(Map<? extends Float, ? extends Character> map) {
        synchronized (mutex) {
            m.putAll(map);
        }
    }

    public void putAll(TFloatCharMap map) {
        synchronized (mutex) {
            m.putAll(map);
        }
    }

    public void clear() {
        synchronized (mutex) {
            m.clear();
        }
    }

    private transient TFloatSet keySet = null;

    private transient TCharCollection values = null;

    public TFloatSet keySet() {
        synchronized (mutex) {
            if (keySet == null) keySet = new TSynchronizedFloatSet(m.keySet(), mutex);
            return keySet;
        }
    }

    public float[] keys() {
        synchronized (mutex) {
            return m.keys();
        }
    }

    public float[] keys(float[] array) {
        synchronized (mutex) {
            return m.keys(array);
        }
    }

    public TCharCollection valueCollection() {
        synchronized (mutex) {
            if (values == null) values = new TSynchronizedCharCollection(m.valueCollection(), mutex);
            return values;
        }
    }

    public char[] values() {
        synchronized (mutex) {
            return m.values();
        }
    }

    public char[] values(char[] array) {
        synchronized (mutex) {
            return m.values(array);
        }
    }

    public TFloatCharIterator iterator() {
        return m.iterator();
    }

    public float getNoEntryKey() {
        return m.getNoEntryKey();
    }

    public char getNoEntryValue() {
        return m.getNoEntryValue();
    }

    public char putIfAbsent(float key, char value) {
        synchronized (mutex) {
            return m.putIfAbsent(key, value);
        }
    }

    public boolean forEachKey(TFloatProcedure procedure) {
        synchronized (mutex) {
            return m.forEachKey(procedure);
        }
    }

    public boolean forEachValue(TCharProcedure procedure) {
        synchronized (mutex) {
            return m.forEachValue(procedure);
        }
    }

    public boolean forEachEntry(TFloatCharProcedure procedure) {
        synchronized (mutex) {
            return m.forEachEntry(procedure);
        }
    }

    public void transformValues(TCharFunction function) {
        synchronized (mutex) {
            m.transformValues(function);
        }
    }

    public boolean retainEntries(TFloatCharProcedure procedure) {
        synchronized (mutex) {
            return m.retainEntries(procedure);
        }
    }

    public boolean increment(float key) {
        synchronized (mutex) {
            return m.increment(key);
        }
    }

    public boolean adjustValue(float key, char amount) {
        synchronized (mutex) {
            return m.adjustValue(key, amount);
        }
    }

    public char adjustOrPutValue(float key, char adjust_amount, char put_amount) {
        synchronized (mutex) {
            return m.adjustOrPutValue(key, adjust_amount, put_amount);
        }
    }

    public boolean equals(Object o) {
        synchronized (mutex) {
            return m.equals(o);
        }
    }

    public int hashCode() {
        synchronized (mutex) {
            return m.hashCode();
        }
    }

    public String toString() {
        synchronized (mutex) {
            return m.toString();
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        synchronized (mutex) {
            s.defaultWriteObject();
        }
    }
}
