package com.schwidder.fun.objects.interfaces;

import com.schwidder.nucleus.exception.NucleusException;

/**
 * IMethodCall defines the interface to call a wrapper method to call
 * class-methods.
 * 
 * @author kai@schwidder.com
 * @version 1.0
 */
public interface IMethodCall {

    /**
	 * A wrapper is triggered by the call-method.
	 * 
	 * @param aObj
	 *            the transition that initiates the method call
	 */
    public void call(IFTransition aObj) throws NucleusException;
}
