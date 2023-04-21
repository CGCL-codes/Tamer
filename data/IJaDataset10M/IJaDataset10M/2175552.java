package com.jlect.swebing.renderers.client;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Collection for {@link PopupListener}
 *
 * @author Sergey Kozmin
 * @since 27.11.2007 10:29:41
 */
public class PopupListenerCollection extends ArrayList {

    /**
     * Fires a popup event to listeners
     *
     * @param visible <code>true</code> if the popup became visible, <code>false</code> otherwise
     *
     */
    public void firePopupVisibilityChanged(boolean visible) {
        for (Iterator it = iterator(); it.hasNext(); ) {
            PopupListener listener = (PopupListener) it.next();
            listener.popupBecomeVisible(visible);
        }
    }
}
