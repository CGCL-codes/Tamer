package org.nakedobjects.metamodel.specloader.internal.cache;

import org.nakedobjects.commons.components.ApplicationScopedComponent;
import org.nakedobjects.commons.components.SessionScopedComponent;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.NakedObjectReflectorAbstract;

/**
 * This is not API.
 * 
 * <p>
 * In an earlier version it was possible to inject the {@link SpecificationCache} into the 
 * {@link NakedObjectReflectorAbstract reflector}.  This was needed when the reflector was
 * original (what is now called) {@link SessionScopedComponent session scoped}, rather than 
 * {@link ApplicationScopedComponent application-scoped}.
 *
 * <p>
 * This interface has been left in for now, but will likely be removed.
 */
public interface SpecificationCache {

    /**
     * Returns the {@link NakedObjectSpecification}, or possibly <tt>null</tt> if has not
     * been cached.
     */
    NakedObjectSpecification get(String className);

    NakedObjectSpecification[] allSpecifications();

    void cache(String className, NakedObjectSpecification spec);

    void clear();
}
