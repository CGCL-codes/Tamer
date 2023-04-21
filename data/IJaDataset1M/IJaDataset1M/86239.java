package org.gamenet.application.mm8leveleditor.data.mm6.fileFormat;

import java.util.ArrayList;
import java.util.List;
import org.gamenet.application.mm8leveleditor.data.DsftBinSprite;
import org.gamenet.swing.controls.ComparativeTableControl;
import org.gamenet.util.ByteConversions;

public class DsftBinSpriteMM6 implements DsftBinSprite {

    public static final int DSFT_SPRITE_RECORD_LENGTH = 56;

    protected static final int SET_NAME_OFFSET = 0x00;

    protected static final int SET_NAME_MAXLENGTH = 12;

    protected static final int SPRITE_NAME_OFFSET = 12;

    protected static final int SPRITE_NAME_MAXLENGTH = 28;

    private static final int SPRITE_FRAME_PTR_TABLE_OFFSET = 24;

    private static final int SCALE_OFFSET = 40;

    private static final int ATTRIBUTE_MASK_OFFSET = 44;

    private static final int LIGHT_RADIUS_OFFSET = 46;

    private static final int PALETTE_ID_OFFSET = 48;

    private static final int PALETTE_INDEX_OFFSET = 50;

    private static final int FRAME_DISPLAY_TIME_OFFSET = 52;

    private static final int TOTAL_ANIMATION_TIME_OFFSET = 54;

    protected static final int CONTINUATION_OFFSET = 54;

    private String spriteName = null;

    private byte data[] = null;

    private long offset = 0;

    public DsftBinSpriteMM6() {
        super();
    }

    public int initialize(byte[] data, int offset) {
        this.offset = offset;
        this.data = new byte[DSFT_SPRITE_RECORD_LENGTH];
        System.arraycopy(data, offset, this.data, 0, DSFT_SPRITE_RECORD_LENGTH);
        offset += DSFT_SPRITE_RECORD_LENGTH;
        return offset;
    }

    public int updateData(byte[] newData, int offset) {
        System.arraycopy(this.data, 0, newData, offset, DSFT_SPRITE_RECORD_LENGTH);
        offset += DSFT_SPRITE_RECORD_LENGTH;
        return offset;
    }

    public boolean hasMoreRecords() {
        int continuation = ByteConversions.getShortInByteArrayAtPosition(data, CONTINUATION_OFFSET);
        if ((this.getSetName().length() != 0) && (continuation > 1)) return true;
        return (0 == continuation);
    }

    public int getContinuation() {
        return ByteConversions.getShortInByteArrayAtPosition(data, CONTINUATION_OFFSET);
    }

    public String getSpriteName() {
        return ByteConversions.getZeroTerminatedStringInByteArrayAtPositionMaxLength(this.data, SPRITE_NAME_OFFSET, SPRITE_NAME_MAXLENGTH);
    }

    public void setSpriteName(String spriteName) {
        ByteConversions.setZeroTerminatedStringInByteArrayAtPositionMaxLength(spriteName, this.data, SPRITE_NAME_OFFSET, SPRITE_NAME_MAXLENGTH);
    }

    public String getSetName() {
        return ByteConversions.getZeroTerminatedStringInByteArrayAtPositionMaxLength(this.data, SET_NAME_OFFSET, SET_NAME_MAXLENGTH);
    }

    public void setSetName(String setName) {
        ByteConversions.setZeroTerminatedStringInByteArrayAtPositionMaxLength(setName, this.data, SET_NAME_OFFSET, SET_NAME_MAXLENGTH);
    }

    public byte[] getData() {
        return this.data;
    }

    public long getOffset() {
        return this.offset;
    }

    public int getRecordSize() {
        return getStaticRecordSize();
    }

    public static int getStaticRecordSize() {
        return DSFT_SPRITE_RECORD_LENGTH;
    }

    public static List getOffsetList() {
        List offsetList = new ArrayList();
        offsetList.add(new ComparativeTableControl.OffsetData(0, 12, ComparativeTableControl.REPRESENTATION_STRING, "set name"));
        offsetList.add(new ComparativeTableControl.OffsetData(12, 12, ComparativeTableControl.REPRESENTATION_STRING, "file name"));
        offsetList.add(new ComparativeTableControl.OffsetData(24, 16, ComparativeTableControl.REPRESENTATION_STRING));
        offsetList.add(new ComparativeTableControl.OffsetData(40, 4, ComparativeTableControl.REPRESENTATION_INT_DEC));
        offsetList.add(new ComparativeTableControl.OffsetData(44, 4, ComparativeTableControl.REPRESENTATION_INT_HEX, "attributes"));
        offsetList.add(new ComparativeTableControl.OffsetData(48, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "palette index"));
        offsetList.add(new ComparativeTableControl.OffsetData(50, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC));
        offsetList.add(new ComparativeTableControl.OffsetData(52, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "frame time"));
        offsetList.add(new ComparativeTableControl.OffsetData(54, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "total time"));
        return offsetList;
    }
}
