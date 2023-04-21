package net.grinder.engine.process.jython;

import java.lang.reflect.Field;
import org.python.core.Py;
import org.python.core.PyClass;
import org.python.core.PyException;
import org.python.core.PyFunction;
import org.python.core.PyInstance;
import org.python.core.PyJavaClass;
import org.python.core.PyMethod;
import org.python.core.PyObject;
import org.python.core.PyProxy;
import org.python.core.PyReflectedFunction;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import net.grinder.common.Test;
import net.grinder.common.UncheckedGrinderException;
import net.grinder.engine.common.EngineException;
import net.grinder.engine.common.ScriptLocation;
import net.grinder.engine.process.ScriptEngine;
import net.grinder.script.NotWrappableTypeException;
import net.grinder.script.Grinder.ScriptContext;

/**
 * Wrap up the context information necessary to invoke a Jython script.
 *
 * Package scope.
 *
 * @author Philip Aston
 * @version $Revision: 3762 $
 */
public final class JythonScriptEngine implements ScriptEngine {

    private static final String TEST_RUNNER_CALLABLE_NAME = "TestRunner";

    private final PySystemState m_systemState;

    private final PythonInterpreter m_interpreter;

    private final JythonVersionAdapter m_versionAdapter;

    private final PyInstrumentedProxyFactory m_instrumentedProxyFactory;

    private PyObject m_testRunnerFactory;

    /**
   * Constructor for JythonScriptEngine.
   *
   * @param scriptContext The script context.
   * @throws EngineException If the script engine could not be created.
   */
    public JythonScriptEngine(ScriptContext scriptContext) throws EngineException {
        PySystemState.initialize();
        m_systemState = new PySystemState();
        m_interpreter = new PythonInterpreter(null, m_systemState);
        m_versionAdapter = new JythonVersionAdapter();
        m_instrumentedProxyFactory = new PyInstrumentedProxyFactory();
    }

    /**
   * Run any process initialisation required by the script. Called once
   * per ScriptEngine instance.
   *
   * @param script The script.
   * @throws EngineException If process initialisation failed.
   */
    public void initialise(ScriptLocation script) throws EngineException {
        m_systemState.path.insert(0, new PyString(script.getDirectory().getFile().getPath()));
        try {
            m_interpreter.execfile(script.getFile().getPath());
        } catch (PyException e) {
            throw new JythonScriptExecutionException("initialising test script", e);
        }
        m_testRunnerFactory = m_interpreter.get(TEST_RUNNER_CALLABLE_NAME);
        if (m_testRunnerFactory == null || !m_testRunnerFactory.isCallable()) {
            throw new EngineException("There is no callable (class or function) named '" + TEST_RUNNER_CALLABLE_NAME + "' in " + script);
        }
    }

    /**
   * Create a {@link WorkerRunnable} that will be used to run the work
   * for one worker thread.
   *
   * @return The runnable.
   * @throws EngineException If the runnable could not be created.
   */
    public WorkerRunnable createWorkerRunnable() throws EngineException {
        return new JythonWorkerRunnable();
    }

    /**
   * Create a proxy object that wraps an target object for a test.
   *
   * @param test The test.
   * @param dispatcher The proxy should use this to dispatch the work.
   * @param o Object to wrap.
   * @return The instrumented proxy.
   * @throws NotWrappableTypeException If the target cannot be wrapped.
   */
    public Object createInstrumentedProxy(Test test, Dispatcher dispatcher, Object o) throws NotWrappableTypeException {
        return m_instrumentedProxyFactory.instrumentObject(test, new PyDispatcher(dispatcher), o);
    }

