package org.gamenet.application.mm8leveleditor.data.mm6.fileFormat;

import java.util.ArrayList;
import java.util.List;
import org.gamenet.swing.controls.ComparativeTableControl;
import org.gamenet.util.ByteConversions;

public class DChestBin {

    private static final int DCHEST_BIN_RECORD_SIZE = 36;

    private int dChestBinOffset = 0;

    private byte dChestBinData[] = null;

    public DChestBin() {
        super();
    }

    public DChestBin(String fileName) {
        super();
        this.dChestBinData = new byte[DCHEST_BIN_RECORD_SIZE];
    }

    public int initialize(byte dataSrc[], int offset) {
        this.dChestBinOffset = offset;
        this.dChestBinData = new byte[DCHEST_BIN_RECORD_SIZE];
        System.arraycopy(dataSrc, offset, this.dChestBinData, 0, this.dChestBinData.length);
        offset += this.dChestBinData.length;
        return offset;
    }

    public static boolean checkDataIntegrity(byte[] data, int offset, int expectedNewOffset) {
        int count = ByteConversions.getIntegerInByteArrayAtPosition(data, offset);
        offset += 4;
        offset += count * DCHEST_BIN_RECORD_SIZE;
        return (offset == expectedNewOffset);
    }

    public static int populateObjects(byte[] data, int offset, List dChestBinList) {
        int dChestBinCount = ByteConversions.getIntegerInByteArrayAtPosition(data, offset);
        offset += 4;
        for (int dChestBinIndex = 0; dChestBinIndex < dChestBinCount; ++dChestBinIndex) {
            DChestBin dChestBin = new DChestBin();
            dChestBinList.add(dChestBin);
            offset = dChestBin.initialize(data, offset);
        }
        return offset;
    }

    public static int updateData(byte[] newData, int offset, List dChestBinList) {
        ByteConversions.setIntegerInByteArrayAtPosition(dChestBinList.size(), newData, offset);
        offset += 4;
        for (int dChestBinIndex = 0; dChestBinIndex < dChestBinList.size(); ++dChestBinIndex) {
            DChestBin dChestBin = (DChestBin) dChestBinList.get(dChestBinIndex);
            System.arraycopy(dChestBin.getDChestBinData(), 0, newData, offset, dChestBin.getDChestBinData().length);
            offset += dChestBin.getDChestBinData().length;
        }
        return offset;
    }

    public byte[] getDChestBinData() {
        return this.dChestBinData;
    }

    public int getDChestBinOffset() {
        return this.dChestBinOffset;
    }

    public static int getRecordSize() {
        return DCHEST_BIN_RECORD_SIZE;
    }

    public static List getOffsetList() {
        List offsetList = new ArrayList();
        offsetList.add(new ComparativeTableControl.OffsetData(0, 32, ComparativeTableControl.REPRESENTATION_STRING, "Name"));
        offsetList.add(new ComparativeTableControl.OffsetData(32, 1, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "Width"));
        offsetList.add(new ComparativeTableControl.OffsetData(33, 1, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "Height"));
        offsetList.add(new ComparativeTableControl.OffsetData(34, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "Image ID #"));
        return offsetList;
    }

    public static ComparativeTableControl.DataSource getComparativeDataSource(final List dChestBinList) {
        return new ComparativeTableControl.DataSource() {

            public int getRowCount() {
                return dChestBinList.size();
            }

            public byte[] getData(int dataRow) {
                DChestBin dChestBin = (DChestBin) dChestBinList.get(dataRow);
                return dChestBin.getDChestBinData();
            }

            public int getAdjustedDataRowIndex(int dataRow) {
                return dataRow;
            }

            public String getIdentifier(int dataRow) {
                return "";
            }

            public int getOffset(int dataRow) {
                return 0;
            }
        };
    }
}
