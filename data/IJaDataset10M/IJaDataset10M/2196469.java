package com.mattharrah.gedcom4j.model;

/**
 * A class for an individual attribute. Corresponds to
 * INDIVIDUAL_ATTRIBUTE_STRUCTURE in the GEDCOM standard
 * 
 * @author frizbog1
 * 
 */
public class IndividualAttribute extends Event {

    /**
     * The type of attribute
     */
    public IndividualAttributeType type;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof IndividualAttribute)) {
            return false;
        }
        IndividualAttribute other = (IndividualAttribute) obj;
        if (type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }
}
