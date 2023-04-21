package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ABlockClassBodyDeclaration extends PClassBodyDeclaration {

    private PBlock _block_;

    public ABlockClassBodyDeclaration() {
    }

    public ABlockClassBodyDeclaration(@SuppressWarnings("hiding") PBlock _block_) {
        setBlock(_block_);
    }

    @Override
    public Object clone() {
        return new ABlockClassBodyDeclaration(cloneNode(this._block_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseABlockClassBodyDeclaration(this);
    }

    public PBlock getBlock() {
        return this._block_;
    }

    public void setBlock(PBlock node) {
        if (this._block_ != null) {
            this._block_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._block_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._block_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._block_ == child) {
            this._block_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._block_ == oldChild) {
            setBlock((PBlock) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
