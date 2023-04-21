package iwork.patchpanel.node;

import java.util.*;
import iwork.patchpanel.analysis.*;

public final class AEqExpression extends PEqExpression {

    private TAssign _assign_;

    private TLParenthese _lParenthese_;

    private PType _type_;

    private TRParenthese _rParenthese_;

    private PExpression _expression_;

    public AEqExpression() {
    }

    public AEqExpression(TAssign _assign_, TLParenthese _lParenthese_, PType _type_, TRParenthese _rParenthese_, PExpression _expression_) {
        setAssign(_assign_);
        setLParenthese(_lParenthese_);
        setType(_type_);
        setRParenthese(_rParenthese_);
        setExpression(_expression_);
    }

    public Object clone() {
        return new AEqExpression((TAssign) cloneNode(_assign_), (TLParenthese) cloneNode(_lParenthese_), (PType) cloneNode(_type_), (TRParenthese) cloneNode(_rParenthese_), (PExpression) cloneNode(_expression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAEqExpression(this);
    }

    public TAssign getAssign() {
        return _assign_;
    }

    public void setAssign(TAssign node) {
        if (_assign_ != null) {
            _assign_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _assign_ = node;
    }

    public TLParenthese getLParenthese() {
        return _lParenthese_;
    }

    public void setLParenthese(TLParenthese node) {
        if (_lParenthese_ != null) {
            _lParenthese_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _lParenthese_ = node;
    }

    public PType getType() {
        return _type_;
    }

    public void setType(PType node) {
        if (_type_ != null) {
            _type_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _type_ = node;
    }

    public TRParenthese getRParenthese() {
        return _rParenthese_;
    }

    public void setRParenthese(TRParenthese node) {
        if (_rParenthese_ != null) {
            _rParenthese_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _rParenthese_ = node;
    }

    public PExpression getExpression() {
        return _expression_;
    }

    public void setExpression(PExpression node) {
        if (_expression_ != null) {
            _expression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _expression_ = node;
    }

    public String toString() {
        return "" + toString(_assign_) + toString(_lParenthese_) + toString(_type_) + toString(_rParenthese_) + toString(_expression_);
    }

    void removeChild(Node child) {
        if (_assign_ == child) {
            _assign_ = null;
            return;
        }
        if (_lParenthese_ == child) {
            _lParenthese_ = null;
            return;
        }
        if (_type_ == child) {
            _type_ = null;
            return;
        }
        if (_rParenthese_ == child) {
            _rParenthese_ = null;
            return;
        }
        if (_expression_ == child) {
            _expression_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_assign_ == oldChild) {
            setAssign((TAssign) newChild);
            return;
        }
        if (_lParenthese_ == oldChild) {
            setLParenthese((TLParenthese) newChild);
            return;
        }
        if (_type_ == oldChild) {
            setType((PType) newChild);
            return;
        }
        if (_rParenthese_ == oldChild) {
            setRParenthese((TRParenthese) newChild);
            return;
        }
        if (_expression_ == oldChild) {
            setExpression((PExpression) newChild);
            return;
        }
    }
}
