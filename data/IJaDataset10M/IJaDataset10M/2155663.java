package com.rapitasystems.jgraphviz;

import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import com.rapitasystems.jgraphviz.editpart.GraphFactory;

public class GraphViewer extends ScrollingGraphicalViewer {

    private ZoomManager zoomManager;

    public GraphViewer() {
        setEditPartFactory(new GraphFactory());
        ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
        setRootEditPart(rootEditPart);
        zoomManager = rootEditPart.getZoomManager();
        zoomManager.setZoomLevels(new double[] { 0.01, 0.1, 0.2, 0.3, 0.4, 0.5, 1.0, 1.5 });
        zoomManager.setZoom(1);
    }

    public GraphViewer(Graph graph) {
        this();
        setContents(graph);
    }

    public ZoomManager getZoomManager() {
        return zoomManager;
    }
}
