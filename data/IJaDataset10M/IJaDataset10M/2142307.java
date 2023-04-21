package org.quantumleaphealth.model.trial;

import java.io.Serializable;

/**
 * Allowed to match to a patient's health characteristics.
 * 
 * @author Tom Bechtold
 * @version 2008-02-19
 */
public interface Criterion extends Serializable, Cloneable {

    /**
     * Returns the first abstract criterion or <code>null</code> if there is no first criterion.
     * @return the first abstract criterion or <code>null</code> if there is no first criterion
     */
    public AbstractCriterion getFirstCriterion();

    /**
     * Inverts the logic.
     */
    public void invert();

    /**
     * Clones the object.
     * @throws CloneNotSupportedException if the class does not support cloning
     */
    public Object clone() throws CloneNotSupportedException;
}
