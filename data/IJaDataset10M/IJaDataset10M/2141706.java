package de.fhg.igd.semoa.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JViewport;

/**
 * A <code>JDesktopPane</code> that sets its preferred size according to 
 * the size requirements of all its internal frames. If embedded in a 
 * <code>JScrollPane</code> all maximized internal frames will be set to match 
 * the bounds of the <code>JScrollPane</code>s viewport.
 *
 * @author Dennis Bartussek <dennis.bartussek@igd.fhg.de>
 * @version $Id: VirtualDesktopPane.java 1913 2007-08-08 02:41:53Z jpeters $
 *
 * @see ScrollDesktopPane
 */
public class VirtualDesktopPane extends JDesktopPane {

    /**
     * Creates a new <code>VirtualDesktopPane</code>.
     */
    public VirtualDesktopPane() {
        setDesktopManager(new DefaultDesktopManager() {

            public void maximizeFrame(JInternalFrame frame) {
                super.maximizeFrame(frame);
                revalidate();
            }

            public void minimizeFrame(JInternalFrame frame) {
                super.minimizeFrame(frame);
                revalidate();
            }

            public void endResizingFrame(JComponent frame) {
                super.endResizingFrame(frame);
                revalidate();
            }

            public void endDraggingFrame(JComponent frame) {
                super.endDraggingFrame(frame);
                frame.repaint();
                revalidate();
            }

            public void iconifyFrame(JInternalFrame frame) {
                super.iconifyFrame(frame);
                revalidate();
            }
        });
    }

    /**
     * If this <code>VirtualDesktopPane</code> is embedded in a 
     * <code>JScrollPane</code>, this method returns the bounds of the 
     * <code>JScrollPane</code>s viewport, otherwise the bounds
     * of this <code>VirtualDesktopPane</code> are returned.
     */
    public Rectangle getBounds() {
        Container parent = getParent();
        Rectangle bounds;
        if (parent instanceof JViewport) {
            bounds = parent.getBounds();
        } else {
            bounds = super.getBounds();
        }
        return bounds;
    }

    /**
     * Computes the preferred size according to the size requirements of all 
     * internal frames and returns it using a <code>Dimension</code> object.
     *
     * @return the preferred size as a <code>Dimension</code> object
     */
    public Dimension getPreferredSize() {
        JInternalFrame allFrames[] = getAllFrames();
        Rectangle frameRect;
        int virtualHeight = 0;
        int virtualWidth = 0;
        for (int index = 0; index < allFrames.length; index++) {
            frameRect = allFrames[index].getBounds();
            if (frameRect.getX() + frameRect.getWidth() > virtualWidth) {
                virtualWidth = (int) (frameRect.getX() + frameRect.getWidth());
            }
            if (frameRect.getY() + frameRect.getHeight() > virtualHeight) {
                virtualHeight = (int) (frameRect.getY() + frameRect.getHeight());
            }
        }
        return new Dimension(virtualWidth, virtualHeight);
    }

    public void remove(Component comp) {
        super.remove(comp);
        revalidate();
    }
}
