package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP9ModuleOrGenerateItemDeclaration extends PModuleOrGenerateItemDeclaration {

    private PFunctionDeclaration _functionDeclaration_;

    public AP9ModuleOrGenerateItemDeclaration() {
    }

    public AP9ModuleOrGenerateItemDeclaration(@SuppressWarnings("hiding") PFunctionDeclaration _functionDeclaration_) {
        setFunctionDeclaration(_functionDeclaration_);
    }

    @Override
    public Object clone() {
        return new AP9ModuleOrGenerateItemDeclaration(cloneNode(this._functionDeclaration_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP9ModuleOrGenerateItemDeclaration(this);
    }

    public PFunctionDeclaration getFunctionDeclaration() {
        return this._functionDeclaration_;
    }

    public void setFunctionDeclaration(PFunctionDeclaration node) {
        if (this._functionDeclaration_ != null) {
            this._functionDeclaration_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._functionDeclaration_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._functionDeclaration_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._functionDeclaration_ == child) {
            this._functionDeclaration_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._functionDeclaration_ == oldChild) {
            setFunctionDeclaration((PFunctionDeclaration) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
