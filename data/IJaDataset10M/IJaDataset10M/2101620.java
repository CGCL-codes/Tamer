package gr.spinellis.ckjm;

import org.apache.bcel.classfile.*;
import java.util.*;
import java.io.*;

/**
 * A container of class metrics mapping class names to their metrics.
 * This class contains the the metrics for all class's during the filter's
 * operation.  Some metrics need to be updated as the program processes
 * other classes, so the class's metrics will be recovered from this
 * container to be updated.
 *
 * @version $Revision: 78 $
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
class ClassMetricsContainer {

    /** The map from class names to the corresponding metrics */
    private HashMap<String, ClassMetrics> m = new HashMap<String, ClassMetrics>();

    /** Return a class's metrics */
    public ClassMetrics getMetrics(String name) {
        ClassMetrics cm = m.get(name);
        if (cm == null) {
            cm = new ClassMetrics();
            m.put(name, cm);
        }
        return cm;
    }

    /** Print the metrics of all the visited classes. */
    public void printMetrics(CkjmOutputHandler handler) {
        Set<Map.Entry<String, ClassMetrics>> entries = m.entrySet();
        Iterator<Map.Entry<String, ClassMetrics>> i;
        for (i = entries.iterator(); i.hasNext(); ) {
            Map.Entry<String, ClassMetrics> e = i.next();
            ClassMetrics cm = e.getValue();
            if (cm.isVisited() && (MetricsFilter.includeAll() || cm.isPublic())) handler.handleClass(e.getKey(), cm);
        }
    }
}
