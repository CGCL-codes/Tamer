package cheesymock.recorder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import cheesymock.Cheesy;
import cheesymock.Mock;

/**
 * Creates all error messages for the Recorder functionality
 *
 * @author Bjorn
 */
public class RecorderErrorMessageFactory {

    private String nl = System.getProperty("line.separator");

    public String createArgumentModifierIncorrectClass(Queue<MethodInvocation> correctInvocationsQueue, MethodInvocation expectedMethodInvocation, MethodInvocation currentMethodInvocation, int argumentIndex) {
        StringBuffer returnValue = new StringBuffer();
        String argumentString = getArgumentsString(expectedMethodInvocation, argumentIndex);
        returnValue.append(String.format("ArgumentComparator<???> in argument nr %s has wrong comparer class specified, use ArgumentComparator<%s> instead:", argumentString, expectedMethodInvocation.getMethod().getParameterTypes()[argumentIndex].getSimpleName())).append(nl);
        returnValue.append("  Expected: ");
        printMethod(expectedMethodInvocation, returnValue);
        returnValue.append(nl);
        returnValue.append("  Actual  : ");
        printMethod(currentMethodInvocation, returnValue);
        addPreviousMethods(correctInvocationsQueue, returnValue);
        return returnValue.toString();
    }

    public String createMethodArgumentMissmatch(Queue<MethodInvocation> correctInvocationsQueue, MethodInvocation expectedMethodInvocation, MethodInvocation currentMethodInvocation, Set<Integer> argumentMissmatcheIndexes) {
        StringBuffer returnValue = new StringBuffer();
        String argumentString = getArgumentsString(expectedMethodInvocation, argumentMissmatcheIndexes);
        final boolean oneArgumentMissmatch = argumentMissmatcheIndexes.size() == 1;
        returnValue.append(String.format("Method argument%s nr %s %s not equal:", oneArgumentMissmatch ? "" : "s", argumentString, oneArgumentMissmatch ? "was" : "where")).append(nl);
        returnValue.append("  Expected: ");
        printMethod(expectedMethodInvocation, returnValue);
        returnValue.append(nl);
        returnValue.append("  Actual  : ");
        printMethod(currentMethodInvocation, returnValue);
        addPreviousMethods(correctInvocationsQueue, returnValue);
        return returnValue.toString();
    }

    public String createMethodMissmatchMessage(Queue<MethodInvocation> correctInvocationsQueue, MethodInvocation expectedMethodInvocation, MethodInvocation currentMethodInvocation) {
        StringBuffer returnValue = new StringBuffer();
        returnValue.append("Did not receive expected method invocation:").append(nl);
        returnValue.append("  Expected: ");
        printMethod(expectedMethodInvocation, returnValue);
        returnValue.append(nl);
        returnValue.append("  Actual  : ");
        printMethod(currentMethodInvocation, returnValue);
        addPreviousMethods(correctInvocationsQueue, returnValue);
        return returnValue.toString();
    }

    public String createNoExpectationMessage(Queue<MethodInvocation> correctInvocationsQueue, MethodInvocation currentMethodInvocation) {
        StringBuffer returnValue = new StringBuffer();
        returnValue.append("No method invocation expected by Recorder:").append(nl);
        returnValue.append("  Actual invocation: ");
        printMethod(currentMethodInvocation, returnValue);
        addPreviousMethods(correctInvocationsQueue, returnValue);
        return returnValue.toString();
    }

    public String createNotSameProxyMessage(Queue<MethodInvocation> correctInvocationsQueue, MethodInvocation expectedMethodInvocation, MethodInvocation currentMethodInvocation) {
        final Mock originalMock = (Mock) Cheesy.getProxyFactory().getHandler(expectedMethodInvocation.getOriginalProxy());
        final RecorderMock originalRecorder = originalMock.getRecorder();
        originalMock.setRecorder(null);
        final Mock currentMock = (Mock) Cheesy.getProxyFactory().getHandler(currentMethodInvocation.getOriginalProxy());
        final RecorderMock currentRecorder = currentMock.getRecorder();
        currentMock.setRecorder(null);
        StringBuffer returnValue = new StringBuffer();
        returnValue.append(String.format("The method was invoked on a different instance of the proxy class: %s", expectedMethodInvocation.getOriginalProxy().getClass().getSimpleName())).append(nl);
        returnValue.append("  Actual invocation: ");
        printMethod(currentMethodInvocation, returnValue);
        addPreviousMethods(correctInvocationsQueue, returnValue);
        originalMock.setRecorder(originalRecorder);
        currentMock.setRecorder(currentRecorder);
        return returnValue.toString();
    }

    public String createUninvokedExpectationsMessage(Queue<MethodInvocation> correctInvocationsQueue, Queue<MethodInvocation> queue) {
        StringBuffer returnValue = new StringBuffer();
        returnValue.append("Did not invoke expected methods:");
        for (MethodInvocation methodInvocation : queue) {
            returnValue.append(nl).append("  ");
            printMethod(methodInvocation, returnValue);
        }
        addPreviousMethods(correctInvocationsQueue, returnValue);
        return returnValue.toString();
    }

    public String getDefaultToString(Object object) {
        String string = object.toString();
        if (string != null) return string;
        return object.getClass().getSimpleName() + "@" + Integer.toHexString(object.hashCode());
    }

    private void addPreviousMethods(Queue<MethodInvocation> correctInvocationsQueue, StringBuffer returnValue) {
        if (!correctInvocationsQueue.isEmpty()) {
            returnValue.append(nl).append("The following correct method calls have been invoked before failure:");
            for (MethodInvocation methodInvocation : correctInvocationsQueue) {
                returnValue.append(nl).append("  ");
                printMethod(methodInvocation, returnValue);
            }
        }
    }

    private String getArgumentsString(MethodInvocation expectedMethodInvocation, int argumentIndex) {
        Set<Integer> argumentIndexes = new TreeSet<Integer>();
        argumentIndexes.add(argumentIndex);
        return getArgumentsString(expectedMethodInvocation, argumentIndexes);
    }

    private String getArgumentsString(MethodInvocation expectedMethodInvocation, Set<Integer> argumentIndexes) {
        StringBuffer argumentString = new StringBuffer();
        final Class<?>[] parameterTypes = expectedMethodInvocation.getMethod().getParameterTypes();
        boolean first = true;
        for (Integer integer : argumentIndexes) {
            if (first) {
                first = false;
            } else {
                argumentString.append(", ");
            }
            argumentString.append(integer + 1).append(" (").append(parameterTypes[integer].getSimpleName()).append(")");
        }
        return argumentString.toString();
    }

    private String getParametersString(MethodInvocation methodInvocation, final Method method) {
        final StringBuffer returnValue = new StringBuffer();
        final Object[] args = methodInvocation.getArgs();
        final Class<?>[] parameterTypes = method.getParameterTypes();
        boolean first = true;
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (first) {
                first = false;
            } else {
                returnValue.append(", ");
            }
            returnValue.append('(').append(parameterTypes[i].getSimpleName()).append(')').append(" ").append(arg);
        }
        return returnValue.toString();
    }

    private void printMethod(MethodInvocation methodInvocation, StringBuffer returnValue) {
        final Method method = methodInvocation.getMethod();
        final int modifiers = method.getModifiers();
        returnValue.append(String.format("%s %s %s.%s(%s)", Modifier.toString(modifiers), method.getReturnType().getSimpleName(), method.getDeclaringClass().getSimpleName(), method.getName(), getParametersString(methodInvocation, method)));
    }
}
