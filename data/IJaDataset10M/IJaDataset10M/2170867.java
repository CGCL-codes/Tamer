package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AParamAssignment extends PParamAssignment {

    private PIdentifier _identifier_;

    private TTEquals _tEquals_;

    private PMintypmaxExpression _mintypmaxExpression_;

    public AParamAssignment() {
    }

    public AParamAssignment(@SuppressWarnings("hiding") PIdentifier _identifier_, @SuppressWarnings("hiding") TTEquals _tEquals_, @SuppressWarnings("hiding") PMintypmaxExpression _mintypmaxExpression_) {
        setIdentifier(_identifier_);
        setTEquals(_tEquals_);
        setMintypmaxExpression(_mintypmaxExpression_);
    }

    @Override
    public Object clone() {
        return new AParamAssignment(cloneNode(this._identifier_), cloneNode(this._tEquals_), cloneNode(this._mintypmaxExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAParamAssignment(this);
    }

    public PIdentifier getIdentifier() {
        return this._identifier_;
    }

    public void setIdentifier(PIdentifier node) {
        if (this._identifier_ != null) {
            this._identifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier_ = node;
    }

    public TTEquals getTEquals() {
        return this._tEquals_;
    }

    public void setTEquals(TTEquals node) {
        if (this._tEquals_ != null) {
            this._tEquals_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tEquals_ = node;
    }

    public PMintypmaxExpression getMintypmaxExpression() {
        return this._mintypmaxExpression_;
    }

    public void setMintypmaxExpression(PMintypmaxExpression node) {
        if (this._mintypmaxExpression_ != null) {
            this._mintypmaxExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._mintypmaxExpression_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._identifier_) + toString(this._tEquals_) + toString(this._mintypmaxExpression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        if (this._tEquals_ == child) {
            this._tEquals_ = null;
            return;
        }
        if (this._mintypmaxExpression_ == child) {
            this._mintypmaxExpression_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._identifier_ == oldChild) {
            setIdentifier((PIdentifier) newChild);
            return;
        }
        if (this._tEquals_ == oldChild) {
            setTEquals((TTEquals) newChild);
            return;
        }
        if (this._mintypmaxExpression_ == oldChild) {
            setMintypmaxExpression((PMintypmaxExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
