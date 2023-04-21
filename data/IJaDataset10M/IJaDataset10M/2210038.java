package org.synthful.smartgwt.client.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.synthful.smartgwt.client.UIMasquerade;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.LoadState;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.DataChangedHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

public class UITree extends Widget implements HasWidgets, UIMasquerade<Tree> {

    protected final Tree tree;

    public UITree() {
        this.tree = new Tree();
    }

    @Override
    public void add(Widget w) {
        if (tree.getModelType() == TreeModelType.PARENT) {
            if (w instanceof UIParentLinkedNode) {
                TreeNode[] nodes = { ((UIParentLinkedNode) w).getSmartObject() };
                tree.linkNodes(nodes);
            }
        } else {
            if (w instanceof UIChildLinkedNode) {
                TreeNode node = ((UIChildLinkedNode) w).finaliseChildren();
                tree.setRoot(node);
            }
        }
    }

    @Override
    public void clear() {
        TreeNode[] newTreeNodes = {};
        tree.setData(newTreeNodes);
    }

    @Override
    public Iterator<Widget> iterator() {
        TreeNode[] treeNodes = tree.getAllNodes();
        List<Widget> nodes = new ArrayList<Widget>(treeNodes.length);
        for (TreeNode treeNode : treeNodes) {
            if (tree.getModelType() == TreeModelType.PARENT) nodes.add(new UIParentLinkedNode(treeNode)); else nodes.add(new UIChildLinkedNode(treeNode));
        }
        return nodes.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        if (w instanceof BaseUITreeNode) {
            return tree.remove(((BaseUITreeNode) w).getSmartObject());
        }
        return false;
    }

    @Override
    public Tree getSmartObject() {
        return this.tree;
    }

    /**
	 * to avoid confusion with GWT's setId
	 * use treeID='tree_id' in uibinder ui.xml.
	 * 
	 * @param id
	 */
    public void setTreeID(String id) {
        tree.setID(id);
    }

    public String getID() {
        return tree.getID();
    }

    public void setID(String id) {
        tree.setID(id);
    }

    public String getScClassName() {
        return tree.getScClassName();
    }

    public void setScClassName(String scClassName) {
        tree.setScClassName(scClassName);
    }

    public int hashCode() {
        return tree.hashCode();
    }

    public JavaScriptObject getConfig() {
        return tree.getConfig();
    }

    public boolean isCreated() {
        return tree.isCreated();
    }

    public JavaScriptObject getJsObj() {
        return tree.getJsObj();
    }

    public JavaScriptObject getOrCreateJsObj() {
        return tree.getOrCreateJsObj();
    }

    public JavaScriptObject create() {
        return tree.create();
    }

    public void destroy() {
        tree.destroy();
    }

    public void setAutoOpenRoot(Boolean autoOpenRoot) {
        tree.setAutoOpenRoot(autoOpenRoot);
    }

    public Boolean getAutoOpenRoot() {
        return tree.getAutoOpenRoot();
    }

    public boolean equals(Object obj) {
        return tree.equals(obj);
    }

    public void setChildrenProperty(String childrenProperty) {
        tree.setChildrenProperty(childrenProperty);
    }

    public String getChildrenProperty() {
        return tree.getChildrenProperty();
    }

    public void setDefaultIsFolder(Boolean defaultIsFolder) throws IllegalStateException {
        tree.setDefaultIsFolder(defaultIsFolder);
    }

    public Boolean getDefaultIsFolder() {
        return tree.getDefaultIsFolder();
    }

    public void setDefaultNodeTitle(String defaultNodeTitle) {
        tree.setDefaultNodeTitle(defaultNodeTitle);
    }

    public String getDefaultNodeTitle() {
        return tree.getDefaultNodeTitle();
    }

    public void setDiscardParentlessNodes(Boolean discardParentlessNodes) throws IllegalStateException {
        tree.setDiscardParentlessNodes(discardParentlessNodes);
    }

    public Boolean getDiscardParentlessNodes() {
        return tree.getDiscardParentlessNodes();
    }

    public void setIdField(String idField) throws IllegalStateException {
        tree.setIdField(idField);
    }

    public String getIdField() {
        return tree.getIdField();
    }

    public String toString() {
        return tree.toString();
    }

    public void setIsFolderProperty(String isFolderProperty) {
        tree.setIsFolderProperty(isFolderProperty);
    }

    public String getIsFolderProperty() {
        return tree.getIsFolderProperty();
    }

