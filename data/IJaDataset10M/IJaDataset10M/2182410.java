package org.omwg.mediation.parser.hrsyntax.node;

import java.util.*;
import org.omwg.mediation.parser.hrsyntax.analysis.*;

public final class ASourceExp extends PSourceExp {

    private TSource _source_;

    private TLpar _lpar_;

    private POntologyid _ontologyid_;

    private TRpar _rpar_;

    public ASourceExp() {
    }

    public ASourceExp(TSource _source_, TLpar _lpar_, POntologyid _ontologyid_, TRpar _rpar_) {
        setSource(_source_);
        setLpar(_lpar_);
        setOntologyid(_ontologyid_);
        setRpar(_rpar_);
    }

    public Object clone() {
        return new ASourceExp((TSource) cloneNode(_source_), (TLpar) cloneNode(_lpar_), (POntologyid) cloneNode(_ontologyid_), (TRpar) cloneNode(_rpar_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASourceExp(this);
    }

    public TSource getSource() {
        return _source_;
    }

    public void setSource(TSource node) {
        if (_source_ != null) {
            _source_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _source_ = node;
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

    public POntologyid getOntologyid() {
        return _ontologyid_;
    }

    public void setOntologyid(POntologyid node) {
        if (_ontologyid_ != null) {
            _ontologyid_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _ontologyid_ = node;
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
        return "" + toString(_source_) + toString(_lpar_) + toString(_ontologyid_) + toString(_rpar_);
    }

    void removeChild(Node child) {
        if (_source_ == child) {
            _source_ = null;
            return;
        }
        if (_lpar_ == child) {
            _lpar_ = null;
            return;
        }
        if (_ontologyid_ == child) {
            _ontologyid_ = null;
            return;
        }
        if (_rpar_ == child) {
            _rpar_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_source_ == oldChild) {
            setSource((TSource) newChild);
            return;
        }
        if (_lpar_ == oldChild) {
            setLpar((TLpar) newChild);
            return;
        }
        if (_ontologyid_ == oldChild) {
            setOntologyid((POntologyid) newChild);
            return;
        }
        if (_rpar_ == oldChild) {
            setRpar((TRpar) newChild);
            return;
        }
    }
}
