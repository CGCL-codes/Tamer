package net.grinder.engine.process.jython;

import net.grinder.common.Test;
import net.grinder.engine.process.ScriptEngine.Dispatcher;
import net.grinder.engine.process.jython.JythonScriptEngine.PyDispatcher;
import org.python.core.PyFunction;
import org.python.core.PyMethod;
import org.python.core.PyObject;

/**
 * An instrumented <code>PyJavaInstance</code>, used to wrap PyFunctions and
 * PyMethods.
 *
 * @author Philip Aston
 * @version $Revision: 3762 $
 */
class InstrumentedPyJavaInstanceForPyMethodsAndPyFunctions extends AbstractInstrumentedPyJavaInstance {

    private final PyFunction m_pyFunction;

    private final PyMethod m_pyMethod;

    public InstrumentedPyJavaInstanceForPyMethodsAndPyFunctions(Test test, PyDispatcher dispatcher, PyFunction pyFunction) {
        super(test, dispatcher, pyFunction);
        m_pyFunction = pyFunction;
        m_pyMethod = null;
    }

    public InstrumentedPyJavaInstanceForPyMethodsAndPyFunctions(Test test, PyDispatcher dispatcher, PyMethod pyMethod) {
        super(test, dispatcher, pyMethod);
        m_pyFunction = null;
        m_pyMethod = pyMethod;
    }

    public final PyObject invoke(final String name) {
        if (name == InstrumentedPyInstance.TARGET_FIELD_NAME) {
            if (m_pyFunction != null) {
                return m_pyFunction.__call__();
            } else {
                return m_pyMethod.__call__();
            }
        }
        return getDispatcher().dispatch(new Dispatcher.Callable() {

            public Object call() {
                return InstrumentedPyJavaInstanceForPyMethodsAndPyFunctions.super.invoke(name);
            }
        });
    }

    public final PyObject invoke(final String name, final PyObject arg1) {
        if (name == InstrumentedPyInstance.TARGET_FIELD_NAME) {
            if (m_pyFunction != null) {
                return m_pyFunction.__call__(arg1);
            } else {
                return m_pyMethod.__call__(arg1);
            }
        }
        return getDispatcher().dispatch(new Dispatcher.Callable() {

            public Object call() {
                return InstrumentedPyJavaInstanceForPyMethodsAndPyFunctions.super.invoke(name, arg1);
            }
        });
    }

    public final PyObject invoke(final String name, final PyObject arg1, final PyObject arg2) {
        if (name == InstrumentedPyInstance.TARGET_FIELD_NAME) {
            if (m_pyFunction != null) {
                return m_pyFunction.__call__(arg1, arg2);
            } else {
                return m_pyMethod.__call__(arg1, arg2);
            }
        }
        return getDispatcher().dispatch(new Dispatcher.Callable() {

            public Object call() {
                return InstrumentedPyJavaInstanceForPyMethodsAndPyFunctions.super.invoke(name, arg1, arg2);
            }
        });
    }

    public final PyObject invoke(final String name, final PyObject[] args) {
        if (name == InstrumentedPyInstance.TARGET_FIELD_NAME) {
            if (m_pyFunction != null) {
                return m_pyFunction.__call__(args);
            } else {
                return m_pyMethod.__call__(args);
            }
        }
        return getDispatcher().dispatch(new Dispatcher.Callable() {

            public Object call() {
                return InstrumentedPyJavaInstanceForPyMethodsAndPyFunctions.super.invoke(name, args);
            }
        });
    }

    public final PyObject invoke(final String name, final PyObject[] args, final String[] keywords) {
        return getDispatcher().dispatch(new Dispatcher.Callable() {

            public Object call() {
                return InstrumentedPyJavaInstanceForPyMethodsAndPyFunctions.super.invoke(name, args, keywords);
            }
        });
    }
}
