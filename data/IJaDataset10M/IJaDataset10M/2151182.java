package org.sableccsupport.scclexer.node;

import java.util.*;
import org.sableccsupport.scclexer.analysis.*;

@SuppressWarnings("nls")
public final class AConcat extends PConcat {

    private final LinkedList<PUnExp> _unExps_ = new LinkedList<PUnExp>();

    public AConcat() {
    }

    public AConcat(@SuppressWarnings("hiding") List<PUnExp> _unExps_) {
        setUnExps(_unExps_);
    }

    @Override
    public Object clone() {
        return new AConcat(cloneList(this._unExps_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAConcat(this);
    }

    public LinkedList<PUnExp> getUnExps() {
        return this._unExps_;
    }

    public void setUnExps(List<PUnExp> list) {
        this._unExps_.clear();
        this._unExps_.addAll(list);
        for (PUnExp e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._unExps_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._unExps_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        for (ListIterator<PUnExp> i = this._unExps_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PUnExp) newChild);
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
