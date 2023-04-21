package org.geonetwork.domain.ebrim.informationmodel.core.datatype;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * 
 * @author Heikki Doeleman
 * 
 */
@Indexed
public class String16 {

    @Field
    private String value;

    public String16() {
    }

    /**
	 * For Jixb binding.
	 * 
	 * @param string
	 */
    public String16(String string) {
        this.value = string;
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
        String16 other = (String16) obj;
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
