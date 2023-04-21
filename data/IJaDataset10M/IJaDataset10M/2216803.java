package ontorama.model.tree.controller;

import ontorama.model.tree.events.TreeNodeSelectedEvent;
import ontorama.model.tree.TreeNode;
import ontorama.model.tree.TreeNodeImpl;
import ontorama.model.tree.TreeView;
import ontorama.model.graph.events.NodeSelectedEvent;
import org.tockit.events.EventBroker;
import org.tockit.events.Event;
import org.tockit.events.EventBrokerListener;

/**
 * Created by IntelliJ IDEA.
 * User: nataliya
 * Date: Dec 2, 2002
 * Time: 10:13:22 AM
 * To change this template use Options | File Templates.
 */
public class TreeViewFocusEventHandler implements EventBrokerListener {

    private TreeView _treeView;

    private EventBroker _eventBroker;

    public TreeViewFocusEventHandler(EventBroker eventBroker, TreeView treeView) {
        _treeView = treeView;
        _eventBroker = eventBroker;
        eventBroker.subscribe(this, TreeNodeSelectedEvent.class, TreeNode.class);
    }

    public void processEvent(Event e) {
        TreeNode node = (TreeNode) e.getSubject();
        _treeView.focus(node);
        if (node instanceof TreeNodeImpl) {
            TreeNodeImpl tn = (TreeNodeImpl) node;
            _eventBroker.processEvent(new NodeSelectedEvent(tn.getGraphNode()));
        }
    }
}
