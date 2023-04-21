package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ASwitchStatementStatementWithoutTrailingSubstatement extends PStatementWithoutTrailingSubstatement {

    private PSwitchStatement _switchStatement_;

    public ASwitchStatementStatementWithoutTrailingSubstatement() {
    }

    public ASwitchStatementStatementWithoutTrailingSubstatement(@SuppressWarnings("hiding") PSwitchStatement _switchStatement_) {
        setSwitchStatement(_switchStatement_);
    }

    @Override
    public Object clone() {
        return new ASwitchStatementStatementWithoutTrailingSubstatement(cloneNode(this._switchStatement_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASwitchStatementStatementWithoutTrailingSubstatement(this);
    }

    public PSwitchStatement getSwitchStatement() {
        return this._switchStatement_;
    }

    public void setSwitchStatement(PSwitchStatement node) {
        if (this._switchStatement_ != null) {
            this._switchStatement_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._switchStatement_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._switchStatement_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._switchStatement_ == child) {
            this._switchStatement_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._switchStatement_ == oldChild) {
            setSwitchStatement((PSwitchStatement) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
