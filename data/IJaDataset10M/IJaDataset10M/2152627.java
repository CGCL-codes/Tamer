package iwork.patchpanel.manager.script.node;

import java.util.*;
import iwork.patchpanel.manager.script.analysis.*;

public final class ACompoundStatement extends PCompoundStatement {

    private TLBrace _lBrace_;

    private final LinkedList _actionLine_ = new TypedLinkedList(new ActionLine_Cast());

    private TRBrace _rBrace_;

    public ACompoundStatement() {
    }

    public ACompoundStatement(TLBrace _lBrace_, List _actionLine_, TRBrace _rBrace_) {
        setLBrace(_lBrace_);
        {
            this._actionLine_.clear();
            this._actionLine_.addAll(_actionLine_);
        }
        setRBrace(_rBrace_);
    }

    public ACompoundStatement(TLBrace _lBrace_, XPActionLine _actionLine_, TRBrace _rBrace_) {
        setLBrace(_lBrace_);
        if (_actionLine_ != null) {
            while (_actionLine_ instanceof X1PActionLine) {
                this._actionLine_.addFirst(((X1PActionLine) _actionLine_).getPActionLine());
                _actionLine_ = ((X1PActionLine) _actionLine_).getXPActionLine();
            }
            this._actionLine_.addFirst(((X2PActionLine) _actionLine_).getPActionLine());
        }
        setRBrace(_rBrace_);
    }

    public Object clone() {
        return new ACompoundStatement((TLBrace) cloneNode(_lBrace_), cloneList(_actionLine_), (TRBrace) cloneNode(_rBrace_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseACompoundStatement(this);
    }

    public TLBrace getLBrace() {
        return _lBrace_;
    }

    public void setLBrace(TLBrace node) {
        if (_lBrace_ != null) {
            _lBrace_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _lBrace_ = node;
    }

    public LinkedList getActionLine() {
        return _actionLine_;
    }

    public void setActionLine(List list) {
        _actionLine_.clear();
        _actionLine_.addAll(list);
    }

    public TRBrace getRBrace() {
        return _rBrace_;
    }

    public void setRBrace(TRBrace node) {
        if (_rBrace_ != null) {
            _rBrace_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _rBrace_ = node;
    }

    public String toString() {
        return "" + toString(_lBrace_) + toString(_actionLine_) + toString(_rBrace_);
    }

    void removeChild(Node child) {
        if (_lBrace_ == child) {
            _lBrace_ = null;
            return;
        }
        if (_actionLine_.remove(child)) {
            return;
        }
        if (_rBrace_ == child) {
            _rBrace_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_lBrace_ == oldChild) {
            setLBrace((TLBrace) newChild);
            return;
        }
        for (ListIterator i = _actionLine_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set(newChild);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (_rBrace_ == oldChild) {
            setRBrace((TRBrace) newChild);
            return;
        }
    }

    private class ActionLine_Cast implements Cast {

        public Object cast(Object o) {
            PActionLine node = (PActionLine) o;
            if ((node.parent() != null) && (node.parent() != ACompoundStatement.this)) {
                node.parent().removeChild(node);
            }
            if ((node.parent() == null) || (node.parent() != ACompoundStatement.this)) {
                node.parent(ACompoundStatement.this);
            }
            return node;
        }
    }
}
