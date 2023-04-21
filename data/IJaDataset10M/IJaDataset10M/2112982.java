package org.xnap.commons.gui.table;

import junit.framework.TestCase;

public class StringCellRendererTest extends TestCase {

    public void testSetValue() {
        StringCellRenderer r = new StringCellRenderer();
        r.setValue("tooltiptext");
        assertEquals("tooltiptext", r.getText());
        assertEquals("tooltiptext", r.getToolTipText());
        r.setValue(null);
        assertEquals("", r.getText());
        assertEquals(null, r.getToolTipText());
    }
}
