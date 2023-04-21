package iwork.patchpanel.manager.script.node;

import java.util.*;
import iwork.patchpanel.manager.script.analysis.*;

public final class AExclInclRange extends PRange {

    private TLParenthese _lParenthese_;

    private PLowerbound _lowerbound_;

    private TComma _comma_;

    private PUpperbound _upperbound_;

    private TRBracket _rBracket_;

    public AExclInclRange() {
    }

    public AExclInclRange(TLParenthese _lParenthese_, PLowerbound _lowerbound_, TComma _comma_, PUpperbound _upperbound_, TRBracket _rBracket_) {
        setLParenthese(_lParenthese_);
        setLowerbound(_lowerbound_);
        setComma(_comma_);
        setUpperbound(_upperbound_);
        setRBracket(_rBracket_);
    }

    public Object clone() {
        return new AExclInclRange((TLParenthese) cloneNode(_lParenthese_), (PLowerbound) cloneNode(_lowerbound_), (TComma) cloneNode(_comma_), (PUpperbound) cloneNode(_upperbound_), (TRBracket) cloneNode(_rBracket_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAExclInclRange(this);
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

    public PLowerbound getLowerbound() {
        return _lowerbound_;
    }

    public void setLowerbound(PLowerbound node) {
        if (_lowerbound_ != null) {
            _lowerbound_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _lowerbound_ = node;
    }

    public TComma getComma() {
        return _comma_;
    }

    public void setComma(TComma node) {
        if (_comma_ != null) {
            _comma_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _comma_ = node;
    }

    public PUpperbound getUpperbound() {
        return _upperbound_;
    }

    public void setUpperbound(PUpperbound node) {
        if (_upperbound_ != null) {
            _upperbound_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _upperbound_ = node;
    }

    public TRBracket getRBracket() {
        return _rBracket_;
    }

    public void setRBracket(TRBracket node) {
        if (_rBracket_ != null) {
            _rBracket_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _rBracket_ = node;
    }

    public String toString() {
        return "" + toString(_lParenthese_) + toString(_lowerbound_) + toString(_comma_) + toString(_upperbound_) + toString(_rBracket_);
    }

    void removeChild(Node child) {
        if (_lParenthese_ == child) {
            _lParenthese_ = null;
            return;
        }
        if (_lowerbound_ == child) {
            _lowerbound_ = null;
            return;
        }
        if (_comma_ == child) {
            _comma_ = null;
            return;
        }
        if (_upperbound_ == child) {
            _upperbound_ = null;
            return;
        }
        if (_rBracket_ == child) {
            _rBracket_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_lParenthese_ == oldChild) {
            setLParenthese((TLParenthese) newChild);
            return;
        }
        if (_lowerbound_ == oldChild) {
            setLowerbound((PLowerbound) newChild);
            return;
        }
        if (_comma_ == oldChild) {
            setComma((TComma) newChild);
            return;
        }
        if (_upperbound_ == oldChild) {
            setUpperbound((PUpperbound) newChild);
            return;
        }
        if (_rBracket_ == oldChild) {
            setRBracket((TRBracket) newChild);
            return;
        }
    }
}
