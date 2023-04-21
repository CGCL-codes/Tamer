package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AOneIdAssertStatement extends PAssertStatement {

    private TAssert _assert_;

    private TIdentifier _identifier_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers_ = new LinkedList<PAdditionalIdentifier>();

    private TSemi _semi_;

    public AOneIdAssertStatement() {
    }

    public AOneIdAssertStatement(@SuppressWarnings("hiding") TAssert _assert_, @SuppressWarnings("hiding") TIdentifier _identifier_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers_, @SuppressWarnings("hiding") TSemi _semi_) {
        setAssert(_assert_);
        setIdentifier(_identifier_);
        setAdditionalIdentifiers(_additionalIdentifiers_);
        setSemi(_semi_);
    }

    @Override
    public Object clone() {
        return new AOneIdAssertStatement(cloneNode(this._assert_), cloneNode(this._identifier_), cloneList(this._additionalIdentifiers_), cloneNode(this._semi_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAOneIdAssertStatement(this);
    }

    public TAssert getAssert() {
        return this._assert_;
    }

    public void setAssert(TAssert node) {
        if (this._assert_ != null) {
            this._assert_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._assert_ = node;
    }

    public TIdentifier getIdentifier() {
        return this._identifier_;
    }

    public void setIdentifier(TIdentifier node) {
        if (this._identifier_ != null) {
            this._identifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier_ = node;
    }

    public LinkedList<PAdditionalIdentifier> getAdditionalIdentifiers() {
        return this._additionalIdentifiers_;
    }

    public void setAdditionalIdentifiers(List<PAdditionalIdentifier> list) {
        this._additionalIdentifiers_.clear();
        this._additionalIdentifiers_.addAll(list);
        for (PAdditionalIdentifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public TSemi getSemi() {
        return this._semi_;
    }

    public void setSemi(TSemi node) {
        if (this._semi_ != null) {
            this._semi_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._semi_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._assert_) + toString(this._identifier_) + toString(this._additionalIdentifiers_) + toString(this._semi_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._assert_ == child) {
            this._assert_ = null;
            return;
        }
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        if (this._additionalIdentifiers_.remove(child)) {
            return;
        }
        if (this._semi_ == child) {
            this._semi_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._assert_ == oldChild) {
            setAssert((TAssert) newChild);
            return;
        }
        if (this._identifier_ == oldChild) {
            setIdentifier((TIdentifier) newChild);
            return;
        }
        for (ListIterator<PAdditionalIdentifier> i = this._additionalIdentifiers_.listIterator(); i.hasNext(); ) {
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
        if (this._semi_ == oldChild) {
            setSemi((TSemi) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
