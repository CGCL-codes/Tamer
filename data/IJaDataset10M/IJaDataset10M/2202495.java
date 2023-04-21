package org.piccolo2d.extras.swt;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

/**
 * Key event overridden to wrap an SWT KeyEvent as a swing KeyEvent.
 * 
 * @author Lance Good
 */
public class PSWTKeyEvent extends KeyEvent {

    private static final long serialVersionUID = 1L;

    private static Component fakeSrc = new Component() {
    };

    private org.eclipse.swt.events.KeyEvent swtEvent;

    /**
     * Creates an object that wraps a SWT Key event. Making it queriable from
     * Piccolo2d as though it were a Swing one.
     * 
     * @param ke key event object
     * @param eventType type of key event
     */
    public PSWTKeyEvent(final org.eclipse.swt.events.KeyEvent ke, final int eventType) {
        super(fakeSrc, eventType, ke.time, 0, ke.keyCode, ke.character, KeyEvent.KEY_LOCATION_STANDARD);
        swtEvent = ke;
    }

    /** {@inheritDoc} */
    public Object getSource() {
        return swtEvent.getSource();
    }

    /** {@inheritDoc} */
    public boolean isShiftDown() {
        return (swtEvent.stateMask & SWT.SHIFT) != 0;
    }

    /** {@inheritDoc} */
    public boolean isControlDown() {
        return (swtEvent.stateMask & SWT.CONTROL) != 0;
    }

    /** {@inheritDoc} */
    public boolean isAltDown() {
        return (swtEvent.stateMask & SWT.ALT) != 0;
    }

    /** {@inheritDoc} */
    public int getModifiers() {
        int modifiers = 0;
        if (swtEvent != null) {
            if ((swtEvent.stateMask & SWT.ALT) != 0) {
                modifiers = modifiers | InputEvent.ALT_MASK;
            }
            if ((swtEvent.stateMask & SWT.CONTROL) != 0) {
                modifiers = modifiers | InputEvent.CTRL_MASK;
            }
            if ((swtEvent.stateMask & SWT.SHIFT) != 0) {
                modifiers = modifiers | InputEvent.SHIFT_MASK;
            }
        }
        return modifiers;
    }

    /** {@inheritDoc} */
    public int getModifiersEx() {
        int modifiers = 0;
        if (swtEvent != null) {
            if ((swtEvent.stateMask & SWT.ALT) != 0) {
                modifiers = modifiers | InputEvent.ALT_DOWN_MASK;
            }
            if ((swtEvent.stateMask & SWT.CONTROL) != 0) {
                modifiers = modifiers | InputEvent.CTRL_DOWN_MASK;
            }
            if ((swtEvent.stateMask & SWT.SHIFT) != 0) {
                modifiers = modifiers | InputEvent.SHIFT_DOWN_MASK;
            }
        }
        return modifiers;
    }

    /** {@inheritDoc} */
    public boolean isActionKey() {
        return false;
    }

    /**
     * Returns the widget from which the event was emitted.
     * 
     * @return source widget
     */
    public Widget getWidget() {
        return swtEvent.widget;
    }

    /**
     * Return the display on which the interaction occurred.
     * 
     * @return display on which the interaction occurred
     */
    public Display getDisplay() {
        return swtEvent.display;
    }

    /**
     * Return the associated SWT data for the event.
     * 
     * @return data associated to the SWT event
     */
    public Object getData() {
        return swtEvent.data;
    }
}
