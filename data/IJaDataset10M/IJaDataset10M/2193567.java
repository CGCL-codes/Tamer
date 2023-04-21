package org.metawb.astro;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Handler;
import java.util.logging.Level;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.metawb.lib.SimpleLogFormatter;

/**
 *
 * @author erra
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ org.metawb.astro.DefaultAspectFinderTest.class, org.metawb.astro.ui.chart.DefaultPlanetModelTest.class, org.metawb.astro.ui.chart.DefaultAspectModelTest.class, org.metawb.astro.GeneralOutput.class })
public class AstroSuite {

    private static Handler handler;

    private static Deque<Level> stack = new ArrayDeque<Level>();

    public static void pushLogLevel(Level level) {
        if (handler == null) {
            handler = SimpleLogFormatter.setup("org.metawb", level);
        } else {
            handler.setLevel(level);
        }
        stack.push(level);
        return;
    }

    public static Level popLogLevel() {
        stack.pop();
        Level newLevel = stack.peek();
        if (newLevel == null) newLevel = Level.OFF;
        handler.setLevel(newLevel);
        return newLevel;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}
