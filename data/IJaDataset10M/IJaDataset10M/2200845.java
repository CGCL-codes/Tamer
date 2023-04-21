package net.sf.joafip.btreeplus.entity;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import net.sf.joafip.NoStorableAccess;
import net.sf.joafip.NotStorableClass;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@NoStorableAccess
public class TestDataBlockContext {

    private static class DataBlockInfo {

        private final byte contentValue;

        private final long position;

        public DataBlockInfo(final DataBlock dataBlock, final byte contentValue) {
            super();
            this.contentValue = contentValue;
            this.position = dataBlock.getPositionInFile();
        }

        public byte getContentValue() {
            return contentValue;
        }

        public long getPosition() {
            return position;
        }
    }

    private final byte bits;

    private final List<DataBlockInfo> dataBlockInfoList = new ArrayList<DataBlockInfo>();

    private long position;

    private Deque<Long> positionDeque;

    public TestDataBlockContext(final byte bits) {
        super();
        this.bits = bits;
    }

    public byte getBits() {
        return bits;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(final long position) {
        this.position = position;
    }

    public void addDataBlock(final DataBlock dataBlock, final byte contentValue) {
        final DataBlockInfo dataBlockInfo = new DataBlockInfo(dataBlock, contentValue);
        dataBlockInfoList.add(dataBlockInfo);
    }

    public int size() {
        return dataBlockInfoList.size();
    }

    public byte getContentValue(final int index) {
        return dataBlockInfoList.get(index).getContentValue();
    }

    public long getPosition(final int index) {
        return dataBlockInfoList.get(index).getPosition();
    }

    public void setPositionDeque(final Deque<Long> positionDeque) {
        this.positionDeque = positionDeque;
    }

    public Deque<Long> getPositionDeque() {
        return positionDeque;
    }
}
