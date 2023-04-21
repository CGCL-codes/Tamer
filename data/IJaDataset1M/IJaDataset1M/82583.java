package gov.sns.apps.bricks;

import gov.sns.application.*;
import gov.sns.tools.apputils.iconlib.*;
import gov.sns.tools.bricks.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.Point;
import javax.swing.event.*;
import java.util.*;
import java.beans.*;

/**
 * BuilderWindow
 * @author  tap
 */
class BricksWindow extends XalWindow implements SwingConstants, BrickListener {

    /** tree model of view nodes */
    protected final DefaultTreeModel VIEW_NODE_TREE_MODEL;

    /** the tree of views */
    protected final JTree VIEW_TREE;

    /** root brick */
    protected final RootBrick ROOT_BRICK;

    /** code assistant for generating code snippets */
    protected CodeAssistant _codeAssistant;

    /** Constructor */
    public BricksWindow(final BricksDocument aDocument, final RootBrick rootBrick) {
        super(aDocument);
        setSize(500, 400);
        ROOT_BRICK = rootBrick;
        final DefaultMutableTreeNode rootNode = ROOT_BRICK.getTreeNode();
        ROOT_BRICK.addBrickListener(this);
        rootNode.setAllowsChildren(true);
        VIEW_NODE_TREE_MODEL = new DefaultTreeModel(rootNode);
        VIEW_TREE = new JTree(VIEW_NODE_TREE_MODEL);
        _codeAssistant.setViewTree(VIEW_TREE);
        makeContent();
        ViewPalette.displayDefaultPaletteAbout(this);
    }

