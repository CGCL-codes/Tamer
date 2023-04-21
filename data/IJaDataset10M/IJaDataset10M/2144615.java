package org.azrul.liquidframe.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Azrul Hasni MADISA
 */
public abstract class OneToMany extends AbstractActivity {

    protected Map<Long, Edge> toEdges = new HashMap<Long, Edge>();

    protected Edge fromEdge = null;

    /** Creates a new instance of Join */
    public OneToMany(Kernel kernel) {
        super(kernel);
    }

    public OneToMany(long id, Kernel kernel) {
        super(kernel);
        this.setId(id);
    }

    public List<Long> getToEdgeIds() {
        return new ArrayList<Long>(toEdges.keySet());
    }

    public void removeToEdge(Edge _toEdge) {
        if (this.currentModule != null) {
            if (this.currentModule.getAccessSpecifier().equals("PRIVATE")) {
                return;
            }
        }
        if (toEdges.containsKey(_toEdge.getId())) {
            _toEdge.setToActivity(null);
            toEdges.remove(_toEdge.getId());
        }
    }

    public void clearToEdges() {
        if (this.currentModule != null) {
            if (this.currentModule.getAccessSpecifier().equals("PRIVATE")) {
                return;
            }
        }
        for (Edge edge : toEdges.values()) {
            edge.setFromActivity(null);
        }
        toEdges.clear();
    }

    public Edge getFromEdge() {
        return fromEdge;
    }

    public List<Edge> getToEdges() {
        return new ArrayList<Edge>(toEdges.values());
    }

    public void removeFromEdge() {
        if (this.currentModule != null) {
            if (this.currentModule.getAccessSpecifier().equals("PRIVATE")) {
                return;
            }
        }
        fromEdge.setToActivity(null);
        this.fromEdge = null;
    }
}
