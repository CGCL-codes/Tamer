package org.jdmp.core.script.jdmp.node;

import java.util.*;
import org.jdmp.core.script.jdmp.analysis.*;

@SuppressWarnings("nls")
public final class ARow extends PRow {

    private PExpression _expression_;

    private final LinkedList<PCommaValue> _additionalValues_ = new LinkedList<PCommaValue>();

    public ARow() {
    }

    public ARow(@SuppressWarnings("hiding") PExpression _expression_, @SuppressWarnings("hiding") List<PCommaValue> _additionalValues_) {
        setExpression(_expression_);
        setAdditionalValues(_additionalValues_);
    }

    public Object clone() {
        return new ARow(cloneNode(this._expression_), cloneList(this._additionalValues_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseARow(this);
    }

    public PExpression getExpression() {
        return this._expression_;
    }

    public void setExpression(PExpression node) {
        if (this._expression_ != null) {
            this._expression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._expression_ = node;
    }

    public LinkedList<PCommaValue> getAdditionalValues() {
        return this._additionalValues_;
    }

    public void setAdditionalValues(List<PCommaValue> list) {
        this._additionalValues_.clear();
        this._additionalValues_.addAll(list);
        for (PCommaValue e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public String toString() {
        return "" + toString(this._expression_) + toString(this._additionalValues_);
    }

    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._expression_ == child) {
            this._expression_ = null;
            return;
        }
        if (this._additionalValues_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._expression_ == oldChild) {
            setExpression((PExpression) newChild);
            return;
        }
        for (ListIterator<PCommaValue> i = this._additionalValues_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PCommaValue) newChild);
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
