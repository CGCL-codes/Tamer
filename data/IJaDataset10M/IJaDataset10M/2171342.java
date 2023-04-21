package com.volantis.mcs.interaction;

import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.property.PropertyIdentifier;

/**
 * A proxy for a bean like object in the model.
 *
 * <p>As far as the interaction layer is concerned a bean object is an object
 * that simply has a set of properties of various different types.</p>
 *
 * <p>This has a reference to a proxy object for every property in the model
 * object, irrespective of whether that property is a simple type, or another
 * model object.</p>
 */
public interface BeanProxy extends ParentProxy {

    /**
     * Get the descriptor of the underlying model object.
     *
     * @return The descriptor of the underlying model object.
     */
    BeanClassDescriptor getBeanClassDescriptor();

    /**
     * Get the value of the proxy's property.
     *
     * <p>If no proxy has been associated with the property then this will
     * automatically create one based on the description of that property.</p>
     *
     * @param property The property whose proxy should be returned.
     * @return The proxy for the property.
     */
    Proxy getPropertyProxy(PropertyIdentifier property);

    /**
     * Set the value of the proxy's property.
     *
     * @param property The property whose proxy should be set.
     * @param newProxy The value of the proxy's property.
     * @return The old value of the proxy's property.
     */
    Object setPropertyProxy(PropertyIdentifier property, Proxy newProxy);

    /**
     * Get the property with which the associated proxy is associated.
     *
     * @param proxy A child proxy.
     * @return The property with which the proxy is associated.
     * @throws IllegalStateException if the proxy is not associated with any
     *                               property.
     */
    PropertyIdentifier getPropertyForProxy(Proxy proxy);
}
