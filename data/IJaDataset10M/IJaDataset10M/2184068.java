package com.pentagaia.eclipse.sgs.api;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

/**
 * An utility to customize the sgs configuration.
 * 
 * This utility will be called to customize configurations. At normal situations the user can change properties. At ui the sgs
 * gameserver project will call method {@link #propertyProptected(String)}. Every property that is marked as protected will be
 * wrapped through this utility class. The utility class can divide read-only and writable attributes.<br /><br />
 * 
 * <b>Read-only attributes</b><br /><br />
 * 
 * If a read-only attribute occurs the user cannot change it's value. The eclipse plugin will always present the value given by
 * method {@link #calculatePropertyValue(ISgsMutableApplicationConfig, String)}.<br /><br />
 * 
 * <b>Writable attributes</b><br /><br />
 * 
 * The user can change the value of this attribute. The ui presents the value given by {@link #getProtectedProperty(ISgsMutableApplicationConfig, String)}.
 * A plugin may store this value in different configuration files. If the user changes the value this is wrapped by attribut
 * {@link #setProtectedProperty(ISgsMutableApplicationConfig, String, String)}. An utility may use {@link #validateInput(ISgsMutableApplicationConfig, String, String)}
 * to verify the user input before it is saved. Before the plugin calculates the resulting properties file it asks for
 * the correct value by invoking {@link #calculatePropertyValue(ISgsMutableApplicationConfig, String)}.
 * 
 * 
 * @author mepeisen
 */
public interface IConfigurationUtil {

    /**
     * Tests if given property is protected.
     * 
     * A protected property will be wrapped by this utility class. F.e. this can be used to specify application listeners. The user might
     * want to change this value.
     * 
     * @param propertyName
     * @return {@code true} if this property is protected and read-only
     */
    boolean propertyProptected(String propertyName);

    /**
     * Tests if given property is read-only.
     * 
     * A read-only property cannot be changed by the user. F.e. this can be used to specify application listeners the user
     * cannot change.
     * 
     * @param propertyName
     * @return {@code true} if this property is protected and read-only
     */
    boolean propertyReadOnly(String propertyName);

    /**
     * Returns the default value for protected properties
     * 
     * @param config the mutable configuration
     * @param property key
     * @return default value
     */
    String calculatePropertyValue(ISgsMutableApplicationConfig config, String property);

    /**
     * Validates the user input on protected and writable properties.
     * 
     * @param config the mutable configuration
     * @param property key
     * @param value
     * @return the state
     */
    IStatus validateInput(ISgsMutableApplicationConfig config, String property, String value);

    /**
     * Sets a protected property (called if a property is protected and not read-only)
     * 
     * @param config the mutable configuration
     * @param property key
     * @param value
     * @throws CoreException thrown if the given value is not valid
     */
    void setProtectedProperty(ISgsMutableApplicationConfig config, String property, String value) throws CoreException;

    /**
     * Returns a protected property (called if a property is protected and not read-only)
     *  
     * @param config the mutable configuration
     * @param property key
     * @return value
     */
    String getProtectedProperty(ISgsMutableApplicationConfig config, String property);
}
