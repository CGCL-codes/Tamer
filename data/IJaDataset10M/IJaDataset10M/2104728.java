package kroff;

import com.dustedpixels.common.base.Repeatable;

/**
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class OptimizedKroffUnit implements Repeatable {

    private boolean signal1;

    private boolean signal2;

    private boolean signal3;

    public void repeat(int steps) {
        if (steps < 0) throw new IllegalArgumentException("Negative steps");
        boolean localSignal1 = signal1;
        boolean localSignal2 = signal2;
        boolean localSignal3 = signal3;
        while (steps-- != 0) {
            localSignal1 = !localSignal1;
            localSignal2 = localSignal1 && localSignal3;
            localSignal3 = !localSignal2;
        }
        signal1 = localSignal1;
        signal2 = localSignal2;
        signal3 = localSignal3;
    }
}
