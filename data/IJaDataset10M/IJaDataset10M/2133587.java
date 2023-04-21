package org.uc3m.verbus.bpa.impl;

import org.uc3m.verbus.bpa.Activity;
import org.uc3m.verbus.bpa.ExpressionBoolean;
import org.uc3m.verbus.bpa.Transition;
import org.uc3m.verbus.bpa.VPostRequirement;
import org.uc3m.verbus.bpa.VProperty;

/**
 * A post-requirement for a transition
 * 
 */
public class VPostRequirementImpl extends BPABaseImpl implements VPostRequirement {

    private ExpressionBoolean expression;

    private Transition transition;

    private Activity activity;

    /**
     * Return the expression (predicate) of this requirement.
     * 
     */
    public ExpressionBoolean getExpression() {
        return expression;
    }

    /**
     *  Set the expression (predicate) of this requirement.
     * 
     */
    public void setExpression(ExpressionBoolean expression) {
        this.expression = expression;
    }

    /**
     * Get the transition to which the requirement applies.
     * 
     */
    public Transition getTransition() {
        return transition;
    }

    /**
     * Set the transition to which the requirement applies.
     *  
     */
    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * @see org.uc3m.verbus.bpa.VProperty#getType()
     */
    public int getType() {
        return VProperty.PREREQ;
    }
}
