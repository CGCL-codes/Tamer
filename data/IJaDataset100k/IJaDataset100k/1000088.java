package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP2TaskItemDeclaration extends PTaskItemDeclaration {

    private PTfOutputDeclaration _tfOutputDeclaration_;

    private TTSemicolon _tSemicolon_;

    public AP2TaskItemDeclaration() {
    }

    public AP2TaskItemDeclaration(@SuppressWarnings("hiding") PTfOutputDeclaration _tfOutputDeclaration_, @SuppressWarnings("hiding") TTSemicolon _tSemicolon_) {
        setTfOutputDeclaration(_tfOutputDeclaration_);
        setTSemicolon(_tSemicolon_);
    }

    @Override
    public Object clone() {
        return new AP2TaskItemDeclaration(cloneNode(this._tfOutputDeclaration_), cloneNode(this._tSemicolon_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP2TaskItemDeclaration(this);
    }

    public PTfOutputDeclaration getTfOutputDeclaration() {
        return this._tfOutputDeclaration_;
    }

    public void setTfOutputDeclaration(PTfOutputDeclaration node) {
        if (this._tfOutputDeclaration_ != null) {
            this._tfOutputDeclaration_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tfOutputDeclaration_ = node;
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
        return "" + toString(this._tfOutputDeclaration_) + toString(this._tSemicolon_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._tfOutputDeclaration_ == child) {
            this._tfOutputDeclaration_ = null;
            return;
        }
        if (this._tSemicolon_ == child) {
            this._tSemicolon_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._tfOutputDeclaration_ == oldChild) {
            setTfOutputDeclaration((PTfOutputDeclaration) newChild);
            return;
        }
        if (this._tSemicolon_ == oldChild) {
            setTSemicolon((TTSemicolon) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
