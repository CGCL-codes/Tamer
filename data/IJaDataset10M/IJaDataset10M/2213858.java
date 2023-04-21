package net.sourceforge.jdefprog.mcl.generated.node;

import net.sourceforge.jdefprog.mcl.generated.analysis.*;

@SuppressWarnings("nls")
public final class AFRelopRelExpr extends PRelExpr {

    private PRelExprEl _a_;

    private TRelop _relop_;

    private PRelExprEl _b_;

    public AFRelopRelExpr() {
    }

    public AFRelopRelExpr(@SuppressWarnings("hiding") PRelExprEl _a_, @SuppressWarnings("hiding") TRelop _relop_, @SuppressWarnings("hiding") PRelExprEl _b_) {
        setA(_a_);
        setRelop(_relop_);
        setB(_b_);
    }

    @Override
    public Object clone() {
        return new AFRelopRelExpr(cloneNode(this._a_), cloneNode(this._relop_), cloneNode(this._b_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAFRelopRelExpr(this);
    }

    public PRelExprEl getA() {
        return this._a_;
    }

    public void setA(PRelExprEl node) {
        if (this._a_ != null) {
            this._a_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._a_ = node;
    }

    public TRelop getRelop() {
        return this._relop_;
    }

    public void setRelop(TRelop node) {
        if (this._relop_ != null) {
            this._relop_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._relop_ = node;
    }

    public PRelExprEl getB() {
        return this._b_;
    }

    public void setB(PRelExprEl node) {
        if (this._b_ != null) {
            this._b_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._b_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._a_) + toString(this._relop_) + toString(this._b_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._a_ == child) {
            this._a_ = null;
            return;
        }
        if (this._relop_ == child) {
            this._relop_ = null;
            return;
        }
        if (this._b_ == child) {
            this._b_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._a_ == oldChild) {
            setA((PRelExprEl) newChild);
            return;
        }
        if (this._relop_ == oldChild) {
            setRelop((TRelop) newChild);
            return;
        }
        if (this._b_ == oldChild) {
            setB((PRelExprEl) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
