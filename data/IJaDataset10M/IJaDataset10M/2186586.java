package comp.logo.node;

import comp.logo.analysis.*;

@SuppressWarnings("nls")
public final class ARtsPGraphics extends PPGraphics {

    private PPRight _pRight_;

    public ARtsPGraphics() {
    }

    public ARtsPGraphics(@SuppressWarnings("hiding") PPRight _pRight_) {
        setPRight(_pRight_);
    }

    @Override
    public Object clone() {
        return new ARtsPGraphics(cloneNode(this._pRight_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseARtsPGraphics(this);
    }

    public PPRight getPRight() {
        return this._pRight_;
    }

    public void setPRight(PPRight node) {
        if (this._pRight_ != null) {
            this._pRight_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._pRight_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._pRight_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._pRight_ == child) {
            this._pRight_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._pRight_ == oldChild) {
            setPRight((PPRight) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
