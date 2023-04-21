package org.shalma.bean;

import java.util.List;

/**
 * Instances of the class {@code Bean} represent classes (and interfaces) or
 * objects in a running Java application.
 */
public interface Bean {

    Class getType();

    String getName(Object object);

    /**
	 * Returns a {@code Attribute} object that reflects the specified attribute
	 * of the class represented by this {@code Class} object, or null if a
	 * attribute with the specified name is not found.
	 * <p>
	 * The {@code name} parameter is a {@code String} specifying the simple name
	 * of the desired attribute.
	 */
    Attribute getAttribute(String name);

    List<Attribute> getAttributes();

    /**
	 * Returns a list of {@code Operation} objects reflecting all the operations
	 * declared by the class represented by this {@code Bean}. This includes
	 * public constructors and public methods that are not attribute accessors;
	 * inherited constructors or methods are excluded.
	 * 
	 * <p>
	 * The elements in the list returned are ordered following method class
	 * declaration. This method returns an empty list if the class declares no
	 * methods and no constructors.
	 */
    List<Operation> getRoutines();

    /**
	 * Returns a {@code Operation} object that reflects the specified routine of
	 * the class or interface represented by this {@code Class} object. The
	 * {@code name} parameter is a {@code String} specifying the simple name of
	 * the desired routine. The {@code parameterTypes} parameter is an array of
	 * {@code Class} objects that identify the method's formal parameter types,
	 * in declared order. If {@code parameterTypes} is {@code null}, it is
	 * treated as if it were an empty array.
	 * 
	 * @return the {@code Operation} object that matches the specified
	 *         {@code name} and {@code parameterTypes}, or {@code null} if a
	 *         matching routine is not found.
	 */
    Operation getRoutine(String name, Class... parameterTypes);
}
