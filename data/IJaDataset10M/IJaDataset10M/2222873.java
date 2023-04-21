package org.mandarax.kernel;

/**
 * Functions are typed functions operating on a domain of objects.
 * Functions have a name, a return type and an array of input types.
 * In this respect, they are very similar to instances of <code>java.lang.reflect.Method</code>.
 * @see java.lang.reflect.Method
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.0
 */
public interface Function extends Constructor {

    /**
     * Get the return type.
     * @return the return type
     */
    Class getReturnType();
}
