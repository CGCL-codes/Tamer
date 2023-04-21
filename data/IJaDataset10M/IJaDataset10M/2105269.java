package org.modsl.core.render;

import static org.junit.Assert.assertTrue;
import java.awt.Font;
import org.junit.Test;
import org.modsl.core.AbstractModSLTest;

public class HeadlessCanvasTest extends AbstractModSLTest {

    @Test
    public void fontMetrics() {
        assertTrue(HeadlessCanvas.getMetrics("Serif", 12, Font.PLAIN).stringWidth("test") > 17);
        assertTrue(HeadlessCanvas.getMetrics("Serif", 12, Font.BOLD).stringWidth("test") > 18);
    }
}
