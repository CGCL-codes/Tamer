package com.swabunga.spell.event;

import java.util.EventListener;

/**
 * This is the event based listener interface.
 *
 * @author Jason Height (jheight@chariot.net.au)
 */
public interface SpellCheckListener extends EventListener {

    /**
   * Propagates the spelling errors to listeners.
   * @param event The event to handle
   */
    public void spellingError(SpellCheckEvent event);
}
