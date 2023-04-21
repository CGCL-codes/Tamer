package com.rapidminer.example.set;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;

/**
 * This subclass of {@link Condition} serves to excludes all examples containing
 * missing values from an example set.
 * 
 * @author Timm Euler, Ingo Mierswa
 *          ingomierswa Exp $
 */
public class NoMissingAttributesCondition implements Condition {

    private static final long serialVersionUID = 631871757551493977L;

    /**
	 * Throws an exception since this condition does not support parameter
	 * string.
	 */
    public NoMissingAttributesCondition(ExampleSet exampleSet, String parameterString) {
    }

    /**
	 * Since the condition cannot be altered after creation we can just return
	 * the condition object itself.
	 * 
	 * @deprecated Conditions should not be able to be changed dynamically and hence there is no need for a copy
	 */
    @Deprecated
    public Condition duplicate() {
        return this;
    }

    /** Returns true if the example does not contain missing values. */
    public boolean conditionOk(Example example) {
        for (Attribute attribute : example.getAttributes()) {
            if (Double.isNaN(example.getValue(attribute))) {
                return false;
            }
        }
        return true;
    }
}
