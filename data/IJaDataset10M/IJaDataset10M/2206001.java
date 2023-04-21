package com.bluemarsh.jswat.breakpoint.ui;

import com.bluemarsh.jswat.breakpoint.Condition;

/**
 * Interface ConditionUI defines the methods necessary for a condition
 * UI adapter implementation.
 *
 * @author  Nathan Fiedler
 */
public interface ConditionUI extends UIAdapter {

    /**
     * Generates a string descriptor of this condition.
     *
     * @return  description.
     */
    public String descriptor();

    /**
     * Returns the Condition object this ui adapter represents.
     *
     * @return  Condition object.
     */
    public Condition getCondition();
}
