package com.hongbo.cobweb.nmr.deployer;

/**
 * This interface represents a JBI Service Unit and will be registered in
 * the OSGi registry
 */
public interface ServiceUnit {

    /**
     * Retrieves the name of this service assembly
     *
     * @return the name
     */
    String getName();

    /**
     * Retrieves the description of this service assembly
     *
     * @return the description
     */
    String getDescription();

    /**
     * Retrieve the JBI descriptor for this service assembly
     *
     * @return the JBI descriptor
     */
    String getDescriptor();

    /**
     * Get the ServiceAssembly to which this ServiceUnit belongs
     *
     * @return
     */
    ServiceAssembly getServiceAssembly();

    /**
     * Retrieve the Component onto which this ServiceUnit is deployed
     *
     * @return
     */
    Component getComponent();
}
