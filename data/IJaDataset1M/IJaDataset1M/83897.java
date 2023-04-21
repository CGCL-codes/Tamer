package edu.upmc.opi.caBIG.caTIES.client.vr.FilterFlowGraph;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.util.JGraphUtilities;
import edu.upmc.opi.caBIG.caTIES.client.vr.FilterFlowGraph.popupactions.AddTemporalAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.FilterFlowGraph.popupactions.AddVertexAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.FilterFlowGraph.popupactions.DeleteGroupAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.FilterFlowGraph.popupactions.DeleteVertexAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.FilterFlowGraph.popupactions.ModifyTemporalEdgeAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.FilterFlowGraph.popupactions.ModifyVertexAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.constraints.FieldConstraint;
import edu.upmc.opi.caBIG.caTIES.client.vr.constraints.TemporalConstraint;

/**
 * Handles mouse events on the graph.
 * 
 */
public class FilterMarqueeHandler extends BasicMarqueeHandler {

    /**
     * Field graph.
     */
    FilterFlowGraph graph;

    /**
     * The Constructor.
     * 
     * @param graph the graph
     */
    public FilterMarqueeHandler(FilterFlowGraph graph) {
        super();
        this.graph = graph;
    }

    /**
     * Method isForceMarqueeEvent.
     * 
     * @param e MouseEvent
     * 
     * @return boolean
     */
    public boolean isForceMarqueeEvent(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) return true; else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) return true; else return super.isForceMarqueeEvent(e);
    }

    /**
     * Method mousePressed.
     * 
     * @param e MouseEvent
     */
    public void mousePressed(final MouseEvent e) {
        Point p = (Point) graph.fromScreen(e.getPoint());
        Object c = graph.getFirstCellForLocation(p.getX(), p.getY());
        if (JGraphUtilities.isGroup(graph, c)) {
            c = graph.getNextCellForLocation(c, p.getX(), p.getY());
        }
        if (c == null) c = graph.getFirstCellForLocation(p.getX(), p.getY());
        graph.setSelectionCell(c);
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
            if (c == null) {
                super.mousePressed(e);
            } else if (JGraphUtilities.isGroup(graph, c)) {
                super.mousePressed(e);
            } else if (JGraphUtilities.isVertex(graph, c)) {
                new ModifyVertexAction(graph, (DefaultGraphCell) c, p.getX(), p.getY()).actionPerformed(null);
            } else if (graph.getModel().isEdge(c)) {
                if (((DefaultEdge) c).getUserObject() instanceof TemporalConstraint) {
                    Rectangle b = (Rectangle) graph.getGraphLayoutCache().getMapping(c, false).getBounds();
                    int x = (int) (b.getX() + b.getWidth() / 2);
                    int y = (int) (b.getY() + b.getHeight() / 2);
                    new ModifyTemporalEdgeAction(graph, (DefaultEdge) c, x, y).actionPerformed(null);
                } else {
                    final int ADD_ACTION = 0;
                    new AddVertexAction(ADD_ACTION, graph, (DefaultEdge) c).actionPerformed(null);
                }
            } else super.mousePressed(e);
        } else if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
            if (c == null) {
                super.mousePressed(e);
            } else if (graph.getSelectionCount() > 1) {
            } else if (JGraphUtilities.isGroup(graph, c)) {
                JPopupMenu popup = getPopupForGroup(graph, null, c);
                popup.show(graph, e.getX(), e.getY());
            } else if (graph.getModel().isEdge(c)) {
                if (((DefaultEdge) c).getUserObject() instanceof TemporalConstraint) {
                    JPopupMenu popup = getPopupForTemporalEdge(graph, (DefaultEdge) c, e.getX(), e.getY());
                    popup.show(graph, e.getX(), e.getY());
                } else {
                    JPopupMenu popup = getPopupForEdge(graph, (DefaultEdge) c);
                    popup.show(graph, e.getX(), e.getY());
                }
            } else if (JGraphUtilities.isVertex(graph, c)) {
                JPopupMenu popup = getPopupForVertex(graph, (DefaultGraphCell) c, e.getX(), e.getY());
                if (popup != null) popup.show(graph, e.getX(), e.getY());
            } else super.mousePressed(e);
        } else super.mousePressed(e);
    }

    /**
     * Method mouseReleased.
     * 
     * @param e MouseEvent
     */
    public void mouseReleased(MouseEvent e) {
        e.consume();
        super.mouseReleased(e);
    }

    /**
     * Gets the popup for group.
     * 
     * @param graph the graph
     * @param groupcell the groupcell
     * @param cells the cells
     * 
     * @return the popup for group
     */
    private JPopupMenu getPopupForGroup(FilterFlowGraph graph, Object[] cells, Object groupcell) {
        JPopupMenu popup = new JPopupMenu();
        popup.add(new AddTemporalAction(graph, groupcell));
        popup.add(new DeleteGroupAction(graph, groupcell, graph.getStartNodes().size() > 1));
        return popup;
    }

    /**
     * Method getPopupForEdge.
     * 
     * @param edge DefaultEdge
     * @param graph FilterFlowGraph
     * 
     * @return JPopupMenu
     */
    private JPopupMenu getPopupForEdge(FilterFlowGraph graph, DefaultEdge edge) {
        JPopupMenu popup = new JPopupMenu();
        popup.add(new AddVertexAction(AddVertexAction.ADD_ACTION, graph, edge));
        popup.add(new AddVertexAction(AddVertexAction.BRANCH_ADD_ACTION, graph, edge));
        return popup;
    }

    /**
     * Method getPopupForTemporalEdge.
     * 
     * @param edge DefaultEdge
     * @param graph FilterFlowGraph
     * @param y int
     * @param x int
     * 
     * @return JPopupMenu
     */
    private JPopupMenu getPopupForTemporalEdge(FilterFlowGraph graph, DefaultEdge edge, int x, int y) {
        JPopupMenu popup = new JPopupMenu();
        popup.add(new ModifyTemporalEdgeAction(graph, edge, x, y));
        return popup;
    }

    /**
     * Method getPopupForVertex.
     * 
     * @param cell DefaultGraphCell
     * @param graph FilterFlowGraph
     * @param y int
     * @param x int
     * 
     * @return JPopupMenu
     */
    private JPopupMenu getPopupForVertex(FilterFlowGraph graph, DefaultGraphCell cell, int x, int y) {
        JPopupMenu popup = new JPopupMenu();
        boolean above = false;
        boolean below = true;
        if (graph.getStartNodes().contains(cell)) {
            above = false;
        } else if (graph.getFinishNodes().contains(cell)) {
            below = false;
            above = true;
        } else if (cell.getUserObject() instanceof FieldConstraint) {
            popup.add(new ModifyVertexAction(graph, cell, x, y));
            popup.add(new DeleteVertexAction(graph));
            popup.add(new AddVertexAction(AddVertexAction.ADD_BESIDE_ACTION, graph, cell));
        }
        if (above) popup.add(new AddVertexAction(AddVertexAction.ADD_ABOVE_ACTION, graph, cell));
        if (below) popup.add(new AddVertexAction(AddVertexAction.ADD_BELOW_ACTION, graph, cell));
        return popup;
    }
}
