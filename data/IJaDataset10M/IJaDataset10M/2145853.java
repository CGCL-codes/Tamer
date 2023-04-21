package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ABarBarIdIdConditionalOrExpression extends PConditionalOrExpression {

    private TIdentifier _identifier1_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers1_ = new LinkedList<PAdditionalIdentifier>();

    private TBarBar _barBar_;

    private TIdentifier _identifier2_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers2_ = new LinkedList<PAdditionalIdentifier>();

    public ABarBarIdIdConditionalOrExpression() {
    }

    public ABarBarIdIdConditionalOrExpression(@SuppressWarnings("hiding") TIdentifier _identifier1_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers1_, @SuppressWarnings("hiding") TBarBar _barBar_, @SuppressWarnings("hiding") TIdentifier _identifier2_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers2_) {
        setIdentifier1(_identifier1_);
        setAdditionalIdentifiers1(_additionalIdentifiers1_);
        setBarBar(_barBar_);
        setIdentifier2(_identifier2_);
        setAdditionalIdentifiers2(_additionalIdentifiers2_);
    }

    @Override
    public Object clone() {
        return new ABarBarIdIdConditionalOrExpression(cloneNode(this._identifier1_), cloneList(this._additionalIdentifiers1_), cloneNode(this._barBar_), cloneNode(this._identifier2_), cloneList(this._additionalIdentifiers2_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseABarBarIdIdConditionalOrExpression(this);
    }

    public TIdentifier getIdentifier1() {
        return this._identifier1_;
    }

    public void setIdentifier1(TIdentifier node) {
        if (this._identifier1_ != null) {
            this._identifier1_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier1_ = node;
    }

    public LinkedList<PAdditionalIdentifier> getAdditionalIdentifiers1() {
        return this._additionalIdentifiers1_;
    }

    public void setAdditionalIdentifiers1(List<PAdditionalIdentifier> list) {
        this._additionalIdentifiers1_.clear();
        this._additionalIdentifiers1_.addAll(list);
        for (PAdditionalIdentifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public TBarBar getBarBar() {
        return this._barBar_;
    }

    public void setBarBar(TBarBar node) {
        if (this._barBar_ != null) {
            this._barBar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._barBar_ = node;
    }

    public TIdentifier getIdentifier2() {
        return this._identifier2_;
    }

    public void setIdentifier2(TIdentifier node) {
        if (this._identifier2_ != null) {
            this._identifier2_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier2_ = node;
    }

    public LinkedList<PAdditionalIdentifier> getAdditionalIdentifiers2() {
        return this._additionalIdentifiers2_;
    }

    public void setAdditionalIdentifiers2(List<PAdditionalIdentifier> list) {
        this._additionalIdentifiers2_.clear();
        this._additionalIdentifiers2_.addAll(list);
        for (PAdditionalIdentifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._identifier1_) + toString(this._additionalIdentifiers1_) + toString(this._barBar_) + toString(this._identifier2_) + toString(this._additionalIdentifiers2_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._identifier1_ == child) {
            this._identifier1_ = null;
            return;
        }
        if (this._additionalIdentifiers1_.remove(child)) {
            return;
        }
        if (this._barBar_ == child) {
            this._barBar_ = null;
            return;
        }
        if (this._identifier2_ == child) {
            this._identifier2_ = null;
            return;
        }
        if (this._additionalIdentifiers2_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._identifier1_ == oldChild) {
            setIdentifier1((TIdentifier) newChild);
            return;
        }
        for (ListIterator<PAdditionalIdentifier> i = this._additionalIdentifiers1_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PAdditionalIdentifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._barBar_ == oldChild) {
            setBarBar((TBarBar) newChild);
            return;
        }
        if (this._identifier2_ == oldChild) {
            setIdentifier2((TIdentifier) newChild);
            return;
        }
        for (ListIterator<PAdditionalIdentifier> i = this._additionalIdentifiers2_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PAdditionalIdentifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        throw new RuntimeException("Not a child.");
    }
}
