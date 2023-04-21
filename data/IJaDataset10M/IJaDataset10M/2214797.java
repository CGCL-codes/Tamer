package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ASlashMuUnMultiplicativeExpression extends PMultiplicativeExpression {

    private PMultiplicativeExpression _multiplicativeExpression_;

    private TSlash _slash_;

    private PUnaryExpression _unaryExpression_;

    public ASlashMuUnMultiplicativeExpression() {
    }

    public ASlashMuUnMultiplicativeExpression(@SuppressWarnings("hiding") PMultiplicativeExpression _multiplicativeExpression_, @SuppressWarnings("hiding") TSlash _slash_, @SuppressWarnings("hiding") PUnaryExpression _unaryExpression_) {
        setMultiplicativeExpression(_multiplicativeExpression_);
        setSlash(_slash_);
        setUnaryExpression(_unaryExpression_);
    }

    @Override
    public Object clone() {
        return new ASlashMuUnMultiplicativeExpression(cloneNode(this._multiplicativeExpression_), cloneNode(this._slash_), cloneNode(this._unaryExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASlashMuUnMultiplicativeExpression(this);
    }

    public PMultiplicativeExpression getMultiplicativeExpression() {
        return this._multiplicativeExpression_;
    }

    public void setMultiplicativeExpression(PMultiplicativeExpression node) {
        if (this._multiplicativeExpression_ != null) {
            this._multiplicativeExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._multiplicativeExpression_ = node;
    }

    public TSlash getSlash() {
        return this._slash_;
    }

    public void setSlash(TSlash node) {
        if (this._slash_ != null) {
            this._slash_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._slash_ = node;
    }

    public PUnaryExpression getUnaryExpression() {
        return this._unaryExpression_;
    }

    public void setUnaryExpression(PUnaryExpression node) {
        if (this._unaryExpression_ != null) {
            this._unaryExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._unaryExpression_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._multiplicativeExpression_) + toString(this._slash_) + toString(this._unaryExpression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._multiplicativeExpression_ == child) {
            this._multiplicativeExpression_ = null;
            return;
        }
        if (this._slash_ == child) {
            this._slash_ = null;
            return;
        }
        if (this._unaryExpression_ == child) {
            this._unaryExpression_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._multiplicativeExpression_ == oldChild) {
            setMultiplicativeExpression((PMultiplicativeExpression) newChild);
            return;
        }
        if (this._slash_ == oldChild) {
            setSlash((TSlash) newChild);
            return;
        }
        if (this._unaryExpression_ == oldChild) {
            setUnaryExpression((PUnaryExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
