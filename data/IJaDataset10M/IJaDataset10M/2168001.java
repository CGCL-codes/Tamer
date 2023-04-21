package biologicalObjects.nodes;

import biologicalElements.Elementdeclerations;
import edu.uci.ics.jung.graph.Vertex;

public class Receptor extends Protein {

    public Receptor(String label, String name, Vertex vertex) {
        super(label, name, vertex);
        setBiologicalElement(Elementdeclerations.receptor);
        setAbstract(false);
    }
}
