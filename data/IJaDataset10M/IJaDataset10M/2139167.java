package nl.utwente.ewi.stream.network.utils;

import java.util.LinkedList;
import nl.utwente.ewi.stream.network.TestHelper;
import nl.utwente.ewi.stream.utils.ParameterHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import be.ac.ulg.montefiore.run.jahmm.ObservationInteger;

public class ParameterHelperTester {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        TestHelper.tearDown();
    }

    @Test
    public void testDumpObs() {
        int max_i = 20;
        int max_j = 20;
        LinkedList<LinkedList<ObservationInteger>> data = new LinkedList<LinkedList<ObservationInteger>>();
        for (int i = 0; i < max_i; i++) {
            data.add(new LinkedList<ObservationInteger>());
            for (int j = 0; j < max_j; j++) {
                data.get(i).add(new ObservationInteger(i * j + i + j));
            }
        }
        ParameterHelper.dumpObs(TestHelper.testResourcesPath() + "/temp.obj", 1, data);
    }

    @Test
    public void testDumpObsNotSquare() {
        int max_i = 20;
        int max_j = 20;
        LinkedList<LinkedList<ObservationInteger>> data = new LinkedList<LinkedList<ObservationInteger>>();
        for (int i = 0; i < max_i; i++) {
            data.add(new LinkedList<ObservationInteger>());
            int cMax = (int) (Math.random() * 100) % max_j;
            for (int j = 0; j < cMax; j++) {
                data.get(i).add(new ObservationInteger(i * j + i + j));
            }
        }
        try {
            ParameterHelper.dumpObs("/tmp/temp.obj", 2, data);
        } catch (RuntimeException rte) {
            rte.printStackTrace();
            assert (false);
        }
    }
}
