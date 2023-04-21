package org.sablecc.sablecc.intermediate.syntax3.node;

import org.sablecc.sablecc.intermediate.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class AParseLexerAction extends PLexerAction {

    private TString _token_;

    private TString _start_;

    public AParseLexerAction() {
    }

    public AParseLexerAction(@SuppressWarnings("hiding") TString _token_, @SuppressWarnings("hiding") TString _start_) {
        setToken(_token_);
        setStart(_start_);
    }

    @Override
    public Object clone() {
        return new AParseLexerAction(cloneNode(this._token_), cloneNode(this._start_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAParseLexerAction(this);
    }

    public TString getToken() {
        return this._token_;
    }

    public void setToken(TString node) {
        if (this._token_ != null) {
            this._token_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._token_ = node;
    }

    public TString getStart() {
        return this._start_;
    }

    public void setStart(TString node) {
        if (this._start_ != null) {
            this._start_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._start_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._token_) + toString(this._start_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._token_ == child) {
            this._token_ = null;
            return;
        }
        if (this._start_ == child) {
            this._start_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._token_ == oldChild) {
            setToken((TString) newChild);
            return;
        }
        if (this._start_ == oldChild) {
            setStart((TString) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
