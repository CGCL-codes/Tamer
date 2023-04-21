package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AManyActualTypeArgumentListShr extends PActualTypeArgumentListShr {

    private PActualTypeArgumentList _actualTypeArgumentList_;

    private TComma _comma_;

    private PActualTypeArgumentShr _actualTypeArgumentShr_;

    public AManyActualTypeArgumentListShr() {
    }

    public AManyActualTypeArgumentListShr(@SuppressWarnings("hiding") PActualTypeArgumentList _actualTypeArgumentList_, @SuppressWarnings("hiding") TComma _comma_, @SuppressWarnings("hiding") PActualTypeArgumentShr _actualTypeArgumentShr_) {
        setActualTypeArgumentList(_actualTypeArgumentList_);
        setComma(_comma_);
        setActualTypeArgumentShr(_actualTypeArgumentShr_);
    }

    @Override
    public Object clone() {
        return new AManyActualTypeArgumentListShr(cloneNode(this._actualTypeArgumentList_), cloneNode(this._comma_), cloneNode(this._actualTypeArgumentShr_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAManyActualTypeArgumentListShr(this);
    }

    public PActualTypeArgumentList getActualTypeArgumentList() {
        return this._actualTypeArgumentList_;
    }

    public void setActualTypeArgumentList(PActualTypeArgumentList node) {
        if (this._actualTypeArgumentList_ != null) {
            this._actualTypeArgumentList_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._actualTypeArgumentList_ = node;
    }

    public TComma getComma() {
        return this._comma_;
    }

    public void setComma(TComma node) {
        if (this._comma_ != null) {
            this._comma_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._comma_ = node;
    }

    public PActualTypeArgumentShr getActualTypeArgumentShr() {
        return this._actualTypeArgumentShr_;
    }

    public void setActualTypeArgumentShr(PActualTypeArgumentShr node) {
        if (this._actualTypeArgumentShr_ != null) {
            this._actualTypeArgumentShr_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._actualTypeArgumentShr_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._actualTypeArgumentList_) + toString(this._comma_) + toString(this._actualTypeArgumentShr_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._actualTypeArgumentList_ == child) {
            this._actualTypeArgumentList_ = null;
            return;
        }
        if (this._comma_ == child) {
            this._comma_ = null;
            return;
        }
        if (this._actualTypeArgumentShr_ == child) {
            this._actualTypeArgumentShr_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._actualTypeArgumentList_ == oldChild) {
            setActualTypeArgumentList((PActualTypeArgumentList) newChild);
            return;
        }
        if (this._comma_ == oldChild) {
            setComma((TComma) newChild);
            return;
        }
        if (this._actualTypeArgumentShr_ == oldChild) {
            setActualTypeArgumentShr((PActualTypeArgumentShr) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
