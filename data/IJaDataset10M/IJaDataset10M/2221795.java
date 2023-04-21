package de.grogra.imp;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import de.grogra.graph.ArrayPath;
import de.grogra.graph.Graph;
import de.grogra.graph.GraphState;
import de.grogra.graph.GraphUtils;
import de.grogra.graph.Path;
import de.grogra.graph.impl.Edge;
import de.grogra.graph.impl.EdgeImpl;
import de.grogra.graph.impl.Node;
import de.grogra.imp.awt.CanvasAdapter;
import de.grogra.imp.edit.ViewSelection;
import de.grogra.pf.ui.Command;
import de.grogra.pf.ui.Context;
import de.grogra.pf.ui.JobManager;
import de.grogra.pf.ui.UI;
import de.grogra.pf.ui.event.ClickEvent;
import de.grogra.pf.ui.event.DragEvent;
import de.grogra.util.DisposableEventListener;
import de.grogra.util.EventListener;
import de.grogra.util.I18NBundle;
import de.grogra.util.Map;
import de.grogra.util.Utils;

public abstract class ComponentViewHierarchicalGraphModeEventHandler implements ViewEventHandlerIF {

    private boolean disposed = false;

    private JobManager jm;

    private View view;

    private DisposableEventListener navigator = null;

    private PickList list, list2;

    private PickElement pickInfo = new PickElement();

    private ArrayPath lastSelected = new ArrayPath((Graph) null);

    private static final int NOTHING = 0, HIGHLIGHTED = 1, SELECTED = 2;

    private int highlightState = NOTHING, highlightIndex;

    private static final int NORMAL = 1, NAVIGATING = 2, DRAGGING = 3;

    private int state = NORMAL;

    private MouseEvent dragEvent = null, pressEvent = null;

    private int pickX, pickY, lastDragX, lastDragY, lastDragXrec, lastDragYrec;

    private int tolerance = 10, highlightDelay = 250;

    private final int[] a2 = new int[2];

    private int chX = -1, chY;

    private Point lastClickedPosition = new Point();

    private Node startConnectionNode;

    private Node pickedNode;

    private int edgeSelected = 0;

    private class Unhighlight implements Command {

        private boolean canceled = false;

        void cancel() {
            canceled = true;
        }

        public String getCommandName() {
            return null;
        }

        public void run(Object info, Context ctx) {
            if (!canceled) {
                resetHighlight();
            }
        }
    }

    private Unhighlight unhighlight = null;

    public ComponentViewHierarchicalGraphModeEventHandler(View view, boolean allowNegativePickDist) {
        this.view = view;
        jm = view.getWorkbench().getJobManager();
        list = new PickList(10, allowNegativePickDist);
        list2 = new PickList(10, allowNegativePickDist);
    }

    public void dispose() {
        if (disposed) {
            return;
        }
        disposed = true;
        if (unhighlight != null) {
            unhighlight.cancel();
            unhighlight = null;
        }
        list.reset();
        list2.reset();
        pickInfo = null;
        if (navigator != null) {
            navigator.dispose();
            navigator = null;
        }
        dragEvent = null;
        view = null;
        jm = null;
    }

    public final View getView() {
        return view;
    }

    private void setState(int newState) {
        if (state == newState) {
            return;
        }
        switch(state) {
            case NORMAL:
                if (unhighlight != null) {
                    unhighlight.cancel();
                    unhighlight = null;
                }
                if (newState != DRAGGING) {
                    resetHighlight();
                }
                break;
            case NAVIGATING:
                navigator.dispose();
                navigator = null;
                break;
            case DRAGGING:
                if (dragEvent != null) {
                    mouseDragged(dragEvent, DragEvent.DRAGGING_FINISHED, 0, 0);
                    System.out.println(pickedNode.hashCode());
                    handleDraggedLayout(pickedNode, lastDragX - lastDragXrec, lastDragY - lastDragYrec);
                    dragEvent = null;
                    dragEvent = null;
                }
                break;
        }
        state = newState;
    }

    public void disposeNavigator(EventObject e) {
        setState(NORMAL);
        if (e != null) {
            eventOccured(e);
        }
    }

    protected abstract NavigatorFactory getNavigatorFactory();

    protected abstract void handleDraggedLayout(Object co, int x, int y);

