package com.werno.wmflib.records;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.werno.wmflib.WMFConstants;

/**
 * This class implements the select palette record
 *
 * @author Peter Werno
 */
public class SelectPalette extends ShapeRecord implements Record {

    /** Properties */
    short palette;

    /**
     * Creates a new (blank) instance of SelectPalette. The properties can then
     * be filled via the read method or the property setters.
     */
    public SelectPalette() {
        this.palette = 0;
    }

    /**
     * Creates a new instance of SelectPalette with all properties preset
     *
     * @param palette (short) the palette object number
     */
    public SelectPalette(short palette) {
        this.palette = palette;
    }

    /**
     * Setter for property palette
     *
     * @param palette (short) new value of property palette
     */
    public void setPalette(short palette) {
        this.palette = palette;
    }

    /**
     * Getter for property palette
     * 
     * @return the value of the property palette (short)
     */
    public short getPalette() {
        return this.palette;
    }

    /**
     * Writes the content of the select palette record to a stream
     *
     * @param out (OutputStream) the stream
     * @throws IOException
     */
    public void write(OutputStream out) throws IOException {
        int size = this.getSize();
        WMFConstants.writeLittleEndian(out, size);
        WMFConstants.writeLittleEndian(out, WMFConstants.WMF_RECORD_SELECTPALETTE);
        WMFConstants.writeLittleEndian(out, palette);
    }

    /**
     * Reads the content of a select palette record from a stream
     *
     * @param in (InputStream) the stream
     * @throws IOException
     */
    public void read(InputStream in) throws IOException {
        this.palette = WMFConstants.readLittleEndianShort(in);
    }

    /**
     * Returns the size of the record
     *
     * @return the size (short)
     */
    public short getSize() {
        return 4;
    }

    /**
     * Returns the content of the select palette record in a human readable
     * format
     *
     * @return the content (String)
     */
    @Override
    public String toString() {
        return "SelectPalette: palette: " + this.palette;
    }
}
