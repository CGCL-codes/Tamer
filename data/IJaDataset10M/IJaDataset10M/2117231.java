package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

/**
 * Title: Codepage Record<P>
 * Description:  the default characterset. for the workbook<P>
 * REFERENCE:  PG 293 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */
public final class CodepageRecord extends StandardRecord {

    public static final short sid = 0x42;

    private short field_1_codepage;

    /**
     * the likely correct value for CODEPAGE (at least for US versions).  We could use
     * some help with international versions (which we do not have access to documentation
     * for)
     */
    public static final short CODEPAGE = (short) 0x4b0;

    public CodepageRecord() {
    }

    public CodepageRecord(RecordInputStream in) {
        field_1_codepage = in.readShort();
    }

    /**
     * set the codepage for this workbook
     *
     * @see #CODEPAGE
     * @param cp the codepage to set
     */
    public void setCodepage(short cp) {
        field_1_codepage = cp;
    }

    /**
     * get the codepage for this workbook
     *
     * @see #CODEPAGE
     * @return codepage - the codepage to set
     */
    public short getCodepage() {
        return field_1_codepage;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CODEPAGE]\n");
        buffer.append("    .codepage        = ").append(Integer.toHexString(getCodepage())).append("\n");
        buffer.append("[/CODEPAGE]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getCodepage());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
