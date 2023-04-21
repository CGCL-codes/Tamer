package com.googlecode.bdoc.doc.tinybdd.testdata;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestWithBeforeAndAfter {

    public static Object beforeObject = null;

    public static Object afterObject = null;

    @Before
    public void setBeforeObject() {
        beforeObject = new Object();
        afterObject = null;
    }

    @Test
    public void test() {
        assertNotNull(beforeObject);
        assertNull(afterObject);
    }

    @After
    public void setAfterObject() {
        afterObject = new Object();
    }
}
