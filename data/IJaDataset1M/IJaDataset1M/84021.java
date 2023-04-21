package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class ABracketRange extends PBracketRange {

    private TTLbracket _tLbracket_;

    private PRangeExpression _rangeExpression_;

    private TTRbracket _tRbracket_;

    public ABracketRange() {
    }

    public ABracketRange(@SuppressWarnings("hiding") TTLbracket _tLbracket_, @SuppressWarnings("hiding") PRangeExpression _rangeExpression_, @SuppressWarnings("hiding") TTRbracket _tRbracket_) {
        setTLbracket(_tLbracket_);
        setRangeExpression(_rangeExpression_);
        setTRbracket(_tRbracket_);
    }

    @Override
    public Object clone() {
        return new ABracketRange(cloneNode(this._tLbracket_), cloneNode(this._rangeExpression_), cloneNode(this._tRbracket_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseABracketRange(this);
    }

    public TTLbracket getTLbracket() {
        return this._tLbracket_;
    }

    public void setTLbracket(TTLbracket node) {
        if (this._tLbracket_ != null) {
            this._tLbracket_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tLbracket_ = node;
    }

    public PRangeExpression getRangeExpression() {
        return this._rangeExpression_;
    }

    public void setRangeExpression(PRangeExpression node) {
        if (this._rangeExpression_ != null) {
            this._rangeExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._rangeExpression_ = node;
    }

    public TTRbracket getTRbracket() {
        return this._tRbracket_;
    }

    public void setTRbracket(TTRbracket node) {
        if (this._tRbracket_ != null) {
            this._tRbracket_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tRbracket_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._tLbracket_) + toString(this._rangeExpression_) + toString(this._tRbracket_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._tLbracket_ == child) {
            this._tLbracket_ = null;
            return;
        }
        if (this._rangeExpression_ == child) {
            this._rangeExpression_ = null;
            return;
        }
        if (this._tRbracket_ == child) {
            this._tRbracket_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._tLbracket_ == oldChild) {
            setTLbracket((TTLbracket) newChild);
            return;
        }
        if (this._rangeExpression_ == oldChild) {
            setRangeExpression((PRangeExpression) newChild);
            return;
        }
        if (this._tRbracket_ == oldChild) {
            setTRbracket((TTRbracket) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
