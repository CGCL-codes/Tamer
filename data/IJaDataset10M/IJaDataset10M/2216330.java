package org.jdmp.core.script.jdmp.node;

import java.util.*;
import org.jdmp.core.script.jdmp.analysis.*;

@SuppressWarnings("nls")
public final class AArray extends PArray {

    private PRow _row_;

    private final LinkedList<PSemicolonRow> _additionalRows_ = new LinkedList<PSemicolonRow>();

    public AArray() {
    }

    public AArray(@SuppressWarnings("hiding") PRow _row_, @SuppressWarnings("hiding") List<PSemicolonRow> _additionalRows_) {
        setRow(_row_);
        setAdditionalRows(_additionalRows_);
    }

    public Object clone() {
        return new AArray(cloneNode(this._row_), cloneList(this._additionalRows_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAArray(this);
    }

    public PRow getRow() {
        return this._row_;
    }

    public void setRow(PRow node) {
        if (this._row_ != null) {
            this._row_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._row_ = node;
    }

    public LinkedList<PSemicolonRow> getAdditionalRows() {
        return this._additionalRows_;
    }

    public void setAdditionalRows(List<PSemicolonRow> list) {
        this._additionalRows_.clear();
        this._additionalRows_.addAll(list);
        for (PSemicolonRow e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public String toString() {
        return "" + toString(this._row_) + toString(this._additionalRows_);
    }

    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._row_ == child) {
            this._row_ = null;
            return;
        }
        if (this._additionalRows_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._row_ == oldChild) {
            setRow((PRow) newChild);
            return;
        }
        for (ListIterator<PSemicolonRow> i = this._additionalRows_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PSemicolonRow) newChild);
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
