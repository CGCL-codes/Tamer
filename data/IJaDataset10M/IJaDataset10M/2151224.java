package org.sablecc.sablecc.syntax3.node;

import java.util.*;
import org.sablecc.sablecc.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class ASelectionParserProduction extends PParserProduction {

    private final LinkedList<TIdentifier> _names_ = new LinkedList<TIdentifier>();

    private TAssign _assign_;

    private TIdentifier _selectorName_;

    private TIdentifier _parameter_;

    public ASelectionParserProduction() {
    }

    public ASelectionParserProduction(@SuppressWarnings("hiding") List<TIdentifier> _names_, @SuppressWarnings("hiding") TAssign _assign_, @SuppressWarnings("hiding") TIdentifier _selectorName_, @SuppressWarnings("hiding") TIdentifier _parameter_) {
        setNames(_names_);
        setAssign(_assign_);
        setSelectorName(_selectorName_);
        setParameter(_parameter_);
    }

    @Override
    public Object clone() {
        return new ASelectionParserProduction(cloneList(this._names_), cloneNode(this._assign_), cloneNode(this._selectorName_), cloneNode(this._parameter_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASelectionParserProduction(this);
    }

    public LinkedList<TIdentifier> getNames() {
        return this._names_;
    }

    public void setNames(List<TIdentifier> list) {
        this._names_.clear();
        this._names_.addAll(list);
        for (TIdentifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public TAssign getAssign() {
        return this._assign_;
    }

    public void setAssign(TAssign node) {
        if (this._assign_ != null) {
            this._assign_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._assign_ = node;
    }

    public TIdentifier getSelectorName() {
        return this._selectorName_;
    }

    public void setSelectorName(TIdentifier node) {
        if (this._selectorName_ != null) {
            this._selectorName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._selectorName_ = node;
    }

    public TIdentifier getParameter() {
        return this._parameter_;
    }

    public void setParameter(TIdentifier node) {
        if (this._parameter_ != null) {
            this._parameter_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._parameter_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._names_) + toString(this._assign_) + toString(this._selectorName_) + toString(this._parameter_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._names_.remove(child)) {
            return;
        }
        if (this._assign_ == child) {
            this._assign_ = null;
            return;
        }
        if (this._selectorName_ == child) {
            this._selectorName_ = null;
            return;
        }
        if (this._parameter_ == child) {
            this._parameter_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        for (ListIterator<TIdentifier> i = this._names_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((TIdentifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._assign_ == oldChild) {
            setAssign((TAssign) newChild);
            return;
        }
        if (this._selectorName_ == oldChild) {
            setSelectorName((TIdentifier) newChild);
            return;
        }
        if (this._parameter_ == oldChild) {
            setParameter((TIdentifier) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
