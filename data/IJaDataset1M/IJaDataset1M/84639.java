package com.eptica.ias.models.requests.request.comparator;

import java.io.*;
import java.util.Comparator;
import com.eptica.ias.models.requests.request.*;

/**
 * A comparator that compares RequestModel based on their lastRequestEventTypeId field.<br/>
 * Note: this comparator imposes orderings that are inconsistent with equals.
 */
public class RequestByLastRequestEventTypeIdComparator<T extends RequestModel> implements Serializable, Comparator<RequestModel> {

    private boolean isReverseOrder = false;

    /**
     * Constructs a new RequestByLastRequestEventTypeIdComparator that relies on the 
     * Integer natural ordering.
     */
    public RequestByLastRequestEventTypeIdComparator() {
        isReverseOrder = false;
    }

    /**
     * Constructs a new RequestByLastRequestEventTypeIdComparator that relies on the 
     * Integer ordering.
     * @param isReverseOrder true to obtain a reverse order, false otherwise.
     */
    public RequestByLastRequestEventTypeIdComparator(boolean isReverseOrder) {
        this.isReverseOrder = isReverseOrder;
    }

    /**
     * Compare two RequestModel by comparing their lastRequestEventTypeId field.
     * @param firstItem a request
     * @param secondItem another request
     */
    public int compare(RequestModel firstItem, RequestModel secondItem) {
        int result = 0;
        Integer lastRequestEventTypeId1 = firstItem.getLastRequestEventTypeId();
        Integer lastRequestEventTypeId2 = secondItem.getLastRequestEventTypeId();
        if (lastRequestEventTypeId1 == null && lastRequestEventTypeId2 == null) {
            result = 0;
        } else if (lastRequestEventTypeId1 == null && lastRequestEventTypeId2 != null) {
            result = 1;
        } else if (lastRequestEventTypeId1 != null && lastRequestEventTypeId2 == null) {
            result = -1;
        } else {
            result = lastRequestEventTypeId1.compareTo(lastRequestEventTypeId2);
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
        if (!(obj instanceof RequestByLastRequestEventTypeIdComparator)) {
            return false;
        }
        return (this.isReverseOrder == ((RequestByLastRequestEventTypeIdComparator) obj).isReverseOrder);
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
