package org.jjazz.rhythm;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.jjazz.util.Utilities;

/**
 * A range of tempo.
 */
public final class TempoRange implements Cloneable, Serializable {

    public static final int TEMPO_MIN = 10;

    public static final int TEMPO_STD = 120;

    public static final int TEMPO_MAX = 400;

    /** Minimum recommanded tempo. */
    private int min = TEMPO_MIN;

    /** Maximum recommanded tempo. */
    private int max = TEMPO_MAX;

    /** Associated tags. */
    private List<String> tags = new ArrayList<String>();

    private static final Logger LOGGER = Logger.getLogger(TempoRange.class.getName());

    /**
     * Objects can be constructed from the TempoRangeFactory.
     * @param min
     * @param max
     * @param tags Optional tags associated to this tempo range.
     */
    protected TempoRange(int min, int max, String... tags) {
        if (checkTempo(min) || checkTempo(max) || min > max) {
            throw new IllegalArgumentException(" min=" + min + " max=" + max + " tags=" + tags);
        }
        this.min = min;
        this.max = max;
        this.tags = Arrays.asList(tags);
    }

    /**
     * @param tempo
     * @return True if tempo is included in the bounds of this TempoRange.
     */
    public boolean contains(int tempo) {
        if (!checkTempo(tempo)) {
            throw new IllegalArgumentException("tempo=" + tempo);
        }
        return (tempo >= min && tempo <= max);
    }

    /**
     *
     * @param tag
     * @return True if this tag is associated to this TempoRange. Comparison done ignoring case.
     */
    public boolean contains(String tag) {
        return Utilities.indexOfStringIgnoreCase(tags, tag) != -1;
    }

    /**
     * The tags associated to this tempo range. Can be an empty list.
     * @return
     */
    public List<String> getTags() {
        return tags;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    @Override
    public String toString() {
        return "[" + min + "-" + max + "] tags=" + tags;
    }

    /**
     * Compute a percentage that say how similar are this object's tempo bounds with tr's tempo bounds.
     * Return value = (tempo range of the intersection of both objects)/(tempo range of the union of both objects)
     * Examples:
     * this = [60,80], tr=[90,100] => return value = 0
     * this = [60,80], tr=[70,90]  => return value = 10/30 = 0.33
     * this = [60,80], tr=[58,85]  => return value = 20/27 = 0.74
     * this = [60,80], tr=[60,80]  => return value = 20/20 = 1
     * @param tr TempoRange
     * @return A value between 0 and 1.
     */
    public float computeSimilarityLevel(TempoRange tr) {
        float inter = 0;
        float union = 1;
        if (tr == null) {
            throw new NullPointerException("tr=" + tr);
        }
        if (min <= tr.min && max >= tr.min) {
            inter = Math.min(max, tr.max) - tr.min + 1;
            union = Math.max(max, tr.max) - min + 1;
        } else if (tr.min <= min && tr.max >= min) {
            inter = Math.min(max, tr.max) - min + 1;
            union = Math.max(max, tr.max) - tr.min + 1;
        }
        return inter / union;
    }

    public static boolean checkTempo(int t) {
        if ((t < TEMPO_MIN) || (t > TEMPO_MAX)) {
            return false;
        } else {
            return true;
        }
    }
}
