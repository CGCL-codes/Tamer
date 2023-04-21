package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AIfThenElseStatementNoShortIf extends PIfThenElseStatementNoShortIf {

    private TIf _if_;

    private PCondition _condition_;

    private PStatementNoShortIf _ifs_;

    private TElse _else_;

    private PStatementNoShortIf _elses_;

    public AIfThenElseStatementNoShortIf() {
    }

    public AIfThenElseStatementNoShortIf(@SuppressWarnings("hiding") TIf _if_, @SuppressWarnings("hiding") PCondition _condition_, @SuppressWarnings("hiding") PStatementNoShortIf _ifs_, @SuppressWarnings("hiding") TElse _else_, @SuppressWarnings("hiding") PStatementNoShortIf _elses_) {
        setIf(_if_);
        setCondition(_condition_);
        setIfs(_ifs_);
        setElse(_else_);
        setElses(_elses_);
    }

    @Override
    public Object clone() {
        return new AIfThenElseStatementNoShortIf(cloneNode(this._if_), cloneNode(this._condition_), cloneNode(this._ifs_), cloneNode(this._else_), cloneNode(this._elses_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAIfThenElseStatementNoShortIf(this);
    }

    public TIf getIf() {
        return this._if_;
    }

    public void setIf(TIf node) {
        if (this._if_ != null) {
            this._if_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._if_ = node;
    }

    public PCondition getCondition() {
        return this._condition_;
    }

    public void setCondition(PCondition node) {
        if (this._condition_ != null) {
            this._condition_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._condition_ = node;
    }

    public PStatementNoShortIf getIfs() {
        return this._ifs_;
    }

    public void setIfs(PStatementNoShortIf node) {
        if (this._ifs_ != null) {
            this._ifs_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._ifs_ = node;
    }

    public TElse getElse() {
        return this._else_;
    }

    public void setElse(TElse node) {
        if (this._else_ != null) {
            this._else_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._else_ = node;
    }

    public PStatementNoShortIf getElses() {
        return this._elses_;
    }

    public void setElses(PStatementNoShortIf node) {
        if (this._elses_ != null) {
            this._elses_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._elses_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._if_) + toString(this._condition_) + toString(this._ifs_) + toString(this._else_) + toString(this._elses_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._if_ == child) {
            this._if_ = null;
            return;
        }
        if (this._condition_ == child) {
            this._condition_ = null;
            return;
        }
        if (this._ifs_ == child) {
            this._ifs_ = null;
            return;
        }
        if (this._else_ == child) {
            this._else_ = null;
            return;
        }
        if (this._elses_ == child) {
            this._elses_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._if_ == oldChild) {
            setIf((TIf) newChild);
            return;
        }
        if (this._condition_ == oldChild) {
            setCondition((PCondition) newChild);
            return;
        }
        if (this._ifs_ == oldChild) {
            setIfs((PStatementNoShortIf) newChild);
            return;
        }
        if (this._else_ == oldChild) {
            setElse((TElse) newChild);
            return;
        }
        if (this._elses_ == oldChild) {
            setElses((PStatementNoShortIf) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
