package com.jgoodies.plaf.common;

import java.lang.reflect.Method;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.Popup;

/**
 * A helper class used to implements drop shadows for popup menus.  
 * Hooked into the creation of either the light weight or heavy weight 
 * <code>JPopupMenu</code> container. Either removes its opaqueness 
 * or creates a background snapshot to create the illusion that menus 
 * can shadow even parts of the screen outside the menu's window.  
 * Unfortunately this illusion isn't perfect.<p>
 * 
 * We need access to <code>javax.swing.Popup#getComponent()</code> and require
 * <code>java.lang.reflect.AccessibleObject#ACCESS_PERMISSION</code> to do so.
 * If this code is executed with some security manager set, it will fail
 * silently and drop shadow support is inactive.
 * 
 * @author Stefan Matthias Aust
 * @author Karsten Lentzsch
 * @version $Revision: 1.1.1.1 $
 * 
 * @see com.jgoodies.plaf.common.ShadowPopupBorder
 */
public final class ShadowPopupMenuUtils {

    /**
     * Refers to <code>javax.swing.Popup#getComponent</code>
     * if the method lookup was permitted by the runtime.
     * If <code>null</code> <code>#getPopupWithShadow</code> just returns
     * the unmodified popup.
     */
    private static Method getComponentMethod;

    /**
     * Tries to access {@link javax.swing.Popup#getComponent()}. 
     */
    static {
        try {
            getComponentMethod = Popup.class.getDeclaredMethod("getComponent", null);
            getComponentMethod.setAccessible(true);
        } catch (Exception e) {
            ShadowPopupBorder.setActive(false);
        }
    }

    /**
	 * Returns the Popup that will be responsible for displaying the JPopupMenu.
	 * Overwritten to fix the opaqueness of the component in the case of light weight
	 * menus and to make a background snapshot to simulate the shadows in the case of
	 * heavy weight menus. 
	 */
    public static void setTransparent(JPopupMenu popupMenu, Popup popup) {
        if (getComponentMethod != null) {
            try {
                final Object component = getComponentMethod.invoke(popup, null);
                if (component instanceof JPanel) {
                    ((JPanel) component).setOpaque(false);
                    popupMenu.setOpaque(false);
                    ShadowPopupBorder.clearSnapshot();
                } else if (component instanceof JWindow) {
                    ShadowPopupBorder.makeSnapshot((JWindow) component);
                }
            } catch (Exception e) {
                getComponentMethod = null;
                ShadowPopupBorder.setActive(false);
            }
        }
    }
}
