package com.werno.wmflib.records;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.werno.wmflib.WMFContainer;
import com.werno.wmflib.WMFConstants;

/**
 * This class implements the META_HEADER part of a WMF file
 *
 * @author Peter Werno
 */
public class Header extends ShapeRecord implements Record {

    /** Definitions */
    public static final short HEADER_TYPE_MEMORY = 0x0001;

    public static final short HEADER_TYPE_DISK = 0x0002;

    public static final short HEADER_VERSION_100 = 0x0100;

    public static final short HEADER_VERSION_300 = 0x0300;

    /** stores the original container */
    private WMFContainer container;

    /** Properties */
    private short type;

    private short version;

    private boolean overrideNumberOfObjects;

    private short numberOfObjects;

    private boolean overrideNumberOfMembers;

    private short numberOfMembers;

    /**
     * Creates a new instance of Header
     *
     * @param container (WMFContainer) the wmf writer(reader object
     */
    public Header(WMFContainer container) {
        this.container = container;
        this.type = HEADER_TYPE_MEMORY;
        this.version = HEADER_VERSION_300;
    }

    /**
     * Creates a new instance of Header and presets the type and version
     *
     * @param container (WMFContainer) the wmf writer/reader object
     * @param type (short) the value of the type property
     * @param version (short) the value of the version property
     */
    public Header(WMFContainer container, short type, short version) {
        this.container = container;
        this.type = type;
        this.version = version;
    }

    /**
     * Created a new instance of Header and reads all properties from
     * a stream
     *
     * @param in (InputStream) the stream
     * @throws IOException
     */
    public Header(InputStream in) throws IOException {
        this.read(in);
    }

    /**
     * Sets the type property
     *
     * @param type (short) the new value of the type property
     */
    public void setType(short type) {
        this.type = type;
    }

    /**
     * Returns the current type property
     *
     * @return the type (short)
     */
    public short getType() {
        return this.type;
    }

    /**
     * Sets the version property
     *
     * @param version (short) the new value of the version property
     */
    public void setVersion(short version) {
        this.version = version;
    }

    /**
     * Returns the version property
     *
     * @return the version property (short)
     */
    public short getVersion() {
        return this.version;
    }

    /**
     * Setter for property numberOfObjects
     *
     * @param numberOfObjects (short) the new value of the numberOfObjects property
     */
    public void setNumberOfObjects(short numberOfObjects) {
        this.numberOfObjects = numberOfObjects;
        this.overrideNumberOfObjects = true;
    }

    /**
     * Getter for property numberOfObjects
     *
     * @return the value of the property numberOfObjects (short)
     */
    public short getNumberOfObjects() {
        return this.numberOfObjects;
    }

    /**
     * Setter for property numberOfMembers
     *
     * @param numberOfMembers (short) the new value of the numberOfMembers property
     */
    public void setNumberOfMembers(short numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
        this.overrideNumberOfMembers = true;
    }

    /**
     * Getter for property numberOfMembers
     *
     * @return the value of the property numberOfMembers (short)
     */
    public short getNumberOfMembers() {
        return this.numberOfMembers;
    }

    /**
     * Writes the header structure to the stream
     *
     * @param out (OutputStream) the stream
     * @throws IOException
     */
    public void write(OutputStream out) throws IOException {
        WMFConstants.writeLittleEndian(out, type);
        WMFConstants.writeLittleEndian(out, getSize());
        WMFConstants.writeLittleEndian(out, version);
        WMFConstants.writeLittleEndian(out, container.getTotalRecordSize());
        if (this.overrideNumberOfObjects) WMFConstants.writeLittleEndian(out, this.numberOfObjects); else WMFConstants.writeLittleEndian(out, container.getObjectCount());
        WMFConstants.writeLittleEndian(out, container.getMaxRecordSize());
        if (this.overrideNumberOfMembers) WMFConstants.writeLittleEndian(out, this.numberOfMembers); else {
            out.write(0);
            out.write(0);
        }
    }

    /**
     * Reads the header information from a stream
     *
     * @param in (InputStream) the stream
     * @throws IOException
     */
    public void read(InputStream in) throws IOException {
        WMFConstants.readLittleEndianInt(in);
        this.numberOfObjects = WMFConstants.readLittleEndianShort(in);
        WMFConstants.readLittleEndianInt(in);
        this.numberOfMembers = WMFConstants.readLittleEndianShort(in);
    }

    /**
     * The size of the header is 9 words
     *
     * @return 9 (short)
     */
    public short getSize() {
        return 9;
    }

    /**
     * Returns the content of the header record in human readable format
     *
     * @return the content (String)
     */
    @Override
    public String toString() {
        return "Header : Version " + this.version + ", Type " + this.type;
    }
}
