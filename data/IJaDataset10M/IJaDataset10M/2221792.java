package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("nls")
public final class ATypeMethodDeclaration extends PMethodDeclaration {

    private final LinkedList<PModifier> _modifier_ = new LinkedList<PModifier>();

    private PTypeParameters _typeParameters_;

    private PType _type_;

    private TIdentifier _identifier_;

    private PFormalParameters _formalParameters_;

    private final LinkedList<PDim> _dim_ = new LinkedList<PDim>();

    private PThrowsException _throwsException_;

    private PMethodBody _methodBody_;

    public ATypeMethodDeclaration() {
    }

    public ATypeMethodDeclaration(@SuppressWarnings("hiding") List<PModifier> _modifier_, @SuppressWarnings("hiding") PTypeParameters _typeParameters_, @SuppressWarnings("hiding") PType _type_, @SuppressWarnings("hiding") TIdentifier _identifier_, @SuppressWarnings("hiding") PFormalParameters _formalParameters_, @SuppressWarnings("hiding") List<PDim> _dim_, @SuppressWarnings("hiding") PThrowsException _throwsException_, @SuppressWarnings("hiding") PMethodBody _methodBody_) {
        setModifier(_modifier_);
        setTypeParameters(_typeParameters_);
        setType(_type_);
        setIdentifier(_identifier_);
        setFormalParameters(_formalParameters_);
        setDim(_dim_);
        setThrowsException(_throwsException_);
        setMethodBody(_methodBody_);
    }

    @Override
    public Object clone() {
        return new ATypeMethodDeclaration(cloneList(this._modifier_), cloneNode(this._typeParameters_), cloneNode(this._type_), cloneNode(this._identifier_), cloneNode(this._formalParameters_), cloneList(this._dim_), cloneNode(this._throwsException_), cloneNode(this._methodBody_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseATypeMethodDeclaration(this);
    }

    public LinkedList<PModifier> getModifier() {
        return this._modifier_;
    }

    public void setModifier(List<PModifier> list) {
        this._modifier_.clear();
        this._modifier_.addAll(list);
        for (PModifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public PTypeParameters getTypeParameters() {
        return this._typeParameters_;
    }

    public void setTypeParameters(PTypeParameters node) {
        if (this._typeParameters_ != null) {
            this._typeParameters_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._typeParameters_ = node;
    }

    public PType getType() {
        return this._type_;
    }

    public void setType(PType node) {
        if (this._type_ != null) {
            this._type_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._type_ = node;
    }

    public TIdentifier getIdentifier() {
        return this._identifier_;
    }

    public void setIdentifier(TIdentifier node) {
        if (this._identifier_ != null) {
            this._identifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier_ = node;
    }

    public PFormalParameters getFormalParameters() {
        return this._formalParameters_;
    }

    public void setFormalParameters(PFormalParameters node) {
        if (this._formalParameters_ != null) {
            this._formalParameters_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._formalParameters_ = node;
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

    public PThrowsException getThrowsException() {
        return this._throwsException_;
    }

    public void setThrowsException(PThrowsException node) {
        if (this._throwsException_ != null) {
            this._throwsException_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._throwsException_ = node;
    }

    public PMethodBody getMethodBody() {
        return this._methodBody_;
    }

    public void setMethodBody(PMethodBody node) {
        if (this._methodBody_ != null) {
            this._methodBody_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._methodBody_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._modifier_) + toString(this._typeParameters_) + toString(this._type_) + toString(this._identifier_) + toString(this._formalParameters_) + toString(this._dim_) + toString(this._throwsException_) + toString(this._methodBody_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._modifier_.remove(child)) {
            return;
        }
        if (this._typeParameters_ == child) {
            this._typeParameters_ = null;
            return;
        }
        if (this._type_ == child) {
            this._type_ = null;
            return;
        }
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        if (this._formalParameters_ == child) {
            this._formalParameters_ = null;
            return;
        }
        if (this._dim_.remove(child)) {
            return;
        }
        if (this._throwsException_ == child) {
            this._throwsException_ = null;
            return;
        }
        if (this._methodBody_ == child) {
            this._methodBody_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        for (ListIterator<PModifier> i = this._modifier_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PModifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._typeParameters_ == oldChild) {
            setTypeParameters((PTypeParameters) newChild);
            return;
        }
        if (this._type_ == oldChild) {
            setType((PType) newChild);
            return;
        }
        if (this._identifier_ == oldChild) {
            setIdentifier((TIdentifier) newChild);
            return;
        }
        if (this._formalParameters_ == oldChild) {
            setFormalParameters((PFormalParameters) newChild);
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
        if (this._throwsException_ == oldChild) {
            setThrowsException((PThrowsException) newChild);
            return;
        }
        if (this._methodBody_ == oldChild) {
            setMethodBody((PMethodBody) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
