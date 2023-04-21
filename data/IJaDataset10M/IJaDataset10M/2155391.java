package org.usixml.model.imports.businessprocess;

import java.util.ArrayList;
import java.util.List;
import org.iknowu.util.TreeNode;
import org.usixml.model.imports.ExternalModelElement;

/**
 *
 * @author htmfilho
 */
public class BusinessProcessElement extends ExternalModelElement implements TreeNode {

    private TreeNode parent;

    private List<TreeNode> children;

    private String name;

    private String modified;

    private String description;

    private String stereotype;

    public BusinessProcessElement() {
    }

    public BusinessProcessElement(String id) {
        super.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStereotype() {
        return stereotype;
    }

    public void setStereotype(String stereotype) {
        this.stereotype = stereotype;
    }

    public String getNodeId() {
        return super.getId();
    }

    public TreeNode getSuperNode() {
        return this.parent;
    }

    public void setSuperNode(TreeNode superNode) {
        this.parent = superNode;
    }

    public List<TreeNode> getSubNodes() {
        return this.children;
    }

    public void removeSubNode(TreeNode subNode) {
        if (children != null) children.remove(subNode);
    }

    public String getLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLevel(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addNode(TreeNode subNode) {
        if (children == null) children = new ArrayList<TreeNode>();
        children.add(subNode);
    }

    public int compareTo(Object o) {
        return 0;
    }

    public String toString() {
        return this.name;
    }
}
