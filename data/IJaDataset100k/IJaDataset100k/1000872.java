package org.nymph.player;

/**
 * Defines set of methods to controls Android's media player.
 *
 * @author Sergey Krutsenko
 *
 * @version 1.0
 *
 */
public interface IPlayerController {

    /**
	 * Starts media player.
	 */
    void start();

    /**
	 * Stops media player.
	 */
    void stop();

    /**
	 * Pauses media player.
	 */
    void pause();

    /**
     * Sets data source to player.     *
     * @param src The String used to provide full path
     * to a local media file or a direct link to on-line stream
     */
    void setSource(String src);

    /**
     * Returns current position of playing song.
     * @return
     */
    int getCurrentPosition();
}
