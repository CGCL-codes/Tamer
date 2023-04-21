package com.eptica.ias.models.requests.request.comparator;

import java.io.*;
import java.util.Comparator;
import com.eptica.ias.models.requests.request.*;

/**
 * A comparator that compares RequestModel based on their lastMessageSubject field.<br/>
 * Note: this comparator imposes orderings that are inconsistent with equals.
 */
public class RequestByLastMessageSubjectComparator<T extends RequestModel> implements Serializable, Comparator<RequestModel> {

    private boolean isReverseOrder = false;

    /**
     * Constructs a new RequestByLastMessageSubjectComparator that relies on the 
     * String natural ordering.
     */
    public RequestByLastMessageSubjectComparator() {
        isReverseOrder = false;
    }

    /**
     * Constructs a new RequestByLastMessageSubjectComparator that relies on the 
     * String ordering.
     * @param isReverseOrder true to obtain a reverse order, false otherwise.
     */
    public RequestByLastMessageSubjectComparator(boolean isReverseOrder) {
        this.isReverseOrder = isReverseOrder;
    }

    /**
     * Compare two RequestModel by comparing their lastMessageSubject field.
     * @param firstItem a request
     * @param secondItem another request
     */
    public int compare(RequestModel firstItem, RequestModel secondItem) {
        int result = 0;
        String lastMessageSubject1 = firstItem.getLastMessageSubject();
        String lastMessageSubject2 = secondItem.getLastMessageSubject();
        if (lastMessageSubject1 == null && lastMessageSubject2 == null) {
            result = 0;
        } else if (lastMessageSubject1 == null && lastMessageSubject2 != null) {
            result = 1;
        } else if (lastMessageSubject1 != null && lastMessageSubject2 == null) {
            result = -1;
        } else {
            result = lastMessageSubject1.compareTo(lastMessageSubject2);
        }
        if (result == 0) {
            result = firstItem.compareTo(secondItem);
        }
        return isReverseOrder ? -1 * result : result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RequestByLastMessageSubjectComparator)) {
            return false;
        }
        return (this.isReverseOrder == ((RequestByLastMessageSubjectComparator) obj).isReverseOrder);
    }

    /**
     * When two objects are equals, their hashcode must be equal too
     * @see #equals(Object)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (this.isReverseOrder ? -1 : 1) * getClass().hashCode();
    }
}
