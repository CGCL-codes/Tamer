package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AClassClassMemberDeclaration extends PClassMemberDeclaration {

    private PClassDeclaration _classDeclaration_;

    public AClassClassMemberDeclaration() {
    }

    public AClassClassMemberDeclaration(@SuppressWarnings("hiding") PClassDeclaration _classDeclaration_) {
        setClassDeclaration(_classDeclaration_);
    }

    @Override
    public Object clone() {
        return new AClassClassMemberDeclaration(cloneNode(this._classDeclaration_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAClassClassMemberDeclaration(this);
    }

    public PClassDeclaration getClassDeclaration() {
        return this._classDeclaration_;
    }

    public void setClassDeclaration(PClassDeclaration node) {
        if (this._classDeclaration_ != null) {
            this._classDeclaration_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._classDeclaration_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._classDeclaration_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._classDeclaration_ == child) {
            this._classDeclaration_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._classDeclaration_ == oldChild) {
            setClassDeclaration((PClassDeclaration) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
