package org.osid.registry;

/**
 * ProviderIterator implements a "straw-man" registry interface.
 * </p>
 * 
 * @author Massachusetts Institute of Technology
 */
public interface ProviderIterator extends java.io.Serializable {

    /**
	 */
    public boolean hasNextProvider() throws org.osid.registry.RegistryException;

    /**
	 */
    public org.osid.registry.Provider nextProvider() throws org.osid.registry.RegistryException;
}
