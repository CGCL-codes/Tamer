package fedora.swing.mdi;

import javax.swing.*;
import java.awt.*;
import java.beans.*;

/**
 * <p><b>Title:</b> SortHeaderRenderer.java</p>
 * <p><b>Description:</b>
 * An extension of WDesktopPane that supports often used MDI functionality. This
 * class also handles setting scroll bars for when windows move too far to the left or
 * bottom, providing the MDIDesktopPane is in a ScrollPane.
 * <p>
 *
 * -----------------------------------------------------------------------------
 *
 * Portions created by Gerald Nunn are Copyright &copy;
 * Gerald Nunn, originally made available at
 * http://www.javaworld.com/javaworld/jw-05-2001/jw-0525-mdi.html</p>
 *
 * -----------------------------------------------------------------------------
 *
 * @author Gerald Nunn, cwilper@cs.cornell.edu
 * @version $Id: MDIDesktopPane.java 5162 2006-10-25 00:49:06Z eddie $
 */
public class MDIDesktopPane extends JDesktopPane {

    private static final long serialVersionUID = 1L;

    private static int FRAME_OFFSET = 20;

    private MDIDesktopManager manager;

    public MDIDesktopPane() {
        manager = new MDIDesktopManager(this);
        setDesktopManager(manager);
        setDragMode(JDesktopPane.LIVE_DRAG_MODE);
    }

    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        checkDesktopSize();
    }

    public Component add(JInternalFrame frame) {
        JInternalFrame[] array = getAllFrames();
        Point p;
        int w;
        int h;
        Component retval = super.add(frame);
        checkDesktopSize();
        if (array.length > 0) {
            p = array[0].getLocation();
            p.x = p.x + FRAME_OFFSET;
            p.y = p.y + FRAME_OFFSET;
        } else {
            p = new Point(0, 0);
        }
        frame.setLocation(p.x, p.y);
        moveToFront(frame);
        frame.setVisible(true);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {
            frame.toBack();
        }
        return retval;
    }

    public void remove(Component c) {
        super.remove(c);
        checkDesktopSize();
    }

    /**
     * Cascade all internal frames, un-iconfying any minimized first
     */
    public void cascadeFrames() {
        restoreFrames();
        int x = 0;
        int y = 0;
        JInternalFrame allFrames[] = getAllFrames();
        manager.setNormalSize();
        int frameHeight = (getBounds().height - 5) - allFrames.length * FRAME_OFFSET;
        int frameWidth = (getBounds().width - 5) - allFrames.length * FRAME_OFFSET;
        for (int i = allFrames.length - 1; i >= 0; i--) {
            allFrames[i].setSize(frameWidth, frameHeight);
            allFrames[i].setLocation(x, y);
            x = x + FRAME_OFFSET;
            y = y + FRAME_OFFSET;
        }
    }

    /**
     * Tile all internal frames, un-iconifying any minimized first
     */
    public void tileFrames() {
        restoreFrames();
        java.awt.Component allFrames[] = getAllFrames();
        manager.setNormalSize();
        int frameHeight = getBounds().height / allFrames.length;
        int y = 0;
        for (int i = 0; i < allFrames.length; i++) {
            allFrames[i].setSize(getBounds().width, frameHeight);
            allFrames[i].setLocation(0, y);
            y = y + frameHeight;
        }
    }

    public void minimizeFrames() {
        JInternalFrame[] array = getAllFrames();
        for (int i = 0; i < array.length; i++) {
            try {
                array[i].setIcon(true);
            } catch (PropertyVetoException pve) {
            }
        }
    }

    public void restoreFrames() {
        JInternalFrame[] array = getAllFrames();
        for (int i = 0; i < array.length; i++) {
            try {
                array[i].setIcon(false);
            } catch (PropertyVetoException pve) {
            }
        }
    }

    public int deIconifiedFrames() {
        int c = 0;
        JInternalFrame[] array = getAllFrames();
        for (int i = 0; i < array.length; i++) {
            if (!array[i].isIcon()) c++;
        }
        return c;
    }

    public int iconifiedFrames() {
        int c = 0;
        JInternalFrame[] array = getAllFrames();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isIcon()) c++;
        }
        return c;
    }

    /**
     * Sets all component size properties ( maximum, minimum, preferred)
     * to the given dimension.
     */
    public void setAllSize(Dimension d) {
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);
    }

    /**
     * Sets all component size properties ( maximum, minimum, preferred)
     * to the given width and height.
     */
    public void setAllSize(int width, int height) {
        setAllSize(new Dimension(width, height));
    }

    private void checkDesktopSize() {
        if (getParent() != null && isVisible()) manager.resizeDesktop();
    }
}

