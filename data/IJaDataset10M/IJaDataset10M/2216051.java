package org.springframework.remoting.support;

import org.aopalliance.intercept.MethodInvocation;

/**
 * Abstract base class for remote service accessors that are based on
 * serialization of RemoteInvocation objects. Provides a "remoteInvocationFactory"
 * property, with a DefaultRemoteInvocationFactory as default.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see RemoteInvocationFactory
 * @see DefaultRemoteInvocationFactory
 */
public abstract class RemoteInvocationBasedAccessor extends UrlBasedRemoteAccessor {

    private RemoteInvocationFactory remoteInvocationFactory = new DefaultRemoteInvocationFactory();

    /**
	 * Set the RemoteInvocationFactory to use for this accessor.
	 * Default is a DefaultRemoteInvocationFactory.
	 * <p>A custom invocation factory can add further context information
	 * to the invocation, for example user credentials.
	 */
    public void setRemoteInvocationFactory(RemoteInvocationFactory remoteInvocationFactory) {
        this.remoteInvocationFactory = remoteInvocationFactory;
    }

    /**
	 * Return the RemoteInvocationFactory used by this accessor.
	 */
    public RemoteInvocationFactory getRemoteInvocationFactory() {
        return remoteInvocationFactory;
    }

    /**
	 * Create a new RemoteInvocation object for the given AOP method invocation.
	 * The default implementation delegates to the RemoteInvocationFactory.
	 * <p>Can be overridden in subclasses to provide custom RemoteInvocation
	 * subclasses, containing additional invocation parameters like user credentials.
	 * Note that it is preferable to use a custom RemoteInvocationFactory which
	 * is a reusable strategy.
	 * @param methodInvocation the current AOP method invocation
	 * @return the RemoteInvocation object
	 * @see RemoteInvocationFactory#createRemoteInvocation
	 */
    protected RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
        return getRemoteInvocationFactory().createRemoteInvocation(methodInvocation);
    }

    /**
	 * Recreate the invocation result contained in the given RemoteInvocationResult
	 * object. The default implementation calls the default recreate method.
	 * <p>Can be overridden in subclass to provide custom recreation, potentially
	 * processing the returned result object.
	 * @param result the RemoteInvocationResult to recreate
	 * @return a return value if the invocation result is a successful return
	 * @throws Throwable if the invocation result is an exception
	 * @see org.springframework.remoting.support.RemoteInvocationResult#recreate
	 */
    protected Object recreateRemoteInvocationResult(RemoteInvocationResult result) throws Throwable {
        return result.recreate();
    }
}
