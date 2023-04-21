package org.omwg.mediation.parser.hrsyntax.node;

import org.omwg.mediation.parser.hrsyntax.analysis.*;

public final class X2PParam extends XPParam {

    private PParam _pParam_;

    public X2PParam() {
    }

    public X2PParam(PParam _pParam_) {
        setPParam(_pParam_);
    }

    public Object clone() {
        throw new RuntimeException("Unsupported Operation");
    }

    public void apply(Switch sw) {
        throw new RuntimeException("Switch not supported.");
    }

    public PParam getPParam() {
        return _pParam_;
    }

    public void setPParam(PParam node) {
        if (_pParam_ != null) {
            _pParam_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _pParam_ = node;
    }

    void removeChild(Node child) {
        if (_pParam_ == child) {
            _pParam_ = null;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
    }

    public String toString() {
        return "" + toString(_pParam_);
    }
}
