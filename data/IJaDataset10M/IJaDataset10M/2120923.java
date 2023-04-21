package org.jmove.kernel;

/**
 * This factory implementation can generically be used to create instances for a specified interface.
 * The instances created by the factory are called concrete products while the type requested by the
 * factories client is the abstract product type. The factory instance will be parametrized with the
 * abstract or interface type and the class of the concrete product.<br>
 * <br>
 * This factory defines some constraints:
 * <ol>
 *   <li>The interface type must be assignable from the product type</li>
 *   <li>The product class must provide a public default constructor</li>
 *   <li>The factory can only be used for one abstract/concrete product pair type.</li>
 *   <li>Creating an instance will fail if the requested type is not equal to the interface type</li>
 * </ol>
 * <br>
 * The constraints enforced by this implementation make this generic implementation type save to a hight degree.
 * It allows the installation of the factory with one call into a core context. Remember that this generic implementation
 * is slower than a hand implemented factory.
 *
 * @author axelt
 */
public class SimpleFactory extends AbstractFactory {

    /**
     * Creates an new factory and registers it in the specified aContext.
     * All parameters have to be not null.
     * @param aContext the created factory will be placed in
     * @param aType the product type of the factory
     */
    public static SimpleFactory addFactoryToContext(Context aContext, Class aType) {
        return addFactoryToContext(aContext, aType, aType);
    }

    /**
     * Creates an new factory and registers it in the specified aContext.
     * @param aContext the created factory will be placed in
     * @param anAbstractType the abstract product type of the factory
     * @param aConcreteType the concrete product type of the factory,
     * the concrete product type should implement or extend the abstract product type
     */
    public static SimpleFactory addFactoryToContext(Context aContext, Class anAbstractType, Class aConcreteType) {
        Assert.isNotNull(aContext);
        SimpleFactory factory = new SimpleFactory();
        factory.init(anAbstractType, aConcreteType);
        aContext.factory().register(anAbstractType, factory);
        return factory;
    }

    /** Creates an instance of the configured concrete product type. This will fail with an exception,
	 * if the factory is not properly configured of if the class passes as the parameter to the method does
	 * not match the abstract product type.
	 */
    public Object newInstance(Class aType) {
        assertNewInstance(aType);
        return super.newInstance(aType);
    }

    private void assertNewInstance(Class aType) {
        Assert.isTrue("Requested product type does not match the abstract product type", aType == myAbstractProductType);
    }
}
