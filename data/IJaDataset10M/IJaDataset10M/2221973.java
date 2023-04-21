package org.catacombae.hfsexplorer.types.hfsplus;

import org.catacombae.hfsexplorer.Util;
import java.io.PrintStream;

public class HFSPlusExtentLeafNode extends BTLeafNode {

    protected HFSPlusExtentLeafRecord[] leafRecords;

    protected short[] leafRecordOffsets;

    public HFSPlusExtentLeafNode(byte[] data, int offset, int nodeSize) {
        super(data, offset, nodeSize);
        leafRecordOffsets = new short[Util.unsign(nodeDescriptor.getNumRecords()) + 1];
        for (int i = 0; i < leafRecordOffsets.length; ++i) {
            leafRecordOffsets[i] = Util.readShortBE(data, offset + nodeSize - ((i + 1) * 2));
        }
        leafRecords = new HFSPlusExtentLeafRecord[leafRecordOffsets.length - 1];
        for (int i = 0; i < leafRecords.length; ++i) {
            int currentOffset = Util.unsign(leafRecordOffsets[i]);
            leafRecords[i] = new HFSPlusExtentLeafRecord(data, offset + currentOffset);
        }
    }

    public short[] getLeafRecordOffsets() {
        short[] offsets = new short[leafRecordOffsets.length];
        for (int i = 0; i < offsets.length; ++i) {
            offsets[i] = leafRecordOffsets[i];
        }
        return offsets;
    }

    public HFSPlusExtentLeafRecord getLeafRecord(int index) {
        return leafRecords[index];
    }

    public HFSPlusExtentLeafRecord[] getLeafRecords() {
        HFSPlusExtentLeafRecord[] copy = new HFSPlusExtentLeafRecord[leafRecords.length];
        for (int i = 0; i < copy.length; ++i) copy[i] = leafRecords[i];
        return copy;
    }

    public void printFields(PrintStream ps, String prefix) {
        ps.println(prefix + " nodeDescriptor:");
        nodeDescriptor.printFields(ps, prefix + "  ");
        for (int i = 0; i < leafRecords.length; ++i) {
            ps.println(prefix + " leafRecords[" + i + "]:");
            leafRecords[i].printFields(ps, prefix + "  ");
        }
    }

    public void print(PrintStream ps, String prefix) {
        ps.println(prefix + "HFSPlusExtentLeafNode:");
        printFields(ps, prefix);
    }
}
