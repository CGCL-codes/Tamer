package com.david.client.ui;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.layout.ContentPanel;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreeNodeConfig;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.TreePanelConfig;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

public class NaviTreePanel extends ContentPanel {

    private MainPanel mainPanel = null;

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public NaviTreePanel() {
        super("eg-explorer", "Task Navigation");
        TreePanel menuTree = new TreePanel("Navi-Tree", new TreePanelConfig() {

            {
                setAnimate(true);
                setEnableDD(true);
                setContainerScroll(true);
                setRootVisible(true);
            }
        });
        final TreeNode root = new TreeNode(new TreeNodeConfig() {

            {
                setText("Available Tasks");
            }
        });
        menuTree.setRootNode(root);
        load(root);
        menuTree.render();
        menuTree.expandAll();
        add(menuTree);
    }

    private void load(TreeNode currentNode) {
        TreeNode userMenu = new TreeNode("User Managerment");
        currentNode.appendChild(userMenu);
        TreeNode addUserNode = new TreeNode("Add New User");
        addUserNode.addTreeNodeListener(new TreeNodeListenerAdapter() {

            public void onClick(Node node, EventObject e) {
                mainPanel.showPanel("addUser");
            }
        });
        userMenu.appendChild(addUserNode);
        TreeNode delUserNode = new TreeNode("Delete User");
        delUserNode.addTreeNodeListener(new TreeNodeListenerAdapter() {

            public void onClick(Node node, EventObject e) {
                mainPanel.showPanel("delUser");
            }
        });
        userMenu.appendChild(delUserNode);
        userMenu.appendChild(new TreeNode("Edit User"));
        currentNode.appendChild(new TreeNode("HR management"));
        currentNode.appendChild(new TreeNode("Project Management"));
        currentNode.appendChild(new TreeNode("Office Management"));
        currentNode.appendChild(new TreeNode("Knowledge base"));
        currentNode.appendChild(new TreeNode("Log out"));
    }
}
