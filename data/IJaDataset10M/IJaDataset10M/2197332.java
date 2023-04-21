package org.nightlabs.classloader;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Interface for a delegator that dispatches queries for classes
 * and resources to {@link ClassDataLoaderDelegate}s and {@link ClassLoaderDelegate}s.
 * 
 * @author Alexander Bieber <alex [AT] nightlabs [DOT] de>
 *
 */
public interface IClassLoadingDelegator {

    /**
	 * Add a ClassDataLoaderDelegate that will be asked for
	 * ClassData and resources.
	 * 
	 * @param delegate The new delegate to add.
	 */
    public abstract void addDelegate(ClassDataLoaderDelegate delegate);

    /**
	 * Add a ClassLoaderDelegate that will be asked for
	 * classes and resources.
	 * 
	 * @param delegate The new delegate to add.
	 */
    public abstract void addDelegate(ClassLoaderDelegate delegate);

    /**
	 * Remove the given delegate from the list.
	 * @param delegate The delegate to remove
	 */
    public abstract void removeDelegate(Object delegate);

    /**
	 * Asks the registered delegates for this class and
	 * returns the first one found.
	 * 
	 * @param name The classname
	 * @return The class found
	 * @throws ClassNotFoundException Thrown when the class was not found by any delegate.
	 */
    public abstract Class<?> findDelegateClass(String name) throws ClassNotFoundException;

    /**
	 * Asks the registered delegates for this resouces and returns the
	 * first list of resources found by a delegate.
	 * 
	 * @param name The name of the resources to find.
	 * @param returnAfterFoundFirst Whether to return only the first entry in the list of resources possibly to find.
	 * @return A list of resources found for the given name, or null if none where found.
	 * @throws IOException
	 */
    public abstract List<URL> findDelegateResources(String name, boolean returnAfterFoundFirst) throws IOException;
}
