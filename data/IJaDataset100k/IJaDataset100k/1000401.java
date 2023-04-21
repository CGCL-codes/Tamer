package com.nokia.ats4.appmodel.grapheditor.swing;

import com.nokia.ats4.appmodel.event.EventListener;
import com.nokia.ats4.appmodel.event.EventQueue;
import com.nokia.ats4.appmodel.grapheditor.GraphEditor;
import com.nokia.ats4.appmodel.grapheditor.event.CopyCellsEvent;
import com.nokia.ats4.appmodel.grapheditor.event.CutCellsEvent;
import com.nokia.ats4.appmodel.grapheditor.event.GraphCellDoubleClickedEvent;
import com.nokia.ats4.appmodel.grapheditor.event.GraphEditorEvent;
import com.nokia.ats4.appmodel.grapheditor.event.EditorBackgroundDoubleClickedEvent;
import com.nokia.ats4.appmodel.grapheditor.event.ConnectCellsEvent;
import com.nokia.ats4.appmodel.grapheditor.event.PasteCellsEvent;
import com.nokia.ats4.appmodel.grapheditor.event.SelectCellEvent;
import com.nokia.ats4.appmodel.grapheditor.event.DeleteCellEvent;
import com.nokia.ats4.appmodel.grapheditor.event.GraphZoomEvent;
import com.nokia.ats4.appmodel.grapheditor.event.InsertImageFromToolEvent;
import com.nokia.ats4.appmodel.grapheditor.event.ReconnectCellsEvent;
import com.nokia.ats4.appmodel.grapheditor.event.ShowPopupEvent;
import com.nokia.ats4.appmodel.main.event.DeleteChildModelEvent;
import com.nokia.ats4.appmodel.main.event.OpenModelEvent;
import com.nokia.ats4.appmodel.model.KendoModel;
import com.nokia.ats4.appmodel.model.domain.ApplicationModelState;
import com.nokia.ats4.appmodel.model.domain.CompositeState;
import com.nokia.ats4.appmodel.model.domain.ExitPoint;
import com.nokia.ats4.appmodel.model.domain.Port;
import com.nokia.ats4.appmodel.model.domain.StartState;
import com.nokia.ats4.appmodel.model.domain.State;
import com.nokia.ats4.appmodel.model.domain.SubModelState;
import com.nokia.ats4.appmodel.model.domain.SystemState;
import com.nokia.ats4.appmodel.model.domain.Transition;
import com.nokia.ats4.appmodel.util.Settings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.ToolTipManager;
import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphSelectionModel;
import org.jgraph.graph.PortView;

/**
 * Implements a graph editor based on JGraph. Extends the functionality of
 * JGraph by adding support for Kendo specific functionality such as inserting
 * new edges with the mouse. Client of this class do not have to be concerned
 * about low-level mouse events as this class converts low-level events to
 * appropriate higher-level action events.
 *
 * More intelligent graph editing functionality that modifies the underlying
 * model should be implemented in Kendo specific classes since the interface of
 * JGraph is too bloated, making it difficult to see the forest for the trees.
 *
 * @author Timo Sillanp&auml;&auml;
 * @version $Revision: 44 $
 */
public class JGraphEditor extends JGraph {

    public static final int BUFFER_SIZE = 250;

    /**Indicates UI if there is mouse exit on port */
    private DefaultPort lastPort = null;

    /**Indicates UI if there is mouse exit on preview */
    private AbstractKendoStateView lastPreview = null;

    /** Flag that indicates if the mouse popup menu is enabled or disabled */
    private boolean transitionCreated = false;

    /** List of registered GraphEditorEvent listeners */
    private List<EventListener<GraphEditorEvent>> listeners = new ArrayList<EventListener<GraphEditorEvent>>();

    private boolean isControlDown = false;

