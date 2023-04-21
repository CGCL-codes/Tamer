package org.proteusframework.core.api.config;

/**
 * Dynamically configures a {@link org.proteusframework.core.api.model.IStandardPlugin} using a well-defined, queryable
 * set of configuration properties. Any property can be marked as mandatory or optional.
 * * <p/>
 * Configurable objects provide maximum flexibility without imposing an inordinate amount of method signatures across
 * an interface.
 * <p/>
 * The <code>IConfigurable</code> interface is typically implemented by the object managing the configuration, whereas
 * the {@link IConfiguration} interface is used by the consumer. For example, if an abstract class is built using
 * composition, it can manage the collective set of configurations, sharing the configurations with the composed
 * objects via its {@link IConfiguration} interface instead of its <code>IConfigurable</code> interface. This approach
 * ensures that the composed objects can only read the values, not update them.
 *
 * @author Tacoma Four
 */
public interface IConfigurable extends IConfiguration {

    /**
     * Defines a runtime configuration property for the given key.
     *
     * @param property Configured property
     * @throws IllegalArgumentException when the key does not match a well-defined configurable property already defined
     *                                  within the {@link #configuredProperties()} collection.
     */
    void setConfigProperty(IConfigProperty property) throws IllegalArgumentException;

    /**
     * Defines a runtime configuration property, as read from an XML configuration file. The assigned value is
     * guaranteed to be a non-null String. It is the responsibility of the configurable object to parse and process
     * the String into a meaningful object, as required.
     *
     * @param property Runtime property to define
     */
    void processRawProperty(IConfigProperty property);
}
