package mpmetr.core;

import static mpmetr.core.SectorEnum.SECTOR_COUNT;

/**
 * Converts the current time and a BPM to an Integer index
 * representing which sector should be active.
 * It caches the most recent calculation in a non-threadsafe way.
 */
final class TimeIndexer {

    private final SystemTime clock = new SystemTime();

    private final TickRatioCalculator ticker = new TickRatioCalculator();

    private static final ValueCache<Integer> bpmCache = new ValueCache<Integer>(-1);

    private static final ValueCache<Long> timeCache = new ValueCache<Long>(-1L);

    private static Long lastIndex = -1L;

    public long timeIndex(Integer bpm) {
        boolean needCalc = updateBpm(bpm) | updateTime();
        if (needCalc) calcIndex();
        return lastIndex;
    }

    private boolean updateTime() {
        Long time = clock.getTime();
        return timeCache.assign(time);
    }

    private boolean updateBpm(Integer bpm) {
        return bpmCache.assign(bpm);
    }

    private void calcIndex() {
        Integer bpm = bpmCache.getValue();
        Long time = timeCache.getValue();
        Double tickRatio = ticker.calc(bpm);
        Long ticks = (long) (time * tickRatio);
        lastIndex = ticks % SECTOR_COUNT;
    }
}