    /**
     * Creates a new instance of JGraphEditor
     */
    public JGraphEditor() {
        super(new DefaultGraphModel());
        setConnectable(true);
        setDragEnabled(false);
        setGridEnabled(true);
        setGridVisible(true);
        int grid = 10;
        int tolerance = 2;
        try {
            grid = Settings.getIntegerProperty("graph.gridSize");
            tolerance = Settings.getIntegerProperty("graph.portTolerance");
        } catch (Exception e) {
        }
        setGridSize(grid);
        setTolerance(tolerance);
        setGridColor(Color.GRAY);
        setJumpToDefaultPort(true);
        setMarqueeHandler(new MarqueeHandler());
        setAntiAliased(true);
        GraphMouseListener ml = new GraphMouseListener();
        addMouseListener(ml);
        addMouseWheelListener(ml);
        addKeyListener(new GraphKeyboardListener());
        setHighlightColor(new Color(255, 255, 0, 200));
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    /**
     * Add a listener to catch change events from the graph.
     */
    public void addGraphChangeEventListener(EventListener<GraphEditorEvent> listener) {
        listeners.add(listener);
    }

    /**
     * This fetches the tooltip text from cell in graph and displays it in tooltip
     */
    @Override
    public String getToolTipText(MouseEvent event) {
        String toolTipText = null;
        Object cell = getFirstCellForLocation(event.getX(), event.getY());
        if (cell instanceof KendoGraphCell) {
            toolTipText = ((KendoGraphCell) cell).getToolTipString();
        } else if (cell instanceof KendoEdge) {
            toolTipText = ((KendoEdge) cell).getToolTipString();
        }
        return toolTipText;
    }

    public void setLastPort(DefaultPort port) {
        this.lastPort = port;
    }

    public DefaultPort getLastPort() {
        return this.lastPort;
    }

    public void setLastPreview(AbstractKendoStateView view) {
        this.lastPreview = view;
    }

    public AbstractKendoStateView getLastPreview() {
        return this.lastPreview;
    }

    /**
     * Dispatches the given event to all registered GraphEditorEvent listeners.
     *
     * @param event GraphEditorEvent to dispatch
     */
    private void notifyListeners(GraphEditorEvent event) {
        for (EventListener<GraphEditorEvent> listener : listeners) {
            listener.processEvent(event);
        }
    }

    /**
     * Overrides the default implementation by adding a graph model listener to
     * given model before passing it to super class.
     */
    @Override
    public void setModel(GraphModel model) {
        if (model != null) {
            model.addGraphModelListener(new JGraphModelListener());
        }
        super.setModel(model);
    }

    public JGraph getThis() {
        return this;
    }

    public synchronized void edit(Map changes) {
        this.getModel().edit(changes, null, null, null);
    }

    /**
     * Sets the graph editor mode, i.e. enables or disables certain functions
     * according to given mode.
     *
     * @param mode GraphEditor.Mode to set, or null for default (MODEL_DESIGN)
     */
    public void setEditorMode(GraphEditor.Mode mode) {
        switch(mode) {
            case TESTER:
            case TEST_DESIGN:
                this.setEditable(true);
                this.setMoveable(true);
                this.setPortsVisible(false);
                this.setGridVisible(false);
                this.setConnectable(false);
                getSelectionModel().setSelectionMode(GraphSelectionModel.MULTIPLE_GRAPH_SELECTION);
                break;
            default:
                this.setEditable(true);
                this.setMoveable(true);
                this.setPortsVisible(true);
                this.setGridVisible(true);
                this.setConnectable(true);
                getSelectionModel().setSelectionMode(GraphSelectionModel.MULTIPLE_GRAPH_SELECTION);
                break;
        }
    }

    /**
     * Adjusts the given Point to conform the current grid. In other words,
     * the method "snaps" the given point to the closest grid point.
     *
     * @param point Point to adjust to grid
     * @return Point object with adjusted x and y coordinates.
     */
    public Point2D adjustToGrid(Point2D point) {
        Point2D fs = fromScreen(point);
        int x = (int) Math.round(fs.getX());
        int y = (int) Math.round(fs.getY());
        double size = getGridSize();
        double dx = Math.round(x / size) * size;
        double dy = Math.round(y / size) * size;
        return toScreen(new Point2D.Double(dx, dy));
    }

    /**
     * A marquee handler that manages cell connecting.
     */
    class MarqueeHandler extends BasicMarqueeHandler {

        private boolean wasRepainted = false;

        private PortView sourcePort = null;

        private PortView targetPort = null;

        private Point2D sourcePoint = null;

        private Point2D targetPoint = null;

        @SuppressWarnings(value = { "unchecked" })
        @Override
        public boolean isForceMarqueeEvent(MouseEvent mouseEvent) {
            Point point = mouseEvent.getPoint();
            DefaultPort portAt = (DefaultPort) getPortForLocation(point.getX(), point.getY());
            if (portAt != null) {
                if (portAt.getUserObject() instanceof Port) {
                    Port p = (Port) portAt.getUserObject();
                    if (p.getType() != Port.PortType.EXIT && p.getType() != Port.PortType.IN_GATE_EXIT && p.getType() != Port.PortType.OUT_GATE_EXIT) {
                        return false;
                    }
                }
            }
            Object obj = getSelectionCell();
            if (obj instanceof DefaultEdge) {
                DefaultEdge edge = (DefaultEdge) obj;
                Object source = edge.getTarget();
                Object target = edge.getSource();
                if (source == portAt || target == portAt) {
                    return false;
                }
            }
            DefaultGraphCell c = (DefaultGraphCell) getFirstCellForLocation(point.getX(), point.getY());
            if (c != null && isEditable() && c.getUserObject() instanceof SystemState && mouseEvent.getButton() != MouseEvent.BUTTON3) {
                Rectangle2D rect = GraphConstants.getBounds(c.getAttributes());
                double y = toScreen(rect.getBounds2D()).getY();
                double height = 15 * getScale();
                if (y + height < point.getY()) {
                    List<DefaultPort> p = new ArrayList<DefaultPort>();
                    p.addAll((List<DefaultPort>) c.getChildren());
                    for (int i = 0; i < p.size(); i++) {
                        Port.PortType pType = ((Port) p.get(i).getUserObject()).getType();
                        if (pType == Port.PortType.EXIT || pType == Port.PortType.IN_GATE_EXIT || pType == Port.PortType.OUT_GATE_EXIT) {
                            sourcePort = (PortView) getGraphLayoutCache().getMapping(p.get(i), false);
                            return true;
                        }
                    }
                }
            }
            PortView port = getSourcePortAt(point);
            if (port != null && isPortsVisible()) {
                sourcePort = port;
                return true;
            }
            return super.isForceMarqueeEvent(mouseEvent);
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            Object cell = null;
            if (mouseEvent != null && mouseEvent.getPoint() != null) {
                cell = getSelectionCellAt(mouseEvent.getPoint());
            }
            if (mouseEvent.getButton() == MouseEvent.BUTTON1 && sourcePort != null && isPortsVisible()) {
                sourcePoint = toScreen(sourcePort.getLocation());
            } else if (mouseEvent.getButton() != MouseEvent.BUTTON3 || cell == null) {
                super.mousePressed(mouseEvent);
            }
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            GraphEditorEvent evt = null;
            if (sourcePort == null && mouseEvent.getClickCount() == 2) {
                Point2D adjusted = adjustToGrid(mouseEvent.getPoint());
                Point2D p = fromScreen(adjusted);
                evt = new EditorBackgroundDoubleClickedEvent(JGraphEditor.this, (int) p.getX(), (int) p.getY());
            } else if (sourcePort != null && targetPort != null && !sourcePort.equals(targetPort)) {
                evt = new ConnectCellsEvent(JGraphEditor.this, sourcePort.getCell(), targetPort.getCell());
                sourcePort = targetPort = null;
                sourcePoint = targetPoint = null;
                transitionCreated = true;
            } else {
                sourcePort = targetPort = null;
                sourcePoint = targetPoint = null;
            }
            if (evt != null) {
                notifyListeners(evt);
            }
            super.mouseReleased(mouseEvent);
        }

        @SuppressWarnings(value = { "unchecked" })
        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            boolean clear = true;
            if (sourcePort != null) {
                Point point = mouseEvent.getPoint();
                PortView newTargetPort = getTargetPortAt(point);
                DefaultGraphCell c = (DefaultGraphCell) getFirstCellForLocation(point.getX(), point.getY());
                if (newTargetPort != null && c != null && (c.getUserObject() instanceof SubModelState || c.getUserObject() instanceof ApplicationModelState)) {
                    if (newTargetPort instanceof KendoAbstractPortView) {
                        Port port = (Port) ((DefaultPort) newTargetPort.getCell()).getUserObject();
                        State target = port.getImmediateTargetState();
                        if (lastPort != (DefaultPort) newTargetPort.getCell() && !(target instanceof StartState)) {
                            if (!(target instanceof ExitPoint)) {
                                paintTooltip(newTargetPort);
                            }
                            clear = false;
                        } else {
                            clear = false;
                        }
                    }
                }
                if (c != null && isEditable() && c.getUserObject() instanceof State && !(c.getUserObject() instanceof CompositeState)) {
                    List<DefaultPort> p = new ArrayList<DefaultPort>();
                    p.addAll((List<DefaultPort>) c.getChildren());
                    for (int i = 0; i < p.size(); i++) {
                        Port.PortType pType = ((Port) p.get(i).getUserObject()).getType();
                        if (pType == Port.PortType.ENTRY || pType == Port.PortType.IN_GATE_ENTRY || pType == Port.PortType.OUT_GATE_ENTRY) {
                            newTargetPort = (PortView) getGraphLayoutCache().getMapping(p.get(i), false);
                            break;
                        }
                    }
                }
                Graphics g = getGraphics();
                Point2D newTargetPoint = (newTargetPort != null) ? toScreen(newTargetPort.getLocation()) : point;
                if (lastPort != null && clear) {
                    PortView cv = (PortView) graphLayoutCache.getMapping(lastPort, false);
                    ((KendoAbstractPortView) cv).setToolTip(false);
                    repaint();
                    targetPoint = null;
                    lastPort = null;
                    clear = false;
                } else {
                    if (newTargetPoint != targetPoint && !wasRepainted) {
                        paintConnector(Color.black, getBackground(), g);
                        targetPoint = newTargetPoint;
                        targetPort = newTargetPort;
                        if (!sourcePort.equals(targetPort)) {
                            paintConnector(getBackground(), Color.black, g);
                        }
                    }
                }
            } else {
                super.mouseDragged(mouseEvent);
            }
            wasRepainted = false;
        }

        private void paintConnector(Color fg, Color bg, Graphics g) {
            g.setColor(fg);
            g.setXORMode(bg);
            if (sourcePoint != null && targetPoint != null) {
                g.drawLine((int) sourcePoint.getX(), (int) sourcePoint.getY(), (int) targetPoint.getX(), (int) targetPoint.getY());
            }
        }

        void paintTooltip(PortView newTargetPort) {
            if (lastPort != null) {
                PortView cv = (PortView) graphLayoutCache.getMapping(lastPort, false);
                ((KendoAbstractPortView) cv).setToolTip(false);
                repaint();
                targetPoint = null;
                wasRepainted = true;
            }
            lastPort = (DefaultPort) newTargetPort.getCell();
            ((KendoAbstractPortView) newTargetPort).setToolTip(true);
            boolean o = GraphConstants.getOffset(newTargetPort.getAllAttributes()) != null;
            Rectangle2D r = o ? newTargetPort.getBounds() : newTargetPort.getParentView().getBounds();
            Graphics g = getGraphics();
            r = toScreen((Rectangle2D) r.clone());
            getUI().paintCell(g, newTargetPort, r, false);
        }

        void paintPort(Graphics g) {
            if (targetPort != null) {
                CellView sourceParent = sourcePort.getParentView();
                CellView targetParent = targetPort.getParentView();
                if (sourceParent != targetParent) {
                    boolean o = GraphConstants.getOffset(targetPort.getAllAttributes()) != null;
                    Rectangle2D r = o ? targetPort.getBounds() : targetPort.getParentView().getBounds();
                    r = toScreen((Rectangle2D) r.clone());
                    r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r.getHeight() + 6);
                    getUI().paintCell(g, targetPort, r, true);
                }
            } else if (sourcePort != null) {
                boolean o = GraphConstants.getOffset(sourcePort.getAllAttributes()) != null;
                Rectangle2D r = o ? sourcePort.getBounds() : sourcePort.getParentView().getBounds();
                r = toScreen((Rectangle2D) r.clone());
                r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r.getHeight() + 6);
                getUI().paintCell(g, sourcePort, r, true);
            }
        }

        private PortView getSourcePortAt(Point2D point) {
            setJumpToDefaultPort(false);
            try {
                return getPortViewAt(point.getX(), point.getY());
            } finally {
                setJumpToDefaultPort(true);
            }
        }

        private PortView getTargetPortAt(Point2D point) {
            return getPortViewAt(point.getX(), point.getY());
        }
    }

    /**
     * GraphMouseListener listens for MouseEvents in the JGraph component.
     */
    private class GraphMouseListener extends MouseAdapter {

        /** Handles the cell selection and background double-clicking */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1 && e.getButton() != MouseEvent.BUTTON3 && !isControlDown) {
                Object cell = getSelectionCellAt(e.getPoint());
                notifyListeners(new SelectCellEvent(JGraphEditor.this, cell));
            } else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                Object cell = getFirstCellForLocation(e.getX(), e.getY());
                if (cell != null && cell instanceof KendoGraphCell) {
                    DefaultGraphCell graphCell = (DefaultGraphCell) cell;
                    if (graphCell.getUserObject() instanceof SystemState) {
                        SystemState userObject = (SystemState) graphCell.getUserObject();
                        EventQueue.postEvent(new InsertImageFromToolEvent(e.getSource(), userObject));
                    } else {
                        notifyListeners(new GraphCellDoubleClickedEvent(JGraphEditor.this, getModel().getValue(cell)));
                    }
                }
            }
        }

        /** Sets the selection enabled or disabled depending on pressed button */
        @Override
        public void mousePressed(MouseEvent e) {
            Object cell = getSelectionCellAt(e.getPoint());
            if (cell != null && e.getButton() == MouseEvent.BUTTON3 && getSelectionCount() > 1) {
                setSelectionEnabled(false);
            } else {
                setSelectionEnabled(true);
            }
        }

        /** Handles the displaying of the graph pop-up menu */
        @Override
        public void mouseReleased(MouseEvent e) {
            repaint();
            if (transitionCreated) {
                transitionCreated = false;
                clearSelection();
                return;
            }
            if (lastPort != null) {
                CellView cv = graphLayoutCache.getMapping(lastPort, false);
                ((KendoAbstractPortView) cv).setToolTip(false);
                graphLayoutCache.cellViewsChanged(new CellView[] { cv });
                lastPort = null;
            }
            if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
                Object[] cells = getSelectionCells();
                if (cells.length <= 1) {
                    if (getSelectionCellAt(e.getPoint()) != null) {
                        cells = new Object[] { getSelectionCellAt(e.getPoint()) };
                    } else {
                        cells = new Object[0];
                    }
                } else {
                    setSelectionCells(cells);
                    if (cells.length > 0) {
                        EventQueue.dispatchEvent(new SelectCellEvent(this, cells[0]));
                    }
                }
                ShowPopupEvent event = new ShowPopupEvent(JGraphEditor.this, e.getComponent(), e.getX(), e.getY(), cells);
                notifyListeners(event);
            }
            Object cell = getSelectionCellAt(e.getPoint());
            if (cell != null && getSelectionCount() == 1 && !isControlDown) {
                notifyListeners(new SelectCellEvent(JGraphEditor.this, cell));
            } else if (isControlDown) {
                Object[] selections = getSelectionCells();
                List<Object> temp = new ArrayList<Object>(Arrays.asList(selections));
                if (temp.contains(cell) && cell == null) {
                    temp.remove(cell);
                } else {
                    temp.add(cell);
                }
                EventQueue.dispatchEvent(new SelectCellEvent(JGraphEditor.this, getSelectionCell()));
                setSelectionCells(temp.toArray());
            } else {
                EventQueue.dispatchEvent(new SelectCellEvent(this, getSelectionCell()));
            }
            setSelectionEnabled(true);
        }

        /** Handles the zooming and scrolling of the graph when mouse wheel is rolled */
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int rotation = e.getWheelRotation();
            if (rotation > 0 && e.isControlDown()) {
                EventQueue.postEvent(new GraphZoomEvent(JGraphEditor.this, e.getPoint(), GraphZoomEvent.ZOOM_IN));
            } else if (rotation < 0 && e.isControlDown()) {
                EventQueue.postEvent(new GraphZoomEvent(JGraphEditor.this, e.getPoint(), GraphZoomEvent.ZOOM_OUT));
            }
            if (rotation > 0) {
                Point2D p = getThis().getCenterPoint();
                p.setLocation(p.getX(), getThis().getSize().getHeight() / 2 + p.getY());
                getThis().scrollPointToVisible(p);
            } else if (rotation < 0) {
                Point2D p = getThis().getCenterPoint();
                p.setLocation(p.getX(), p.getY() - getThis().getSize().getHeight() / 2);
                getThis().scrollPointToVisible(p);
            }
        }
    }

    /**
     * Listens for keyboard events in graph component.
     */
    private class GraphKeyboardListener extends KeyAdapter {

        private int pasteX = 150;

        private int pasteY = 150;

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                if (isControlDown != true) {
                    isControlDown = true;
                }
            } else {
                e.consume();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            Object[] cells = getSelectionCells();
            isControlDown = false;
            switch(e.getKeyCode()) {
                case KeyEvent.VK_DELETE:
                    for (Object o : cells) {
                        DefaultGraphCell cell = (DefaultGraphCell) o;
                        if (cell.getUserObject() instanceof CompositeState) {
                            CompositeState s = (CompositeState) cell.getUserObject();
                            KendoModel m = s.getLinkedModel().getContainingModel();
                            EventQueue.postEvent(new DeleteChildModelEvent(JGraphEditor.this, m));
                        } else {
                            notifyListeners(new DeleteCellEvent(JGraphEditor.this, o));
                        }
                    }
                    break;
                case KeyEvent.VK_C:
                    if (e.isControlDown()) {
                        this.pasteX = 150;
                        this.pasteY = 150;
                        if (cells.length < 1) {
                            cells = new Object[1];
                            cells[0] = getSelectionCell();
                        }
                        EventQueue.dispatchEvent(new CopyCellsEvent(JGraphEditor.this, cells));
                    }
                    break;
                case KeyEvent.VK_V:
                    if (e.isControlDown()) {
                        PasteCellsEvent pasteEvent = new PasteCellsEvent(JGraphEditor.this, this.pasteX, this.pasteY);
                        this.pasteX += 11;
                        this.pasteY += 11;
                        EventQueue.dispatchEvent(pasteEvent);
                    }
                    break;
                case KeyEvent.VK_X:
                    this.pasteX = 150;
                    this.pasteY = 150;
                    if (e.isControlDown()) {
                        if (cells.length < 1) {
                            cells = new Object[1];
                            cells[0] = getSelectionCell();
                        }
                        EventQueue.dispatchEvent(new CutCellsEvent(JGraphEditor.this, cells));
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (getSelectionCells().length == 1) {
                        Object selected = getSelectionCells()[0];
                        if (getModel().isEdge(selected) && ((DefaultEdge) selected).getUserObject() instanceof Transition) {
                            State s = ((Transition) ((DefaultEdge) selected).getUserObject()).getTargetState();
                            DefaultGraphCell cell = (DefaultGraphCell) s.getContainingModel().getContainingModel().getEditorModel().getCellByUserObject(s);
                            if (getModel().contains(cell)) {
                                EventQueue.postEvent(new SelectCellEvent(getThis(), cell));
                                scrollCellToVisible(cell);
                            } else {
                                EventQueue.postEvent(new OpenModelEvent(getThis(), s.getContainingModel().getContainingModel()));
                                EventQueue.postEvent(new SelectCellEvent(getThis(), cell));
                            }
                        } else if (((DefaultGraphCell) selected).getUserObject() instanceof State) {
                            List<DefaultEdge> children = getChilds((DefaultGraphCell) selected, false);
                            if (children.size() > 0) {
                                EventQueue.postEvent(new SelectCellEvent(getThis(), children.get(0)));
                            }
                        }
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (getSelectionCells().length == 1) {
                        Object selected = getSelectionCells()[0];
                        if (getModel().isEdge(selected) && ((DefaultEdge) selected).getUserObject() instanceof Transition) {
                            State s = ((Transition) ((DefaultEdge) selected).getUserObject()).getSourceState();
                            DefaultGraphCell cell = (DefaultGraphCell) s.getContainingModel().getContainingModel().getEditorModel().getCellByUserObject(s);
                            if (getModel().contains(cell)) {
                                EventQueue.postEvent(new SelectCellEvent(getThis(), cell));
                                scrollCellToVisible(cell);
                            } else {
                                EventQueue.postEvent(new OpenModelEvent(getThis(), s.getContainingModel().getContainingModel()));
                                EventQueue.postEvent(new SelectCellEvent(getThis(), cell));
                            }
                        } else if (((DefaultGraphCell) selected).getUserObject() instanceof State) {
                            List<DefaultEdge> children = getChilds((DefaultGraphCell) selected, true);
                            if (children.size() > 0) {
                                EventQueue.postEvent(new SelectCellEvent(getThis(), children.get(0)));
                            }
                        }
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (getSelectionCells().length == 1) {
                        Object selected = getSelectionCells()[0];
                        if (getModel().isEdge(selected)) {
                            List<DefaultEdge> children = getChilds((DefaultGraphCell) selected, false);
                            for (DefaultEdge edge : children) {
                                if (edge == ((DefaultEdge) selected)) {
                                    if (children.indexOf(edge) < children.size() - 1) {
                                        EventQueue.postEvent(new SelectCellEvent(getThis(), children.get(children.indexOf(edge) + 1)));
                                    } else {
                                        EventQueue.postEvent(new SelectCellEvent(getThis(), children.get(0)));
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (getSelectionCells().length == 1) {
                        Object selected = getSelectionCells()[0];
                        if (getModel().isEdge(selected)) {
                            List<DefaultEdge> children = getChilds((DefaultGraphCell) selected, false);
                            for (DefaultEdge edge : children) {
                                if (edge == ((DefaultEdge) selected)) {
                                    if (children.indexOf(edge) > 0) {
                                        EventQueue.postEvent(new SelectCellEvent(getThis(), children.get(children.indexOf(edge) - 1)));
                                    } else {
                                        EventQueue.postEvent(new SelectCellEvent(getThis(), children.get(children.size() - 1)));
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case KeyEvent.VK_ALT:
                case KeyEvent.VK_SHIFT:
                case KeyEvent.VK_CONTROL:
                    break;
                default:
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    e.consume();
            }
        }

        List<DefaultEdge> getChilds(DefaultGraphCell parent, boolean useIncomming) {
            State realParent = null;
            if (parent.getUserObject() instanceof Transition) {
                realParent = ((Transition) parent.getUserObject()).getSourceState();
            } else if (parent.getUserObject() instanceof State) {
                realParent = (State) parent.getUserObject();
            }
            List<Transition> childs = realParent.getOutgoingTransitions();
            if (useIncomming) {
                childs = realParent.getIncomingTransitions();
            }
            List<DefaultEdge> edges = new ArrayList<DefaultEdge>();
            for (int i = 0; i < childs.size(); i++) {
                for (int j = 0; j < graphModel.getRootCount(); j++) {
                    DefaultGraphCell cell = (DefaultGraphCell) graphModel.getRootAt(j);
                    if (cell.getUserObject() == childs.get(i)) {
                        boolean wasBigger = false;
                        for (int k = 0; k < edges.size(); k++) {
                            DefaultGraphCell temptarget = (DefaultGraphCell) getModel().getParent(getModel().getTarget(edges.get(k)));
                            DefaultGraphCell activeTarget = (DefaultGraphCell) getModel().getParent(getModel().getTarget((DefaultEdge) cell));
                            Rectangle2D temp = GraphConstants.getBounds(temptarget.getAttributes());
                            Rectangle2D active = GraphConstants.getBounds(activeTarget.getAttributes());
                            if (active.getY() < temp.getY()) {
                                edges.add(k, (DefaultEdge) cell);
                                wasBigger = true;
                                break;
                            }
                        }
                        if (!wasBigger) {
                            edges.add((DefaultEdge) cell);
                        }
                        break;
                    }
                }
            }
            return edges;
        }
    }

    /**
     * Listener for GraphModel changes. Handles the re-connecting of cells
     * when an edge is dragged from a cell to another.
     */
    private class JGraphModelListener implements GraphModelListener {

        public void graphChanged(GraphModelEvent event) {
            GraphModel model = getModel();
            GraphModelEvent.GraphModelChange change = event.getChange();
            Object[] changed = change.getChanged();
            if (changed != null && model != null) {
                for (Object obj : changed) {
                    if (obj != null && model.isEdge(obj)) {
                        DefaultEdge edge = (DefaultEdge) obj;
                        ReconnectCellsEvent evt = new ReconnectCellsEvent(JGraphEditor.this, edge.getUserObject(), edge.getSource(), edge.getTarget());
                        notifyListeners(evt);
                    }
                }
            }
        }
    }

    public void updateUI() {
        setUI(new KendoGraphUI(this));
        invalidate();
    }

    void clearLastPort() {
        this.lastPort = null;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension defaultSize = super.getPreferredSize();
        return new Dimension((int) (defaultSize.getWidth() + BUFFER_SIZE * getScale()), (int) (defaultSize.getHeight() + BUFFER_SIZE * getScale()));
    }
}
