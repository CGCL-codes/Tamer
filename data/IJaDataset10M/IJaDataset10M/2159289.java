package org.t2framework.confeito.exception;

import org.t2framework.confeito.Constants;

/**
 * 
 * <#if locale="en">
 * <p>
 * Exception throws when
 * {@link org.t2framework.confeito.handler.ExceptionHandlerChain} handles same
 * exceptions cyclically.
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 * @see org.t2framework.confeito.handler.ExceptionHandlerChain
 */
public class ExceptionHandlerCyclicRuntimeException extends T2InternalRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
	 * <#if locale="en">
	 * <p>
	 * TODO error message.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param cause
	 */
    public ExceptionHandlerCyclicRuntimeException(Throwable cause) {
        super(cause, "ETDA0001", Constants.EMPTY_ARRAY);
    }
}
