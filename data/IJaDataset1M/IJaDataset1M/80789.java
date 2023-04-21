package hats;

import genomeEnums.Chrom;
import java.util.*;
import nutils.CompareUtils;

public class RegionRange {

    Chrom mChrom;

    boolean mRangeFinalized;

    int mRangeStart;

    int mRangeEnd;

    public RegionRange(Chrom chrom, int rangeStart) {
        mChrom = chrom;
        mRangeStart = rangeStart;
        mRangeEnd = mRangeStart;
        mRangeFinalized = false;
    }

    public RegionRange(Chrom chrom, int rangeStart, int rangeEnd) {
        mChrom = chrom;
        mRangeStart = rangeStart;
        mRangeEnd = mRangeStart;
        mRangeFinalized = false;
        extendRange(chrom, rangeEnd);
    }

    public RegionRange(RegionRange rhs) {
        mChrom = rhs.mChrom;
        mRangeStart = rhs.mRangeStart;
        mRangeEnd = rhs.mRangeEnd;
        mRangeFinalized = rhs.mRangeFinalized;
    }

    /** Returns the range start. */
    public int getRangeStart() {
        return mRangeStart;
    }

    /** Returns the range end. */
    public int getRangeEnd() {
        return mRangeEnd;
    }

    /** Returns the range length, inclusive of start and end. */
    public int getRangeLength() {
        return mRangeEnd - mRangeStart + 1;
    }

    /** Returns the chromosome. */
    public Chrom getChromosome() {
        return mChrom;
    }

    /** Makes the range finalized. */
    public void makeFinalized() {
        mRangeFinalized = true;
    }

    /** Returns whether this range is finalized or not. */
    public boolean isFinalized() {
        return mRangeFinalized;
    }

    /** Sets the range. */
    public void set(Chrom chrom, int rangeStart, int rangeEnd, boolean makeFinalized) {
        CompareUtils.ensureTrue(rangeEnd >= rangeStart, "ERROR: RegionRange.set(): Range end must follow range start!");
        mChrom = chrom;
        mRangeStart = rangeStart;
        mRangeEnd = rangeEnd;
        mRangeFinalized = makeFinalized;
    }

    /** Given a chromNum and a position, this checks whether the chrom
	 *  number and the position can be used to extend the range.  First, 
	 *  this checks that the chrom number and position do not already 
	 *  exist in the range.  If so, and the chrom number is appropriate,
	 *  then it adds it to the range, and true is returned.  Else, false. 
	 */
    public boolean extendRange(Chrom chrom, int position) {
        if (mRangeFinalized || (chrom != mChrom) || (position <= mRangeEnd)) {
            makeFinalized();
            return false;
        }
        mRangeEnd = position;
        return true;
    }

    /** Given a chromNum and a position, this returns whether in range. */
    public boolean inRange(Chrom chrom, int position) {
        return ((chrom.getCode() == mChrom.getCode()) && ((position >= mRangeStart) && (position <= mRangeEnd)));
    }

    /** Given a chromNum and a position, this returns whether it is after the range. */
    public boolean afterRange(Chrom chrom, int position) {
        return ((chrom.getCode() > mChrom.getCode()) || ((chrom.getCode() == mChrom.getCode()) && (position > mRangeEnd)));
    }

    /** Given a chromNum and a position, this returns whether it is before the range. */
    public boolean beforeRange(Chrom chrom, int position) {
        return ((chrom.getCode() < mChrom.getCode()) || ((chrom.getCode() == mChrom.getCode()) && (position < mRangeStart)));
    }

    /** Given a chromNum and a position, this returns whether there is any overlap.  */
    public boolean overlapRange(RegionRange r) {
        return (mChrom.getCode() == r.mChrom.getCode()) && ((r.mRangeStart >= mRangeStart) || (r.mRangeEnd <= mRangeEnd));
    }

    /** Returns this as a string. */
    public String toString() {
        StringBuilder sb = new StringBuilder(512);
        sb.append('[');
        sb.append(mChrom);
        sb.append(',');
        sb.append(mRangeStart);
        sb.append(',');
        sb.append(mRangeEnd);
        sb.append(']');
        return sb.toString();
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }

    public static class RegionRangeComparator implements Comparator<RegionRange> {

        public static final RegionRangeComparator TheComparator = new RegionRangeComparator();

        public boolean equals(Object o) {
            RegionRangeComparator rhs = (RegionRangeComparator) o;
            return true;
        }

        public int compare(RegionRange ar1, RegionRange ar2) {
            if (ar1.mRangeStart == ar1.mRangeEnd) {
                return compareHelper(ar1, ar2);
            } else {
                return (-1 * compareHelper(ar2, ar1));
            }
        }

        private int compareHelper(RegionRange ar1, RegionRange ar2) {
            if (ar1.mChrom.getCode() < ar2.mChrom.getCode()) {
                return -1;
            } else if (ar1.mChrom.getCode() > ar2.mChrom.getCode()) {
                return 1;
            } else {
                if (ar1.mRangeStart < ar2.mRangeStart) {
                    return -1;
                } else {
                    if (ar1.mRangeStart > ar2.mRangeEnd) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }
}
