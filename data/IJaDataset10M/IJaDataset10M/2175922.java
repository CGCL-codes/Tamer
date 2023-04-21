package biologicalObjects.edges;

import biologicalElements.Elementdeclerations;
import edu.uci.ics.jung.graph.Edge;

public class Compound extends BiologicalEdgeAbstract {

    public Compound(Edge edge, String label, String name) {
        super(edge, label, name);
        setBiologicalElement(Elementdeclerations.compoundEdge);
        setAbstract(false);
    }
}
