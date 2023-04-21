package org.jrichclient.richdock.dockable;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.jrichclient.richdock.UnitTestUtils.*;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import junit.framework.JUnit4TestAdapter;
import org.jrichclient.richdock.DockableTester;
import org.jrichclient.richdock.icons.ImageResources;
import org.junit.*;

public class TestBasicDockable extends DockableTester {

    private final BasicDockable dockable;

    public TestBasicDockable() {
        this(new BasicDockable());
    }

    public TestBasicDockable(BasicDockable dockable) {
        super(dockable);
        this.dockable = dockable;
    }

    @Test
    public void testConstructor1() {
        assertNull(dockable.getContent());
        assertEquals("", dockable.getTitle());
        assertNull(dockable.getIconFile());
        assertNull(dockable.getToolTipText());
        assertNull(dockable.getPopupMenu());
        assertTrue(dockable.isDragable());
        assertTrue(dockable.isFloatable());
        assertNull(dockable.getDockingPort());
        assertNotNull(dockable.getComponent());
        assertTrue(dockable.canClose());
        assertFalse(dockable.isDisposed());
    }

    @Test
    public void testConstructor2() {
        Component content = new JButton("Test Content");
        BasicDockable dockable = new BasicDockable(content);
        assertSame(content, dockable.getContent());
        assertEquals("", dockable.getTitle());
        assertNull(dockable.getIconFile());
        assertNull(dockable.getToolTipText());
        assertNull(dockable.getPopupMenu());
        assertTrue(dockable.isDragable());
        assertTrue(dockable.isFloatable());
        assertNull(dockable.getDockingPort());
        assertNotNull(dockable.getComponent());
        assertTrue(dockable.canClose());
        assertFalse(dockable.isDisposed());
        testDispose(dockable);
    }

    @Test
    public void testConstructor3() {
        Component content = new JButton("Test Content");
        String title = "Test Title";
        BasicDockable dockable = new BasicDockable(content, title);
        assertSame(content, dockable.getContent());
        assertSame(title, dockable.getTitle());
        assertNull(dockable.getIconFile());
        assertNull(dockable.getToolTipText());
        assertNull(dockable.getPopupMenu());
        assertTrue(dockable.isDragable());
        assertTrue(dockable.isFloatable());
        assertNull(dockable.getDockingPort());
        assertNotNull(dockable.getComponent());
        assertTrue(dockable.canClose());
        assertFalse(dockable.isDisposed());
        testDispose(dockable);
    }

    @Test
    public void testConstructor4() {
        Component content = new JButton("Test Content");
        String title = "Test Title";
        String iconFile = ImageResources.GLOBE_IMAGE;
        BasicDockable dockable = new BasicDockable(content, title, iconFile);
        assertSame(content, dockable.getContent());
        assertSame(title, dockable.getTitle());
        assertSame(iconFile, dockable.getIconFile());
        assertNull(dockable.getToolTipText());
        assertNull(dockable.getPopupMenu());
        assertTrue(dockable.isDragable());
        assertTrue(dockable.isFloatable());
        assertNull(dockable.getDockingPort());
        assertNotNull(dockable.getComponent());
        assertTrue(dockable.canClose());
        assertFalse(dockable.isDisposed());
        testDispose(dockable);
    }

    @Test
    public void testConstructor5() {
        Component content = new JButton("Test Content");
        String title = "Test Title";
        String iconFile = ImageResources.GLOBE_IMAGE;
        String toolTipText = "Test ToolTipText";
        BasicDockable dockable = new BasicDockable(content, title, iconFile, toolTipText);
        assertSame(content, dockable.getContent());
        assertSame(title, dockable.getTitle());
        assertSame(iconFile, dockable.getIconFile());
        assertSame(toolTipText, dockable.getToolTipText());
        assertNull(dockable.getPopupMenu());
        assertTrue(dockable.isDragable());
        assertTrue(dockable.isFloatable());
        assertNull(dockable.getDockingPort());
        assertNotNull(dockable.getComponent());
        assertTrue(dockable.canClose());
        assertFalse(dockable.isDisposed());
        testDispose(dockable);
    }

    @Test
    public void testConstructor6() {
        Component content = new JButton("Test Content");
        String title = "Test Title";
        String iconFile = ImageResources.GLOBE_IMAGE;
        String toolTipText = "Test ToolTipText";
        JPopupMenu popupMenu = new JPopupMenu();
        BasicDockable dockable = new BasicDockable(content, title, iconFile, toolTipText, popupMenu);
        assertSame(content, dockable.getContent());
        assertSame(title, dockable.getTitle());
        assertSame(iconFile, dockable.getIconFile());
        assertSame(toolTipText, dockable.getToolTipText());
        assertSame(popupMenu, dockable.getPopupMenu());
        assertTrue(dockable.isDragable());
        assertTrue(dockable.isFloatable());
        assertNull(dockable.getDockingPort());
        assertNotNull(dockable.getComponent());
        assertTrue(dockable.canClose());
        assertFalse(dockable.isDisposed());
        testDispose(dockable);
    }

    @Test
    public void testClone() {
        try {
            BasicDockable original = new BasicDockable();
            original.setContent(new JButton("Test Button"));
            original.setTitle("Test Title");
            original.setIconFile(ImageResources.GLOBE_IMAGE);
            original.setToolTipText("Test ToolTip Text");
            original.setPopupMenu(new JPopupMenu());
            original.setSize(20, 50);
            BasicDockable copy = original.clone();
            assertTrue(copy.getContent() instanceof JButton);
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
    public void testContent() {
        Component oldContent = dockable.getContent();
        Component newContent = new JButton("Test Content");
        PropertyChangeListener listener = createStrictMock(PropertyChangeListener.class);
        dockable.addPropertyChangeListener(BasicDockable.PROPERTYNAME_CONTENT, listener);
        propertyChange(listener, new PropertyChangeEvent(dockable, BasicDockable.PROPERTYNAME_CONTENT, oldContent, newContent));
        propertyChange(listener, new PropertyChangeEvent(dockable, BasicDockable.PROPERTYNAME_CONTENT, newContent, oldContent));
        replay(listener);
        dockable.setContent(newContent);
        assertSame(newContent, dockable.getContent());
        dockable.setContent(oldContent);
        assertSame(oldContent, dockable.getContent());
        dockable.removePropertyChangeListener(BasicDockable.PROPERTYNAME_CONTENT, listener);
        verify(listener);
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestBasicDockable.class);
    }
}
