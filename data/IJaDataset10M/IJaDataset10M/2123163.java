package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ASingleElementAnnotationAnnotation extends PAnnotation {

    private PSingleElementAnnotation _singleElementAnnotation_;

    public ASingleElementAnnotationAnnotation() {
    }

    public ASingleElementAnnotationAnnotation(@SuppressWarnings("hiding") PSingleElementAnnotation _singleElementAnnotation_) {
        setSingleElementAnnotation(_singleElementAnnotation_);
    }

    @Override
    public Object clone() {
        return new ASingleElementAnnotationAnnotation(cloneNode(this._singleElementAnnotation_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASingleElementAnnotationAnnotation(this);
    }

    public PSingleElementAnnotation getSingleElementAnnotation() {
        return this._singleElementAnnotation_;
    }

    public void setSingleElementAnnotation(PSingleElementAnnotation node) {
        if (this._singleElementAnnotation_ != null) {
            this._singleElementAnnotation_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._singleElementAnnotation_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._singleElementAnnotation_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._singleElementAnnotation_ == child) {
            this._singleElementAnnotation_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._singleElementAnnotation_ == oldChild) {
            setSingleElementAnnotation((PSingleElementAnnotation) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
