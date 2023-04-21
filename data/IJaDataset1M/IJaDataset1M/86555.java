package nick.graph.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import junit.framework.TestCase;
import nick.graph.Edge;
import nick.graph.EdgeDouble;

/**
 * Test methods for the EdgeDouble class.
 * 
 * @author Nick Aschenbach
 * @version March 15th, 2009
 */
public class EdgeDoubleTester extends TestCase {

    public void testDoubleEdge() {
        Edge<Double> e1 = new EdgeDouble<Double>(Math.PI);
        if (e1.data() != Math.PI) {
            fail();
        }
        if (e1.name() != null) {
            fail();
        }
        Edge<Double> e2 = new EdgeDouble<Double>(-395238.3428554, "e2");
        if (e2.data() != -395238.3428554) {
            fail();
        }
        if (e2.name() != "e2") {
            fail();
        }
    }

    public void testEquals1() {
        Edge<Double> e1 = new Edge<Double>(1.0);
        EdgeDouble<Double> e2 = new EdgeDouble<Double>(1.0);
        if (e1.equals(e2)) {
            fail();
        }
        if (e2.equals(e1)) {
            fail();
        }
    }

    public void testEquals2() {
        EdgeDouble<Double> e1 = new EdgeDouble<Double>(0.0, "a");
        EdgeDouble<Double> e2 = new EdgeDouble<Double>(0.0, "a");
        if (!e1.equals(e2)) {
            fail();
        }
        e1.setData(1.1);
        if (e1.equals(e2)) {
            fail();
        }
        e2.setData(1.1);
        if (!e1.equals(e2)) {
            fail();
        }
    }

    public void testSortingDoubleEdge() {
        List<EdgeDouble<Double>> list = new ArrayList<EdgeDouble<Double>>();
        int num = 1000;
        Random r = new Random();
        for (int i = 0; i < num; i++) {
            list.add(new EdgeDouble<Double>(r.nextDouble()));
        }
        Collections.sort(list);
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).data() > list.get(i + 1).data()) {
                fail();
            }
        }
    }
}
