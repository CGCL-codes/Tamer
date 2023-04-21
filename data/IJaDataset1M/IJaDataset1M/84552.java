package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import com.volantis.shared.metadata.value.SetValue;
import com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitee;
import com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitor;
import com.volantis.shared.metadata.value.immutable.ImmutableSetValue;
import java.io.Serializable;

/**
 * Implementation of {@link ImmutableSetValue}.
 */
final class ImmutableSetValueImpl extends SetValueImpl implements ImmutableSetValue, Serializable, ImmutableMetaDataValueVisitee {

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = 990036004537854968L;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public ImmutableSetValueImpl(SetValue value) {
        super(value);
    }

    /**
     * Protected method for future use by JDO.
     */
    protected ImmutableSetValueImpl() {
    }

    /**
     * Override to return this object rather than create a new one.
     *
     * <p>This is simply a performance optimisation and has no impact on the
     * behaviour.</p>
     */
    public ImmutableInhibitor createImmutable() {
        return this;
    }

    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.IMMUTABLE_SET_VALUE;
    }

    public void accept(ImmutableMetaDataValueVisitor visitor) {
        visitor.visit(this);
    }
}
