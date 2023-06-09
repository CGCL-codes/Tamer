package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

public final class X1PIndexedByPhrase extends XPIndexedByPhrase {

    private XPIndexedByPhrase _xPIndexedByPhrase_;

    private PIndexedByPhrase _pIndexedByPhrase_;

    public X1PIndexedByPhrase() {
    }

    public X1PIndexedByPhrase(XPIndexedByPhrase _xPIndexedByPhrase_, PIndexedByPhrase _pIndexedByPhrase_) {
        setXPIndexedByPhrase(_xPIndexedByPhrase_);
        setPIndexedByPhrase(_pIndexedByPhrase_);
    }

    public Object clone() {
        throw new RuntimeException("Unsupported Operation");
    }

    public void apply(Switch sw) {
        throw new RuntimeException("Switch not supported.");
    }

    public XPIndexedByPhrase getXPIndexedByPhrase() {
        return _xPIndexedByPhrase_;
    }

    public void setXPIndexedByPhrase(XPIndexedByPhrase node) {
        if (_xPIndexedByPhrase_ != null) {
            _xPIndexedByPhrase_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _xPIndexedByPhrase_ = node;
    }

    public PIndexedByPhrase getPIndexedByPhrase() {
        return _pIndexedByPhrase_;
    }

    public void setPIndexedByPhrase(PIndexedByPhrase node) {
        if (_pIndexedByPhrase_ != null) {
            _pIndexedByPhrase_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _pIndexedByPhrase_ = node;
    }

    void removeChild(Node child) {
        if (_xPIndexedByPhrase_ == child) {
            _xPIndexedByPhrase_ = null;
        }
        if (_pIndexedByPhrase_ == child) {
            _pIndexedByPhrase_ = null;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
    }

    public String toString() {
        return "" + toString(_xPIndexedByPhrase_) + toString(_pIndexedByPhrase_);
    }
}
