package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AEmptyBasicForStatement extends PBasicForStatement {

    private TFor _for_;

    private TLPar _lPar_;

    private PForInit _forInit_;

    private TSemi _semi1_;

    private TSemi _semi2_;

    private PForUpdate _forUpdate_;

    private TRPar _rPar_;

    private PStatement _statement_;

    public AEmptyBasicForStatement() {
    }

    public AEmptyBasicForStatement(@SuppressWarnings("hiding") TFor _for_, @SuppressWarnings("hiding") TLPar _lPar_, @SuppressWarnings("hiding") PForInit _forInit_, @SuppressWarnings("hiding") TSemi _semi1_, @SuppressWarnings("hiding") TSemi _semi2_, @SuppressWarnings("hiding") PForUpdate _forUpdate_, @SuppressWarnings("hiding") TRPar _rPar_, @SuppressWarnings("hiding") PStatement _statement_) {
        setFor(_for_);
        setLPar(_lPar_);
        setForInit(_forInit_);
        setSemi1(_semi1_);
        setSemi2(_semi2_);
        setForUpdate(_forUpdate_);
        setRPar(_rPar_);
        setStatement(_statement_);
    }

    @Override
    public Object clone() {
        return new AEmptyBasicForStatement(cloneNode(this._for_), cloneNode(this._lPar_), cloneNode(this._forInit_), cloneNode(this._semi1_), cloneNode(this._semi2_), cloneNode(this._forUpdate_), cloneNode(this._rPar_), cloneNode(this._statement_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAEmptyBasicForStatement(this);
    }

    public TFor getFor() {
        return this._for_;
    }

    public void setFor(TFor node) {
        if (this._for_ != null) {
            this._for_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._for_ = node;
    }

    public TLPar getLPar() {
        return this._lPar_;
    }

    public void setLPar(TLPar node) {
        if (this._lPar_ != null) {
            this._lPar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._lPar_ = node;
    }

    public PForInit getForInit() {
        return this._forInit_;
    }

    public void setForInit(PForInit node) {
        if (this._forInit_ != null) {
            this._forInit_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._forInit_ = node;
    }

    public TSemi getSemi1() {
        return this._semi1_;
    }

    public void setSemi1(TSemi node) {
        if (this._semi1_ != null) {
            this._semi1_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._semi1_ = node;
    }

    public TSemi getSemi2() {
        return this._semi2_;
    }

    public void setSemi2(TSemi node) {
        if (this._semi2_ != null) {
            this._semi2_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._semi2_ = node;
    }

    public PForUpdate getForUpdate() {
        return this._forUpdate_;
    }

    public void setForUpdate(PForUpdate node) {
        if (this._forUpdate_ != null) {
            this._forUpdate_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._forUpdate_ = node;
    }

    public TRPar getRPar() {
        return this._rPar_;
    }

    public void setRPar(TRPar node) {
        if (this._rPar_ != null) {
            this._rPar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._rPar_ = node;
    }

    public PStatement getStatement() {
        return this._statement_;
    }

    public void setStatement(PStatement node) {
        if (this._statement_ != null) {
            this._statement_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._statement_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._for_) + toString(this._lPar_) + toString(this._forInit_) + toString(this._semi1_) + toString(this._semi2_) + toString(this._forUpdate_) + toString(this._rPar_) + toString(this._statement_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._for_ == child) {
            this._for_ = null;
            return;
        }
        if (this._lPar_ == child) {
            this._lPar_ = null;
            return;
        }
        if (this._forInit_ == child) {
            this._forInit_ = null;
            return;
        }
        if (this._semi1_ == child) {
            this._semi1_ = null;
            return;
        }
        if (this._semi2_ == child) {
            this._semi2_ = null;
            return;
        }
        if (this._forUpdate_ == child) {
            this._forUpdate_ = null;
            return;
        }
        if (this._rPar_ == child) {
            this._rPar_ = null;
            return;
        }
        if (this._statement_ == child) {
            this._statement_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._for_ == oldChild) {
            setFor((TFor) newChild);
            return;
        }
        if (this._lPar_ == oldChild) {
            setLPar((TLPar) newChild);
            return;
        }
        if (this._forInit_ == oldChild) {
            setForInit((PForInit) newChild);
            return;
        }
        if (this._semi1_ == oldChild) {
            setSemi1((TSemi) newChild);
            return;
        }
        if (this._semi2_ == oldChild) {
            setSemi2((TSemi) newChild);
            return;
        }
        if (this._forUpdate_ == oldChild) {
            setForUpdate((PForUpdate) newChild);
            return;
        }
        if (this._rPar_ == oldChild) {
            setRPar((TRPar) newChild);
            return;
        }
        if (this._statement_ == oldChild) {
            setStatement((PStatement) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
