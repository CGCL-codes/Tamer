package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ASimpleMethodInvocation extends PMethodInvocation {

    private TIdentifier _identifier_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers_ = new LinkedList<PAdditionalIdentifier>();

    private TLPar _lPar_;

    private PArgumentList _argumentList_;

    private TRPar _rPar_;

    public ASimpleMethodInvocation() {
    }

    public ASimpleMethodInvocation(@SuppressWarnings("hiding") TIdentifier _identifier_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers_, @SuppressWarnings("hiding") TLPar _lPar_, @SuppressWarnings("hiding") PArgumentList _argumentList_, @SuppressWarnings("hiding") TRPar _rPar_) {
        setIdentifier(_identifier_);
        setAdditionalIdentifiers(_additionalIdentifiers_);
        setLPar(_lPar_);
        setArgumentList(_argumentList_);
        setRPar(_rPar_);
    }

    @Override
    public Object clone() {
        return new ASimpleMethodInvocation(cloneNode(this._identifier_), cloneList(this._additionalIdentifiers_), cloneNode(this._lPar_), cloneNode(this._argumentList_), cloneNode(this._rPar_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASimpleMethodInvocation(this);
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

    public LinkedList<PAdditionalIdentifier> getAdditionalIdentifiers() {
        return this._additionalIdentifiers_;
    }

    public void setAdditionalIdentifiers(List<PAdditionalIdentifier> list) {
        this._additionalIdentifiers_.clear();
        this._additionalIdentifiers_.addAll(list);
        for (PAdditionalIdentifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
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

    public PArgumentList getArgumentList() {
        return this._argumentList_;
    }

    public void setArgumentList(PArgumentList node) {
        if (this._argumentList_ != null) {
            this._argumentList_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._argumentList_ = node;
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
        return "" + toString(this._identifier_) + toString(this._additionalIdentifiers_) + toString(this._lPar_) + toString(this._argumentList_) + toString(this._rPar_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        if (this._additionalIdentifiers_.remove(child)) {
            return;
        }
        if (this._lPar_ == child) {
            this._lPar_ = null;
            return;
        }
        if (this._argumentList_ == child) {
            this._argumentList_ = null;
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
        if (this._identifier_ == oldChild) {
            setIdentifier((TIdentifier) newChild);
            return;
        }
        for (ListIterator<PAdditionalIdentifier> i = this._additionalIdentifiers_.listIterator(); i.hasNext(); ) {
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
        if (this._lPar_ == oldChild) {
            setLPar((TLPar) newChild);
            return;
        }
        if (this._argumentList_ == oldChild) {
            setArgumentList((PArgumentList) newChild);
            return;
        }
        if (this._rPar_ == oldChild) {
            setRPar((TRPar) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
