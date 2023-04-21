package comp.logo.node;

import comp.logo.analysis.*;

@SuppressWarnings("nls")
public final class AVarsPPrimitives extends PPPrimitives {

    private PPWord _pWord_;

    public AVarsPPrimitives() {
    }

    public AVarsPPrimitives(@SuppressWarnings("hiding") PPWord _pWord_) {
        setPWord(_pWord_);
    }

    @Override
    public Object clone() {
        return new AVarsPPrimitives(cloneNode(this._pWord_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAVarsPPrimitives(this);
    }

    public PPWord getPWord() {
        return this._pWord_;
    }

    public void setPWord(PPWord node) {
        if (this._pWord_ != null) {
            this._pWord_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._pWord_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._pWord_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._pWord_ == child) {
            this._pWord_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._pWord_ == oldChild) {
            setPWord((PPWord) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
