package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AInstanceofNameInstanceofExpNn extends PInstanceofExpNn {

    private PName _name_;

    private TInstanceof _instanceof_;

    private PReferenceType _referenceType_;

    public AInstanceofNameInstanceofExpNn() {
    }

    public AInstanceofNameInstanceofExpNn(@SuppressWarnings("hiding") PName _name_, @SuppressWarnings("hiding") TInstanceof _instanceof_, @SuppressWarnings("hiding") PReferenceType _referenceType_) {
        setName(_name_);
        setInstanceof(_instanceof_);
        setReferenceType(_referenceType_);
    }

    @Override
    public Object clone() {
        return new AInstanceofNameInstanceofExpNn(cloneNode(this._name_), cloneNode(this._instanceof_), cloneNode(this._referenceType_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAInstanceofNameInstanceofExpNn(this);
    }

    public PName getName() {
        return this._name_;
    }

    public void setName(PName node) {
        if (this._name_ != null) {
            this._name_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._name_ = node;
    }

    public TInstanceof getInstanceof() {
        return this._instanceof_;
    }

    public void setInstanceof(TInstanceof node) {
        if (this._instanceof_ != null) {
            this._instanceof_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._instanceof_ = node;
    }

    public PReferenceType getReferenceType() {
        return this._referenceType_;
    }

    public void setReferenceType(PReferenceType node) {
        if (this._referenceType_ != null) {
            this._referenceType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._referenceType_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._name_) + toString(this._instanceof_) + toString(this._referenceType_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._name_ == child) {
            this._name_ = null;
            return;
        }
        if (this._instanceof_ == child) {
            this._instanceof_ = null;
            return;
        }
        if (this._referenceType_ == child) {
            this._referenceType_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._name_ == oldChild) {
            setName((PName) newChild);
            return;
        }
        if (this._instanceof_ == oldChild) {
            setInstanceof((TInstanceof) newChild);
            return;
        }
        if (this._referenceType_ == oldChild) {
            setReferenceType((PReferenceType) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
