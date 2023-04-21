package org.jsecurity.aop;

/**
 * A <tt>MethodInterceptor</tt> intercepts a <tt>MethodInvocation</tt> to perform before or after logic (aka 'advice').
 *
 * <p>JSecurity's implementations of this interface mostly have to deal with ensuring a current Subject has the
 * ability to execute the method before allowing it to continue.
 *
 * @author Les Hazlewood
 * @since 0.2
 */
public interface MethodInterceptor {

    /**
     * Invokes the specified <code>MethodInvocation</code>, allowing implementations to perform pre/post/finally
     * surrounding the actual invocation.
     *
     * @param methodInvocation the <code>MethodInvocation</code> to execute.
     * @return the result of the invocation
     * @throws Throwable if the method invocation throws a Throwable or if an error occurs in pre/post/finally advice.
     */
    Object invoke(MethodInvocation methodInvocation) throws Throwable;
}
