package net.sourceforge.poi.hssf.record;

import net.sourceforge.poi.util.LittleEndian;

/**
 * End Of File record.
 * <P>
 * Description:  Marks the end of records belonging to a particular object in the
 *               HSSF File<P>
 * Copyright:    Copyright (c) 2001 SuperLink Software, Inc. <P>
 * REFERENCE:  PG 307 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (andycoliver@excite.com)
 * @version 2.0-pre
 */
public class EOFRecord extends Record {

    public static final short sid = 0x0A;

    public EOFRecord() {
    }

    /**
     * Constructs a EOFRecord record and sets its fields appropriately.
     *
     * @param short id must be 0x0A or an exception will be throw upon validation
     * @param short size the size of the data area of the record
     * @param byte[] data of the record (should not contain sid/len)
     */
    public EOFRecord(short id, short size, byte[] data) {
        super(id, size, data);
    }

    /**
     * Constructs a EOFRecord record and sets its fields appropriately.
     *
     * @param short id must be 0x0A or an exception will be throw upon validation
     * @param short size the size of the data area of the record
     * @param byte[] data of the record (should not contain sid/len)
     * @param offset of the record's data
     */
    public EOFRecord(short id, short size, byte[] data, int offset) {
        super(id, size, data, offset);
    }

    protected void validateSid(short id) {
        if (id != sid) {
            throw new RecordFormatException("NOT An EOF RECORD");
        }
    }

    protected void fillFields(byte[] data, short size, int offset) {
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[EOF]\n");
        buffer.append("[/EOF]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte[] data) {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, ((short) 0));
        return getRecordSize();
    }

    public int getRecordSize() {
        return 4;
    }

    public short getSid() {
        return this.sid;
    }
}
