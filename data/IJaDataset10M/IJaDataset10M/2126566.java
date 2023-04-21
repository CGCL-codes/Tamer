package org.omwg.mediation.parser.hrsyntax.node;

import java.util.*;
import org.omwg.mediation.parser.hrsyntax.analysis.*;

public final class AArityVal extends PArityVal {

    private TPosInt _posInt_;

    public AArityVal() {
    }

    public AArityVal(TPosInt _posInt_) {
        setPosInt(_posInt_);
    }

    public Object clone() {
        return new AArityVal((TPosInt) cloneNode(_posInt_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAArityVal(this);
    }

    public TPosInt getPosInt() {
        return _posInt_;
    }

    public void setPosInt(TPosInt node) {
        if (_posInt_ != null) {
            _posInt_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _posInt_ = node;
    }

    public String toString() {
        return "" + toString(_posInt_);
    }

    void removeChild(Node child) {
        if (_posInt_ == child) {
            _posInt_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_posInt_ == oldChild) {
            setPosInt((TPosInt) newChild);
            return;
        }
    }
}
