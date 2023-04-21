package org.sablecc.objectmacro.intermediate.syntax3.node;

import org.sablecc.objectmacro.intermediate.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class AStringInlineText extends PInlineText {

    private TString _string_;

    public AStringInlineText() {
    }

    public AStringInlineText(@SuppressWarnings("hiding") TString _string_) {
        setString(_string_);
    }

    @Override
    public Object clone() {
        return new AStringInlineText(cloneNode(this._string_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAStringInlineText(this);
    }

    public TString getString() {
        return this._string_;
    }

    public void setString(TString node) {
        if (this._string_ != null) {
            this._string_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._string_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._string_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._string_ == child) {
            this._string_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._string_ == oldChild) {
            setString((TString) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
