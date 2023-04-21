package net.assimilator.springcontainer.jericonnector;

import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.RemoteProxyFailureException;
import org.aopalliance.intercept.MethodInvocation;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.rmi.*;
import java.util.Arrays;

/**
 * Factored-out methods for working with JERI client remoting code.
 * <p/>
 * <p>Note: This is an SPI class, not intended to be used by applications.
 *
 * @author Larry Mitchell
 * @author Kevin Hartig
 * @version $Id$
 */
public class JeriClientInterceptorUtils {

    /**
     * Perform a raw method invocation on the given RMI stub,
     * letting reflection exceptions through as-is.
     * @param invocation the AOP MethodInvocation
     * @param stub the RMI stub
     * @return the invocation result, if any
     * @throws InvocationTargetException if thrown by reflection
     */
    public static Object doInvoke(MethodInvocation invocation, Remote stub) throws InvocationTargetException {
        Method method = invocation.getMethod();
        try {
            if (method.getDeclaringClass().isInstance(stub)) {
                return method.invoke(stub, invocation.getArguments());
            } else {
                Method stubMethod = stub.getClass().getMethod(method.getName(), method.getParameterTypes());
                return stubMethod.invoke(stub, invocation.getArguments());
            }
        } catch (InvocationTargetException ex) {
            throw ex;
        } catch (NoSuchMethodException ex) {
            throw new RemoteProxyFailureException("No matching RMI stub method found for: " + method, ex);
        } catch (Throwable ex) {
            throw new RemoteProxyFailureException("Invocation of RMI stub method failed: " + method, ex);
        }
    }

    /**
     * Determine whether the given Jeri exception indicates a connect failure.
     * Treats ConnectException, ConnectIOException, UnknownHostException,
     * NoSuchObjectException, StubNotFoundException, MarshalException and
     * UnmarshalException as connect failure.
     *
     * @param ex the RMI exception to check
     * @return whether the exception should be treated as connect failure
     * @see java.rmi.ConnectException
     * @see java.rmi.ConnectIOException
     * @see java.rmi.UnknownHostException
     * @see java.rmi.NoSuchObjectException
     * @see java.rmi.StubNotFoundException
     * @see java.rmi.MarshalException
     * @see java.rmi.UnmarshalException
     */
    public static boolean isConnectFailure(RemoteException ex) {
        return (ex instanceof ConnectException || ex instanceof ConnectIOException || ex instanceof UnknownHostException || ex instanceof NoSuchObjectException || ex instanceof StubNotFoundException || ex instanceof MarshalException || ex instanceof UnmarshalException);
    }

    /**
     * Wrap the given arbitrary exception that happened during remote access
     * in either a RemoteException or a Spring RemoteAccessException (if the
     * method signature does not support RemoteException).
     * <p>Only call this for remote access exceptions, not for exceptions
     * thrown by the target service itself!
     *
     * @param method  the invoked method
     * @param ex      the exception that happened, to be used as cause for the
     *                RemoteAccessException or RemoteException
     * @param message the message for the RemoteAccessException respectively
     *                RemoteException
     * @return the exception to be thrown to the caller
     */
    public static Exception convertJeriAccessException(Method method, Throwable ex, String message) {
        if (!Arrays.asList(method.getExceptionTypes()).contains(RemoteException.class)) {
            return new RemoteAccessException(message, ex);
        } else {
            return new RemoteException(message, ex);
        }
    }

    /**
     * Convert the given RemoteException that happened during remote access
     * to Spring's RemoteAccessException if the method signature does not
     * support RemoteException. Else, return the original RemoteException.
     *
     * @param method      the invoked method
     * @param ex          the RemoteException that happened
     * @param serviceName the name of the service (for debugging purposes)
     * @return the exception to be thrown to the caller
     */
    public static Exception convertJeriAccessException(Method method, RemoteException ex, String serviceName) {
        return convertJeriAccessException(method, ex, isConnectFailure(ex), serviceName);
    }

    /**
     * Convert the given RemoteException that happened during remote access
     * to Spring's RemoteAccessException if the method signature does not
     * support RemoteException. Else, return the original RemoteException.
     *
     * @param method           the invoked method
     * @param ex               the RemoteException that happened
     * @param isConnectFailure whether the given exception should be considered
     *                         a connect failure
     * @param serviceName      the name of the service (for debugging purposes)
     * @return the exception to be thrown to the caller
     */
    public static Exception convertJeriAccessException(Method method, RemoteException ex, boolean isConnectFailure, String serviceName) {
        if (!Arrays.asList(method.getExceptionTypes()).contains(RemoteException.class)) {
            if (isConnectFailure) {
                return new RemoteConnectFailureException("Cannot connect to remote service [" + serviceName + "]", ex);
            } else {
                return new RemoteAccessException("Cannot access remote service [" + serviceName + "]", ex);
            }
        } else {
            return ex;
        }
    }
}
