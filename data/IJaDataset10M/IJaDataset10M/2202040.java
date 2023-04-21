package netgest.bo.xwc.framework.components;

/**
 * <p>Base class for classes that wrap a <code>MethodBinding</code> and
 * implement a faces listener-like interface.</p>
 *
 */
abstract class XUIMethodBindingAdapterBase extends Object {

    /**
     * <p>Recursively interrogate the <code>cause</code> property of the
     * argument <code>exception</code> and stop recursing either when it
     * is an instance of <code>expectedExceptionClass</code> or
     * <code>null</code>.  Return the result.</p>
     */
    public Throwable getExpectedCause(Class expectedExceptionClass, Throwable exception) {
        Throwable result = exception.getCause();
        if (null != result) {
            if (!result.getClass().isAssignableFrom(expectedExceptionClass)) {
                result = getExpectedCause(expectedExceptionClass, result);
            }
        }
        return result;
    }
}
