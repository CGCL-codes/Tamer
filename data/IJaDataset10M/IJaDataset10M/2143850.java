package com.phloc.commons.collections.multimap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Multi map based on {@link TreeMap} and {@link ArrayList} values.<br>
 * 
 * @author philip
 * @param <KEYTYPE>
 *        key type
 * @param <VALUETYPE>
 *        value type
 */
@NotThreadSafe
public class MultiTreeMapArrayListBased<KEYTYPE, VALUETYPE> extends AbstractMultiTreeMapListBased<KEYTYPE, VALUETYPE> {

    public MultiTreeMapArrayListBased() {
    }

    public MultiTreeMapArrayListBased(@Nullable final KEYTYPE aKey, @Nullable final VALUETYPE aValue) {
        super(aKey, aValue);
    }

    public MultiTreeMapArrayListBased(@Nullable final KEYTYPE aKey, @Nullable final List<VALUETYPE> aCollection) {
        super(aKey, aCollection);
    }

    public MultiTreeMapArrayListBased(@Nullable final Map<? extends KEYTYPE, ? extends List<VALUETYPE>> aCont) {
        super(aCont);
    }

    @Override
    @Nonnull
    protected final List<VALUETYPE> createNewCollection() {
        return new ArrayList<VALUETYPE>();
    }
}
