package phasedDataStruc;

import nutils.BitSetByUnit;
import nutils.CompareUtils;
import nutils.ObjectPoolUnlimited;
import dynamicArray.DArrayInteger;
import genomeEnums.Nuc;
import java.util.*;

/**
 * Modified by Yang Hu
 * An implementation with BitSetByUnit
 */
public class HaplotypesWithCountsUnlimited extends HaplotypesWithCounts {

    int mHaplotypeLength;

    public ArrayList<BitSetByUnit> mHaplotypes;

    public DArrayInteger mHaplotypeCounts;

    StringBuilder mSB;

    public HaplotypesWithCountsUnlimited() {
        mHaplotypes = new ArrayList<BitSetByUnit>();
        mHaplotypeCounts = new DArrayInteger();
        mSB = new StringBuilder(128);
    }

    public void clear() {
        mHaplotypeLength = 0;
        mHaplotypeCounts.clear();
        mSB.delete(0, mSB.length());
        for (BitSetByUnit b : mHaplotypes) {
            b.clear(true);
            BitSetByUnitPool.GlobalBSBUPool.returnElement(b);
        }
        mHaplotypes.clear();
    }

    public DArrayInteger getCountsOnly() {
        return mHaplotypeCounts;
    }

    public int getSumOfCounts() {
        return mHaplotypeCounts.getSumOfCounts();
    }

    /** Sets the length. */
    public boolean isEmpty() {
        return mHaplotypes.size() == 0;
    }

    public int getLength() {
        return mHaplotypeLength;
    }

    public void setLength(int hapLength) {
        mHaplotypeLength = hapLength;
    }

    /** This makes a copy of the BitSetByUnit argument and stores this copy. */
    private void add(BitSetByUnit haplotype, int count) {
        BitSetByUnit b = BitSetByUnitPool.GlobalBSBUPool.borrowElement();
        b.set(haplotype);
        mHaplotypes.add(b);
        mHaplotypeCounts.add(count);
    }

    /** This makes a copy of the BitSetByUnit argument and stores this copy. */
    private void add(int index, BitSetByUnit haplotype, int count) {
        BitSetByUnit b = BitSetByUnitPool.GlobalBSBUPool.borrowElement();
        b.set(haplotype);
        mHaplotypes.add(index, b);
        mHaplotypeCounts.add(index, count);
    }

    /** Given a haplotype, this tests whether the haplotype exists in the haplotype list.
	 *  If so, the count of the particular haplotype in the list is incremented.  If not,
	 *  the haplotype is added to the list with a count of 1. 
	 */
    public void registerHaplotype(BitSetByUnit b) {
        int resultIndex = Collections.binarySearch(mHaplotypes, b);
        if (resultIndex >= 0) {
            mHaplotypeCounts.set(resultIndex, mHaplotypeCounts.get(resultIndex) + 1);
        } else {
            add(-(resultIndex + 1), b, 1);
        }
    }

    public int searchHaplotype(BitSetByUnit b) {
        return Collections.binarySearch(mHaplotypes, b);
    }

    /** @param numBitsPerAllele This value is ignored, as it's already represented in the data. */
    public ArrayList<String> convertHaplotypesToString() {
        ArrayList<String> rV = new ArrayList<String>(mHaplotypes.size());
        for (BitSetByUnit bsbu : mHaplotypes) {
            convertHaplotypeToString(bsbu, mHaplotypeLength, mSB);
            rV.add(mSB.toString());
        }
        return rV;
    }

    /** This converts the haplotype to a string form, given the length of the
	 *  haplotype.  This assumes that two bits are used to encode each character. */
    public static StringBuilder convertHaplotypeToString(BitSetByUnit b, int haplotypeLength, StringBuilder sb) {
        sb.delete(0, sb.length());
        CompareUtils.ensureTrue(b.getNumUnits() == haplotypeLength, "ERROR: convertHaplotypeToString(): Number of units incongruous with determined length!");
        for (int i = 0; i < haplotypeLength; i++) {
            sb.append(Nuc.getAllele(b.get(i)).getChar());
        }
        return sb;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }

    public static class BitSetByUnitPool extends ObjectPoolUnlimited<BitSetByUnit> {

        public static final BitSetByUnitPool GlobalBSBUPool = new BitSetByUnitPool();

        public BitSetByUnitPool() {
            super("BitSetByUnitPool");
        }

        protected BitSetByUnit instantiateNewObject() {
            return new BitSetByUnit(HapDataStrucUnlimited.SIZE_OF_ALLELE_ENCODING, 0);
        }
    }
}
