package org.xmlfield.core;

/**
 * Genric xml field factory.
 * 
 * @author Guillaume Mary <guillaume.mary@capgemini.com>
 */
public abstract class XmlFieldFactory {

    /**
     * <p>
     * Get a new factory instance managed by the <code>XmlFieldFactoryFinder</code>.
     * 
     * @return Instance of an xml field factory.
     * 
     * @throws RuntimeException
     *             When there is a failure in creating an <code>XmlFieldSelectorFactory</code>
     */
    protected static final <T> T newInstance(Class<T> factoryClass) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = XmlFieldFactory.class.getClassLoader();
        }
        T xmlFieldFactory = new XmlFieldFactoryFinder(classLoader).newFactory(factoryClass);
        return xmlFieldFactory;
    }
}
