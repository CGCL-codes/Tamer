package org.sablecc.objectmacro.intermediate.syntax3.node;

import org.sablecc.objectmacro.intermediate.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class ATextInsertValue extends PValue {

    private PTextInsert _textInsert_;

    public ATextInsertValue() {
    }

    public ATextInsertValue(@SuppressWarnings("hiding") PTextInsert _textInsert_) {
        setTextInsert(_textInsert_);
    }

    @Override
    public Object clone() {
        return new ATextInsertValue(cloneNode(this._textInsert_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseATextInsertValue(this);
    }

    public PTextInsert getTextInsert() {
        return this._textInsert_;
    }

    public void setTextInsert(PTextInsert node) {
        if (this._textInsert_ != null) {
            this._textInsert_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._textInsert_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._textInsert_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._textInsert_ == child) {
            this._textInsert_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._textInsert_ == oldChild) {
            setTextInsert((PTextInsert) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
