package org.argouml.ui.cmd;

import java.util.EventObject;
import javax.swing.KeyStroke;

/**
 * An event to be fired in order to tell some other listener of a shortcut
 * change.
 *
 * @author andrea.nironi@gmail.com
 */
public class ShortcutChangedEvent extends EventObject {

    /**
     * The UID
     */
    private static final long serialVersionUID = 961611716902568240L;

    private KeyStroke keyStroke;

    /**
     * Creates a new ShortcutChangedEvent object
     * 
     * @param source    the source that generated this event
     * @param newStroke the new KeyStroke for the corresponding shortcut
     */
    public ShortcutChangedEvent(Object source, KeyStroke newStroke) {
        super(source);
        this.keyStroke = newStroke;
    }

    /**
     * Getter for KeyStroke
     * 
     * @return  the keyStroke for this event
     */
    public KeyStroke getKeyStroke() {
        return keyStroke;
    }
}
