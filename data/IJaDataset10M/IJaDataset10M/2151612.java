package org.jquantlib.testsuite.util.stdlibc;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.jquantlib.testsuite.math.interpolations.BilinearInterpolationTest;
import org.jquantlib.util.stdlibc.Std;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StdTest {

    private static final Logger logger = LoggerFactory.getLogger(BilinearInterpolationTest.class);

    public StdTest() {
    }

    @Test
    public void shouldReturnAdjacent_difference() {
        List<Double> inputList = Arrays.asList(1.0, 2.0, 3.0, 5.0, 9.0, 11.0, 12.0);
        List<Double> outputList = new ArrayList<Double>();
        List<Double> expected = Arrays.asList(1.0, 1.0, 1.0, 2.0, 4.0, 2.0, 1.0);
        outputList = Std.adjacent_difference(inputList, 0, outputList);
        Iterator<Double> outIter = outputList.iterator();
        Iterator<Double> expIter = expected.iterator();
        while (outIter.hasNext()) {
            assertEquals("adjacent_difference failed", outIter.next(), expIter.next());
        }
    }
}
