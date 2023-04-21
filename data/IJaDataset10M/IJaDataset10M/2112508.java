package com.eptica.ias.models.resources.comparator;

import java.io.*;
import java.util.Comparator;
import com.eptica.ias.models.resources.*;

/**
 * A comparator that compares ApplicationResourceModel based on their value field.<br/>
 * Note: this comparator imposes orderings that are inconsistent with equals.
 */
public class ApplicationResourceByValueComparator<T extends ApplicationResourceModel> implements Serializable, Comparator<ApplicationResourceModel> {

    private boolean isReverseOrder = false;

    /**
     * Constructs a new ApplicationResourceByValueComparator that relies on the 
     * String natural ordering.
     */
    public ApplicationResourceByValueComparator() {
        isReverseOrder = false;
    }

    /**
     * Constructs a new ApplicationResourceByValueComparator that relies on the 
     * String ordering.
     * @param isReverseOrder true to obtain a reverse order, false otherwise.
     */
    public ApplicationResourceByValueComparator(boolean isReverseOrder) {
        this.isReverseOrder = isReverseOrder;
    }

    /**
     * Compare two ApplicationResourceModel by comparing their value field.
     * @param firstItem a applicationResource
     * @param secondItem another applicationResource
     */
    public int compare(ApplicationResourceModel firstItem, ApplicationResourceModel secondItem) {
        int result = 0;
        String value1 = firstItem.getValue();
        String value2 = secondItem.getValue();
        if (value1 == null && value2 == null) {
            result = 0;
        } else if (value1 == null && value2 != null) {
            result = 1;
        } else if (value1 != null && value2 == null) {
            result = -1;
        } else {
            result = value1.compareTo(value2);
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
        if (!(obj instanceof ApplicationResourceByValueComparator)) {
            return false;
        }
        return (this.isReverseOrder == ((ApplicationResourceByValueComparator) obj).isReverseOrder);
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
