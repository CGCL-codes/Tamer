package org.omwg.mediation.parser.hrsyntax.node;

import java.util.*;
import org.omwg.mediation.parser.hrsyntax.analysis.*;

public final class AOrAttributeexpr extends PAttributeexpr {

    private TOr _or_;

    private TLpar _lpar_;

    private PAttributeexpr _first_;

    private PAttributeexpr _second_;

    private final LinkedList _attributeexpr_ = new TypedLinkedList(new Attributeexpr_Cast());

    private TRpar _rpar_;

    public AOrAttributeexpr() {
    }

    public AOrAttributeexpr(TOr _or_, TLpar _lpar_, PAttributeexpr _first_, PAttributeexpr _second_, List _attributeexpr_, TRpar _rpar_) {
        setOr(_or_);
        setLpar(_lpar_);
        setFirst(_first_);
        setSecond(_second_);
        {
            this._attributeexpr_.clear();
            this._attributeexpr_.addAll(_attributeexpr_);
        }
        setRpar(_rpar_);
    }

    public AOrAttributeexpr(TOr _or_, TLpar _lpar_, PAttributeexpr _first_, PAttributeexpr _second_, XPAttributeexpr _attributeexpr_, TRpar _rpar_) {
        setOr(_or_);
        setLpar(_lpar_);
        setFirst(_first_);
        setSecond(_second_);
        if (_attributeexpr_ != null) {
            while (_attributeexpr_ instanceof X1PAttributeexpr) {
                this._attributeexpr_.addFirst(((X1PAttributeexpr) _attributeexpr_).getPAttributeexpr());
                _attributeexpr_ = ((X1PAttributeexpr) _attributeexpr_).getXPAttributeexpr();
            }
            this._attributeexpr_.addFirst(((X2PAttributeexpr) _attributeexpr_).getPAttributeexpr());
        }
        setRpar(_rpar_);
    }

    public Object clone() {
        return new AOrAttributeexpr((TOr) cloneNode(_or_), (TLpar) cloneNode(_lpar_), (PAttributeexpr) cloneNode(_first_), (PAttributeexpr) cloneNode(_second_), cloneList(_attributeexpr_), (TRpar) cloneNode(_rpar_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAOrAttributeexpr(this);
    }

    public TOr getOr() {
        return _or_;
    }

    public void setOr(TOr node) {
        if (_or_ != null) {
            _or_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _or_ = node;
    }

    public TLpar getLpar() {
        return _lpar_;
    }

    public void setLpar(TLpar node) {
        if (_lpar_ != null) {
            _lpar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _lpar_ = node;
    }

    public PAttributeexpr getFirst() {
        return _first_;
    }

    public void setFirst(PAttributeexpr node) {
        if (_first_ != null) {
            _first_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _first_ = node;
    }

    public PAttributeexpr getSecond() {
        return _second_;
    }

    public void setSecond(PAttributeexpr node) {
        if (_second_ != null) {
            _second_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _second_ = node;
    }

    public LinkedList getAttributeexpr() {
        return _attributeexpr_;
    }

    public void setAttributeexpr(List list) {
        _attributeexpr_.clear();
        _attributeexpr_.addAll(list);
    }

    public TRpar getRpar() {
        return _rpar_;
    }

    public void setRpar(TRpar node) {
        if (_rpar_ != null) {
            _rpar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _rpar_ = node;
    }

    public String toString() {
        return "" + toString(_or_) + toString(_lpar_) + toString(_first_) + toString(_second_) + toString(_attributeexpr_) + toString(_rpar_);
    }

    void removeChild(Node child) {
        if (_or_ == child) {
            _or_ = null;
            return;
        }
        if (_lpar_ == child) {
            _lpar_ = null;
            return;
        }
        if (_first_ == child) {
            _first_ = null;
            return;
        }
        if (_second_ == child) {
            _second_ = null;
            return;
        }
        if (_attributeexpr_.remove(child)) {
            return;
        }
        if (_rpar_ == child) {
            _rpar_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_or_ == oldChild) {
            setOr((TOr) newChild);
            return;
        }
        if (_lpar_ == oldChild) {
            setLpar((TLpar) newChild);
            return;
        }
        if (_first_ == oldChild) {
            setFirst((PAttributeexpr) newChild);
            return;
        }
        if (_second_ == oldChild) {
            setSecond((PAttributeexpr) newChild);
            return;
        }
        for (ListIterator i = _attributeexpr_.listIterator(); i.hasNext(); ) {
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
        if (_rpar_ == oldChild) {
            setRpar((TRpar) newChild);
            return;
        }
    }

    private class Attributeexpr_Cast implements Cast {

        public Object cast(Object o) {
            PAttributeexpr node = (PAttributeexpr) o;
            if ((node.parent() != null) && (node.parent() != AOrAttributeexpr.this)) {
                node.parent().removeChild(node);
            }
            if ((node.parent() == null) || (node.parent() != AOrAttributeexpr.this)) {
                node.parent(AOrAttributeexpr.this);
            }
            return node;
        }
    }
}
