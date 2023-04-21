package iwork.patchpanel.manager.script.node;

import java.util.*;
import iwork.patchpanel.manager.script.analysis.*;

public final class AWithspaceTypeName extends PTypeName {

    private PNameWithSpace _nameWithSpace_;

    public AWithspaceTypeName() {
    }

    public AWithspaceTypeName(PNameWithSpace _nameWithSpace_) {
        setNameWithSpace(_nameWithSpace_);
    }

    public Object clone() {
        return new AWithspaceTypeName((PNameWithSpace) cloneNode(_nameWithSpace_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAWithspaceTypeName(this);
    }

    public PNameWithSpace getNameWithSpace() {
        return _nameWithSpace_;
    }

    public void setNameWithSpace(PNameWithSpace node) {
        if (_nameWithSpace_ != null) {
            _nameWithSpace_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _nameWithSpace_ = node;
    }

    public String toString() {
        return "" + toString(_nameWithSpace_);
    }

    void removeChild(Node child) {
        if (_nameWithSpace_ == child) {
            _nameWithSpace_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_nameWithSpace_ == oldChild) {
            setNameWithSpace((PNameWithSpace) newChild);
            return;
        }
    }
}
