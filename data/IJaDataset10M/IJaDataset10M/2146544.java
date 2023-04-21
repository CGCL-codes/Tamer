package org.sablecc.sablecc.launcher.syntax3.node;

import java.util.*;
import org.sablecc.sablecc.launcher.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class AShortOptionsArgument extends PArgument {

    private final LinkedList<PShortOption> _shortOptions_ = new LinkedList<PShortOption>();

    public AShortOptionsArgument() {
    }

    public AShortOptionsArgument(@SuppressWarnings("hiding") List<PShortOption> _shortOptions_) {
        setShortOptions(_shortOptions_);
    }

    @Override
    public Object clone() {
        return new AShortOptionsArgument(cloneList(this._shortOptions_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAShortOptionsArgument(this);
    }

    public LinkedList<PShortOption> getShortOptions() {
        return this._shortOptions_;
    }

    public void setShortOptions(List<PShortOption> list) {
        this._shortOptions_.clear();
        this._shortOptions_.addAll(list);
        for (PShortOption e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._shortOptions_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._shortOptions_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        for (ListIterator<PShortOption> i = this._shortOptions_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PShortOption) newChild);
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
