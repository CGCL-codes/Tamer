package org.python.core;

public abstract class IdImpl {

    public static IdImpl getInstance() {
        if (System.getProperty("java.version").compareTo("1.2") >= 0) {
            try {
                return (IdImpl) Class.forName("org.python.core.IdImpl2").newInstance();
            } catch (Throwable e) {
                return null;
            }
        } else {
            return new IdImpl1();
        }
    }

    public abstract long id(PyObject o);

    public abstract String idstr(PyObject o);

    public abstract long java_obj_id(Object o);
}
