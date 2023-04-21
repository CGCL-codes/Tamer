package pl.edu.agh.ssm.monitor.visualization;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Iterator;
import pl.edu.agh.ssm.monitor.data.SessionMediaType;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.render.EdgeRenderer;
import prefuse.visual.EdgeItem;

/**
 * 
 * @author Aksel Schmidt, Monika Nawrot
 * 
 * Edge renderer
 * 
 */
public class MultipleEdgeRenderer extends EdgeRenderer {

    private boolean useStraightLineForSingleEdges;

    public MultipleEdgeRenderer(int edgeTypeCurve, int edgeArrowForward) {
        super(edgeTypeCurve, edgeArrowForward);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void getCurveControlPoints(EdgeItem eitem, Point2D[] cp, double x1, double y1, double x2, double y2) {
        if (!eitem.isValid()) {
            return;
        }
        try {
            if (eitem.get("edgeMedia").equals(SessionMediaType.AUDIO)) eitem.setStrokeColor(new Color(255, 153, 153).getRGB());
            if (eitem.get("edgeMedia").equals(SessionMediaType.VIDEO)) eitem.setStrokeColor(new Color(204, 255, 153).getRGB());
            if (eitem.get("edgeMedia").equals(SessionMediaType.OTHER)) eitem.setStrokeColor(new Color(204, 204, 153).getRGB());
            if (eitem.get("edgeMedia").equals(SessionMediaType.RTSP_CONTROL)) eitem.setStrokeColor(new Color(10, 10, 10).getRGB());
            Node sourceNode = eitem.getSourceNode();
            Node targetNode = eitem.getTargetNode();
            Iterator edges = sourceNode.edges();
            int noOfEqualEdges = 0;
            int noOfSameNodeEdges = 0;
            int myEdgeIndex = 0;
            int row = eitem.getRow();
            while (edges.hasNext()) {
                Edge edge = (Edge) edges.next();
                int edgeRow = edge.getRow();
                if (edge.getSourceNode() == sourceNode && edge.getTargetNode() == targetNode) {
                    if (row == edgeRow) {
                        myEdgeIndex = noOfEqualEdges;
                    }
                    noOfEqualEdges++;
                    noOfSameNodeEdges++;
                } else if (edge.getSourceNode() == targetNode && edge.getTargetNode() == sourceNode) {
                    noOfSameNodeEdges++;
                }
            }
            double dx = x2 - x1, dy = y2 - y1;
            dx = dx * (1 + myEdgeIndex);
            dy = dy * (1 + myEdgeIndex);
            cp[0].setLocation(x1 + 2 * dx / 3, y1);
            cp[1].setLocation(x2 - dx / 8, y2 - dy / 8);
            if (useStraightLineForSingleEdges && myEdgeIndex == 0 && noOfSameNodeEdges == 1) {
                cp[0].setLocation(x2, y2);
                cp[1].setLocation(x1, y1);
            }
        } catch (NullPointerException ex) {
        }
    }

    public boolean isUseStraightLineForSingleEdges() {
        return useStraightLineForSingleEdges;
    }

    public void setUseStraightLineForSingleEdges(boolean useStraightLineForSingleEdges) {
        this.useStraightLineForSingleEdges = useStraightLineForSingleEdges;
    }
}
