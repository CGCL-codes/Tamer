package net.sf.dsaman.model.arithmetic.node;

import java.util.*;
import net.sf.dsaman.model.arithmetic.analysis.*;

public final class AFunctionMaxArithmeticFunction extends PArithmeticFunction {

    private TFuncMax _funcMax_;

    private TLPar _lPar_;

    private PArithmeticParameterList _arithmeticParameterList_;

    private TRPar _rPar_;

    public AFunctionMaxArithmeticFunction() {
    }

    public AFunctionMaxArithmeticFunction(TFuncMax _funcMax_, TLPar _lPar_, PArithmeticParameterList _arithmeticParameterList_, TRPar _rPar_) {
        setFuncMax(_funcMax_);
        setLPar(_lPar_);
        setArithmeticParameterList(_arithmeticParameterList_);
        setRPar(_rPar_);
    }

    public Object clone() {
        return new AFunctionMaxArithmeticFunction((TFuncMax) cloneNode(_funcMax_), (TLPar) cloneNode(_lPar_), (PArithmeticParameterList) cloneNode(_arithmeticParameterList_), (TRPar) cloneNode(_rPar_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAFunctionMaxArithmeticFunction(this);
    }

    public TFuncMax getFuncMax() {
        return _funcMax_;
    }

    public void setFuncMax(TFuncMax node) {
        if (_funcMax_ != null) {
            _funcMax_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _funcMax_ = node;
    }

    public TLPar getLPar() {
        return _lPar_;
    }

    public void setLPar(TLPar node) {
        if (_lPar_ != null) {
            _lPar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _lPar_ = node;
    }

    public PArithmeticParameterList getArithmeticParameterList() {
        return _arithmeticParameterList_;
    }

    public void setArithmeticParameterList(PArithmeticParameterList node) {
        if (_arithmeticParameterList_ != null) {
            _arithmeticParameterList_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _arithmeticParameterList_ = node;
    }

    public TRPar getRPar() {
        return _rPar_;
    }

    public void setRPar(TRPar node) {
        if (_rPar_ != null) {
            _rPar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _rPar_ = node;
    }

    public String toString() {
        return "" + toString(_funcMax_) + toString(_lPar_) + toString(_arithmeticParameterList_) + toString(_rPar_);
    }

    void removeChild(Node child) {
        if (_funcMax_ == child) {
            _funcMax_ = null;
            return;
        }
        if (_lPar_ == child) {
            _lPar_ = null;
            return;
        }
        if (_arithmeticParameterList_ == child) {
            _arithmeticParameterList_ = null;
            return;
        }
        if (_rPar_ == child) {
            _rPar_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_funcMax_ == oldChild) {
            setFuncMax((TFuncMax) newChild);
            return;
        }
        if (_lPar_ == oldChild) {
            setLPar((TLPar) newChild);
            return;
        }
        if (_arithmeticParameterList_ == oldChild) {
            setArithmeticParameterList((PArithmeticParameterList) newChild);
            return;
        }
        if (_rPar_ == oldChild) {
            setRPar((TRPar) newChild);
            return;
        }
    }
}
