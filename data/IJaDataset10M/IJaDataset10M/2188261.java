package de.javacus.grafmach.twoD.attr;

import java.awt.Color;
import de.javacus.grafmach.twoD.Drawable;
import de.javacus.grafmach.twoD.GrafTestCase;
import de.javacus.grafmach.twoD.IALine;
import de.javacus.grafmach.twoD.plain.Line;

/**
 * 
 */
public class ADrawPanelTest extends GrafTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * 
	 */
    public final void testALine() {
        Line line = new Line(100, 100, 200, 100);
        IALine aLine = new ALine(line);
        aLine.setColor(Color.MAGENTA);
        ADrawPanel aDrawPanel = new ADrawPanel();
        aDrawPanel.add(aLine);
        openFrame(aDrawPanel);
        assertEquals(true, ask("testALine"));
        pause(0.1);
    }
}
