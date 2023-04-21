package org.sableccsupport.sccparser.node;

import org.sableccsupport.sccparser.analysis.*;

@SuppressWarnings("nls")
public final class AHelperDef extends PHelperDef {

    private TId _id_;

    private TEqual _equal_;

    private PRegExp _regExp_;

    private TSemicolon _semicolon_;

    public AHelperDef() {
    }

    public AHelperDef(@SuppressWarnings("hiding") TId _id_, @SuppressWarnings("hiding") TEqual _equal_, @SuppressWarnings("hiding") PRegExp _regExp_, @SuppressWarnings("hiding") TSemicolon _semicolon_) {
        setId(_id_);
        setEqual(_equal_);
        setRegExp(_regExp_);
        setSemicolon(_semicolon_);
    }

    @Override
    public Object clone() {
        return new AHelperDef(cloneNode(this._id_), cloneNode(this._equal_), cloneNode(this._regExp_), cloneNode(this._semicolon_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAHelperDef(this);
    }

    public TId getId() {
        return this._id_;
    }

    public void setId(TId node) {
        if (this._id_ != null) {
            this._id_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._id_ = node;
    }

    public TEqual getEqual() {
        return this._equal_;
    }

    public void setEqual(TEqual node) {
        if (this._equal_ != null) {
            this._equal_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._equal_ = node;
    }

    public PRegExp getRegExp() {
        return this._regExp_;
    }

    public void setRegExp(PRegExp node) {
        if (this._regExp_ != null) {
            this._regExp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._regExp_ = node;
    }

    public TSemicolon getSemicolon() {
        return this._semicolon_;
    }

    public void setSemicolon(TSemicolon node) {
        if (this._semicolon_ != null) {
            this._semicolon_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._semicolon_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._id_) + toString(this._equal_) + toString(this._regExp_) + toString(this._semicolon_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._id_ == child) {
            this._id_ = null;
            return;
        }
        if (this._equal_ == child) {
            this._equal_ = null;
            return;
        }
        if (this._regExp_ == child) {
            this._regExp_ = null;
            return;
        }
        if (this._semicolon_ == child) {
            this._semicolon_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._id_ == oldChild) {
            setId((TId) newChild);
            return;
        }
        if (this._equal_ == oldChild) {
            setEqual((TEqual) newChild);
            return;
        }
        if (this._regExp_ == oldChild) {
            setRegExp((PRegExp) newChild);
            return;
        }
        if (this._semicolon_ == oldChild) {
            setSemicolon((TSemicolon) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
