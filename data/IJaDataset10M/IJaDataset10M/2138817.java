package org.waveprotocol.wave.client.wavepanel.event;

import org.waveprotocol.wave.client.common.util.KeyCombo;

/**
 * Handles a key signal.
 *
 * Unlike other event handlers (e.g., {@link WaveClickHandler}), there is no
 * context for a key signal, because they are not directed at any particular DOM
 * element.
 *
 */
public interface KeySignalHandler {

    /**
   * @return true if this handler intends other handlers not to see the event.
   *         This is typically because this handler has taken an action that
   *         should be mutually exclusive with other actions that other handlers
   *         may perform.
   */
    boolean onKeySignal(KeyCombo key);
}
