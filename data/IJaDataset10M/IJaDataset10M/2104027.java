package tudresden.ocl.codegen.decl.treegen.node;

import tudresden.ocl.codegen.decl.treegen.analysis.*;

public final class Start extends Node {

    private PQueryExpression _pQueryExpression_;

    private EOF _eof_;

    public Start() {
    }

    public Start(PQueryExpression _pQueryExpression_, EOF _eof_) {
        setPQueryExpression(_pQueryExpression_);
        setEOF(_eof_);
    }

    public Object clone() {
        return new Start((PQueryExpression) cloneNode(_pQueryExpression_), (EOF) cloneNode(_eof_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseStart(this);
    }

    public PQueryExpression getPQueryExpression() {
        return _pQueryExpression_;
    }

    public void setPQueryExpression(PQueryExpression node) {
        if (_pQueryExpression_ != null) {
            _pQueryExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _pQueryExpression_ = node;
    }

    public EOF getEOF() {
        return _eof_;
    }

    public void setEOF(EOF node) {
        if (_eof_ != null) {
            _eof_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _eof_ = node;
    }

    void removeChild(Node child) {
        if (_pQueryExpression_ == child) {
            _pQueryExpression_ = null;
            return;
        }
        if (_eof_ == child) {
            _eof_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_pQueryExpression_ == oldChild) {
            setPQueryExpression((PQueryExpression) newChild);
            return;
        }
        if (_eof_ == oldChild) {
            setEOF((EOF) newChild);
            return;
        }
    }

    public String toString() {
        return "" + toString(_pQueryExpression_) + toString(_eof_);
    }
}
