package galoot.node;

import galoot.analysis.*;

@SuppressWarnings("nls")
public final class AVarPlugin extends PPlugin {

    private PVarExpression _var_;

    public AVarPlugin() {
    }

    public AVarPlugin(@SuppressWarnings("hiding") PVarExpression _var_) {
        setVar(_var_);
    }

    @Override
    public Object clone() {
        return new AVarPlugin(cloneNode(this._var_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAVarPlugin(this);
    }

    public PVarExpression getVar() {
        return this._var_;
    }

    public void setVar(PVarExpression node) {
        if (this._var_ != null) {
            this._var_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._var_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._var_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._var_ == child) {
            this._var_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._var_ == oldChild) {
            setVar((PVarExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
