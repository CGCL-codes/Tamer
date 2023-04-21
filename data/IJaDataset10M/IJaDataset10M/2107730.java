package jung.ext.predicates.vertex.impl;

import java.util.HashSet;
import java.util.Set;
import jung.ext.predicates.general.PredicateMode;
import jung.ext.predicates.vertex.DefaultVertexPredicate;
import jung.ext.utils.BasicUtils;
import edu.uci.ics.jung.graph.Vertex;

/**
 * The class <code>ThisPredicate</code> filters vertices upon
 * their relationship with a certain <em>main vertex</em>. In
 * this case this relationship is from primary order. Only the
 * vertex that equals the main vertex will pass the evaluation.
 * 
 * @author A.C. van Rossum
 */
public class ThisPredicate extends DefaultVertexPredicate {

    private static final String ID = ThisPredicate.class.getName();

    protected Set thisVertices = new HashSet();

    public ThisPredicate() {
        super();
    }

    public ThisPredicate(Vertex vertex) {
        super();
        addVertex(vertex);
    }

    /**
   * The vertex itself will be the filtering constraint.
   */
    public void addVertices(Set vertices) {
        thisVertices = vertices;
        useGraph(false);
        useVertices(vertices);
        setMode(PredicateMode.CHANGED);
    }

    public void addVertex(Vertex vertex) {
        Set vertices = new HashSet(1);
        vertices.add(vertex);
        addVertices(vertices);
    }

    public boolean preEvaluateVertex(Vertex vertex) {
        return thisVertices.contains(vertex);
    }

    public String getName() {
        return BasicUtils.afterDot(ID);
    }
}
