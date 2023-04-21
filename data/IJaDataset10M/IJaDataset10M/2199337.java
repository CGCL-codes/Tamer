package JavaTron;

import java.util.*;

/**
 * AudioTronListener interface describes a notification mechanism
 * for state changes.
 *
 * @author Taylor Gautier
 * @version $Revision: 1.1 $
 */
public interface AudioTronListener {

    /**
   * Notification of mute state
   *
   * @param muted true if muted, false otherwise
   */
    public void mute(boolean muted);

    /**
   * Notification of repeat state
   *
   * @param repeat true if repeat is set, false otherwise
   */
    public void repeat(boolean repeat);

    /**
   * Notification of random state
   *
   * @param random true if random is set, false otherwise
   */
    public void random(boolean random);

    /**
   * Notification of playing state.
   *
   * @param state can be one of {@link #STATE_UNKNOWN}, 
                  {@link #STATE_PLAYING}, {@link #STATE_PAUSED},
                  {@link #STATE_STOPPED}
   */
    public void state(ATState state);

    /**
   * Notification of AudioTron version
   *
   * @param version The AudioTron version as reported by the AudioTron webpage
   */
    public void version(String version);

    /**
   * Notification of the details of the current song playing
   *
   * @param song the current song playing
   */
    public void currentSong(AudioTronSong song);

    /**
   * Notification of the details of the next song to be played
   *
   * @param song the next song to play
   */
    public void nextSong(AudioTronSong song);

    /**
   * Notification of the current song position
   *
   * @param song the position of the song
   */
    public void songPositionUpdated(AudioTronSong song);

    /**
   * Notification of the AudioTron "controller enginer" status.  This
   * value is suitable for display to the user.
   *
   * @param status a string that indicates what the "engine" is doing
   * @param longRunning this value is set if the current operation is long
   *                    running.  In the case that it is long running,
   *                    status will be called repeatedly (likely with
   *                    the same value) during the long running operation.
   *
   */
    public void status(String status, boolean longRunning);
}
