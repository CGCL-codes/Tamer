package com.werno.wmflib.records;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.werno.wmflib.WMFConstants;

/**
 * This class implements the scale window ext record
 *
 * @author Peter Werno
 */
public class ScaleWindowExt extends ShapeRecord implements Record {

    /** Properties */
    short xNum;

    short yNum;

    short xDenom;

    short yDenom;

    /**
     * Creates a new (blank) instance of ScaleWindowExt. The properties can
     * then be set via the read method or the property setters.
     */
    public ScaleWindowExt() {
        this.xNum = 0;
        this.yNum = 0;
        this.xDenom = 0;
        this.yDenom = 0;
    }

    /**
     * Creates a new instance of ScaleWindowExt with all properties preset
     *
     * @param xNum (short)
     * @param xDenom (short)
     * @param yNum (short)
     * @param yDenom (short)
     */
    public ScaleWindowExt(short xNum, short xDenom, short yNum, short yDenom) {
        this.xNum = xNum;
        this.xDenom = xDenom;
        this.yNum = yNum;
        this.yDenom = yDenom;
    }

    /**
     * Creates a new instance of ScaleWindowExt and reads all properties
     * from a stream
     *
     * @param in (InputStream) the stream
     * @throws IOException
     */
    public ScaleWindowExt(InputStream in) throws IOException {
        this.read(in);
    }

    /**
     * Setter for property xNum
     *
     * @param xNum (short) new value of property xNum
     */
    public void setXNum(short xNum) {
        this.xNum = xNum;
    }

    /**
     * Getter for property xNum
     *
     * @return the value of the property xNum (short)
     */
    public short getXNum() {
        return this.xNum;
    }

    /**
     * Setter for property xDenom
     *
     * @param xDenom (short) new value of property xDenom
     */
    public void setXDenom(short xDenom) {
        this.xDenom = xDenom;
    }

    /**
     * Getter for property xDenom
     *
     * @return the value of the property xDenom (short)
     */
    public short getXDenom() {
        return this.xDenom;
    }

    /**
     * Setter for property yNum
     *
     * @param yNum (short) new value of property yNum
     */
    public void setYNum(short yNum) {
        this.yNum = yNum;
    }

    /**
     * Getter for property yNum
     *
     * @return the value of the property yNum (short)
     */
    public short getYNum() {
        return this.yNum;
    }

    /**
     * Setter for property yDenom
     *
     * @param yDenom (short) new value of property yDenom
     */
    public void setYDenom(short yDenom) {
        this.yDenom = yDenom;
    }

    /**
     * Getter for property yDenom
     *
     * @return the value of the property yDenom (short)
     */
    public short getYDenom() {
        return this.yDenom;
    }

    /**
     * Writes the content of the ScaleWindowExt record to a stream
     *
     * @param out (OutputStream) the stream
     * @throws IOException
     */
    public void write(OutputStream out) throws IOException {
        int size = this.getSize();
        WMFConstants.writeLittleEndian(out, size);
        WMFConstants.writeLittleEndian(out, WMFConstants.WMF_RECORD_SCALEWINDOWEXT);
        WMFConstants.writeLittleEndian(out, this.yDenom);
        WMFConstants.writeLittleEndian(out, this.yNum);
        WMFConstants.writeLittleEndian(out, this.xDenom);
        WMFConstants.writeLittleEndian(out, this.xNum);
    }

    /**
     * Reads the content of a ScaleWindowExt record from a stream
     *
     * @param in (InputStream) the stream
     * @throws IOException
     */
    public void read(InputStream in) throws IOException {
        this.yDenom = WMFConstants.readLittleEndianShort(in);
        this.yNum = WMFConstants.readLittleEndianShort(in);
        this.xDenom = WMFConstants.readLittleEndianShort(in);
        this.xNum = WMFConstants.readLittleEndianShort(in);
    }

    /**
     * Returns the size of the record
     *
     * @return the size (short)
     */
    public short getSize() {
        return 7;
    }

    /**
     * Returns the content of the ScaleWindowExt record in a human readable
     * format
     *
     * @return the content (String)
     */
    @Override
    public String toString() {
        return "ScaleWindowExt (" + this.xNum + "/" + this.xDenom + ") - (" + this.yNum + "/" + this.yDenom + ")";
    }
}
