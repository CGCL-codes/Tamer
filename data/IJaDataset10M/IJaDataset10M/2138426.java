package python;

/** Python module. */
public class PyModule extends PyObject {

    /** Python '__builtin__' module, container of all built-in objects  */
    public static final PyModule Builtins = new PyModule("__builtin__");

    protected PyModule(long ptr) {
        super(ptr);
    }

    /** 
   * return Python module wrapper
   * @param name Python module name
   * <br>example: PyModule myPyModule = PyModule( "my.Py.Module")
   */
    public PyModule(String name) {
        super(Javapy.PyImport_ImportModule(PyThreadState.get(), name));
    }
}
