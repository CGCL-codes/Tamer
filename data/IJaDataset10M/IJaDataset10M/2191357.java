package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AForStatementStatement extends PStatement {

    private PForStatement _forStatement_;

    public AForStatementStatement() {
    }

    public AForStatementStatement(@SuppressWarnings("hiding") PForStatement _forStatement_) {
        setForStatement(_forStatement_);
    }

    @Override
    public Object clone() {
        return new AForStatementStatement(cloneNode(this._forStatement_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAForStatementStatement(this);
    }

    public PForStatement getForStatement() {
        return this._forStatement_;
    }

    public void setForStatement(PForStatement node) {
        if (this._forStatement_ != null) {
            this._forStatement_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._forStatement_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._forStatement_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._forStatement_ == child) {
            this._forStatement_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._forStatement_ == oldChild) {
            setForStatement((PForStatement) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
