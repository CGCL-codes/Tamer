package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AMultiMintypmaxExpression extends PMintypmaxExpression {

    private PExpression _e1_;

    private TTColon _c1_;

    private PExpression _e2_;

    private TTColon _c2_;

    private PExpression _e3_;

    public AMultiMintypmaxExpression() {
    }

    public AMultiMintypmaxExpression(@SuppressWarnings("hiding") PExpression _e1_, @SuppressWarnings("hiding") TTColon _c1_, @SuppressWarnings("hiding") PExpression _e2_, @SuppressWarnings("hiding") TTColon _c2_, @SuppressWarnings("hiding") PExpression _e3_) {
        setE1(_e1_);
        setC1(_c1_);
        setE2(_e2_);
        setC2(_c2_);
        setE3(_e3_);
    }

    @Override
    public Object clone() {
        return new AMultiMintypmaxExpression(cloneNode(this._e1_), cloneNode(this._c1_), cloneNode(this._e2_), cloneNode(this._c2_), cloneNode(this._e3_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAMultiMintypmaxExpression(this);
    }

    public PExpression getE1() {
        return this._e1_;
    }

    public void setE1(PExpression node) {
        if (this._e1_ != null) {
            this._e1_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._e1_ = node;
    }

    public TTColon getC1() {
        return this._c1_;
    }

    public void setC1(TTColon node) {
        if (this._c1_ != null) {
            this._c1_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._c1_ = node;
    }

    public PExpression getE2() {
        return this._e2_;
    }

    public void setE2(PExpression node) {
        if (this._e2_ != null) {
            this._e2_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._e2_ = node;
    }

    public TTColon getC2() {
        return this._c2_;
    }

    public void setC2(TTColon node) {
        if (this._c2_ != null) {
            this._c2_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._c2_ = node;
    }

    public PExpression getE3() {
        return this._e3_;
    }

    public void setE3(PExpression node) {
        if (this._e3_ != null) {
            this._e3_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._e3_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._e1_) + toString(this._c1_) + toString(this._e2_) + toString(this._c2_) + toString(this._e3_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._e1_ == child) {
            this._e1_ = null;
            return;
        }
        if (this._c1_ == child) {
            this._c1_ = null;
            return;
        }
        if (this._e2_ == child) {
            this._e2_ = null;
            return;
        }
        if (this._c2_ == child) {
            this._c2_ = null;
            return;
        }
        if (this._e3_ == child) {
            this._e3_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._e1_ == oldChild) {
            setE1((PExpression) newChild);
            return;
        }
        if (this._c1_ == oldChild) {
            setC1((TTColon) newChild);
            return;
        }
        if (this._e2_ == oldChild) {
            setE2((PExpression) newChild);
            return;
        }
        if (this._c2_ == oldChild) {
            setC2((TTColon) newChild);
            return;
        }
        if (this._e3_ == oldChild) {
            setE3((PExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
