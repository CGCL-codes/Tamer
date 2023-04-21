package org.sablecc.sablecc.node;

import java.util.*;
import org.sablecc.sablecc.analysis.*;
import org.sablecc.sablecc.node.AIgnoredAlt;
import org.sablecc.sablecc.node.Cast;
import org.sablecc.sablecc.node.Node;
import org.sablecc.sablecc.node.PAlt;
import org.sablecc.sablecc.node.PAltName;
import org.sablecc.sablecc.node.PElem;
import org.sablecc.sablecc.node.Switch;
import org.sablecc.sablecc.node.TLPar;
import org.sablecc.sablecc.node.TRPar;
import org.sablecc.sablecc.node.TypedLinkedList;
import org.sablecc.sablecc.node.X1PElem;
import org.sablecc.sablecc.node.X2PElem;
import org.sablecc.sablecc.node.XPElem;

public final class AIgnoredAlt extends PAlt {

    private TLPar _lPar_;

    private PAltName _altName_;

    private final LinkedList _elems_ = new TypedLinkedList(new Elems_Cast());

    private TRPar _rPar_;

    public AIgnoredAlt() {
    }

    public AIgnoredAlt(TLPar _lPar_, PAltName _altName_, List _elems_, TRPar _rPar_) {
        setLPar(_lPar_);
        setAltName(_altName_);
        {
            this._elems_.clear();
            this._elems_.addAll(_elems_);
        }
        setRPar(_rPar_);
    }

    public AIgnoredAlt(TLPar _lPar_, PAltName _altName_, XPElem _elems_, TRPar _rPar_) {
        setLPar(_lPar_);
        setAltName(_altName_);
        if (_elems_ != null) {
            while (_elems_ instanceof X1PElem) {
                this._elems_.addFirst(((X1PElem) _elems_).getPElem());
                _elems_ = ((X1PElem) _elems_).getXPElem();
            }
            this._elems_.addFirst(((X2PElem) _elems_).getPElem());
        }
        setRPar(_rPar_);
    }

    public Object clone() {
        return new AIgnoredAlt((TLPar) cloneNode(_lPar_), (PAltName) cloneNode(_altName_), cloneList(_elems_), (TRPar) cloneNode(_rPar_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAIgnoredAlt(this);
    }

    public TLPar getLPar() {
        return _lPar_;
    }

    public void setLPar(TLPar node) {
        if (_lPar_ != null) {
            _lPar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _lPar_ = node;
    }

    public PAltName getAltName() {
        return _altName_;
    }

    public void setAltName(PAltName node) {
        if (_altName_ != null) {
            _altName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _altName_ = node;
    }

    public LinkedList getElems() {
        return _elems_;
    }

    public void setElems(List list) {
        _elems_.clear();
        _elems_.addAll(list);
    }

    public TRPar getRPar() {
        return _rPar_;
    }

    public void setRPar(TRPar node) {
        if (_rPar_ != null) {
            _rPar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _rPar_ = node;
    }

    public String toString() {
        return "" + toString(_lPar_) + toString(_altName_) + toString(_elems_) + toString(_rPar_);
    }

    void removeChild(Node child) {
        if (_lPar_ == child) {
            _lPar_ = null;
            return;
        }
        if (_altName_ == child) {
            _altName_ = null;
            return;
        }
        if (_elems_.remove(child)) {
            return;
        }
        if (_rPar_ == child) {
            _rPar_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_lPar_ == oldChild) {
            setLPar((TLPar) newChild);
            return;
        }
        if (_altName_ == oldChild) {
            setAltName((PAltName) newChild);
            return;
        }
        for (ListIterator i = _elems_.listIterator(); i.hasNext(); ) {
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
        if (_rPar_ == oldChild) {
            setRPar((TRPar) newChild);
            return;
        }
    }

    private class Elems_Cast implements Cast {

        public Object cast(Object o) {
            PElem node = (PElem) o;
            if ((node.parent() != null) && (node.parent() != AIgnoredAlt.this)) {
                node.parent().removeChild(node);
            }
            if ((node.parent() == null) || (node.parent() != AIgnoredAlt.this)) {
                node.parent(AIgnoredAlt.this);
            }
            return node;
        }
    }
}