    public void setModelType(TreeModelType modelType) {
        tree.setModelType(modelType);
    }

    public TreeModelType getModelType() {
        return tree.getModelType();
    }

    public void setNameProperty(String nameProperty) {
        tree.setNameProperty(nameProperty);
    }

    public String getNameProperty() {
        return tree.getNameProperty();
    }

    public void setOpenProperty(String openProperty) {
        tree.setOpenProperty(openProperty);
    }

    public String getOpenProperty() {
        return tree.getOpenProperty();
    }

    public void setParentIdField(String parentIdField) throws IllegalStateException {
        tree.setParentIdField(parentIdField);
    }

    public String getParentIdField() {
        return tree.getParentIdField();
    }

    public void setPathDelim(String pathDelim) {
        tree.setPathDelim(pathDelim);
    }

    public void fireEvent(GwtEvent<?> event) {
        tree.fireEvent(event);
    }

    public int getHandlerCount(Type<?> type) {
        return tree.getHandlerCount(type);
    }

    public String getPathDelim() {
        return tree.getPathDelim();
    }

    public void setReportCollisions(Boolean reportCollisions) throws IllegalStateException {
        tree.setReportCollisions(reportCollisions);
    }

    public Boolean getReportCollisions() {
        return tree.getReportCollisions();
    }

    public void setRoot(TreeNode root) {
        tree.setRoot(root);
    }

    public TreeNode getRoot() {
        return tree.getRoot();
    }

    public void setSeparateFolders(Boolean separateFolders) {
        tree.setSeparateFolders(separateFolders);
    }

    public Boolean getSeparateFolders() {
        return tree.getSeparateFolders();
    }

    public void setShowRoot(Boolean showRoot) {
        tree.setShowRoot(showRoot);
    }

    public Boolean getShowRoot() {
        return tree.getShowRoot();
    }

    public void setSortFoldersBeforeLeaves(Boolean sortFoldersBeforeLeaves) {
        tree.setSortFoldersBeforeLeaves(sortFoldersBeforeLeaves);
    }

    public Boolean getSortFoldersBeforeLeaves() {
        return tree.getSortFoldersBeforeLeaves();
    }

    public void setTitleProperty(String titleProperty) {
        tree.setTitleProperty(titleProperty);
    }

    public String getTitleProperty() {
        return tree.getTitleProperty();
    }

    public void closeAll() {
        tree.closeAll();
    }

    public void closeAll(TreeNode node) {
        tree.closeAll(node);
    }

    public void closeFolder(TreeNode node) {
        tree.closeFolder(node);
    }

    public HandlerRegistration addDataChangedHandler(DataChangedHandler handler) {
        return tree.addDataChangedHandler(handler);
    }

    public int getLength() {
        return tree.getLength();
    }

    public int getLevel(TreeNode node) {
        return tree.getLevel(node);
    }

    public LoadState getLoadState(TreeNode node) {
        return tree.getLoadState(node);
    }

    public String getName(TreeNode node) {
        return tree.getName(node);
    }

    public String getParentPath(TreeNode node) {
        return tree.getParentPath(node);
    }

    public String getPath(TreeNode node) {
        return tree.getPath(node);
    }

    public String getTitle(TreeNode node) {
        return tree.getTitle(node);
    }

    public Boolean hasChildren(TreeNode node) {
        return tree.hasChildren(node);
    }

    public Boolean hasFolders(TreeNode node) {
        return tree.hasFolders(node);
    }

    public Boolean hasLeaves(TreeNode node) {
        return tree.hasLeaves(node);
    }

    public Boolean isDescendantOf(TreeNode child, TreeNode parent) {
        return tree.isDescendantOf(child, parent);
    }

    public Boolean isFolder(TreeNode node) {
        return tree.isFolder(node);
    }

    public Boolean isLeaf(TreeNode node) {
        return tree.isLeaf(node);
    }

    public Boolean isLoaded(TreeNode node) {
        return tree.isLoaded(node);
    }

    public Boolean isOpen(TreeNode node) {
        return tree.isOpen(node);
    }

    public Boolean isRoot(TreeNode node) {
        return tree.isRoot(node);
    }

    public void move(TreeNode node, TreeNode newParent) {
        tree.move(node, newParent);
    }

    public void move(TreeNode node, TreeNode newParent, int position) {
        tree.move(node, newParent, position);
    }

    public void openAll() {
        tree.openAll();
    }

