package net.sf.dsaman.model.arithmetic.node;

import java.util.*;
import net.sf.dsaman.model.arithmetic.analysis.*;

public final class AFunctionSquareRootArithmeticFunction extends PArithmeticFunction {

    private TFuncSqrt _funcSqrt_;

    private TLPar _lPar_;

    private PArithmeticExpression _parameter_;

    private TRPar _rPar_;

    public AFunctionSquareRootArithmeticFunction() {
    }

    public AFunctionSquareRootArithmeticFunction(TFuncSqrt _funcSqrt_, TLPar _lPar_, PArithmeticExpression _parameter_, TRPar _rPar_) {
        setFuncSqrt(_funcSqrt_);
        setLPar(_lPar_);
        setParameter(_parameter_);
        setRPar(_rPar_);
    }

    public Object clone() {
        return new AFunctionSquareRootArithmeticFunction((TFuncSqrt) cloneNode(_funcSqrt_), (TLPar) cloneNode(_lPar_), (PArithmeticExpression) cloneNode(_parameter_), (TRPar) cloneNode(_rPar_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAFunctionSquareRootArithmeticFunction(this);
    }

    public TFuncSqrt getFuncSqrt() {
        return _funcSqrt_;
    }

    public void setFuncSqrt(TFuncSqrt node) {
        if (_funcSqrt_ != null) {
            _funcSqrt_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _funcSqrt_ = node;
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

    public PArithmeticExpression getParameter() {
        return _parameter_;
    }

    public void setParameter(PArithmeticExpression node) {
        if (_parameter_ != null) {
            _parameter_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _parameter_ = node;
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
        return "" + toString(_funcSqrt_) + toString(_lPar_) + toString(_parameter_) + toString(_rPar_);
    }

    void removeChild(Node child) {
        if (_funcSqrt_ == child) {
            _funcSqrt_ = null;
            return;
        }
        if (_lPar_ == child) {
            _lPar_ = null;
            return;
        }
        if (_parameter_ == child) {
            _parameter_ = null;
            return;
        }
        if (_rPar_ == child) {
            _rPar_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_funcSqrt_ == oldChild) {
            setFuncSqrt((TFuncSqrt) newChild);
            return;
        }
        if (_lPar_ == oldChild) {
            setLPar((TLPar) newChild);
            return;
        }
        if (_parameter_ == oldChild) {
            setParameter((PArithmeticExpression) newChild);
            return;
        }
        if (_rPar_ == oldChild) {
            setRPar((TRPar) newChild);
            return;
        }
    }
}
