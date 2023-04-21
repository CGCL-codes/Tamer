package net.sourceforge.jdefprog.mcl.generated.node;

import net.sourceforge.jdefprog.mcl.generated.analysis.*;

@SuppressWarnings("nls")
public final class ASimpleMulExpr extends PMulExpr {

    private PMulExprEl _mulExprEl_;

    public ASimpleMulExpr() {
    }

    public ASimpleMulExpr(@SuppressWarnings("hiding") PMulExprEl _mulExprEl_) {
        setMulExprEl(_mulExprEl_);
    }

    @Override
    public Object clone() {
        return new ASimpleMulExpr(cloneNode(this._mulExprEl_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASimpleMulExpr(this);
    }

    public PMulExprEl getMulExprEl() {
        return this._mulExprEl_;
    }

    public void setMulExprEl(PMulExprEl node) {
        if (this._mulExprEl_ != null) {
            this._mulExprEl_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._mulExprEl_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._mulExprEl_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._mulExprEl_ == child) {
            this._mulExprEl_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._mulExprEl_ == oldChild) {
            setMulExprEl((PMulExprEl) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
