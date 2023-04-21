package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP0Terminals extends PTerminals {

    private PTerminal _terminal_;

    public AP0Terminals() {
    }

    public AP0Terminals(@SuppressWarnings("hiding") PTerminal _terminal_) {
        setTerminal(_terminal_);
    }

    @Override
    public Object clone() {
        return new AP0Terminals(cloneNode(this._terminal_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP0Terminals(this);
    }

    public PTerminal getTerminal() {
        return this._terminal_;
    }

    public void setTerminal(PTerminal node) {
        if (this._terminal_ != null) {
            this._terminal_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._terminal_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._terminal_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._terminal_ == child) {
            this._terminal_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._terminal_ == oldChild) {
            setTerminal((PTerminal) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
