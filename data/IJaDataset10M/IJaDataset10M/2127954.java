package ao.bucket.abstraction.access.odds;

import ao.holdem.persist.GenericBinding;
import ao.odds.agglom.hist.CompactRiverStrengths;
import ao.odds.agglom.hist.StrengthHist;
import ao.odds.eval.eval5.Eval5;
import ao.util.data.Arrs;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import java.util.Arrays;

/**
 * Date: Jan 30, 2009
 * Time: 2:21:11 PM
 *
 * Histogram of 7 card hand strengths at river.
 *
 */
public class RiverHist {

    private final int HIST[];

    private double mean = Double.NaN;

    public RiverHist() {
        this(new int[CompactRiverStrengths.COUNT]);
    }

    private RiverHist(int hist[]) {
        HIST = hist;
    }

    public RiverHist(StrengthHist hist) {
        this();
        for (short i = 0; i < Eval5.VALUE_COUNT; i++) {
            short riverI = CompactRiverStrengths.compact(i);
            if (riverI != -1) {
                HIST[riverI] = hist.get(i);
            }
        }
    }

    public void count(short riverStrength) {
        count(riverStrength, 1);
    }

    public void count(short riverStrength, int count) {
        HIST[riverStrength] += count;
        mean = Double.NaN;
    }

    public int get(short riverStrength) {
        return HIST[riverStrength];
    }

    public int maxCount() {
        int maxCount = 0;
        for (int count : HIST) {
            if (maxCount < count) {
                maxCount = count;
            }
        }
        return maxCount;
    }

    public long totalCount() {
        long total = 0;
        for (int count : HIST) total += count;
        return total;
    }

    public double mean() {
        if (Double.isNaN(mean)) {
            mean = calculateMean();
        }
        return mean;
    }

    private double calculateMean() {
        long sum = 0;
        long count = 0;
        for (int i = 0; i < HIST.length; i++) {
            long histCount = HIST[i];
            sum += histCount * (i + 1);
            count += histCount;
        }
        return ((double) sum / count) / (CompactRiverStrengths.COUNT + 1);
    }

    public double nonLossProb(RiverHist that) {
        long thisSum = totalCount();
        long thatSum = that.totalCount();
        double tieProb = 0;
        double winProb = 0;
        double thatCumProb = 0;
        for (int i = 0; i < HIST.length; i++) {
            double thisPointProb = (double) HIST[i] / thisSum;
            double thatPointProb = (double) that.HIST[i] / thatSum;
            winProb += thisPointProb * thatCumProb;
            tieProb += thisPointProb * thatPointProb;
            thatCumProb += thatPointProb;
        }
        return winProb + tieProb / 2;
    }

    public SlimRiverHist slim() {
        return new SlimRiverHist(HIST);
    }

    @Override
    public String toString() {
        return Arrs.join(HIST, "\t");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiverHist strengthHist = (RiverHist) o;
        return Arrays.equals(HIST, strengthHist.HIST);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(HIST);
    }

    public static final int BINDING_SIZE = CompactRiverStrengths.COUNT * 4;

    public static final Binding BINDING = new Binding();

    public static class Binding extends GenericBinding<RiverHist> {

        public RiverHist read(TupleInput input) {
            int hist[] = new int[CompactRiverStrengths.COUNT];
            for (int i = 0; i < hist.length; i++) {
                hist[i] = input.readInt();
            }
            return new RiverHist(hist);
        }

        public void write(RiverHist o, TupleOutput to) {
            for (int i = 0; i < o.HIST.length; i++) {
                to.writeInt(o.HIST[i]);
            }
        }
    }
}
