package org.skycastle.util.componentgraph.model;

import java.util.List;

/**
 * Provides the data backend to a component graph.  In addition to the components and their properties, it also stores
 * the location of the components.
 *
 * @author Hans Haggstrom
 */
public interface ComponentGraph {

    /**
     * @return the components in this ComponentGraph.
     */
    List<ComponentInfo> getComponents();

    /**
     * @return the links between component properties (and components) in this ComponentGraph.
     */
    List<LinkInfo> getLinks();

    /**
     * @param listener a listener that is notified about added and removed components and links.  Should not be null.
     */
    void addComponentGraphModelListener(ComponentGraphModelListener listener);

    /**
     * @param listener listener to remove.  Should not be null.
     */
    void removeComponentGraphModelListener(ComponentGraphModelListener listener);
}
