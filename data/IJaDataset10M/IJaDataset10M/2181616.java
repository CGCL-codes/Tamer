package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP1StatementNsfOrNull extends PStatementNsfOrNull {

    private TTSemicolon _tSemicolon_;

    public AP1StatementNsfOrNull() {
    }

    public AP1StatementNsfOrNull(@SuppressWarnings("hiding") TTSemicolon _tSemicolon_) {
        setTSemicolon(_tSemicolon_);
    }

    @Override
    public Object clone() {
        return new AP1StatementNsfOrNull(cloneNode(this._tSemicolon_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP1StatementNsfOrNull(this);
    }

    public TTSemicolon getTSemicolon() {
        return this._tSemicolon_;
    }

    public void setTSemicolon(TTSemicolon node) {
        if (this._tSemicolon_ != null) {
            this._tSemicolon_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tSemicolon_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._tSemicolon_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._tSemicolon_ == child) {
            this._tSemicolon_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._tSemicolon_ == oldChild) {
            setTSemicolon((TTSemicolon) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