    /** Make the content for the window. */
    protected void makeContent() {
        final Box mainView = new Box(BoxLayout.Y_AXIS);
        getContentPane().add(mainView);
        VIEW_TREE.setDragEnabled(true);
        VIEW_TREE.setTransferHandler(new ViewNodesTransferHandler());
        final DropTarget dropTarget = new DropTarget();
        try {
            dropTarget.addDropTargetListener(new TreeDropHandler());
            VIEW_TREE.setDropTarget(dropTarget);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        VIEW_TREE.setShowsRootHandles(true);
        mainView.add(new JScrollPane(VIEW_TREE));
        VIEW_TREE.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(final TreeSelectionEvent event) {
                if (ViewInspector.getInspector().isVisible()) {
                    inspectSelection(VIEW_TREE);
                }
            }
        });
        VIEW_TREE.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent event) {
                if (event.getClickCount() == 2) {
                    displaySelection(VIEW_TREE);
                }
            }
        });
    }

    /**
	 * Register actions for the custom menu items.
     * @param commander The commander with which to register the custom commands.
     */
    @Override
    protected void customizeCommands(final Commander commander) {
        final Action inspectorAction = new AbstractAction("inspect-view", IconLib.getIcon("general", "Information24.gif")) {

            public void actionPerformed(final ActionEvent event) {
                inspectSelection(VIEW_TREE);
            }
        };
        commander.registerAction(inspectorAction);
        final Action displayViewPaletteAction = new AbstractAction("display-view-palette", IconLib.getIcon("development", "Bean24.gif")) {

            public void actionPerformed(final ActionEvent event) {
                ViewPalette.displayDefaultPaletteAbout(BricksWindow.this);
            }
        };
        commander.registerAction(displayViewPaletteAction);
        _codeAssistant = new CodeAssistant();
        _codeAssistant.registerActions(commander);
    }

    /** Dispose of custom window resources. */
    @Override
    protected void freeCustomResources() {
        ROOT_BRICK.disposeAllWindows();
    }

    /** get the root brick */
    public RootBrick getRootBrick() {
        return ROOT_BRICK;
    }

    /** get the selected bean node */
    private BeanNode getSelectedBeanNode() {
        return TreeUtility.getSelectedBeanNode(VIEW_TREE);
    }

    /** get the selected bean nodes */
    private BeanNode[] getSelectedBeanNodes() {
        return TreeUtility.getSelectedBeanNodes(VIEW_TREE);
    }

    /** inspect the selected item */
    private void inspectSelection(final JTree tree) {
        final BeanNode beanNode = TreeUtility.getSelectedBeanNode(tree);
        if (beanNode != null) {
            ViewInspector.inspectBeanNear(beanNode, this);
        }
    }

    /** display the selected item */
    private void displaySelection(final JTree tree) {
        final BeanNode beanNode = TreeUtility.getSelectedBeanNode(tree);
        if (beanNode != null) {
            beanNode.display();
        }
    }

    /**
	 * Handle the event in which nodes have been added to a container
	 * @param source the source of the event
	 * @param container the node to which nodes have been added
	 * @param nodes the nodes which have been added
	 */
    public void nodesAdded(final Object source, final Brick container, final List<? extends BeanNode<?, ?>> nodes) {
    }

    /**
	 * Handle the event in which nodes have been removed from a container
	 * @param source the source of the event
	 * @param container the node from which nodes have been removed
	 * @param nodes the nodes which have been removed
	 */
    public void nodesRemoved(final Object source, final Brick container, final List<? extends BeanNode<?, ?>> nodes) {
    }

    /**
	 * Handle the event in which a bean's property has been changed
	 * @param beanNode the node whose property has changed
	 * @param propertyDescritpr the property which has changed
	 * @param value the new value
	 */
    public void propertyChanged(final BeanNode<?, ?> beanNode, final PropertyDescriptor propertyDescriptor, final Object value) {
    }

    /**
	 * Handle the event in which a brick's tree path needs refresh
	 * @param source the source of the event
	 * @param brick the brick at which the refresh needs to be done
	 */
    public void treeNeedsRefresh(final Object source, final Brick brick) {
        VIEW_NODE_TREE_MODEL.reload(brick.getTreeNode());
    }

    /** process the dropping of a view node */
    protected static boolean processViewNodeDrop(final TreePath treePath, final List<? extends BeanNode<?, ?>> nodes) throws Exception {
        if (treePath != null) {
            final DefaultMutableTreeNode dropNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            final Object dropComponent = dropNode.getUserObject();
            if (dropComponent instanceof ViewNodeContainer) {
                final ViewNodeContainer target = (ViewNodeContainer) dropComponent;
                if (target.canAddAllNodes(nodes)) {
                    target.addNodes(nodes);
                    return true;
                } else if (target.canAllNodesBeSiblings(nodes)) {
                    target.insertSiblingNodes(nodes);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /** handle drop events on the tree of nodes */
    protected class TreeDropHandler extends DropTargetAdapter {

        @Override
        public void dragEnter(final DropTargetDragEvent event) {
        }

        public void drop(final DropTargetDropEvent event) {
            try {
                final Transferable transferable = event.getTransferable();
                if (transferable.isDataFlavorSupported(ViewNodeTransferable.viewNodeFlavor)) {
                    processViewNodeDrop(event);
                } else if (transferable.isDataFlavorSupported(ViewTransferable.viewFlavor)) {
                    processViewDrop(event);
                } else {
                    System.out.println("Transferable not supported:  " + transferable);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        /** process the dropping of a view node */
        void processViewNodeDrop(final DropTargetDropEvent event) throws Exception {
            final JTree tree = (JTree) event.getDropTargetContext().getComponent();
            final List<? extends BeanNode<?, ?>> nodes = (List<? extends BeanNode<?, ?>>) event.getTransferable().getTransferData(ViewNodeTransferable.viewNodeFlavor);
            final Point location = event.getLocation();
            final TreePath treePath = tree.getClosestPathForLocation(location.x, location.y);
            if (BricksWindow.processViewNodeDrop(treePath, nodes)) {
                event.dropComplete(true);
            } else {
                event.dropComplete(false);
            }
        }

        /** process the dropping of a view */
        void processViewDrop(final DropTargetDropEvent event) throws Exception {
            final JTree tree = (JTree) event.getDropTargetContext().getComponent();
            final List<? extends BeanProxy<?>> views = (List<? extends BeanProxy<?>>) event.getTransferable().getTransferData(ViewTransferable.viewFlavor);
            final Point location = event.getLocation();
            final TreePath treePath = tree.getClosestPathForLocation(location.x, location.y);
            if (treePath != null) {
                final DefaultMutableTreeNode dropNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                final Object dropComponent = dropNode.getUserObject();
                if (dropComponent instanceof ViewNodeContainer) {
                    final ViewNodeContainer target = (ViewNodeContainer) dropComponent;
                    if (target.canAddAll(views)) {
                        target.add(views);
                        event.dropComplete(true);
                    } else if (target.canAllBeSiblings(views)) {
                        target.insertSiblings(views);
                        event.dropComplete(true);
                    } else {
                        event.dropComplete(false);
                    }
                }
            } else {
                event.dropComplete(false);
            }
        }
    }

    /** View nodes transfer handler */
    class ViewNodesTransferHandler extends TransferHandler {

        /** transfer view nodes */
        @Override
        protected Transferable createTransferable(final JComponent component) {
            final JTree nodeTree = (JTree) component;
            final TreePath[] selections = nodeTree.getSelectionPaths();
            final List<BeanNode<?, ?>> nodes = new ArrayList<BeanNode<?, ?>>(selections.length);
            for (final TreePath path : selections) {
                final BeanNode<?, ?> node = (BeanNode<?, ?>) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                nodes.add(node);
            }
            return new ViewNodeTransferable(nodes);
        }

        /** provides copy or move operation */
        @Override
        public int getSourceActions(final JComponent component) {
            return COPY_OR_MOVE;
        }

        /** determine if we can import at least one of the tranferable flavors */
        @Override
        public boolean canImport(final JComponent component, final DataFlavor[] flavors) {
            for (DataFlavor flavor : flavors) {
                if (flavor == ViewNodeTransferable.viewNodeFlavor) return true;
            }
            return false;
        }

        /** import the transferable */
        @Override
        public boolean importData(final JComponent component, final Transferable transferable) {
            try {
                final JTree nodeTree = (JTree) component;
                final TreePath treePath = nodeTree.getSelectionPath();
                final List<? extends BeanNode<?, ?>> nodes = (List<? extends BeanNode<?, ?>>) transferable.getTransferData(ViewNodeTransferable.viewNodeFlavor);
                return BricksWindow.processViewNodeDrop(treePath, nodes);
            } catch (UnsupportedFlavorException exception) {
                exception.printStackTrace();
                return false;
            } catch (java.io.IOException exception) {
                exception.printStackTrace();
                return false;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }

        /** perform cleanup operations */
        @Override
        protected void exportDone(final JComponent component, Transferable transferable, int action) {
            switch(action) {
                case TransferHandler.MOVE:
                    if (transferable != null) {
                        try {
                            final List<BeanNode> nodes = (List<BeanNode>) transferable.getTransferData(ViewNodeTransferable.viewNodeFlavor);
                            for (final BeanNode node : nodes) {
                                node.removeFromParent();
                            }
                        } catch (java.awt.datatransfer.UnsupportedFlavorException exception) {
                            exception.printStackTrace();
                        } catch (java.io.IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
