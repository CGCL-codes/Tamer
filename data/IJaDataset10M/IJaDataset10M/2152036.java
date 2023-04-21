package graph.jung.graphDrawing;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.DirectionalEdgeArrowFunction;
import edu.uci.ics.jung.graph.decorators.EdgeArrowFunction;
import edu.uci.ics.jung.visualization.contrib.Arrow;
import biologicalElements.Pathway;
import biologicalObjects.edges.BiologicalEdgeAbstract;

public class MyEdgeArrowFunction implements EdgeArrowFunction {

    Pathway pw;

    public MyEdgeArrowFunction(Pathway pw) {
        this.pw = pw;
    }

    @Override
    public Shape getArrow(Edge e) {
        Iterator i = pw.getAllEdges().iterator();
        while (i.hasNext()) {
            BiologicalEdgeAbstract bea = (BiologicalEdgeAbstract) i.next();
            if (bea.getEdge().equals(e) && (bea.getBiologicalElement().equals(biologicalElements.Elementdeclerations.pnInhibitionEdge) || bea.getBiologicalElement().equals(biologicalElements.Elementdeclerations.inhibitionEdge) || bea.getBiologicalElement().equals(biologicalElements.Elementdeclerations.inhibitor))) return new Ellipse2D.Double(-10, -5, 10, 10);
        }
        return new DirectionalEdgeArrowFunction(10, 8, 4).getArrow(e);
    }
}
