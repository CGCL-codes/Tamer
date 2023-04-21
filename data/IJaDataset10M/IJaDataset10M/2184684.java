package org.sablecc.sablecc.intermediate.syntax3.node;

import java.util.*;
import org.sablecc.sablecc.intermediate.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class ASymbol extends PSymbol {

    private TString _name_;

    private final LinkedList<PInterval> _intervals_ = new LinkedList<PInterval>();

    public ASymbol() {
    }

    public ASymbol(@SuppressWarnings("hiding") TString _name_, @SuppressWarnings("hiding") List<PInterval> _intervals_) {
        setName(_name_);
        setIntervals(_intervals_);
    }

    @Override
    public Object clone() {
        return new ASymbol(cloneNode(this._name_), cloneList(this._intervals_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASymbol(this);
    }

    public TString getName() {
        return this._name_;
    }

    public void setName(TString node) {
        if (this._name_ != null) {
            this._name_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._name_ = node;
    }

    public LinkedList<PInterval> getIntervals() {
        return this._intervals_;
    }

    public void setIntervals(List<PInterval> list) {
        this._intervals_.clear();
        this._intervals_.addAll(list);
        for (PInterval e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._name_) + toString(this._intervals_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._name_ == child) {
            this._name_ = null;
            return;
        }
        if (this._intervals_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._name_ == oldChild) {
            setName((TString) newChild);
            return;
        }
        for (ListIterator<PInterval> i = this._intervals_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PInterval) newChild);
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
