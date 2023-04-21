package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class ANamedNewClassExp extends PNewClassExp {

    private PName _name_;

    private TDot _dot_;

    private TNew _new_;

    private PTypeArguments _typeArguments_;

    private TIdentifier _identifier_;

    private PArguments _arguments_;

    private PClassBody _classBody_;

    public ANamedNewClassExp() {
    }

    public ANamedNewClassExp(@SuppressWarnings("hiding") PName _name_, @SuppressWarnings("hiding") TDot _dot_, @SuppressWarnings("hiding") TNew _new_, @SuppressWarnings("hiding") PTypeArguments _typeArguments_, @SuppressWarnings("hiding") TIdentifier _identifier_, @SuppressWarnings("hiding") PArguments _arguments_, @SuppressWarnings("hiding") PClassBody _classBody_) {
        setName(_name_);
        setDot(_dot_);
        setNew(_new_);
        setTypeArguments(_typeArguments_);
        setIdentifier(_identifier_);
        setArguments(_arguments_);
        setClassBody(_classBody_);
    }

    @Override
    public Object clone() {
        return new ANamedNewClassExp(cloneNode(this._name_), cloneNode(this._dot_), cloneNode(this._new_), cloneNode(this._typeArguments_), cloneNode(this._identifier_), cloneNode(this._arguments_), cloneNode(this._classBody_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseANamedNewClassExp(this);
    }

    public PName getName() {
        return this._name_;
    }

    public void setName(PName node) {
        if (this._name_ != null) {
            this._name_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._name_ = node;
    }

    public TDot getDot() {
        return this._dot_;
    }

    public void setDot(TDot node) {
        if (this._dot_ != null) {
            this._dot_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._dot_ = node;
    }

    public TNew getNew() {
        return this._new_;
    }

    public void setNew(TNew node) {
        if (this._new_ != null) {
            this._new_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._new_ = node;
    }

    public PTypeArguments getTypeArguments() {
        return this._typeArguments_;
    }

    public void setTypeArguments(PTypeArguments node) {
        if (this._typeArguments_ != null) {
            this._typeArguments_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._typeArguments_ = node;
    }

    public TIdentifier getIdentifier() {
        return this._identifier_;
    }

    public void setIdentifier(TIdentifier node) {
        if (this._identifier_ != null) {
            this._identifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier_ = node;
    }

    public PArguments getArguments() {
        return this._arguments_;
    }

    public void setArguments(PArguments node) {
        if (this._arguments_ != null) {
            this._arguments_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._arguments_ = node;
    }

    public PClassBody getClassBody() {
        return this._classBody_;
    }

    public void setClassBody(PClassBody node) {
        if (this._classBody_ != null) {
            this._classBody_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._classBody_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._name_) + toString(this._dot_) + toString(this._new_) + toString(this._typeArguments_) + toString(this._identifier_) + toString(this._arguments_) + toString(this._classBody_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._name_ == child) {
            this._name_ = null;
            return;
        }
        if (this._dot_ == child) {
            this._dot_ = null;
            return;
        }
        if (this._new_ == child) {
            this._new_ = null;
            return;
        }
        if (this._typeArguments_ == child) {
            this._typeArguments_ = null;
            return;
        }
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        if (this._arguments_ == child) {
            this._arguments_ = null;
            return;
        }
        if (this._classBody_ == child) {
            this._classBody_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._name_ == oldChild) {
            setName((PName) newChild);
            return;
        }
        if (this._dot_ == oldChild) {
            setDot((TDot) newChild);
            return;
        }
        if (this._new_ == oldChild) {
            setNew((TNew) newChild);
            return;
        }
        if (this._typeArguments_ == oldChild) {
            setTypeArguments((PTypeArguments) newChild);
            return;
        }
        if (this._identifier_ == oldChild) {
            setIdentifier((TIdentifier) newChild);
            return;
        }
        if (this._arguments_ == oldChild) {
            setArguments((PArguments) newChild);
            return;
        }
        if (this._classBody_ == oldChild) {
            setClassBody((PClassBody) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
