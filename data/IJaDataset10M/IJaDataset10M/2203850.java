package org.nakedobjects.nof.reflect.javax;

import org.nakedobjects.noa.spec.NakedObjectSpecification;

/**
 * Implemented by all objects that have been proxied as per
 * {@link JavaNotifyingDomainObjectContainer#proxy(Object)}.
 */
public interface ViewObject<T> {

    DomainObjectInvocationHandler<T> getHandler();

    /**
     * So that tests can save on transient objects (programmatically equivalent to hitting the <i>save</i>
     * button that the DnD viewer automatically renders.
     * 
     * <p>
     * To use, simply cast the domain object view to {@link ViewObject}.
     */
    void save();

    /**
     * Provide access to the underlying object.
     * 
     * <p>
     * Used to unwrap objects used as arguments to actions (otherwise, end up creating a
     * {@link NakedObjectSpecification} for the CGLib-enhanced class, not the original class).
     */
    Object underlying();
}
