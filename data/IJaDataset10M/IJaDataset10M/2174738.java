package org.start;

import org.swing.JModalInternalFrame;
import javax.swing.*;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: babitsky.viacheslav
 * Date: 21.01.2008
 * Time: 13:10:28
 */
public class MDIDesktopPane extends JDesktopPane {

    private MDIDesktopManager mdiDesktopManager;

    public MDIDesktopManager getMDIDesktopManager() {
        return mdiDesktopManager;
    }

    /**
     * Sets the <code>DesktopManger</code> that will handle
     * desktop-specific UI actions.
     *
     * @param d the <code>DesktopManager</code> to use
     * @beaninfo bound: true
     * description: Desktop manager to handle the internal frames in the
     * desktop pane.
     */
    public void setMDIDesktopManager(MDIDesktopManager d) {
        mdiDesktopManager = d;
        setDesktopManager(d);
    }

    /**
     * Returns all <code>JModalInternalFrame</code> currently displayed in the
     * desktop. Returns iconified frames as well as expanded frames.
     *
     * @return an array of <code>JModalInternalFrame</code> objects
     */
    public JModalInternalFrame[] getAllModalFrames() {
        int i, count;
        JModalInternalFrame[] results;
        count = getComponentCount();
        Vector<Object> vResults = new Vector<Object>(10);
        Object next, tmp;
        for (i = 0; i < count; i++) {
            next = getComponent(i);
            if (next instanceof JModalInternalFrame) vResults.addElement(next); else if (next instanceof JModalInternalFrame.JDesktopIcon) {
                tmp = ((JModalInternalFrame.JDesktopIcon) next).getInternalFrame();
                if (tmp != null) vResults.addElement(tmp);
            }
        }
        results = new JModalInternalFrame[vResults.size()];
        vResults.copyInto(results);
        return results;
    }

    /**
     * Returns the currently active <code>JModalInternalFrame</code>
     * in this <code>JDesktopPane</code>, or <code>null</code>
     * if no <code>JModalInternalFrame</code> is currently active.
     *
     * @return the currently active <code>JModalInternalFrame</code> or
     *         <code>null</code>
     * @since 1.3
     */
    public JModalInternalFrame getSelectedModalFrame() {
        return (JModalInternalFrame) super.getSelectedFrame();
    }

    /**
     * Returns all <code>JModalInternalFrames</code> currently displayed in the
     * specified layer of the desktop. Returns iconified frames as well
     * expanded frames.
     *
     * @param layer an int specifying the desktop layer
     * @return an array of <code>JModalInternalFrame</code> objects
     * @see JLayeredPane
     */
    public JModalInternalFrame[] getAllFramesInLayer(int layer) {
        int i, count;
        JModalInternalFrame[] results;
        Vector<JModalInternalFrame> vResults = new Vector<JModalInternalFrame>(10);
        Object next;
        JModalInternalFrame tmp;
        count = getComponentCount();
        for (i = 0; i < count; i++) {
            next = getComponent(i);
            if (next instanceof JModalInternalFrame) {
                final JModalInternalFrame internalFrame = (JModalInternalFrame) next;
                if (internalFrame.getLayer() == layer) vResults.addElement(internalFrame);
            } else if (next instanceof JModalInternalFrame.JDesktopIcon) {
                tmp = (JModalInternalFrame) ((JModalInternalFrame.JDesktopIcon) next).getInternalFrame();
                if (tmp != null && tmp.getLayer() == layer) vResults.addElement(tmp);
            }
        }
        results = new JModalInternalFrame[vResults.size()];
        vResults.copyInto(results);
        return results;
    }

    /**
     * Selects the next <code>JModalInternalFrame</code> in this desktop pane.
     *
     * @param forward a boolean indicating which direction to select in;
     *                <code>true</code> for forward, <code>false</code> for
     *                backward
     * @return the JModalInternalFrame that was selected or <code>null</code>
     *         if nothing was selected
     * @since 1.6
     */
    public JModalInternalFrame selectModalFrame(boolean forward) {
        return (JModalInternalFrame) selectFrame(forward);
    }
}
