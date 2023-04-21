package com.google.code.joto.exportable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Liliana.nu
 * @author epere4
 */
public class InstancesMap {

    /** instances */
    private Map<Integer, Object> instances = new HashMap<Integer, Object>();

    /**
     * This is just a wrapper to a {@link Map} that will hold all the instances using its
     * identityHashCode as key. You can get the identityHashCode for an object by calling
     * {@link System#identityHashCode(Object)}.
     * <p>
     * Example of intended usage:
     * <p>
     * <code>ReverseEngineerReflectionUtil.registerInstance( System.identityHashCode( someObject ), someObject);</code>
     * @param <T> the type of the instance.
     * @param identityHashCode the value that will be used as key in the map.
     * @param instance the instance to store.
     * @return the same instance received as parameter.
     */
    public <T> T registerInstance(Integer identityHashCode, T instance) {
        instances.put(identityHashCode, instance);
        return instance;
    }

    /**
     * This method retrieves objects stored by {@link #registerInstance(Integer, Object)}. *
     * <p>
     * Example of intended usage:
     * 
     * <pre>
     * Integer identityHashCode = ...;
     * Object someObject = ReverseEngineerReflectionUtil.getInstance(identityHashCode)
     * </pre>
     * @param <T>
     * @param identityHashCode
     * @return the object previously stored for that identityHashCode.
     */
    @SuppressWarnings("unchecked")
    public <T> T getInstance(Integer identityHashCode) {
        return (T) instances.get(identityHashCode);
    }
}
