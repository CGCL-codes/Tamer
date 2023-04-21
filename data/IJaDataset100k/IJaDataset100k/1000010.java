package vilaug.grammar;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.StringLabeller;
import jung.ext.EdgeStringLabeller;
import jung.ext.DefaultDirectedGraph;

/**
 * The <code>GrammarGraph</code> does allow for multiple edges and
 * even circular edges to the very same node.
 *
 * The StringLabeller for vertices is stored with the graph. Function
 * <code>addVertex</code> and <code>getVertex</code> that can handle 
 * labels are added. These labels are not ordinary strings, but of the
 * type Dot. Appropriate for GrammarGraph's.
 * 
 * @author A.C. van Rossum
 *
 */
public class GrammarGraph extends DefaultDirectedGraph {

    protected StringLabeller nodeLabeller;

    protected EdgeStringLabeller edgeLabeller;

    protected GrammarWeavers crawlers;

    public GrammarGraph(GrammarWeavers crawlers) {
        super();
        nodeLabeller = StringLabeller.getLabeller(this);
        edgeLabeller = EdgeStringLabeller.getLabeller(this);
        this.crawlers = crawlers;
    }

    /**
   * A GrammarVertex can be added to the graph on a certain position. This
   * position should be stored in the UserData of the Vertex. The dot itself
   * should not be stored overthere. In that way it is possible to advance
   * the dot without effecting previous created vertices. It catches multiple
   * adding of the same vertex with a warning.
   * @param v The GrammarVertex implementation of Vertex
   * @return
   */
    public Vertex addVertex(Vertex v) {
        if (!(v instanceof GrammarVertex)) {
            System.out.println("Error: the Vertex is no (subclass of) GrammarVertex");
            return null;
        }
        GrammarVertex added = (GrammarVertex) super.addVertex(v);
        String label = Integer.toString(added.getPos());
        try {
            nodeLabeller.setLabel((Vertex) added, label);
        } catch (StringLabeller.UniqueLabelException ule) {
            System.out.println("Remark: Vertex \"" + label + "\" " + "exists already in this graph. This new vertex will" + "be removed again.");
            removeVertex(added);
        }
        return added;
    }

    /**
   * The GrammarGraph stores a nodeLabeller for the vertices. Vertices do
   * have unique dot positions. So they can be retrieved by using a
   * Dot object.
   * @param dot Dot storing position.
   * @return The GrammarVertex belonging to the Dot.
   */
    public Vertex getVertex(Dot dot) {
        String label = dot.toString();
        return nodeLabeller.getVertex(label);
    }

    /**
   * A GrammarEdge can be added to the graph having his source and 
   * destination Vertex defined in its parameters. It does have a Rule
   * stored in its UserData. The completeness of this rule dictates the
   * behaviour at the end of this function. If the rule is complete the
   * Completer will be executed, if its incomplete the Predictor. Only
   * after they have completed their tasks of adding edges the added 
   * edge of this function will be returned. It's LIFO.
   * <p>It catches multiple adding of the same vertex with a warning.
   * The function edgeExists should refrain components of adding edges 
   * when they already exist.
   * @param e The GrammarEdge implemention of the Edge interface.
   * @return The added GrammarEdge. <code>addEdge</code> returns added
   * edges in a LIFO way after calling the predictor and completer 
   * recursively and alternately.
   */
    public Edge addEdge(Edge e) {
        boolean edgeExists = false;
        if (!(e instanceof GrammarEdge)) {
            System.out.println("Error: the Edge is no (subclass of) GrammarEdge");
            return null;
        }
        GrammarEdge added = (GrammarEdge) super.addEdge(e);
        String label = added.toString();
        try {
            edgeLabeller.setLabel((Edge) added, label);
        } catch (EdgeStringLabeller.UniqueLabelException ule) {
            System.out.println("Remark: Edge \"" + label + "\" " + "exists already in this graph. This new edge will " + "be removed again. \nThis warning should be removed.");
            removeEdge(added);
            edgeExists = true;
        }
        if (!edgeExists) {
            if (added.isComplete()) {
                crawlers.getCompleter().complete();
            } else {
                crawlers.getPredictor().predict();
            }
        }
        return added;
    }

    /**
   * The function <code>exists</code> can be used to be sure that an edge
   * does not exist, before adding it to the graph by <code>addEdge</code>.
   * The latter does correct multiple addings, but gives a warning. 
   * @return A boolean denoting existence in this graph.
   */
    public boolean exists(Edge edge) {
        return (getEdge(edge) != null);
    }

    public Edge getEdge(Edge edge) {
        String label = edge.toString();
        return edgeLabeller.getEdge(label);
    }
}
