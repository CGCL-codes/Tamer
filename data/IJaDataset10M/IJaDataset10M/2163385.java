package org.sodeja.rel.impl;

import java.util.Arrays;
import java.util.SortedSet;
import org.sodeja.rel.Attribute;

class PrimaryKey {

    private final SortedSet<Attribute> pk;

    private final TupleImpl tuple;

    private final AttributeValue[] valuesCache;

    private final int hashCodeCache;

    public PrimaryKey(SortedSet<Attribute> pk, TupleImpl tuple) {
        this.pk = pk;
        this.tuple = tuple;
        valuesCache = new AttributeValue[pk.size()];
        int num = 0;
        for (Attribute a : pk) {
            valuesCache[num++] = tuple.getAttributeValue(a);
        }
        this.hashCodeCache = Arrays.hashCode(this.valuesCache);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrimaryKey)) {
            return false;
        }
        return Arrays.equals(valuesCache, ((PrimaryKey) obj).valuesCache);
    }

    @Override
    public int hashCode() {
        return hashCodeCache;
    }
}
