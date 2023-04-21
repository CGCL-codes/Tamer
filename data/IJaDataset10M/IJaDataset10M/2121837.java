package net.sf.staccatocommons.io.internal.lifecycle;

import java.util.concurrent.Callable;
import net.sf.staccatocommons.defs.Thunk;
import net.sf.staccatocommons.io.internal.Handle;
import net.sf.staccatocommons.lang.SoftException;
import net.sf.staccatocommons.restrictions.check.NonNull;

/**
 * 
 * A {@link Lifecycle} is a logic of initialization, use and dispose of a
 * resource , that can be executed as a single unit of work. It can be executed
 * in different fashions - using {@link #value()}, {@link #call()} ,
 * {@link #run()} or {@link #value()}
 * 
 * Lifecycles are abstract, so in order to use them, client code must implement
 * the following methods
 * <ul>
 * <li>{@link #initialize()}: this method will be invoked in order to create an
 * initialize the handled resource. It <strong>may</strong> throw any exception.
 * Typical example of such initialization is instantiating the resource or
 * sending an <code>init</code>message.</li>
 * <li>
 * <p>
 * {@link #doWork(Object)}: this method uses the resource. It will be invoked to
 * produce the result of the lifecycle, passing the handled resource returned by
 * the {@link #initialize()} method. It <strong>may</strong> throw any
 * exception.
 * </p>
 * <p>
 * Lifecycles parameterized to have a result of type {@link Void}
 * <strong>must</strong> return <code>null</code>; for those cases, client code,
 * instead of overriding {@link #doWork(Object)}, <strong>should</strong>
 * override {@link #doVoidWork(Object)}
 * </p>
 * </li>
 * <li>
 * <p>
 * {@link #dispose(Object)}: this method release the handled resource, freeing
 * any system resources associated to it. It <strong>may</strong> throw any
 * exception. Typical example of such disposal is sending a <code>close</code>
 * message.
 * </p>
 * <p>
 * This method will be invoked even if the usage of the resource - that is
 * {@link #doWork(Object)} or {@link #doVoidWork(Object)} execution - failed
 * throwin and exception.
 * </p>
 * </li>
 * </ul>
 * 
 * @author fbulgarelli
 * 
 * @param <ResourceType>
 *          the type of resource handled by this lifecycle
 * @param <ResultType>
 *          the type of result returned by this lifecycle
 */
public abstract class Lifecycle<ResourceType, ResultType> implements Thunk<ResultType>, Callable<ResultType> {

    /**
   * Executes this {@link Lifecycle}, initializing the resource it handles,
   * doing some work with it, disposing the resource and returning the work
   * result.
   * 
   * Any checked or unchecked exception produced during the flow execution will
   * be catch, soften and rethrown.
   * 
   * 
   * @throws RuntimeException
   *           if any exception is thrown during the flow execution
   * @return the result of the work over the resource
   * @see SoftException#soften(Exception)
   */
    public ResultType call() throws Exception {
        ResourceType resource = null;
        try {
            resource = initialize();
            return doWork(resource);
        } finally {
            if (resource != null) dispose(resource);
        }
    }

    /**
   * Sends the {@link #call()} message, softening any exception that may occur.
   * 
   * @see SoftException#callOrSoften(Callable)
   */
    @Override
    public ResultType value() {
        return SoftException.callOrSoften(this);
    }

    /**
   * Handles exceptions of type <code>exceptionClass</code> that may occur when
   * sending {@link #call()}.
   * 
   * This method is just a shortcut for
   * <code>Handle.throwing(this, exceptionClass)</code>
   * 
   * @see Handle#throwing(Callable, Class)
   */
    public final <E extends Exception> ResultType throwing(Class<E> exceptionClass) throws E {
        return Handle.throwing(this, exceptionClass);
    }

    /**
   * Handles exceptions of type <code>exceptionClass1</code> and
   * <code>exceptionClass2</code> that may occur when sending {@link #call()}.
   * 
   * This method is just a shortcut for
   * <code>Handle.throwing(this, exceptionClass1, exceptionClass2)</code>
   * 
   * @see Handle#throwing(Callable, Class, Class)
   */
    public final <E1 extends Exception, E2 extends Exception> ResultType throwing(Class<E1> exceptionClass1, Class<E2> exceptionClass2) throws E1, E2 {
        return Handle.throwing(this, exceptionClass1, exceptionClass2);
    }

    /**
   * Initializes and gets a resource of ResourceType
   * 
   * @return the initialized resources
   * @throws Exception
   *           if any error occurs
   */
    protected abstract ResourceType initialize() throws Exception;

    /**
   * Makes usage of a resource, and returns a result
   * 
   * {@link Lifecycle}s parameterized to have a {@link Void} result
   * <strong>should not</strong> override this method, but
   * {@link #doVoidWork(Object)} instead
   * 
   * @param resource
   *          the resource to use
   * @return the result of using the resource, of ResultType. It may be null, if
   *         and only if ResultType is {@link Void}
   * @throws Exception
   *           if any error occurs
   */
    protected ResultType doWork(@NonNull ResourceType resource) throws Exception {
        doVoidWork(resource);
        return null;
    }

    /**
   * Makes usage of a resource, without returning a result.
   * 
   * This method <strong>should</strong> only be overriden in {@link Lifecycle}s
   * parameterized to have a {@link Void} result. If it is not the case,
   * override {@link #doWork(Object)} instead.
   * 
   * @param resource
   *          the resource to use
   * @throws Exception
   *           is any error occurs
   */
    protected void doVoidWork(@NonNull ResourceType resource) throws Exception {
    }

    /**
   * Disposes the resource.
   * 
   * Default implementation does nothing, subclasses may want to override this
   * method to add disposal logic
   * 
   * @param resource
   * @throws Exception
   *           if any error occurs
   */
    protected void dispose(@NonNull ResourceType resource) throws Exception {
    }
}
