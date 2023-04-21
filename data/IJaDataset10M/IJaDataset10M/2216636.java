package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AEqEqIdEqualityExpression extends PEqualityExpression {

    private PEqualityExpression _equalityExpression_;

    private TEq _eq_;

    private TIdentifier _identifier_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers_ = new LinkedList<PAdditionalIdentifier>();

    public AEqEqIdEqualityExpression() {
    }

    public AEqEqIdEqualityExpression(@SuppressWarnings("hiding") PEqualityExpression _equalityExpression_, @SuppressWarnings("hiding") TEq _eq_, @SuppressWarnings("hiding") TIdentifier _identifier_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers_) {
        setEqualityExpression(_equalityExpression_);
        setEq(_eq_);
        setIdentifier(_identifier_);
        setAdditionalIdentifiers(_additionalIdentifiers_);
    }

    @Override
    public Object clone() {
        return new AEqEqIdEqualityExpression(cloneNode(this._equalityExpression_), cloneNode(this._eq_), cloneNode(this._identifier_), cloneList(this._additionalIdentifiers_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAEqEqIdEqualityExpression(this);
    }

    public PEqualityExpression getEqualityExpression() {
        return this._equalityExpression_;
    }

    public void setEqualityExpression(PEqualityExpression node) {
        if (this._equalityExpression_ != null) {
            this._equalityExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._equalityExpression_ = node;
    }

    public TEq getEq() {
        return this._eq_;
    }

    public void setEq(TEq node) {
        if (this._eq_ != null) {
            this._eq_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._eq_ = node;
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

    @Override
    public String toString() {
        return "" + toString(this._equalityExpression_) + toString(this._eq_) + toString(this._identifier_) + toString(this._additionalIdentifiers_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._equalityExpression_ == child) {
            this._equalityExpression_ = null;
            return;
        }
        if (this._eq_ == child) {
            this._eq_ = null;
            return;
        }
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        if (this._additionalIdentifiers_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._equalityExpression_ == oldChild) {
            setEqualityExpression((PEqualityExpression) newChild);
            return;
        }
        if (this._eq_ == oldChild) {
            setEq((TEq) newChild);
            return;
        }
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
        throw new RuntimeException("Not a child.");
    }
}
