package net.sourceforge.jdefprog.mcl.generated.node;

import net.sourceforge.jdefprog.mcl.generated.analysis.*;

@SuppressWarnings("nls")
public final class ARelexprLevelFive extends PLevelFive {

    private PLevelFive _levelFive_;

    private TRelop _relop_;

    private PLevelFour _levelFour_;

    public ARelexprLevelFive() {
    }

    public ARelexprLevelFive(@SuppressWarnings("hiding") PLevelFive _levelFive_, @SuppressWarnings("hiding") TRelop _relop_, @SuppressWarnings("hiding") PLevelFour _levelFour_) {
        setLevelFive(_levelFive_);
        setRelop(_relop_);
        setLevelFour(_levelFour_);
    }

    @Override
    public Object clone() {
        return new ARelexprLevelFive(cloneNode(this._levelFive_), cloneNode(this._relop_), cloneNode(this._levelFour_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseARelexprLevelFive(this);
    }

    public PLevelFive getLevelFive() {
        return this._levelFive_;
    }

    public void setLevelFive(PLevelFive node) {
        if (this._levelFive_ != null) {
            this._levelFive_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._levelFive_ = node;
    }

    public TRelop getRelop() {
        return this._relop_;
    }

    public void setRelop(TRelop node) {
        if (this._relop_ != null) {
            this._relop_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._relop_ = node;
    }

    public PLevelFour getLevelFour() {
        return this._levelFour_;
    }

    public void setLevelFour(PLevelFour node) {
        if (this._levelFour_ != null) {
            this._levelFour_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._levelFour_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._levelFive_) + toString(this._relop_) + toString(this._levelFour_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._levelFive_ == child) {
            this._levelFive_ = null;
            return;
        }
        if (this._relop_ == child) {
            this._relop_ = null;
            return;
        }
        if (this._levelFour_ == child) {
            this._levelFour_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._levelFive_ == oldChild) {
            setLevelFive((PLevelFive) newChild);
            return;
        }
        if (this._relop_ == oldChild) {
            setRelop((TRelop) newChild);
            return;
        }
        if (this._levelFour_ == oldChild) {
            setLevelFour((PLevelFour) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
