package uk.ac.ebi.intact.application.search3.advancedSearch.powerSearch.business.graphdraw.graph;

/**
 * General graph edge.
 *
 * @author EGO
 * @version $Id: Edge.java 4573 2006-02-02 16:13:50Z skerrien $
 * @since 27.04.2005
 */
public interface Edge {

    /**
     * Returns the parent value.
     * @return a Node object representing the parent value
     */
    Node getParent();

    /**
     * Returns the child value.
     * @return a Node object representing the child value
     */
    Node getChild();
}
