package com.eptica.ias.models.requests.request.comparator;

import java.io.*;
import java.util.Comparator;
import com.eptica.ias.models.requests.request.*;

/**
 * A comparator that compares RequestModel based on their requestOriginId field.<br/>
 * Note: this comparator imposes orderings that are inconsistent with equals.
 */
public class RequestByRequestOriginIdComparator<T extends RequestModel> implements Serializable, Comparator<RequestModel> {

    private boolean isReverseOrder = false;

    /**
     * Constructs a new RequestByRequestOriginIdComparator that relies on the 
     * Integer natural ordering.
     */
    public RequestByRequestOriginIdComparator() {
        isReverseOrder = false;
    }

    /**
     * Constructs a new RequestByRequestOriginIdComparator that relies on the 
     * Integer ordering.
     * @param isReverseOrder true to obtain a reverse order, false otherwise.
     */
    public RequestByRequestOriginIdComparator(boolean isReverseOrder) {
        this.isReverseOrder = isReverseOrder;
    }

    /**
     * Compare two RequestModel by comparing their requestOriginId field.
     * @param firstItem a request
     * @param secondItem another request
     */
    public int compare(RequestModel firstItem, RequestModel secondItem) {
        int result = 0;
        Integer requestOriginId1 = firstItem.getRequestOriginId();
        Integer requestOriginId2 = secondItem.getRequestOriginId();
        if (requestOriginId1 == null && requestOriginId2 == null) {
            result = 0;
        } else if (requestOriginId1 == null && requestOriginId2 != null) {
            result = 1;
        } else if (requestOriginId1 != null && requestOriginId2 == null) {
            result = -1;
        } else {
            result = requestOriginId1.compareTo(requestOriginId2);
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
        if (!(obj instanceof RequestByRequestOriginIdComparator)) {
            return false;
        }
        return (this.isReverseOrder == ((RequestByRequestOriginIdComparator) obj).isReverseOrder);
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
