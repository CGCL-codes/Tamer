package org.sablecc.sablecc.node;

import java.util.*;
import org.sablecc.sablecc.analysis.*;

public final class AHelpers extends PHelpers {

    private final LinkedList _helperDefs_ = new TypedLinkedList(new HelperDefs_Cast());

    public AHelpers() {
    }

    public AHelpers(List _helperDefs_) {
        {
            this._helperDefs_.clear();
            this._helperDefs_.addAll(_helperDefs_);
        }
    }

    public Object clone() {
        return new AHelpers(cloneList(_helperDefs_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAHelpers(this);
    }

    public LinkedList getHelperDefs() {
        return _helperDefs_;
    }

    public void setHelperDefs(List list) {
        _helperDefs_.clear();
        _helperDefs_.addAll(list);
    }

    public String toString() {
        return "" + toString(_helperDefs_);
    }

    void removeChild(Node child) {
        if (_helperDefs_.remove(child)) {
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        for (ListIterator i = _helperDefs_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set(newChild);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
    }

    private class HelperDefs_Cast implements Cast {

        public Object cast(Object o) {
            PHelperDef node = (PHelperDef) o;
            if ((node.parent() != null) && (node.parent() != AHelpers.this)) {
                node.parent().removeChild(node);
            }
            if ((node.parent() == null) || (node.parent() != AHelpers.this)) {
                node.parent(AHelpers.this);
            }
            return node;
        }
    }
}
