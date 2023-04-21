package com.ohrasys.cad.gds;

import junit.framework.*;
import junit.framework.TestCase;
import java.io.*;

public class GDSUstringRecordTest extends TestCase {

    public GDSUstringRecordTest(String testName) {
        super(testName);
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(GDSUstringRecordTest.class);
        return suite;
    }

    public void testData() {
        System.out.println("testData");
        try {
            GDSUstringRecord rec = new GDSUstringRecord();
            fail("Direct constructor should not be supported");
        } catch (GDSRecordException e) {
        }
    }
}
