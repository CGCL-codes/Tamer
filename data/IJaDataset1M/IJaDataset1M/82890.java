package edu.ucsd.ncmir.ontology.browser;

import edu.ucsd.ncmir.ontology.OntologyNode;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;

class OntologyTreeNode implements TreeNode {

    private OntologyTreeNode _parent;

    private OntologyNode _ontology_node;

    private OntologyTreeNode[] _children;

    OntologyTreeNode(OntologyTreeNode parent, OntologyNode ontology_node, OntologyNode[] children) {
        this._parent = parent;
        this._ontology_node = ontology_node;
        if (children != null) {
            this._children = new OntologyTreeNode[children.length];
            for (int i = 0; i < children.length; i++) this._children[i] = new OntologyTreeNode(this, children[i], children[i].getChildren());
        } else this._children = null;
    }

    OntologyTreeNode(OntologyTreeNode sibling) {
        this._parent = (OntologyTreeNode) sibling.getParent();
        this._ontology_node = sibling.getOntologyNode();
        this._children = sibling.getChildrenArray();
    }

    public OntologyNode getOntologyNode() {
        return this._ontology_node;
    }

    public String getInternalName() {
        return this._ontology_node != null ? this._ontology_node.getInternalName() : "Ontology";
    }

    @Override
    public String toString() {
        return this._ontology_node != null ? this._ontology_node.toString() : "Ontology";
    }

    public boolean equals(OntologyTreeNode node) {
        return this.getOntologyNode().equals(node.getOntologyNode());
    }

    public OntologyTreeNode[] getChildrenArray() {
        return this._children;
    }

    public Enumeration children() {
        return new TreeEnumeration(this._children);
    }

    private class TreeEnumeration implements Enumeration<OntologyTreeNode> {

        private int _index = 0;

        private OntologyTreeNode[] _children = null;

        TreeEnumeration(OntologyTreeNode[] children) {
            if (children != null) this._children = children; else this._children = new OntologyTreeNode[0];
        }

        public boolean hasMoreElements() {
            return this._index < this._children.length;
        }

        public OntologyTreeNode nextElement() {
            return this.hasMoreElements() ? this._children[this._index++] : null;
        }
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public TreeNode getChildAt(int index) {
        return this._children[index];
    }

    public int getChildCount() {
        return (this._children != null) ? this._children.length : 0;
    }

    public int getIndex(TreeNode node) {
        int index = -1;
        OntologyTreeNode otn = (OntologyTreeNode) node;
        if (this._children != null) for (int i = 0; i < this._children.length; i++) if (this._children[i].equals(otn)) {
            index = i;
            break;
        }
        return index;
    }

    public TreeNode getParent() {
        return this._parent;
    }

    public boolean isLeaf() {
        return (this._children == null) || (this._children.length == 0);
    }
}
