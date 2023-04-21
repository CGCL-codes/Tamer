package edu.uci.ics.jung.graph.impl;

import java.util.Collection;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.predicates.TreePredicate;
import edu.uci.ics.jung.utils.UserData;

/**
 * An implementation of <code>Graph</code> that consists of a 
 * <code>Vertex</code> set and a <code>DirectedEdge</code> set.
 * Further, a vertex can have no more than one incoming directed
 * edge (enforced with <code>TreePredicate</code>); the tree must
 * define a root vertex at construction time.
 * This implementation does NOT ALLOW parallel edges. 
 * <code>SimpleDirectedSparseVertex</code> is the most efficient
 * vertex for this graph type.
 *
 * @author Danyel Fisher
 * @author Joshua O'Madadhain
 * 
 * @see DirectedSparseVertex
 * @see DirectedSparseEdge
 */
public class SparseTree extends SparseGraph implements DirectedGraph {

    protected Vertex mRoot;

    public static final Object SPARSE_ROOT_KEY = "edu.uci.ics.jung.graph.impl.SparseTree.RootKey";

    public static final Object IN_TREE_KEY = "edu.uci.ics.jung.graph.impl.SparseTree.InTreeKey";

    /**
     * @param root
     */
    public SparseTree(Vertex root) {
        Collection edge_predicates = getEdgeConstraints();
        edge_predicates.add(DIRECTED_EDGE);
        edge_predicates.add(NOT_PARALLEL_EDGE);
        edge_predicates.add(TreePredicate.getInstance());
        this.mRoot = root;
        addVertex(root);
        mRoot.setUserDatum(SPARSE_ROOT_KEY, SPARSE_ROOT_KEY, UserData.SHARED);
        mRoot.setUserDatum(IN_TREE_KEY, IN_TREE_KEY, UserData.SHARED);
    }

    /**
     * @return
     */
    public Vertex getRoot() {
        return mRoot;
    }

    /**
     * @see edu.uci.ics.jung.graph.Graph#addEdge(edu.uci.ics.jung.graph.Edge)
     */
    public Edge addEdge(Edge e) {
        Edge rv = super.addEdge(e);
        Vertex dest = (Vertex) rv.getEndpoints().getSecond();
        dest.setUserDatum(IN_TREE_KEY, IN_TREE_KEY, UserData.SHARED);
        return rv;
    }
}
