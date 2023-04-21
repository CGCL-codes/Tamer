package iwork.patchpanel.manager.script.node;

import java.util.*;
import iwork.patchpanel.manager.script.analysis.*;

public final class AVarExpExpression extends PExpression {

    private TStringLiteral _stringLiteral_;

    public AVarExpExpression() {
    }

    public AVarExpExpression(TStringLiteral _stringLiteral_) {
        setStringLiteral(_stringLiteral_);
    }

    public Object clone() {
        return new AVarExpExpression((TStringLiteral) cloneNode(_stringLiteral_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAVarExpExpression(this);
    }

    public TStringLiteral getStringLiteral() {
        return _stringLiteral_;
    }

    public void setStringLiteral(TStringLiteral node) {
        if (_stringLiteral_ != null) {
            _stringLiteral_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _stringLiteral_ = node;
    }

    public String toString() {
        return "" + toString(_stringLiteral_);
    }

    void removeChild(Node child) {
        if (_stringLiteral_ == child) {
            _stringLiteral_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_stringLiteral_ == oldChild) {
            setStringLiteral((TStringLiteral) newChild);
            return;
        }
    }
}
