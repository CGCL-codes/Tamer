package net.sourceforge.jdefprog.mcl.generated.node;

import net.sourceforge.jdefprog.mcl.generated.analysis.*;

@SuppressWarnings("nls")
public final class AUpLevelOne extends PLevelOne {

    private PPrimary _lower_;

    public AUpLevelOne() {
    }

    public AUpLevelOne(@SuppressWarnings("hiding") PPrimary _lower_) {
        setLower(_lower_);
    }

    @Override
    public Object clone() {
        return new AUpLevelOne(cloneNode(this._lower_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAUpLevelOne(this);
    }

    public PPrimary getLower() {
        return this._lower_;
    }

    public void setLower(PPrimary node) {
        if (this._lower_ != null) {
            this._lower_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._lower_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._lower_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._lower_ == child) {
            this._lower_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._lower_ == oldChild) {
            setLower((PPrimary) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
