package hadit;

import nutils.CompareUtils;

public class CopyNumberRow implements Comparable<CopyNumberRow> {

    CopyNumberBitSet mRowBitSet;

    CopyNumberBitSetSpillover mRowBitSetSpillover;

    byte mChromosome;

    int mPosition;

    int mRsId;

    AlleleCountPair mAcpTotal;

    public static CopyNumberRow GetDummyCopyNumberRow(byte chromNum, int positionOnChrom) {
        return new CopyNumberRow(0, chromNum, 0, positionOnChrom);
    }

    public CopyNumberRow(int rowLength, byte chromNum, int rsID, int positionOnChrom) {
        mRowBitSet = new CopyNumberBitSet(rowLength);
        mRowBitSetSpillover = new CopyNumberBitSetSpillover();
        mChromosome = chromNum;
        mRsId = rsID;
        mPosition = positionOnChrom;
        mAcpTotal = new AlleleCountPair();
    }

    public byte getChromosome() {
        return mChromosome;
    }

    public int getPosition() {
        return mPosition;
    }

    public int getRsID() {
        return mRsId;
    }

    public AlleleCountPair getAlleleCountsTotal() {
        return mAcpTotal;
    }

    /** Needed by container for binary search.  Depends only on the chrom number and position. */
    public int compareTo(CopyNumberRow rhsCnr) {
        int result = CompareUtils.compareInt(getChromosome(), rhsCnr.getChromosome());
        if (result == 0) {
            result = CompareUtils.compareInt(getPosition(), rhsCnr.getPosition());
        }
        return result;
    }

    /** Converts the BitSet to a string. */
    public String toString() {
        return mRowBitSet.toString();
    }

    /** Adds the tallies to the total allele count pair for this row. */
    private void addToAcpTotal(byte count1, byte count2) {
        if (count1 != CopyNumberTableASCN.InvalidCountASCN) {
            mAcpTotal.incrAlleleCount(0);
        }
        if (count2 != CopyNumberTableASCN.InvalidCountASCN) {
            mAcpTotal.incrAlleleCount(1);
        }
    }

    public void getSampleCountsEnum(int sampleIndex, byte[] sampleCountsReturned) {
        mRowBitSet.getCountsForSampleIndex(sampleIndex, sampleCountsReturned);
    }

    public void setSampleCountsEnum(int sampleIndex, byte[] enumCode) {
        setSampleCountsEnum(sampleIndex, enumCode[0], enumCode[1]);
    }

    public void setSampleCountsEnum(int sampleIndex, byte count1, byte count2) {
        mRowBitSet.registerSampleIndexAndCounts(sampleIndex, count1, count2);
        addToAcpTotal(count1, count2);
    }

    public byte[] getSpilloverCountsForSampleIndex(short sampleIndex, byte[] sampleCountsReturned) {
        return mRowBitSetSpillover.getSpilloverCountsForSampleIndex(sampleIndex, sampleCountsReturned);
    }

    public void registerSpilloverSampleIndexAndCounts(short sampleIndex, byte count1, byte count2) {
        mRowBitSetSpillover.registerSpilloverSampleIndexAndCounts(sampleIndex, count1, count2);
        addToAcpTotal(count1, count2);
    }

    public static void TestCopyNumberRow() {
        CopyNumberRow cnr = new CopyNumberRow(6, (byte) 4, 0, 5000);
        cnr.setSampleCountsEnum(0, (byte) 1, (byte) 1);
        cnr.setSampleCountsEnum(1, (byte) 2, (byte) 0);
        cnr.setSampleCountsEnum(3, (byte) 0, (byte) 2);
        cnr.setSampleCountsEnum(2, (byte) 3, (byte) 1);
        cnr.setSampleCountsEnum(5, (byte) 3, (byte) 3);
        cnr.setSampleCountsEnum(4, (byte) 3, (byte) 3);
        System.out.println(cnr.toString());
        byte[] result = new byte[CopyNumberACPTranslator.NumAllelesInCallSet];
        cnr.getSampleCountsEnum(0, result);
        System.out.println("01 01 == " + result[0] + result[1]);
        cnr.getSampleCountsEnum(2, result);
        System.out.println("11 01 == " + result[0] + result[1]);
        cnr.getSampleCountsEnum(1, result);
        System.out.println("10 00 == " + result[0] + result[1]);
        cnr.getSampleCountsEnum(3, result);
        System.out.println("00 10 == " + result[0] + result[1]);
        cnr.getSampleCountsEnum(4, result);
        System.out.println("11 11 == " + result[0] + result[1]);
        cnr.getSampleCountsEnum(5, result);
        System.out.println("11 11 == " + result[0] + result[1]);
    }

    public static long GetFreeMemory() {
        return 0;
    }

    public static void TestBitSetSize() {
        for (int i = 0; i < 2000000000; i++) ;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        TestCopyNumberRow();
    }
}
