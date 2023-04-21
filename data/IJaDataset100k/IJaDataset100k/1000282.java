package com.c4j.ant;

import com.c4j.sre.C4JRuntimeException;

/**
 * A simple factory class that creates an instance of the appropriate component.
 */
public final class AntTasksFactory {

    /**
     * A private empty constructor to avoid instantiation of this class.
     */
    private AntTasksFactory() {
    }

    /**
     * The factory method that creates an instance of the appropriate component by reflection
     * to be independent from the implementation of the component.
     *
     * @param instanceName
     *         the name of the created instance.
     *
     * @return an instance of the appropriate component.
     *
     * @throws C4JRuntimeException if the instance can not be created by reflection for some
     *         reason.
     */
    public static AntTasksIface create(final String instanceName) {
        try {
            return (AntTasksIface) Class.forName("com.c4j.ant.AntTasksImpl").getConstructor(Class.forName("java.lang.String")).newInstance(instanceName);
        } catch (Exception e) {
            throw new C4JRuntimeException(String.format("Could not create component instance ‘%s’.", instanceName), e);
        }
    }
}
