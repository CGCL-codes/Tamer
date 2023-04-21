package jpdl.parser.node;

import jpdl.parser.analysis.*;

@SuppressWarnings("nls")
public final class ADerivativeStm extends PStm {

    private TIdent _der_;

    private TStringLiteral _arg_;

    public ADerivativeStm() {
    }

    public ADerivativeStm(@SuppressWarnings("hiding") TIdent _der_, @SuppressWarnings("hiding") TStringLiteral _arg_) {
        setDer(_der_);
        setArg(_arg_);
    }

    @Override
    public Object clone() {
        return new ADerivativeStm(cloneNode(this._der_), cloneNode(this._arg_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseADerivativeStm(this);
    }

    public TIdent getDer() {
        return this._der_;
    }

    public void setDer(TIdent node) {
        if (this._der_ != null) {
            this._der_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._der_ = node;
    }

    public TStringLiteral getArg() {
        return this._arg_;
    }

    public void setArg(TStringLiteral node) {
        if (this._arg_ != null) {
            this._arg_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._arg_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._der_) + toString(this._arg_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._der_ == child) {
            this._der_ = null;
            return;
        }
        if (this._arg_ == child) {
            this._arg_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._der_ == oldChild) {
            setDer((TIdent) newChild);
            return;
        }
        if (this._arg_ == oldChild) {
            setArg((TStringLiteral) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
