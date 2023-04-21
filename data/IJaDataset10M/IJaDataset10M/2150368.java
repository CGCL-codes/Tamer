package iwork.patchpanel.manager.script.node;

import java.util.*;
import iwork.patchpanel.manager.script.analysis.*;

public final class AFloatFloatingPointType extends PFloatingPointType {

    private TFloat _float_;

    public AFloatFloatingPointType() {
    }

    public AFloatFloatingPointType(TFloat _float_) {
        setFloat(_float_);
    }

    public Object clone() {
        return new AFloatFloatingPointType((TFloat) cloneNode(_float_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAFloatFloatingPointType(this);
    }

    public TFloat getFloat() {
        return _float_;
    }

    public void setFloat(TFloat node) {
        if (_float_ != null) {
            _float_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _float_ = node;
    }

    public String toString() {
        return "" + toString(_float_);
    }

    void removeChild(Node child) {
        if (_float_ == child) {
            _float_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_float_ == oldChild) {
            setFloat((TFloat) newChild);
            return;
        }
    }
}
