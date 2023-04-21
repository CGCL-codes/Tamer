package itGraph.PetriNetElements;

import java.awt.geom.Point2D;
import org.jdom.Element;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;

public class BasicArcView extends EdgeView {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6490103045995707413L;

    public BasicArcView(Object cell) {
        super(cell);
        BasicArc arc = (BasicArc) cell;
        AttributeMap attributes = arc.getAttributes();
        Element weightLabelOffSet;
        weightLabelOffSet = arc.getData().getChild("inscription").getChild("graphics").getChild("offset");
        GraphConstants.setLabelPosition(attributes, new Point2D.Double(GraphConstants.PERMILLE / 2, Integer.parseInt(weightLabelOffSet.getAttributeValue("y"))));
    }
}
