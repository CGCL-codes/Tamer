package net.jtank.input;

/**
 * This type of eevnt is fired when a key was pressed.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class KeyPressedEvent extends KeyboardEvent {

    /**
     * {@inheritDoc}
     */
    @Override
    public int getKeyCode() {
        return super.getKeyCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "KeyPressedEvent( keyCode = " + getKeyCode() + ", when = " + getWhen() + ")";
    }

    /**
     * Initialises this KeyboardEvent using the given values.
     * 
     * @param keyCode the key-code whose state changed
     * @param when the timestamp of the KeyboardEvent 
     */
    protected void set(KeyboardDevice keyboard, int keyCode, long when) {
        super.set(keyboard, Type.PRESSED, keyCode, (char) 0, when);
    }

    /**
     * Initialises the new KeyboardEvent using the given values.
     * 
     * @param keyCode the key-code whose state changed
     * @param when the timestamp of the KeyboardEvent 
     */
    protected KeyPressedEvent(KeyboardDevice keyboard, int keyCode, long when) {
        super(keyboard, Type.PRESSED, keyCode, (char) 0, when);
    }

    /**
     * Creates a new KeyboardEvent with the default settings
     */
    protected KeyPressedEvent() {
        super();
    }
}
