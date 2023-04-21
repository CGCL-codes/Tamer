package gnu.trove.impl.unmodifiable;

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

public class TUnmodifiableIntFloatMap implements TIntFloatMap, Serializable {

    private static final long serialVersionUID = -1034234728574286014L;

    private final TIntFloatMap m;

    public TUnmodifiableIntFloatMap(TIntFloatMap m) {
        if (m == null) throw new NullPointerException();
        this.m = m;
    }

    public int size() {
        return m.size();
    }

    public boolean isEmpty() {
        return m.isEmpty();
    }

    public boolean containsKey(int key) {
        return m.containsKey(key);
    }

    public boolean containsValue(float val) {
        return m.containsValue(val);
    }

    public float get(int key) {
        return m.get(key);
    }

    public float put(int key, float value) {
        throw new UnsupportedOperationException();
    }

    public float remove(int key) {
        throw new UnsupportedOperationException();
    }

    public void putAll(TIntFloatMap m) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends Integer, ? extends Float> map) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    private transient TIntSet keySet = null;

    private transient TFloatCollection values = null;

    public TIntSet keySet() {
        if (keySet == null) keySet = TCollections.unmodifiableSet(m.keySet());
        return keySet;
    }

    public int[] keys() {
        return m.keys();
    }

    public int[] keys(int[] array) {
        return m.keys(array);
    }

    public TFloatCollection valueCollection() {
        if (values == null) values = TCollections.unmodifiableCollection(m.valueCollection());
        return values;
    }

    public float[] values() {
        return m.values();
    }

    public float[] values(float[] array) {
        return m.values(array);
    }

    public boolean equals(Object o) {
        return o == this || m.equals(o);
    }

    public int hashCode() {
        return m.hashCode();
    }

    public String toString() {
        return m.toString();
    }

    public int getNoEntryKey() {
        return m.getNoEntryKey();
    }

    public float getNoEntryValue() {
        return m.getNoEntryValue();
    }

    public boolean forEachKey(TIntProcedure procedure) {
        return m.forEachKey(procedure);
    }

    public boolean forEachValue(TFloatProcedure procedure) {
        return m.forEachValue(procedure);
    }

    public boolean forEachEntry(TIntFloatProcedure procedure) {
        return m.forEachEntry(procedure);
    }

    public TIntFloatIterator iterator() {
        return new TIntFloatIterator() {

            TIntFloatIterator iter = m.iterator();

            public int key() {
                return iter.key();
            }

            public float value() {
                return iter.value();
            }

            public void advance() {
                iter.advance();
            }

            public boolean hasNext() {
                return iter.hasNext();
            }

            public float setValue(float val) {
                throw new UnsupportedOperationException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public float putIfAbsent(int key, float value) {
        throw new UnsupportedOperationException();
    }

    public void transformValues(TFloatFunction function) {
        throw new UnsupportedOperationException();
    }

    public boolean retainEntries(TIntFloatProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    public boolean increment(int key) {
        throw new UnsupportedOperationException();
    }

    public boolean adjustValue(int key, float amount) {
        throw new UnsupportedOperationException();
    }

    public float adjustOrPutValue(int key, float adjust_amount, float put_amount) {
        throw new UnsupportedOperationException();
    }
}
