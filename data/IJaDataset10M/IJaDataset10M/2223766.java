package com.phloc.commons.collections.multimap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Multi map based on {@link TreeMap} and {@link HashSet} values.<br>
 * 
 * @author philip
 * @param <KEYTYPE>
 *        key type
 * @param <VALUETYPE>
 *        value type
 */
@NotThreadSafe
public class MultiTreeMapHashSetBased<KEYTYPE, VALUETYPE> extends AbstractMultiTreeMapSetBased<KEYTYPE, VALUETYPE> {

    public MultiTreeMapHashSetBased() {
    }

    public MultiTreeMapHashSetBased(@Nullable final KEYTYPE aKey, @Nullable final VALUETYPE aValue) {
        super(aKey, aValue);
    }

    public MultiTreeMapHashSetBased(@Nullable final KEYTYPE aKey, @Nullable final Set<VALUETYPE> aCollection) {
        super(aKey, aCollection);
    }

    public MultiTreeMapHashSetBased(@Nullable final Map<? extends KEYTYPE, ? extends Set<VALUETYPE>> aCont) {
        super(aCont);
    }

    @Override
    @Nonnull
    protected final Set<VALUETYPE> createNewCollection() {
        return new HashSet<VALUETYPE>();
    }
}
