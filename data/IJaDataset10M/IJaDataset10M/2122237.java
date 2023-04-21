package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AStarMuIdMultiplicativeExpression extends PMultiplicativeExpression {

    private PMultiplicativeExpression _multiplicativeExpression_;

    private TStar _star_;

    private TIdentifier _identifier_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers_ = new LinkedList<PAdditionalIdentifier>();

    public AStarMuIdMultiplicativeExpression() {
    }

    public AStarMuIdMultiplicativeExpression(@SuppressWarnings("hiding") PMultiplicativeExpression _multiplicativeExpression_, @SuppressWarnings("hiding") TStar _star_, @SuppressWarnings("hiding") TIdentifier _identifier_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers_) {
        setMultiplicativeExpression(_multiplicativeExpression_);
        setStar(_star_);
        setIdentifier(_identifier_);
        setAdditionalIdentifiers(_additionalIdentifiers_);
    }

    @Override
    public Object clone() {
        return new AStarMuIdMultiplicativeExpression(cloneNode(this._multiplicativeExpression_), cloneNode(this._star_), cloneNode(this._identifier_), cloneList(this._additionalIdentifiers_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAStarMuIdMultiplicativeExpression(this);
    }

    public PMultiplicativeExpression getMultiplicativeExpression() {
        return this._multiplicativeExpression_;
    }

    public void setMultiplicativeExpression(PMultiplicativeExpression node) {
        if (this._multiplicativeExpression_ != null) {
            this._multiplicativeExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._multiplicativeExpression_ = node;
    }

    public TStar getStar() {
        return this._star_;
    }

    public void setStar(TStar node) {
        if (this._star_ != null) {
            this._star_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._star_ = node;
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
        return "" + toString(this._multiplicativeExpression_) + toString(this._star_) + toString(this._identifier_) + toString(this._additionalIdentifiers_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._multiplicativeExpression_ == child) {
            this._multiplicativeExpression_ = null;
            return;
        }
        if (this._star_ == child) {
            this._star_ = null;
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
        if (this._multiplicativeExpression_ == oldChild) {
            setMultiplicativeExpression((PMultiplicativeExpression) newChild);
            return;
        }
        if (this._star_ == oldChild) {
            setStar((TStar) newChild);
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
