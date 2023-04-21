package org.gamenet.application.mm8leveleditor.data.mm7.fileFormat;

import java.util.ArrayList;
import java.util.List;
import org.gamenet.application.mm8leveleditor.data.DDecListBin;
import org.gamenet.swing.controls.ComparativeTableControl;
import org.gamenet.util.ByteConversions;

public class DDecListBinMM7 implements DDecListBin {

    public static final short ATTRIBUTE_MOVEMENT_NOT_BLOCKED = 1;

    public static final short ATTRIBUTE_NOT_DRAWN = 2;

    public static final short ATTRIBUTE_USE_SLOW_FLICKER = 4;

    public static final short ATTRIBUTE_USE_MEDIUM_FLICKER = 8;

    public static final short ATTRIBUTE_USE_FAST_FLICKER = 16;

    public static final short ATTRIBUTE_MARKER = 32;

    public static final short ATTRIBUTE_PLAY_SOUND_WITH_LONG_LOOP = 64;

    public static final short ATTRIBUTE_EMIT_FIRE_PARTICLES = 128;

    public static final short ATTRIBUTE_ONLY_PLAY_SOUND_AT_DAWN = 256;

    public static final short ATTRIBUTE_ONLY_PLAY_SOUND_AT_DUSK = 512;

    private static final int DDECLIST_BIN_RECORD_SIZE = 84;

    private int dDecListBinOffset = 0;

    private byte dDecListBinData[] = null;

    public DDecListBinMM7() {
        super();
    }

    public DDecListBinMM7(String fileName) {
        super();
        this.dDecListBinData = new byte[DDECLIST_BIN_RECORD_SIZE];
    }

    public int initialize(byte dataSrc[], int offset) {
        this.dDecListBinOffset = offset;
        this.dDecListBinData = new byte[DDECLIST_BIN_RECORD_SIZE];
        System.arraycopy(dataSrc, offset, this.dDecListBinData, 0, this.dDecListBinData.length);
        offset += this.dDecListBinData.length;
        return offset;
    }

    public static boolean checkDataIntegrity(byte[] data, int offset, int expectedNewOffset) {
        int count = ByteConversions.getIntegerInByteArrayAtPosition(data, offset);
        offset += 4;
        offset += count * DDECLIST_BIN_RECORD_SIZE;
        return (offset == expectedNewOffset);
    }

    public static int populateObjects(byte[] data, int offset, List dDecListBinList) {
        int dDecListBinCount = ByteConversions.getIntegerInByteArrayAtPosition(data, offset);
        offset += 4;
        for (int dDecListBinIndex = 0; dDecListBinIndex < dDecListBinCount; ++dDecListBinIndex) {
            DDecListBinMM7 dDecListBin = new DDecListBinMM7();
            dDecListBinList.add(dDecListBin);
            offset = dDecListBin.initialize(data, offset);
        }
        return offset;
    }

    public static int updateData(byte[] newData, int offset, List dDecListBinList) {
        ByteConversions.setIntegerInByteArrayAtPosition(dDecListBinList.size(), newData, offset);
        offset += 4;
        for (int dDecListBinIndex = 0; dDecListBinIndex < dDecListBinList.size(); ++dDecListBinIndex) {
            DDecListBinMM7 dDecListBin = (DDecListBinMM7) dDecListBinList.get(dDecListBinIndex);
            System.arraycopy(dDecListBin.getDDecListBinData(), 0, newData, offset, dDecListBin.getDDecListBinData().length);
            offset += dDecListBin.getDDecListBinData().length;
        }
        return offset;
    }

    public byte[] getDDecListBinData() {
        return this.dDecListBinData;
    }

    public int getDDecListBinOffset() {
        return this.dDecListBinOffset;
    }

    public static int getRecordSize() {
        return DDECLIST_BIN_RECORD_SIZE;
    }

    public static List getOffsetList() {
        List offsetList = new ArrayList();
        offsetList.add(new ComparativeTableControl.OffsetData(0, 32, ComparativeTableControl.REPRESENTATION_STRING));
        offsetList.add(new ComparativeTableControl.OffsetData(32, 32, ComparativeTableControl.REPRESENTATION_STRING));
        offsetList.add(new ComparativeTableControl.OffsetData(64, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "type?"));
        offsetList.add(new ComparativeTableControl.OffsetData(66, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "height?"));
        offsetList.add(new ComparativeTableControl.OffsetData(68, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "radius?"));
        offsetList.add(new ComparativeTableControl.OffsetData(70, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "light radius?"));
        offsetList.add(new ComparativeTableControl.OffsetData(72, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "Dsft ID #"));
        offsetList.add(new ComparativeTableControl.OffsetData(74, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "attr?"));
        offsetList.add(new ComparativeTableControl.OffsetData(76, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "DSounds ID #"));
        offsetList.add(new ComparativeTableControl.OffsetData(78, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "unused?"));
        offsetList.add(new ComparativeTableControl.OffsetData(79, 1, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "unused?"));
        offsetList.add(new ComparativeTableControl.OffsetData(80, 1, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "red light color?"));
        offsetList.add(new ComparativeTableControl.OffsetData(81, 1, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "green light color?"));
        offsetList.add(new ComparativeTableControl.OffsetData(82, 1, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "blue light color?"));
        return offsetList;
    }

    public static ComparativeTableControl.DataSource getComparativeDataSource(final List dDecListBinList) {
        return new ComparativeTableControl.DataSource() {

            public int getRowCount() {
                return dDecListBinList.size();
            }

            public byte[] getData(int dataRow) {
                DDecListBinMM7 dDecListBin = (DDecListBinMM7) dDecListBinList.get(dataRow);
                return dDecListBin.getDDecListBinData();
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
