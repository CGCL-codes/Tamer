package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AByteIntegralType extends PIntegralType {

    private TByte _byte_;

    public AByteIntegralType() {
    }

    public AByteIntegralType(@SuppressWarnings("hiding") TByte _byte_) {
        setByte(_byte_);
    }

    @Override
    public Object clone() {
        return new AByteIntegralType(cloneNode(this._byte_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAByteIntegralType(this);
    }

    public TByte getByte() {
        return this._byte_;
    }

    public void setByte(TByte node) {
        if (this._byte_ != null) {
            this._byte_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._byte_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._byte_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._byte_ == child) {
            this._byte_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._byte_ == oldChild) {
            setByte((TByte) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
