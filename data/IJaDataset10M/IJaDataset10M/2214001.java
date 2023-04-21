package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP1ModulePathPrimary extends PModulePathPrimary {

    private PPrimaryHierarchicalIdentifier _primaryHierarchicalIdentifier_;

    public AP1ModulePathPrimary() {
    }

    public AP1ModulePathPrimary(@SuppressWarnings("hiding") PPrimaryHierarchicalIdentifier _primaryHierarchicalIdentifier_) {
        setPrimaryHierarchicalIdentifier(_primaryHierarchicalIdentifier_);
    }

    @Override
    public Object clone() {
        return new AP1ModulePathPrimary(cloneNode(this._primaryHierarchicalIdentifier_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP1ModulePathPrimary(this);
    }

    public PPrimaryHierarchicalIdentifier getPrimaryHierarchicalIdentifier() {
        return this._primaryHierarchicalIdentifier_;
    }

    public void setPrimaryHierarchicalIdentifier(PPrimaryHierarchicalIdentifier node) {
        if (this._primaryHierarchicalIdentifier_ != null) {
            this._primaryHierarchicalIdentifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._primaryHierarchicalIdentifier_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._primaryHierarchicalIdentifier_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._primaryHierarchicalIdentifier_ == child) {
            this._primaryHierarchicalIdentifier_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._primaryHierarchicalIdentifier_ == oldChild) {
            setPrimaryHierarchicalIdentifier((PPrimaryHierarchicalIdentifier) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
