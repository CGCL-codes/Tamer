package biologicalObjects.edges;

import biologicalElements.Elementdeclerations;
import edu.uci.ics.jung.graph.Edge;

public class Activation extends BiologicalEdgeAbstract {

    public Activation(Edge edge, String label, String name) {
        super(edge, label, name);
        setBiologicalElement(Elementdeclerations.activationEdge);
        setAbstract(false);
    }
}
