package midi;

import uk.org.toot.audio.core.AudioBuffer;
import uk.org.toot.audio.core.AudioProcess;

/**
 *
 * Implementation of a cyclic buffer.
 *
 * This buffer is fed using the in.processAudio(buff);
 *   this returns OVERFLOW if the buffer is full
 *
 * You read the buffer using out.processAudio(buff);
 *   this will block if no data is ready
 *
 *
 * @author pjl
 */
public class CycliclyBufferedAudio {

    public static final int OVERFLOW = -1;

    float buff[];

    int cacheSize;

    long inPtr = 0;

    public final AudioProcess in = new In();

    private final Object muex = new Object();

    public CycliclyBufferedAudio(int cacheSize) {
        this.cacheSize = cacheSize;
        buff = new float[cacheSize];
    }

    void grabEnd(float x[], int nSamp) {
        synchronized (muex) {
            int ii = (int) (inPtr % cacheSize);
            if (nSamp <= ii) {
                System.arraycopy(buff, (int) (ii - nSamp), x, 0, nSamp);
            } else {
                int nEnd = (int) (nSamp - ii);
                System.arraycopy(buff, cacheSize - 1 - nEnd, x, 0, nEnd);
                System.arraycopy(buff, 0, x, nEnd, nSamp - nEnd);
            }
        }
    }

    class In implements AudioProcess {

        int cnt;

        public void close() throws Exception {
        }

        public void open() throws Exception {
        }

        public int processAudio(AudioBuffer buffer) {
            int n = buffer.getSampleCount();
            int inCy0 = (int) (inPtr % cacheSize);
            int inCy1 = (int) ((inPtr + n) % cacheSize);
            synchronized (muex) {
                if (inCy1 > inCy0) {
                    System.arraycopy(buffer.getChannel(0), 0, buff, inCy0, n);
                } else {
                    System.arraycopy(buffer.getChannel(0), 0, buff, inCy0, n - inCy1);
                    System.arraycopy(buffer.getChannel(0), n - inCy1, buff, 0, inCy1);
                }
                inPtr += n;
            }
            return AUDIO_OK;
        }
    }
}
