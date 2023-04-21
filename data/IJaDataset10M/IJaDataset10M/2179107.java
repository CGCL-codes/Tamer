package graph.jung.graphDrawing;

import java.awt.Color;
import java.awt.Paint;
import java.util.Iterator;
import java.util.Set;
import biologicalElements.GraphElementAbstract;
import biologicalElements.Pathway;
import biologicalObjects.edges.BiologicalEdgeAbstract;
import configurations.NetworkSettings;
import configurations.NetworkSettingsSingelton;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgePaintFunction;
import edu.uci.ics.jung.utils.Pair;
import edu.uci.ics.jung.visualization.PickedState;

public class MyEdgePaintFunction implements EdgePaintFunction {

    protected PickedState ps;

    Color dotted = Color.LIGHT_GRAY.brighter();

    Color dotted_black = Color.BLACK.brighter();

    NetworkSettings settings = NetworkSettingsSingelton.getInstance();

    protected boolean graphTheory = false;

    private Pathway pw;

    public MyEdgePaintFunction(PickedState ps, Pathway pw) {
        this.ps = ps;
        this.pw = pw;
    }

    public Paint getDrawPaint(Edge e) {
        if (e.containsUserDatumKey("alignment")) {
            return getFillPaint(e);
        }
        if (!graphTheory) return getDrawPaintWithoutGraphTheory(e); else return getDrawPaintWithGraphTheory(e);
    }

    public Paint getFillPaint(Edge e) {
        if (e.containsUserDatumKey("alignment")) {
            double val = ((Double) e.getUserDatum("alignment")).doubleValue();
            int r = (int) (255 * val);
            int b = (int) (255 * (1 - val));
            return new Color(r, 0, b);
        } else return null;
    }

    public Paint getDrawPaintWithGraphTheory(Edge e) {
        Pair p = e.getEndpoints();
        Vertex a = (Vertex) p.getFirst();
        Vertex b = (Vertex) p.getSecond();
        if (ps.isPicked(a) == true && ps.isPicked(b) == true) {
            return Color.BLUE;
        } else {
            if (settings.isBackgroundColor()) return dotted_black; else return dotted;
        }
    }

    public Paint getDrawPaintWithoutGraphTheory(Edge e) {
        boolean basic_check = false;
        if (ps.getPickedVertices().isEmpty()) {
            if (ps.getPickedEdges().isEmpty()) {
                Pair endPoints = e.getEndpoints();
                Vertex a = (Vertex) endPoints.getFirst();
                Vertex b = (Vertex) endPoints.getSecond();
                if (((GraphElementAbstract) pw.getElement(a)).isHidden() || ((GraphElementAbstract) pw.getElement(b)).isHidden()) {
                    if (settings.isBackgroundColor()) return dotted_black; else return dotted;
                } else {
                    return ((BiologicalEdgeAbstract) pw.getElement(e)).getColor();
                }
            } else {
                if (ps.isPicked(e)) return ((BiologicalEdgeAbstract) pw.getElement(e)).getColor(); else if (settings.isBackgroundColor()) return dotted_black; else return dotted;
            }
        } else {
            Pair endPoints = e.getEndpoints();
            Vertex a = (Vertex) endPoints.getFirst();
            Vertex b = (Vertex) endPoints.getSecond();
            Set set = ps.getPickedVertices();
            for (Iterator it = set.iterator(); it.hasNext(); ) {
                Vertex v = (Vertex) it.next();
                if (v == a || v == b) basic_check = true;
            }
            if (ps.isPicked(e)) basic_check = true;
            if (basic_check) return ((BiologicalEdgeAbstract) pw.getElement(e)).getColor(); else if (settings.isBackgroundColor()) return dotted_black; else return dotted;
        }
    }

    public void setGraphTheory(boolean graphTheory) {
        this.graphTheory = graphTheory;
    }
}
