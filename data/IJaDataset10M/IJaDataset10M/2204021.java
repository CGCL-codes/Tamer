package com.volantis.shared.metadata.value.immutable;

import com.volantis.shared.metadata.value.NumberValue;

/**
 * An immutable {@link NumberValue}.
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
public interface ImmutableNumberValue extends NumberValue, ImmutableSimpleValue {
}
