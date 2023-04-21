package org.waveprotocol.wave.client.account;

import org.waveprotocol.wave.model.wave.ParticipantId;
import org.waveprotocol.wave.model.wave.SourcesEvents;

/**
 * Manages profiles for participants.
 *
 * @author kalman@google.com (Benjamin Kalman)
 */
public interface ProfileManager extends SourcesEvents<ProfileListener> {

    /**
   * Gets the profile for a participant.
   *
   * @param participantId id of the participant
   * @return the profile for a participant
   */
    Profile getProfile(ParticipantId participantId);

    /**
   * Returns whether the participant should be ignored in the context of
   * accounts.
   *
   * @param participantId the participant id to check
   * @return true if the participant should be ignored, false if not
   */
    boolean shouldIgnore(ParticipantId participantId);
}
