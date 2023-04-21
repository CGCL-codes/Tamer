package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.metadata.value.ListValue;
import com.volantis.shared.metadata.value.mutable.MutableListValue;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

/**
 * Implementation of {@link MutableListValue}.
 */
final class MutableListValueImpl extends ListValueImpl implements MutableListValue {

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public MutableListValueImpl(ListValue value) {
        super(value);
    }

    /**
     * Public constructor for use by factory.
     */
    public MutableListValueImpl() {
    }

    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.MUTABLE_LIST_VALUE;
    }
}
