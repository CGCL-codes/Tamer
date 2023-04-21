package org.databene.commons.accessor;

/**
 * Typed Accessor that returns a default value if invoked on argument null.<br/>
 * <br/>
 * Created: 22.02.2006 20:08:36
 * @author Volker Bergmann
 */
public class NullSafeTypedAccessor<C, V> extends NullSafeAccessor<C, V> implements TypedAccessor<C, V> {

    public NullSafeTypedAccessor(TypedAccessor<C, V> realAccessor, V nullValue) {
        super(realAccessor, nullValue);
    }

    public Class<? extends V> getValueType() {
        return ((TypedAccessor<C, V>) realAccessor).getValueType();
    }
}
