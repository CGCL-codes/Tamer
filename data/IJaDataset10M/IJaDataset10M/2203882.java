package jexample.internal;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

/**
 * The states, a {@link TestMethod} can have.
 * 
 * @author Lea Haensenberger (lhaensenberger at students.unibe.ch)
 */
enum TestResult {

    NOT_YET_RUN, GREEN, RED, WHITE
}

/**
 * The wrapper for the {@link Method}'s to be run.
 * 
 * @author Lea Haensenberger (lhaensenberger at students.unibe.ch)
 */
public class TestMethod {

    private Method javaMethod;

    private List<TestMethod> dependencies;

    private TestResult state;

    private Object returnValue;

    /**
	 * @param method
	 *            the {@link Method} to be run
	 */
    public TestMethod(Method method) {
        this.javaMethod = method;
        this.dependencies = new ArrayList<TestMethod>();
        this.state = TestResult.NOT_YET_RUN;
    }

    /**
	 * @param testClass
	 *            the {@link TestClass} the method is declared in
	 * @return a {@link List} of {@link Method}'s, which are the dependencies
	 *         of this {@link TestMethod}
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 */
    public List<Method> extractDependencies(TestClass testClass) throws SecurityException, ClassNotFoundException, NoSuchMethodException {
        return testClass.getDependenciesFor(this.javaMethod);
    }

    /**
	 * Checks, if this {@link TestMethod} belongs to <code>testClass</code>
	 * 
	 * @param testClass
	 *            the {@link TestClass} to be compared
	 * @return true, if the {@link TestMethod} belongs to <code>testClass</code>,
	 *         false otherwise
	 */
    public boolean belongsToClass(TestClass testClass) {
        return this.javaMethod.getDeclaringClass().equals(testClass.getJavaClass());
    }

    /**
	 * Runs this {@link TestMethod} after it run all of its dependencies.
	 * 
	 * @param notifier
	 *            the {@link RunNotifier}
	 */
    public void run(RunNotifier notifier) {
        if (this.hasBeenRun()) return;
        boolean allParentsGreen = true;
        for (TestMethod dependency : this.dependencies) {
            dependency.run(notifier);
            allParentsGreen = allParentsGreen && dependency.isGreen();
        }
        if (allParentsGreen && !this.isIgnoredByAnnotation()) {
            this.runTestMethod(notifier);
        } else {
            this.setWhite();
            notifier.fireTestIgnored(this.createDescription());
        }
    }

    public boolean equals(Object obj) {
        return this.javaMethod.equals(((TestMethod) obj).javaMethod);
    }

    /**
	 * If the TestMethod doesn't already have the dependency
	 * <code>testMethod</code>, <code>testMethod</code> is added as a
	 * dependency.
	 * 
	 * @param testMethod
	 *            the {@link TestMethod} to be added as a dependency
	 */
    public void addDependency(TestMethod testMethod) {
        if (!this.dependencies.contains(testMethod)) {
            this.dependencies.add(testMethod);
        }
    }

    /**
	 * @return a {@link List} of {@link TestMethod}'s, being the dependencies
	 */
    public List<TestMethod> getDependencies() {
        return this.dependencies;
    }

    /**
	 * @return the test {@link Description} of this {@link TestMethod}
	 */
    public Description createDescription() {
        return Description.createTestDescription(this.javaMethod.getDeclaringClass(), this.javaMethod.getName());
    }

    /**
	 * @return the declaring {@link Class} of <code>javaMethod</code>
	 */
    public Class<?> getDeclaringClass() {
        return this.javaMethod.getDeclaringClass();
    }

    private void runTestMethod(RunNotifier notifier) {
        Description description = this.createDescription();
        Object test;
        try {
            test = this.javaMethod.getDeclaringClass().getConstructor().newInstance();
            this.invokeMethod(test, description, notifier, this.getArguments());
        } catch (InvocationTargetException e) {
            notifier.testAborted(description, e.getCause());
            return;
        } catch (Exception e) {
            notifier.testAborted(description, e);
            return;
        }
    }

