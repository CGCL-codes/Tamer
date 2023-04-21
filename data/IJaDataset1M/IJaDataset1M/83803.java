package com.emental.mindraider.core.facet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Custodian that takes care of facets. Facet is a class used to "filter" a RDF
 * graph to be more "readable".
 * 
 * @author Martin.Dvorak
 */
public final class FacetCustodian {

    /**
     * facets
     */
    private HashMap<String, Facet> facets;

    /**
     * singleton
     */
    private static FacetCustodian singleton;

    public static synchronized FacetCustodian getInstance() {
        if (singleton == null) {
            singleton = new FacetCustodian();
        }
        return singleton;
    }

    /**
     * Constructor.
     */
    private FacetCustodian() {
        facets = new HashMap<String, Facet>();
        register(new ExpertFacet());
        register(new NormalFacet());
        register(new BriefFacet());
        register(new NoAttachmentsFacet());
    }

    /**
     * Register new facet.
     * 
     * @param facet
     */
    public void register(Facet facet) {
        if (facet != null) {
            facets.put(facet.getLabel(), facet);
        }
    }

    /**
     * Get facet labels.
     * 
     * @return facet labels.
     */
    public String[] getFacetLabels() {
        ArrayList<String> result = new ArrayList<String>();
        Iterator iterator = facets.values().iterator();
        while (iterator.hasNext()) {
            result.add(((Facet) iterator.next()).getLabel());
        }
        if (result.size() > 0) {
            return result.toArray(new String[result.size()]);
        }
        return null;
    }

    /**
     * Get facet with particular label.
     * 
     * @param label
     * @return
     */
    public Facet getFacet(String label) {
        if (label != null) {
            return (Facet) facets.get(label);
        }
        return null;
    }
}
