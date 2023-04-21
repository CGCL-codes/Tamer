package jpdl.patterns.parser.node;

import jpdl.patterns.parser.analysis.*;

@SuppressWarnings("nls")
public final class APathArgItem extends PArgItem {

    private TPathPattern _pathPattern_;

    public APathArgItem() {
    }

    public APathArgItem(@SuppressWarnings("hiding") TPathPattern _pathPattern_) {
        setPathPattern(_pathPattern_);
    }

    @Override
    public Object clone() {
        return new APathArgItem(cloneNode(this._pathPattern_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPathArgItem(this);
    }

    public TPathPattern getPathPattern() {
        return this._pathPattern_;
    }

    public void setPathPattern(TPathPattern node) {
        if (this._pathPattern_ != null) {
            this._pathPattern_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._pathPattern_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._pathPattern_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._pathPattern_ == child) {
            this._pathPattern_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._pathPattern_ == oldChild) {
            setPathPattern((TPathPattern) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
