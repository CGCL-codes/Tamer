package org.sablecc.sablecc.node;

import org.sablecc.sablecc.analysis.*;
import org.sablecc.sablecc.node.Node;
import org.sablecc.sablecc.node.PUnExp;
import org.sablecc.sablecc.node.Switch;
import org.sablecc.sablecc.node.XPUnExp;

public final class X2PUnExp extends XPUnExp {

    private PUnExp _pUnExp_;

    public X2PUnExp() {
    }

    public X2PUnExp(PUnExp _pUnExp_) {
        setPUnExp(_pUnExp_);
    }

    public Object clone() {
        throw new RuntimeException("Unsupported Operation");
    }

    public void apply(Switch sw) {
        throw new RuntimeException("Switch not supported.");
    }

    public PUnExp getPUnExp() {
        return _pUnExp_;
    }

    public void setPUnExp(PUnExp node) {
        if (_pUnExp_ != null) {
            _pUnExp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _pUnExp_ = node;
    }

    void removeChild(Node child) {
        if (_pUnExp_ == child) {
            _pUnExp_ = null;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
    }

    public String toString() {
        return "" + toString(_pUnExp_);
    }
}
