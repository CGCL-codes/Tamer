package com.eptica.ias.models.helpdemand.comparator;

import java.io.*;
import java.util.Comparator;
import com.eptica.ias.models.helpdemand.*;

/**
 * A comparator that compares HelpDemandStateModel based on their nameLocale field.<br/>
 * Note: this comparator imposes orderings that are inconsistent with equals.
 */
public class HelpDemandStateByNameLocaleComparator<T extends HelpDemandStateModel> implements Serializable, Comparator<HelpDemandStateModel> {

    private boolean isReverseOrder = false;

    /**
     * Constructs a new HelpDemandStateByNameLocaleComparator that relies on the 
     * String natural ordering.
     */
    public HelpDemandStateByNameLocaleComparator() {
        isReverseOrder = false;
    }

    /**
     * Constructs a new HelpDemandStateByNameLocaleComparator that relies on the 
     * String ordering.
     * @param isReverseOrder true to obtain a reverse order, false otherwise.
     */
    public HelpDemandStateByNameLocaleComparator(boolean isReverseOrder) {
        this.isReverseOrder = isReverseOrder;
    }

    /**
     * Compare two HelpDemandStateModel by comparing their nameLocale field.
     * @param firstItem a helpDemandState
     * @param secondItem another helpDemandState
     */
    public int compare(HelpDemandStateModel firstItem, HelpDemandStateModel secondItem) {
        int result = 0;
        String nameLocale1 = firstItem.getNameLocale();
        String nameLocale2 = secondItem.getNameLocale();
        if (nameLocale1 == null && nameLocale2 == null) {
            result = 0;
        } else if (nameLocale1 == null && nameLocale2 != null) {
            result = 1;
        } else if (nameLocale1 != null && nameLocale2 == null) {
            result = -1;
        } else {
            result = nameLocale1.compareTo(nameLocale2);
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
        if (!(obj instanceof HelpDemandStateByNameLocaleComparator)) {
            return false;
        }
        return (this.isReverseOrder == ((HelpDemandStateByNameLocaleComparator) obj).isReverseOrder);
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
