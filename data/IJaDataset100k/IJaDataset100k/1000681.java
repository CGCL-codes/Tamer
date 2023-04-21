package org.geonetwork.domain.ebrim.informationmodel.core.datatype;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * 
 * @author Heikki Doeleman
 * 
 */
@Indexed
public class ShortName {

    @Field
    private String value;

    public ShortName() {
    }

    /**
	 * For Jixb binding.
	 * 
	 * @param shortName
	 */
    public ShortName(String shortName) {
        this.value = shortName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ShortName other = (ShortName) obj;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
