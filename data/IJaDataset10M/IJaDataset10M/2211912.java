package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.metadata.type.constraint.MaximumValueConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumValueConstraint;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

/**
 * Implementation of {@link com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumValueConstraint}.
 */
final class MutableMaximumValueConstraintImpl extends MaximumValueConstraintImpl implements MutableMaximumValueConstraint {

    /**
     * Copy constructor.
     *
     * @param contraint The object to copy.
     */
    public MutableMaximumValueConstraintImpl(MaximumValueConstraint contraint) {
        super(contraint);
    }

    /**
     * Public constructor for use by factory.
     */
    public MutableMaximumValueConstraintImpl() {
    }

    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.MUTABLE_MAXIMUM_VALUE_CONSTRAINT;
    }
}
