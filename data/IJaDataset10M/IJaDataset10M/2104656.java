package net.derquinse.common.base;

/**
 * Marker class for not instantiable objects. Subclasses are recommended to have private
 * constructors an be final.
 * @author Andres Rodriguez
 */
public abstract class NotInstantiable {

    /**
	 * Constructor.
	 * @throws AssertionError if called.
	 */
    protected NotInstantiable() {
        throw new AssertionError(String.format("Class %s is not instantiable"));
    }
}
