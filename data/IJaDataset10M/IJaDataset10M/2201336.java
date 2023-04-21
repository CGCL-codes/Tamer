package org.broad.igv.tdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.broad.igv.feature.LocusScore;
import org.broad.igv.tools.Accumulator;
import org.broad.igv.track.WindowFunction;
import org.broad.igv.util.collections.FloatArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jrobinso
 * Date: Dec 18, 2009
 * Time: 11:22:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Bin implements LocusScore {

    private int start;

    private int end;

    private float score = Float.MIN_VALUE;

    Accumulator accumulator;

    FloatArrayList values;

    List<String> names;

    WindowFunction windowFunction;

    private static final int maxValues = 5;

    public Bin(int start, int end, String probeName, float initialValue, WindowFunction windowFunction) {
        this.start = start;
        this.end = end;
        this.values = new FloatArrayList(maxValues);
        this.accumulator = new Accumulator(Arrays.asList(windowFunction));
        this.windowFunction = windowFunction;
        addValue(probeName, initialValue);
    }

    /**
     * Copy constructor
     *
     * @param otherBin
     */
    public Bin(Bin otherBin) {
        this.start = otherBin.start;
        this.end = otherBin.end;
        this.accumulator = otherBin.accumulator;
        this.names = otherBin.names;
        this.score = otherBin.score;
        this.windowFunction = otherBin.windowFunction;
    }

    public boolean isExtension(Bin bin) {
        return (end == bin.start) && getScore() == bin.getScore();
    }

    public void addValue(String name, float value) {
        if (values.size() < maxValues) {
            if (name != null) {
                if (names == null) {
                    names = new ArrayList<String>(maxValues);
                }
                names.add(name);
            }
            values.add(value);
        }
        accumulator.add(value);
    }

    public String getChr() {
        return null;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public float getScore() {
        if (score == Float.MIN_VALUE) {
            computeScore();
        }
        return score;
    }

    private void computeScore() {
        if (accumulator == null) {
            score = Float.NaN;
        } else {
            accumulator.finish();
            score = accumulator.getValue(windowFunction);
        }
    }

    public void setConfidence(float confidence) {
    }

    public float getConfidence() {
        return 1;
    }

    public LocusScore copy() {
        return new Bin(this);
    }

    public String getValueString(double position, WindowFunction windowFunction) {
        StringBuffer sb = new StringBuffer(50);
        sb.append("Value: ");
        sb.append(String.valueOf(getScore()));
        if (values.size() == 1) {
            if (names != null && names.size() > 0) {
                sb.append(" (");
                sb.append(names.get(0));
                sb.append(")");
            }
        } else {
            sb.append("<br> ");
            sb.append(windowFunction.getDisplayName() + " of " + (values.size() == maxValues ? "> " : "") + values.size() + " values:");
            float[] v = values.toArray();
            for (int i = 0; i < v.length; i++) {
                sb.append("<br>   " + v[i]);
                if (names != null && names.size() > i) {
                    sb.append("  (");
                    sb.append(names.get(i));
                    sb.append(")");
                }
            }
            if (v.length == maxValues) {
                sb.append("<br>...");
            }
        }
        return sb.toString();
    }
}
