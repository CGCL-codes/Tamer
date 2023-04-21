package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AWhileStatementNoShortIfStatementNoShortIf extends PStatementNoShortIf {

    private PWhileStatementNoShortIf _whileStatementNoShortIf_;

    public AWhileStatementNoShortIfStatementNoShortIf() {
    }

    public AWhileStatementNoShortIfStatementNoShortIf(@SuppressWarnings("hiding") PWhileStatementNoShortIf _whileStatementNoShortIf_) {
        setWhileStatementNoShortIf(_whileStatementNoShortIf_);
    }

    @Override
    public Object clone() {
        return new AWhileStatementNoShortIfStatementNoShortIf(cloneNode(this._whileStatementNoShortIf_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAWhileStatementNoShortIfStatementNoShortIf(this);
    }

    public PWhileStatementNoShortIf getWhileStatementNoShortIf() {
        return this._whileStatementNoShortIf_;
    }

    public void setWhileStatementNoShortIf(PWhileStatementNoShortIf node) {
        if (this._whileStatementNoShortIf_ != null) {
            this._whileStatementNoShortIf_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._whileStatementNoShortIf_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._whileStatementNoShortIf_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._whileStatementNoShortIf_ == child) {
            this._whileStatementNoShortIf_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._whileStatementNoShortIf_ == oldChild) {
            setWhileStatementNoShortIf((PWhileStatementNoShortIf) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
