package org.python.core;

public class PyStaticMethod extends PyObject implements PyType.Newstyle {

    public static final String exposed_name = "staticmethod";

    public static void typeSetup(PyObject dict, PyType.Newstyle marker) {
        dict.__setitem__("__new__", new PyNewWrapper(PyStaticMethod.class, "__new__", 1, 1) {

            public PyObject new_impl(boolean init, PyType subtype, PyObject[] args, String[] keywords) {
                if (keywords.length != 0 || args.length != 1) {
                    throw info.unexpectedCall(args.length, keywords.length != 0);
                }
                return new PyStaticMethod(args[0]);
            }
        });
    }

    protected PyObject callable;

    public PyStaticMethod(PyObject callable) {
        this.callable = callable;
    }

    public PyObject __get__(PyObject obj, PyObject type) {
        return callable;
    }
}
