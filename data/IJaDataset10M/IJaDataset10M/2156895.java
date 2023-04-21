package jmri.jmrit.catalog;

import jmri.*;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the CatalogTreeIndex class
 * @author	Bob Jacobsen  Copyright (C) 2009
 * @version $Revision: 17977 $
 */
public class CatalogTreeIndexTest extends TestCase {

    public void testSetProperty() {
        NamedBean n = new CatalogTreeIndex("sys", "usr") {

            public int getState() {
                return 0;
            }

            public void setState(int i) {
            }
        };
        n.setProperty("foo", "bar");
    }

    public void testGetParameter() {
        NamedBean n = new CatalogTreeIndex("sys", "usr") {

            public int getState() {
                return 0;
            }

            public void setState(int i) {
            }
        };
        n.setProperty("foo", "bar");
        Assert.assertEquals("bar", n.getProperty("foo"));
    }

    public void testGetSetNull() {
        NamedBean n = new CatalogTreeIndex("sys", "usr") {

            public int getState() {
                return 0;
            }

            public void setState(int i) {
            }
        };
        n.setProperty("foo", "bar");
        Assert.assertEquals("bar", n.getProperty("foo"));
        n.setProperty("foo", null);
        Assert.assertEquals(null, n.getProperty("foo"));
    }

    public CatalogTreeIndexTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { CatalogTreeIndexTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CatalogTreeIndexTest.class);
        return suite;
    }
}
