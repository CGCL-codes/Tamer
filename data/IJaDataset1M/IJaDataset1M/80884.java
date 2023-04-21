package ampt.core.time;

/**
 * This class is a modifed version of the ampt.core.time.StandardClock,
 * authored by Ben. It uses System.nanoTime() rather than
 * System.currentTimeMillis(). The Java 6 API states that nanoTime() will use
 * the most precise available system timer, which if that is the case
 * should provide resolution comparable to Ben's JNI implementation.
 *
 *
 * @author robert
 */
public class JavaNanoClock extends Clock {

    @Override
    public long getTime() {
        return System.nanoTime();
    }

    @Override
    public int getDefaultUnit() {
        return UNIT_NANOS;
    }

    @Override
    public void stampTime() {
        stampTime = System.nanoTime();
    }

    @Override
    public long getElapsedTime() {
        return System.nanoTime() - stampTime;
    }

    @Override
    public long getElapsedTime(int unit) {
        return ((System.nanoTime() - stampTime) * unit) / UNIT_NANOS;
    }
}
