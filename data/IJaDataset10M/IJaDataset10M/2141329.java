package org.jscroll.widgets;

/**
 * This interface exposes the accessor and mutator (getter and setter) methods
 * required to get and set the internal frame associated with an implementing class.
 * Used by {@link org.jscroll.widgets.DesktopListener DesktopListener}
 * to abstract access to the menu and toggle buttons that implement this interface.
 *
 * @author <a href="mailto:tessier@gabinternet.com">Tom Tessier</a>
 * @version 1.0  2-Aug-2001
 */
public interface FrameAccessorInterface {

    /**
     *  returns the associated frame
     *
     * @return the JScrollInternalFrame associated with the object
     */
    JScrollInternalFrame getAssociatedFrame();

    /**
     *  sets the associated frame
     *
     * @param associatedFrame the JScrollInternalFrame to associate with
     *    the object
     */
    void setAssociatedFrame(JScrollInternalFrame associatedFrame);
}
