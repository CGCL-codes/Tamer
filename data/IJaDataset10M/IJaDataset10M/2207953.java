package net.sf.dsaman.model.arithmetic.node;

import java.util.*;
import net.sf.dsaman.model.arithmetic.analysis.*;

public final class AIdentifierOfTypeStringStringExpression extends PStringExpression {

    private TIdentifierPath _identifierPath_;

    public AIdentifierOfTypeStringStringExpression() {
    }

    public AIdentifierOfTypeStringStringExpression(TIdentifierPath _identifierPath_) {
        setIdentifierPath(_identifierPath_);
    }

    public Object clone() {
        return new AIdentifierOfTypeStringStringExpression((TIdentifierPath) cloneNode(_identifierPath_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAIdentifierOfTypeStringStringExpression(this);
    }

    public TIdentifierPath getIdentifierPath() {
        return _identifierPath_;
    }

    public void setIdentifierPath(TIdentifierPath node) {
        if (_identifierPath_ != null) {
            _identifierPath_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _identifierPath_ = node;
    }

    public String toString() {
        return "" + toString(_identifierPath_);
    }

    void removeChild(Node child) {
        if (_identifierPath_ == child) {
            _identifierPath_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_identifierPath_ == oldChild) {
            setIdentifierPath((TIdentifierPath) newChild);
            return;
        }
    }
}
