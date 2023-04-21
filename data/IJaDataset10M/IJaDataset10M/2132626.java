package cunei.collections;

import java.util.Iterator;

public class PrunedSet<T extends ScoredItem> extends ScoredSet<T> {

    private static final long serialVersionUID = 1L;

    private int pruned;

    private int invalid;

    private final int pruneMin;

    private final int pruneMax;

    private final float pruneRatio;

    private float bestLogScore;

    private float worstLogScore;

    private float avgLogScore;

    private double rmsLogScore;

    public PrunedSet(int pruneMin, int pruneMax, float pruneRatio) {
        assert pruneMin <= pruneMax;
        this.pruned = 0;
        this.invalid = 0;
        this.pruneMin = pruneMin;
        this.pruneMax = pruneMax;
        this.pruneRatio = pruneRatio;
        this.bestLogScore = Float.POSITIVE_INFINITY;
        this.worstLogScore = Float.POSITIVE_INFINITY;
        this.avgLogScore = 0;
        this.rmsLogScore = 0;
    }

    public boolean add(T entry) {
        if (entry == null) return false;
        final float logScore = entry.getLogScore();
        if (isValid(logScore)) return addValidEntry(entry, logScore);
        invalid++;
        return false;
    }

    private boolean addValidEntry(final T entry, final float logScore) {
        if (size() >= pruneMax) removeWorst();
        if (logScore < worstLogScore) worstLogScore = logScore;
        super.add(entry);
        if (size() <= pruneMin) {
            updateRatio(logScore, false);
            if (logScore < bestLogScore) bestLogScore = logScore;
        } else if (logScore > bestLogScore) {
            updateRatio(bestLogScore, true);
            updateRatio(logScore, false);
            if (pruneMin == 1) bestLogScore = logScore; else {
                Iterator<T> iter = iterator();
                T best = iter.next();
                for (int i = 1; i < pruneMin && iter.hasNext(); i++) best = iter.next();
                bestLogScore = best.getLogScore();
            }
            final float ratioLogScore = getRatioLogScore();
            while (ratioLogScore > worstLogScore) removeWorst();
        }
        return true;
    }

    public void clear() {
        super.clear();
        pruned = 0;
        bestLogScore = Float.NEGATIVE_INFINITY;
        worstLogScore = Float.POSITIVE_INFINITY;
        avgLogScore = 0;
        rmsLogScore = 0;
    }

    public PrunedSet<T> clone() {
        return (PrunedSet<T>) super.clone();
    }

    private final float getRatioLogScore() {
        final float normAvgLogScore = avgLogScore / pruneMin;
        final float logScoreVar = (float) Math.sqrt(rmsLogScore / pruneMin - Math.pow(normAvgLogScore, 2));
        return normAvgLogScore - pruneRatio * logScoreVar;
    }

    public final int invalid() {
        return invalid;
    }

    protected final boolean isValid(float logScore) {
        if (logScore == Float.NEGATIVE_INFINITY) return false;
        final int size = size();
        return size < pruneMin || logScore > worstLogScore || size < pruneMax && logScore >= getRatioLogScore();
    }

    public final int pruned() {
        return pruned;
    }

    public final boolean remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    protected T removeWorst() {
        pruned++;
        final T result = pollLast();
        if (isEmpty()) worstLogScore = Float.POSITIVE_INFINITY; else worstLogScore = last().getLogScore();
        if (size() < pruneMin) {
            bestLogScore = worstLogScore;
            updateRatio(result.getLogScore(), true);
        }
        return result;
    }

    protected final boolean replace(T existingEntry, T entry) {
        final float logScore = entry.getLogScore();
        final float existingLogScore = existingEntry.getLogScore();
        if (logScore <= existingLogScore) return false;
        if (!super.remove(existingEntry)) return false;
        if (isEmpty()) worstLogScore = Float.POSITIVE_INFINITY; else worstLogScore = last().getLogScore();
        if (size() < pruneMin) {
            bestLogScore = worstLogScore;
            updateRatio(existingLogScore, true);
        } else if (existingLogScore > bestLogScore) {
            updateRatio(bestLogScore, false);
            updateRatio(existingLogScore, true);
        }
        return addValidEntry(entry, logScore);
    }

    public String toString() {
        return "{PRUNED " + pruned + " " + bestLogScore + " " + super.toString() + "}";
    }

    private final void updateRatio(final float logScore, final boolean negate) {
        if (negate) {
            avgLogScore -= logScore;
            rmsLogScore -= Math.pow(logScore, 2);
        } else {
            avgLogScore += logScore;
            rmsLogScore += Math.pow(logScore, 2);
        }
    }
}
