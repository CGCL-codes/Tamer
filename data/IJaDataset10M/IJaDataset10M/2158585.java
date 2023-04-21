package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.metadata.type.constraint.NumberSubTypeConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableNumberSubTypeConstraint;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

/**
 * Implementation of {@link MutableNumberSubTypeConstraint}.
 */
final class MutableNumberSubTypeConstraintImpl extends NumberSubTypeConstraintImpl implements MutableNumberSubTypeConstraint {

    /**
     * Copy constructor.
     *
     * @param numberSubTypeConstraint The object to copy.
     */
    public MutableNumberSubTypeConstraintImpl(NumberSubTypeConstraint numberSubTypeConstraint) {
        super(numberSubTypeConstraint);
    }

    /**
     * Public constructor for use by factory.
     */
    public MutableNumberSubTypeConstraintImpl() {
    }

    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.MUTABLE_NUMBER_SUBTYPE;
    }
}
