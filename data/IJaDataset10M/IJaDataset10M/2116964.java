package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AMultipleConcatenation extends PMultipleConcatenation {

    private TTLbrace _tLbrace_;

    private PExpression _expression_;

    private PConcatenation _concatenation_;

    private TTRbrace _tRbrace_;

    public AMultipleConcatenation() {
    }

    public AMultipleConcatenation(@SuppressWarnings("hiding") TTLbrace _tLbrace_, @SuppressWarnings("hiding") PExpression _expression_, @SuppressWarnings("hiding") PConcatenation _concatenation_, @SuppressWarnings("hiding") TTRbrace _tRbrace_) {
        setTLbrace(_tLbrace_);
        setExpression(_expression_);
        setConcatenation(_concatenation_);
        setTRbrace(_tRbrace_);
    }

    @Override
    public Object clone() {
        return new AMultipleConcatenation(cloneNode(this._tLbrace_), cloneNode(this._expression_), cloneNode(this._concatenation_), cloneNode(this._tRbrace_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAMultipleConcatenation(this);
    }

    public TTLbrace getTLbrace() {
        return this._tLbrace_;
    }

    public void setTLbrace(TTLbrace node) {
        if (this._tLbrace_ != null) {
            this._tLbrace_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tLbrace_ = node;
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

    public PConcatenation getConcatenation() {
        return this._concatenation_;
    }

    public void setConcatenation(PConcatenation node) {
        if (this._concatenation_ != null) {
            this._concatenation_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._concatenation_ = node;
    }

    public TTRbrace getTRbrace() {
        return this._tRbrace_;
    }

    public void setTRbrace(TTRbrace node) {
        if (this._tRbrace_ != null) {
            this._tRbrace_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tRbrace_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._tLbrace_) + toString(this._expression_) + toString(this._concatenation_) + toString(this._tRbrace_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._tLbrace_ == child) {
            this._tLbrace_ = null;
            return;
        }
        if (this._expression_ == child) {
            this._expression_ = null;
            return;
        }
        if (this._concatenation_ == child) {
            this._concatenation_ = null;
            return;
        }
        if (this._tRbrace_ == child) {
            this._tRbrace_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._tLbrace_ == oldChild) {
            setTLbrace((TTLbrace) newChild);
            return;
        }
        if (this._expression_ == oldChild) {
            setExpression((PExpression) newChild);
            return;
        }
        if (this._concatenation_ == oldChild) {
            setConcatenation((PConcatenation) newChild);
            return;
        }
        if (this._tRbrace_ == oldChild) {
            setTRbrace((TTRbrace) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
