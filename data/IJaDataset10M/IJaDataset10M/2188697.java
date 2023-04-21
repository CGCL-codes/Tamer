package com.neoworks.jukex.tracksource.filter;

import com.neoworks.jukex.*;

/**
 * Filter based on equality (LIKE) matching of track Attributes
 *
 * @author Nigel Atkinson <a href="mailto:nigel@neoworks.com">nigel@neoworks.com</a>
 */
public class AttributeEqualityTrackFilter implements AttributeTrackFilter {

    private Attribute attribute;

    private AttributeValue value;

    /**
	 * Public constructor
	 */
    public AttributeEqualityTrackFilter(Attribute a, AttributeValue v) {
        attribute = a;
        value = v;
    }

    /**
	 * Check whether a Track matches this filter
	 *
	 * @param t The Track to check for a match
	 * @return Whether the Track matches
	 */
    public boolean match(Track t) {
        boolean retVal = false;
        AttributeValue trackValue = t.getAttributeValue(attribute);
        if (trackValue != null) {
            switch(attribute.getType()) {
                case Attribute.TYPE_STRING:
                    if (trackValue.getString().toLowerCase().equals(value.getString().toLowerCase())) {
                        retVal = true;
                    }
                    break;
                case Attribute.TYPE_INT:
                    if (trackValue.getInt() == value.getInt()) {
                        retVal = true;
                    }
            }
        }
        return retVal;
    }

    /**
	 * Get the Attribute used for matching
	 *
	 * @return The Attribute
	 */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
	 * Get the AttributeValue to match against
	 *
	 * @return The AttributeValue
	 */
    public AttributeValue getValue() {
        return value;
    }

    /**
	 * Get a description of the comparator being used in this filter
	 *
	 * @return A String description
	 */
    public String getComparatorDescription() {
        return "=";
    }

    /**
	 * Get a String representation of this filter
	 */
    public String toString() {
        if (this.attribute.getType() == Attribute.TYPE_STRING) {
            return (this.attribute.getName() + " " + this.getComparatorDescription() + " " + this.value.getString());
        } else {
            return (this.attribute.getName() + " " + this.getComparatorDescription() + " " + this.value.getInt());
        }
    }
}
