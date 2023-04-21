package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import riaf.controller.RiafMgr;
import riaf.facade.IStatusBar;
import riafswing.RStatusBar;

public class RStatusBarStaticTest extends RiafTestCase {

    RStatusBar bar;

    @Before
    public void init() {
        bar = new RStatusBar("testStatusBar", RiafMgr.global().searchPageHolder("main").getContainer());
    }

    @Test
    public void testConstructor() {
        assertNotNull(bar);
    }

    @Test
    public void testName() {
        assertEquals(IStatusBar.name, bar.getName());
    }
}
