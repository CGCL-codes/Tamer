package com.googlecode.charts4j;

import static com.googlecode.charts4j.Color.*;
import static com.googlecode.charts4j.UrlUtil.normalize;
import static org.junit.Assert.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.googlecode.charts4j.collect.Lists;

/**
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 *
 */
public class GoogleOMeterTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.ALL);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void basicTest() {
        GoogleOMeter chart = GCharts.newGoogleOMeter(100);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(chart.toURLString());
        String expectedString = "http://chart.apis.google.com/chart?cht=gom&chs=200x125&chd=e:..";
        assertEquals("Junit error", normalize(expectedString), normalize(chart.toURLString()));
    }

    @Test
    public void basicTest2() {
        GoogleOMeter chart = GCharts.newGoogleOMeter(10, "Slow");
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(chart.toURLString());
        String expectedString = "http://chart.apis.google.com/chart?chd=e:Ga&chl=Slow&chs=200x125&cht=gom";
        assertEquals("Junit error", normalize(expectedString), normalize(chart.toURLString()));
    }

    @Test
    public void colorInterpolationTest() {
        GoogleOMeter chart = GCharts.newGoogleOMeter(50, "speed", "fast", RED, GREEN, BLUE);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(chart.toURLString());
        String expectedString = "http://chart.apis.google.com/chart?chco=FF0000,008000,0000FF&chd=e:gA&chdl=fast&chl=speed&chs=200x125&cht=gom";
        assertEquals("Junit error", normalize(expectedString), normalize(chart.toURLString()));
    }

    @Test
    public void colorInterpolationTest2() {
        List<Color> colors = Lists.of(BLUE, WHITE, RED);
        GoogleOMeter chart = GCharts.newGoogleOMeter(25, "speed", "fast", colors);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(chart.toURLString());
        String expectedString = "http://chart.apis.google.com/chart?chco=0000FF,FFFFFF,FF0000&chd=e:QA&chdl=fast&chl=speed&chs=200x125&cht=gom";
        assertEquals("Junit error", normalize(expectedString), normalize(chart.toURLString()));
    }

    @Test
    public void negativeControl() {
        try {
            @SuppressWarnings("unused") GoogleOMeter chart = GCharts.newGoogleOMeter(50, "speed", "fast", RED);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void fillAndTitleTest() {
        GoogleOMeter chart = GCharts.newGoogleOMeter(100);
        chart.setAreaFill(Fills.newSolidFill(LIGHTGREY));
        LinearGradientFill fill = Fills.newLinearGradientFill(0, RED, 100);
        fill.addColorAndOffset(BLUE, 0);
        chart.setBackgroundFill(fill);
        chart.setTitle("Title Test", WHITE, 12);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(chart.toURLString());
        String expectedString = "http://chart.apis.google.com/chart?chd=e:..&chf=bg,lg,0,FF0000,1.0,0000FF,0.0|c,s,D3D3D3&chs=200x125&cht=gom&chts=FFFFFF,12&chtt=Title+Test";
        assertEquals("Junit error", normalize(expectedString), normalize(chart.toURLString()));
    }
}
