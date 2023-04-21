package org.nakedobjects.noa.interactions;

import org.nakedobjects.noa.facets.Facet;
import org.nakedobjects.noa.reflect.Allow;
import org.nakedobjects.noa.reflect.Veto;

/**
 * Marker interface for implementations (specifically, {@link Facet}s) that can advise as to whether a member
 * should be disabled.
 * 
 * Used within {@link Allow} and {@link Veto}.
 */
public interface InteractionAdvisor {

    /**
     * For testing purposes only.
     */
    public static InteractionAdvisor NOOP = new InteractionAdvisor() {
    };
}
