package com.eptica.ias.models.requests.requestinfo.comparator;

import java.io.*;
import java.util.Comparator;
import com.eptica.ias.models.requests.requestinfo.*;

/**
 * A comparator that compares RequestInfoModel based on their fieldName field.<br/>
 * Note: this comparator imposes orderings that are inconsistent with equals.
 */
public class RequestInfoByFieldNameComparator<T extends RequestInfoModel> implements Serializable, Comparator<RequestInfoModel> {

    private boolean isReverseOrder = false;

    /**
     * Constructs a new RequestInfoByFieldNameComparator that relies on the 
     * String natural ordering.
     */
    public RequestInfoByFieldNameComparator() {
        isReverseOrder = false;
    }

    /**
     * Constructs a new RequestInfoByFieldNameComparator that relies on the 
     * String ordering.
     * @param isReverseOrder true to obtain a reverse order, false otherwise.
     */
    public RequestInfoByFieldNameComparator(boolean isReverseOrder) {
        this.isReverseOrder = isReverseOrder;
    }

    /**
     * Compare two RequestInfoModel by comparing their fieldName field.
     * @param firstItem a requestInfo
     * @param secondItem another requestInfo
     */
    public int compare(RequestInfoModel firstItem, RequestInfoModel secondItem) {
        int result = 0;
        String fieldName1 = firstItem.getFieldName();
        String fieldName2 = secondItem.getFieldName();
        if (fieldName1 == null && fieldName2 == null) {
            result = 0;
        } else if (fieldName1 == null && fieldName2 != null) {
            result = 1;
        } else if (fieldName1 != null && fieldName2 == null) {
            result = -1;
        } else {
            result = fieldName1.compareTo(fieldName2);
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
        if (!(obj instanceof RequestInfoByFieldNameComparator)) {
            return false;
        }
        return (this.isReverseOrder == ((RequestInfoByFieldNameComparator) obj).isReverseOrder);
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
