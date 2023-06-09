package com.google.wave.api.event;

import com.google.wave.api.BlipData;
import com.google.wave.api.BlipThread;
import com.google.wave.api.Wavelet;
import com.google.wave.api.impl.EventMessageBundle;
import com.google.wave.api.impl.WaveletData;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Event triggered when a wavelet is fetched. This event is triggered after a
 * robot requests to see another wavelet. The robot has to be a participant of
 * the requested wavelet.
 */
public class WaveletFetchedEvent extends AbstractEvent {

    /** The message that was passed into the wavelet fetch operation. */
    private final String message;

    /** The fetched wavelet. */
    private final WaveletData waveletData;

    /** The blips that are associated with the fetched wavelet. */
    private final Map<String, BlipData> blips;

    /** The threads that are associated with the fetched wavelet. */
    private final Map<String, BlipThread> threads;

    /** Raw deltas.  Null if not requested. */
    @Nullable
    private final List<String> rawDeltas;

    /**
   * Raw snapshot.  The first element is the wavelet metadata, the
   * rest are blips.  Null if no snapshot was requested.
   */
    @Nullable
    private final List<String> rawSnapshot;

    /**
   * Constructor.
   *
   * @param wavelet the wavelet in which this event was triggered.
   * @param bundle the message bundle this event belongs to.
   * @param modifiedBy the id of the participant that triggered this event.
   * @param timestamp the timestamp of this event.
   * @param message the message that was passed into the wavelet fetch
   *     operation.
   * @param rootBlipId the root blip id of the wavelet where this event occurs.
   * @param waveletData the wavelet data of the fetched wavelet.
   * @param blips the blips of the fetched wavelet.
   * @param threads the threads of the fetched wavelet.
   */
    public WaveletFetchedEvent(Wavelet wavelet, EventMessageBundle bundle, String modifiedBy, Long timestamp, String message, String rootBlipId, WaveletData waveletData, Map<String, BlipData> blips, Map<String, BlipThread> threads, @Nullable List<String> rawDeltas, @Nullable List<String> rawSnapshot) {
        super(EventType.WAVELET_FETCHED, wavelet, bundle, modifiedBy, timestamp, rootBlipId);
        this.message = message;
        this.waveletData = waveletData;
        this.blips = blips;
        this.threads = threads;
        this.rawDeltas = rawDeltas;
        this.rawSnapshot = rawSnapshot;
    }

    /**
   * Constructor for deserialization.
   */
    WaveletFetchedEvent() {
        this.message = null;
        this.waveletData = null;
        this.blips = null;
        this.threads = null;
        this.rawDeltas = null;
        this.rawSnapshot = null;
    }

    /**
   * Returns the message that was passed into the wavelet fetch operation.
   *
   * @return the message that was passed into the wavelet fetch operation.
   */
    public String getMessage() {
        return message;
    }

    /**
   * Returns the fetched wavelet.
   *
   * @return an instance of {@link WaveletData} that represents the fetched
   *     wavelet.
   */
    public WaveletData getWaveletData() {
        return waveletData;
    }

    /**
   * Returns the blips that are associated with the fetched wavelet.
   *
   * @return a map of {@link BlipData} that are associated with the fetched
   *     wavelet.
   */
    public Map<String, BlipData> getBlips() {
        return blips;
    }

    /**
   * Returns the threads that are associated with the fetched wavelet.
   *
   * @return a map of {@link BlipThread} that are associated with the fetched
   *     wavelet.
   */
    public Map<String, BlipThread> getThreads() {
        return threads;
    }

    @Nullable
    public List<String> getRawDeltas() {
        return rawDeltas;
    }

    @Nullable
    public List<String> getRawSnapshot() {
        return rawSnapshot;
    }

    /**
   * Helper method for type conversion.
   *
   * @return the concrete type of this event, or {@code null} if it is of a
   *     different event type.
   */
    public static WaveletFetchedEvent as(Event event) {
        if (!(event instanceof WaveletFetchedEvent)) {
            return null;
        }
        return WaveletFetchedEvent.class.cast(event);
    }
}
