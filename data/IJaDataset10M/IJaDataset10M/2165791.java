package org.ztest.filter;

import java.util.Set;
import org.ztest.classinfo.ZIClassEdge;
import org.ztest.classinfo.ZIClassEdgeFilter;

public class ZClassEdgeFilterByClasses implements ZIClassEdgeFilter {

    private final ZIClassEdgeFilter next;

    final Set<String> classes;

    public ZClassEdgeFilterByClasses(Set<String> classes) {
        this(classes, new ZClassEdgeFilterAcceptAll());
    }

    public ZClassEdgeFilterByClasses(Set<String> classes, ZIClassEdgeFilter next) {
        super();
        this.classes = classes;
        this.next = next;
    }

    public boolean accept(ZIClassEdge edge) throws Exception {
        if (edge.isLoop()) {
            return false;
        }
        return classes.contains(edge.getSource()) && classes.contains(edge.getTarget()) && next.accept(edge);
    }
}
