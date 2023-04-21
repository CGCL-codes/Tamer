package com.winterwar.base;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import java.io.Serializable;

/**
 * Base class for Model objects.  This is basically for the toString, equals
 * and hashCode methods.
 *
 * @author Matt Raible
 */
public class BaseObject implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7846913764554067380L;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
