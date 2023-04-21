package org.jargo;

/**
 * 
 * @see ConstructorInjection
 * @author Leon van Zantvoort
 */
public interface ObjectFactory<T> {

    /**
     * Creates
     * 
     * @throws ComponentCreationException indicates that the object cannot be
     * created.
     */
    T newInstance() throws ComponentCreationException;

    /**
     * Cr
     * @param proxyGenerator
     * @throws ComponentCreationException indicates that the object proxy cannot 
     * be created.
     */
    T newInstance(ProxyGenerator<T> proxyGenerator) throws ComponentCreationException;
}
