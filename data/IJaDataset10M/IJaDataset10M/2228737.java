package com.volantis.shared.metadata.type.mutable;

import com.volantis.shared.metadata.type.UnitType;

/**
 * A mutable {@link UnitType}.
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
public interface MutableUnitType extends UnitType, MutableSimpleType {
}
