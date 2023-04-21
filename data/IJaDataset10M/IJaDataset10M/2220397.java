package org.eclipse.swt.events;

import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.widgets.HotkeyChangedEvent;

/**
 * Notify about change of key value at HotkeyText widget.
 * 
 * @author Dmitry Pelevin
 */
public interface HotkeyChangeListner extends SWTEventListener {

    /**
	 * Hotkey changed.
	 * 
	 * @param event
	 *            the key code
	 */
    void hotkeyChanged(HotkeyChangedEvent event);
}
