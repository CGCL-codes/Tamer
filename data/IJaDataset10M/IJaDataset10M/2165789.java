package gov.sns.tools.bricks;

import java.beans.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import gov.sns.tools.data.*;

/** root brick to which windows are added */
public class RootBrick extends Brick implements ViewNodeContainer, DataListener, BrickListener {

    /** data label */
    public static final String DATA_LABEL = "RootBrick";

    /** list of window nodes */
    final List<ViewNode<?, ?>> windowNodes;

    /** Constructor */
    public RootBrick() {
        windowNodes = new ArrayList<ViewNode<?, ?>>();
    }

    /** dispose of all windows */
    public void disposeAllWindows() {
        for (final ViewNode<?, ?> node : windowNodes) {
            final Component window = node.getView();
            if (window instanceof Window) {
                ((Window) window).dispose();
            }
        }
    }

    /**
     * Determine if the brick can add the specified view
     * @return true if it can add the specified view and false if not
     */
    @Override
    public boolean canAdd(final BeanProxy<?> beanProxy) {
        if (beanProxy instanceof ViewProxy) {
            return ((ViewProxy<?>) beanProxy).isWindow();
        } else {
            return false;
        }
    }

    /**
     * Get the label
     * @return the label for this brick
     */
    @Override
    public String toString() {
        return "windows";
    }

    /**
     * Add the views to this node
     * @param beanProxies the views to add to this node
     */
    public void add(final List<? extends BeanProxy<?>> beanProxies) {
        final List<BeanNode<?, ?>> nodes = new ArrayList<BeanNode<?, ?>>(beanProxies.size());
        for (final BeanProxy<?> beanProxy : beanProxies) {
            if (beanProxy instanceof ViewProxy) {
                final ViewNode<?, ?> node = ViewNode.getInstance((ViewProxy<?>) beanProxy);
                windowNodes.add(node);
                node.addBrickListener(this);
                nodes.add(node);
                treeNode.add(node.getTreeNode());
                ((Window) node.getView()).setVisible(true);
            }
        }
        brickListener.nodesAdded(this, this, nodes);
        brickListener.treeNeedsRefresh(this, this);
    }

    /**
     * Get the tree index offset from the view index
     * @return the tree index offset
     */
    public int getTreeIndexOffsetFromViewIndex() {
        return 0;
    }

    /**
     * Insert the bean nodes in this node beginning at the specified index
     * @param nodes the nodes to add to this node
     * @param viewIndex the initial index at which to begin inserting the nodes
     */
    public void insertViewNode(final ViewNode<?, ?> node, final int viewIndex) {
        windowNodes.add(viewIndex, node);
        node.addBrickListener(this);
        treeNode.insert(node.getTreeNode(), viewIndex);
        ((Window) node.getView()).setVisible(true);
        brickListener.treeNeedsRefresh(this, this);
    }

    /**
     * Insert the views in this node beginning at the specified index
     * @param views the views to add to this node
     * @param startIndex the initial index at which to begin inserting the nodes
     */
    public void insertSiblings(final List<? extends BeanProxy<?>> viewProxies) {
    }

    /**
     * Add the views nodes to this node
     * @param originalNodes the nodes to add to this node
     */
    public void addNodes(final List<? extends BeanNode<?, ?>> originalNodes) {
        final List<BeanNode<?, ?>> nodes = new ArrayList<BeanNode<?, ?>>(originalNodes.size());
        for (final BeanNode<?, ?> originalNode : originalNodes) {
            if (originalNode instanceof ViewNode) {
                final ViewNode<?, ?> node = ((ViewNode<?, ?>) originalNode).copy();
                windowNodes.add(node);
                node.addBrickListener(this);
                nodes.add(node);
                treeNode.add(node.getTreeNode());
            }
        }
        brickListener.nodesAdded(this, this, nodes);
        brickListener.treeNeedsRefresh(this, this);
    }

    /**
     * Insert the view nodes in this node beginning at the specified index
     * @param nodes the nodes to add to this node
     * @param startIndex the initial index at which to begin inserting the nodes
     */
    public void insertSiblingNodes(final List<? extends BeanNode<?, ?>> originalNodes) {
    }

    /**
     * Remove the view node from this container
     * @param node the node to remove
     */
    public void removeNode(final BeanNode<?, ?> node) {
        removeNodes(Collections.singletonList(node));
    }

    /**
     * Remove the view nodes from this container
     * @param nodes the nodes to remove
     */
    public void removeNodes(final List<? extends BeanNode<?, ?>> nodes) {
        for (final BeanNode<?, ?> node : nodes) {
            if (node instanceof ViewNode) {
                final ViewNode<?, ?> viewNode = (ViewNode<?, ?>) node;
                viewNode.removeBrickListener(this);
                windowNodes.remove(viewNode);
                treeNode.remove(viewNode.getTreeNode());
                final Window window = (Window) viewNode.getView();
                window.dispose();
            }
        }
        brickListener.nodesRemoved(this, this, nodes);
        brickListener.treeNeedsRefresh(this, this);
    }

    /** Remove this brick from its parent */
    @Override
    public void removeFromParent() {
    }

    /** 
     * Provides the name used to identify the class in an external data source.
     * @return a tag that identifies the receiver's type
     */
    public String dataLabel() {
        return DATA_LABEL;
    }

    /**
     * Update the data based on the information provided by the data provider.
     * @param adaptor The adaptor from which to update the data
     */
    public void update(final DataAdaptor adaptor) {
        final List<? extends DataAdaptor> nodeAdaptors = adaptor.childAdaptors(ViewNode.DATA_LABEL);
        final List<BeanNode<?, ?>> nodes = new ArrayList<BeanNode<?, ?>>(nodeAdaptors.size());
        for (final DataAdaptor nodeAdaptor : nodeAdaptors) {
            nodes.add(ViewNode.getInstance(nodeAdaptor));
        }
        addNodes(nodes);
    }

    /**
     * Write data to the data adaptor for storage.
     * @param adaptor The adaptor to which the receiver's data is written
     */
    public void write(final DataAdaptor adaptor) {
        adaptor.writeNodes(windowNodes);
    }

    /**
     * Handle the event in which nodes have been added to a container
     * @param source the source of the event
     * @param container the node to which nodes have been added
     * @param nodes the nodes which have been added
     */
    public void nodesAdded(final Object source, final Brick container, final List<? extends BeanNode<?, ?>> nodes) {
        brickListener.nodesAdded(this, container, nodes);
    }

    /**
     * Handle the event in which nodes have been removed from a container
     * @param source the source of the event
     * @param container the node from which nodes have been removed
     * @param nodes the nodes which have been removed
     */
    public void nodesRemoved(final Object source, final Brick container, final List<? extends BeanNode<?, ?>> nodes) {
        brickListener.nodesRemoved(this, container, nodes);
    }

    /**
     * Handle the event in which a bean's property has been changed
     * @param node the node whose property has changed
     * @param propertyDescritpr the property which has changed
     * @param value the new value
     */
    public void propertyChanged(final BeanNode<?, ?> node, final PropertyDescriptor propertyDescriptor, final Object value) {
        brickListener.propertyChanged(node, propertyDescriptor, value);
    }

    /**
     * Handle the event in which a brick's tree path needs refresh
     * @param source the source of the event
     * @param brick the brick at which the refresh needs to be done
     */
    public void treeNeedsRefresh(final Object source, final Brick brick) {
        brickListener.treeNeedsRefresh(this, brick);
    }
}
