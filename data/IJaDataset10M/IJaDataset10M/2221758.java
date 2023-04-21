package test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import core.Base;
import core.Launcher;
import core.Map;
import core.Model;
import junit.framework.TestCase;

public class InlogTest extends TestCase {

    private Model model;

    protected void setUp() throws Exception {
        super.setUp();
        model = new Model(new Launcher(), new Map(new Base(new Point2D.Double(0.0, 0.0)), new ArrayList<ArrayList<Point2D.Double>>(), 1), 1, false);
    }

    public void testOnthouden() {
        model.setUser("userName", "userEmail");
        assertEquals("userEmail", model.getUserEmail());
        assertEquals("userName", model.getUserName());
        model.setUser("tester", "test@ditmooieproject.nl");
        assertEquals("test@ditmooieproject.nl", model.getUserEmail());
        assertEquals("tester", model.getUserName());
    }
}
