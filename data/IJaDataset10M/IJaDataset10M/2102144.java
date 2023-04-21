package com.tensegrity.palobrowser.editors.cubeeditor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.palo.api.ElementNode;
import com.tensegrity.palobrowser.tree.TreeNode;

/**
 * <code>ElementTreeSelector</code>
 *
 * @author Arnd Houben
 * @author Stepan Rutz
 * @version $ID$
 */
public class ElementTreeSelector {

    private CheckboxTreeViewer cbViewer;

    private TreeNode[] elements;

    public ElementTreeSelector() {
    }

    public final void setInput(TreeNode[] elements) {
        this.elements = elements;
    }

    public final void setViewer(CheckboxTreeViewer cbViewer) {
        this.cbViewer = cbViewer;
    }

    public final void selectLevel(String lvl, boolean status) {
        if (lvl.equals("B")) {
            for (int i = 0; i < elements.length; i++) selectLeafs(elements[i], status);
        } else {
            int depth = Integer.parseInt(lvl) - 1;
            for (int i = 0; i < elements.length; i++) selectLevel(elements[i], status, depth);
        }
    }

    public final void selectAll() {
        for (int i = 0; i < elements.length; i++) cbViewer.setSubtreeChecked(elements[i], true);
    }

    public final void deselectAll() {
        for (int i = 0; i < elements.length; i++) cbViewer.setSubtreeChecked(elements[i], false);
    }

    public final void invertSelection() {
        for (int i = 0; i < elements.length; i++) invert(elements[i]);
    }

    /**
     * Adds all matching nodes to selected ones
     * @param regEx
     */
    public final void selectMatch(String regEx, boolean select) {
        regEx = parseWildcards(regEx);
        for (int i = 0; i < elements.length; i++) match(elements[i], regEx, select);
    }

    public final boolean isLevelSelected(String lvl) {
        boolean selected = true;
        if (lvl.equals("B")) {
            for (int i = 0; i < elements.length; i++) {
                selected = areLeafsSelected(elements[i]);
                if (!selected) break;
            }
        } else {
            int depth = Integer.parseInt(lvl) - 1;
            ArrayList lvlNodes = new ArrayList();
            for (int i = 0; i < elements.length; i++) {
                collectNodeFor(depth, elements[i], lvlNodes);
            }
            if (lvlNodes.isEmpty()) return false;
            return isLevelSelected((TreeNode[]) lvlNodes.toArray(new TreeNode[lvlNodes.size()]));
        }
        return selected;
    }

    public final void selectChildren(TreeNode treeNode, boolean status) {
        cbViewer.setChecked(treeNode, status);
        TreeNode[] children = treeNode.getChildren();
        for (int i = 0; i < children.length; i++) selectChildren(children[i], status);
    }

    public final void selectChildrenOnSameLevel(TreeNode treeNode, boolean status) {
        TreeNode parent = treeNode.getParent();
        if (parent != null) {
            TreeNode[] children = parent.getChildren();
            for (int i = 0, n = children.length; i < n; i++) cbViewer.setChecked(children[i], status);
        } else cbViewer.setChecked(treeNode, status);
    }

    public final void selectChildrenAndSameLevel(TreeNode treeNode, boolean status) {
        TreeNode parent = treeNode.getParent();
        if (parent != null) {
            TreeNode[] children = parent.getChildren();
            for (int i = 0, n = children.length; i < n; i++) selectChildren(children[i], status);
        } else selectChildren(treeNode, status);
    }

    private final void selectLevel(TreeNode treeNode, boolean status, int depth) {
        Object userObject = treeNode.getUserObject();
        if (userObject instanceof ElementNode) {
            ElementNode elNode = (ElementNode) userObject;
            int nodeDepth = elNode.getDepth();
            if (nodeDepth == depth) cbViewer.setChecked(treeNode, status); else if (nodeDepth > depth) return;
        }
        TreeNode[] children = treeNode.getChildren();
        for (int i = 0; i < children.length; i++) selectLevel(children[i], status, depth);
    }

    private final void selectLeafs(TreeNode elTreeNode, boolean status) {
        TreeNode[] children = elTreeNode.getChildren();
        for (int i = 0; i < children.length; i++) selectLeafs(children[i], status);
        if (!elTreeNode.hasChildren()) cbViewer.setChecked(elTreeNode, status);
    }

    private final void invert(TreeNode treeNode) {
        cbViewer.setChecked(treeNode, !cbViewer.getChecked(treeNode));
        TreeNode[] children = treeNode.getChildren();
        for (int i = 0; i < children.length; i++) invert(children[i]);
    }

    private final void match(TreeNode node, String regEx, boolean select) {
        ElementNode elNode = (ElementNode) node.getUserObject();
        if (elNode.getElement().getName().matches(regEx)) cbViewer.setChecked(node, select);
        TreeNode[] children = node.getChildren();
        for (int i = 0; i < children.length; i++) match(children[i], regEx, select);
    }

    private final boolean areLeafsSelected(TreeNode node) {
        TreeNode[] children = node.getChildren();
        for (int i = 0; i < children.length; i++) if (!areLeafsSelected(children[i])) return false;
        if (!node.hasChildren()) return cbViewer.getChecked(node);
        return true;
    }

    private final void collectNodeFor(int depth, TreeNode node, List nodes) {
        ElementNode elNode = (ElementNode) node.getUserObject();
        if (elNode.getDepth() == depth) {
            nodes.add(node);
        } else if (elNode.getDepth() < depth) {
            TreeNode[] children = node.getChildren();
            for (int i = 0; i < children.length; i++) collectNodeFor(depth, children[i], nodes);
        }
    }

    private final boolean isLevelSelected(TreeNode[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            if (!cbViewer.getChecked(nodes[i])) return false;
        }
        return true;
    }

    /**
     * Parses the given <code>String</code> and replaces any wildcard with a
     * suitable regular expression
     * 
     * @param str
     * @return a regular expression
     */
    private final String parseWildcards(String str) {
        str = str.replaceAll("\\*", ".*");
        str = str.replaceAll("\\?", ".?");
        return str;
    }
}
