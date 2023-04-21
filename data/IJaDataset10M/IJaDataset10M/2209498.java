package org.jnativehook;

import java.awt.Toolkit;
import java.util.EventObject;

/**
 * The root event class for all native-level input events.  Input events are 
 * delivered to listeners as they are received by the native source. There is 
 * no method for listeners or subclasses to prevent delivery of the event to 
 * the native system. There is no guarantee that the events will be received by 
 * Java before they are delivered natively.
 * <p/>
 * 
 * @author	Alexander Barker (<a href="mailto:alex@1stleg.com">alex@1stleg.com</a>)
 * @since	1.0
 * 
 * @see org.jnativehook.keyboard.NativeKeyListener
 * @see org.jnativehook.mouse.NativeMouseListener
 * @see org.jnativehook.mouse.NativeMouseMotionListener
 */
public class NativeInputEvent extends EventObject {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5581084516392038598L;

    /** The type of event. */
    private int id;

    /** The time the event occurred. */
    private long when;

    /** The modifier keys down during event. */
    private int modifiers;

    /** The Shift key modifier constant. */
    public static final int SHIFT_MASK = 1;

    /** The Ctrl key modifier constant. */
    public static final int CTRL_MASK = 2;

    /** The Meta key modifier constant. */
    public static final int META_MASK = 4;

    /** The Alt key modifier constant. */
    public static final int ALT_MASK = 8;

    /** The Button1 modifier constant. */
    public static final int BUTTON1_MASK = 16;

    /** The Button2 modifier constant. */
    public static final int BUTTON2_MASK = 32;

    /** The Button3 modifier constant. */
    public static final int BUTTON3_MASK = 64;

    /** The Button4 modifier constant. */
    public static final int BUTTON4_MASK = 128;

    /** The Button5 modifier constant. */
    public static final int BUTTON5_MASK = 256;

    /**
	 * Instantiates a new native input event.
	 *
	 * @param source The source of the event.
	 * @param id The type of event.
	 * @param when The timestamp for the event.
	 * @param modifiers the modifier keys down during event. 
	 * <code>NativeInputEvent</code> _MASK modifiers should be used as they are 
	 * not compatible with the extended _DOWN_MASK or the old _MASK 
	 * <code>InputEvent</code> modifiers.
	 */
    public NativeInputEvent(GlobalScreen source, int id, long when, int modifiers) {
        super(source);
        this.id = id;
        this.when = when;
        this.modifiers = modifiers;
    }

    /**
	 * Gets the event type.
	 *
	 * @return the event type
	 */
    public int getID() {
        return id;
    }

    /**
	 * Gets the timestamp for when this event occurred.
	 *
	 * @return the timestamp in milliseconds
	 */
    public long getWhen() {
        return when;
    }

    /**
	 * Gets the modifier flags for this event.
	 *
	 * @return the modifier flags
	 */
    public int getModifiers() {
        return this.modifiers;
    }

    /**
	 * Sets the modifier flags for this event.
	 *
	 * @param modifiers the new modifier flags
	 */
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    /**
	 * Gets a <code>String</code> describing the modifier flags, such as 
	 * "Button1", or "Ctrl+Alt". These strings can be localized by changing the 
	 * awt.properties file.
	 *
	 * @param modifiers a modifier mask describing the modifier keys and mouse 
	 * buttons of an event.
	 * @return the modifier mask's textual representation
	 */
    public static String getModifiersText(int modifiers) {
        StringBuilder param = new StringBuilder(255);
        if ((modifiers & NativeInputEvent.SHIFT_MASK) != 0) {
            param.append(Toolkit.getProperty("AWT.shift", "Shift"));
            param.append('+');
        }
        if ((modifiers & NativeInputEvent.CTRL_MASK) != 0) {
            param.append(Toolkit.getProperty("AWT.control", "Ctrl"));
            param.append('+');
        }
        if ((modifiers & NativeInputEvent.META_MASK) != 0) {
            param.append(Toolkit.getProperty("AWT.meta", "Meta"));
            param.append('+');
        }
        if ((modifiers & NativeInputEvent.ALT_MASK) != 0) {
            param.append(Toolkit.getProperty("AWT.alt", "Alt"));
            param.append('+');
        }
        if ((modifiers & NativeInputEvent.BUTTON1_MASK) != 0) {
            param.append(Toolkit.getProperty("AWT.button1", "Button1"));
            param.append('+');
        }
        if ((modifiers & NativeInputEvent.BUTTON2_MASK) != 0) {
            param.append(Toolkit.getProperty("AWT.button2", "Button2"));
            param.append('+');
        }
        if ((modifiers & NativeInputEvent.BUTTON3_MASK) != 0) {
            param.append(Toolkit.getProperty("AWT.button3", "Button3"));
            param.append('+');
        }
        if ((modifiers & NativeInputEvent.BUTTON4_MASK) != 0) {
            param.append(Toolkit.getProperty("AWT.button4", "Button4"));
            param.append('+');
        }
        if ((modifiers & NativeInputEvent.BUTTON5_MASK) != 0) {
            param.append(Toolkit.getProperty("AWT.button5", "Button5"));
            param.append('+');
        }
        if (param.length() > 0) {
            param.deleteCharAt(param.length() - 1);
        }
        return param.toString();
    }

    /**
	 * Gets a <code>String</code> representation of this event. This method is 
	 * useful for event-logging and debugging.
	 *
	 * @return a string identifying the event and its attributes
	 */
    public String paramString() {
        StringBuilder param = new StringBuilder(255);
        param.append("id=");
        param.append(getID());
        param.append(',');
        param.append("when=");
        param.append(getWhen());
        param.append(',');
        param.append("mask=");
        param.append(Integer.toBinaryString(getModifiers()));
        param.append(',');
        param.append("modifiers=");
        param.append(getModifiersText(getModifiers()));
        param.append(',');
        return param.toString();
    }
}
