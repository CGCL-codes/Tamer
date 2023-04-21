package org.peertrust.modeler.policysystem.gui.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.Page;
import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.Edge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;
import org.peertrust.modeler.policysystem.gui.views.GraphSelectionProvider;
import org.peertrust.modeler.policysystem.model.HierarchyNodeCreationMechanism;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import com.hp.hpl.jena.rdf.model.Resource;

public class PSHierarchyVisualizationPage extends Page implements GraphSelectionListener, KeyListener {

    static final String RESOURCE_BASE = "org.peertrust.modeler.policysystem/jgraph_resources/";

    protected JGraph graph;

    protected GraphUndoManager undoManager;

    protected Action undo, redo, remove, group, ungroup, tofront, toback, cut, copy, paste;

    protected int cellCount = 0;

    protected EdStatusBar statusBar;

    private Composite composite;

    private JPanel panel;

    private PolicySystemRDFModel psRDFModel;

    private HierarchyNodeCreationMechanism hierarchyNodeCreationMechanism;

    private GraphSelectionProvider graphSelectionProvider = new GraphSelectionProvider();

    public static void main(String[] args) {
    }

    public PSHierarchyVisualizationPage() {
        panel = new JPanel();
        psRDFModel = PolicySystemRDFModel.getInstance();
        hierarchyNodeCreationMechanism = PolicySystemRDFModel.resourceHierarchyCreationMechanism;
        graph = createGraph();
        graph.setMarqueeHandler(createMarqueeHandler());
        undoManager = new GraphUndoManager() {

            public void undoableEditHappened(UndoableEditEvent e) {
                super.undoableEditHappened(e);
                updateHistoryButtons();
            }
        };
        populateContentPane();
        installListeners(graph);
        graph.addGraphSelectionListener(graphSelectionProvider);
    }

    protected void populateContentPane() {
        panel.setLayout(new BorderLayout());
        panel.add(createToolBar(), BorderLayout.NORTH);
        panel.add(new JScrollPane(graph), BorderLayout.CENTER);
        panel.add(createStatusBar(), BorderLayout.SOUTH);
    }

    protected JGraph createGraph() {
        JGraph graph = new MyGraph(new MyModel());
        graph.getGraphLayoutCache().setFactory(new DefaultCellViewFactory() {

            protected EdgeView createEdgeView(Object cell) {
                return new EdgeView(cell) {

                    /**
					 * Returns a cell handle for the view.
					 */
                    public CellHandle getHandle(GraphContext context) {
                        return new MyEdgeHandle(this, context);
                    }
                };
            }
        });
        return graph;
    }

    protected void installListeners(JGraph graph) {
        graph.getModel().addUndoableEditListener(undoManager);
        graph.getSelectionModel().addGraphSelectionListener(this);
        graph.addKeyListener(this);
        graph.getModel().addGraphModelListener(statusBar);
    }

    protected void uninstallListeners(JGraph graph) {
        graph.getModel().removeUndoableEditListener(undoManager);
        graph.getSelectionModel().removeGraphSelectionListener(this);
        graph.removeKeyListener(this);
        graph.getModel().removeGraphModelListener(statusBar);
    }

    protected BasicMarqueeHandler createMarqueeHandler() {
        return new MyMarqueeHandler();
    }

    public void insert(Point2D point) {
        DefaultGraphCell vertex = createDefaultGraphCell();
        if (vertex == null) {
            return;
        }
        vertex.getAttributes().applyMap(createCellAttributes(point));
        graph.getGraphLayoutCache().insert(vertex);
    }

    public Map createCellAttributes(Point2D point) {
        Map map = new Hashtable();
        if (graph != null) {
            point = graph.snap((Point2D) point.clone());
        } else {
            point = (Point2D) point.clone();
        }
        GraphConstants.setBounds(map, new Rectangle2D.Double(point.getX(), point.getY(), 0, 0));
        GraphConstants.setResize(map, true);
        GraphConstants.setGradientColor(map, Color.blue);
        GraphConstants.setBorderColor(map, Color.black);
        GraphConstants.setBackground(map, Color.white);
        GraphConstants.setOpaque(map, true);
        return map;
    }

