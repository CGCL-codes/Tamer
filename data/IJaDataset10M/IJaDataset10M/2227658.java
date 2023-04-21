package org.sablecc.sablecc.syntax3.node;

import org.sablecc.sablecc.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class ASeparatedAtLeastExpression extends PExpression {

    private PExpression _base_;

    private TSeparatorKeyword _separatorKeyword_;

    private PExpression _separator_;

    private TCaret _caret_;

    private TNumber _number_;

    private TThreeDots _threeDots_;

    public ASeparatedAtLeastExpression() {
    }

    public ASeparatedAtLeastExpression(@SuppressWarnings("hiding") PExpression _base_, @SuppressWarnings("hiding") TSeparatorKeyword _separatorKeyword_, @SuppressWarnings("hiding") PExpression _separator_, @SuppressWarnings("hiding") TCaret _caret_, @SuppressWarnings("hiding") TNumber _number_, @SuppressWarnings("hiding") TThreeDots _threeDots_) {
        setBase(_base_);
        setSeparatorKeyword(_separatorKeyword_);
        setSeparator(_separator_);
        setCaret(_caret_);
        setNumber(_number_);
        setThreeDots(_threeDots_);
    }

    @Override
    public Object clone() {
        return new ASeparatedAtLeastExpression(cloneNode(this._base_), cloneNode(this._separatorKeyword_), cloneNode(this._separator_), cloneNode(this._caret_), cloneNode(this._number_), cloneNode(this._threeDots_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASeparatedAtLeastExpression(this);
    }

    public PExpression getBase() {
        return this._base_;
    }

    public void setBase(PExpression node) {
        if (this._base_ != null) {
            this._base_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._base_ = node;
    }

    public TSeparatorKeyword getSeparatorKeyword() {
        return this._separatorKeyword_;
    }

    public void setSeparatorKeyword(TSeparatorKeyword node) {
        if (this._separatorKeyword_ != null) {
            this._separatorKeyword_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._separatorKeyword_ = node;
    }

    public PExpression getSeparator() {
        return this._separator_;
    }

    public void setSeparator(PExpression node) {
        if (this._separator_ != null) {
            this._separator_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._separator_ = node;
    }

    public TCaret getCaret() {
        return this._caret_;
    }

    public void setCaret(TCaret node) {
        if (this._caret_ != null) {
            this._caret_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._caret_ = node;
    }

    public TNumber getNumber() {
        return this._number_;
    }

    public void setNumber(TNumber node) {
        if (this._number_ != null) {
            this._number_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._number_ = node;
    }

    public TThreeDots getThreeDots() {
        return this._threeDots_;
    }

    public void setThreeDots(TThreeDots node) {
        if (this._threeDots_ != null) {
            this._threeDots_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._threeDots_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._base_) + toString(this._separatorKeyword_) + toString(this._separator_) + toString(this._caret_) + toString(this._number_) + toString(this._threeDots_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._base_ == child) {
            this._base_ = null;
            return;
        }
        if (this._separatorKeyword_ == child) {
            this._separatorKeyword_ = null;
            return;
        }
        if (this._separator_ == child) {
            this._separator_ = null;
            return;
        }
        if (this._caret_ == child) {
            this._caret_ = null;
            return;
        }
        if (this._number_ == child) {
            this._number_ = null;
            return;
        }
        if (this._threeDots_ == child) {
            this._threeDots_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._base_ == oldChild) {
            setBase((PExpression) newChild);
            return;
        }
        if (this._separatorKeyword_ == oldChild) {
            setSeparatorKeyword((TSeparatorKeyword) newChild);
            return;
        }
        if (this._separator_ == oldChild) {
            setSeparator((PExpression) newChild);
            return;
        }
        if (this._caret_ == oldChild) {
            setCaret((TCaret) newChild);
            return;
        }
        if (this._number_ == oldChild) {
            setNumber((TNumber) newChild);
            return;
        }
        if (this._threeDots_ == oldChild) {
            setThreeDots((TThreeDots) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