    /**
   * Create a proxy PyObject that wraps an target object for a test.
   *
   * <p>
   * We could have defined overloaded createProxy methods that take a
   * PyInstance, PyFunction etc., and return decorator PyObjects. There's no
   * obvious way of doing this in a polymorphic way, so we would be forced to
   * have n factories, n types of decorator, and probably run into identity
   * issues. Instead we lean on Jython and force it to give us Java proxy which
   * we then dynamically subclass with our own wrappers.
   * </p>
   *
   * <p>
   * Of course we're only really interested in the things we can invoke in some
   * way. We throw NotWrappableTypeException for the things we don't want to
   * handle.
   * </p>
   *
   * <p>
   * The specialised PyJavaInstance works surprisingly well for everything bar
   * PyInstances. It can't work for PyInstances, because invoking on the
   * PyJavaInstance calls the PyInstance which in turn attempts to call back on
   * the PyJavaInstance. Use specialised PyInstance clone objects to handle this
   * case. We also need to handle PyReflectedFunctions as an exception.
   * </p>
   *
   * <p>
   * Jython 2.2 requires special handling for Java instances, as method
   * invocations are now dispatched by first looking up the method using
   * __findattr__. See {@link InstrumentedPyJavaInstanceForJavaInstances}.
   * </p>
   *
   * <p>
   * There's a subtle difference in the equality semantics of
   * InstrumentedPyInstances and InstrumentedPyJavaInstances.
   * InstrumentedPyInstances compare do not equal to the wrapped objects, where
   * as due to <code>PyJavaInstance._is()</code> semantics,
   * InstrumentedPyJavaInstances <em>do</em> compare equal to the wrapped
   * objects. We can only influence one side of the comparison (we can't easily
   * alter the <code>_is</code> implementation of wrapped objects) so we can't
   * do anything nice about this.
   * </p>
   */
    class PyInstrumentedProxyFactory {

        /**
     * See {@link PyInstrumentedProxyFactory}.
     *
     *
     * @param test
     *          The test.
     * @param pyDispatcher
     *          The proxy should use this to dispatch the work.
     * @param o
     *          Object to wrap.
     * @return The instrumented proxy.
     * @throws NotWrappableTypeException
     *           If the target cannot be wrapped.
     */
        public PyObject instrumentObject(Test test, PyDispatcher pyDispatcher, Object o) throws NotWrappableTypeException {
            if (o instanceof PyObject) {
                if (o instanceof PyInstance) {
                    final PyInstance pyInstance = (PyInstance) o;
                    final PyClass pyClass = m_versionAdapter.getClassForInstance(pyInstance);
                    return new InstrumentedPyInstance(this, test, pyDispatcher, pyClass, pyInstance);
                } else if (o instanceof PyFunction) {
                    return new InstrumentedPyJavaInstanceForPyMethodsAndPyFunctions(test, pyDispatcher, (PyFunction) o);
                } else if (o instanceof PyMethod) {
                    return instrumentPyMethod(test, pyDispatcher, (PyMethod) o);
                } else if (o instanceof PyReflectedFunction) {
                    return new InstrumentedPyReflectedFunction(test, pyDispatcher, (PyReflectedFunction) o);
                }
            } else if (o instanceof PyProxy) {
                final PyInstance pyInstance = ((PyProxy) o)._getPyInstance();
                final PyClass pyClass = m_versionAdapter.getClassForInstance(pyInstance);
                return new InstrumentedPyInstance(this, test, pyDispatcher, pyClass, pyInstance);
            } else if (o == null) {
                throw new NotWrappableTypeException("Can't wrap null/None");
            } else {
                final Class c = o.getClass();
                if (!c.isArray() && !(o instanceof Number) && !(o instanceof String)) {
                    return new InstrumentedPyJavaInstanceForJavaInstances(this, test, pyDispatcher, o);
                }
            }
            throw new NotWrappableTypeException(o.getClass().getName());
        }

        public PyObject instrumentPyMethod(Test test, PyDispatcher pyDispatcher, PyMethod o) {
            return new InstrumentedPyJavaInstanceForPyMethodsAndPyFunctions(test, pyDispatcher, o);
        }
    }

    /**
   * Shut down the engine.
   *
   * <p>
   * We don't use m_interpreter.cleanup(), which delegates to
   * PySystemState.callExitFunc, as callExitFunc logs problems to stderr.
   * Instead we duplicate the callExitFunc behaviour raise our own exceptions.
   * </p>
   *
   * @throws EngineException
   *           If the engine could not be shut down.
   */
    public void shutdown() throws EngineException {
        final PyObject exitfunc = m_systemState.__findattr__("exitfunc");
        if (exitfunc != null) {
            try {
                exitfunc.__call__();
            } catch (PyException e) {
                throw new JythonScriptExecutionException("calling script exit function", e);
            }
        }
    }

