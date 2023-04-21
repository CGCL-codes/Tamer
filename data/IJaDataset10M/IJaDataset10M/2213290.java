package de.mud.jta.event;

import de.mud.jta.Plugin;
import de.mud.jta.PluginMessage;
import de.mud.jta.PluginListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

/**
 * Notify all listeners that a key has been pressed.
 * <P>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @version $Id: KeyPress.java,v 1.1.1.1 2004/10/25 15:52:11 jbwiv Exp $
 * @author Matthias L. Jugel, Marcus Mei?ner
 */
public class KeyPress implements PluginMessage {

    private KeyEvent evt;

    public KeyPress(KeyEvent evt) {
        this.evt = evt;
    }

    /**
   * Notify the listers about the focus status of the sending component.
   * @param pl the list of plugin message listeners
   * @return null
   */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof KeyPressListener) {
            ((KeyPressListener) pl).handleKeyPress(evt);
        }
        return null;
    }
}
