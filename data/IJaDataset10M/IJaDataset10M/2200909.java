package org.ztest.filter;

import java.util.Set;
import org.ztest.classinfo.ZIClassEdge;
import org.ztest.classinfo.ZIClassEdgeFilter;

public class ZClassEdgeFilterByEdges implements ZIClassEdgeFilter {

    final Set<ZIClassEdge> edges;

    private final ZIClassEdgeFilter next;

    public ZClassEdgeFilterByEdges(Set<ZIClassEdge> edges) {
        this(edges, new ZClassEdgeFilterAcceptAll());
    }

    public ZClassEdgeFilterByEdges(Set<ZIClassEdge> edges, ZIClassEdgeFilter next) {
        super();
        this.next = next;
        this.edges = edges;
    }

    public boolean accept(ZIClassEdge edge) throws Exception {
        if (edge.isLoop()) {
            return false;
        }
        return edges.contains(edge) && next.accept(edge);
    }
}
