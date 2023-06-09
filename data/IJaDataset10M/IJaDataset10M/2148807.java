package org.sablecc.sablecc.node;

import java.util.*;
import org.sablecc.sablecc.analysis.*;

public final class AConcat extends PConcat {

    private final LinkedList _unExps_ = new TypedLinkedList(new UnExps_Cast());

    public AConcat() {
    }

    public AConcat(List _unExps_) {
        {
            this._unExps_.clear();
            this._unExps_.addAll(_unExps_);
        }
    }

    public Object clone() {
        return new AConcat(cloneList(_unExps_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAConcat(this);
    }

    public LinkedList getUnExps() {
        return _unExps_;
    }

    public void setUnExps(List list) {
        _unExps_.clear();
        _unExps_.addAll(list);
    }

    public String toString() {
        return "" + toString(_unExps_);
    }

    void removeChild(Node child) {
        if (_unExps_.remove(child)) {
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        for (ListIterator i = _unExps_.listIterator(); i.hasNext(); ) {
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

    private class UnExps_Cast implements Cast {

        public Object cast(Object o) {
            PUnExp node = (PUnExp) o;
            if ((node.parent() != null) && (node.parent() != AConcat.this)) {
                node.parent().removeChild(node);
            }
            if ((node.parent() == null) || (node.parent() != AConcat.this)) {
                node.parent(AConcat.this);
            }
            return node;
        }
    }
}
