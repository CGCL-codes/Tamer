package net.sf.mzmine.modules.peaklistmethods.alignment.path.functions;

import net.sf.mzmine.data.ChromatographicPeak;
import net.sf.mzmine.data.PeakListRow;
import net.sf.mzmine.data.impl.SimplePeakListRow;

public class AlignmentPath implements Comparable<AlignmentPath>, Cloneable {

    public static final int NOT_USED = -1;

    private PeakListRow peaks[];

    private int indices[];

    private int nonGapCount;

    private double rtsum, mzsum;

    private double meanRT, meanMZ;

    private double score;

    private boolean isEmpty;

    private boolean identified;

    private PeakListRow base;

    @Override
    public AlignmentPath clone() {
        AlignmentPath p = new AlignmentPath();
        p.peaks = peaks.clone();
        p.nonGapCount = nonGapCount;
        p.rtsum = rtsum;
        p.mzsum = mzsum;
        p.score = score;
        p.isEmpty = isEmpty;
        p.identified = identified;
        p.base = base;
        p.meanRT = meanRT;
        p.meanMZ = meanMZ;
        return p;
    }

    private AlignmentPath() {
    }

    private AlignmentPath(int n) {
        peaks = new PeakListRow[n];
        isEmpty = true;
    }

    /**
         * @param len
         * @param base
         * @param startCol
         * @param params2
         */
    public AlignmentPath(int len, PeakListRow base, int startCol) {
        this(len);
        this.base = base;
        this.add(startCol, this.base, 0);
    }

    public int nonEmptyPeaks() {
        return nonGapCount;
    }

    public boolean containsSame(AlignmentPath anotherPath) {
        boolean same = false;
        for (int i = 0; i < peaks.length; i++) {
            PeakListRow d = peaks[i];
            if (d != null) {
                same = d.equals(anotherPath.peaks[i]);
            }
            if (same) {
                break;
            }
        }
        return same;
    }

    public void addGap(int col, double score) {
        this.score += score;
    }

    /**
         * No peaks with differing mass should reach this point.
         * @param col column in peak table that contains this peak
         * @param peak
         * @param matchScore
         */
    public void add(int col, PeakListRow peak, double matchScore) {
        if (peaks[col] != null) {
            return;
        }
        peaks[col] = peak;
        if (peak != null) {
            nonGapCount++;
            rtsum += peak.getAverageRT();
            meanRT = rtsum / nonGapCount;
            mzsum += peak.getAverageMZ();
            meanMZ = mzsum / nonGapCount;
        }
        isEmpty = false;
        score += matchScore;
    }

    public double getRT() {
        return meanRT;
    }

    public double getMZ() {
        return meanMZ;
    }

    public double getScore() {
        return score;
    }

    public int getIndex(int i) {
        return indices[i];
    }

    /**
         * Is the whole alignment path still empty?
         * @return
         */
    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isFull() {
        return nonEmptyPeaks() == length();
    }

    public int length() {
        return peaks.length;
    }

    public PeakListRow convertToAlignmentRow(int ID) {
        PeakListRow newRow = new SimplePeakListRow(ID);
        try {
            for (PeakListRow row : this.peaks) {
                if (row != null) {
                    for (ChromatographicPeak peak : row.getPeaks()) {
                        newRow.addPeak(peak.getDataFile(), peak);
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return newRow;
    }

    public PeakListRow getPeak(int index) {
        return peaks[index];
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        for (PeakListRow d : peaks) {
            sb.append(d != null ? d.toString() : "GAP").append(' ');
        }
        return sb.toString();
    }

    public boolean isIdentified() {
        return identified;
    }

    public int compareTo(AlignmentPath o) {
        double diff = score - o.score;
        return (diff < 0) ? -1 : (diff == 0) ? 0 : 1;
    }

    public double getArea() {
        double areaSum = 0.0;
        for (PeakListRow d : peaks) {
            if (d != null) {
                areaSum += d.getAverageArea();
            }
        }
        return areaSum;
    }

    public double getConcentration() {
        double concentrationSum = 0.0;
        for (PeakListRow d : peaks) {
            if (d != null) {
                concentrationSum += d.getAverageHeight();
            }
        }
        return concentrationSum;
    }
}
