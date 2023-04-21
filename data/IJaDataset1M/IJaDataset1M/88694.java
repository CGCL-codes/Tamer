package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AComplementUnaryExpNotPlusMinus extends PUnaryExpNotPlusMinus {

    private TComplement _complement_;

    private PUnaryExp _unaryExp_;

    public AComplementUnaryExpNotPlusMinus() {
    }

    public AComplementUnaryExpNotPlusMinus(@SuppressWarnings("hiding") TComplement _complement_, @SuppressWarnings("hiding") PUnaryExp _unaryExp_) {
        setComplement(_complement_);
        setUnaryExp(_unaryExp_);
    }

    @Override
    public Object clone() {
        return new AComplementUnaryExpNotPlusMinus(cloneNode(this._complement_), cloneNode(this._unaryExp_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAComplementUnaryExpNotPlusMinus(this);
    }

    public TComplement getComplement() {
        return this._complement_;
    }

    public void setComplement(TComplement node) {
        if (this._complement_ != null) {
            this._complement_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._complement_ = node;
    }

    public PUnaryExp getUnaryExp() {
        return this._unaryExp_;
    }

    public void setUnaryExp(PUnaryExp node) {
        if (this._unaryExp_ != null) {
            this._unaryExp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._unaryExp_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._complement_) + toString(this._unaryExp_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._complement_ == child) {
            this._complement_ = null;
            return;
        }
        if (this._unaryExp_ == child) {
            this._unaryExp_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._complement_ == oldChild) {
            setComplement((TComplement) newChild);
            return;
        }
        if (this._unaryExp_ == oldChild) {
            setUnaryExp((PUnaryExp) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
