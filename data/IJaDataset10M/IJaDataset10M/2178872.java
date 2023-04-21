package org.xith3d.loop;

import org.jagatoo.util.timing.TimerInterface;

/**
 * The default implementation of {@link FPSLimiter}.
 * It guarantees, that the CPU load is kept at a minimum,
 * while animation might be juddering.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class DefaultFPSLimiter implements FPSLimiter {

    private static final long ONE_MIO = 1000000L;

    private static final long HALF_MIO = 500000L;

    private long accumulator = 0L;

    private static DefaultFPSLimiter instance = null;

    public static final DefaultFPSLimiter getInstance() {
        if (instance == null) instance = new DefaultFPSLimiter();
        return (instance);
    }

    /**
     * {@inheritDoc}
     */
    public long limitFPS(long frameIdx, long frameTime, long minFrameTime, TimerInterface timer) {
        accumulator += minFrameTime - frameTime;
        if (accumulator <= 0L) {
            Thread.yield();
            return (0L);
        }
        long waitMillis = accumulator / ONE_MIO;
        accumulator -= waitMillis * ONE_MIO;
        if (accumulator >= HALF_MIO) {
            accumulator -= ONE_MIO;
            waitMillis += 1L;
        }
        long t1 = timer.getNanoseconds();
        try {
            Thread.sleep(waitMillis);
        } catch (InterruptedException e) {
        }
        long sleptTime = timer.getNanoseconds() - t1;
        accumulator -= sleptTime - waitMillis * ONE_MIO;
        return (sleptTime);
    }
}
