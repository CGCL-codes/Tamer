package tudresden.ocl.codegen.decl.treegen.node;

import java.util.*;
import tudresden.ocl.codegen.decl.treegen.analysis.*;

public final class AQuerySpecQueryExpression extends PQueryExpression {

    private PSelectClause _selectClause_;

    private PFromClause _fromClause_;

    private PWhereClause _whereClause_;

    public AQuerySpecQueryExpression() {
    }

    public AQuerySpecQueryExpression(PSelectClause _selectClause_, PFromClause _fromClause_, PWhereClause _whereClause_) {
        setSelectClause(_selectClause_);
        setFromClause(_fromClause_);
        setWhereClause(_whereClause_);
    }

    public Object clone() {
        return new AQuerySpecQueryExpression((PSelectClause) cloneNode(_selectClause_), (PFromClause) cloneNode(_fromClause_), (PWhereClause) cloneNode(_whereClause_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAQuerySpecQueryExpression(this);
    }

    public PSelectClause getSelectClause() {
        return _selectClause_;
    }

    public void setSelectClause(PSelectClause node) {
        if (_selectClause_ != null) {
            _selectClause_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _selectClause_ = node;
    }

    public PFromClause getFromClause() {
        return _fromClause_;
    }

    public void setFromClause(PFromClause node) {
        if (_fromClause_ != null) {
            _fromClause_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _fromClause_ = node;
    }

    public PWhereClause getWhereClause() {
        return _whereClause_;
    }

    public void setWhereClause(PWhereClause node) {
        if (_whereClause_ != null) {
            _whereClause_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _whereClause_ = node;
    }

    public String toString() {
        return "" + toString(_selectClause_) + toString(_fromClause_) + toString(_whereClause_);
    }

    void removeChild(Node child) {
        if (_selectClause_ == child) {
            _selectClause_ = null;
            return;
        }
        if (_fromClause_ == child) {
            _fromClause_ = null;
            return;
        }
        if (_whereClause_ == child) {
            _whereClause_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_selectClause_ == oldChild) {
            setSelectClause((PSelectClause) newChild);
            return;
        }
        if (_fromClause_ == oldChild) {
            setFromClause((PFromClause) newChild);
            return;
        }
        if (_whereClause_ == oldChild) {
            setWhereClause((PWhereClause) newChild);
            return;
        }
    }
}
