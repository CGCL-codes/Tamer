package org.personalsmartspace.lm.api;

import java.io.Serializable;
import java.util.Collection;

/**
 * This is the interface that rules that are created by 
 * {@link ILearner}s must implement.
 * @author Guido Spadotto
 * @version 1.0
 */
public interface IRule extends Serializable {

    /**
     * Returns the collection of attribute types (i.e. attribute names) that are
     * affected by this rule. These attribute types will have to be
     * stored in RMs internal DB for later reference. 
     */
    public Collection<String> getOutputTypes();

    /**
     * Returns the collection of attribute types (i.e. attribute names) that 
     * this rule relies on for its evaluation.
     */
    public Collection<String> getInputTypes();
}
