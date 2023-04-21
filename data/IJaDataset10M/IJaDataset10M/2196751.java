package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP2PortDeclarationSingle extends PPortDeclarationSingle {

    private POutputDeclarationS _outputDeclarationS_;

    public AP2PortDeclarationSingle() {
    }

    public AP2PortDeclarationSingle(@SuppressWarnings("hiding") POutputDeclarationS _outputDeclarationS_) {
        setOutputDeclarationS(_outputDeclarationS_);
    }

    @Override
    public Object clone() {
        return new AP2PortDeclarationSingle(cloneNode(this._outputDeclarationS_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP2PortDeclarationSingle(this);
    }

    public POutputDeclarationS getOutputDeclarationS() {
        return this._outputDeclarationS_;
    }

    public void setOutputDeclarationS(POutputDeclarationS node) {
        if (this._outputDeclarationS_ != null) {
            this._outputDeclarationS_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._outputDeclarationS_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._outputDeclarationS_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._outputDeclarationS_ == child) {
            this._outputDeclarationS_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._outputDeclarationS_ == oldChild) {
            setOutputDeclarationS((POutputDeclarationS) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
