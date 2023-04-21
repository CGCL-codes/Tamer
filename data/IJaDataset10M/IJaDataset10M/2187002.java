package org.apache.shale.tiger.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.shale.tiger.managed.config.ManagedBeanConfig;

/**
 * <p>
 * Configuration bean representing the entire contents of zero or more <code>faces-config.xml</code> resources.
 * </p>
 */
public class FacesConfigConfig {

    private static final Logger log = Logger.getLogger(FacesConfigConfig.class);

    private ClassLoader loader;

    /** Creates a new instance of FacesConfigConfig. */
    public FacesConfigConfig() {
        loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = this.getClass().getClassLoader();
        }
    }

    /**
     * <p>
     * Map of {@link ManagedBeanConfig} elements for all defined managed bean configurations, keyed by managed bean name.
     * </p>
     */
    private Map<String, ManagedBeanConfig> managedBeans = new HashMap<String, ManagedBeanConfig>();

    /**
     * <p>
     * Add a new managed bean configuration element.
     * </p>
     * 
     * Method now only add a ManagedBeanConfig if no bean under this name exists or:
     * <ul>
     * <li>A existing bean is of the same class as the new bean, keeping the newest strategy
     * <li>A existing bean is not assignable to nor from the new bean, keeping the newest strategy
     * <li>A existing bean is assignable from the new bean, keeping the most specific strategy (the new one)
     * <li>A existing bean is assignable to the new bean, keeping the most specific strategy (the old one)
     * 
     * @since 2.0
     * 
     * @param newConfig
     *            {@link ManagedBeanConfig} element to be added
     * 
     */
    public void addManagedBean(ManagedBeanConfig newConfig) {
        ManagedBeanConfig existingConfig = this.managedBeans.get(newConfig.getName());
        if (existingConfig != null) {
            try {
                Class<?> existingClass = loader.loadClass(existingConfig.getType());
                Class<?> newClass = loader.loadClass(newConfig.getType());
                if (existingClass.equals(newClass)) {
                    log.error("two beans with the same name and the same class, keeping the newest one");
                    this.managedBeans.put(newConfig.getName(), newConfig);
                } else if (!existingClass.isAssignableFrom(newClass) && !newClass.isAssignableFrom(existingClass)) {
                    log.warn("two beans with the same name but not assignable to each other, keeping the newest one");
                    this.managedBeans.put(newConfig.getName(), newConfig);
                } else if (existingClass.isAssignableFrom(newClass)) {
                    if (log.isDebugEnabled()) {
                        log.debug("two beans with the same name, keeping the most specific one (the child)");
                    }
                    this.managedBeans.put(newConfig.getName(), newConfig);
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("two beans with the same name, keeping the most specific one (the child)");
                    }
                }
            } catch (ClassNotFoundException e) {
                log.error("should not happen, since the classes have already bean loaded!");
            }
        } else {
            this.managedBeans.put(newConfig.getName(), newConfig);
        }
    }

    /**
     * <p>
     * Return the {@link ManagedBeanConfig} element for the specified managed bean name, if any; otherwise, return <code>null</code>.
     * </p>
     * 
     * @param name
     *            Name of the managed bean configuration to return
     */
    public ManagedBeanConfig getManagedBean(String name) {
        return this.managedBeans.get(name);
    }

    /**
     * <p>
     * Return a map of defined {@link ManagedBeanConfig} elements, keyed by managed bean name.
     * </p>
     */
    public Map<String, ManagedBeanConfig> getManagedBeans() {
        return this.managedBeans;
    }
}
