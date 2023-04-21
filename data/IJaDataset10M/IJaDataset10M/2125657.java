package org.trebor.util.encapsulate;

import javax.swing.tree.DefaultMutableTreeNode;

public class DefaultNodeFactory implements INodeFactory {

    public INode createNode(INode parent) {
        Node node = new Node();
        if (null != parent) ((DefaultMutableTreeNode) parent).add(node);
        return node;
    }

    private static class Node extends DefaultMutableTreeNode implements INode {

        private static final long serialVersionUID = -7593032721134208980L;

        private boolean mSelected = false;

        public String getLabel() {
            return "the name of this thing";
        }

        public boolean isSelected() {
            return mSelected;
        }

        public void setSelected(boolean selected) {
            mSelected = selected;
        }

        public INode getParent() {
            return (INode) super.getParent();
        }

        @Override
        public INode getChildAt(int index) {
            return (INode) super.getChildAt(index);
        }

        public INode getNextSib() {
            return (INode) super.getNextSibling();
        }

        public INode getPreviousSib() {
            return (INode) super.getPreviousSibling();
        }
    }
}
