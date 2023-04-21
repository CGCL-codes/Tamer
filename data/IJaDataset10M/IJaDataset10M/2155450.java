package org.sablecc.sablecc.syntax3.node;

import java.util.*;
import org.sablecc.sablecc.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class AProductionTransformation extends PProductionTransformation {

    private TIdentifier _production_;

    private TArrow _arrow_;

    private final LinkedList<PElement> _elements_ = new LinkedList<PElement>();

    public AProductionTransformation() {
    }

    public AProductionTransformation(@SuppressWarnings("hiding") TIdentifier _production_, @SuppressWarnings("hiding") TArrow _arrow_, @SuppressWarnings("hiding") List<PElement> _elements_) {
        setProduction(_production_);
        setArrow(_arrow_);
        setElements(_elements_);
    }

    @Override
    public Object clone() {
        return new AProductionTransformation(cloneNode(this._production_), cloneNode(this._arrow_), cloneList(this._elements_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAProductionTransformation(this);
    }

    public TIdentifier getProduction() {
        return this._production_;
    }

    public void setProduction(TIdentifier node) {
        if (this._production_ != null) {
            this._production_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._production_ = node;
    }

    public TArrow getArrow() {
        return this._arrow_;
    }

    public void setArrow(TArrow node) {
        if (this._arrow_ != null) {
            this._arrow_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._arrow_ = node;
    }

    public LinkedList<PElement> getElements() {
        return this._elements_;
    }

    public void setElements(List<PElement> list) {
        this._elements_.clear();
        this._elements_.addAll(list);
        for (PElement e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._production_) + toString(this._arrow_) + toString(this._elements_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._production_ == child) {
            this._production_ = null;
            return;
        }
        if (this._arrow_ == child) {
            this._arrow_ = null;
            return;
        }
        if (this._elements_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._production_ == oldChild) {
            setProduction((TIdentifier) newChild);
            return;
        }
        if (this._arrow_ == oldChild) {
            setArrow((TArrow) newChild);
            return;
        }
        for (ListIterator<PElement> i = this._elements_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PElement) newChild);
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
