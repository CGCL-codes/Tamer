package net.grinder.engine.process;

import org.python.core.Py;
import org.python.core.PyFinalizableInstance;
import org.python.core.PyJavaInstance;
import org.python.core.PyObject;

/**
 *
 * @author Philip Aston
 * @version $Revision: 1340 $
 */
class TestPyFinalizableInstance extends PyFinalizableInstance {

    private final TestData m_testData;

    private final PyObject m_pyTest;

    public TestPyFinalizableInstance(TestData testData, PyFinalizableInstance target) {
        super(target.__class__);
        __dict__ = target.__dict__;
        m_testData = testData;
        m_pyTest = new PyJavaInstance(testData.getTest());
    }

    private final PyObject dispatch(TestData.Invokeable invokeable) {
        try {
            return (PyObject) m_testData.dispatch(invokeable);
        } catch (Exception e) {
            throw Py.JavaError(e);
        }
    }

    protected PyObject ifindlocal(String name) {
        if (name == "__test__") {
            return m_pyTest;
        }
        return super.ifindlocal(name);
    }

    public PyObject invoke(final String name) {
        return dispatch(new TestData.Invokeable() {

            public Object call() {
                return TestPyFinalizableInstance.super.invoke(name);
            }
        });
    }

    public PyObject invoke(final String name, final PyObject arg1) {
        return dispatch(new TestData.Invokeable() {

            public Object call() {
                return TestPyFinalizableInstance.super.invoke(name, arg1);
            }
        });
    }

    public PyObject invoke(final String name, final PyObject arg1, final PyObject arg2) {
        return dispatch(new TestData.Invokeable() {

            public Object call() {
                return TestPyFinalizableInstance.super.invoke(name, arg1, arg2);
            }
        });
    }

    public PyObject invoke(final String name, final PyObject[] args) {
        return dispatch(new TestData.Invokeable() {

            public Object call() {
                return TestPyFinalizableInstance.super.invoke(name, args);
            }
        });
    }

    public PyObject invoke(final String name, final PyObject[] args, final String[] keywords) {
        return dispatch(new TestData.Invokeable() {

            public Object call() {
                return TestPyFinalizableInstance.super.invoke(name, args, keywords);
            }
        });
    }
}
