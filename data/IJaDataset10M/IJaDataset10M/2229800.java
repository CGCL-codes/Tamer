package org.sablecc.objectmacro.syntax3.node;

import org.sablecc.objectmacro.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class AEolMacroBodyPart extends PMacroBodyPart {

    private TEol _eol_;

    public AEolMacroBodyPart() {
    }

    public AEolMacroBodyPart(@SuppressWarnings("hiding") TEol _eol_) {
        setEol(_eol_);
    }

    @Override
    public Object clone() {
        return new AEolMacroBodyPart(cloneNode(this._eol_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAEolMacroBodyPart(this);
    }

    public TEol getEol() {
        return this._eol_;
    }

    public void setEol(TEol node) {
        if (this._eol_ != null) {
            this._eol_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._eol_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._eol_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._eol_ == child) {
            this._eol_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._eol_ == oldChild) {
            setEol((TEol) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
