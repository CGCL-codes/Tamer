package ase.eleitweg.wfeditor.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Paint;
import java.awt.event.InputEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import ase.eleitweg.TestDBMemoryFilled;
import ase.eleitweg.common.Edge;
import ase.eleitweg.common.Node;
import ase.eleitweg.common.Workflow;
import ase.eleitweg.server.UserManager;
import ase.eleitweg.server.WorkflowManager;
import ase.eleitweg.wfeditor.gui.GDEditNode.Mode;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.swtdesigner.SWTResourceManager;
import edu.uci.ics.jung.exceptions.ConstraintViolationException;
import edu.uci.ics.jung.graph.DirectedEdge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.ToolTipFunction;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.graph.event.GraphEventType;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;
import edu.uci.ics.jung.utils.PredicateUtils;
import edu.uci.ics.jung.visualization.DefaultSettableVertexLocationFunction;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.MultiPickedState;
import edu.uci.ics.jung.visualization.PickSupport;
import edu.uci.ics.jung.visualization.PickedState;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;

public class GDesigner {

    /**
	 * This is the main composite of this frame
	 */
    protected Shell shell;

    protected final Display display = Display.getDefault();

    /**
	 * Jung only support to render a graph into a java.awt.frame, so this
	 * frame holds the visual display of the graph
	 *
	 */
    protected Frame drawFrame;

    /**
	 * This is my data structures represents the current graph
	 */
    protected LinkedList<INode> globalNodeList;

    protected LinkedList<IEdge> globalEdgeList;

    /**
	 * Here is the representation of the workflow as a jung graph
	 */
    protected Graph graph;

    /**
	 * Some stuff for presenting the graph and to interact with the user
	 */
    protected VisualizationViewer vv;

    protected Layout layout;

    protected PluggableRenderer renderer;

    protected DefaultSettableVertexLocationFunction vertexLocations;

    protected PluggableGraphMouse gMouse;

    /**
	 * WorkflowManager and UserManager are used for accessing the DB
	 * and adding the Workflow, getting the available UserGroups and so..
	 */
    protected WorkflowManager wfManager;

    protected UserManager uManager;

    /**
	 * The Workflow to edit is contained here
	 */
    protected Workflow workflow, retWF;

    /**
	 * Launch the application
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            Injector injector = Guice.createInjector(new TestDBMemoryFilled());
            WorkflowManager wm = injector.getInstance(WorkflowManager.class);
            UserManager um = injector.getInstance(UserManager.class);
            GDesigner window = new GDesigner(wm.getWorkflows().get(0), wm, um);
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GDesigner(Workflow workflow, WorkflowManager wfManager, UserManager uManager) {
        this.workflow = workflow;
        this.wfManager = wfManager;
        this.uManager = uManager;
    }

    /**
	 * Open the window
	 */
    public Workflow open() {
        retWF = null;
        init();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        return retWF;
    }

    protected void init() {
        globalNodeList = new LinkedList<INode>();
        globalEdgeList = new LinkedList<IEdge>();
    }