    public void openAll(TreeNode node) {
        tree.openAll(node);
    }

    public void reloadChildren(TreeNode node) {
        tree.reloadChildren(node);
    }

    public Boolean remove(TreeNode node) {
        return tree.remove(node);
    }

    public void unloadChildren(TreeNode node) {
        tree.unloadChildren(node);
    }

    public void setData(TreeNode[] nodes) {
        tree.setData(nodes);
    }

    public ListGridRecord[] getData() {
        return tree.getData();
    }

    public void setRootValue(String rootValue) {
        tree.setRootValue(rootValue);
    }

    public void setRootValue(int rootValue) {
        tree.setRootValue(rootValue);
    }

    public String getRootValue() {
        return tree.getRootValue();
    }

    public TreeNode getParent(TreeNode node) {
        return tree.getParent(node);
    }

    public void linkNodes(TreeNode[] nodes) {
        tree.linkNodes(nodes);
    }

    public TreeNode[] getParents(TreeNode node) {
        return tree.getParents(node);
    }

    public TreeNode findById(String id) {
        return tree.findById(id);
    }

    public TreeNode find(String fieldNameOrPath) {
        return tree.find(fieldNameOrPath);
    }

    public TreeNode find(String fieldNameOrPath, Object value) {
        return tree.find(fieldNameOrPath, value);
    }

    public TreeNode[] getChildren(TreeNode node) {
        return tree.getChildren(node);
    }

    public TreeNode[] getFolders(TreeNode node) {
        return tree.getFolders(node);
    }

    public TreeNode[] getLeaves(TreeNode node) {
        return tree.getLeaves(node);
    }

    public TreeNode[] getDescendants() {
        return tree.getDescendants();
    }

    public TreeNode[] getDescendants(TreeNode node) {
        return tree.getDescendants(node);
    }

    public TreeNode[] getDescendantFolders() {
        return tree.getDescendantFolders();
    }

    public TreeNode[] getDescendantFolders(TreeNode node) {
        return tree.getDescendantFolders(node);
    }

    public TreeNode[] getDescendantLeaves() {
        return tree.getDescendantLeaves();
    }

    public TreeNode[] getDescendantLeaves(TreeNode node) {
        return tree.getDescendantLeaves(node);
    }

    public TreeNode add(TreeNode node, TreeNode parent) {
        return tree.add(node, parent);
    }

    public TreeNode add(TreeNode node, String parentPath) {
        return tree.add(node, parentPath);
    }

    public TreeNode add(TreeNode node, TreeNode parent, int position) {
        return tree.add(node, parent, position);
    }

    public TreeNode add(TreeNode node, String parentPath, int position) {
        return tree.add(node, parentPath, position);
    }

    public TreeNode[] addList(TreeNode[] nodeList, TreeNode parent) {
        return tree.addList(nodeList, parent);
    }

    public TreeNode[] addList(TreeNode[] nodeList, String parentPath) {
        return tree.addList(nodeList, parentPath);
    }

    public TreeNode[] addList(TreeNode[] nodeList, TreeNode parent, int position) {
        return tree.addList(nodeList, parent, position);
    }

    public TreeNode[] addList(TreeNode[] nodeList, String parentPath, int position) {
        return tree.addList(nodeList, parentPath, position);
    }

    public Boolean removeList(TreeNode[] nodeList) {
        return tree.removeList(nodeList);
    }

    public void openFolders(TreeNode[] nodeList) {
        tree.openFolders(nodeList);
    }

    public void closeFolders(TreeNode[] nodeList) {
        tree.closeFolders(nodeList);
    }

    public TreeNode[] getOpenList(TreeNode node) {
        return tree.getOpenList(node);
    }

    public void loadChildren(TreeNode node) {
        tree.loadChildren(node);
    }

    public void openFolder(TreeNode node) {
        tree.openFolder(node);
    }

    public TreeNode[] getAllNodes() {
        return tree.getAllNodes();
    }

    public TreeNode[] getAllNodes(TreeNode node) {
        return tree.getAllNodes(node);
    }

    public void setProperty(String property, String value) {
        tree.setProperty(property, value);
    }

    public void setProperty(String property, boolean value) {
        tree.setProperty(property, value);
    }

    public void setProperty(String property, double value) {
        tree.setProperty(property, value);
    }

    public void setProperty(String property, JavaScriptObject value) {
        tree.setProperty(property, value);
    }
}
