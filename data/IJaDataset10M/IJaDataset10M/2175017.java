package edu.unika.aifb.rules.rules.heuristic.object;

import edu.unika.aifb.rules.result.ResultTable;
import edu.unika.aifb.rules.rules.heuristic.Heuristic;

/**
 * @author Marc Ehrig
 *
 */
public class Equal implements Heuristic {

    public double get(Object object1, Object object2) {
        boolean equal = object1.equals(object2);
        if (equal) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    public void setPreviousResult(ResultTable resultTable) {
    }
}
