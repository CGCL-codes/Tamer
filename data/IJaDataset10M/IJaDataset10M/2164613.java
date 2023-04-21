package org.eiichiro.monophony;

/**
 * {@code EndpointInvocationFailedException} wraps the exception thrown by Web 
 * endpoint invocation.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class EndpointInvocationFailedException extends RuntimeException {

    private static final long serialVersionUID = -476641563705358404L;

    /**
	 * Constructs a new {@code EndpointInvocationFailedException} with the 
	 * specified cause exception.
	 * 
	 * @param cause The cause exception.
	 */
    public EndpointInvocationFailedException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
