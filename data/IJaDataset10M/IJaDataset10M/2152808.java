package com.googlecode.gchartjava.parameters;

import static org.junit.Assert.assertEquals;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.googlecode.gchartjava.collect.Lists;

/**
 * 
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
public class AxisLabelPositionsParameterTest {

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
        final AxisLabelPositionsParameter p = new AxisLabelPositionsParameter();
        p.addLabelPosition(1, Lists.of(1, 2, 3));
        p.addLabelPosition(2, Lists.of(7, 8, 9));
        Logger.global.info(p.toURLParameterString());
        final String expectedString = "chxp=1,1,2,3|2,7,8,9";
        assertEquals("Junit error", expectedString, p.toURLParameterString());
    }

    @Test
    public void test1() {
        AxisLabelPositionsParameter p = new AxisLabelPositionsParameter();
        Logger.global.info(p.toURLParameterString());
        String expectedString = "";
        assertEquals("Junit error", expectedString, p.toURLParameterString());
    }

    @Test
    public void test2() {
        AxisLabelPositionsParameter p = new AxisLabelPositionsParameter();
        Logger.global.info(p.toURLParameterString());
        p.addLabelPosition(2, Lists.of(1, 2, 3));
        Logger.global.info(p.toURLParameterString());
        final String expectedString = "chxp=2,1,2,3";
        assertEquals("Junit error", expectedString, p.toURLParameterString());
    }
}
