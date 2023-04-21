package com.ibm.wala.cast.tree.pattern;

import com.ibm.wala.cast.tree.CAstNode;

/**
 * A node pattern matching any constant. A pattern of this class also stores its last match.
 * 
 * @author mschaefer
 *
 */
public class SomeConstant extends NodeOfKind {

    private Object last_match;

    public SomeConstant() {
        super(CAstNode.CONSTANT);
    }

    @Override
    public boolean matches(CAstNode node) {
        boolean res = super.matches(node);
        if (res) this.last_match = node.getValue();
        return res;
    }

    public Object getLastMatch() {
        return last_match;
    }
}
