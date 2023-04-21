package iwork.patchpanel.manager.script.node;

import java.util.*;
import iwork.patchpanel.manager.script.analysis.*;

public final class AConstExpCaseExpression extends PCaseExpression {

    private PConstantExpression _constantExpression_;

    public AConstExpCaseExpression() {
    }

    public AConstExpCaseExpression(PConstantExpression _constantExpression_) {
        setConstantExpression(_constantExpression_);
    }

    public Object clone() {
        return new AConstExpCaseExpression((PConstantExpression) cloneNode(_constantExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAConstExpCaseExpression(this);
    }

    public PConstantExpression getConstantExpression() {
        return _constantExpression_;
    }

    public void setConstantExpression(PConstantExpression node) {
        if (_constantExpression_ != null) {
            _constantExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _constantExpression_ = node;
    }

    public String toString() {
        return "" + toString(_constantExpression_);
    }

    void removeChild(Node child) {
        if (_constantExpression_ == child) {
            _constantExpression_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_constantExpression_ == oldChild) {
            setConstantExpression((PConstantExpression) newChild);
            return;
        }
    }
}
