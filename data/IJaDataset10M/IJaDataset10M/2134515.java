package ingenias.jade.graphics;

import ingenias.editor.CellHelpWindow;
import ingenias.editor.cell.NAryEdge;
import ingenias.editor.entities.RoleEntity;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.PortView;

public abstract class MarqueeHandlerIAF extends BasicMarqueeHandler implements java.io.Serializable {

    /**
	 *  Description of the Field
	 */
    protected Point start, current;

    /**
	 *  Description of the Field
	 */
    protected PortView port, firstPort;

    private JGraph graph;

    public MarqueeHandlerIAF(JGraph graph) {
        this.graph = graph;
    }

    protected Point convert(java.awt.geom.Point2D p2d) {
        Point p = new Point((int) p2d.getX(), (int) p2d.getY());
        return p;
    }

    protected JGraph getGraph() {
        return graph;
    }

    /**
	 *  Gets the forceMarqueeEvent attribute of the MarqueeHandler object
	 *
	 *@param  e  Description of Parameter
	 *@return    The forceMarqueeEvent value
	 */
    public boolean isForceMarqueeEvent(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            return true;
        }
        port = getSourcePortAt(e.getPoint());
        if (port != null && getGraph().isPortsVisible()) {
            return true;
        }
        return super.isForceMarqueeEvent(e);
    }

    /**
	 *  Gets the sourcePortAt attribute of the MarqueeHandler object
	 *
	 *@param  point  Description of Parameter
	 *@return        The sourcePortAt value
	 */
    public PortView getSourcePortAt(Point point) {
        Point tmp = convert(getGraph().fromScreen(new Point(point)));
        return getGraph().getPortViewAt(tmp.x, tmp.y);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of Parameter
	 */
    public void mousePressed(final MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            Point loc = convert(getGraph().fromScreen(e.getPoint()));
            Object cell = getGraph().getFirstCellForLocation(loc.x, loc.y);
            JPopupMenu menu = new JPopupMenu();
            if (cell != null) System.err.println(cell.getClass().getName());
            if (cell instanceof DefaultGraphCell) {
                menu.add("Entity");
                menu.addSeparator();
                addActionsToPopupMenu(menu, this.createBasicActions((DefaultGraphCell) cell));
                menu.addSeparator();
                menu.add(createMenu("Views", this.createChangeViewActions((DefaultGraphCell) cell)));
                menu.add(createMenu("Debug", this.createDebugActions((DefaultGraphCell) cell)));
            } else {
                if (getGraph().getSelectionCells() != null && getGraph().getSelectionCells().length > 1) {
                    GraphCell[] gc = new GraphCell[getGraph().getSelectionCells().length];
                    System.arraycopy(getGraph().getSelectionCells(), 0, gc, 0, gc.length);
                    addActionsToPopupMenu(menu, this.createDiagramIndependentActions(e.getPoint(), gc));
                }
                addActionsToPopupMenu(menu, this.createDiagramOperations());
                addActionsToPopupMenu(menu, this.createDiagramSpecificInsertActions(e.getPoint()));
            }
            menu.show(getGraph(), e.getX(), e.getY());
        } else if (port != null && !e.isConsumed() && getGraph().isPortsVisible()) {
            start = convert(getGraph().toScreen(port.getLocation(null)));
            firstPort = port;
            e.consume();
        } else {
        }
        super.mousePressed(e);
    }

    private Vector<AbstractAction> createDiagramOperations() {
        Vector<AbstractAction> actions = new Vector<AbstractAction>();
        return actions;
    }

    private Vector<AbstractAction> createDiagramIndependentActions(final Point point, final GraphCell[] selected) {
        Vector<AbstractAction> actions = new Vector<AbstractAction>();
        return actions;
    }

    private void createRelationshipMenu(JPopupMenu menu, NAryEdge nary) {
        String typeOfRelationship = ((ingenias.editor.entities.NAryEdgeEntity) nary.getUserObject()).getType();
        Vector<String> roles = new Vector<String>(nary.getRoles());
        addActionsToPopupMenu(menu, this.createBasicActions(nary));
        menu.addSeparator();
        menu.add(createMenu("views", this.createChangeViewActions(nary)));
        for (int k = 0; k < roles.size(); k++) {
            DefaultEdge[] edgesPerRole = nary.getRoleEdges(roles.elementAt(k));
            if (edgesPerRole.length > 1) {
                for (int j = 0; j < edgesPerRole.length; j++) {
                    Vector<AbstractAction> edgeMenuActions = createEdgeActions(nary.getRoleEdges(roles.elementAt(k))[j]);
                    menu.add(createMenu("role:" + roles.elementAt(k), edgeMenuActions));
                }
            } else {
                if (edgesPerRole.length == 1) {
                    Vector<AbstractAction> edgeMenuActions = createEdgeActions(nary.getRoleEdges(roles.elementAt(k))[0]);
                    menu.add(createMenu("role:" + roles.elementAt(k), edgeMenuActions));
                }
            }
        }
    }

    private JMenu createMenu(String name, Vector<AbstractAction> actions) {
        Iterator<AbstractAction> it = actions.iterator();
        JMenu menu = new JMenu(name);
        while (it.hasNext()) {
            menu.add(it.next());
        }
        return menu;
    }

    private void addActionsToPopupMenu(JPopupMenu menu, Vector<AbstractAction> actions) {
        Iterator<AbstractAction> it = actions.iterator();
        while (it.hasNext()) {
            menu.add(it.next());
        }
    }

    private Vector<AbstractAction> createEdgeActions(final DefaultEdge cell) {
        Vector<AbstractAction> actions = new Vector<AbstractAction>();
        final RoleEntity re = (RoleEntity) (cell).getUserObject();
        Field[] fs = re.getClass().getFields();
        if (fs.length > 1) actions.add(new AbstractAction("Edit") {

            public void actionPerformed(ActionEvent e) {
                System.err.println("clase cell" + cell.getClass() + " " + cell.getUserObject());
                getGraph().startEditingAtCell(cell);
            }
        });
        if (fs.length > 1) actions.add(new AbstractAction("Hide fields") {

            public void actionPerformed(ActionEvent e) {
                re.hide();
            }
        });
        for (int k = 0; k < fs.length; k++) {
            if (!fs[k].getName().equalsIgnoreCase("id")) {
                final int j = k;
                actions.add(new AbstractAction("Show field " + fs[k].getName()) {

                    public void actionPerformed(ActionEvent e) {
                        re.show(j);
                    }
                });
            }
        }
        return actions;
    }

    protected abstract Vector<AbstractAction> createDebugActions(final DefaultGraphCell cell);

    private Vector<AbstractAction> createBasicActions(final DefaultGraphCell cell) {
        Vector<AbstractAction> actions = new Vector<AbstractAction>();
        actions.add(new AbstractAction("Help") {

            public void actionPerformed(ActionEvent e) {
                CellHelpWindow chw = new CellHelpWindow();
                DefaultGraphCell dgc = (DefaultGraphCell) cell;
                ingenias.editor.entities.Entity ent = ((ingenias.editor.entities.Entity) dgc.getUserObject());
                chw.setDescription(ent.getHelpDesc());
                chw.setRec(ent.getHelpRecom());
                chw.setSize(350, 300);
                chw.setLocation(new Point(150, 200));
                chw.show();
            }
        });
        actions.add(new AbstractAction("Edit") {

            public void actionPerformed(ActionEvent e) {
                getGraph().startEditingAtCell(cell);
            }
        });
        final ingenias.editor.entities.Entity ent = ((ingenias.editor.entities.Entity) cell.getUserObject());
        return actions;
    }

    protected abstract Vector<AbstractAction> createChangeViewActions(final DefaultGraphCell cell);

    protected abstract Vector<AbstractAction> createDiagramSpecificInsertActions(Point pt);

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of Parameter
	 */
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of Parameter
	 */
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of Parameter
	 */
    public void mouseMoved(MouseEvent e) {
        super.mouseReleased(e);
    }
}
