package com.tykhe.systems.snmpoller.template.token;

import java.util.ArrayList;
import com.tykhe.systems.snmpoller.exceptions.InvalidBindingException;
import com.tykhe.systems.snmpoller.template.Mapping;

/**
 * An interface to encapsulate the idea that this token depends on certain
 * unknowns whose values can be bound at runtime.  In other words, the
 * value or operation that this token represents depends on the variable to
 * value mapping found in a VarToValueMapping. 
 *   
 * @author plubans
 * @see Mapping
 */
public interface BindableToken extends Token {

    /**
	 * Bind this Token to the given i=*, j=*, k=*, ... mapping.
	 * @param map The mapping to use for binding
	 * @throws InvalidBindingException In the event that this binding does not uniquely identify 
	 * a meaning for this token.
	 */
    public void setBinding(Mapping map) throws InvalidBindingException;

    /**
	 * Returns the names of the unknowns that this token needs to have bound in order
	 * to uniquely identify a meaning.
	 * @return The list of names of the unknowns that need to appear in any VarToValueMapping
	 * used in a call to setBinding.
	 */
    public ArrayList<String> getUnknownList();
}
