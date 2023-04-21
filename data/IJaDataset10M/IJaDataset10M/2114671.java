package org.nakedobjects.metamodel.specloader;

import java.util.List;
import org.nakedobjects.commons.components.ApplicationScopedComponent;
import org.nakedobjects.commons.components.Injectable;
import org.nakedobjects.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

public interface SpecificationLoader extends ApplicationScopedComponent, Injectable, DebugInfo {

    NakedObjectSpecification[] allSpecifications();

    /**
     * @see #loadSpecification(String)
     */
    NakedObjectSpecification loadSpecification(Class<?> cls);

    /**
     * Return the specification for the specified class of object.
     * 
     * <p>
     * It is possible for this method to return <tt>null</tt>, for example if the configured
     * {@link #getClassSubstitutor()} has filtered out the class.
     */
    NakedObjectSpecification loadSpecification(String fullyQualifiedClassName);

    /**
     * Whether this class has been loaded.
     */
    boolean loaded(Class<?> cls);

    /**
     * @see #loaded(Class).
     */
    boolean loaded(String fullyQualifiedClassName);

    /**
     * Specify the classes of the services to pro-actively prime the cache.
     */
    void setServiceClasses(List<Class<?>> serviceClasses);
}
