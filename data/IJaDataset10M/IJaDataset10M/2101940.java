package com.werno.wmflib.records;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.werno.wmflib.WMFConstants;
import com.werno.wmflib.records.objects.DIBitmap;

/**
 * This class implements the DIB bit BLT record
 *
 * @author Peter Werno
 */
public class DIBBitBLT extends ShapeRecord implements Record {

    /** Properties */
    int rasterOperation;

    short xSrc;

    short ySrc;

    short width;

    short height;

    short xDest;

    short yDest;

    DIBitmap target;

    /**
     * Creates a new (blank) instance of DIBBitBLT. The properties can then be
     * set via the read method or the property setters.
     */
    public DIBBitBLT() {
        this.rasterOperation = 0;
        this.xSrc = 0;
        this.ySrc = 0;
        this.width = 0;
        this.height = 0;
        this.xDest = 0;
        this.yDest = 0;
        this.target = null;
    }

    /**
     * Creates a new instance of DIBBitBLT with all properties preset
     *
     * @param rasterOperation (int)
     * @param xSrc (short)
     * @param ySrc (short)
     * @param width (short)
     * @param height (short)
     * @param xDest (short)
     * @param yDest (short)
     * @param target (DIBitmap)
     */
    public DIBBitBLT(int rasterOperation, short xSrc, short ySrc, short width, short height, short xDest, short yDest, DIBitmap target) {
        this.rasterOperation = rasterOperation;
        this.xSrc = xSrc;
        this.ySrc = ySrc;
        this.width = width;
        this.height = height;
        this.xDest = xDest;
        this.yDest = yDest;
        this.target = target;
    }

    /**
     * Creates a new instance of DIBBitBLT and reads all properties from
     * a stream
     *
     * @param in (InputStream) the stream
     * @throws IOException
     */
    public DIBBitBLT(InputStream in) throws IOException {
        this.read(in);
    }

    /**
     * Setter for property rasterOperation
     *
     * @param rasterOperation (int) new value of property rasterOperation
     */
    public void setRasterOperation(int rasterOperation) {
        this.rasterOperation = rasterOperation;
    }

    /**
     * Getter for property rasterOperation
     *
     * @return the value of the property rasterOperation (int)
     */
    public int getRasterOperation() {
        return this.rasterOperation;
    }

    /**
     * Setter for the property xSrc
     *
     * @param xSrc (short) new value of the property xSrc
     */
    public void setXSrc(short xSrc) {
        this.xSrc = xSrc;
    }

    /**
     * Getter for the property xSrc
     *
     * @return the value of the property xSrc (short)
     */
    public short getXSrc() {
        return this.xSrc;
    }

    /**
     * Setter for the property ySrc
     *
     * @param ySrc (short) new value of the property ySrc
     */
    public void setYSrc(short ySrc) {
        this.ySrc = ySrc;
    }

    /**
     * Getter for the property ySrc
     *
     * @return the value of the property ySrc (short)
     */
    public short getYSrc() {
        return this.ySrc;
    }

    /**
     * Setter for the property width
     *
     * @param width (short) new value of the property width
     */
    public void setWidth(short width) {
        this.width = width;
    }

    /**
     * Getter for the property width
     *
     * @return the value of the property width (short)
     */
    public short getWidth() {
        return this.width;
    }

    /**
     * Setter for the property height
     *
     * @param height (short) new value of the property height
     */
    public void setHeight(short height) {
        this.height = height;
    }

    /**
     * Getter for the property height
     *
     * @return the value of the property height (short)
     */
    public short getHeight() {
        return this.height;
    }

    /**
     * Setter for the property xDest
     *
     * @param xDest (short) new value of the property xDest
     */
    public void setXDest(short xDest) {
        this.xDest = xDest;
    }

    /**
     * Getter for the property xDest
     *
     * @return the value of the property xDest (short)
     */
    public short getXDest() {
        return this.xDest;
    }

    /**
     * Setter for the property yDest
     *
     * @param yDest (short) new value of the property yDest
     */
    public void setYDest(short yDest) {
        this.yDest = yDest;
    }

    /**
     * Getter for the property yDest
     *
     * @return the value of the property yDest (short)
     */
    public short getYDest() {
        return this.yDest;
    }

    /**
     * Setter for property target
     *
     * @param target (DIBitmap) new value of property target
     */
    public void setTarget(DIBitmap target) {
        this.target = target;
    }

    /**
     * Getter for property target
     *
     * @return the value of the property target (DIBitmap)
     */
    public DIBitmap getTarget() {
        return this.target;
    }

    /**
     * Writes the content of the DIBBitBLT record to a stream
     *
     * @param out (OutputStream) the stream
     * @throws IOException
     */
    public void write(OutputStream out) throws IOException {
        int size = this.getSize();
        WMFConstants.writeLittleEndian(out, size);
        WMFConstants.writeLittleEndian(out, WMFConstants.WMF_RECORD_BITBLT);
        WMFConstants.writeLittleEndian(out, this.rasterOperation);
        WMFConstants.writeLittleEndian(out, this.ySrc);
        WMFConstants.writeLittleEndian(out, this.xSrc);
        if (this.target == null) WMFConstants.writeLittleEndian(out, (short) 0);
        WMFConstants.writeLittleEndian(out, this.height);
        WMFConstants.writeLittleEndian(out, this.width);
        WMFConstants.writeLittleEndian(out, this.yDest);
        WMFConstants.writeLittleEndian(out, this.xDest);
        if (this.target != null) this.target.write(out);
    }

    /**
     * Reads the content of a DIBBitBLT record from a stream
     *
     * @param in (InputStream) the stream
     * @throws IOException
     */
    public void read(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b = in.read();
        while (b >= 0) {
            baos.write(b);
            int avail = in.available();
            byte[] buffer = new byte[avail];
            int rd = in.read(buffer);
            baos.write(buffer, 0, rd);
            b = in.read();
        }
        byte[] buffer = baos.toByteArray();
        in = new ByteArrayInputStream(buffer);
        if (buffer.length > 18) {
            this.rasterOperation = WMFConstants.readLittleEndianInt(in);
            this.ySrc = WMFConstants.readLittleEndianShort(in);
            this.xSrc = WMFConstants.readLittleEndianShort(in);
            this.height = WMFConstants.readLittleEndianShort(in);
            this.width = WMFConstants.readLittleEndianShort(in);
            this.yDest = WMFConstants.readLittleEndianShort(in);
            this.xDest = WMFConstants.readLittleEndianShort(in);
            this.target = new DIBitmap(in, (short) 0);
        } else {
            this.rasterOperation = WMFConstants.readLittleEndianInt(in);
            this.ySrc = WMFConstants.readLittleEndianShort(in);
            this.xSrc = WMFConstants.readLittleEndianShort(in);
            WMFConstants.readLittleEndianShort(in);
            this.height = WMFConstants.readLittleEndianShort(in);
            this.width = WMFConstants.readLittleEndianShort(in);
            this.yDest = WMFConstants.readLittleEndianShort(in);
            this.xDest = WMFConstants.readLittleEndianShort(in);
            this.target = null;
        }
    }

    /**
     * Returns the size of the record
     *
     * @return the size (short)
     */
    public short getSize() {
        if (this.target != null) return (short) (11 + this.target.getSize()); else return (short) 12;
    }

    /**
     * Returns the content of the SIBBitBLT record in a human readable format
     *
     * @return the content (String)
     */
    public String toString() {
        return "DIBBitBLT:";
    }
}
