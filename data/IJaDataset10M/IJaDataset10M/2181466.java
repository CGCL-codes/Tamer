package tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.node;

import java.util.*;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.analysis.*;

public final class AAssignment extends PAssignment {

    private TIdent _ident_;

    private TAssign _assign_;

    private PModelExpression _modelExpression_;

    public AAssignment() {
    }

    public AAssignment(TIdent _ident_, TAssign _assign_, PModelExpression _modelExpression_) {
        setIdent(_ident_);
        setAssign(_assign_);
        setModelExpression(_modelExpression_);
    }

    public Object clone() {
        return new AAssignment((TIdent) cloneNode(_ident_), (TAssign) cloneNode(_assign_), (PModelExpression) cloneNode(_modelExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAAssignment(this);
    }

    public Object apply(SwitchWithReturn sw, Object param) throws AttrEvalException {
        return ((AnalysisWithReturn) sw).caseAAssignment(this, param);
    }

    public TIdent getIdent() {
        return _ident_;
    }

    public void setIdent(TIdent node) {
        if (_ident_ != null) {
            _ident_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _ident_ = node;
    }

    public TAssign getAssign() {
        return _assign_;
    }

    public void setAssign(TAssign node) {
        if (_assign_ != null) {
            _assign_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _assign_ = node;
    }

    public PModelExpression getModelExpression() {
        return _modelExpression_;
    }

    public void setModelExpression(PModelExpression node) {
        if (_modelExpression_ != null) {
            _modelExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _modelExpression_ = node;
    }

    public String toString() {
        return "" + toString(_ident_) + toString(_assign_) + toString(_modelExpression_);
    }

    void removeChild(Node child) {
        if (_ident_ == child) {
            _ident_ = null;
            return;
        }
        if (_assign_ == child) {
            _assign_ = null;
            return;
        }
        if (_modelExpression_ == child) {
            _modelExpression_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_ident_ == oldChild) {
            setIdent((TIdent) newChild);
            return;
        }
        if (_assign_ == oldChild) {
            setAssign((TAssign) newChild);
            return;
        }
        if (_modelExpression_ == oldChild) {
            setModelExpression((PModelExpression) newChild);
            return;
        }
    }
}
