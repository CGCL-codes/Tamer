package org.waveprotocol.box.server.waveserver;

import com.google.common.collect.ImmutableSet;
import org.waveprotocol.box.common.ExceptionalIterator;
import org.waveprotocol.box.server.frontend.CommittedWaveletSnapshot;
import org.waveprotocol.wave.federation.Proto.ProtocolWaveletDelta;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.id.WaveletId;
import org.waveprotocol.wave.model.id.WaveletName;
import org.waveprotocol.wave.model.operation.wave.TransformedWaveletDelta;
import org.waveprotocol.wave.model.version.HashedVersion;
import org.waveprotocol.wave.model.wave.ParticipantId;
import java.util.Collection;

/**
 * Provides wavelet snapshots and history, and accepts delta submissions to
 * wavelets.
 */
public interface WaveletProvider {

    /**
   * Receives the result of a delta submission request.
   */
    interface SubmitRequestListener {

        /**
     * Notifies the listener that the delta was successfully applied.
     *
     * @param operationsApplied number of operations applied
     * @param hashedVersionAfterApplication wavelet hashed version after the delta
     * @param applicationTimestamp timestamp of delta application
     */
        void onSuccess(int operationsApplied, HashedVersion hashedVersionAfterApplication, long applicationTimestamp);

        /**
     * Notifies the listener that the delta failed to apply.
     */
        void onFailure(String errorMessage);
    }

    /**
   * Initializes the provider from storage. No other method is valid until
   * initialization is complete.
   */
    void initialize() throws WaveServerException;

    /**
   * Request that a given delta is submitted to the wavelet.
   *
   * @param waveletName name of wavelet.
   * @param delta to be submitted to the server.
   * @param listener callback which will return the result of the submission.
   */
    void submitRequest(WaveletName waveletName, ProtocolWaveletDelta delta, SubmitRequestListener listener);

    /**
   * Retrieve the wavelet history of deltas applied to the wavelet.
   *
   * @param waveletName name of wavelet.
   * @param versionStart start version (inclusive), minimum 0.
   * @param versionEnd end version (exclusive).
   * @return deltas in the range as requested, ordered by applied version.
   * @throws AccessControlException if {@code versionStart} or
   *         {@code versionEnd} are not in the wavelet history.
   * @throws WaveServerException if storage access fails or if the wavelet is in
   *         a bad state
   */
    Collection<TransformedWaveletDelta> getHistory(WaveletName waveletName, HashedVersion versionStart, HashedVersion versionEnd) throws WaveServerException;

    /**
   * Check if the specified participantId has access to the named wavelet.
   *
   * @param waveletName name of wavelet.
   * @param participantId id of participant attempting to gain access to
   *        wavelet, or null if the user isn't logged in.
   * @return true if the wavelet exists and the participant is a participant on
   *         the wavelet.
   * @throws WaveServerException if storage access fails or if the wavelet is in
   *         a bad state
   */
    boolean checkAccessPermission(WaveletName waveletName, ParticipantId participantId) throws WaveServerException;

    /**
   * Returns an iterator over all waves in the server.
   *
   * The iterator may or may not include waves created after the iterator is returned.
   *
   * @return an iterator over the ids of all waves
   * @throws WaveServerException if storage access fails
   */
    ExceptionalIterator<WaveId, WaveServerException> getWaveIds() throws WaveServerException;

    /**
   * Looks up all wavelets in a wave.
   *
   * @param waveId wave to look up
   * @return ids of all non-empty wavelets
   * @throws WaveServerException if storage access fails
   */
    ImmutableSet<WaveletId> getWaveletIds(WaveId waveId) throws WaveServerException;

    /**
   * Request the current state of the wavelet.
   *
   * @param waveletName the name of the wavelet
   * @return the wavelet, or null if the wavelet doesn't exist
   * @throws WaveServerException if storage access fails or if the wavelet is in
   *         a bad state
   */
    CommittedWaveletSnapshot getSnapshot(WaveletName waveletName) throws WaveServerException;
}