    protected DefaultGraphCell createDefaultGraphCell() {
        if (PolicySystemRDFModel.CLASS_RESOURCE == null) {
            return null;
        }
        String cellName = "Cell " + new Integer(cellCount++);
        DefaultGraphCell cell = new DefaultGraphCell(cellName);
        PSModelObject res = hierarchyNodeCreationMechanism.createNode(psRDFModel.getRdfModel(), cellName);
        cell.addPort(null, res);
        cell.setUserObject(res);
        return cell;
    }

    public void connect(Port source, Port target) {
        DefaultEdge edge = createDefaultEdge();
        if (graph.getModel().acceptsSource(edge, source) && graph.getModel().acceptsTarget(edge, target)) {
            edge.getAttributes().applyMap(createEdgeAttributes());
            PSModelObject sourceMOW = (PSModelObject) ((DefaultGraphCell) source).getUserObject();
            PSModelObject targetMOW = (PSModelObject) ((DefaultGraphCell) target).getUserObject();
            PSModelObject edgeStatement = hierarchyNodeCreationMechanism.createLink(PolicySystemRDFModel.getInstance().getRdfModel(), (Resource) sourceMOW.getModelObject(), (Resource) targetMOW.getModelObject());
            edge.setUserObject(edgeStatement);
            graph.getGraphLayoutCache().insertEdge(edge, source, target);
        }
    }

    protected DefaultEdge createDefaultEdge() {
        return new DefaultEdge();
    }

    public Map createEdgeAttributes() {
        Map map = new Hashtable();
        GraphConstants.setLineEnd(map, GraphConstants.ARROW_SIMPLE);
        GraphConstants.setLabelAlongEdge(map, true);
        return map;
    }

    public void group(Object[] cells) {
        cells = graph.order(cells);
        if (cells != null && cells.length > 0) {
            DefaultGraphCell group = createGroupCell();
            graph.getGraphLayoutCache().insertGroup(group, cells);
        }
    }

    protected DefaultGraphCell createGroupCell() {
        return new DefaultGraphCell();
    }

    protected int getCellCount(JGraph graph) {
        Object[] cells = graph.getDescendants(graph.getRoots());
        return cells.length;
    }

    public void ungroup(Object[] cells) {
        graph.getGraphLayoutCache().ungroup(cells);
    }

    public boolean isGroup(Object cell) {
        CellView view = graph.getGraphLayoutCache().getMapping(cell, false);
        if (view != null) return !view.isLeaf();
        return false;
    }

    public void toFront(Object[] c) {
        graph.getGraphLayoutCache().toFront(c);
    }

    public void toBack(Object[] c) {
        graph.getGraphLayoutCache().toBack(c);
    }

    public void undo() {
        try {
            undoManager.undo(graph.getGraphLayoutCache());
        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            updateHistoryButtons();
        }
    }

    public void redo() {
        try {
            undoManager.redo(graph.getGraphLayoutCache());
        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            updateHistoryButtons();
        }
    }

    protected void updateHistoryButtons() {
        undo.setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
        redo.setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
    }

