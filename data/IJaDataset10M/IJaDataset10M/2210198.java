package ontorama.tree.view;

import ontorama.controller.NodeSelectedEvent;
import ontorama.controller.QueryEvent;
import ontorama.graph.controller.*;
import ontorama.graph.view.GraphView;
import ontorama.graph.view.GraphQuery;
import ontorama.model.Graph;
import ontorama.model.GraphNode;
import ontorama.tree.model.OntoTreeBuilder;
import ontorama.tree.model.OntoTreeModel;
import ontorama.tree.model.OntoTreeNode;
import ontorama.util.Debug;
import org.tockit.events.EventBroker;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class OntoTreeView extends JScrollPane implements KeyListener, MouseListener, TreeSelectionListener, GraphView {

    private JTree tree;

    private Debug debug = new Debug(false);

    private EventBroker eventBroker;

    private Graph graph;

    private boolean KEY_IS_PRESSED = false;

    private boolean MOUSE_IS_PRESSED = false;

    private int pressedKey = -1;

    private int pressedMouseButton = -1;

    private KeyEvent curKeyEvent = null;

    private MouseEvent curMouseEvent = null;

    /**
     * Constructor
     */
    public OntoTreeView(EventBroker eventBroker) {
        super();
        this.eventBroker = eventBroker;
        new GraphViewFocusEventHandler(eventBroker, this);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
        }
    }

    /**
     *
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
        OntoTreeModel ontoTreeModel = new OntoTreeModel(graph);
        this.tree = new JTree(ontoTreeModel);
        this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.tree.setEditable(false);
        this.tree.putClientProperty("JTree.lineStyle", "Angled");
        this.tree.addMouseListener(this);
        this.tree.addKeyListener(this);
        ToolTipManager.sharedInstance().registerComponent(this.tree);
        this.tree.setCellRenderer(new OntoTreeRenderer());
        Iterator it = ontoTreeModel.getOntoTreeIterator();
        while (it.hasNext()) {
            OntoTreeNode node = (OntoTreeNode) it.next();
            GraphNode curGraphNode = node.getGraphNode();
            TreePath path = node.getTreePath();
            if (!curGraphNode.getFoldedState()) {
                this.tree.expandPath(path);
            }
        }
        setViewportView(tree);
        tree.putClientProperty("JTree.lineStyle", "Angled");
        this.tree.setScrollsOnExpand(true);
        repaint();
        this.tree.repaint();
    }

    /**
     * Implementation of valueChanged needed for interface
     * implementation of TreeSelectionListener.
     * Overriten here so we can control single mouse clicks behaviour ourselves.
     * @param   e - TreeSelectionEvent
     */
    public void valueChanged(TreeSelectionEvent e) {
        OntoTreeNode node = (OntoTreeNode) tree.getLastSelectedPathComponent();
        debug.message("TreeSelectionListener ******** node " + node);
        System.out.println("TreeSelectionListener ******** node " + node);
        return;
    }

    /**
     *
     */
    public void keyPressed(KeyEvent e) {
        this.KEY_IS_PRESSED = true;
        this.pressedKey = e.getModifiers();
        this.curKeyEvent = e;
    }

    /**
     *
     */
    public void keyReleased(KeyEvent e) {
        this.pressedKey = -1;
        this.KEY_IS_PRESSED = false;
        this.curKeyEvent = null;
        System.out.println("... key event = " + e.getModifiers());
        if (this.KEY_IS_PRESSED) {
            notifyMouseKeyEvent(e, this.curMouseEvent);
            return;
        }
    }

    /**
     *
     */
    public void keyTyped(KeyEvent e) {
        debug.message("keyTyped, key char " + e.getKeyChar() + ", key code" + e.getKeyCode() + ", keyText = " + e.getKeyText(e.getKeyCode()));
    }

    /**
     *
     */
    public void mousePressed(MouseEvent e) {
        this.curMouseEvent = e;
        this.MOUSE_IS_PRESSED = true;
        this.pressedMouseButton = e.getModifiers();
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        if (selRow == -1) {
            return;
        }
        if (e.getClickCount() == 1) {
            debug.message("mousePressed, single click,  row=" + selRow);
            if (this.KEY_IS_PRESSED) {
                notifyMouseKeyEvent(this.curKeyEvent, e);
            } else {
                GraphNode graphNode = getGraphNodeFromMouseEvent(e);
                if (graphNode == null) return;
                eventBroker.processEvent(new NodeSelectedEvent(graphNode));
            }
        }
    }

    /**
     *
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     *
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     *
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     *
     */
    public void mouseReleased(MouseEvent e) {
        this.pressedMouseButton = -1;
        this.MOUSE_IS_PRESSED = false;
        this.curMouseEvent = null;
    }

    /**
     *
     */
    private void notifyMouseKeyEvent(KeyEvent keyEvent, MouseEvent mouseEvent) {
        GraphNode graphNode = getGraphNodeFromMouseEvent(mouseEvent);
        if (graphNode == null) return;
        int keyEventCode = keyEvent.getModifiers();
        int mouseEventCode = mouseEvent.getModifiers();
        System.out.println("notifyMouseKeyEvent, Event.META_MASK = " + Event.META_MASK + ", mouseEventCode = " + mouseEventCode);
        if (keyEventCode == InputEvent.CTRL_MASK) {
            eventBroker.processEvent(new QueryEvent(graphNode));
        }
    }

    /**
     * 
     */
    private GraphNode getGraphNodeFromMouseEvent(MouseEvent mouseEvent) {
        TreePath selPath = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
        if (selPath == null) {
            return null;
        }
        return getGraphNodeFromTreePath(selPath);
    }

    /**
	 * 
	 */
    private GraphNode getGraphNodeFromTreePath(TreePath selPath) {
        OntoTreeNode treeNode = (OntoTreeNode) selPath.getLastPathComponent();
        GraphNode graphNode = treeNode.getGraphNode();
        return graphNode;
    }

    /**
     * @todo	shouldn't need to check if treeNode == null. This is a hack! this should be fixed in graphBuilder
     */
    public void focus(GraphNode node) {
        OntoTreeNode treeNode = (OntoTreeNode) OntoTreeBuilder.getTreeNode(node);
        TreePath path = treeNode.getTreePath();
        this.tree.setSelectionPath(path);
        this.tree.scrollPathToVisible(path);
    }

    public void repaint() {
        super.repaint();
    }
}
