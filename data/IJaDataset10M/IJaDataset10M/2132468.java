package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP1ListOfVariablePortIdentifiers extends PListOfVariablePortIdentifiers {

    private PIdentifier _identifier_;

    private TTEquals _tEquals_;

    private PExpression _expression_;

    public AP1ListOfVariablePortIdentifiers() {
    }

    public AP1ListOfVariablePortIdentifiers(@SuppressWarnings("hiding") PIdentifier _identifier_, @SuppressWarnings("hiding") TTEquals _tEquals_, @SuppressWarnings("hiding") PExpression _expression_) {
        setIdentifier(_identifier_);
        setTEquals(_tEquals_);
        setExpression(_expression_);
    }

    @Override
    public Object clone() {
        return new AP1ListOfVariablePortIdentifiers(cloneNode(this._identifier_), cloneNode(this._tEquals_), cloneNode(this._expression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP1ListOfVariablePortIdentifiers(this);
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

    public PExpression getExpression() {
        return this._expression_;
    }

    public void setExpression(PExpression node) {
        if (this._expression_ != null) {
            this._expression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._expression_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._identifier_) + toString(this._tEquals_) + toString(this._expression_);
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
        if (this._expression_ == child) {
            this._expression_ = null;
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
        if (this._expression_ == oldChild) {
            setExpression((PExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
