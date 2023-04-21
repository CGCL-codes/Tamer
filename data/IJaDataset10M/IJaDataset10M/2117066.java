package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.value.UnitValue;
import com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitee;
import com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitor;
import com.volantis.shared.metadata.value.immutable.ImmutableUnitValue;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import java.io.Serializable;

/**
 * Implementation of {@link ImmutableUnitValue}.
 */
public final class ImmutableUnitValueImpl extends UnitValueImpl implements ImmutableUnitValue, Serializable, ImmutableMetaDataValueVisitee {

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = 4942622368795437090L;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public ImmutableUnitValueImpl(UnitValue value) {
        super(value);
    }

    /**
     * Constructor which takes the name of this unit.
     * @param name The name of this unit.
     */
    public ImmutableUnitValueImpl(String name) {
        super(name);
    }

    /**
     * Protected method for future use by JDO.
     */
    protected ImmutableUnitValueImpl() {
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

    public void accept(ImmutableMetaDataValueVisitor visitor) {
        visitor.visit(this);
    }

    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.IMMUTABLE_UNIT_VALUE;
    }
}
