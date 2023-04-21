package de.jmda.util.gui.awt.graphics;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import org.junit.Test;
import de.jmda.util.gui.awt.graphics.RelationEndpoint.Orientation;
import de.jmda.util.gui.awt.graphics.RelationEndpoint.Style;

/**
 * @author roger@jmda.de
 */
public class JUTRelationEndpoint {

    @Test
    public void test() {
        RelationEndpoint relationEndpoint = new RelationEndpoint(new Point(100, 100), 10, Style.FILLED, false, 4, Orientation.DOWN);
        assertTrue(relationEndpoint.contains(94, 98));
        assertTrue(relationEndpoint.contains(94, 99));
        assertTrue(relationEndpoint.contains(105, 98));
        assertTrue(relationEndpoint.contains(105, 99));
        assertFalse(relationEndpoint.contains(93, 98));
        assertFalse(relationEndpoint.contains(94, 100));
        assertFalse(relationEndpoint.contains(106, 98));
        assertFalse(relationEndpoint.contains(105, 100));
    }
}
