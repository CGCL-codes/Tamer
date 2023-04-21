package play.higraph.view;

import higraph.view.HigraphView;
import java.awt.BasicStroke;
import java.awt.Color;
import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class PlaceHolderNodeView extends PLAYNodeView {

    /**
     * @param v
     * @param node
     * @param timeMan
     */
    protected PlaceHolderNodeView(HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v, PLAYNode node, BTTimeManager timeMan) {
        super(v, node, timeMan);
        super.strokeVar.set(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 2.0f }, 0.0f));
        super.setNodeSize(100, 30);
        super.setColor(Color.BLUE);
        super.setFillColor(null);
    }
}
