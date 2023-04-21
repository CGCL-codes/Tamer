package musicseeder.node;

import musicseeder.analysis.*;

@SuppressWarnings("nls")
public final class ASplitNote extends PNote {

    private PDivision _division_;

    private PNote _first_;

    private PNote _second_;

    public ASplitNote() {
    }

    public ASplitNote(@SuppressWarnings("hiding") PDivision _division_, @SuppressWarnings("hiding") PNote _first_, @SuppressWarnings("hiding") PNote _second_) {
        setDivision(_division_);
        setFirst(_first_);
        setSecond(_second_);
    }

    @Override
    public Object clone() {
        return new ASplitNote(cloneNode(this._division_), cloneNode(this._first_), cloneNode(this._second_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASplitNote(this);
    }

    public PDivision getDivision() {
        return this._division_;
    }

    public void setDivision(PDivision node) {
        if (this._division_ != null) {
            this._division_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._division_ = node;
    }

    public PNote getFirst() {
        return this._first_;
    }

    public void setFirst(PNote node) {
        if (this._first_ != null) {
            this._first_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._first_ = node;
    }

    public PNote getSecond() {
        return this._second_;
    }

    public void setSecond(PNote node) {
        if (this._second_ != null) {
            this._second_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._second_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._division_) + toString(this._first_) + toString(this._second_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._division_ == child) {
            this._division_ = null;
            return;
        }
        if (this._first_ == child) {
            this._first_ = null;
            return;
        }
        if (this._second_ == child) {
            this._second_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._division_ == oldChild) {
            setDivision((PDivision) newChild);
            return;
        }
        if (this._first_ == oldChild) {
            setFirst((PNote) newChild);
            return;
        }
        if (this._second_ == oldChild) {
            setSecond((PNote) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
