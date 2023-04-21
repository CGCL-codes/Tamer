package jpdl.parser.node;

import jpdl.parser.analysis.*;

@SuppressWarnings("nls")
public final class AUnaryOpPointcut extends PPointcut {

    private PUnaryOparator _unaryOparator_;

    private PPointcut _pointcut_;

    public AUnaryOpPointcut() {
    }

    public AUnaryOpPointcut(@SuppressWarnings("hiding") PUnaryOparator _unaryOparator_, @SuppressWarnings("hiding") PPointcut _pointcut_) {
        setUnaryOparator(_unaryOparator_);
        setPointcut(_pointcut_);
    }

    @Override
    public Object clone() {
        return new AUnaryOpPointcut(cloneNode(this._unaryOparator_), cloneNode(this._pointcut_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAUnaryOpPointcut(this);
    }

    public PUnaryOparator getUnaryOparator() {
        return this._unaryOparator_;
    }

    public void setUnaryOparator(PUnaryOparator node) {
        if (this._unaryOparator_ != null) {
            this._unaryOparator_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._unaryOparator_ = node;
    }

    public PPointcut getPointcut() {
        return this._pointcut_;
    }

    public void setPointcut(PPointcut node) {
        if (this._pointcut_ != null) {
            this._pointcut_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._pointcut_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._unaryOparator_) + toString(this._pointcut_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._unaryOparator_ == child) {
            this._unaryOparator_ = null;
            return;
        }
        if (this._pointcut_ == child) {
            this._pointcut_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._unaryOparator_ == oldChild) {
            setUnaryOparator((PUnaryOparator) newChild);
            return;
        }
        if (this._pointcut_ == oldChild) {
            setPointcut((PPointcut) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
