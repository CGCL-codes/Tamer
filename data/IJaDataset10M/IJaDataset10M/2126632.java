package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.metadata.type.StringType;
import com.volantis.shared.metadata.type.mutable.MutableStringType;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

/**
 * Implementation of {@link com.volantis.shared.metadata.type.mutable.MutableStringType}.
 */
class MutableStringTypeImpl extends StringTypeImpl implements MutableStringType {

    /**
     * Copy constructor.
     *
     * @param type The object to copy.
     */
    public MutableStringTypeImpl(StringType type) {
        super(type);
    }

    /**
     * Public constructor for use by factory.
     */
    public MutableStringTypeImpl() {
    }

    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.MUTABLE_STRING_TYPE;
    }
}
