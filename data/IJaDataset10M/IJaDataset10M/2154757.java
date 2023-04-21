package org.spantus.chart;

import java.awt.Point;
import java.math.BigDecimal;
import net.quies.math.plot.GraphDomain;
import net.quies.math.plot.GraphInstance;
import net.quies.math.plot.InteractiveGraph;
import net.quies.math.plot.ZoomSelection;

public class SpantusChartZoomSelection extends ZoomSelection {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    GraphDomain zoomDomain;

    SpantusChartZoomSelection(InteractiveGraph graph, Point start) {
        super(graph, start);
    }

    public boolean apply() {
        GraphInstance render = getGraph().getRender();
        if (render == null) return false;
        final BigDecimal A = render.getXValue(origin.x);
        final BigDecimal B = render.getXValue(current.x);
        final int compare = A.compareTo(B);
        if (compare == 0) return false;
        if (compare < 0) {
            setZoomDomain(new GraphDomain(A, B));
        } else {
            setZoomDomain(new GraphDomain(B, A));
        }
        return true;
    }

    protected GraphDomain getZoomDomain() {
        return zoomDomain;
    }

    protected void setZoomDomain(GraphDomain zoomDomain) {
        this.zoomDomain = zoomDomain;
    }
}
