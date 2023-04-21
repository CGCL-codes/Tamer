package org.jdmp.core.script.jdmp.node;

import org.jdmp.core.script.jdmp.analysis.*;

@SuppressWarnings("nls")
public final class ALevel10Expression extends PExpression {

    private PLevel10 _level10_;

    public ALevel10Expression() {
    }

    public ALevel10Expression(@SuppressWarnings("hiding") PLevel10 _level10_) {
        setLevel10(_level10_);
    }

    public Object clone() {
        return new ALevel10Expression(cloneNode(this._level10_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseALevel10Expression(this);
    }

    public PLevel10 getLevel10() {
        return this._level10_;
    }

    public void setLevel10(PLevel10 node) {
        if (this._level10_ != null) {
            this._level10_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._level10_ = node;
    }

    public String toString() {
        return "" + toString(this._level10_);
    }

    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._level10_ == child) {
            this._level10_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._level10_ == oldChild) {
            setLevel10((PLevel10) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
