package org.nakedobjects.noa.facets;

import org.nakedobjects.noa.facets.actions.invoke.ActionInvocationFacet;

public interface Facet {

    /**
     * The {@link FacetHolder holder} of this facet.
     * 
     * @return
     */
    FacetHolder getFacetHolder();

    /**
     * Allows reparenting of Facet.
     * 
     * <p>
     * Used by Facet decorators.
     * 
     * @param facetHolder
     */
    public void setFacetHolder(FacetHolder facetHolder);

    /**
     * Determines the type of this facet to be stored under.
     * 
     * <p>
     * The framework looks for {@link Facet}s of certain well-known facet types. Each facet implementation
     * must specify which type of facet it corresponds to. This therefore allows the (rules of the)
     * programming model to be varied without impacting the rest of the framework.
     * 
     * <p>
     * For example, the {@link ActionInvocationFacet} specifies the facet to invoke an action. The typical
     * implementation of this wraps a <tt>public</tt> method. However, a different facet factory could be
     * installed that creates facet also of type {@link ActionInvocationFacet} but that have some other rule,
     * such as requiring an <i>action</i> prefix, or that decorate the interaction by logging it, for example.
     */
    Class<? extends Facet> facetType();

    /**
     * Whether this facet implementation is a no-op.
     */
    public boolean isNoop();

    /**
     * Whether this facet implementation should replace existing (none-noop) implementations.
     */
    public boolean alwaysReplace();
}
