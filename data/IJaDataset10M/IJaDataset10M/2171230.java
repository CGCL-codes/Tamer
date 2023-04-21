package org.gbif.ipt.validation;

import org.gbif.ipt.utils.CoordinateUtils;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for decToDms method in CoordinateUtils class.
 *
 * @author julieth
 */
@RunWith(value = Parameterized.class)
public class CoordinateUtilsTest {

    private String expectedString;

    private Double firstDoubleValue;

    private String secondStringValue;

    private static String degreeSign = "°";

    public CoordinateUtilsTest(String expectedString, Double firstDoubleValue, String secondStringValue) {
        this.expectedString = expectedString;
        this.firstDoubleValue = firstDoubleValue;
        this.secondStringValue = secondStringValue;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters() {
        Collection<Object[]> list = new ArrayList<Object[]>();
        list.add(new Object[] { "10" + degreeSign + "3'0''S", -10.05, CoordinateUtils.LATITUDE });
        list.add(new Object[] { "10" + degreeSign + "3'0''N", 10.05, CoordinateUtils.LATITUDE });
        list.add(new Object[] { "0" + degreeSign + "0'0''N", 0.0, CoordinateUtils.LATITUDE });
        list.add(new Object[] { "1" + degreeSign + "0'0''N", 1.0, CoordinateUtils.LATITUDE });
        list.add(new Object[] { "176" + degreeSign + "11'60''N", 176.20, CoordinateUtils.LATITUDE });
        list.add(new Object[] { "0" + degreeSign + "58'48''N", .98, CoordinateUtils.LATITUDE });
        list.add(new Object[] { "10" + degreeSign + "3'0''W", -10.05, CoordinateUtils.LONGITUDE });
        list.add(new Object[] { "10" + degreeSign + "3'0''E", 10.05, CoordinateUtils.LONGITUDE });
        list.add(new Object[] { "176" + degreeSign + "11'60''E", 176.20, CoordinateUtils.LONGITUDE });
        list.add(new Object[] { "0" + degreeSign + "0'0''E", 0.0, CoordinateUtils.LONGITUDE });
        list.add(new Object[] { "1" + degreeSign + "0'0''E", 1.0, CoordinateUtils.LONGITUDE });
        list.add(new Object[] { "0" + degreeSign + "58'48''E", .98, CoordinateUtils.LONGITUDE });
        list.add(new Object[] { "", 1.0, "" });
        list.add(new Object[] { "", 1.0, null });
        return list;
    }

    @Test
    public void decToDmsTest() {
        assertEquals(expectedString, CoordinateUtils.decToDms(firstDoubleValue, secondStringValue));
    }
}
