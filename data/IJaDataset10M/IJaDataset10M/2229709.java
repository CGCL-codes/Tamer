package com.werno.wmflib.records;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.werno.wmflib.WMFConstants;

/**
 * This class implements the offset window org record
 *
 * @author Peter Werno
 */
public class OffsetWindowOrg extends ShapeRecord implements Record {

    /** Properties */
    short xOffset;

    short yOffset;

    /**
     * Creates a new (blank) instance of OffsetWindowOrg. The properties can then
     * be set via the read method or the property setters
     */
    public OffsetWindowOrg() {
        this.xOffset = 0;
        this.yOffset = 0;
    }

    /**
     * Creates a new instance of OffsetWindowOrg with all properties preset.
     *
     * @param xOffset (short) the x offset
     * @param yOffset (short) the y offset
     */
    public OffsetWindowOrg(short xOffset, short yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /**
     * Creates a new instance of OffsetWindowOrg and reads all properties from
     * a stream
     *
     * @param in (InputStream) the stream
     * @throws IOException
     */
    public OffsetWindowOrg(InputStream in) throws IOException {
        this.read(in);
    }

    /**
     * Setter for property xOffset
     *
     * @param xOffset (short) new value of property xOffset
     */
    public void setXOffset(short xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * Getter for property xOffset
     *
     * @return the value of the property xOffset (short)
     */
    public short getXOffset() {
        return this.xOffset;
    }

    /**
     * Setter for property yOffset
     *
     * @param yOffset (short) new value of property yOffset
     */
    public void setYOffset(short yOffset) {
        this.yOffset = yOffset;
    }

    /**
     * Getter for property yOffset
     *
     * @return the value of the property yOffset (short)
     */
    public short getYOffset() {
        return this.yOffset;
    }

    /**
     * Writes the content of the OffsetWindowOrg record to a stream
     *
     * @param out (OutputStream) the stream
     * @throws IOException
     */
    public void write(OutputStream out) throws IOException {
        int size = this.getSize();
        WMFConstants.writeLittleEndian(out, size);
        WMFConstants.writeLittleEndian(out, WMFConstants.WMF_RECORD_OFFSETWINDOWORG);
        WMFConstants.writeLittleEndian(out, this.yOffset);
        WMFConstants.writeLittleEndian(out, this.xOffset);
    }

    /**
     * Reads the content of an OffsetWindowOrg from a stream
     *
     * @param in (InputStream) the stream
     * @throws IOException
     */
    public void read(InputStream in) throws IOException {
        this.yOffset = WMFConstants.readLittleEndianShort(in);
        this.xOffset = WMFConstants.readLittleEndianShort(in);
    }

    /**
     * Returns the size of the record
     *
     * @return the size (short)
     */
    public short getSize() {
        return 5;
    }

    /**
     * Returns the content of the OffsetWindowOrg record in a human readable
     * format
     *
     * @return the content (String)
     */
    @Override
    public String toString() {
        return "OffsetWindowOrg: (" + this.xOffset + "/" + this.yOffset + ")";
    }
}
