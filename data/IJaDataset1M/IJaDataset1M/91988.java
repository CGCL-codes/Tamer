package org.jrichclient.richdock.dockable;

import static org.easymock.EasyMock.*;
import static org.jrichclient.richdock.UnitTestUtils.propertyChange;
import static org.junit.Assert.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import junit.framework.JUnit4TestAdapter;
import org.jrichclient.richdock.DockableTester;
import org.jrichclient.richdock.icons.ImageResources;
import org.junit.*;

public class TestStatusBarDockable extends DockableTester {

    private final StatusBarDockable dockable;

    public TestStatusBarDockable() {
        this(new StatusBarDockable());
    }

    public TestStatusBarDockable(StatusBarDockable dockable) {
        super(dockable);
        this.dockable = dockable;
    }

    @Test
    public void testConstructor1() {
        assertEquals("", dockable.getTitle());
        assertNull(dockable.getIconFile());
        assertNull(dockable.getToolTipText());
        assertNull(dockable.getPopupMenu());
        assertTrue(dockable.isDragable());
        assertTrue(dockable.isFloatable());
        assertNull(dockable.getDockingPort());
        assertNotNull(dockable.getComponent());
        assertNull(dockable.getCornerIcon());
        assertTrue(dockable.canClose());
        assertFalse(dockable.isDisposed());
    }

    @Test
    public void testClone() {
        try {
            StatusBarDockable original = new StatusBarDockable();
            original.setTitle("Test Title");
            original.setIconFile(ImageResources.GLOBE_IMAGE);
            original.setToolTipText("Test ToolTip Text");
            original.setPopupMenu(new JPopupMenu());
            original.setSize(20, 50);
            StatusBarDockable copy = original.clone();
            assertEquals(original.getTitle(), copy.getTitle());
            assertEquals(original.getIconFile(), copy.getIconFile());
            assertEquals(original.getToolTipText(), copy.getToolTipText());
            assertTrue(copy.getPopupMenu() instanceof JPopupMenu);
            assertEquals(original.isDragable(), copy.isDragable());
            assertEquals(original.isFloatable(), copy.isFloatable());
            assertEquals(original.getDockingPort(), copy.getDockingPort());
            assertNotNull(copy.getComponent());
            assertFalse(copy.isDisposed());
            original.dispose();
            copy.dispose();
        } catch (CloneNotSupportedException ex) {
            fail("Clone not supported");
        }
    }

    @Test
    public void testCornerIcon() {
        Icon oldCornerIcon = dockable.getCornerIcon();
        Icon newCornerIcon = ImageResources.createIcon(ImageResources.RESIZE_IMAGE);
        PropertyChangeListener listener = createStrictMock(PropertyChangeListener.class);
        dockable.addPropertyChangeListener(StatusBarDockable.PROPERTYNAME_CORNER_ICON, listener);
        propertyChange(listener, new PropertyChangeEvent(dockable, StatusBarDockable.PROPERTYNAME_CORNER_ICON, oldCornerIcon, newCornerIcon));
        propertyChange(listener, new PropertyChangeEvent(dockable, StatusBarDockable.PROPERTYNAME_CORNER_ICON, newCornerIcon, oldCornerIcon));
        replay(listener);
        dockable.setCornerIcon(newCornerIcon);
        assertSame(newCornerIcon, dockable.getCornerIcon());
        dockable.setCornerIcon(oldCornerIcon);
        assertSame(oldCornerIcon, dockable.getCornerIcon());
        dockable.removePropertyChangeListener(StatusBarDockable.PROPERTYNAME_CORNER_ICON, listener);
        verify(listener);
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestStatusBarDockable.class);
    }
}
