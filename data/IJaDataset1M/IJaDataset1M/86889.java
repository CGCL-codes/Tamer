package com.cubusmail.smartgwt.client;

import com.smartgwt.client.widgets.tree.TreeNode;

public class ExplorerTreeNode extends TreeNode {

    public ExplorerTreeNode(String name, String nodeID, String parentNodeID, String icon) {
        setName(name);
        setIcon(icon);
        if (nodeID.equals("featured-category")) {
            setIsOpen(true);
        }
    }

    public void setNodeID(String value) {
        setAttribute("nodeID", value);
    }

    public String getNodeID() {
        return getAttribute("nodeID");
    }

    public void setParentNodeID(String value) {
        setAttribute("parentNodeID", value);
    }

    public void setName(String name) {
        setAttribute("name", name);
    }

    public String getName() {
        return getAttributeAsString("name");
    }

    public void setIcon(String icon) {
        setAttribute("icon", icon);
    }

    public String getIcon() {
        return getAttributeAsString("icon");
    }

    public void setIsOpen(boolean isOpen) {
        setAttribute("isOpen", isOpen);
    }

    public void setIconSrc(String iconSrc) {
        setAttribute("iconSrc", iconSrc);
    }

    public String getIconSrc() {
        return getAttributeAsString("iconSrc");
    }
}
