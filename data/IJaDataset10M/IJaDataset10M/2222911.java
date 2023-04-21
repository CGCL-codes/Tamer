package org.catacombae.hfsexplorer.types.hfscommon;

import org.catacombae.hfsexplorer.types.hfsplus.HFSPlusExtentKey;
import org.catacombae.hfsexplorer.types.hfsplus.HFSPlusExtentLeafRecord;
import org.catacombae.hfsexplorer.types.hfsplus.HFSPlusExtentRecord;
import org.catacombae.hfsexplorer.types.hfs.ExtDataRec;
import org.catacombae.hfsexplorer.types.hfs.ExtKeyRec;

/**
 *
 * @author erik
 */
public abstract class CommonHFSExtentLeafNode extends CommonBTNode<CommonHFSExtentLeafRecord> {

    protected CommonHFSExtentLeafNode(byte[] data, int offset, int nodeSize, FSType type) {
        super(data, offset, nodeSize, type);
    }

    public CommonHFSExtentLeafRecord[] getLeafRecords() {
        return ic.records.toArray(new CommonHFSExtentLeafRecord[ic.records.size()]);
    }

    public static CommonHFSExtentLeafNode createHFS(byte[] data, int offset, int nodeSize) {
        return new HFSImplementation(data, offset, nodeSize);
    }

    public static CommonHFSExtentLeafNode createHFSPlus(byte[] data, int offset, int nodeSize) {
        return new HFSPlusImplementation(data, offset, nodeSize);
    }

    public static class HFSPlusImplementation extends CommonHFSExtentLeafNode {

        public HFSPlusImplementation(byte[] data, int offset, int nodeSize) {
            super(data, offset, nodeSize, FSType.HFS_PLUS);
        }

        @Override
        protected CommonHFSExtentLeafRecord createBTRecord(int recordNumber, byte[] data, int offset, int length) {
            final HFSPlusExtentKey key = new HFSPlusExtentKey(data, offset);
            final HFSPlusExtentRecord recordData = new HFSPlusExtentRecord(data, offset + key.length());
            return CommonHFSExtentLeafRecord.create(key, recordData);
        }
    }

    public static class HFSImplementation extends CommonHFSExtentLeafNode {

        public HFSImplementation(byte[] data, int offset, int nodeSize) {
            super(data, offset, nodeSize, FSType.HFS);
        }

        @Override
        protected CommonHFSExtentLeafRecord createBTRecord(int recordNumber, byte[] data, int offset, int length) {
            final ExtKeyRec key = new ExtKeyRec(data, offset);
            final ExtDataRec recordData = new ExtDataRec(data, offset + key.length());
            return CommonHFSExtentLeafRecord.create(key, recordData);
        }
    }
}