    public void valueChanged(GraphSelectionEvent e) {
        group.setEnabled(graph.getSelectionCount() > 1);
        boolean enabled = !graph.isSelectionEmpty();
        remove.setEnabled(enabled);
        ungroup.setEnabled(enabled);
        tofront.setEnabled(enabled);
        toback.setEnabled(enabled);
        copy.setEnabled(enabled);
        cut.setEnabled(enabled);
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) remove.actionPerformed(null);
    }

    public static class MyGraph extends JGraph {

        public MyGraph(GraphModel model) {
            this(model, null);
        }

        public MyGraph(GraphModel model, GraphLayoutCache cache) {
            super(model, cache);
            setPortsVisible(true);
            setGridEnabled(true);
            setGridSize(6);
            setTolerance(2);
            setInvokesStopCellEditing(true);
            setCloneable(true);
            setJumpToDefaultPort(true);
        }
    }

    public static class MyEdgeHandle extends EdgeView.EdgeHandle {

        /**
		 * @param edge
		 * @param ctx
		 */
        public MyEdgeHandle(EdgeView edge, GraphContext ctx) {
            super(edge, ctx);
        }

        public boolean isAddPointEvent(MouseEvent event) {
            return event.isShiftDown();
        }

        public boolean isRemovePointEvent(MouseEvent event) {
            return event.isShiftDown();
        }
    }

    public static class MyModel extends DefaultGraphModel {

        public boolean acceptsSource(Object edge, Object port) {
            return (((Edge) edge).getTarget() != port);
        }

        public boolean acceptsTarget(Object edge, Object port) {
            return (((Edge) edge).getSource() != port);
        }
    }

    public class MyMarqueeHandler extends BasicMarqueeHandler {

        protected Point2D start, current;

        protected PortView port, firstPort;

        public boolean isForceMarqueeEvent(MouseEvent e) {
            if (e.isShiftDown()) return false;
            if (SwingUtilities.isRightMouseButton(e)) return true;
            port = getSourcePortAt(e.getPoint());
            if (port != null && graph.isPortsVisible()) return true;
            return super.isForceMarqueeEvent(e);
        }

        public void mousePressed(final MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
                JPopupMenu menu = createPopupMenu(e.getPoint(), cell);
                menu.show(graph, e.getX(), e.getY());
            } else if (port != null && graph.isPortsVisible()) {
                start = graph.toScreen(port.getLocation());
                firstPort = port;
                System.out.println("CELL=" + graph.getFirstCellForLocation(e.getX(), e.getY()));
            } else {
                System.out.println("CELL1=" + graph.getFirstCellForLocation(e.getX(), e.getY()));
                super.mousePressed(e);
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (start != null) {
                Graphics g = graph.getGraphics();
                PortView newPort = getTargetPortAt(e.getPoint());
                if (newPort == null || newPort != port) {
                    paintConnector(Color.black, graph.getBackground(), g);
                    port = newPort;
                    if (port != null) current = graph.toScreen(port.getLocation()); else current = graph.snap(e.getPoint());
                    paintConnector(graph.getBackground(), Color.black, g);
                }
            }
            super.mouseDragged(e);
        }

        public PortView getSourcePortAt(Point2D point) {
            graph.setJumpToDefaultPort(false);
            PortView result;
            try {
                result = graph.getPortViewAt(point.getX(), point.getY());
            } finally {
                graph.setJumpToDefaultPort(true);
            }
            return result;
        }

        protected PortView getTargetPortAt(Point2D point) {
            return graph.getPortViewAt(point.getX(), point.getY());
        }

        public void mouseReleased(MouseEvent e) {
            if (e != null && port != null && firstPort != null && firstPort != port) {
                connect((Port) firstPort.getCell(), (Port) port.getCell());
                e.consume();
            } else graph.repaint();
            firstPort = port = null;
            start = current = null;
            super.mouseReleased(e);
        }

        public void mouseMoved(MouseEvent e) {
            if (e != null && getSourcePortAt(e.getPoint()) != null && graph.isPortsVisible()) {
                graph.setCursor(new Cursor(Cursor.HAND_CURSOR));
                e.consume();
            } else super.mouseMoved(e);
        }

        protected void paintConnector(Color fg, Color bg, Graphics g) {
            g.setColor(fg);
            g.setXORMode(bg);
            paintPort(graph.getGraphics());
            if (firstPort != null && start != null && current != null) g.drawLine((int) start.getX(), (int) start.getY(), (int) current.getX(), (int) current.getY());
        }

        protected void paintPort(Graphics g) {
            if (port != null) {
                boolean o = (GraphConstants.getOffset(port.getAllAttributes()) != null);
                Rectangle2D r = (o) ? port.getBounds() : port.getParentView().getBounds();
                r = graph.toScreen((Rectangle2D) r.clone());
                r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r.getHeight() + 6);
                graph.getUI().paintCell(g, port, r, true);
            }
        }
    }

    public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
        JPopupMenu menu = new JPopupMenu();
        if (cell != null) {
            menu.add(new AbstractAction("Edit") {

                public void actionPerformed(ActionEvent e) {
                    graph.startEditingAtCell(cell);
                }
            });
        }
        if (!graph.isSelectionEmpty()) {
            menu.addSeparator();
            menu.add(new AbstractAction("Remove") {

                public void actionPerformed(ActionEvent e) {
                    remove.actionPerformed(e);
                }
            });
        }
        menu.addSeparator();
        menu.add(new AbstractAction("Insert") {

            public void actionPerformed(ActionEvent ev) {
                insert(pt);
            }
        });
        return menu;
    }

    private ImageIcon getImageIcon(String iconName) {
        URL insertUrl = getClass().getClassLoader().getResource(RESOURCE_BASE + iconName);
        ImageIcon icon;
        if (insertUrl != null) {
            icon = new ImageIcon(insertUrl);
        } else {
            String imgKey = ISharedImages.IMG_OBJS_INFO_TSK;
            ISharedImages si = PlatformUI.getWorkbench().getSharedImages();
            icon = new ImageIcon(si.getImage(imgKey).getImageData().data);
        }
        return icon;
    }

    public JToolBar createToolBar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        ImageIcon insertIcon = getImageIcon("insert.gif");
        toolbar.add(new AbstractAction("", insertIcon) {

            public void actionPerformed(ActionEvent e) {
                insert(new Point(10, 10));
            }
        });
        ImageIcon connectIcon = getImageIcon("connection.gif");
        toolbar.add(new AbstractAction("", connectIcon) {

            public void actionPerformed(ActionEvent e) {
                graph.setPortsVisible(!graph.isPortsVisible());
                URL connectUrl;
                if (graph.isPortsVisible()) connectUrl = getClass().getClassLoader().getResource(RESOURCE_BASE + "connectoff.gif"); else connectUrl = getClass().getClassLoader().getResource(RESOURCE_BASE + "connecton.gif");
                ImageIcon connectIcon = new ImageIcon(connectUrl);
                putValue(SMALL_ICON, connectIcon);
            }
        });
        toolbar.addSeparator();
        ImageIcon undoIcon = getImageIcon("undo.gif");
        undo = new AbstractAction("", undoIcon) {

            public void actionPerformed(ActionEvent e) {
                undo();
            }
        };
        undo.setEnabled(false);
        toolbar.add(undo);
        ImageIcon redoIcon = getImageIcon("redo.gif");
        redo = new AbstractAction("", redoIcon) {

            public void actionPerformed(ActionEvent e) {
                redo();
            }
        };
        redo.setEnabled(false);
        toolbar.add(redo);
        toolbar.addSeparator();
        Action action;
        URL url;
        action = javax.swing.TransferHandler.getCopyAction();
        ImageIcon cpActionIcon = getImageIcon("copy.gif");
        action.putValue(Action.SMALL_ICON, cpActionIcon);
        toolbar.add(copy = new EventRedirector(action));
        action = javax.swing.TransferHandler.getPasteAction();
        ImageIcon pastActionIcon = getImageIcon("past.gif");
        action.putValue(Action.SMALL_ICON, pastActionIcon);
        toolbar.add(paste = new EventRedirector(action));
        action = javax.swing.TransferHandler.getCutAction();
        ImageIcon cutActionIcon = getImageIcon("cut.gif");
        action.putValue(Action.SMALL_ICON, cutActionIcon);
        toolbar.add(cut = new EventRedirector(action));
        ImageIcon removeIcon = getImageIcon("delete.gif");
        remove = new AbstractAction("", removeIcon) {

            public void actionPerformed(ActionEvent e) {
                if (!graph.isSelectionEmpty()) {
                    Object[] cells = graph.getSelectionCells();
                    cells = graph.getDescendants(cells);
                    graph.getModel().remove(cells);
                }
            }
        };
        remove.setEnabled(false);
        toolbar.add(remove);
        toolbar.addSeparator();
        ImageIcon toFrontIcon = getImageIcon("tofront.gif");
        tofront = new AbstractAction("", toFrontIcon) {

            public void actionPerformed(ActionEvent e) {
                if (!graph.isSelectionEmpty()) toFront(graph.getSelectionCells());
            }
        };
        tofront.setEnabled(false);
        toolbar.add(tofront);
        ImageIcon toBackIcon = getImageIcon("toback.gif");
        toback = new AbstractAction("", toBackIcon) {

            public void actionPerformed(ActionEvent e) {
                if (!graph.isSelectionEmpty()) toBack(graph.getSelectionCells());
            }
        };
        toback.setEnabled(false);
        toolbar.add(toback);
        toolbar.addSeparator();
        ImageIcon zoomIcon = getImageIcon("zoom.gif");
        toolbar.add(new AbstractAction("", zoomIcon) {

            public void actionPerformed(ActionEvent e) {
                graph.setScale(1.0);
            }
        });
        ImageIcon zoomInIcon = getImageIcon("zoomin.gif");
        toolbar.add(new AbstractAction("", zoomInIcon) {

            public void actionPerformed(ActionEvent e) {
                graph.setScale(2 * graph.getScale());
            }
        });
        ImageIcon zoomOutIcon = getImageIcon("zoomout.gif");
        toolbar.add(new AbstractAction("", zoomOutIcon) {

            public void actionPerformed(ActionEvent e) {
                graph.setScale(graph.getScale() / 2);
            }
        });
        toolbar.addSeparator();
        ImageIcon groupIcon = getImageIcon("group.gif");
        group = new AbstractAction("", groupIcon) {

            public void actionPerformed(ActionEvent e) {
                group(graph.getSelectionCells());
            }
        };
        group.setEnabled(false);
        toolbar.add(group);
        ImageIcon ungroupIcon = getImageIcon("ungroup.gif");
        ungroup = new AbstractAction("", ungroupIcon) {

            public void actionPerformed(ActionEvent e) {
                ungroup(graph.getSelectionCells());
            }
        };
        ungroup.setEnabled(false);
        toolbar.add(ungroup);
        return toolbar;
    }

    /**
	 * @return Returns the graph.
	 */
    public JGraph getGraph() {
        return graph;
    }

    /**
	 * @param graph
	 *            The graph to set.
	 */
    public void setGraph(JGraph graph) {
        this.graph = graph;
    }

    public class EventRedirector extends AbstractAction {

        protected Action action;

        public EventRedirector(Action a) {
            super("", (ImageIcon) a.getValue(Action.SMALL_ICON));
            this.action = a;
        }

        public void actionPerformed(ActionEvent e) {
            e = new ActionEvent(graph, e.getID(), e.getActionCommand(), e.getModifiers());
            action.actionPerformed(e);
        }
    }

    /**
	 * Create a status bar
	 */
    protected JPanel createStatusBar() {
        return new EdStatusBar();
    }

    /**
	 * 
	 * @return a String representing the version of this application
	 */
    protected String getVersion() {
        return JGraph.VERSION;
    }

    /**
	 * @return Returns the redo.
	 */
    public Action getRedo() {
        return redo;
    }

    /**
	 * @param redo
	 *            The redo to set.
	 */
    public void setRedo(Action redo) {
        this.redo = redo;
    }

    /**
	 * @return Returns the undo.
	 */
    public Action getUndo() {
        return undo;
    }

    /**
	 * @param undo
	 *            The undo to set.
	 */
    public void setUndo(Action undo) {
        this.undo = undo;
    }

    public class EdStatusBar extends JPanel implements GraphModelListener {

        /**
		 * 
		 */
        protected JLabel leftSideStatus;

        /**
		 * contains the scale for the current graph
		 */
        protected JLabel rightSideStatus;

        /**
		 * Constructor for GPStatusBar.
		 * 
		 */
        public EdStatusBar() {
            super();
            setLayout(new BorderLayout());
            leftSideStatus = new JLabel(getVersion());
            rightSideStatus = new JLabel("0/0Mb");
            leftSideStatus.setBorder(BorderFactory.createLoweredBevelBorder());
            rightSideStatus.setBorder(BorderFactory.createLoweredBevelBorder());
            add(leftSideStatus, BorderLayout.CENTER);
            add(rightSideStatus, BorderLayout.EAST);
        }

        /**
		 * Graph Model change event
		 */
        public void graphChanged(GraphModelEvent e) {
            updateHeapStatus();
        }

        protected void updateHeapStatus() {
            Runtime runtime = Runtime.getRuntime();
            int freeMemory = (int) (runtime.freeMemory() / 1024);
            int totalMemory = (int) (runtime.totalMemory() / 1024);
            int usedMemory = (totalMemory - freeMemory);
            String str = (usedMemory / 1024) + "/" + (totalMemory / 1024) + "Mb";
            rightSideStatus.setText(str);
        }

        /**
		 * @return Returns the leftSideStatus.
		 */
        public JLabel getLeftSideStatus() {
            return leftSideStatus;
        }

        /**
		 * @param leftSideStatus
		 *            The leftSideStatus to set.
		 */
        public void setLeftSideStatus(JLabel leftSideStatus) {
            this.leftSideStatus = leftSideStatus;
        }

        /**
		 * @return Returns the rightSideStatus.
		 */
        public JLabel getRightSideStatus() {
            return rightSideStatus;
        }

        /**
		 * @param rightSideStatus
		 *            The rightSideStatus to set.
		 */
        public void setRightSideStatus(JLabel rightSideStatus) {
            this.rightSideStatus = rightSideStatus;
        }
    }

    /**
	 * @return Returns the copy.
	 */
    public Action getCopy() {
        return copy;
    }

    /**
	 * @param copy
	 *            The copy to set.
	 */
    public void setCopy(Action copy) {
        this.copy = copy;
    }

    /**
	 * @return Returns the cut.
	 */
    public Action getCut() {
        return cut;
    }

    /**
	 * @param cut
	 *            The cut to set.
	 */
    public void setCut(Action cut) {
        this.cut = cut;
    }

    /**
	 * @return Returns the paste.
	 */
    public Action getPaste() {
        return paste;
    }

    /**
	 * @param paste
	 *            The paste to set.
	 */
    public void setPaste(Action paste) {
        this.paste = paste;
    }

    /**
	 * @return Returns the toback.
	 */
    public Action getToback() {
        return toback;
    }

    /**
	 * @param toback
	 *            The toback to set.
	 */
    public void setToback(Action toback) {
        this.toback = toback;
    }

    /**
	 * @return Returns the tofront.
	 */
    public Action getTofront() {
        return tofront;
    }

    /**
	 * @param tofront
	 *            The tofront to set.
	 */
    public void setTofront(Action tofront) {
        this.tofront = tofront;
    }

    /**
	 * @return Returns the remove.
	 */
    public Action getRemove() {
        return remove;
    }

    /**
	 * @param remove
	 *            The remove to set.
	 */
    public void setRemove(Action remove) {
        this.remove = remove;
    }

    public void createControl(Composite parent) {
        composite = new Composite(parent, SWT.EMBEDDED);
        composite.setLayout(new FillLayout());
        Frame frame = SWT_AWT.new_Frame(composite);
        frame.add(panel);
    }

    public Control getControl() {
        return composite;
    }

    public void setFocus() {
    }
}
