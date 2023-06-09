package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AReferenceIdentifierCastExpression extends PCastExpression {

    private TLPar _lPar_;

    private TIdentifier _identifier1_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers1_ = new LinkedList<PAdditionalIdentifier>();

    private final LinkedList<PTypeComponent> _typeComponents_ = new LinkedList<PTypeComponent>();

    private PTypeArguments _typeArguments_;

    private final LinkedList<PDim> _dims_ = new LinkedList<PDim>();

    private TRPar _rPar_;

    private TIdentifier _identifier2_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers2_ = new LinkedList<PAdditionalIdentifier>();

    public AReferenceIdentifierCastExpression() {
    }

    public AReferenceIdentifierCastExpression(@SuppressWarnings("hiding") TLPar _lPar_, @SuppressWarnings("hiding") TIdentifier _identifier1_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers1_, @SuppressWarnings("hiding") List<PTypeComponent> _typeComponents_, @SuppressWarnings("hiding") PTypeArguments _typeArguments_, @SuppressWarnings("hiding") List<PDim> _dims_, @SuppressWarnings("hiding") TRPar _rPar_, @SuppressWarnings("hiding") TIdentifier _identifier2_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers2_) {
        setLPar(_lPar_);
        setIdentifier1(_identifier1_);
        setAdditionalIdentifiers1(_additionalIdentifiers1_);
        setTypeComponents(_typeComponents_);
        setTypeArguments(_typeArguments_);
        setDims(_dims_);
        setRPar(_rPar_);
        setIdentifier2(_identifier2_);
        setAdditionalIdentifiers2(_additionalIdentifiers2_);
    }

    @Override
    public Object clone() {
        return new AReferenceIdentifierCastExpression(cloneNode(this._lPar_), cloneNode(this._identifier1_), cloneList(this._additionalIdentifiers1_), cloneList(this._typeComponents_), cloneNode(this._typeArguments_), cloneList(this._dims_), cloneNode(this._rPar_), cloneNode(this._identifier2_), cloneList(this._additionalIdentifiers2_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAReferenceIdentifierCastExpression(this);
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

    public TIdentifier getIdentifier1() {
        return this._identifier1_;
    }

    public void setIdentifier1(TIdentifier node) {
        if (this._identifier1_ != null) {
            this._identifier1_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier1_ = node;
    }

    public LinkedList<PAdditionalIdentifier> getAdditionalIdentifiers1() {
        return this._additionalIdentifiers1_;
    }

    public void setAdditionalIdentifiers1(List<PAdditionalIdentifier> list) {
        this._additionalIdentifiers1_.clear();
        this._additionalIdentifiers1_.addAll(list);
        for (PAdditionalIdentifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public LinkedList<PTypeComponent> getTypeComponents() {
        return this._typeComponents_;
    }

    public void setTypeComponents(List<PTypeComponent> list) {
        this._typeComponents_.clear();
        this._typeComponents_.addAll(list);
        for (PTypeComponent e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public PTypeArguments getTypeArguments() {
        return this._typeArguments_;
    }

    public void setTypeArguments(PTypeArguments node) {
        if (this._typeArguments_ != null) {
            this._typeArguments_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._typeArguments_ = node;
    }

    public LinkedList<PDim> getDims() {
        return this._dims_;
    }

    public void setDims(List<PDim> list) {
        this._dims_.clear();
        this._dims_.addAll(list);
        for (PDim e : list) {
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

    public TIdentifier getIdentifier2() {
        return this._identifier2_;
    }

    public void setIdentifier2(TIdentifier node) {
        if (this._identifier2_ != null) {
            this._identifier2_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier2_ = node;
    }

    public LinkedList<PAdditionalIdentifier> getAdditionalIdentifiers2() {
        return this._additionalIdentifiers2_;
    }

    public void setAdditionalIdentifiers2(List<PAdditionalIdentifier> list) {
        this._additionalIdentifiers2_.clear();
        this._additionalIdentifiers2_.addAll(list);
        for (PAdditionalIdentifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._lPar_) + toString(this._identifier1_) + toString(this._additionalIdentifiers1_) + toString(this._typeComponents_) + toString(this._typeArguments_) + toString(this._dims_) + toString(this._rPar_) + toString(this._identifier2_) + toString(this._additionalIdentifiers2_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._lPar_ == child) {
            this._lPar_ = null;
            return;
        }
        if (this._identifier1_ == child) {
            this._identifier1_ = null;
            return;
        }
        if (this._additionalIdentifiers1_.remove(child)) {
            return;
        }
        if (this._typeComponents_.remove(child)) {
            return;
        }
        if (this._typeArguments_ == child) {
            this._typeArguments_ = null;
            return;
        }
        if (this._dims_.remove(child)) {
            return;
        }
        if (this._rPar_ == child) {
            this._rPar_ = null;
            return;
        }
        if (this._identifier2_ == child) {
            this._identifier2_ = null;
            return;
        }
        if (this._additionalIdentifiers2_.remove(child)) {
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
        if (this._identifier1_ == oldChild) {
            setIdentifier1((TIdentifier) newChild);
            return;
        }
        for (ListIterator<PAdditionalIdentifier> i = this._additionalIdentifiers1_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PAdditionalIdentifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        for (ListIterator<PTypeComponent> i = this._typeComponents_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PTypeComponent) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._typeArguments_ == oldChild) {
            setTypeArguments((PTypeArguments) newChild);
            return;
        }
        for (ListIterator<PDim> i = this._dims_.listIterator(); i.hasNext(); ) {
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
        if (this._rPar_ == oldChild) {
            setRPar((TRPar) newChild);
            return;
        }
        if (this._identifier2_ == oldChild) {
            setIdentifier2((TIdentifier) newChild);
            return;
        }
        for (ListIterator<PAdditionalIdentifier> i = this._additionalIdentifiers2_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PAdditionalIdentifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        throw new RuntimeException("Not a child.");
    }
}
