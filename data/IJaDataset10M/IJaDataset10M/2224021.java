package com.xenoage.zong.renderer.targets;

import static org.junit.Assert.*;
import com.xenoage.util.math.Rectangle2i;
import com.xenoage.util.math.Size2i;
import com.xenoage.zong.renderer.targets.GLPageRenderer;
import org.junit.Test;

/**
 * Test cases for a GLPageRenderer.
 *
 * @author Andreas Wenger
 */
public class GLPageRendererTest {

    @Test
    public void testIsRectVisible() {
        GLPageRenderer renderer = GLPageRenderer.getInstance();
        Size2i windowSize = new Size2i(800, 600);
        assertTrue(renderer.isRectVisible(windowSize, new Rectangle2i(100, 100, 100, 100)));
        assertTrue(renderer.isRectVisible(windowSize, new Rectangle2i(-99, -99, 100, 100)));
        assertTrue(renderer.isRectVisible(windowSize, new Rectangle2i(300, -99, 100, 100)));
        assertTrue(renderer.isRectVisible(windowSize, new Rectangle2i(799, -99, 100, 100)));
        assertTrue(renderer.isRectVisible(windowSize, new Rectangle2i(799, 300, 100, 100)));
        assertTrue(renderer.isRectVisible(windowSize, new Rectangle2i(799, 599, 100, 100)));
        assertTrue(renderer.isRectVisible(windowSize, new Rectangle2i(300, 599, 100, 100)));
        assertTrue(renderer.isRectVisible(windowSize, new Rectangle2i(-99, 599, 100, 100)));
        assertTrue(renderer.isRectVisible(windowSize, new Rectangle2i(-99, 300, 100, 100)));
        assertFalse(renderer.isRectVisible(windowSize, new Rectangle2i(-101, -101, 100, 100)));
        assertFalse(renderer.isRectVisible(windowSize, new Rectangle2i(300, -101, 100, 100)));
        assertFalse(renderer.isRectVisible(windowSize, new Rectangle2i(801, -101, 100, 100)));
        assertFalse(renderer.isRectVisible(windowSize, new Rectangle2i(801, 300, 100, 100)));
        assertFalse(renderer.isRectVisible(windowSize, new Rectangle2i(801, 601, 100, 100)));
        assertFalse(renderer.isRectVisible(windowSize, new Rectangle2i(300, 601, 100, 100)));
        assertFalse(renderer.isRectVisible(windowSize, new Rectangle2i(-101, 601, 100, 100)));
        assertFalse(renderer.isRectVisible(windowSize, new Rectangle2i(-101, 300, 100, 100)));
    }
}
