package org.poset.client;

import org.poset.model.DomainObject;
import org.poset.model.Node;
import org.poset.model.UpdateListener;
import com.google.gwt.user.client.ui.TreeItem;

public class NodeItem extends TreeItem implements UpdateListener {

    private Node node = null;

    private boolean childrenAdded = false;

    private boolean placeholder = false;

    public NodeItem(Node node) {
        this.node = node;
        setText(node.getReference());
        node.addUpdateListener(this);
    }

    public NodeItem(String uidShort, String description) {
        node = Node.fromUidShort(uidShort);
        setText(description);
    }

    public NodeItem(String text) {
        placeholder = true;
        setHTML(text);
    }

    public boolean getChildrenAdded() {
        return childrenAdded;
    }

    public Node getNode() {
        return node;
    }

    public String getUidShort() {
        return node.getUIDShort();
    }

    public boolean isPlaceholder() {
        return placeholder;
    }

    public void onUpdate(DomainObject object) {
        node = (Node) object;
        setText(node.getReference());
    }

    public void setChildrenAdded(boolean childrenAdded) {
        this.childrenAdded = childrenAdded;
    }
}
