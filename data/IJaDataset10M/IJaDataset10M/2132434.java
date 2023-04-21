package de.jakop.rugby.zeit.gui;

import junit.framework.TestCase;
import de.jakop.rugby.util.UhrHelper;
import de.jakop.rugby.zeit.Timer;

/**
 * @author jakop
 */
public class ZeitTableCellRendererTest extends TestCase {

    private ZeitTableCellRenderer renderer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.renderer = new ZeitTableCellRenderer();
    }

    /** */
    public void testSetValue() {
        assertEquals("", this.renderer.getText());
        Timer t = new Timer(1);
        this.renderer.setValue(t);
        assertEquals(UhrHelper.format(t.getRestzeit()), this.renderer.getText());
        t.setRestzeit(123);
        this.renderer.setValue(t);
        assertEquals(UhrHelper.format(t.getRestzeit()), this.renderer.getText());
    }
}
