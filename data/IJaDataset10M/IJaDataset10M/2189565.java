package persistence.antlr;

class TreeSpecifierNode {

    private TreeSpecifierNode parent = null;

    private TreeSpecifierNode firstChild = null;

    private TreeSpecifierNode nextSibling = null;

    private Token tok;

    TreeSpecifierNode(Token tok_) {
        tok = tok_;
    }

    public TreeSpecifierNode getFirstChild() {
        return firstChild;
    }

    public TreeSpecifierNode getNextSibling() {
        return nextSibling;
    }

    public TreeSpecifierNode getParent() {
        return parent;
    }

    public Token getToken() {
        return tok;
    }

    public void setFirstChild(TreeSpecifierNode child) {
        firstChild = child;
        child.parent = this;
    }

    public void setNextSibling(TreeSpecifierNode sibling) {
        nextSibling = sibling;
        sibling.parent = parent;
    }
}
