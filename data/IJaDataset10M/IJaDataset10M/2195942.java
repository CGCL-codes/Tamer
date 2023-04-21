package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.type.QuantityType;
import com.volantis.shared.metadata.type.ImmutableMetaDataTypeVisitor;
import com.volantis.shared.metadata.type.ImmutableMetaDataTypeVisitee;
import com.volantis.shared.metadata.type.immutable.ImmutableQuantityType;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import java.io.Serializable;

/**
 * Implementation of {@link ImmutableQuantityType}.
 */
final class ImmutableQuantityTypeImpl extends QuantityTypeImpl implements ImmutableQuantityType, Serializable, ImmutableMetaDataTypeVisitee {

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = 4652610693428777579L;

    /**
     * Copy constructor.
     *
     * @param type The object to copy.
     */
    public ImmutableQuantityTypeImpl(QuantityType type) {
        super(type);
    }

    /**
     * Protected method for future use by JDO.
     */
    protected ImmutableQuantityTypeImpl() {
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

    public void accept(ImmutableMetaDataTypeVisitor visitor) {
        visitor.visit(this);
    }

    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.IMMUTABLE_QUANTITY_TYPE;
    }
}