    /**
	 * Create contents of the window
	 */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(765, 726);
        shell.setText("SWT Application");
        final TabFolder mainTabFolder = new TabFolder(shell, SWT.NONE);
        mainTabFolder.setBounds(10, 10, 737, 674);
        final TabItem graphicTab = new TabItem(mainTabFolder, SWT.NONE);
        graphicTab.setText("Graphic");
        final Composite drawMainComposite = new Composite(mainTabFolder, SWT.NONE);
        drawMainComposite.setEnabled(true);
        graphicTab.setControl(drawMainComposite);
        final Group elementsGroup = new Group(drawMainComposite, SWT.NONE);
        elementsGroup.setText("Elements");
        elementsGroup.setBounds(10, 10, 100, 87);
        final Label nodeLabel = new Label(elementsGroup, SWT.NONE);
        nodeLabel.setBackground(SWTResourceManager.getColor(0, 255, 0));
        nodeLabel.setText("Node");
        nodeLabel.setBounds(10, 21, 80, 25);
        nodeLabel.addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent event) {
                openNodeEditor();
            }
        });
        final Label edgeLabel = new Label(elementsGroup, SWT.NONE);
        edgeLabel.setBackground(SWTResourceManager.getColor(0, 255, 255));
        edgeLabel.setText("Edge");
        edgeLabel.setBounds(10, 52, 80, 25);
        edgeLabel.addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent event) {
                GDCreateEdge geFrame = new GDCreateEdge();
                shell.setEnabled(false);
                Edge returnEdge = geFrame.open(vv);
                if (returnEdge != null) {
                    addEdge(returnEdge);
                } else System.out.println("Return edge is null");
                shell.setEnabled(true);
                shell.setActive();
            }
        });
        final Composite drawComposite = new Composite(drawMainComposite, SWT.EMBEDDED);
        drawComposite.setBounds(116, 10, 603, 597);
        drawFrame = SWT_AWT.new_Frame(drawComposite);
        createGraph();
        drawFrame.add(vv, BorderLayout.CENTER);
        final Button deleteButton = new Button(drawMainComposite, SWT.NONE);
        deleteButton.setText("Delete");
        deleteButton.setBounds(20, 103, 80, 25);
        deleteButton.addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent event) {
                deleteNodeAndEdges();
            }
        });
        final Button abbrechenButton = new Button(drawMainComposite, SWT.NONE);
        abbrechenButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                shell.dispose();
            }
        });
        abbrechenButton.setText("Abbrechen");
        abbrechenButton.setBounds(619, 613, 100, 25);
        final Button okButton = new Button(drawMainComposite, SWT.NONE);
        okButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                retWF = completeCurrentWorkflow();
                if (retWF == null) {
                    MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
                    box.setText("Fehler");
                    box.setMessage("Der erstellte Workflow ist inkonsistent! " + "Bitte überprüfen Sie folgende Punkte: " + " der Workflow sollte zusammenhängend sein, " + " und er sollte nur einen Startknoten besitzen!");
                    box.open();
                } else shell.dispose();
            }
        });
        okButton.setText("OK");
        okButton.setBounds(513, 613, 100, 25);
        final Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);
        final MenuItem workflowEigenMenuItem = new MenuItem(menu, SWT.CASCADE);
        workflowEigenMenuItem.setText("Workflow");
        final Menu menu_1 = new Menu(workflowEigenMenuItem);
        workflowEigenMenuItem.setMenu(menu_1);
        final MenuItem eigenschaftenMenuItem = new MenuItem(menu_1, SWT.NONE);
        eigenschaftenMenuItem.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                EditWorkflowProperties wp = new EditWorkflowProperties(shell);
                wp.open(workflow);
            }
        });
        eigenschaftenMenuItem.setText("Eigenschaften");
    }

    /**
	 * Creating the graph
	 * 
	 * @return
	 */
    protected void createGraph() {
        graph = new DirectedSparseGraph();
        graph.addListener(new GraphEventListener() {

            @Override
            public void edgeAdded(GraphEvent arg0) {
                System.out.println("Detecting adding Edge");
                refreshView();
            }

            @Override
            public void edgeRemoved(GraphEvent arg0) {
                System.out.println("Detecting remmoving Edge");
                refreshView();
            }

            @Override
            public void vertexAdded(GraphEvent arg0) {
                System.out.println("Detected adding node");
                refreshView();
            }

            @Override
            public void vertexRemoved(GraphEvent arg0) {
                System.out.println("Detected removing node");
                refreshView();
            }

            protected void refreshView() {
                if (vv != null) {
                    vv.stop();
                    vv.restart();
                    vv.repaint();
                }
            }
        }, GraphEventType.VERTEX_ADDITION);
        vertexLocations = new DefaultSettableVertexLocationFunction();
        layout = new FRLayout(graph);
        layout.initialize(drawFrame.getSize());
        renderer = new PluggableRenderer();
        renderer.setVertexPaintFunction(new VertexPaintFunction() {

            @Override
            public Paint getDrawPaint(Vertex arg0) {
                return Color.BLACK;
            }

            @SuppressWarnings("unchecked")
            @Override
            public Paint getFillPaint(Vertex arg0) {
                INode tmpNode = (INode) arg0;
                int incoming = tmpNode.getInEdges().size();
                int outgoing = tmpNode.getNode().getConditions().getEdgeCount();
                for (Edge tmpEdge : tmpNode.getNode().getConditions().getEdges()) {
                    if (tmpEdge.getFromNode().isSame(tmpEdge.getToNode())) outgoing--;
                }
                if (vv.getPickedState().isPicked(arg0)) return Color.YELLOW; else if (incoming < 1) return Color.GREEN; else if (outgoing < 1) return Color.BLACK; else return Color.RED;
            }
        });
        renderer.setEdgePaintFunction(new EdgePaintFunction() {

            @Override
            public Paint getDrawPaint(edu.uci.ics.jung.graph.Edge arg0) {
                if (vv.getPickedState().isPicked(arg0)) return Color.CYAN;
                return Color.BLACK;
            }

            @Override
            public Paint getFillPaint(edu.uci.ics.jung.graph.Edge arg0) {
                return EdgePaintFunction.TRANSPARENT;
            }
        });
        vv = new VisualizationViewer(layout, renderer);
        vv.setPickSupport(new ShapePickSupport());
        vv.setPickedState(new MultiPickedState());
        vv.setDoubleBuffered(true);
        vv.setToolTipListener(new MyToolTipAdapter());
        gMouse = new PluggableGraphMouse();
        gMouse.add(new PickingGraphMousePlugin());
        gMouse.add(new MyEditNodeGraphMousePlugin());
        gMouse.add(new MyCreateNodeGraphMousePlugin());
        vv.setGraphMouse(gMouse);
        extractGraph();
    }

    /**
	 * Create a graph out of the workflow
	 */
    private void extractGraph() {
        if (workflow != null) {
            Set<Node> tmpSet = workflow.getNodes();
            for (Node tmpNode : tmpSet) {
                addNodeToPosition(new INode(tmpNode), (double) tmpNode.getX(), (double) tmpNode.getY());
            }
            for (INode tmpINode : globalNodeList) {
                Node tmpNode = tmpINode.getNode();
                List<Edge> tmpEdgeList = tmpNode.getConditions().getEdges();
                for (Edge tmpEdge : tmpEdgeList) {
                    addEdge(tmpEdge.getFromNode(), tmpEdge.getToNode(), tmpEdge.getDescription(), true);
                }
            }
        }
    }

    /**
	 * Like method name says the propose of this method is to add a node
	 */
    protected void addNode(Node node) {
        addNode(new INode(node));
    }

    protected void addNode(INode node) {
        addNodeToPosition(node, null);
    }

    protected void addNodeToPosition(Node node, Point2D position) {
        addNodeToPosition(new INode(node), position);
    }

    protected void addNodeToPosition(INode node, Point2D position) {
        if (position == null) addNodeToPosition(node, 0.0, 0.0); else addNodeToPosition(node, position.getX(), position.getY());
    }

    protected void addNodeToPosition(INode node, double x, double y) {
        for (Iterator iterator = layout.getVertexIterator(); iterator.hasNext(); ) {
            layout.lockVertex((Vertex) iterator.next());
        }
        globalNodeList.add((INode) graph.addVertex(node));
        if (x != 0.0 && x != 0.0) layout.forceMove(node, x, y);
        for (Iterator iterator = layout.getVertexIterator(); iterator.hasNext(); ) {
            layout.unlockVertex((Vertex) iterator.next());
        }
    }

    /**
	 * With the help of this method you could add a new edge to the graph
	 */
    protected void addEdge(Node fromNode, Node toNode, String edgeDiscription, boolean init) {
        INode fromINode = null, toINode = null;
        IEdge createdEdge = null;
        for (INode tmpNode : globalNodeList) {
            if (tmpNode.getNode().isSimilar(fromNode)) {
                fromINode = tmpNode;
                break;
            }
        }
        for (INode tmpNode : globalNodeList) {
            if (tmpNode.getNode().isSimilar(toNode)) {
                toINode = tmpNode;
                break;
            }
        }
        if (fromINode != null && toINode != null) {
            createdEdge = new IEdge(fromINode, toINode, edgeDiscription);
            try {
                boolean found = false;
                for (IEdge tmpEdge : globalEdgeList) {
                    if (tmpEdge.getEdge().isSame(createdEdge.getEdge())) {
                        System.out.println("AddEdge: Found duplication");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    if (!init) {
                        for (INode tmpNode : globalNodeList) {
                            if (tmpNode.getNode().isSame(fromNode)) {
                                System.out.println("AddEdge: adding CONDITION!! " + tmpNode.getNode().getId());
                                tmpNode.getNode().getConditions().addEdge(createdEdge.getEdge());
                                break;
                            }
                        }
                    }
                    globalEdgeList.add((IEdge) graph.addEdge(createdEdge));
                }
            } catch (ConstraintViolationException e) {
                System.out.println("AddEdge: JUNG CVE Exception:");
                System.out.println(PredicateUtils.evaluateNestedPredicates(e.getViolatedConstraint(), null));
            }
        } else {
            if (fromINode == null) System.out.println("AddEdge: fromNode is NULL");
            if (toINode == null) System.out.println("AddEdge: toNode is NULL");
        }
    }

    protected void addEdge(Node fromNode, Node toNode, String edgeDiscription) {
        addEdge(fromNode, toNode, edgeDiscription, false);
    }

    protected void addEdge(Edge edge) {
        addEdge(edge.getFromNode(), edge.getToNode(), edge.getDescription());
    }

    protected void addEdge(IEdge edge) {
        addEdge(edge.getEdge());
    }

    /**
	 * Here you could delete selected Node and Edges
	 */
    protected void deleteNodeAndEdges() {
        Set<DirectedEdge> edgeToDel = vv.getPickedState().getPickedEdges();
        for (Iterator<DirectedEdge> iter = edgeToDel.iterator(); iter.hasNext(); ) {
            DirectedEdge directedEdge = iter.next();
            deleteEdge((IEdge) directedEdge);
        }
        Set<Vertex> vertToDel = vv.getPickedState().getPickedVertices();
        for (Iterator<Vertex> iter = vertToDel.iterator(); iter.hasNext(); ) {
            Vertex vertex = iter.next();
            deleteNode((INode) vertex);
        }
    }

    protected void deleteNode(Node node) {
        for (Object tmpNode : graph.getVertices()) {
            if (((INode) tmpNode).getNode().isSame(node)) {
                deleteNode((INode) tmpNode);
                break;
            }
        }
    }

    protected void deleteNode(INode toDeleteNode) {
        List<Edge> tmpEdgeList = toDeleteNode.getNode().getConditions().getEdges();
        List<IEdge> toRemoveIncEdges = new LinkedList<IEdge>();
        for (Edge tmpEdge : tmpEdgeList) {
            for (IEdge tmpIEdge : globalEdgeList) {
                if (tmpIEdge.getEdge().isSimilar(tmpEdge)) {
                    toRemoveIncEdges.add(tmpIEdge);
                }
            }
        }
        for (IEdge tmpEdge : toRemoveIncEdges) {
            deleteEdge(tmpEdge);
        }
        LinkedList<IEdge> toRemoveOutEdges = new LinkedList<IEdge>();
        for (IEdge tmpEdge : globalEdgeList) {
            if (tmpEdge.getEdge().getToNode().isSame(toDeleteNode.getNode())) toRemoveOutEdges.add(tmpEdge);
        }
        for (IEdge tmpEdge : toRemoveOutEdges) {
            deleteEdge(tmpEdge);
        }
        graph.removeVertex(toDeleteNode);
        globalNodeList.remove(toDeleteNode);
    }

    protected void deleteEdge(IEdge toDeleteEdge) {
        System.out.println("DeleteEdge deleting Edge from: " + toDeleteEdge.getEdge().getFromNode().getId() + " to " + toDeleteEdge.getEdge().getToNode().getId());
        INode tmpSourceNode = (INode) toDeleteEdge.getSource();
        for (INode tmpNode : globalNodeList) {
            if (tmpNode.getNode().isSame(tmpSourceNode.getNode())) {
                System.out.println("DeleteEdge deleting condition from " + tmpNode.getNode().getId());
                System.out.println(tmpNode.getNode().getConditions().getEdgeCount());
                System.out.println(tmpNode.getNode().getConditions().removeEdge(toDeleteEdge.getEdge()));
                System.out.println(tmpNode.getNode().getConditions().getEdgeCount());
            }
        }
        graph.removeEdge(toDeleteEdge);
        globalEdgeList.remove(toDeleteEdge);
    }

    protected void openNodeEditor() {
        openNodeEditor(Mode.CREATE, null);
    }

    protected void openNodeEditor(final Mode mode, final Node node) {
        openNodeEditor(mode, node, null);
    }

    protected void openNodeEditor(Point2D position) {
        openNodeEditor(Mode.CREATE, null, position);
    }

    protected void openNodeEditor(final Mode mode, final Node oldNode, final Point2D position) {
        display.syncExec(new Runnable() {

            public void run() {
                GDEditNode gden = new GDEditNode(mode, oldNode, vv, uManager);
                shell.setEnabled(false);
                Node newNode = gden.open();
                if (newNode != null && oldNode != null) {
                    if (newNode.isSame(oldNode)) {
                        System.out.println("OpenNodeEditor: " + oldNode.getConditions().getEdgeCount());
                        System.out.println("OpenNodeEditor: " + newNode.getConditions().getEdgeCount());
                        System.out.println("Return node is same, do nothing");
                    } else if (oldNode.getId() == newNode.getId()) {
                        System.out.println("Substitute returning node!");
                        LinkedList<Edge> incNode = new LinkedList<Edge>();
                        for (IEdge tmpEdge : globalEdgeList) {
                            if (tmpEdge.getEdge().getToNode().isSame(oldNode)) {
                                if (tmpEdge.getEdge().getFromNode().isSame(oldNode)) {
                                    incNode.add(new Edge(tmpEdge.getEdge().getDescription(), newNode, newNode));
                                } else {
                                    incNode.add(tmpEdge.getEdge());
                                }
                            }
                        }
                        Point2D oldPosition = null;
                        for (INode tmpNode : globalNodeList) {
                            if (tmpNode.getNode().isSame(oldNode)) {
                                System.out.println("Saving the OLDPOSITION");
                                oldPosition = layout.getLocation(tmpNode);
                                if (oldPosition == null) System.out.println("OLDPOSITIOIN is NULL!");
                                break;
                            }
                        }
                        deleteNode(oldNode);
                        addingCompleteNode(newNode, oldPosition);
                        for (Edge tmpEdge : incNode) {
                            addEdge(tmpEdge.getFromNode(), newNode, tmpEdge.getDescription());
                        }
                    } else {
                        System.out.println("Adding returning node!");
                        addingCompleteNode(newNode, position);
                    }
                } else if (newNode != null) {
                    addingCompleteNode(newNode, position);
                } else {
                    System.out.println("Node is null");
                }
                shell.setEnabled(true);
                shell.setActive();
            }
        });
    }

    protected void addingCompleteNode(Node node, Point2D position) {
        if (position != null) addNodeToPosition(node, position); else addNode(node);
        for (Edge tmpEdge : node.getConditions().getEdges()) {
            addEdge(tmpEdge.getFromNode(), tmpEdge.getToNode(), tmpEdge.getDescription(), true);
        }
    }

    protected Workflow completeCurrentWorkflow() {
        LinkedList<INode> startNodeList = new LinkedList<INode>();
        for (INode tmpNode : globalNodeList) {
            if (tmpNode.getInEdges().size() == 0) startNodeList.add(tmpNode);
        }
        if (startNodeList.size() != 1) {
            System.out.println("Error, workflow got more then one potential start node!");
            return null;
        }
        Node startNode = startNodeList.get(0).getNode();
        startNode.setW(workflow);
        workflow.setStartnode(startNode);
        for (INode tmpNode : globalNodeList) {
            tmpNode.getNode().setW(workflow);
            Point2D nodePosition = layout.getLocation(tmpNode);
            tmpNode.getNode().setX((int) nodePosition.getX());
            tmpNode.getNode().setY((int) nodePosition.getY());
        }
        if (workflow.getNodes().size() != globalNodeList.size()) {
            System.out.println("Size check failed! Graph nodes and workflow nodes are not equal!");
            return null;
        }
        return workflow;
    }

    class INode extends DirectedSparseVertex {

        Node node = null;

        public INode(Node node) {
            super();
            this.node = node;
        }

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
        }
    }

    class IEdge extends DirectedSparseEdge {

        Edge edge = null;

        public IEdge(INode from, INode to, String description) {
            super(from, to);
            edge = new Edge(description, from.getNode(), to.getNode());
        }

        public Edge getEdge() {
            return edge;
        }

        public void setEdge(Edge edge) {
            this.edge = edge;
        }
    }

    class MyToolTipAdapter implements ToolTipFunction {

        @Override
        public String getToolTipText(Vertex arg0) {
            Node tmpVertex = ((INode) arg0).getNode();
            return "<html><b>Knotenname: </b>" + tmpVertex.getName() + "<br><b>Gruppe: </b>" + tmpVertex.getGroup().getName() + "</html>";
        }

        @Override
        public String getToolTipText(edu.uci.ics.jung.graph.Edge arg0) {
            Edge tmpEdge = ((IEdge) arg0).getEdge();
            return "<html><b>Anfangsknoten: </b>" + tmpEdge.getFromNode().getName() + "<br><b>Endknoten: </b>" + tmpEdge.getToNode().getName() + "</html>";
        }

        @Override
        public String getToolTipText(java.awt.event.MouseEvent arg0) {
            return null;
        }
    }

    class MyEditNodeGraphMousePlugin extends AbstractGraphMousePlugin implements MouseListener {

        public MyEditNodeGraphMousePlugin() {
            this(InputEvent.BUTTON1_MASK | InputEvent.CTRL_MASK);
        }

        public MyEditNodeGraphMousePlugin(int modifiers) {
            super(modifiers);
        }

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getModifiers() == modifiers) {
                VisualizationViewer vv = (VisualizationViewer) e.getSource();
                PickSupport pickSupport = vv.getPickSupport();
                PickedState pickedState = vv.getPickedState();
                if (pickSupport != null && pickedState != null) {
                    Point2D clickPoint = vv.inverseViewTransform(e.getPoint());
                    Vertex vertex = pickSupport.getVertex(clickPoint.getX(), clickPoint.getY());
                    if (vertex != null) {
                        openNodeEditor(Mode.EDIT, ((INode) vertex).getNode());
                    }
                }
                e.consume();
            }
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent arg0) {
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent arg0) {
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent arg0) {
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent arg0) {
        }
    }

    class MyCreateNodeGraphMousePlugin extends AbstractGraphMousePlugin implements MouseListener {

        public MyCreateNodeGraphMousePlugin() {
            this(InputEvent.BUTTON1_MASK | InputEvent.SHIFT_MASK);
        }

        public MyCreateNodeGraphMousePlugin(int modifiers) {
            super(modifiers);
        }

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getModifiers() == modifiers) {
                VisualizationViewer vv = (VisualizationViewer) e.getSource();
                PickSupport pickSupport = vv.getPickSupport();
                PickedState pickedState = vv.getPickedState();
                if (pickSupport != null && pickedState != null) {
                    Point2D clickPoint = vv.inverseViewTransform(e.getPoint());
                    Vertex vertex = pickSupport.getVertex(clickPoint.getX(), clickPoint.getY());
                    if (vertex == null) {
                        openNodeEditor(clickPoint);
                    }
                }
                e.consume();
            }
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent arg0) {
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent arg0) {
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent arg0) {
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent arg0) {
        }
    }
}
