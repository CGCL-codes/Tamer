package org.sablecc.sablecc.syntax3.node;

import java.util.*;
import org.sablecc.sablecc.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class ATreeAlternative extends PTreeAlternative {

    private TAlternativeName _alternativeName_;

    private final LinkedList<PElement> _elements_ = new LinkedList<PElement>();

    public ATreeAlternative() {
    }

    public ATreeAlternative(@SuppressWarnings("hiding") TAlternativeName _alternativeName_, @SuppressWarnings("hiding") List<PElement> _elements_) {
        setAlternativeName(_alternativeName_);
        setElements(_elements_);
    }

    @Override
    public Object clone() {
        return new ATreeAlternative(cloneNode(this._alternativeName_), cloneList(this._elements_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseATreeAlternative(this);
    }

    public TAlternativeName getAlternativeName() {
        return this._alternativeName_;
    }

    public void setAlternativeName(TAlternativeName node) {
        if (this._alternativeName_ != null) {
            this._alternativeName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._alternativeName_ = node;
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
        return "" + toString(this._alternativeName_) + toString(this._elements_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._alternativeName_ == child) {
            this._alternativeName_ = null;
            return;
        }
        if (this._elements_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._alternativeName_ == oldChild) {
            setAlternativeName((TAlternativeName) newChild);
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
