package org.apache.tomcat.util.digester;

import org.xml.sax.Attributes;

/**
 * <p> Interface for use with {@link FactoryCreateRule}.
 * The rule calls {@link #createObject} to create an object
 * to be pushed onto the <code>Digester</code> stack
 * whenever it is matched.</p>
 *
 * <p> {@link AbstractObjectCreationFactory} is an abstract
 * implementation suitable for creating anonymous
 * <code>ObjectCreationFactory</code> implementations.
 */
public interface ObjectCreationFactory {

    /**
     * <p>Factory method called by {@link FactoryCreateRule} to supply an
     * object based on the element's attributes.
     *
     * @param attributes the element's attributes
     *
     * @throws Exception any exception thrown will be propagated upwards
     */
    public Object createObject(Attributes attributes) throws Exception;

    /**
     * <p>Returns the {@link Digester} that was set by the
     * {@link FactoryCreateRule} upon initialization.
     */
    public Digester getDigester();

    /**
     * <p>Set the {@link Digester} to allow the implementation to do logging,
     * classloading based on the digester's classloader, etc.
     *
     * @param digester parent Digester object
     */
    public void setDigester(Digester digester);
}
