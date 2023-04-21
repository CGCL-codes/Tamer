package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("nls")
public final class APrimitiveTypePrimaryNoNewArray extends PPrimaryNoNewArray {

    private PPrimitiveType _primitiveType_;

    private final LinkedList<PDim> _dim_ = new LinkedList<PDim>();

    private TDot _dot_;

    private TClazz _clazz_;

    public APrimitiveTypePrimaryNoNewArray() {
    }

    public APrimitiveTypePrimaryNoNewArray(@SuppressWarnings("hiding") PPrimitiveType _primitiveType_, @SuppressWarnings("hiding") List<PDim> _dim_, @SuppressWarnings("hiding") TDot _dot_, @SuppressWarnings("hiding") TClazz _clazz_) {
        setPrimitiveType(_primitiveType_);
        setDim(_dim_);
        setDot(_dot_);
        setClazz(_clazz_);
    }

    @Override
    public Object clone() {
        return new APrimitiveTypePrimaryNoNewArray(cloneNode(this._primitiveType_), cloneList(this._dim_), cloneNode(this._dot_), cloneNode(this._clazz_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPrimitiveTypePrimaryNoNewArray(this);
    }

    public PPrimitiveType getPrimitiveType() {
        return this._primitiveType_;
    }

    public void setPrimitiveType(PPrimitiveType node) {
        if (this._primitiveType_ != null) {
            this._primitiveType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._primitiveType_ = node;
    }

    public LinkedList<PDim> getDim() {
        return this._dim_;
    }

    public void setDim(List<PDim> list) {
        this._dim_.clear();
        this._dim_.addAll(list);
        for (PDim e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public TDot getDot() {
        return this._dot_;
    }

    public void setDot(TDot node) {
        if (this._dot_ != null) {
            this._dot_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._dot_ = node;
    }

    public TClazz getClazz() {
        return this._clazz_;
    }

    public void setClazz(TClazz node) {
        if (this._clazz_ != null) {
            this._clazz_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._clazz_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._primitiveType_) + toString(this._dim_) + toString(this._dot_) + toString(this._clazz_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._primitiveType_ == child) {
            this._primitiveType_ = null;
            return;
        }
        if (this._dim_.remove(child)) {
            return;
        }
        if (this._dot_ == child) {
            this._dot_ = null;
            return;
        }
        if (this._clazz_ == child) {
            this._clazz_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._primitiveType_ == oldChild) {
            setPrimitiveType((PPrimitiveType) newChild);
            return;
        }
        for (ListIterator<PDim> i = this._dim_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PDim) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._dot_ == oldChild) {
            setDot((TDot) newChild);
            return;
        }
        if (this._clazz_ == oldChild) {
            setClazz((TClazz) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
