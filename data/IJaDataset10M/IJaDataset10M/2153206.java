package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class APublicModifier extends PModifier {

    private TPublic _public_;

    public APublicModifier() {
    }

    public APublicModifier(@SuppressWarnings("hiding") TPublic _public_) {
        setPublic(_public_);
    }

    @Override
    public Object clone() {
        return new APublicModifier(cloneNode(this._public_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPublicModifier(this);
    }

    public TPublic getPublic() {
        return this._public_;
    }

    public void setPublic(TPublic node) {
        if (this._public_ != null) {
            this._public_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._public_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._public_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._public_ == child) {
            this._public_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._public_ == oldChild) {
            setPublic((TPublic) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
