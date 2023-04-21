package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.metadata.type.constraint.EnumeratedConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

/**
 * Implementation of {@link com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint}.
 */
final class MutableEnumeratedConstraintImpl extends EnumeratedConstraintImpl implements MutableEnumeratedConstraint {

    /**
     * Copy constructor.
     *
     * @param constraint The object to copy.
     */
    public MutableEnumeratedConstraintImpl(EnumeratedConstraint constraint) {
        super(constraint);
    }

    /**
     * Public constructor for use by factory.
     */
    public MutableEnumeratedConstraintImpl() {
    }

    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.MUTABLE_ENUMERATED_CONSTRAINT;
    }
}
