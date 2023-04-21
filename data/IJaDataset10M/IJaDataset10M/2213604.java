package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP0PortDeclarations extends PPortDeclarations {

    private PPortDeclarationFoo _portDeclarationFoo_;

    public AP0PortDeclarations() {
    }

    public AP0PortDeclarations(@SuppressWarnings("hiding") PPortDeclarationFoo _portDeclarationFoo_) {
        setPortDeclarationFoo(_portDeclarationFoo_);
    }

    @Override
    public Object clone() {
        return new AP0PortDeclarations(cloneNode(this._portDeclarationFoo_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP0PortDeclarations(this);
    }

    public PPortDeclarationFoo getPortDeclarationFoo() {
        return this._portDeclarationFoo_;
    }

    public void setPortDeclarationFoo(PPortDeclarationFoo node) {
        if (this._portDeclarationFoo_ != null) {
            this._portDeclarationFoo_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._portDeclarationFoo_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._portDeclarationFoo_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._portDeclarationFoo_ == child) {
            this._portDeclarationFoo_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._portDeclarationFoo_ == oldChild) {
            setPortDeclarationFoo((PPortDeclarationFoo) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
