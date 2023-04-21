package net.sf.dpdesktop.service.container;

import net.sf.dpdesktop.service.container.Container;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Heiner Reinhardt
 */
public class ContainerTest {

    /**
     * Any trackable container should be accessible at least at level
     * three of the tree.
     */
    @Test
    public void test3DHierachy() {
        Container c = new Container("Company", null);
        Container p = new Container("Project", null, false);
        Container t = new Container("Task", null, true);
        c.addChild(p);
        p.addChild(t);
        assertEquals(t.getParent(), p);
        assertEquals(p.getParent(), c);
        assertEquals(c.getParent(), null);
        assertTrue(t.hasParent());
        assertTrue(p.hasParent());
        assertFalse(c.hasParent());
        assertEquals(c.getChildren().get(0).getChildren().get(0), t);
    }

    /**
     * It is important, that you cannot add elements into the tree from outside
     * the tree. Especially elements with pointer (such as List) should be
     * returned as a clone or something like that.
     */
    @Test
    public void testInconsitenciesFromOuter() {
        Container c = new Container("Company", null, false);
        Container p = new Container("Project", null, false);
        c.addChild(p);
        c.getChildren().clear();
        assertEquals(c.getChildren().get(0), p);
    }
}
