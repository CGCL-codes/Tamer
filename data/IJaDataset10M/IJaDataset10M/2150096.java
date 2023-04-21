package org.apache.tapestry5.ioc.internal;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ObjectLocator;
import java.util.Collection;

/**
 * Wraps a {@link java.util.Collection} as a {@link org.apache.tapestry5.ioc.Configuration} and perform validation that
 * collected value are of the correct type.
 */
public class ValidatingConfigurationWrapper<T> implements Configuration<T> {

    private final String serviceId;

    private final Class expectedType;

    private final Collection<T> collection;

    private final ObjectLocator locator;

    public ValidatingConfigurationWrapper(Collection<T> collection, String serviceId, Class expectedType, ObjectLocator locator) {
        this.collection = collection;
        this.serviceId = serviceId;
        this.expectedType = expectedType;
        this.locator = locator;
    }

    public void add(T object) {
        if (object == null) throw new NullPointerException(IOCMessages.contributionWasNull(serviceId));
        if (!expectedType.isInstance(object)) throw new IllegalArgumentException(IOCMessages.contributionWrongValueType(serviceId, object.getClass(), expectedType));
        collection.add(object);
    }

    public void addInstance(Class<? extends T> clazz) {
        add(locator.autobuild(clazz));
    }
}
