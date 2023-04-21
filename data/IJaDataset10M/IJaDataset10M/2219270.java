package comp.logo.node;

import comp.logo.analysis.*;

@SuppressWarnings("nls")
public final class ASetxyPGraphics extends PPGraphics {

    private PPSetxy _pSetxy_;

    public ASetxyPGraphics() {
    }

    public ASetxyPGraphics(@SuppressWarnings("hiding") PPSetxy _pSetxy_) {
        setPSetxy(_pSetxy_);
    }

    @Override
    public Object clone() {
        return new ASetxyPGraphics(cloneNode(this._pSetxy_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASetxyPGraphics(this);
    }

    public PPSetxy getPSetxy() {
        return this._pSetxy_;
    }

    public void setPSetxy(PPSetxy node) {
        if (this._pSetxy_ != null) {
            this._pSetxy_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._pSetxy_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._pSetxy_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._pSetxy_ == child) {
            this._pSetxy_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._pSetxy_ == oldChild) {
            setPSetxy((PPSetxy) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
