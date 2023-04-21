package org.progeeks.graph;

import com.phoenixst.plexus.*;

/**
 *  A default implementation of the AtomicGraph interface.
 *  The atomic methods on this class are not thread safe since
 *  this implementation is based on DefaultGraph.
 *
 *  @version   $Revision: 3857 $
 *  @author    Paul Speed
 */
public class DefaultAtomicGraph extends EdgeIdentityGraph implements AtomicGraph {

    /**
     *  Replaces the specified existing edge with the new
     *  edge description if, and only if, the existing edge is
     *  in the graph at the time of this call.  This follows the
     *  typical atomic test and set idiom.
     *
     *  Note: the current implementation does not transfer
     *        edges pointing to the existing edge and they
     *        will be removed as a result of this method.
     */
    public synchronized Graph.Edge replaceEdge(Graph.Edge existing, Object userObject, Object tail, Object head, boolean directed) {
        if (!containsEdge(existing)) {
            return null;
        }
        Graph.Edge newEdge = addEdge(userObject, tail, head, directed);
        if (newEdge == null) {
            return existing;
        }
        removeEdge(existing);
        return newEdge;
    }
}
