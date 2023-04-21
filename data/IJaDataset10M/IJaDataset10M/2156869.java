package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AUshrShiftExp extends PShiftExp {

    private PShiftExp _shiftExp_;

    private TUshr _ushr_;

    private PAdditiveExp _additiveExp_;

    public AUshrShiftExp() {
    }

    public AUshrShiftExp(@SuppressWarnings("hiding") PShiftExp _shiftExp_, @SuppressWarnings("hiding") TUshr _ushr_, @SuppressWarnings("hiding") PAdditiveExp _additiveExp_) {
        setShiftExp(_shiftExp_);
        setUshr(_ushr_);
        setAdditiveExp(_additiveExp_);
    }

    @Override
    public Object clone() {
        return new AUshrShiftExp(cloneNode(this._shiftExp_), cloneNode(this._ushr_), cloneNode(this._additiveExp_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAUshrShiftExp(this);
    }

    public PShiftExp getShiftExp() {
        return this._shiftExp_;
    }

    public void setShiftExp(PShiftExp node) {
        if (this._shiftExp_ != null) {
            this._shiftExp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._shiftExp_ = node;
    }

    public TUshr getUshr() {
        return this._ushr_;
    }

    public void setUshr(TUshr node) {
        if (this._ushr_ != null) {
            this._ushr_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._ushr_ = node;
    }

    public PAdditiveExp getAdditiveExp() {
        return this._additiveExp_;
    }

    public void setAdditiveExp(PAdditiveExp node) {
        if (this._additiveExp_ != null) {
            this._additiveExp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._additiveExp_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._shiftExp_) + toString(this._ushr_) + toString(this._additiveExp_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._shiftExp_ == child) {
            this._shiftExp_ = null;
            return;
        }
        if (this._ushr_ == child) {
            this._ushr_ = null;
            return;
        }
        if (this._additiveExp_ == child) {
            this._additiveExp_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._shiftExp_ == oldChild) {
            setShiftExp((PShiftExp) newChild);
            return;
        }
        if (this._ushr_ == oldChild) {
            setUshr((TUshr) newChild);
            return;
        }
        if (this._additiveExp_ == oldChild) {
            setAdditiveExp((PAdditiveExp) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
