package org.catacombae.hfsexplorer.partitioning;

import org.catacombae.io.ReadableRandomAccessStream;
import org.catacombae.io.ReadableByteArrayStream;
import java.util.ArrayList;
import java.io.PrintStream;
import org.catacombae.hfsexplorer.Util;

public class ApplePartitionMap implements PartitionSystem {

    private final APMPartition[] partitions;

    public ApplePartitionMap(ReadableRandomAccessStream isoRaf, long pmOffset, int blockSize) {
        isoRaf.seek(pmOffset);
        byte[] currentBlock = new byte[blockSize];
        ArrayList<APMPartition> partitionList = new ArrayList<APMPartition>();
        Short pmSig = null;
        Short pmSigPad = null;
        Long pmMapBlkCnt = null;
        while (((partitionList.size() == 0 && pmMapBlkCnt == null) || (partitionList.size() > 0 && partitionList.size() < pmMapBlkCnt))) {
            isoRaf.readFully(currentBlock);
            APMPartition p = new APMPartition(currentBlock, 0, blockSize);
            if (p.isValid()) {
                short curPmSig = p.getPmSig();
                short curPmSigPad = p.getPmSigPad();
                long curPmMapBlkCnt = Util.unsign(p.getPmMapBlkCnt());
                if (pmMapBlkCnt != null && pmSigPad != null && pmSig != null) {
                    if (curPmSig != pmSig || curPmSigPad != pmSigPad || curPmMapBlkCnt != pmMapBlkCnt) throw new RuntimeException("Redundant fields mismatch at index: " + partitionList.size() + " (curPmSig=" + curPmSig + " pmSig=" + pmSig + " curPmSigPad=" + curPmSigPad + " pmSigPad=" + pmSigPad + " curPmMapBlkCnt=" + curPmMapBlkCnt + " pmMapBlkCnt=" + pmMapBlkCnt + ")");
                } else {
                    pmSig = curPmSig;
                    pmSigPad = curPmSigPad;
                    pmMapBlkCnt = curPmMapBlkCnt;
                }
                partitionList.add(p);
            } else {
                System.err.println("Erroneous partition:");
                p.printFields(System.err, "  ");
                throw new RuntimeException("Encountered invalid partition map entry at index: " + partitionList.size() + " pmMapBlkCnt=" + pmMapBlkCnt);
            }
        }
        partitions = partitionList.toArray(new APMPartition[partitionList.size()]);
    }

    public ApplePartitionMap(byte[] data, int off, int blockSize) {
        this(new ReadableByteArrayStream(data, 0, data.length), off, blockSize);
    }

    public boolean isValid() {
        if (partitions.length > 0) {
            for (APMPartition p : partitions) {
                if (!p.isValid()) return false;
            }
            return true;
        } else return false;
    }

    public int getPartitionCount() {
        return partitions.length;
    }

    public int getUsedPartitionCount() {
        return getPartitionCount();
    }

    /** index must be between 0 and getNumPartitions()-1. */
    public APMPartition getAPMPartition(int index) {
        return partitions[index];
    }

    public APMPartition[] getPartitionEntries() {
        APMPartition[] copy = new APMPartition[partitions.length];
        for (int i = 0; i < partitions.length; ++i) copy[i] = partitions[i];
        return copy;
    }

    public Partition[] getUsedPartitionEntries() {
        return getPartitionEntries();
    }

    public byte[] getData() {
        byte[] result = new byte[partitions.length * APMPartition.structSize()];
        int offset = 0;
        for (APMPartition ap : partitions) {
            byte[] tmp = ap.getData();
            System.arraycopy(tmp, 0, result, offset, tmp.length);
            offset += tmp.length;
        }
        if (offset != result.length) throw new RuntimeException("Internal miscalculation..."); else return result;
    }

    public void printFields(PrintStream ps, String prefix) {
        for (int i = 0; i < partitions.length; ++i) {
            ps.println(prefix + " partitions[" + i + "]:");
            partitions[i].print(ps, prefix + "  ");
        }
    }

    public void print(PrintStream ps, String prefix) {
        ps.println("Apple Partition Map:");
        printFields(ps, prefix);
    }

    public Partition getPartitionEntry(int index) {
        return getAPMPartition(index);
    }

    public String getLongName() {
        return "Apple Partition Map";
    }

    public String getShortName() {
        return "APM";
    }
}
