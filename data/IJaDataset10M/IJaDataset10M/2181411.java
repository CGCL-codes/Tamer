package org.unitils.util;

/**
 * Class offering utilities involving the call stack
 *
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class CallStackUtils {

    /**
     * @param invokedClass Class to which an invocation can be found in the current call stack
     * @return Stacktrace that indicates the most recent method call in the stack that calls a method from the given class
     */
    public static StackTraceElement[] getInvocationStackTrace(Class<?> invokedClass) {
        StackTraceElement[] currentStackTrace = Thread.currentThread().getStackTrace();
        for (int i = currentStackTrace.length - 1; i >= 0; i--) {
            if (invokedClass.getName().equals(currentStackTrace[i].getClassName())) {
                int invokedAtIndex = i + 1;
                StackTraceElement[] result = new StackTraceElement[currentStackTrace.length - invokedAtIndex];
                System.arraycopy(currentStackTrace, invokedAtIndex, result, 0, currentStackTrace.length - invokedAtIndex);
                return result;
            }
        }
        throw new IllegalArgumentException("Invoked class " + invokedClass.getName() + " not found in stacktrace");
    }
}
