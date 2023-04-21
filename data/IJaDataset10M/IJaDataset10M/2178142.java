package iwork.patchpanel.manager.script.node;

import java.util.*;
import iwork.patchpanel.manager.script.analysis.*;

public final class AIfElseStmtActionLine extends PActionLine {

    private PIfElseStatement _ifElseStatement_;

    public AIfElseStmtActionLine() {
    }

    public AIfElseStmtActionLine(PIfElseStatement _ifElseStatement_) {
        setIfElseStatement(_ifElseStatement_);
    }

    public Object clone() {
        return new AIfElseStmtActionLine((PIfElseStatement) cloneNode(_ifElseStatement_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAIfElseStmtActionLine(this);
    }

    public PIfElseStatement getIfElseStatement() {
        return _ifElseStatement_;
    }

    public void setIfElseStatement(PIfElseStatement node) {
        if (_ifElseStatement_ != null) {
            _ifElseStatement_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _ifElseStatement_ = node;
    }

    public String toString() {
        return "" + toString(_ifElseStatement_);
    }

    void removeChild(Node child) {
        if (_ifElseStatement_ == child) {
            _ifElseStatement_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_ifElseStatement_ == oldChild) {
            setIfElseStatement((PIfElseStatement) newChild);
            return;
        }
    }
}
