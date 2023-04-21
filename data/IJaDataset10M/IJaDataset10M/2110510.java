package mp3player;

/**
 * Callback interface to get song position etc.
 */
public interface EngineListener {

    /**
	 * Called when the song position has changed.
	 * @param pos New song position, in percent, 0 - 100.
	 */
    void updateSongPosition(float pos);

    /**
	 * Called when the playback has really been started
	 * (i.e. sound is coming out).
	 */
    void updateSongStarted();

    /**
	 * Called when the playback stops without user request.
	 */
    void updateSongEnded();
}
