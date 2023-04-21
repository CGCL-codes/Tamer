package com.google.gwt.user.client.ui;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A helper class for implementers of the SourcesPopupEvents interface. This
 * subclass of {@linkArrayList} assumes that all objects added to it will be of
 * type {@link com.google.gwt.user.client.ui.PopupListener}.
 */
public class PopupListenerCollection extends ArrayList {

    /**
   * Fires a popup closed event to all listeners.
   * 
   * @param sender the widget sending the event.
   * @param autoClosed <code>true</code> if the popup was automatically
   *          closed; <code>false</code> if it was closed programmatically.
   */
    public void firePopupClosed(PopupPanel sender, boolean autoClosed) {
        for (Iterator it = iterator(); it.hasNext(); ) {
            PopupListener listener = (PopupListener) it.next();
            listener.onPopupClosed(sender, autoClosed);
        }
    }
}
