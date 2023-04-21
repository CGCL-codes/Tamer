package net.grinder.scriptengine.jython.instrumentation.traditional;

import net.grinder.common.Test;
import org.python.core.PyObject;

/**
 * An instrumented <code>PyJavaInstance</code> that is used to wrap Java
 * instances.
 *
 * <p>
 * Plain InstrumentedPyJavaInstanceForPyMethodsAndPyFunctions's work for Jython
 * 2.1. However, in Jython 2.2 the invoke methods use {@link #__findattr__} to
 * determine which method to call, which bypassed our instrumentation. This
 * special version ensures that the methods we hand back are also instrumented
 * correctly.
 * </p>
 *
 * @author Philip Aston
 */
final class InstrumentedPyJavaInstanceForJavaInstances extends AbstractInstrumentedPyJavaInstance {

    public InstrumentedPyJavaInstanceForJavaInstances(Test test, Object target, PyDispatcher dispatcher) {
        super(test, target, dispatcher);
    }

    public PyObject __findattr__(String name) {
        return getInstrumentationHelper().findAttrInstrumentingMethods(name);
    }

    public PyObject invoke(final String name) {
        return getInstrumentationHelper().dispatch(new PyDispatcher.Callable() {

            public PyObject call() {
                return InstrumentedPyJavaInstanceForJavaInstances.super.invoke(name);
            }
        });
    }

    public PyObject invoke(final String name, final PyObject arg1) {
        return getInstrumentationHelper().dispatch(new PyDispatcher.Callable() {

            public PyObject call() {
                return InstrumentedPyJavaInstanceForJavaInstances.super.invoke(name, arg1);
            }
        });
    }

    public PyObject invoke(final String name, final PyObject arg1, final PyObject arg2) {
        return getInstrumentationHelper().dispatch(new PyDispatcher.Callable() {

            public PyObject call() {
                return InstrumentedPyJavaInstanceForJavaInstances.super.invoke(name, arg1, arg2);
            }
        });
    }
}