    public void eventOccured(EventObject e) {
        try {
            View.set(GraphState.current(view.getGraph()), view);
            NavigatorFactory nf = getNavigatorFactory();
            if (state == NAVIGATING) {
                navigator.eventOccured(e);
            } else if ((state == NORMAL) && (nf != null) && nf.isActivationEvent(e)) {
                UI.consume(e);
                setState(NAVIGATING);
                navigator = nf.createNavigator(this, e);
            } else {
                if (!(e instanceof MouseEvent)) {
                    return;
                }
                MouseEvent event = (MouseEvent) e;
                switch(event.getID()) {
                    case MouseEvent.MOUSE_PRESSED:
                        if (state == NORMAL) {
                            pressEvent = event;
                            buttonClicked(event);
                            handlePress(event);
                            break;
                        }
                        break;
                    case MouseEvent.MOUSE_RELEASED:
                        if (state == NORMAL) {
                            buttonClicked(event);
                        } else {
                            setState(NORMAL);
                        }
                        break;
                    case MouseEvent.MOUSE_CLICKED:
                        if (state == NORMAL) {
                            handleClick(event);
                            if (!event.isConsumed()) {
                                buttonClicked(event);
                            }
                        }
                        break;
                    case MouseEvent.MOUSE_MOVED:
                        if (state != NORMAL) {
                            setState(NORMAL);
                        }
                        mouseMoved(event);
                        event.consume();
                        break;
                    case MouseEvent.MOUSE_DRAGGED:
                        dragEvent = event;
                        if (state == NORMAL) {
                            setState(DRAGGING);
                        }
                        if (pressEvent != null) {
                            mouseDragged(pressEvent, DragEvent.DRAGGING_STARTED, 0, 0);
                            lastDragX = pressEvent.getX();
                            lastDragXrec = lastDragX;
                            lastDragY = pressEvent.getY();
                            lastDragYrec = lastDragY;
                            pressEvent = null;
                        }
                        mouseDragged(event, DragEvent.DRAGGING_CONTINUED, event.getX() - lastDragX, event.getY() - lastDragY);
                        lastDragX = event.getX();
                        lastDragY = event.getY();
                        event.consume();
                        break;
                    case MouseEvent.MOUSE_EXITED:
                        enqueueUnhighlight();
                        break;
                }
            }
        } finally {
            View.set(GraphState.current(view.getGraph()), null);
        }
    }

    private Path getHighlightedPath(int shift) {
        if (highlightState == NOTHING) {
            return null;
        } else {
            list.getItem((highlightIndex + shift + list.getSize()) % list.getSize(), pickInfo);
            return pickInfo.path;
        }
    }

