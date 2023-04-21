package net.sf.twip;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.Callable;
import net.sf.twip.parameterhandler.ParameterHandler;
import net.sf.twip.util.Parameter;
import org.junit.runner.*;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.Statement;

/**
 * "Tests with Properties" allows you to simply add parameters to your JUnit tests. TwiP calls your
 * test with all possible combinations of these parameters... or at least some of them in the case
 * of Integers, etc.
 * 
 * @see "http://twip.sf.net"
 */
public class TwiP extends ParentRunner<Runner> {

    /** Enable the native Java <code>assert</code> keyword for all tests. */
    static {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
    }

    private final List<Runner> children = new ArrayList<Runner>();

    public TwiP(Class<?> klass) throws Throwable {
        super(klass);
        Class<?> cls = getTestClass().getJavaClass();
        final Constructor<?> constructor = getTestClass().getOnlyConstructor();
        List<Parameter> parameters = Parameter.of(constructor);
        if (parameters.size() == 0) {
            addChild(new ClassTwip(cls, new Callable<Object>() {

                @Override
                public Object call() throws Exception {
                    return constructor.newInstance();
                }
            }, null));
        } else if (parameters.size() == 1) {
            Parameter parameter = parameters.get(0);
            ParameterHandler handler = ParameterHandler.of(parameter);
            for (Object parameterValue : handler.getParameterValues()) {
                final Object[] args = { parameterValue };
                addChild(new ClassTwip(cls, new Callable<Object>() {

                    @Override
                    public Object call() throws Exception {
                        return constructor.newInstance(args);
                    }
                }, args));
            }
        } else if (parameters.size() == 2) {
            for (Object arg0 : ParameterHandler.of(parameters.get(0)).getParameterValues()) {
                Parameter parameter = parameters.get(1);
                ParameterHandler handler = ParameterHandler.of(parameter);
                for (Object parameterValue : handler.getParameterValues()) {
                    final Object[] args = { arg0, parameterValue };
                    addChild(new ClassTwip(cls, new Callable<Object>() {

                        @Override
                        public Object call() throws Exception {
                            return constructor.newInstance(args);
                        }
                    }, args));
                }
            }
        } else {
            throw new UnsupportedOperationException("too many constructor parameters (yet)");
        }
    }

    public void addChild(Runner child) {
        children.add(child);
    }

    @Override
    protected Description describeChild(Runner child) {
        return child.getDescription();
    }

    @Override
    protected List<Runner> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    protected Statement classBlock(final RunNotifier notifier) {
        return childrenInvoker(notifier);
    }

    @Override
    protected void runChild(Runner child, RunNotifier notifier) {
        child.run(notifier);
    }

    @Override
    public Description getDescription() {
        if (children.size() == 1) return children.get(0).getDescription();
        Annotation[] annotations = getTestClass().getAnnotations();
        Description description = Description.createSuiteDescription(getName(), annotations);
        for (Runner child : children) description.addChild(describeChild(child));
        return description;
    }
}
