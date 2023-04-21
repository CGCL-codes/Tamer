package org.onemind.swingweb.client.gwt.ui;

import java.util.List;
import org.onemind.swingweb.client.core.AbstractClient;
import org.onemind.swingweb.client.dom.DomNode;
import org.onemind.swingweb.client.gwt.widget.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;

public class TreeUIHandler extends ContainerUIHandler implements ClickListener, DoubleClickListener, TreeListener {

    public TreeUIHandler(AbstractClient client) {
        super(client);
    }

    protected void selectTab(SourcesTabEvents sender, int tabIndex) {
    }

    /** 
     * {@inheritDoc}
     */
    protected void registerListeners(Object com) {
        ((org.onemind.swingweb.client.gwt.widget.Tree) com).addTreeListener(this);
        ((org.onemind.swingweb.client.gwt.widget.Tree) com).addClickListener(this);
        ((org.onemind.swingweb.client.gwt.widget.Tree) com).addDoubleClickListener(this);
    }

    /** 
     * {@inheritDoc}
     */
    protected Object createComponentInstance(Object parent, DomNode element) {
        org.onemind.swingweb.client.gwt.widget.Tree t = new org.onemind.swingweb.client.gwt.widget.Tree();
        return t;
    }

    /** 
     * {@inheritDoc}
     */
    protected void handleChildren(AbstractClient rootHandler, IContainer c, DomNode element) {
        org.onemind.swingweb.client.gwt.widget.Tree tree = (org.onemind.swingweb.client.gwt.widget.Tree) c;
        tree.clear();
        String elementId = element.getAttribute("id");
        handleTreeNodes(tree, element, elementId + "#");
    }

    protected void handleTreeNodes(ITreeNode treeNode, DomNode element, String idPrefix) {
        List nodeList = getChildrenByTag(element, "treenode");
        for (int n = 0; n < nodeList.size(); n++) {
            DomNode treenodeNode = (DomNode) nodeList.get(n);
            String nodeId = treenodeNode.getAttribute("id");
            List elements = getChildrenByTag(treenodeNode, "element");
            if (elements.size() > 0) {
                DomNode elementNode = (DomNode) elements.get(0);
                String pregenId = idPrefix + nodeId;
                getClient().removeComponent(pregenId);
                Widget content = (Widget) getClient().handle(this, elementNode, pregenId);
                TreeNode tn = new TreeNode(pregenId, content);
                treeNode.addNode(tn);
                boolean expanded = "true".equalsIgnoreCase(treenodeNode.getAttribute("expanded"));
                boolean isLeaf = "true".equalsIgnoreCase(treenodeNode.getAttribute("isLeaf"));
                boolean isSelected = "true".equalsIgnoreCase(treenodeNode.getAttribute("selected"));
                handleTreeNodes(tn, treenodeNode, idPrefix);
                if (expanded) {
                    tn.getItem().setState(true, false);
                } else if (!isLeaf) {
                    tn.getItem().addItem("loading...");
                }
                if (isSelected) {
                    tn.getItem().getTree().setSelectedItem(tn.getItem(), false);
                }
            } else {
                throw new IllegalArgumentException("No widget found in the tree node");
            }
        }
    }

    public void onTreeItemSelected(TreeItem item) {
    }

    public void onTreeItemStateChanged(TreeItem item) {
        String id = DOM.getAttribute(item.getElement(), "swid");
        String[] parts = id.split("#");
        String treeId = parts[0];
        String path = parts[1];
        org.onemind.swingweb.client.gwt.widget.Tree tree = (org.onemind.swingweb.client.gwt.widget.Tree) item.getTree().getParent();
        String action = item.getState() ? "expanded" : "collapsed";
        postEvent(tree, path, false);
        postEvent(tree, "-action", action, true);
    }

    public void onClick(Widget sender) {
        String id = DOM.getAttribute(sender.getElement(), "id");
        String[] parts = id.split("#");
        String treeId = parts[0];
        String path = parts[1];
        System.out.println("Tree id = " + treeId);
        org.onemind.swingweb.client.gwt.widget.Tree tree = (org.onemind.swingweb.client.gwt.widget.Tree) getClient().getComponent(treeId);
        String action = (tree.isAppendSelection()) ? "addSelected" : "selected";
        postEvent(tree, path, false);
        postEvent(tree, "-action", action, true);
    }

    public void onDoubleClick(Widget sender) {
        String id = DOM.getAttribute(sender.getElement(), "id");
        String[] parts = id.split("#");
        String treeId = parts[0];
        String path = parts[1];
        org.onemind.swingweb.client.gwt.widget.Tree tree = (org.onemind.swingweb.client.gwt.widget.Tree) getClient().getComponent(treeId);
        String action = (tree.isAppendSelection()) ? "addSelected-doubleclicked" : "selected-doubleclicked";
        postEvent(tree, path, false);
        postEvent(tree, "-action", action, true);
    }
}