    private void handleClick(MouseEvent event) {
        if (event.isAltDown()) {
            if ((highlightState != NOTHING) && (list.getSize() > 1)) {
                event.consume();
                a2[0] = highlightIndex;
                if (event.isControlDown()) {
                    if (highlightIndex == 0) {
                        highlightIndex = list.getSize();
                    }
                    highlightIndex--;
                } else {
                    highlightIndex++;
                    if (highlightIndex == list.getSize()) {
                        highlightIndex = 0;
                    }
                }
                a2[1] = highlightIndex;
                highlight(list, highlightIndex, a2);
                highlightState = SELECTED;
                lastSelected.set(getHighlightedPath(0));
                if (unhighlight != null) {
                    enqueueUnhighlight();
                }
            }
        }
        if (event.getButton() == MouseEvent.BUTTON1) {
            objectPicked();
        }
        if (event.getButton() == MouseEvent.BUTTON3) {
            I18NBundle thisI18NBundle = view.getWorkbench().getRegistry().getPluginDescriptor("de.grogra.imp").getI18NBundle();
            if (highlightState == 0) {
                return;
            }
            JPopupMenu menu = new JPopupMenu();
            if (!pickInfo.path.endsInNode()) {
                if (pickInfo.path.getNodeAndEdgeCount() - 1 >= 0) {
                    Object co = pickInfo.path.getEdge(pickInfo.path.getNodeAndEdgeCount() - 1);
                    if (co instanceof Node) {
                        final Node originNode = (Node) co;
                        JMenu menuDeleteEdge = new JMenu(thisI18NBundle.getString("menuDeleteEdge.Name"));
                        JMenuItem menuItemDeleteEdge;
                        StringBuffer edgeKeys = new StringBuffer();
                        Edge tEdge = new EdgeImpl(originNode.getSource(), originNode.getTarget());
                        tEdge.setEdgeBits(originNode.getEdgeBits(), null);
                        tEdge.getEdgeKeys(edgeKeys, true, true);
                        menuItemDeleteEdge = new JMenuItem(thisI18NBundle.getString("menuItemDeleteEdge.Name") + "   " + edgeKeys);
                        menuItemDeleteEdge.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent event) {
                                view.getWorkbench().delete(null, null, view);
                                view.repaint(ViewComponent.SCENE | ViewComponent.CHANGED);
                            }
                        });
                        menuDeleteEdge.add(menuItemDeleteEdge);
                        menu.add(menuDeleteEdge);
                    } else if (co instanceof EdgeImpl) {
                        final EdgeImpl originEdge = (EdgeImpl) co;
                        JMenu menuDeleteEdge = new JMenu(thisI18NBundle.getString("menuDeleteEdge.Name"));
                        JMenuItem menuItemDeleteEdge;
                        for (int i = Graph.MIN_NORMAL_BIT_INDEX; i < Graph.MAX_NORMAL_BIT_INDEX; i++) {
                            final int mask = 1 << i;
                            if (originEdge.testEdgeBits(mask)) {
                                StringBuffer edgeKeys = new StringBuffer();
                                Edge tEdge = new EdgeImpl(originEdge.getSource(), originEdge.getTarget());
                                tEdge.setEdgeBits(mask, null);
                                tEdge.getEdgeKeys(edgeKeys, true, true);
                                if (mask != Graph.DUMMY_EDGE) {
                                    menuItemDeleteEdge = new JMenuItem(thisI18NBundle.getString("menuItemDeleteEdge.Name") + "   " + edgeKeys);
                                    menuItemDeleteEdge.addActionListener(new ActionListener() {

                                        @Override
                                        public void actionPerformed(ActionEvent event) {
                                            originEdge.removeEdgeBits(mask, null);
                                            view.repaint(ViewComponent.SCENE | ViewComponent.CHANGED);
                                        }
                                    });
                                    menuDeleteEdge.add(menuItemDeleteEdge);
                                }
                            }
                        }
                        menu.add(menuDeleteEdge);
                    }
                }
            } else {
                Object co = pickInfo.path.getNode(pickInfo.path.getNodeAndEdgeCount() - 1);
                if (co instanceof Node) {
                    final Node n = (Node) co;
                    JMenuItem menuItemDelete = new JMenuItem(thisI18NBundle.getString("menuItemDelete.Name"));
                    menuItemDelete.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent event) {
                            if (n.getGraph() != null) {
                                view.getWorkbench().delete(null, null, view);
                            }
                            view.repaint(ViewComponent.SCENE | ViewComponent.CHANGED);
                        }
                    });
                    menu.add(menuItemDelete);
                }
            }
            menu.addSeparator();
            JMenuItem menuItemTest = new JMenuItem(thisI18NBundle.getString("menuItemTest.Name"));
            menuItemTest.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                }
            });
            menu.add(menuItemTest);
            menu.show(event.getComponent(), event.getX(), event.getY());
        }
    }

    private void handlePress(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            objectPicked();
        }
    }

    private void objectPicked() {
        if (pickInfo.path.endsInNode()) {
            Object co = pickInfo.path.getNode(pickInfo.path.getNodeAndEdgeCount() - 1);
            if (co instanceof Node) {
                Node n = (Node) co;
                if (n.getId() != -1) {
                    pickedNode = n;
                    if (edgeSelected == 0) {
                        startConnectionNode = n;
                    } else {
                        if (startConnectionNode != null && edgeSelected != 0) {
                            if (startConnectionNode != n) {
                                connect(startConnectionNode, n, edgeSelected);
                                edgeSelected = 0;
                                startConnectionNode = null;
                                view.repaint(ViewComponent.SCENE | ViewComponent.CHANGED);
                            }
                        }
                    }
                }
            } else {
                startConnectionNode = null;
            }
        }
    }

    public void setDropTarget(Node node) {
        connect(startConnectionNode, node, Graph.REFINEMENT_EDGE);
        connect((Node) view.getGraph().getRoot(Graph.COMPONENT_GRAPH), node, Graph.DUMMY_EDGE);
        view.repaint(ViewComponent.SCENE | ViewComponent.CHANGED);
    }

    /**
	 * Connect the two specified nodes according to the edge type.
	 * 
	 * @param start
	 * @param target
	 * @param edgeType
	 */
    public abstract void connect(Node start, Node target, int edgeType);

    public void setSelectedEdge(int edgeType) {
        edgeSelected = edgeType;
    }

    protected void mouseMoved(final MouseEvent event) {
        if ((highlightState != SELECTED) || (Math.abs(event.getX() - pickX) > tolerance) || (Math.abs(event.getY() - pickY) > tolerance)) {
            UI.executeLockedly(view.getGraph(), false, new Command() {

                public String getCommandName() {
                    return null;
                }

                public void run(Object arg, Context c) {
                    calculateHighlight(event.getX(), event.getY());
                }
            }, event, view, JobManager.RENDER_FLAGS);
            if (edgeSelected != 0 && startConnectionNode != null) {
                getViewComponentComponent().setPointForLine(lastClickedPosition.x, lastClickedPosition.y, event.getX(), event.getY());
            }
        }
    }

    public void updateHighlight() {
        if (chX >= 0) {
            calculateHighlight(chX, chY);
        }
    }

    private void calculateHighlight(int x, int y) {
        chX = x;
        chY = y;
        view.pick(x, y, list2);
        if (list2.getSize() > 0) {
            if (unhighlight != null) {
                unhighlight.cancel();
                unhighlight = null;
            }
            if (list2.equals(list)) {
                list2.reset();
            } else {
                resetHighlight();
                PickList l = list;
                list = list2;
                list2 = l;
                pickX = x;
                pickY = y;
                highlightIndex = -1;
                for (int i = 0; i < list.getSize(); i++) {
                    list.getItem(i, pickInfo);
                    if (GraphUtils.equal(pickInfo.path, lastSelected)) {
                        highlightIndex = i;
                        break;
                    }
                }
                if (highlightIndex < 0) {
                    lastSelected.clear(null);
                    highlightIndex = 0;
                }
                Map opt = UI.getOptions(getView().getWorkbench());
                if (Utils.getBoolean(opt, "highlightOnMove", true)) {
                    highlight(list, highlightIndex, null);
                }
                highlightState = HIGHLIGHTED;
            }
        } else if ((highlightState != NOTHING) && (unhighlight == null)) {
            enqueueUnhighlight();
        }
    }

    private void enqueueUnhighlight() {
        if (unhighlight != null) {
            unhighlight.cancel();
        }
        jm.runLater(highlightDelay, unhighlight = new Unhighlight(), null, view);
    }

    private void resetHighlight() {
        if (unhighlight != null) {
            unhighlight.cancel();
            unhighlight = null;
        }
        if (highlightState != NOTHING) {
            highlight(list, -1, null);
            list.reset();
            highlightState = NOTHING;
        }
    }

    private void highlight(PickList list, int index, int[] changed) {
        ViewSelection s = ViewSelection.get(view);
        int i = -1, j = -1, k;
        while (true) {
            if (changed == null) {
                i++;
                if (i == list.getSize()) {
                    break;
                }
            } else {
                j++;
                if (j == changed.length) {
                    break;
                }
                i = changed[j];
            }
            if (index >= 0) {
                if (i == index) {
                    k = ViewSelection.MOUSE_OVER_SELECTED | ViewSelection.MOUSE_OVER;
                } else {
                    k = ViewSelection.MOUSE_OVER;
                }
            } else {
                k = 0;
            }
            Path p = list.getPath(i);
            if (!view.isToolGraph(p.getGraph())) {
                s.removeAndAdd(ViewSelection.MOUSE_OVER_SELECTED | ViewSelection.MOUSE_OVER, k, p);
            }
        }
    }

    protected void buttonClicked(MouseEvent e) {
        lastClickedPosition.setLocation(e.getX(), e.getY());
        e.consume();
        Path path = null;
        boolean tool = false;
        for (int s = 0; s < list.getSize(); s++) {
            Path p = getHighlightedPath(s);
            if (p != null) {
                if (view.isToolGraph(p.getGraph())) {
                    if (s == 0) {
                        tool = true;
                    }
                } else {
                    path = p;
                    break;
                }
            }
        }
        if (!tool && (e.getID() == MouseEvent.MOUSE_PRESSED) && !(e.isAltDown() || e.isMetaDown())) {
            ViewSelection s = ViewSelection.get(view);
            if (s != null) {
                if (path != null) {
                    if (e.isControlDown()) {
                        s.toggle(ViewSelection.SELECTED, path);
                    } else {
                        s.set(ViewSelection.SELECTED, new Path[] { path }, true);
                    }
                } else {
                    s.set(ViewSelection.SELECTED, Path.PATH_0, true);
                }
            }
        }
        if (hasListener(path)) {
            path = new ArrayPath(path);
            ClickEvent me = createClickEvent(e);
            me.set(getView(), path);
            me.set(e);
            send(me, path);
        }
    }

    private static void send(EventObject e, Path p) {
        Object last = p.getObject(-1);
        if (last instanceof EventListener) {
            ((EventListener) last).eventOccured(e);
        }
        if (p.getGraph() instanceof EventListener) {
            ((EventListener) p.getGraph()).eventOccured(e);
        }
    }

    private static boolean hasListener(Path p) {
        if (p == null) {
            return false;
        }
        return (p.getObject(-1) instanceof EventListener) || (p.getGraph() instanceof EventListener);
    }

    protected void mouseDragged(MouseEvent event, int dragState, int dx, int dy) {
        Path p = getHighlightedPath(0);
        if (hasListener(p)) {
            p = new ArrayPath(p);
            DragEvent me = createDragEvent(event);
            me.set(getView(), p);
            me.set(event);
            me.setDragData(dragState, dx, dy);
            send(me, p);
        }
    }

    public abstract ClickEvent createClickEvent(MouseEvent event);

    public abstract DragEvent createDragEvent(MouseEvent event);

    protected abstract CanvasAdapter.CanvasComponent getViewComponentComponent();
}
