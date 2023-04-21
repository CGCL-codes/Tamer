package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class APassSwitchInstance extends PPassSwitchInstance {

    private PNameOfGateInstance _nameOfGateInstance_;

    private TTLparen _tLparen_;

    private PTerminal _t1_;

    private TTComma _tComma_;

    private PTerminal _t2_;

    private TTRparen _tRparen_;

    public APassSwitchInstance() {
    }

    public APassSwitchInstance(@SuppressWarnings("hiding") PNameOfGateInstance _nameOfGateInstance_, @SuppressWarnings("hiding") TTLparen _tLparen_, @SuppressWarnings("hiding") PTerminal _t1_, @SuppressWarnings("hiding") TTComma _tComma_, @SuppressWarnings("hiding") PTerminal _t2_, @SuppressWarnings("hiding") TTRparen _tRparen_) {
        setNameOfGateInstance(_nameOfGateInstance_);
        setTLparen(_tLparen_);
        setT1(_t1_);
        setTComma(_tComma_);
        setT2(_t2_);
        setTRparen(_tRparen_);
    }

    @Override
    public Object clone() {
        return new APassSwitchInstance(cloneNode(this._nameOfGateInstance_), cloneNode(this._tLparen_), cloneNode(this._t1_), cloneNode(this._tComma_), cloneNode(this._t2_), cloneNode(this._tRparen_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPassSwitchInstance(this);
    }

    public PNameOfGateInstance getNameOfGateInstance() {
        return this._nameOfGateInstance_;
    }

    public void setNameOfGateInstance(PNameOfGateInstance node) {
        if (this._nameOfGateInstance_ != null) {
            this._nameOfGateInstance_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._nameOfGateInstance_ = node;
    }

    public TTLparen getTLparen() {
        return this._tLparen_;
    }

    public void setTLparen(TTLparen node) {
        if (this._tLparen_ != null) {
            this._tLparen_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tLparen_ = node;
    }

    public PTerminal getT1() {
        return this._t1_;
    }

    public void setT1(PTerminal node) {
        if (this._t1_ != null) {
            this._t1_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._t1_ = node;
    }

    public TTComma getTComma() {
        return this._tComma_;
    }

    public void setTComma(TTComma node) {
        if (this._tComma_ != null) {
            this._tComma_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tComma_ = node;
    }

    public PTerminal getT2() {
        return this._t2_;
    }

    public void setT2(PTerminal node) {
        if (this._t2_ != null) {
            this._t2_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._t2_ = node;
    }

    public TTRparen getTRparen() {
        return this._tRparen_;
    }

    public void setTRparen(TTRparen node) {
        if (this._tRparen_ != null) {
            this._tRparen_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tRparen_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._nameOfGateInstance_) + toString(this._tLparen_) + toString(this._t1_) + toString(this._tComma_) + toString(this._t2_) + toString(this._tRparen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._nameOfGateInstance_ == child) {
            this._nameOfGateInstance_ = null;
            return;
        }
        if (this._tLparen_ == child) {
            this._tLparen_ = null;
            return;
        }
        if (this._t1_ == child) {
            this._t1_ = null;
            return;
        }
        if (this._tComma_ == child) {
            this._tComma_ = null;
            return;
        }
        if (this._t2_ == child) {
            this._t2_ = null;
            return;
        }
        if (this._tRparen_ == child) {
            this._tRparen_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._nameOfGateInstance_ == oldChild) {
            setNameOfGateInstance((PNameOfGateInstance) newChild);
            return;
        }
        if (this._tLparen_ == oldChild) {
            setTLparen((TTLparen) newChild);
            return;
        }
        if (this._t1_ == oldChild) {
            setT1((PTerminal) newChild);
            return;
        }
        if (this._tComma_ == oldChild) {
            setTComma((TTComma) newChild);
            return;
        }
        if (this._t2_ == oldChild) {
            setT2((PTerminal) newChild);
            return;
        }
        if (this._tRparen_ == oldChild) {
            setTRparen((TTRparen) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