    /**
   * Returns a description of the script engine for the log.
   *
   * @return The description.
   */
    public String getDescription() {
        return "Jython " + PySystemState.version;
    }

    /**
   * Wrapper for script's TestRunner.
   */
    private final class JythonWorkerRunnable implements ScriptEngine.WorkerRunnable {

        private final PyObject m_testRunner;

        private JythonWorkerRunnable() throws EngineException {
            try {
                m_testRunner = m_testRunnerFactory.__call__();
            } catch (PyException e) {
                throw new JythonScriptExecutionException("creating per-thread TestRunner object", e);
            }
            if (!m_testRunner.isCallable()) {
                throw new EngineException("The result of '" + TEST_RUNNER_CALLABLE_NAME + "()' is not callable");
            }
        }

        public void run() throws ScriptExecutionException {
            try {
                m_testRunner.__call__();
            } catch (PyException e) {
                throw new JythonScriptExecutionException("calling TestRunner", e);
            }
        }

        /**
     * <p>Ensure that if the test runner has a <code>__del__</code>
     * attribute, it is called when the thread is shutdown. Normally
     * Jython defers this to the Java garbage collector, so we might
     * have done something like
     *
     * <blockquote><pre>
     * m_testRunner = null; Runtime.getRuntime().gc();
     *</pre></blockquote>
     *
     * instead. However this would have a number of problems:
     *
     * <ol>
     * <li>Some JVM's may chose not to finalise the test runner in
     * response to <code>gc()</code>.</li>
     * <li><code>__del__</code> would be called by a GC thread.</li>
     * <li>The standard Jython finalizer wrapping around
     * <code>__del__</code> logs to <code>stderr</code>.</li>
     * </ol></p>
     *
     * <p>Instead, we call any <code>__del__</code> ourselves. After
     * calling this method, the <code>PyObject</code> that underlies
     * this class is made invalid.</p>
    */
        public void shutdown() throws ScriptExecutionException {
            final PyObject del = m_testRunner.__findattr__("__del__");
            if (del != null) {
                try {
                    del.__call__();
                } catch (PyException e) {
                    throw new JythonScriptExecutionException("deleting TestRunner instance", e);
                } finally {
                    m_versionAdapter.disableDel(m_testRunner);
                }
            }
        }
    }

    /**
   * Work around different the Jython implementations.
   *
   * @author Philip Aston
   * @version $Revision: 3762 $
   */
    private static class JythonVersionAdapter {

        private final Field m_instanceClassField;

        private final PyClass m_dieQuietly = PyJavaClass.lookup(Object.class);

        public JythonVersionAdapter() throws EngineException {
            Field f;
            try {
                f = PyObject.class.getField("__class__");
            } catch (NoSuchFieldException e) {
                try {
                    f = PyInstance.class.getField("instclass");
                } catch (NoSuchFieldException e2) {
                    throw new EngineException("Incompatible Jython release in classpath");
                }
            }
            m_instanceClassField = f;
        }

        public void disableDel(PyObject pyObject) {
            try {
                m_instanceClassField.set(pyObject, m_dieQuietly);
            } catch (IllegalArgumentException e) {
                throw new AssertionError(e);
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }

        public PyClass getClassForInstance(PyInstance target) {
            try {
                return (PyClass) m_instanceClassField.get(target);
            } catch (IllegalArgumentException e) {
                throw new AssertionError(e);
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
    }

    /**
   * A dispatcher that translates return types and exceptions from the script.
   *
   * <p>
   * The delegate {@link Dispatcher} can be safely invoked multiple times for
   * the same test and thread (only the outer invocation will be recorded).
   * Consequently there is no problem with our PyInstance instrumentation and
   * Jython 1.1, where Jython can make multiple calls through our instrumented
   * invoke methods.
   * </p>
   */
    static final class PyDispatcher {

        private final Dispatcher m_delegate;

        private PyDispatcher(Dispatcher delegate) {
            m_delegate = delegate;
        }

        public PyObject dispatch(Dispatcher.Callable callable) {
            try {
                return (PyObject) m_delegate.dispatch(callable);
            } catch (UncheckedGrinderException e) {
                throw e;
            } catch (Exception e) {
                throw Py.JavaError(e);
            }
        }
    }
}
