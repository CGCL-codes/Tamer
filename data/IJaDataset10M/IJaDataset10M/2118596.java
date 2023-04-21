package com.eptica.ias.models.accounts.accountgroupname.comparator;

import java.io.*;
import java.util.Comparator;
import com.eptica.ias.models.accounts.accountgroupname.*;

/**
 * A comparator that compares AccountGroupNameModel based on their locale field.<br/>
 * Note: this comparator imposes orderings that are inconsistent with equals.
 */
public class AccountGroupNameByLocaleComparator<T extends AccountGroupNameModel> implements Serializable, Comparator<AccountGroupNameModel> {

    private boolean isReverseOrder = false;

    /**
     * Constructs a new AccountGroupNameByLocaleComparator that relies on the 
     * String natural ordering.
     */
    public AccountGroupNameByLocaleComparator() {
        isReverseOrder = false;
    }

    /**
     * Constructs a new AccountGroupNameByLocaleComparator that relies on the 
     * String ordering.
     * @param isReverseOrder true to obtain a reverse order, false otherwise.
     */
    public AccountGroupNameByLocaleComparator(boolean isReverseOrder) {
        this.isReverseOrder = isReverseOrder;
    }

    /**
     * Compare two AccountGroupNameModel by comparing their locale field.
     * @param firstItem a accountGroupName
     * @param secondItem another accountGroupName
     */
    public int compare(AccountGroupNameModel firstItem, AccountGroupNameModel secondItem) {
        int result = 0;
        String locale1 = firstItem.getLocale();
        String locale2 = secondItem.getLocale();
        if (locale1 == null && locale2 == null) {
            result = 0;
        } else if (locale1 == null && locale2 != null) {
            result = 1;
        } else if (locale1 != null && locale2 == null) {
            result = -1;
        } else {
            result = locale1.compareTo(locale2);
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
        if (!(obj instanceof AccountGroupNameByLocaleComparator)) {
            return false;
        }
        return (this.isReverseOrder == ((AccountGroupNameByLocaleComparator) obj).isReverseOrder);
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
