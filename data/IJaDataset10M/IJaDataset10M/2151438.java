package org.unitils.mock.mockbehavior.impl;

import org.unitils.core.UnitilsException;
import org.unitils.mock.Mock;
import org.unitils.mock.core.proxy.ProxyInvocation;
import static org.unitils.mock.core.proxy.StackTraceUtils.getInvocationStackTrace;
import org.unitils.mock.mockbehavior.ValidatableMockBehavior;
import java.util.Arrays;

/**
 * Mock behavior that throws a given exception.
 *
 * @author Filip Neven
 * @author Tim Ducheyne
 * @author Kenny Claes
 */
public class ExceptionThrowingMockBehavior implements ValidatableMockBehavior {

    private Throwable exceptionToThrow;

    /**
     * Creates the throwing behavior for the given exception.
     *
     * @param exceptionToThrow The exception, not null
     */
    public ExceptionThrowingMockBehavior(Throwable exceptionToThrow) {
        this.exceptionToThrow = exceptionToThrow;
    }

    /**
     * Checks whether the mock behavior can be executed for the given invocation.
     * An exception is raised if the method is a void method or has a non-assignable return type.
     *
     * @param proxyInvocation The proxy method invocation, not null
     */
    public void assertCanExecute(ProxyInvocation proxyInvocation) throws UnitilsException {
        if (exceptionToThrow instanceof RuntimeException || exceptionToThrow instanceof Error) {
            return;
        }
        Class<?>[] exceptionTypes = proxyInvocation.getMethod().getExceptionTypes();
        for (Class<?> exceptionType : exceptionTypes) {
            if (exceptionType.isAssignableFrom(exceptionToThrow.getClass())) {
                return;
            }
        }
        throw new UnitilsException("Trying to make a method throw an exception that it doesn't declare. Exception type: " + exceptionToThrow.getClass() + (exceptionTypes.length > 0 ? ", declared exceptions: " + Arrays.toString(exceptionTypes) : ", no declared exceptions"));
    }

    /**
     * Executes the mock behavior.
     *
     * @param proxyInvocation The proxy method invocation, not null
     * @return Nothing
     */
    public Object execute(ProxyInvocation proxyInvocation) throws Throwable {
        exceptionToThrow.setStackTrace(getInvocationStackTrace(Mock.class, false));
        throw exceptionToThrow;
    }
}
