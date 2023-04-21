package org.hypergraphdb.type;

import org.hypergraphdb.HGHandle;

/**
 * <p>
 * An <code>ObjectFactory</code> is capable of constructing concrete run-time
 * instances of a certain <code>Class</code>. 
 * </p>
 *  
 * @author Borislav Iordanov
 */
public interface ObjectFactory<T> {

    /**
	 * <p>
	 * Return the <code>Class</code> of the objects being constructed by this factory.
	 * </p> 
	 */
    Class<T> getType();

    /**
	 * <p>Create a new run-time instance of the type this factory 
	 * is responsible for.</p>
	 */
    T make();

    /**
	 * <p>Create a new run-time <code>HGLink</code> instance of the type this factory is
	 * responsible for.</p>
	 */
    T make(HGHandle[] targetSet);
}
