package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

/**
 * Title: MMS Record<P>
 * Description: defines how many add menu and del menu options are stored
 *                    in the file. Should always be set to 0 for HSSF workbooks<P>
 * REFERENCE:  PG 328 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */
public final class MMSRecord extends StandardRecord {

    public static final short sid = 0xC1;

    private byte field_1_addMenuCount;

    private byte field_2_delMenuCount;

    public MMSRecord() {
    }

    public MMSRecord(RecordInputStream in) {
        field_1_addMenuCount = in.readByte();
        field_2_delMenuCount = in.readByte();
    }

    /**
     * set number of add menu options (set to 0)
     * @param am  number of add menu options
     */
    public void setAddMenuCount(byte am) {
        field_1_addMenuCount = am;
    }

    /**
     * set number of del menu options (set to 0)
     * @param dm  number of del menu options
     */
    public void setDelMenuCount(byte dm) {
        field_2_delMenuCount = dm;
    }

    /**
     * get number of add menu options (should be 0)
     * @return number of add menu options
     */
    public byte getAddMenuCount() {
        return field_1_addMenuCount;
    }

    /**
     * get number of add del options (should be 0)
     * @return number of add menu options
     */
    public byte getDelMenuCount() {
        return field_2_delMenuCount;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[MMS]\n");
        buffer.append("    .addMenu        = ").append(Integer.toHexString(getAddMenuCount())).append("\n");
        buffer.append("    .delMenu        = ").append(Integer.toHexString(getDelMenuCount())).append("\n");
        buffer.append("[/MMS]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeByte(getAddMenuCount());
        out.writeByte(getDelMenuCount());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
