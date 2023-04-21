package com.db4o.internal.query.processor;

import com.db4o.foundation.*;

/**
 * @exclude
 */
class QOrder extends Tree {

    static int equalityIDGenerator = 1;

    final QConObject _constraint;

    final QCandidate _candidate;

    private int _equalityID;

    QOrder(QConObject a_constraint, QCandidate a_candidate) {
        _constraint = a_constraint;
        _candidate = a_candidate;
    }

    public boolean isEqual(QOrder other) {
        if (other == null) {
            return false;
        }
        return _equalityID != 0 && _equalityID == other._equalityID;
    }

    public int compare(Tree a_to) {
        int res = internalCompare();
        if (res != 0) {
            return res;
        }
        QOrder other = (QOrder) a_to;
        int equalityID = _equalityID;
        if (equalityID == 0) {
            if (other._equalityID != 0) {
                equalityID = other._equalityID;
            }
        }
        if (equalityID == 0) {
            equalityID = generateEqualityID();
        }
        _equalityID = equalityID;
        other._equalityID = equalityID;
        return res;
    }

    private int internalCompare() {
        int comparisonResult = _constraint._preparedComparison.compareTo(_candidate.value());
        if (comparisonResult > 0) {
            return -_constraint.ordering();
        }
        if (comparisonResult == 0) {
            return 0;
        }
        return _constraint.ordering();
    }

    public Object shallowClone() {
        QOrder order = new QOrder(_constraint, _candidate);
        super.shallowCloneInternal(order);
        return order;
    }

    public Object key() {
        throw new NotImplementedException();
    }

    private static int generateEqualityID() {
        equalityIDGenerator++;
        if (equalityIDGenerator < 1) {
            equalityIDGenerator = 1;
        }
        return equalityIDGenerator;
    }
}
