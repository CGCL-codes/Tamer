package iwork.patchpanel.manager.script.node;

import java.util.*;
import iwork.patchpanel.manager.script.analysis.*;

public final class AFloatingPointTypeNumericType extends PNumericType {

    private PFloatingPointType _floatingPointType_;

    public AFloatingPointTypeNumericType() {
    }

    public AFloatingPointTypeNumericType(PFloatingPointType _floatingPointType_) {
        setFloatingPointType(_floatingPointType_);
    }

    public Object clone() {
        return new AFloatingPointTypeNumericType((PFloatingPointType) cloneNode(_floatingPointType_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAFloatingPointTypeNumericType(this);
    }

    public PFloatingPointType getFloatingPointType() {
        return _floatingPointType_;
    }

    public void setFloatingPointType(PFloatingPointType node) {
        if (_floatingPointType_ != null) {
            _floatingPointType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _floatingPointType_ = node;
    }

    public String toString() {
        return "" + toString(_floatingPointType_);
    }

    void removeChild(Node child) {
        if (_floatingPointType_ == child) {
            _floatingPointType_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_floatingPointType_ == oldChild) {
            setFloatingPointType((PFloatingPointType) newChild);
            return;
        }
    }
}
