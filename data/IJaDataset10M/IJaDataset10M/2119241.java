package jadaserver.script;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 *
 * @author gabry
 */
public class Experience {

    public static void nextLevelExp(byte level) {
        PythonInterpreter interp = new PythonInterpreter();
        interp.set("level", new PyInteger(level));
        interp.execfile("script/experience.py");
        PyFunction func = (PyFunction) interp.get("nextExp");
        PyObject nextLevelExp = func.__call__(new PyInteger(level));
        System.out.println(nextLevelExp);
    }
}
