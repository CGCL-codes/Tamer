package net.sf.cb2xml.sablecc.node;

import java.util.*;
import net.sf.cb2xml.sablecc.analysis.*;

public final class AFillerDataNameOrFiller extends PDataNameOrFiller {

    private TFiller _filler_;

    public AFillerDataNameOrFiller() {
    }

    public AFillerDataNameOrFiller(TFiller _filler_) {
        setFiller(_filler_);
    }

    public Object clone() {
        return new AFillerDataNameOrFiller((TFiller) cloneNode(_filler_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAFillerDataNameOrFiller(this);
    }

    public TFiller getFiller() {
        return _filler_;
    }

    public void setFiller(TFiller node) {
        if (_filler_ != null) {
            _filler_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _filler_ = node;
    }

    public String toString() {
        return "" + toString(_filler_);
    }

    void removeChild(Node child) {
        if (_filler_ == child) {
            _filler_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_filler_ == oldChild) {
            setFiller((TFiller) newChild);
            return;
        }
    }
}
