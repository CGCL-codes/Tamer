package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class APortExpressionRep extends PPortExpressionRep {

    private TTComma _tComma_;

    private PPortReference _portReference_;

    public APortExpressionRep() {
    }

    public APortExpressionRep(@SuppressWarnings("hiding") TTComma _tComma_, @SuppressWarnings("hiding") PPortReference _portReference_) {
        setTComma(_tComma_);
        setPortReference(_portReference_);
    }

    @Override
    public Object clone() {
        return new APortExpressionRep(cloneNode(this._tComma_), cloneNode(this._portReference_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPortExpressionRep(this);
    }

    public TTComma getTComma() {
        return this._tComma_;
    }

    public void setTComma(TTComma node) {
        if (this._tComma_ != null) {
            this._tComma_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tComma_ = node;
    }

    public PPortReference getPortReference() {
        return this._portReference_;
    }

    public void setPortReference(PPortReference node) {
        if (this._portReference_ != null) {
            this._portReference_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._portReference_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._tComma_) + toString(this._portReference_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._tComma_ == child) {
            this._tComma_ = null;
            return;
        }
        if (this._portReference_ == child) {
            this._portReference_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._tComma_ == oldChild) {
            setTComma((TTComma) newChild);
            return;
        }
        if (this._portReference_ == oldChild) {
            setPortReference((PPortReference) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
