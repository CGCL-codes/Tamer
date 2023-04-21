package com.volantis.xml.pipeline.sax;

/**
 * Implementation of this interface indicates that the object owns, or could
 * own resources that need cleaning up.
 * <p>Resources in this sense is a very asbstract term as it could refer to
 * external resources such as sockets, or database connections but equally it
 * could just refer to a Java object that can be reused and so needs adding
 * back to an object pool.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate 
 */
public interface ResourceOwner {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Release any resources associated with this object.
     * <p>This will be called if an error has occurred so it must be written
     * defensively to ensure that it will work no matter what state the object
     * or any dependent objects is in.</p>
     */
    public void release();
}
