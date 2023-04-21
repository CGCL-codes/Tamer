package de.tud.eclipse.plugins.controlflow.view.details;

import java.util.List;
import java.util.Set;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import de.tud.eclipse.plugins.controlflow.model.ICFlowConnection;
import de.tud.eclipse.plugins.controlflow.model.ICFlowNode;

public class ConnectionDetailsTree {

    private ICFlowNode model;

    public ConnectionDetailsTree(Composite parent, Object layoutData, ICFlowNode model) {
        this.model = model;
        final Tree tree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL);
        tree.setLayoutData(layoutData);
        TreeItem incomingItem = new TreeItem(tree, 0);
        incomingItem.setText("Incoming connections");
        List<ICFlowConnection> incoming = model.getInConnections();
        createTreeFor(incomingItem, incoming, "from ");
        TreeItem outgoingItem = new TreeItem(tree, 0);
        outgoingItem.setText("Outgoing connections");
        List<ICFlowConnection> outgoing = model.getOutConnections();
        createTreeFor(outgoingItem, outgoing, "to ");
    }

    private void createTreeFor(TreeItem parentItem, List<ICFlowConnection> incoming, String prefix) {
        for (ICFlowConnection c : incoming) {
            parentItem.setExpanded(true);
            ICFlowNode endpoint = null;
            if (c.getSourceNode() == this.model) endpoint = c.getTargetNode(); else if (c.getTargetNode() == this.model) endpoint = c.getSourceNode();
            TreeItem connectionItem = new TreeItem(parentItem, 0);
            connectionItem.setText(prefix + (endpoint != null ? endpoint.getLabel() : "Undefined"));
            Set<String> keys = c.getAllAttributeKeys();
            for (String key : keys) {
                TreeItem attr = new TreeItem(connectionItem, 0);
                attr.setText(key + ": " + c.getValueForAttributeKey(key));
            }
        }
    }
}
