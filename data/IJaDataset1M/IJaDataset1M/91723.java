package net.sf.magicmap.client.model.node;

/**
 * Created by IntelliJ IDEA.
 * User: jan
 * Date: 14.01.2007
 * Time: 16:27:48
 * To change this template use File | Settings | File Templates.
 */
public interface INodeSelectionModel {

    void addNodeModelSelectionListener(INodeModelSelectionListener l);

    void removeNodeModelSelectionListener(INodeModelSelectionListener l);

    /**
	 *
	 * @param selectedNode the node to select (NEVER null please)
	 * @return the selected node.
	 */
    Node selectNode(Node selectedNode);

    Node getSelectedNode();
}
