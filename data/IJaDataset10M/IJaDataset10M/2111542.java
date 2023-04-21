package net.grinder.engine.process.instrumenter.traditionaljython;

import net.grinder.common.Test;
import org.python.core.PyFunction;
import org.python.core.PyObject;

/**
 * An instrumented <code>PyJavaInstance</code>, used to wrap PyFunctions.
 *
 * @author Philip Aston
 * @version $Revision: 4073 $
 */
class InstrumentedPyJavaInstanceForPyFunctions extends AbstractInstrumentedPyJavaInstance {

    private final PyFunction m_pyFunction;

    public InstrumentedPyJavaInstanceForPyFunctions(Test test, PyFunction pyFunction, PyDispatcher dispatcher) {
        super(test, pyFunction, dispatcher);
        m_pyFunction = pyFunction;
    }

    public final PyObject invoke(final String name) {
        if (name == InstrumentationHelper.TARGET_FIELD_NAME) {
            return m_pyFunction.__call__();
        }
        return getInstrumentationHelper().dispatch(new PyDispatcher.Callable() {

            public PyObject call() {
                return InstrumentedPyJavaInstanceForPyFunctions.super.invoke(name);
            }
        });
    }

    public final PyObject invoke(final String name, final PyObject arg1) {
        if (name == InstrumentationHelper.TARGET_FIELD_NAME) {
            return m_pyFunction.__call__(arg1);
        }
        return getInstrumentationHelper().dispatch(new PyDispatcher.Callable() {

            public PyObject call() {
                return InstrumentedPyJavaInstanceForPyFunctions.super.invoke(name, arg1);
            }
        });
    }

    public final PyObject invoke(final String name, final PyObject arg1, final PyObject arg2) {
        if (name == InstrumentationHelper.TARGET_FIELD_NAME) {
            return m_pyFunction.__call__(arg1, arg2);
        }
        return getInstrumentationHelper().dispatch(new PyDispatcher.Callable() {

            public PyObject call() {
                return InstrumentedPyJavaInstanceForPyFunctions.super.invoke(name, arg1, arg2);
            }
        });
    }

    public final PyObject invoke(final String name, final PyObject[] args) {
        if (name == InstrumentationHelper.TARGET_FIELD_NAME) {
            return m_pyFunction.__call__(args);
        }
        return getInstrumentationHelper().dispatch(new PyDispatcher.Callable() {

            public PyObject call() {
                return InstrumentedPyJavaInstanceForPyFunctions.super.invoke(name, args);
            }
        });
    }

    public final PyObject invoke(final String name, final PyObject[] args, final String[] keywords) {
        return getInstrumentationHelper().dispatch(new PyDispatcher.Callable() {

            public PyObject call() {
                return InstrumentedPyJavaInstanceForPyFunctions.super.invoke(name, args, keywords);
            }
        });
    }
}
