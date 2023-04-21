package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AExpressionCastExpression extends PCastExpression {

    private TLParenthese _lParenthese_;

    private PExpression _expression_;

    private TRParenthese _rParenthese_;

    private PUnaryExpressionNotPlusMinus _unaryExpressionNotPlusMinus_;

    public AExpressionCastExpression() {
    }

    public AExpressionCastExpression(@SuppressWarnings("hiding") TLParenthese _lParenthese_, @SuppressWarnings("hiding") PExpression _expression_, @SuppressWarnings("hiding") TRParenthese _rParenthese_, @SuppressWarnings("hiding") PUnaryExpressionNotPlusMinus _unaryExpressionNotPlusMinus_) {
        setLParenthese(_lParenthese_);
        setExpression(_expression_);
        setRParenthese(_rParenthese_);
        setUnaryExpressionNotPlusMinus(_unaryExpressionNotPlusMinus_);
    }

    @Override
    public Object clone() {
        return new AExpressionCastExpression(cloneNode(this._lParenthese_), cloneNode(this._expression_), cloneNode(this._rParenthese_), cloneNode(this._unaryExpressionNotPlusMinus_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAExpressionCastExpression(this);
    }

    public TLParenthese getLParenthese() {
        return this._lParenthese_;
    }

    public void setLParenthese(TLParenthese node) {
        if (this._lParenthese_ != null) {
            this._lParenthese_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._lParenthese_ = node;
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

    public TRParenthese getRParenthese() {
        return this._rParenthese_;
    }

    public void setRParenthese(TRParenthese node) {
        if (this._rParenthese_ != null) {
            this._rParenthese_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._rParenthese_ = node;
    }

    public PUnaryExpressionNotPlusMinus getUnaryExpressionNotPlusMinus() {
        return this._unaryExpressionNotPlusMinus_;
    }

    public void setUnaryExpressionNotPlusMinus(PUnaryExpressionNotPlusMinus node) {
        if (this._unaryExpressionNotPlusMinus_ != null) {
            this._unaryExpressionNotPlusMinus_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._unaryExpressionNotPlusMinus_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._lParenthese_) + toString(this._expression_) + toString(this._rParenthese_) + toString(this._unaryExpressionNotPlusMinus_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._lParenthese_ == child) {
            this._lParenthese_ = null;
            return;
        }
        if (this._expression_ == child) {
            this._expression_ = null;
            return;
        }
        if (this._rParenthese_ == child) {
            this._rParenthese_ = null;
            return;
        }
        if (this._unaryExpressionNotPlusMinus_ == child) {
            this._unaryExpressionNotPlusMinus_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._lParenthese_ == oldChild) {
            setLParenthese((TLParenthese) newChild);
            return;
        }
        if (this._expression_ == oldChild) {
            setExpression((PExpression) newChild);
            return;
        }
        if (this._rParenthese_ == oldChild) {
            setRParenthese((TRParenthese) newChild);
            return;
        }
        if (this._unaryExpressionNotPlusMinus_ == oldChild) {
            setUnaryExpressionNotPlusMinus((PUnaryExpressionNotPlusMinus) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
