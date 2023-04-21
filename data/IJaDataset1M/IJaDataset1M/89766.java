package com.ohrasys.cad.gds;

import junit.framework.*;
import junit.framework.TestCase;
import java.io.*;

public class GDSPathportRecordTest extends TestCase {

    public GDSPathportRecordTest(String testName) {
        super(testName);
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(GDSPathportRecordTest.class);
        return suite;
    }

    public void testData() {
        System.out.println("testData");
        try {
            GDSPathportRecord rec = new GDSPathportRecord();
            assertEquals(rec.PATHPORT, rec.getRectype());
            assertEquals(rec.NO_DATA_TYPE, rec.getDattype());
            assertEquals((short) 4, rec.getLength());
        } catch (GDSRecordException e) {
            fail(e.getMessage());
        }
    }
}
