package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class ACmosGateInstantiation extends PGateInstantiation {

    private PCmosSwitchtype _cmosSwitchtype_;

    private PDelay3 _delay3_;

    private PCmosSwitchInstances _cmosSwitchInstances_;

    private TTSemicolon _tSemicolon_;

    public ACmosGateInstantiation() {
    }

    public ACmosGateInstantiation(@SuppressWarnings("hiding") PCmosSwitchtype _cmosSwitchtype_, @SuppressWarnings("hiding") PDelay3 _delay3_, @SuppressWarnings("hiding") PCmosSwitchInstances _cmosSwitchInstances_, @SuppressWarnings("hiding") TTSemicolon _tSemicolon_) {
        setCmosSwitchtype(_cmosSwitchtype_);
        setDelay3(_delay3_);
        setCmosSwitchInstances(_cmosSwitchInstances_);
        setTSemicolon(_tSemicolon_);
    }

    @Override
    public Object clone() {
        return new ACmosGateInstantiation(cloneNode(this._cmosSwitchtype_), cloneNode(this._delay3_), cloneNode(this._cmosSwitchInstances_), cloneNode(this._tSemicolon_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseACmosGateInstantiation(this);
    }

    public PCmosSwitchtype getCmosSwitchtype() {
        return this._cmosSwitchtype_;
    }

    public void setCmosSwitchtype(PCmosSwitchtype node) {
        if (this._cmosSwitchtype_ != null) {
            this._cmosSwitchtype_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmosSwitchtype_ = node;
    }

    public PDelay3 getDelay3() {
        return this._delay3_;
    }

    public void setDelay3(PDelay3 node) {
        if (this._delay3_ != null) {
            this._delay3_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._delay3_ = node;
    }

    public PCmosSwitchInstances getCmosSwitchInstances() {
        return this._cmosSwitchInstances_;
    }

    public void setCmosSwitchInstances(PCmosSwitchInstances node) {
        if (this._cmosSwitchInstances_ != null) {
            this._cmosSwitchInstances_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmosSwitchInstances_ = node;
    }

    public TTSemicolon getTSemicolon() {
        return this._tSemicolon_;
    }

    public void setTSemicolon(TTSemicolon node) {
        if (this._tSemicolon_ != null) {
            this._tSemicolon_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tSemicolon_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._cmosSwitchtype_) + toString(this._delay3_) + toString(this._cmosSwitchInstances_) + toString(this._tSemicolon_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._cmosSwitchtype_ == child) {
            this._cmosSwitchtype_ = null;
            return;
        }
        if (this._delay3_ == child) {
            this._delay3_ = null;
            return;
        }
        if (this._cmosSwitchInstances_ == child) {
            this._cmosSwitchInstances_ = null;
            return;
        }
        if (this._tSemicolon_ == child) {
            this._tSemicolon_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._cmosSwitchtype_ == oldChild) {
            setCmosSwitchtype((PCmosSwitchtype) newChild);
            return;
        }
        if (this._delay3_ == oldChild) {
            setDelay3((PDelay3) newChild);
            return;
        }
        if (this._cmosSwitchInstances_ == oldChild) {
            setCmosSwitchInstances((PCmosSwitchInstances) newChild);
            return;
        }
        if (this._tSemicolon_ == oldChild) {
            setTSemicolon((TTSemicolon) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
