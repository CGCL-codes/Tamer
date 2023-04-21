package nl.jteam.jsa.core;

import java.util.Set;

/**
 * Holds all facet information related to a search operation.
 *
 * @author Uri Boness
 * @see FacetedSearchResult#getFacets()
 */
public interface Facets {

    /**
     * Returns all facets.
     *
     * @return All facets.
     */
    Set<Facet> getAllFacets();

    /**
     * Returns the facet associated with the given facet name.
     *
     * @param name The name of the facet to return.
     * @return The facet associated with the given facet name or <code>null</code> if no such facet was found.
     */
    Facet getFacet(String name);
}
