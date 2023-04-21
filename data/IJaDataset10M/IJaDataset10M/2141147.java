package com.reserveamerica.elastica.cluster;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import com.reserveamerica.elastica.common.ExtendableString;

/**
 * An implementation of the FactoryDefinition interface.
 * 
 * @see FactoryDefinition
 * @author BStasyszyn
 */
public class FactoryDefinitionImpl<T> extends DefinitionImpl implements FactoryDefinition<T> {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FactoryDefinitionImpl.class);

    private static final long serialVersionUID = -3286750033667317993L;

    private final String factoryClass;

    private final Map<String, Collection<String>> attributes;

    /**
   * This constructor fully initializes the object.
   * 
   * @param id
   * @param description
   * @param metaData - Map< String, String > - Name-value pairs - must not be null.
   * @param factoryClass
   * @param attributes - Map< String, Collection<String> > - Name to values collection - must not be null.
   */
    public FactoryDefinitionImpl(String id, String description, Map<String, ExtendableString> metaData, String factoryClass, Map<String, Collection<String>> attributes) {
        super(id, description, metaData);
        this.factoryClass = factoryClass;
        this.attributes = new LinkedHashMap<String, Collection<String>>(attributes);
    }

    public String getFactoryClass() {
        return factoryClass;
    }

    public Map<String, Collection<String>> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public Factory<T> create() throws ClassNotFoundException, ClusterConfigException {
        return instantiate(factoryClass);
    }

    /**
   * Instantiates the class.
   * 
   * @param className
   * @throws ClassNotFoundException - Thrown if the class is not found in the current class loader.
   * @throws ClusterConfigException - Thrown if an unexpected error occurs.
   */
    @SuppressWarnings("unchecked")
    private Factory<T> instantiate(String className) throws ClassNotFoundException, ClusterConfigException {
        Class<Factory<T>> clazz = (Class<Factory<T>>) Class.forName(className);
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            log.error("An error occurred while attempting to instantiate class [" + className + "].", ex);
            throw new ClusterConfigException("An error occurred while attempting to instantiate class [" + className + "].");
        }
    }
}
