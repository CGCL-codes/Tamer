package sun.audio;

/**
 * Create a continuous audio stream. This wraps a stream
 * around an AudioData object, the stream is restarted
 * at the beginning everytime the end is reached, thus
 * creating continuous sound.<p>
 * For example:
 * <pre>
 *	AudioData data = AudioData.getAudioData(url);
 *	ContinuousAudioDataStream audiostream = new ContinuousAudioDataStream(data);
 *	AudioPlayer.player.start(audiostream);
 * </pre>
 *
 * @see AudioPlayer
 * @see AudioData
 * @author Arthur van Hoff
 * @version 	1.13, 08/19/02
 */
public class ContinuousAudioDataStream extends AudioDataStream {

    /**
     * Create a continuous stream of audio.
     */
    public ContinuousAudioDataStream(AudioData data) {
        super(data);
    }

    /**
     * When reaching the EOF, rewind to the beginning.
     */
    public int read() {
        int c = super.read();
        if (c == -1) {
            reset();
            c = super.read();
        }
        return c;
    }

    /**
     * When reaching the EOF, rewind to the beginning.
     */
    public int read(byte buf[], int pos, int len) {
        int count = 0;
        while (count < len) {
            int n = super.read(buf, pos + count, len - count);
            if (n >= 0) {
                count += n;
            } else {
                reset();
            }
        }
        return count;
    }
}
