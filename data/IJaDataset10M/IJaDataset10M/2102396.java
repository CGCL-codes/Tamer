package oxygen.jython;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PySequence;
import org.python.util.PythonInterpreter;

public class OxyJythonConvenienceCommands {

    public static Map py2jmap(PyDictionary dict) {
        Map m = new HashMap();
        PyList keys = dict.keys();
        int len = keys.__len__();
        for (int i = 0; i < len; i++) {
            PyObject pykey = keys.__finditem__(i);
            Object key = pykey.__tojava__(Object.class);
            Object value = dict.get(pykey).__tojava__(Object.class);
            m.put(key, value);
        }
        return m;
    }

    public static List py2jlist(PySequence seq) {
        List col = new ArrayList();
        int len = seq.__len__();
        for (int i = 0; i < len; i++) {
            PyObject pykey = seq.__finditem__(i);
            Object key = pykey.__tojava__(Object.class);
            col.add(key);
        }
        return col;
    }

    public static PyList j2pylist(Collection col) {
        PyList pylist = new PyList();
        for (Iterator itr = col.iterator(); itr.hasNext(); ) {
            Object key = itr.next();
            pylist.append(Py.java2py(key));
        }
        return pylist;
    }

    public static PyDictionary j2pydict(Map map) {
        PyDictionary pydict = new PyDictionary();
        for (Iterator itr = map.keySet().iterator(); itr.hasNext(); ) {
            Object key = itr.next();
            Object val = map.get(itr.next());
            pydict.__setitem__(Py.java2py(key), Py.java2py(val));
        }
        return pydict;
    }

    public static void loadinterpvars(PythonInterpreter interp, Map m) throws Exception {
        for (Iterator itr = m.keySet().iterator(); itr.hasNext(); ) {
            String key = (String) itr.next();
            Object val = m.get(key);
            interp.set(key, val);
        }
    }
}
