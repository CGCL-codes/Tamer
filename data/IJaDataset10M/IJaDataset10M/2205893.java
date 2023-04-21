package org.sablecc.objectmacro.intermediate.syntax3.node;

import java.util.*;
import org.sablecc.objectmacro.intermediate.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class AIntermediateRepresentation extends PIntermediateRepresentation {

    private final LinkedList<PText> _texts_ = new LinkedList<PText>();

    private final LinkedList<PMacro> _macros_ = new LinkedList<PMacro>();

    public AIntermediateRepresentation() {
    }

    public AIntermediateRepresentation(@SuppressWarnings("hiding") List<PText> _texts_, @SuppressWarnings("hiding") List<PMacro> _macros_) {
        setTexts(_texts_);
        setMacros(_macros_);
    }

    @Override
    public Object clone() {
        return new AIntermediateRepresentation(cloneList(this._texts_), cloneList(this._macros_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAIntermediateRepresentation(this);
    }

    public LinkedList<PText> getTexts() {
        return this._texts_;
    }

    public void setTexts(List<PText> list) {
        this._texts_.clear();
        this._texts_.addAll(list);
        for (PText e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public LinkedList<PMacro> getMacros() {
        return this._macros_;
    }

    public void setMacros(List<PMacro> list) {
        this._macros_.clear();
        this._macros_.addAll(list);
        for (PMacro e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._texts_) + toString(this._macros_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._texts_.remove(child)) {
            return;
        }
        if (this._macros_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        for (ListIterator<PText> i = this._texts_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PText) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        for (ListIterator<PMacro> i = this._macros_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PMacro) newChild);
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
