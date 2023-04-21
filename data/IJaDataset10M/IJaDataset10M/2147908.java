package tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.node;

import java.util.*;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.analysis.*;

public final class AIdentModelExpression extends PModelExpression {

    private TIdent _ident_;

    public AIdentModelExpression() {
    }

    public AIdentModelExpression(TIdent _ident_) {
        setIdent(_ident_);
    }

    public Object clone() {
        return new AIdentModelExpression((TIdent) cloneNode(_ident_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAIdentModelExpression(this);
    }

    public Object apply(SwitchWithReturn sw, Object param) throws AttrEvalException {
        return ((AnalysisWithReturn) sw).caseAIdentModelExpression(this, param);
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

    public String toString() {
        return "" + toString(_ident_);
    }

    void removeChild(Node child) {
        if (_ident_ == child) {
            _ident_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_ident_ == oldChild) {
            setIdent((TIdent) newChild);
            return;
        }
    }
}
