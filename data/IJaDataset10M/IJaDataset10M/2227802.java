package com.eptica.ias.models.externalspecialist.comparator;

import java.io.*;
import java.util.Comparator;
import com.eptica.ias.models.externalspecialist.*;

/**
 * A comparator that compares ExternalSpecialistModel based on their externalSpecialistId field.<br/>
 * Note: this comparator imposes orderings that are inconsistent with equals.
 */
public class ExternalSpecialistByExternalSpecialistIdComparator<T extends ExternalSpecialistModel> implements Serializable, Comparator<ExternalSpecialistModel> {

    private boolean isReverseOrder = false;

    /**
     * Constructs a new ExternalSpecialistByExternalSpecialistIdComparator that relies on the 
     * String natural ordering.
     */
    public ExternalSpecialistByExternalSpecialistIdComparator() {
        isReverseOrder = false;
    }

    /**
     * Constructs a new ExternalSpecialistByExternalSpecialistIdComparator that relies on the 
     * String ordering.
     * @param isReverseOrder true to obtain a reverse order, false otherwise.
     */
    public ExternalSpecialistByExternalSpecialistIdComparator(boolean isReverseOrder) {
        this.isReverseOrder = isReverseOrder;
    }

    /**
     * Compare two ExternalSpecialistModel by comparing their externalSpecialistId field.
     * @param firstItem a externalSpecialist
     * @param secondItem another externalSpecialist
     */
    public int compare(ExternalSpecialistModel firstItem, ExternalSpecialistModel secondItem) {
        int result = 0;
        String externalSpecialistId1 = firstItem.getExternalSpecialistId();
        String externalSpecialistId2 = secondItem.getExternalSpecialistId();
        if (externalSpecialistId1 == null && externalSpecialistId2 == null) {
            result = 0;
        } else if (externalSpecialistId1 == null && externalSpecialistId2 != null) {
            result = 1;
        } else if (externalSpecialistId1 != null && externalSpecialistId2 == null) {
            result = -1;
        } else {
            result = externalSpecialistId1.compareTo(externalSpecialistId2);
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
        if (!(obj instanceof ExternalSpecialistByExternalSpecialistIdComparator)) {
            return false;
        }
        return (this.isReverseOrder == ((ExternalSpecialistByExternalSpecialistIdComparator) obj).isReverseOrder);
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
