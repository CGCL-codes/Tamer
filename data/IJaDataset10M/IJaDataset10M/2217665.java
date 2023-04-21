package moleDecorator;

import java.awt.Color;
import java.awt.Paint;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.decorators.EdgePaintFunction;

public class MoleEdgePaint implements EdgePaintFunction {

    public static final String WEIGHT = "weight";

    public static final Color[] EDGE_COLORS = { new Color(50, 50, 50), new Color(255, 180, 0), new Color(0, 255, 0), new Color(255, 0, 0), new Color(10, 160, 170), new Color(0, 0, 255), new Color(220, 220, 220), new Color(0, 0, 0) };

    public Paint getFillPaint(Edge e) {
        return null;
    }

    public Paint getDrawPaint(Edge e) {
        String help = (String) e.getUserDatum(WEIGHT);
        int i = new Integer(help).intValue();
        switch(i) {
            case 0:
                return EDGE_COLORS[0];
            case 1:
                return EDGE_COLORS[1];
            case 2:
                return EDGE_COLORS[2];
            case 3:
                return EDGE_COLORS[3];
            case 4:
                return EDGE_COLORS[4];
            case 5:
                return EDGE_COLORS[5];
            case 6:
                return EDGE_COLORS[6];
            default:
                return EDGE_COLORS[7];
        }
    }
}
