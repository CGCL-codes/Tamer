package org.waveprotocol.wave.model.account;

import org.waveprotocol.wave.model.wave.ParticipantId;
import org.waveprotocol.wave.model.wave.SourcesEvents;

/**
 * Indexability that you can listen to.
 *
 *
 */
public interface ObservableIndexability extends Indexability, SourcesEvents<ObservableIndexability.Listener> {

    /**
   * Listen to indexability changes.
   *
   *
   */
    interface Listener {

        /**
     * Indexability has changed.
     */
        void onChanged(ParticipantId participant, IndexDecision newValue);
    }
}
