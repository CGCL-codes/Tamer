package org.herasaf.xacml.core.types.test;

import static org.testng.Assert.assertEquals;
import org.herasaf.xacml.core.types.Time;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestTime {

    @DataProvider(name = "positiveCases")
    public Object[][] createPositiveCases() {
        return new Object[][] { new Object[] { "12:00:01.239" }, new Object[] { "12:00:01.2" }, new Object[] { "12:00:01.35" }, new Object[] { "12:00:01" }, new Object[] { "12:00:01-05:00" } };
    }

    @DataProvider(name = "comparison")
    public Object[][] createComparisonData() {
        return new Object[][] { new Object[] { "12:00:00", "13:00:00", -1 }, new Object[] { "15:00:00", "13:00:00", 1 }, new Object[] { "13:00:00", "13:00:00", 0 }, new Object[] { "12:00:01", "12:00:01.001", -1 }, new Object[] { "15:03:00", "13:00:00.234", 1 }, new Object[] { "13:00:00.978", "13:00:00.978", 0 } };
    }

    @DataProvider(name = "negativeCases")
    public Object[][] createNegativeCases() {
        return new Object[][] { new Object[] { "12.00.01" }, new Object[] { "2005-10-10" } };
    }

    @AfterMethod
    public void afterMethod() {
        System.clearProperty("javax.xml.datatype.DatatypeFactory");
    }

    @Test(dataProvider = "positiveCases")
    public void testInput1(String input) throws Exception {
        Time time = new Time(input);
        assertEquals(time.toString(), input);
    }

    @Test(dataProvider = "negativeCases", expectedExceptions = { IllegalArgumentException.class })
    public void testInputFail(String input) throws Exception {
        new Time(input);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testNoDataFactoryFound() throws Exception {
        System.setProperty("javax.xml.datatype.DatatypeFactory", "bla");
        new Time("12:00:01");
    }

    @Test(dataProvider = "comparison")
    public void testCompare(String input1, String input2, int expected) throws Exception {
        Time one = new Time(input1);
        Time two = new Time(input2);
        assertEquals(one.compareTo(two), expected);
    }
}
