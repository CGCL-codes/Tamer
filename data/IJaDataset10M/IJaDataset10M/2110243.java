package applet;

import java.util.LinkedList;
import java.util.Queue;

/** This class can be used to limit the frame rate. */
public class Fps {

    /** The number of nanoseconds in a millisecond. */
    private static final int NANO_PER_MILLI = 1000000;

    /** The number of nanoseconds in a second. */
    private static final int NANO_PER_SECOND = 1000000000;

    /** */
    private final int fps;

    /** */
    private final int frameNanos;

    /** */
    private final Queue<Long> frameTimes = new LinkedList<Long>();

    /** */
    private long lastFrame;

    /**
	 * @param desiredFps
	 *            - the desired frames per second.
	 */
    public Fps(final int desiredFps) {
        fps = desiredFps;
        frameNanos = NANO_PER_SECOND / fps;
    }

    /** Sets a reference point for the first frame. */
    public final void begin() {
        lastFrame = System.nanoTime();
        frameTimes.add(lastFrame);
    }

    /** Puts the thread to sleep until the next frame. */
    public final void step() {
        final long nextFrame = lastFrame + frameNanos;
        long nanos = nextFrame - System.nanoTime();
        if (nanos > 0) {
            long millis = nanos / NANO_PER_MILLI;
            nanos %= NANO_PER_MILLI;
            try {
                Thread.sleep(millis, (int) nanos);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long now;
            do {
                now = System.nanoTime();
            } while (now < nextFrame);
        }
        lastFrame = System.nanoTime();
        frameTimes.add(lastFrame);
        while (frameTimes.size() != 0) {
            if (lastFrame - frameTimes.peek() > NANO_PER_SECOND) {
                frameTimes.remove();
            } else {
                break;
            }
        }
    }

    /** @return the number of frames performed in the last second. */
    public final int getCurrentFps() {
        return frameTimes.size();
    }
}
