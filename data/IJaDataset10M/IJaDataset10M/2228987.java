package com.volantis.shared.metadata.type;

import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMemberTypeConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMinimumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMaximumLengthConstraint;

/**
 * Base interface for all collection types.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface CollectionType extends CompositeType {

    /**
     * Get the constraint on the allowable types that can be stored in
     * collections of this type.
     *
     * <p>If the constraint is null then this can accept any
     * {@link MetaDataType}.</p>
     *
     * @return The {@link ImmutableMemberTypeConstraint}, may be null.
     */
    public ImmutableMemberTypeConstraint getMemberTypeConstraint();

    /**
     * Get the constraint on the minimum length of this type.
     *
     * @return The constraint on the minimum length of this type, may be null.
     */
    public ImmutableMinimumLengthConstraint getMinimumLengthConstraint();

    /**
     * Get the constraint on the maximum length of this type.
     *
     * @return The constraint on the maximum length of this type, may be null.
     */
    public ImmutableMaximumLengthConstraint getMaximumLengthConstraint();
}
