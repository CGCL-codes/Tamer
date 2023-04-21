package org.sablecc.sablecc.syntax3.node;

import java.util.*;
import org.sablecc.sablecc.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class AListTransformationElement extends PTransformationElement {

    private TLPar _lPar_;

    private final LinkedList<PListElement> _listElements_ = new LinkedList<PListElement>();

    private TRPar _rPar_;

    public AListTransformationElement() {
    }

    public AListTransformationElement(@SuppressWarnings("hiding") TLPar _lPar_, @SuppressWarnings("hiding") List<PListElement> _listElements_, @SuppressWarnings("hiding") TRPar _rPar_) {
        setLPar(_lPar_);
        setListElements(_listElements_);
        setRPar(_rPar_);
    }

    @Override
    public Object clone() {
        return new AListTransformationElement(cloneNode(this._lPar_), cloneList(this._listElements_), cloneNode(this._rPar_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAListTransformationElement(this);
    }

    public TLPar getLPar() {
        return this._lPar_;
    }

    public void setLPar(TLPar node) {
        if (this._lPar_ != null) {
            this._lPar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._lPar_ = node;
    }

    public LinkedList<PListElement> getListElements() {
        return this._listElements_;
    }

    public void setListElements(List<PListElement> list) {
        this._listElements_.clear();
        this._listElements_.addAll(list);
        for (PListElement e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public TRPar getRPar() {
        return this._rPar_;
    }

    public void setRPar(TRPar node) {
        if (this._rPar_ != null) {
            this._rPar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._rPar_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._lPar_) + toString(this._listElements_) + toString(this._rPar_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._lPar_ == child) {
            this._lPar_ = null;
            return;
        }
        if (this._listElements_.remove(child)) {
            return;
        }
        if (this._rPar_ == child) {
            this._rPar_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._lPar_ == oldChild) {
            setLPar((TLPar) newChild);
            return;
        }
        for (ListIterator<PListElement> i = this._listElements_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PListElement) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._rPar_ == oldChild) {
            setRPar((TRPar) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
