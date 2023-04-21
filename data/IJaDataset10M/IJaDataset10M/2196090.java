package com.googlecode.gchartjava;

import static com.googlecode.gchartjava.Color.RED;
import static com.googlecode.gchartjava.UrlUtil.normalize;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
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
public class AxisStyleTest {

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
    public void testNewAxisStyle() {
        final LineChart chart = TestUtil.getBasicChart();
        AxisLabels axisLabels = AxisLabelsFactory.newNumericAxisLabels(Arrays.asList(0, 1, 2, 3, 4, 5));
        axisLabels.setAxisStyle(AxisStyle.newAxisStyle(RED, 16, AxisTextAlignment.CENTER));
        chart.addXAxisLabels(axisLabels);
        Logger.global.info(chart.toURLString());
        String expectedString = "http://chart.apis.google.com/chart?chs=200x125&chd=e:AAgA..&chxt=x&chxp=0,0,1,2,3,4,5&chxr=0,0.0,5.0&chxs=0,FF0000,16,0&cht=lc";
        assertEquals("Junit error", normalize(expectedString), normalize(chart.toURLString()));
    }
}
