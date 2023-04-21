package iwork.patchpanel.manager.script.node;

import java.util.*;
import iwork.patchpanel.manager.script.analysis.*;

public final class AInVarName extends PVarName {

    private TIn _in_;

    private TDot _dot_;

    private PIdentifier _identifier_;

    public AInVarName() {
    }

    public AInVarName(TIn _in_, TDot _dot_, PIdentifier _identifier_) {
        setIn(_in_);
        setDot(_dot_);
        setIdentifier(_identifier_);
    }

    public Object clone() {
        return new AInVarName((TIn) cloneNode(_in_), (TDot) cloneNode(_dot_), (PIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAInVarName(this);
    }

    public TIn getIn() {
        return _in_;
    }

    public void setIn(TIn node) {
        if (_in_ != null) {
            _in_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _in_ = node;
    }

    public TDot getDot() {
        return _dot_;
    }

    public void setDot(TDot node) {
        if (_dot_ != null) {
            _dot_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _dot_ = node;
    }

    public PIdentifier getIdentifier() {
        return _identifier_;
    }

    public void setIdentifier(PIdentifier node) {
        if (_identifier_ != null) {
            _identifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _identifier_ = node;
    }

    public String toString() {
        return "" + toString(_in_) + toString(_dot_) + toString(_identifier_);
    }

    void removeChild(Node child) {
        if (_in_ == child) {
            _in_ = null;
            return;
        }
        if (_dot_ == child) {
            _dot_ = null;
            return;
        }
        if (_identifier_ == child) {
            _identifier_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_in_ == oldChild) {
            setIn((TIn) newChild);
            return;
        }
        if (_dot_ == oldChild) {
            setDot((TDot) newChild);
            return;
        }
        if (_identifier_ == oldChild) {
            setIdentifier((PIdentifier) newChild);
            return;
        }
    }
}
