package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

/**
 * The units record describes units.<p/>
 * 
 * @author Glen Stampoultzis (glens at apache.org)
 */
public final class UnitsRecord extends Record {

    public static final short sid = 0x1001;

    private short field_1_units;

    public UnitsRecord() {
    }

    public UnitsRecord(RecordInputStream in) {
        field_1_units = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[UNITS]\n");
        buffer.append("    .units                = ").append("0x").append(HexDump.toHex(getUnits())).append(" (").append(getUnits()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/UNITS]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte[] data) {
        int pos = 0;
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, (short) (getRecordSize() - 4));
        LittleEndian.putShort(data, 4 + offset + pos, field_1_units);
        return getRecordSize();
    }

    public int getRecordSize() {
        return 4 + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        UnitsRecord rec = new UnitsRecord();
        rec.field_1_units = field_1_units;
        return rec;
    }

    /**
     * Get the units field for the Units record.
     */
    public short getUnits() {
        return field_1_units;
    }

    /**
     * Set the units field for the Units record.
     */
    public void setUnits(short field_1_units) {
        this.field_1_units = field_1_units;
    }
}
