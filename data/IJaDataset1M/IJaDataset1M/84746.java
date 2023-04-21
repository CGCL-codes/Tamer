package com.volantis.xml.expression.atomic;

/**
 * Represents an XPath 2.0 string value.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation in user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate 
 */
public interface StringValue extends AtomicValue {

    /**
     * Get the value of this object as a Java String.
     *
     * @return The value of this object as a Java String.
     */
    public String asJavaString();
}
