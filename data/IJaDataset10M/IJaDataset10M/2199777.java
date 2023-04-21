package org.nakedobjects.noa.adapter;

import org.nakedobjects.noa.spec.NakedObjectSpecification;

public interface ObjectFactory {

    /**
     * Sets up an new object to work within the business container, and initialises the logical objects.
     */
    Object createObject(NakedObjectSpecification specification);

    Object createValueObject(NakedObjectSpecification specification);

    /**
     * Indicates to the component that it is to initialise itself as it will soon be receiving requests.
     */
    void init();

    /**
     * Sets up an existing object to work within the business container. This is only needed if the object is
     * created outside the framework, such as through serialization, or within an object persistor.
     */
    void initRecreatedObject(Object object);

    /**
     * Indicates to the component that no more requests will be made of it and it can safely release any
     * services it has hold of.
     */
    void shutdown();
}
