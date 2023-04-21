package com.googlecode.gchartjava.parameters;

import static org.junit.Assert.assertEquals;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
public class GridLineParameterTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Logger.global.setLevel(Level.ALL);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test0() {
        final GridLineParameter p = new GridLineParameter(20, 50, 1, 5);
        Logger.global.info(p.toURLParameterString());
        final String expectedString = "chg=20.0,50.0,1,5";
        assertEquals("Junit error", expectedString, p.toURLParameterString());
    }
}
