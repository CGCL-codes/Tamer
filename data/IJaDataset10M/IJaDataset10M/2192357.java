package nl.utwente.ewi.tpl.ast.tree;

import nl.utwente.ewi.tpl.runtime.AbstractTPLNode;

public class AbstractToken extends Node {

    public AbstractToken() {
    }

    public void addSubtree(AbstractTPLNode subTree) {
        throw new RuntimeException("Cannot add " + subTree + " to " + this);
    }

    public int getChildCount() {
        int result = 0;
        return result;
    }
}
