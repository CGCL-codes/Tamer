package org.sablecc.sablecc.node;

import java.util.*;
import org.sablecc.sablecc.analysis.*;

public final class AHexChar extends PChar {

    private THexChar _hexChar_;

    public AHexChar() {
    }

    public AHexChar(THexChar _hexChar_) {
        setHexChar(_hexChar_);
    }

    public Object clone() {
        return new AHexChar((THexChar) cloneNode(_hexChar_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAHexChar(this);
    }

    public THexChar getHexChar() {
        return _hexChar_;
    }

    public void setHexChar(THexChar node) {
        if (_hexChar_ != null) {
            _hexChar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _hexChar_ = node;
    }

    public String toString() {
        return "" + toString(_hexChar_);
    }

    void removeChild(Node child) {
        if (_hexChar_ == child) {
            _hexChar_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_hexChar_ == oldChild) {
            setHexChar((THexChar) newChild);
            return;
        }
    }
}
