package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class ANoutputGateInstantiation extends PGateInstantiation {

    private PNOutputGatetype _nOutputGatetype_;

    private PDriveStrength _driveStrength_;

    private PDelay2 _delay2_;

    private PNOutputGateInstances _nOutputGateInstances_;

    private TTSemicolon _tSemicolon_;

    public ANoutputGateInstantiation() {
    }

    public ANoutputGateInstantiation(@SuppressWarnings("hiding") PNOutputGatetype _nOutputGatetype_, @SuppressWarnings("hiding") PDriveStrength _driveStrength_, @SuppressWarnings("hiding") PDelay2 _delay2_, @SuppressWarnings("hiding") PNOutputGateInstances _nOutputGateInstances_, @SuppressWarnings("hiding") TTSemicolon _tSemicolon_) {
        setNOutputGatetype(_nOutputGatetype_);
        setDriveStrength(_driveStrength_);
        setDelay2(_delay2_);
        setNOutputGateInstances(_nOutputGateInstances_);
        setTSemicolon(_tSemicolon_);
    }

    @Override
    public Object clone() {
        return new ANoutputGateInstantiation(cloneNode(this._nOutputGatetype_), cloneNode(this._driveStrength_), cloneNode(this._delay2_), cloneNode(this._nOutputGateInstances_), cloneNode(this._tSemicolon_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseANoutputGateInstantiation(this);
    }

    public PNOutputGatetype getNOutputGatetype() {
        return this._nOutputGatetype_;
    }

    public void setNOutputGatetype(PNOutputGatetype node) {
        if (this._nOutputGatetype_ != null) {
            this._nOutputGatetype_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._nOutputGatetype_ = node;
    }

    public PDriveStrength getDriveStrength() {
        return this._driveStrength_;
    }

    public void setDriveStrength(PDriveStrength node) {
        if (this._driveStrength_ != null) {
            this._driveStrength_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._driveStrength_ = node;
    }

    public PDelay2 getDelay2() {
        return this._delay2_;
    }

    public void setDelay2(PDelay2 node) {
        if (this._delay2_ != null) {
            this._delay2_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._delay2_ = node;
    }

    public PNOutputGateInstances getNOutputGateInstances() {
        return this._nOutputGateInstances_;
    }

    public void setNOutputGateInstances(PNOutputGateInstances node) {
        if (this._nOutputGateInstances_ != null) {
            this._nOutputGateInstances_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._nOutputGateInstances_ = node;
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
        return "" + toString(this._nOutputGatetype_) + toString(this._driveStrength_) + toString(this._delay2_) + toString(this._nOutputGateInstances_) + toString(this._tSemicolon_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._nOutputGatetype_ == child) {
            this._nOutputGatetype_ = null;
            return;
        }
        if (this._driveStrength_ == child) {
            this._driveStrength_ = null;
            return;
        }
        if (this._delay2_ == child) {
            this._delay2_ = null;
            return;
        }
        if (this._nOutputGateInstances_ == child) {
            this._nOutputGateInstances_ = null;
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
        if (this._nOutputGatetype_ == oldChild) {
            setNOutputGatetype((PNOutputGatetype) newChild);
            return;
        }
        if (this._driveStrength_ == oldChild) {
            setDriveStrength((PDriveStrength) newChild);
            return;
        }
        if (this._delay2_ == oldChild) {
            setDelay2((PDelay2) newChild);
            return;
        }
        if (this._nOutputGateInstances_ == oldChild) {
            setNOutputGateInstances((PNOutputGateInstances) newChild);
            return;
        }
        if (this._tSemicolon_ == oldChild) {
            setTSemicolon((TTSemicolon) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
