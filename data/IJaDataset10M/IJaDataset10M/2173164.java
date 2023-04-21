package com.hp.hpl.jena.graph.query;

/**
 	A FixedValuator is a Valuator that delivers a constant value
 	(supplied when it is constructed).
 	
 	@author hedgehog
 */
public class FixedValuator implements Valuator {

    private Object value;

    /**
	 	Initialise this FixedValuator with a specific value
	*/
    public FixedValuator(Object value) {
        this.value = value;
    }

    /**
	 	Answer this FixedValuator's value, which must be a Boolean
	 	object, as a <code>boolean</code>. The index values
	 	are irrelevant.
	*/
    public boolean evalBool(IndexValues iv) {
        return ((Boolean) evalObject(iv)).booleanValue();
    }

    /**
	 	Answer this FixedValuator's value, as supplied when it was constructed.
	*/
    public Object evalObject(IndexValues iv) {
        return value;
    }
}
