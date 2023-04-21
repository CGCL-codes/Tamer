package iwork.patchpanel.node;

import java.util.*;
import iwork.patchpanel.analysis.*;

public final class ACompletmentUnaryExpression extends PUnaryExpression {

    private TComplement _complement_;

    private PUnaryExpression _unaryExpression_;

    public ACompletmentUnaryExpression() {
    }

    public ACompletmentUnaryExpression(TComplement _complement_, PUnaryExpression _unaryExpression_) {
        setComplement(_complement_);
        setUnaryExpression(_unaryExpression_);
    }

    public Object clone() {
        return new ACompletmentUnaryExpression((TComplement) cloneNode(_complement_), (PUnaryExpression) cloneNode(_unaryExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseACompletmentUnaryExpression(this);
    }

    public TComplement getComplement() {
        return _complement_;
    }

    public void setComplement(TComplement node) {
        if (_complement_ != null) {
            _complement_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _complement_ = node;
    }

    public PUnaryExpression getUnaryExpression() {
        return _unaryExpression_;
    }

    public void setUnaryExpression(PUnaryExpression node) {
        if (_unaryExpression_ != null) {
            _unaryExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _unaryExpression_ = node;
    }

    public String toString() {
        return "" + toString(_complement_) + toString(_unaryExpression_);
    }

    void removeChild(Node child) {
        if (_complement_ == child) {
            _complement_ = null;
            return;
        }
        if (_unaryExpression_ == child) {
            _unaryExpression_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_complement_ == oldChild) {
            setComplement((TComplement) newChild);
            return;
        }
        if (_unaryExpression_ == oldChild) {
            setUnaryExpression((PUnaryExpression) newChild);
            return;
        }
    }
}
