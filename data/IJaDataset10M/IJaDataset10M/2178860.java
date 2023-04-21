package smile.node;

import smile.analysis.*;

@SuppressWarnings("nls")
public final class ADecStmt extends PStmt {

    private TDec _dec_;

    public ADecStmt() {
    }

    public ADecStmt(@SuppressWarnings("hiding") TDec _dec_) {
        setDec(_dec_);
    }

    @Override
    public Object clone() {
        return new ADecStmt(cloneNode(this._dec_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseADecStmt(this);
    }

    public TDec getDec() {
        return this._dec_;
    }

    public void setDec(TDec node) {
        if (this._dec_ != null) {
            this._dec_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._dec_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._dec_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._dec_ == child) {
            this._dec_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._dec_ == oldChild) {
            setDec((TDec) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
