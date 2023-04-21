package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AConstantAnnotationTypeElementDeclaration extends PAnnotationTypeElementDeclaration {

    private PConstantDeclaration _constantDeclaration_;

    public AConstantAnnotationTypeElementDeclaration() {
    }

    public AConstantAnnotationTypeElementDeclaration(@SuppressWarnings("hiding") PConstantDeclaration _constantDeclaration_) {
        setConstantDeclaration(_constantDeclaration_);
    }

    @Override
    public Object clone() {
        return new AConstantAnnotationTypeElementDeclaration(cloneNode(this._constantDeclaration_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAConstantAnnotationTypeElementDeclaration(this);
    }

    public PConstantDeclaration getConstantDeclaration() {
        return this._constantDeclaration_;
    }

    public void setConstantDeclaration(PConstantDeclaration node) {
        if (this._constantDeclaration_ != null) {
            this._constantDeclaration_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._constantDeclaration_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._constantDeclaration_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._constantDeclaration_ == child) {
            this._constantDeclaration_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._constantDeclaration_ == oldChild) {
            setConstantDeclaration((PConstantDeclaration) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