/**
 * Private class used to replace the standard DesktopManager for JDesktopPane.
 * Used to provide scrollbar functionality.
 */
class MDIDesktopManager extends DefaultDesktopManager {

    private static final long serialVersionUID = 1L;

    private MDIDesktopPane desktop;

    public MDIDesktopManager(MDIDesktopPane desktop) {
        this.desktop = desktop;
    }

    public void endResizingFrame(JComponent f) {
        super.endResizingFrame(f);
        resizeDesktop();
    }

    public void endDraggingFrame(JComponent f) {
        super.endDraggingFrame(f);
        resizeDesktop();
    }

    public void setNormalSize() {
        JScrollPane scrollPane = getScrollPane();
        int x = 0;
        int y = 0;
        Insets scrollInsets = getScrollPaneInsets();
        if (scrollPane != null) {
            Dimension d = scrollPane.getVisibleRect().getSize();
            if (scrollPane.getBorder() != null) {
                d.setSize(d.getWidth() - scrollInsets.left - scrollInsets.right, d.getHeight() - scrollInsets.top - scrollInsets.bottom);
            }
            d.setSize(d.getWidth() - 20, d.getHeight() - 20);
            desktop.setAllSize(x, y);
            scrollPane.invalidate();
            scrollPane.validate();
        }
    }

    private Insets getScrollPaneInsets() {
        JScrollPane scrollPane = getScrollPane();
        if (scrollPane == null) return new Insets(0, 0, 0, 0); else return getScrollPane().getBorder().getBorderInsets(scrollPane);
    }

    private JScrollPane getScrollPane() {
        if (desktop.getParent() instanceof JViewport) {
            JViewport viewPort = (JViewport) desktop.getParent();
            if (viewPort.getParent() instanceof JScrollPane) return (JScrollPane) viewPort.getParent();
        }
        return null;
    }

    protected void resizeDesktop() {
        int x = 0;
        int y = 0;
        JScrollPane scrollPane = getScrollPane();
        Insets scrollInsets = getScrollPaneInsets();
        if (scrollPane != null) {
            JInternalFrame allFrames[] = desktop.getAllFrames();
            for (int i = 0; i < allFrames.length; i++) {
                if (allFrames[i].getX() + allFrames[i].getWidth() > x) {
                    x = allFrames[i].getX() + allFrames[i].getWidth();
                }
                if (allFrames[i].getY() + allFrames[i].getHeight() > y) {
                    y = allFrames[i].getY() + allFrames[i].getHeight();
                }
            }
            Dimension d = scrollPane.getVisibleRect().getSize();
            if (scrollPane.getBorder() != null) {
                d.setSize(d.getWidth() - scrollInsets.left - scrollInsets.right, d.getHeight() - scrollInsets.top - scrollInsets.bottom);
            }
            if (x <= d.getWidth()) x = ((int) d.getWidth()) - 20;
            if (y <= d.getHeight()) y = ((int) d.getHeight()) - 20;
            desktop.setAllSize(x, y);
            scrollPane.invalidate();
            scrollPane.validate();
        }
    }
}
