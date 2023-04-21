package com.jj.abtesting.process;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents a particular version.
 *
 * <p>
 * The key responsibilities of this class are:
 * <ul>
 * <li> encapsulate a version
 * <li> provide logic to get the version name if it is active.
 * </ul>
 * </p>
 * @author john.p
 *
 */
public final class ABVersion implements ABElement {

    /**
    * version name.
    */
    private String name;

    /**
    * Wegith of this version compared to other versions.
    */
    private int weight;

    /**
    * Represents the number of times this version name was returned when a
    * method called the method {@link "#getVersion" getVersion} on this
    * class. once the value of this variable becomes equal to weight it
    * will be reset to the intial value to start all over again
    */
    private int activeCount;

    /**
    * Making constructor private.
    */
    private ABVersion() {
    }

    /**
    * Initializes an instance of ABVersion and returns it.
    * @param name the version name
    * @param weight the weight of this version
    * @return ABVersion an instance of ABVersion
    */
    public static ABVersion getInstance(String name, int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("weight cannot be less than 0");
        }
        ABVersion version = new ABVersion();
        version.name = name;
        version.weight = weight;
        return version;
    }

    /**
    * Imlementation.
    * @see {@link "ABElement"}.
    */
    public void reset() {
        activeCount = 0;
    }

    /**
    * Returns the version name.
    * @return the version name
    */
    public String getVersion() {
        if (++activeCount <= weight) {
            return name;
        } else {
            return null;
        }
    }

    /**
    * Checks whether the passed in object is equal to this class.
    * @param rhs The object to test the equality against
    * @return true if the objects are equal
    * @see java.lang.Object#equals(Object)
    */
    @Override
    public boolean equals(Object rhs) {
        if (rhs == null || !(rhs instanceof ABVersion)) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, rhs);
    }

    /**
    * Overriding hashCode.
    *
    * @return a hashCode if this class. The dates are compared at the date
    *         level.
    * @see java.lang.Object#hashCode()
    */
    @Override
    public int hashCode() {
        final int nonZeroOdd = 273;
        final int multiplierNonZeroOdd = 245;
        return HashCodeBuilder.reflectionHashCode(nonZeroOdd, multiplierNonZeroOdd, this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
