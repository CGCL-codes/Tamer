package mpmetr.core;

import static mpmetr.core.SectorEnum.SECTOR_COUNT;

/**
 * Calculates and caches the multiplier used to convert time to ticks.
 * This class is NOT thread safe.
 */
public final class TickRatioCalculator {

    private static final int BEATS_PER_REV = 2;

    private static final int TICKS_PER_BEAT = SECTOR_COUNT / BEATS_PER_REV;

    private static final ValueCache<Integer> bpmCache = new ValueCache<Integer>(-1);

    private static Double lastCalc = -1.0;

    public Double calc(Integer bpm) {
        if (bpmCache.assign(bpm)) update();
        return lastCalc;
    }

    private void update() {
        Integer bpm = bpmCache.getValue();
        double i = TICKS_PER_BEAT * bpm;
        lastCalc = i / 60000;
    }
}
