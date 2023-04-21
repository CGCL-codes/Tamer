package visreed.view;

import higraph.view.HigraphView;
import java.awt.Graphics2D;
import tm.backtrack.BTTimeManager;
import visreed.awt.ArrowDirection;
import visreed.awt.ArrowStyle;
import visreed.awt.GraphicsHelper;
import visreed.model.Direction;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.layout.AlternationLayoutManager;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * AlternationNodeView represents the alternation node.
 * @author Xiaoyu Guo
 */
public class AlternationNodeView extends VisreedNodeView {

    public AlternationNodeView(HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v, VisreedNode node, BTTimeManager timeMan) {
        super(v, node, timeMan);
    }

    @Override
    protected void drawNode(Graphics2D screen) {
        screen.setColor(this.getColor());
        for (int i = 0; i < this.getNumChildren(); i++) {
            VisreedNodeView child = this.getVisreedChild(i);
            if (child == null) {
                continue;
            }
            GraphicsHelper.drawHorizontalConnectionCurve(screen, this.getEntryPoint().getX(), this.getEntryPoint().getY(), child.getEntryPoint().getX(), child.getEntryPoint().getY());
            if (this.getCurrentDirection().equals(Direction.EAST)) {
                GraphicsHelper.drawArrow(screen, ArrowStyle.ARROW_AT_THE_BACK, ArrowDirection.EAST, child.getEntryPoint());
            } else if (this.getCurrentDirection().equals(Direction.WEST)) {
                GraphicsHelper.drawArrow(screen, ArrowStyle.ARROW_AT_THE_BACK, ArrowDirection.WEST, child.getExitPoint());
            }
            GraphicsHelper.drawHorizontalConnectionCurve(screen, this.getExitPoint().getX(), this.getExitPoint().getY(), child.getExitPoint().getX(), child.getExitPoint().getY());
        }
    }

    @Override
    protected VisreedNodeLayoutManager getLayoutHelper() {
        return AlternationLayoutManager.getInstance();
    }

    /**
     * Handling DropZones
     * Rules for id: 
     * <li>"head"</li>
     * <li>"node_1"</li>
     * <li>"node_2"</li>
     */
    public static final String ID_DROPZONE_HEAD = "head";

    public static final String ID_PREFIX_DROPZONE_NODE = "node_";

    @Override
    protected boolean shouldRefreshDropZone() {
        boolean shouldRefresh = (this.zones.size() != this.getNumChildren() + 1);
        shouldRefresh |= (this.findZone(ID_DROPZONE_HEAD) == null);
        if (shouldRefresh == false) {
            for (int i = 0; i < this.getNumChildren(); i++) {
                shouldRefresh |= (this.findZone(ID_PREFIX_DROPZONE_NODE + (i + 1)) == null);
            }
        }
        return shouldRefresh;
    }

    @Override
    public void reCreateDropZone() {
        this.removeZones();
        this.makeAndAddInsertChildDropZone(ID_DROPZONE_HEAD, 0);
        for (int i = 0; i < this.getNumChildren(); i++) {
            this.makeAndAddInsertChildDropZone(ID_PREFIX_DROPZONE_NODE + (i + 1), i + 1);
        }
    }

    @Override
    public void resetLayout() {
        super.resetLayout();
        for (int i = 0; i < this.getNumChildren(); i++) {
            VisreedNodeView child = this.getVisreedChild(i);
            child.resetLayout();
        }
    }
}
