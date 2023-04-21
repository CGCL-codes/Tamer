package org.jfrog.maven.viewer.ui.model.jung.filter;

import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.predicates.EdgePredicate;
import org.jfrog.maven.viewer.ui.model.jung.JungModelHelper;

/**
 * User: Dror Bereznitsky
 * Date: 24/03/2007
 * Time: 01:10:01
 */
public class ScopeEdgePredicate extends EdgePredicate {

    private final String scope;

    public ScopeEdgePredicate(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean evaluateEdge(ArchetypeEdge e) {
        String labelScope = JungModelHelper.getScope((Edge) e).getName();
        return !scope.equals(labelScope);
    }

    public String getScope() {
        return scope;
    }
}