    private void reRunTestMethod() throws Exception {
        Object test = this.javaMethod.getDeclaringClass().getConstructor().newInstance();
        this.returnValue = this.javaMethod.invoke(test, this.getArguments());
    }

    /**
	 * Collects all the arguments taken by the test method. If
	 * <code>clone</code> is implemented, the arguments are cloned.
	 * 
	 * @param notifier
	 * 
	 * @return an {@link Array} of arguments to be passed to the test method
	 *         when invoking it.
	 * @throws Exception if the return value could not be cloned and the provider method could
	 * not be re-run
	 */
    private Object[] getArguments() throws Exception {
        Class<?>[] paramTypes = this.javaMethod.getParameterTypes();
        Object[] arguments = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            if (this.dependencies.get(i).returnValue != null) {
                if (this.typeIsCloneable(paramTypes[i])) {
                    arguments[i] = this.cloneReturnValue(this.dependencies.get(i).returnValue, paramTypes[i]);
                } else {
                    TestMethod provider = this.dependencies.get(i);
                    provider.reRunTestMethod();
                    arguments[i] = provider.returnValue;
                }
            }
        }
        return arguments;
    }

    private Object cloneReturnValue(Object returnValue, Class<?> clazz) {
        Object cloned = null;
        try {
            Method cloneMethod = clazz.getMethod("clone");
            cloneMethod.setAccessible(true);
            cloned = cloneMethod.invoke(returnValue);
        } catch (Exception e) {
            return returnValue;
        }
        return cloned;
    }

    /**
	 * Checks if <code>clazz</code> or one of its superlcasses implements
	 * {@link Cloneable} and declares a {@link Method} <code>clone()</code>.
	 * 
	 * @param clazz
	 *            the {@link Class} to check, if it is cloneable
	 * @return true, if all this conditions are fulfilled, false otherwise
	 */
    private boolean typeIsCloneable(Class<?> clazz) {
        for (Class<?> iface : clazz.getInterfaces()) {
            if (iface.equals(Cloneable.class)) {
                try {
                    clazz.getMethod("clone");
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
        }
        if (clazz.getSuperclass() != null) {
            return this.typeIsCloneable(clazz.getSuperclass());
        } else {
            return false;
        }
    }

    private void invokeMethod(Object test, Description description, RunNotifier notifier, Object... args) {
        notifier.fireTestStarted(description);
        try {
            this.returnValue = this.javaMethod.invoke(test, args);
            this.setGreen();
        } catch (InvocationTargetException e) {
            Throwable actual = e.getTargetException();
            if (!this.expectsException()) {
                this.addFailure(actual, notifier, description);
            } else if (this.isUnexpectedException(actual)) {
                String message = "Unexpected exception, expected<" + this.getExpectedException().getName() + "> but was<" + actual.getClass().getName() + ">";
                this.addFailure(new Exception(message, actual), notifier, description);
            }
        } catch (Throwable e) {
            this.addFailure(e, notifier, description);
        } finally {
            notifier.fireTestFinished(description);
        }
    }

    private boolean isUnexpectedException(Throwable actual) {
        return this.getExpectedException() != actual.getClass();
    }

    private boolean expectsException() {
        return this.getExpectedException() != null;
    }

    private Class<? extends Throwable> getExpectedException() {
        Test annotation = this.javaMethod.getAnnotation(Test.class);
        if (annotation != null && annotation.expected() != org.junit.Test.None.class) {
            return annotation.expected();
        }
        return null;
    }

    private void addFailure(Throwable e, RunNotifier notifier, Description description) {
        notifier.fireTestFailure(new Failure(description, e));
        this.setFailed();
    }

    private boolean isIgnoredByAnnotation() {
        return this.javaMethod.getAnnotation(Ignore.class) != null;
    }

    private void setGreen() {
        this.state = TestResult.GREEN;
    }

    private void setWhite() {
        this.state = TestResult.WHITE;
    }

    private void setFailed() {
        this.state = TestResult.RED;
    }

    private boolean isGreen() {
        return state == TestResult.GREEN;
    }

    private boolean hasBeenRun() {
        return state != TestResult.NOT_YET_RUN;
    }
}
