package org.openscience.cdk.qsar.result;

import java.io.Serializable;

/**
 * Object that provides access to the calculated descriptor value.
 * The concept was taken from JOELib.
 *
 * @cdk.module standard
 * @cdk.githash
 */
public interface IDescriptorResult extends Serializable {

    /**
     * String representation of the result.
     *
     * @return A string representation.
     */
    public String toString();

    /**
     * Returns the length of this descriptor.
     * 
     * @return the length of the array of return values.
     */
    public int length();
}
