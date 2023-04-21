package org.freebxml.omar.client.ui.web.client.browser;

import org.freebxml.omar.client.ui.web.client.AbstractAsyncCallback;
import org.freebxml.omar.client.ui.web.client.Application;
import org.freebxml.omar.client.ui.web.client.RootNodeData;
import org.freebxml.omar.client.ui.web.client.TreeNodeLabelAndProperties;
import org.freebxml.omar.client.ui.web.client.TreeNodeUserObject;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.TreePanelConfig;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class TreeViewPanel extends TreePanel {

    public TreeViewPanel(final Browser browser, final String treeViewId) {
        super(Ext.generateId(), new TreePanelConfig() {

            {
                setAnimate(true);
                setContainerScroll(true);
                setRootVisible(true);
            }
        });
        Application.service.getRootNodeData(treeViewId, new AbstractAsyncCallback() {

            public void onSuccess(Object object) {
                RootNodeData rootNodeData = (RootNodeData) object;
                TreeNode rootNode = createTreeNode(rootNodeData.getRootNode());
                setRootNode(rootNode);
                TreeNodeLabelAndProperties[] children = rootNodeData.getChildren();
                for (int i = 0; i < children.length; i++) {
                    rootNode.appendChild(createTreeNode(children[i]));
                }
                ((TreeNodeUserObject) rootNode.getUserObject()).childrenLoaded();
                render();
            }
        });
        addTreePanelListener(new TreePanelListenerAdapter() {

            public void onExpand(TreeNode node) {
                triggerAncestorLoad(treeViewId, node);
            }

            public void onClick(TreeNode node, EventObject event) {
                browser.nodeSelected(treeViewId, ((TreeNodeUserObject) node.getUserObject()).getProperties());
            }

            public void onContextMenu(TreeNode node, EventObject e) {
                RegistryObjectHandle registryObjectHandle = ((TreeNodeUserObject) node.getUserObject()).getRegistryObjectHandle();
                if (registryObjectHandle != null) {
                    Actions.getInstance().createContextMenu(browser, registryObjectHandle).showAt(e.getXY());
                }
            }
        });
    }

    private TreeNode createTreeNode(TreeNodeLabelAndProperties node) {
        TreeNode result = new TreeNode(node.getLabel());
        result.setUserObject(new TreeNodeUserObject(node.getRegistryObject(), node.getProperties()));
        return result;
    }

    private void triggerAncestorLoad(final String treeViewId, final TreeNode node) {
        Node[] childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.length; i++) {
            final TreeNode child = (TreeNode) childNodes[i];
            TreeNodeUserObject treeNodeUserObject = (TreeNodeUserObject) child.getUserObject();
            if (!treeNodeUserObject.isChildrenLoaded()) {
                treeNodeUserObject.childrenLoaded();
                Application.service.getChildren(treeViewId, treeNodeUserObject.getProperties(), new AbstractAsyncCallback() {

                    public void onSuccess(Object object) {
                        TreeNodeLabelAndProperties[] childData = (TreeNodeLabelAndProperties[]) object;
                        for (int i = 0; i < childData.length; i++) {
                            child.appendChild(createTreeNode(childData[i]));
                        }
                        triggerAncestorLoad(treeViewId, node);
                    }
                });
                break;
            }
        }
    }
}
