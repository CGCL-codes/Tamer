package org.eclipse.swt.widgets;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.swt.events.HotkeyChangeListner;

/**
 * HotkeyText widget.
 * 
 * @author Dmitry Pelevin
 */
public abstract class HotkeyText extends Text {

    /** The listners. */
    private Set<HotkeyChangeListner> listners = new HashSet<HotkeyChangeListner>();

    /**
	 * Instantiates a new hotkey text.
	 * 
	 * @param parent
	 *            the parent
	 * @param style
	 *            the style
	 */
    public HotkeyText(final Composite parent, final int style) {
        super(parent, style);
    }

    /**
	 * Adds the hotkey change listener.
	 * 
	 * @param listener
	 *            the listener
	 */
    public final void addHotkeyChangeListener(final HotkeyChangeListner listener) {
        listners.add(listener);
    }

    /**
	 * Removes the hotkey change listener.
	 * 
	 * @param listener
	 *            the listener
	 */
    public final void removeHotkeyChangeListener(final HotkeyChangeListner listener) {
        listners.remove(listener);
    }

    /**
	 * Notify hotkey change listners.
	 * 
	 * @param keyCode
	 *            the key code
	 */
    protected void notifyHotkeyChangeListners(final int keyCode) {
        HotkeyChangedEvent e;
        e = new HotkeyChangedEvent(this);
        e.keyCode = keyCode;
        e.widget = this;
        for (HotkeyChangeListner hotkeyChageListner : listners) {
            hotkeyChageListner.hotkeyChanged(e);
        }
    }
}
