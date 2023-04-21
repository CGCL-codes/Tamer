package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AVariableDeclaratorVariableDeclarators extends PVariableDeclarators {

    private PVariableDeclarator _variableDeclarator_;

    public AVariableDeclaratorVariableDeclarators() {
    }

    public AVariableDeclaratorVariableDeclarators(@SuppressWarnings("hiding") PVariableDeclarator _variableDeclarator_) {
        setVariableDeclarator(_variableDeclarator_);
    }

    @Override
    public Object clone() {
        return new AVariableDeclaratorVariableDeclarators(cloneNode(this._variableDeclarator_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAVariableDeclaratorVariableDeclarators(this);
    }

    public PVariableDeclarator getVariableDeclarator() {
        return this._variableDeclarator_;
    }

    public void setVariableDeclarator(PVariableDeclarator node) {
        if (this._variableDeclarator_ != null) {
            this._variableDeclarator_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._variableDeclarator_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._variableDeclarator_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._variableDeclarator_ == child) {
            this._variableDeclarator_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._variableDeclarator_ == oldChild) {
            setVariableDeclarator((PVariableDeclarator) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
