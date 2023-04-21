package org.sablecc.objectmacro.syntax3.node;

import org.sablecc.objectmacro.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class ATextBlockSourceFilePart extends PSourceFilePart {

    private PTextBlock _textBlock_;

    public ATextBlockSourceFilePart() {
    }

    public ATextBlockSourceFilePart(@SuppressWarnings("hiding") PTextBlock _textBlock_) {
        setTextBlock(_textBlock_);
    }

    @Override
    public Object clone() {
        return new ATextBlockSourceFilePart(cloneNode(this._textBlock_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseATextBlockSourceFilePart(this);
    }

    public PTextBlock getTextBlock() {
        return this._textBlock_;
    }

    public void setTextBlock(PTextBlock node) {
        if (this._textBlock_ != null) {
            this._textBlock_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._textBlock_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._textBlock_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._textBlock_ == child) {
            this._textBlock_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._textBlock_ == oldChild) {
            setTextBlock((PTextBlock) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
