package org.jmlspecs.samples.list.list2;

import org.jmlspecs.models.JMLObjectSequence;
import org.jmlspecs.samples.list.node.OneWayNode;
import org.jmlspecs.samples.list.node.TwoWayNode;
import org.jmlspecs.samples.list.iterator.RestartableIterator;

public class TwoWayIterator implements RestartableIterator {

    protected TwoWayNode firstLink_;

    protected TwoWayNode currLink_;

    protected TwoWayNode lastLink_;

    public TwoWayIterator(TwoWayNode link) {
        firstLink_ = link;
        currLink_ = firstLink_;
        lastLink_ = firstLink_;
        first();
        while (!isDone()) {
            lastLink_ = currLink_;
            next();
        }
        first();
    }

    public void first() {
        currLink_ = (TwoWayNode) firstLink_.getNextNode();
    }

    public void next() {
        currLink_ = (TwoWayNode) currLink_.getNextNode();
    }

    public boolean isDone() {
        return currLink_ == null;
    }

    public Object currentItem() {
        return currLink_.getEntry();
    }

    public void last() {
        currLink_ = lastLink_;
    }

    public void previous() {
        if (currLink_ == null) {
            last();
        } else {
            currLink_ = currLink_.getPrevNode();
        }
    }

    public boolean isAtFront() {
        if (currLink_ == null) {
            return firstLink_.getNextNode() == null;
        } else {
            return currLink_.getPrevNode() == firstLink_;
        }
    }

    protected TwoWayIterator() {
        firstLink_ = new TwoWayNode();
        currLink_ = firstLink_;
        lastLink_ = firstLink_;
        first();
    }

    public String toString() {
        OneWayNode curr = firstLink_.getNextNode();
        String str = "";
        int index = 0;
        if (currLink_ == firstLink_) {
            index = -1;
            str += " || <";
        } else {
            str += "<";
            while (curr != currLink_) {
                str += curr.getEntry();
                if (curr.hasNext()) {
                    str += ", ";
                }
                curr = curr.getNextNode();
                index++;
            }
            str += " || ";
        }
        while (curr != null) {
            str += curr.getEntry();
            if (curr.hasNext()) {
                str += ", ";
            }
            curr = curr.getNextNode();
        }
        str += "> currIndex=" + index;
        return str;
    }
}
