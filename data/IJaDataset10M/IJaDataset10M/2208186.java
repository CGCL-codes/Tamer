package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.metadata.value.StructureValue;
import com.volantis.shared.metadata.value.mutable.MutableStructureValue;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

/**
 * Implementation of {@link MutableStructureValue}.
 */
final class MutableStructureValueImpl extends StructureValueImpl implements MutableStructureValue {

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public MutableStructureValueImpl(StructureValue value) {
        super(value);
    }

    /**
     * Public constructor for use by factory.
     */
    public MutableStructureValueImpl() {
    }

    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.MUTABLE_STRUCTURE_VALUE;
    }
}
