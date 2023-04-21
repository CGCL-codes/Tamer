package tudresden.ocl.codegen.decl.treegen.node;

import java.util.*;
import tudresden.ocl.codegen.decl.treegen.analysis.*;

public final class ALteqRelationalExpression extends PRelationalExpression {

    private PNumericExpression _op1_;

    private PNumericExpression _op2_;

    public ALteqRelationalExpression() {
    }

    public ALteqRelationalExpression(PNumericExpression _op1_, PNumericExpression _op2_) {
        setOp1(_op1_);
        setOp2(_op2_);
    }

    public Object clone() {
        return new ALteqRelationalExpression((PNumericExpression) cloneNode(_op1_), (PNumericExpression) cloneNode(_op2_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseALteqRelationalExpression(this);
    }

    public PNumericExpression getOp1() {
        return _op1_;
    }

    public void setOp1(PNumericExpression node) {
        if (_op1_ != null) {
            _op1_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _op1_ = node;
    }

    public PNumericExpression getOp2() {
        return _op2_;
    }

    public void setOp2(PNumericExpression node) {
        if (_op2_ != null) {
            _op2_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _op2_ = node;
    }

    public String toString() {
        return "" + toString(_op1_) + toString(_op2_);
    }

    void removeChild(Node child) {
        if (_op1_ == child) {
            _op1_ = null;
            return;
        }
        if (_op2_ == child) {
            _op2_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_op1_ == oldChild) {
            setOp1((PNumericExpression) newChild);
            return;
        }
        if (_op2_ == oldChild) {
            setOp2((PNumericExpression) newChild);
            return;
        }
    }
}
