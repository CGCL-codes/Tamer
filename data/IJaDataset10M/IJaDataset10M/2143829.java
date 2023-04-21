package com.c4j.analyser;

import com.c4j.sre.IFacet;

/**
 * Specifies all methods to get the provided facets of the appropriate component and to connect or
 * disconnect the receptacles with facets of possibly different fragment instances.
 */
public interface AnalyserIface {

    /**
     * Returns the facet ‘analyser’ of the appropriate component.
     *
     * Will be implemented finally by the base of the component.
     *
     * @return a facet specialized to the interface as specified for the component facet.
     */
    IFacet<IAnalyser> getFacet_analyser();
}
