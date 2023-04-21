package com.ohrasys.cad.gds;

import junit.framework.*;
import junit.framework.TestCase;
import java.io.*;

public class GDSStransRecordTest extends TestCase {

    public GDSStransRecordTest(String testName) {
        super(testName);
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(GDSStransRecordTest.class);
        return suite;
    }

    public void testData() {
        System.out.println("testData");
        try {
            GDSStransRecord rec = new GDSStransRecord(true, false, true);
            assertEquals(rec.STRANS, rec.getRectype());
            assertEquals(rec.BIT_ARRAY_TYPE, rec.getDattype());
            assertEquals((short) 6, rec.getLength());
        } catch (GDSRecordException e) {
            fail(e.getMessage());
        }
    }

    public void testSetIsAbsAngle() {
        System.out.println("testSetIsAbsAngle");
        try {
            GDSStransRecord rec = new GDSStransRecord(false, false, true);
            assertEquals(rec.isAbsAngle(), true);
            rec.setAbsAngle(false);
            assertEquals(rec.isAbsAngle(), false);
        } catch (GDSRecordException e) {
            fail(e.getMessage());
        }
    }

    public void testSetIsAbsMag() {
        System.out.println("testSetIsAbsMag");
        try {
            GDSStransRecord rec = new GDSStransRecord(false, true, false);
            assertEquals(rec.isAbsMag(), true);
            rec.setAbsMag(false);
            assertEquals(rec.isAbsMag(), false);
        } catch (GDSRecordException e) {
            fail(e.getMessage());
        }
    }

    public void testSetIsMirroredX() {
        System.out.println("testSetIsMirroredX");
        try {
            GDSStransRecord rec = new GDSStransRecord(true, false, false);
            assertEquals(rec.isMirroredX(), true);
            rec.setMirroredX(false);
            assertEquals(rec.isMirroredX(), false);
        } catch (GDSRecordException e) {
            fail(e.getMessage());
        }
    }
}
