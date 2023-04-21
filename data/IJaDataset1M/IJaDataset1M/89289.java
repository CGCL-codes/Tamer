package org.colombbus.tangara.util.bundle;

import java.util.Collection;

/**
 * A library of typed resource loader. A library is a sorted collection
 * providing methods to look up the resource loader.
 * 
 * @version $Id: TypedResourceLoaderLibrary.java,v 1.1 2009/03/22 08:58:18 gwenael.le_roux Exp $
 */
public interface TypedResourceLoaderLibrary {

    /**
	 * Set the list of the loaders of the library
	 * 
	 * @param loaders
	 *            a non-<code>null</code> collection of loaders
	 */
    void setTypedResourceLoaders(Collection<TypedResourceLoader> loaders);

    /**
	 * Find the loader matching a specific resource type
	 * 
	 * @param resourceType
	 *            the type of resource to handle by the loader to found
	 * @return a loader handling the resource type, or <code>null</code> if the
	 *         loader cannot be found
	 */
    TypedResourceLoader findLoader(Class<?> resourceType);

    /**
	 * Verify if a loader of the library is handling a resource type
	 * 
	 * @param resourceType
	 *            the resource type to handle
	 * @return <code>true</code> if a loader has been found, <code>false</code>
	 *         otherwise
	 */
    boolean containsType(Class<?> resourceType);
}
