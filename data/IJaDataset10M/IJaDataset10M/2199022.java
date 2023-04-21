package hub.metrik.lang.eprovide.python;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

public class PythonUtil {

    public static EList<Object> arrayToEList(Object[] array) {
        BasicEList<Object> elist = new BasicEList<Object>();
        elist.addAllUnique(array, 0, array.length);
        return elist;
    }
}
