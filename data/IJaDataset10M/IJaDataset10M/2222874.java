package org.piccolo2d.extras.swt;

import org.piccolo2d.PNode;
import org.piccolo2d.event.PInputEventListener;
import org.piccolo2d.extras.swt.PSWTHandle;
import org.piccolo2d.extras.util.PBoundsLocator;
import org.piccolo2d.extras.util.PLocator;
import junit.framework.TestCase;

/**
 * Unit test for PSWTHandle.
 */
public class PSWTHandleTest extends TestCase {

    private PNode node;

    private PSWTHandle handle;

    private PBoundsLocator locator;

    public void setUp() throws Exception {
        node = new PNode();
        locator = PBoundsLocator.createEastLocator(node);
        handle = new PSWTHandle(locator);
        node.setBounds(0, 0, 100, 100);
        node.addChild(handle);
    }

    public void testDefaultsAreCorrect() {
        assertEquals(PSWTHandle.DEFAULT_COLOR, handle.getPaint());
        assertEquals(PSWTHandle.DEFAULT_HANDLE_SIZE + 2, handle.getHeight(), Float.MIN_VALUE);
    }

    public void testLocatorPersists() {
        assertSame(locator, handle.getLocator());
        PLocator newLocator = PBoundsLocator.createWestLocator(node);
        handle.setLocator(newLocator);
        assertSame(newLocator, handle.getLocator());
    }

    public void testHandleHasDragHandlerInstalled() {
        PInputEventListener dragHandler = handle.getHandleDraggerHandler();
        assertNotNull(dragHandler);
        PInputEventListener[] installedListeners = handle.getInputEventListeners();
        assertEquals(1, installedListeners.length);
        assertSame(dragHandler, installedListeners[0]);
    }

    public void testChangingParentDoesNotChangeLocatorNode() {
        handle.relocateHandle();
        PNode newParent = new PNode();
        newParent.setBounds(50, 50, 100, 100);
        final double originalX = handle.getX();
        handle.setParent(newParent);
        final double newX = handle.getX();
        assertEquals(newX, originalX, Double.MIN_VALUE);
    }
}
