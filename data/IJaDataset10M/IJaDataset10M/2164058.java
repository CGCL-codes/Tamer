package jaxlib.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import javax.annotation.Nullable;
import jaxlib.lang.Call;
import jaxlib.lang.Objects;
import jaxlib.util.CheckArg;

/**
 * Method invocation wrapper allowed to be executed once only.
 * The {@link #call()} method will throw an {@link IllegalStateException} if it already has been invoked.
 * <p>
 * {@code ExpendableInvocation} instances are thread safe.
 * </p>
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: ExpendableMethodCall.java 3057 2012-02-28 04:31:08Z joerg_wassmer $
 */
public class ExpendableMethodCall<R> extends Object implements Call<R> {

    private static final AtomicReferenceFieldUpdater<ExpendableMethodCall, Method> atomicMethod = AtomicReferenceFieldUpdater.newUpdater(ExpendableMethodCall.class, Method.class, "method");

    private volatile Method method;

    private Object target;

    private Object[] arguments;

    /**
   * @param target
   *  the object to invoke the method on.
   * @param method
   *  the method to invoke.
   * @param arguments
   *  the arguments to be received by the method.
   *
   * @throws IllegalArgumentException
   *  if <i>target</i> is null and the method is not static, or if the method is not public and not accessible.
   *
   * @since JaXLib 1.0
   */
    public ExpendableMethodCall(@Nullable final Object target, final Method method, @Nullable final Object... arguments) {
        super();
        CheckArg.notNull(method, "method");
        final int mod = method.getModifiers();
        if ((target == null) && ((mod & Modifier.STATIC) == 0)) throw CheckArg.exception("null target object specified for non-static method: %s", method);
        if (((mod & Modifier.PUBLIC) == 0) && !method.isAccessible()) throw CheckArg.exception("method is non-public and not accessible: %s", method);
        this.method = method;
        this.target = target;
        this.arguments = ((arguments != null) && (arguments.length == 0)) ? Objects.EMPTY_ARRAY : arguments;
    }

    private Object[] args() {
        final Object[] a = this.arguments;
        this.arguments = null;
        return a;
    }

    private Method method() {
        final Method m = this.method;
        if ((m == null) || !ExpendableMethodCall.atomicMethod.compareAndSet(this, m, null)) throw new IllegalStateException("done");
        return m;
    }

    private Object target() {
        final Object target = this.target;
        this.target = null;
        return target;
    }

    @Override
    public R call() throws Exception {
        try {
            final Method m = method();
            return (R) m.invoke(target(), args());
        } catch (final InvocationTargetException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof Exception) throw (Exception) cause; else if (cause instanceof Error) throw (Error) cause; else throw ex;
        }
    }

    @Override
    public void run() {
        try {
            call();
        } catch (final RuntimeException ex) {
            throw ex;
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
