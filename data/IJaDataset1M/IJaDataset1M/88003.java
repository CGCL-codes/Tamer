package com.cube42.util.data;

import java.util.Date;
import junit.framework.TestSuite;
import com.cube42.util.exception.Cube42NullParameterException;
import com.cube42.util.testing.Cube42UnitTestCase;

/**
 * Unit test code for DataQuery
 * <P>
 *
 * @author   Matt Paulin
 * @version  $Id: DataQuery_UT.java,v 1.6 2003/03/12 00:28:06 zer0wing Exp $
 */
public class DataQuery_UT extends Cube42UnitTestCase {

    /**
     * constructor takes name of test method
     *
     * @param name          The name of the test method to be run.
     */
    public DataQuery_UT(String name) {
        super(name);
    }

    /**
     * Test limiting date
     */
    public void testLimitingDate() {
        DataPointCode code1 = new DataQuery_UTDataPointCode("code1", "desc", "units");
        DataQuery query;
        query = new DataQuery(DataQuery.NO_EARLIEST_DATE, DataQuery.NO_LATEST_DATE, code1);
        query.setEarliestDate(new Date(0));
        assertEquals(DataPoint.EARLIEST_DATE, query.getEarliestDate());
        query.setEarliestDate(new Date(Long.MAX_VALUE));
        assertEquals(DataPoint.LATEST_DATE, query.getEarliestDate());
        query.setLatestDate(new Date(0));
        assertEquals(DataPoint.EARLIEST_DATE, query.getLatestDate());
        query.setLatestDate(new Date(Long.MAX_VALUE));
        assertEquals(DataPoint.LATEST_DATE, query.getLatestDate());
    }

    /**
     * Test for nulls in the constructor
     */
    public void testNulls() {
        DataPointCode code1 = new DataQuery_UTDataPointCode("code1", "desc", "units");
        DataQuery query;
        try {
            query = new DataQuery(null, DataQuery.NO_LATEST_DATE, code1);
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals("earliestDate", e.getParameters().toArray()[0]);
        }
        try {
            query = new DataQuery(DataQuery.NO_EARLIEST_DATE, null, code1);
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals("latestDate", e.getParameters().toArray()[0]);
        }
        try {
            query = new DataQuery(DataQuery.NO_EARLIEST_DATE, DataQuery.NO_LATEST_DATE, null);
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals("dataPointCode", e.getParameters().toArray()[0]);
        }
    }

    /**
     * Test getting and setting the data point code
     */
    public void testGettingSetting() {
        DataPointCode code1 = new DataQuery_UTDataPointCode("code1", "desc", "units");
        DataPointCode code2 = new DataQuery_UTDataPointCode("code2", "desc", "units");
        DataQuery query;
        query = new DataQuery(DataQuery.NO_EARLIEST_DATE, DataQuery.NO_LATEST_DATE, code1);
        try {
            query.setDataPointCode(null);
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals("dataPointCode", e.getParameters().toArray()[0]);
        }
        try {
            query.setEarliestDate(null);
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals("earliestDate", e.getParameters().toArray()[0]);
        }
        try {
            query.setLatestDate(null);
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals("latestDate", e.getParameters().toArray()[0]);
        }
        assertEquals(code1, query.getDataPointCode());
        query.setDataPointCode(code2);
        assertEquals(code2, query.getDataPointCode());
        assertEquals(DataQuery.NO_EARLIEST_DATE, query.getEarliestDate());
        query.setEarliestDate(new Date(28800042));
        assertEquals(new Date(28800042), query.getEarliestDate());
        assertEquals(DataQuery.NO_LATEST_DATE, query.getLatestDate());
        query.setLatestDate(new Date(28804242));
        assertEquals(new Date(28804242), query.getLatestDate());
    }

    /**
     * Test to see if the query figures out if it's empty
     */
    public void testQueryEmpty() {
        DataPointCode code1 = new DataQuery_UTDataPointCode("code1", "desc", "units");
        DataQuery query;
        query = new DataQuery(DataQuery.NO_EARLIEST_DATE, DataQuery.NO_LATEST_DATE, code1);
        assertTrue("Empty Query", query.isEmpty());
        query.setEarliestDate(new Date(28800001));
        assertTrue(!query.isEmpty());
        query.setEarliestDate(DataQuery.NO_EARLIEST_DATE);
        assertTrue(query.isEmpty());
        query.setLatestDate(new Date(28800001));
        assertTrue(!query.isEmpty());
        query.setLatestDate(DataQuery.NO_LATEST_DATE);
        assertTrue(query.isEmpty());
    }

    /**
     * Runs this unit tests using junit.textui.TestRunner
     */
    public static void main(String[] args) {
        TestSuite suite = (new TestSuite(DataQuery_UT.class));
        System.out.println("Testing the DataQuery class");
        junit.textui.TestRunner.run(suite);
    }

    /**
     * Internal class that is used to create all DataPointCodes
     */
    class DataQuery_UTDataPointCode extends IntegerDataPointCode {

        /**
         * Constructs a DataQuery_UTDataPointCode
         *
         * @param   type    The type of data point code
         * @param   description The description of the data point code
         * @param   units   The units of the data points
         */
        DataQuery_UTDataPointCode(String type, String description, String units) {
            super(type, description, units);
        }
    }
}
