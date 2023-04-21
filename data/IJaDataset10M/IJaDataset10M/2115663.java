package org.waveprotocol.wave.model.account;

import org.waveprotocol.wave.model.wave.ParticipantId;

/**
 * Indexability that you can change.
 *
 *
 */
public interface MutableIndexability extends Indexability {

    /**
   * Set the indexability for a participant.
   *
   * Assigning null indexability removes the assignment.
   *
   * @param participant Participant whose indexability to change. Not null.
   * @param indexability Whether to index for this participant. May be null.
   */
    void setIndexability(ParticipantId participant, IndexDecision indexability);
}
