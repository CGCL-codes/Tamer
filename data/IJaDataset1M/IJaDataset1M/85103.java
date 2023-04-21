package de.mulchprod.kajona.languageeditor.gui.tree;

import de.mulchprod.kajona.languageeditor.core.textfile.ILanguageFileSet;
import java.util.ArrayList;

/**
 *
 * @author sidler
 */
public class TreeNode {

    private NodeType type;

    private String nodeName;

    private ArrayList<TreeNode> childNodes = new ArrayList<TreeNode>();

    private TreeNode parentNode = null;

    private ILanguageFileSet referencingObject = null;

    public TreeNode(NodeType type, String nodeName) {
        this.type = type;
        this.nodeName = nodeName;
    }

    public void addChildNode(TreeNode childNode) {
        if (!childNodes.contains(childNode)) childNodes.add(childNode);
    }

    public ArrayList<TreeNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(ArrayList<TreeNode> childNodes) {
        this.childNodes = childNodes;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public TreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(TreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public ILanguageFileSet getReferencingObject() {
        return referencingObject;
    }

    public void setReferencingObject(ILanguageFileSet referencingObject) {
        this.referencingObject = referencingObject;
    }
}
