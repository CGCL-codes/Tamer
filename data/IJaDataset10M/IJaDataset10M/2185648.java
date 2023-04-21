package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP8ModuleOrGenerateItemDeclaration extends PModuleOrGenerateItemDeclaration {

    private PTaskDeclaration _taskDeclaration_;

    public AP8ModuleOrGenerateItemDeclaration() {
    }

    public AP8ModuleOrGenerateItemDeclaration(@SuppressWarnings("hiding") PTaskDeclaration _taskDeclaration_) {
        setTaskDeclaration(_taskDeclaration_);
    }

    @Override
    public Object clone() {
        return new AP8ModuleOrGenerateItemDeclaration(cloneNode(this._taskDeclaration_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP8ModuleOrGenerateItemDeclaration(this);
    }

    public PTaskDeclaration getTaskDeclaration() {
        return this._taskDeclaration_;
    }

    public void setTaskDeclaration(PTaskDeclaration node) {
        if (this._taskDeclaration_ != null) {
            this._taskDeclaration_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._taskDeclaration_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._taskDeclaration_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._taskDeclaration_ == child) {
            this._taskDeclaration_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._taskDeclaration_ == oldChild) {
            setTaskDeclaration((PTaskDeclaration) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
