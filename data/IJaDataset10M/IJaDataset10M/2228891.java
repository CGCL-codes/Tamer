package tudresden.ocl.codegen.decl.treegen.node;

import java.util.*;
import tudresden.ocl.codegen.decl.treegen.analysis.*;

public final class AAsteriskSelectList extends PSelectList {

    private TIdentifier _tableQualifier_;

    public AAsteriskSelectList() {
    }

    public AAsteriskSelectList(TIdentifier _tableQualifier_) {
        setTableQualifier(_tableQualifier_);
    }

    public Object clone() {
        return new AAsteriskSelectList((TIdentifier) cloneNode(_tableQualifier_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAAsteriskSelectList(this);
    }

    public TIdentifier getTableQualifier() {
        return _tableQualifier_;
    }

    public void setTableQualifier(TIdentifier node) {
        if (_tableQualifier_ != null) {
            _tableQualifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _tableQualifier_ = node;
    }

    public String toString() {
        return "" + toString(_tableQualifier_);
    }

    void removeChild(Node child) {
        if (_tableQualifier_ == child) {
            _tableQualifier_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_tableQualifier_ == oldChild) {
            setTableQualifier((TIdentifier) newChild);
            return;
        }
    }
}
