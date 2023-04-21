package com.barbarianprince.bus;

import java.util.Comparator;

/**
 * Class for sorting <code>PartyMember</code>s.
 * @author <i>DaLoneDrau</i>
 *
 */
public class PartyComparator implements Comparator<PartyMember> {

    /** the types of SkillComparators. */
    public enum PartyComparatorType {

        /** compares by skill level, lowest to highest. */
        ASC, /** compares by skill level, highest to lowest. */
        DESC
    }

    /** the type. */
    private PartyComparatorType type;

    /**
	 * Constructor.
	 */
    public PartyComparator() {
        this(PartyComparatorType.ASC);
    }

    /**
	 * Constructor.
	 * @param scType the type of comparator.
	 */
    public PartyComparator(final PartyComparatorType scType) {
        type = scType;
    }

    /**
	 * Compares two <code>PartyMember</code>s for order.
	 * @param member1 the first <code>PartyMember</code>
	 * @param member2 the second <code>PartyMember</code>
	 * @return a negative integer, zero, or a positive integer 
	 * as the first argument is less than, equal to, or greater 
	 * than the second.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
    public final int compare(final PartyMember member1, final PartyMember member2) {
        int val = 0;
        int compareVal = 0;
        if (member1 instanceof Prince) {
            compareVal = -1;
        } else if (member2 instanceof Prince) {
            compareVal = 1;
        } else {
            compareVal = member1.getName().compareToIgnoreCase(member2.getName());
        }
        switch(type) {
            case DESC:
                val = -compareVal;
                break;
            default:
                val = compareVal;
        }
        return val;
    }
}
