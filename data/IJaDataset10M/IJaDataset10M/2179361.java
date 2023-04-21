package org.tm4j.jndi;

/**
 * Thrown if the class of an object (e.g. provider) exported through jndi was
 * loaded with a class loader different from the one used for casting the object
 * locally (this is also called the "dreaded" ClassCastException)
 *
 * This exception normally indicates classpath problems
 *
 * @author Harald Kuhn, harald-kuhn@web.de
 */
public class DifferentClassLoaderException extends ClassCastException {

    public DifferentClassLoaderException() {
        super();
    }

    public DifferentClassLoaderException(String message) {
        super(message);
    }
}
