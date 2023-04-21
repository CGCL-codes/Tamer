package de.fhg.igd.earth.model.input.shapefile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

/**
 * Class representing an ESRI Shape File.
 *
 * Title        : Earth
 * Copyright    : Copyright (c) 2001
 * Organisation : IGD FhG
 * @author      : Werner Beutel
 * @version     : 1.0
 */
public class ShapeFile extends ShapeUtils {

    /** A Shape File's magic number. */
    public static final int SHAPE_FILE_CODE = 9994;

    /** The currently handled version of Shape Files. */
    public static final int SHAPE_FILE_VERSION = 1000;

    /** A default record size.  Automatically increased on demand. */
    public static final int DEFAULT_RECORD_BUFFER_SIZE = 50000;

    /** The read/write class for shape files. */
    protected RandomAccessFile raf;

    /** The buffer that holds the 100 byte header. */
    protected byte header[];

    /** Holds the length of the file, in bytes. */
    protected long fileLength;

    /** Holds the version of the file, as an int. */
    protected int fileVersion;

    /** Holds the shape type of the file. */
    protected int fileShapeType;

    /** Holds the bounds of the file (four doubles). */
    protected ESRIBoundingBox fileBounds;

    /** A buffer for current record's header. */
    protected byte recHdr[];

    /** A buffer for the current record's data. */
    protected byte recBuf[];

    /**
     * Construct a <code>ShapeFile</code> from a file name.
     *
     * @exception IOException if something goes wrong opening or
     *            reading the file.
     */
    public ShapeFile(String name) throws IOException {
        raf = new RandomAccessFile(name, "rw");
        recHdr = new byte[ShapeUtils.SHAPE_FILE_RECORD_HEADER_LENGTH];
        recBuf = new byte[DEFAULT_RECORD_BUFFER_SIZE];
        initHeader();
    }

    /**
     * Construct a <code>ShapeFile</code> from the given
     * <code>File</code>.
     *
     * @param file A file object representing an ESRI Shape File
     *
     * @exception IOException if something goes wrong opening or
     *            reading the file.
     */
    public ShapeFile(File file) throws IOException {
        this(file.getPath());
    }

    /**
     * Reads or writes the header of a Shape file.  If the file
     * is empty, a blank header is written and then read.  If the
     * file is not empty, the header is read.
     * <p>
     * After this function runs, the file pointer is set to byte 100,
     * the first byte of the first record in the file.
     *
     * @exception IOException if something goes wrong reading or writing
     *            the shape file
     */
    protected void initHeader() throws IOException {
        int result = raf.read();
        if (result == -1) {
            writeHeader();
        }
        readHeader();
    }

    /**
     * Writes a blank header into the shape file.
     *
     * @exception IOException if something goes wrong writing the shape file
     */
    protected void writeHeader() throws IOException {
        header = new byte[SHAPE_FILE_HEADER_LENGTH];
        writeBEInt(header, 0, SHAPE_FILE_CODE);
        writeBEInt(header, 24, 50);
        writeLEInt(header, 28, SHAPE_FILE_VERSION);
        writeLEInt(header, 32, SHAPE_TYPE_NULL);
        writeLEDouble(header, 36, 0.0);
        writeLEDouble(header, 44, 0.0);
        writeLEDouble(header, 52, 0.0);
        writeLEDouble(header, 60, 0.0);
        raf.seek(0);
        raf.write(header, 0, SHAPE_FILE_HEADER_LENGTH);
    }

    /**
     * Reads and parses the header of the file.  Values from the header
     * are stored in the fields of this class.
     *
     * @exception IOException if something goes wrong reading the file
     * @see #header
     * @see #fileVersion
     * @see #fileLength
     * @see #fileShapeType
     * @see #fileBounds
     */
    protected void readHeader() throws IOException {
        header = new byte[ShapeUtils.SHAPE_FILE_HEADER_LENGTH];
        raf.seek(0);
        raf.read(header, 0, ShapeUtils.SHAPE_FILE_HEADER_LENGTH);
        int fileCode = ShapeUtils.readBEInt(header, 0);
        if (fileCode != SHAPE_FILE_CODE) {
            throw new IOException("Invalid file code, " + "probably not a shape file");
        }
        fileVersion = ShapeUtils.readLEInt(header, 28);
        if (fileVersion != SHAPE_FILE_VERSION) {
            throw new IOException("Unable to read shape files with version " + fileVersion);
        }
        fileLength = ShapeUtils.readBEInt(header, 24);
        fileLength *= 2;
        fileShapeType = ShapeUtils.readLEInt(header, 32);
        fileBounds = ShapeUtils.readBox(header, 36);
    }

    /**
     * Returns the length of the file in bytes.
     *
     * @return the file length
     */
    public long getFileLength() {
        return fileLength;
    }

    /**
     * Returns the version of the file.  The only currently supported
     * version is 1000 (which represents version 1).
     *
     * @return the file version
     */
    public int getFileVersion() {
        return fileVersion;
    }

    /**
     * Returns the shape type of the file.  Shape files do not mix
     * shape types; all the shapes are of the same type.
     *
     * @return the file's shape type
     */
    public int getShapeType() {
        return fileShapeType;
    }

    /**
     * Sets the shape type of the file.  If the file has a shape type
     * already, it cannot be set.  If it does not have a shape type,
     * it is set and written to the file in the header.
     * <p>
     * Shape types are enumerated in the class ShapeUtils.
     *
     * @param newShapeType the new shape type
     * @exception IOException if something goes wrong writing the file
     * @exception IllegalArgumentException if file already has a shape type
     * @see ShapeUtils
     */
    public void setShapeType(int newShapeType) throws IOException, IllegalArgumentException {
        if (fileShapeType == SHAPE_TYPE_NULL) {
            fileShapeType = newShapeType;
            long filePtr = raf.getFilePointer();
            writeLEInt(header, 32, fileShapeType);
            raf.seek(0);
            raf.write(header, 0, 100);
            raf.seek(filePtr);
        } else {
            throw new IllegalArgumentException("file already has a valid" + " shape type: " + fileShapeType);
        }
    }

    /**
     * Returns the bounding box of this shape file.  The bounding box
     * is the smallest rectangle that encloses all the shapes in the
     * file.
     *
     * @return the bounding box
     */
    public ESRIBoundingBox getBoundingBox() {
        return fileBounds;
    }

    /**
     * Returns the next record from the shape file as an
     * <code>ESRIRecord</code>.  Each successive call gets the next
     * record.  There is no way to go back a record.  When there
     * are no more records, <code>null</code> is returned.
     *
     * @return a record, or null if there are no more records
     * @exception IOException if something goes wrong reading the file
     */
    public ESRIRecord getNextRecord() throws IOException {
        int result = raf.read(recHdr, 0, ShapeUtils.SHAPE_FILE_RECORD_HEADER_LENGTH);
        if (result == -1) {
            return null;
        }
        int contentLength = ShapeUtils.readBEInt(recHdr, 4);
        int bytesToRead = contentLength * 2;
        int fullRecordSize = bytesToRead + 8;
        if (recBuf.length < fullRecordSize) {
            if (Debug.debugging("shape")) {
                Debug.output("record size: " + fullRecordSize);
            }
            recBuf = new byte[fullRecordSize];
        }
        System.arraycopy(recHdr, 0, recBuf, 0, ShapeUtils.SHAPE_FILE_RECORD_HEADER_LENGTH);
        raf.read(recBuf, ShapeUtils.SHAPE_FILE_RECORD_HEADER_LENGTH, bytesToRead);
        switch(fileShapeType) {
            case ShapeUtils.SHAPE_TYPE_NULL:
                throw new IOException("Can't parse NULL shape type");
            case ShapeUtils.SHAPE_TYPE_POINT:
                return new ESRIPointRecord(recBuf, 0);
            case ShapeUtils.SHAPE_TYPE_ARC:
                return new ESRIPolygonRecord(recBuf, 0);
            case ShapeUtils.SHAPE_TYPE_POLYGON:
                return new ESRIPolygonRecord(recBuf, 0);
            case ShapeUtils.SHAPE_TYPE_MULTIPOINT:
                throw new IOException("Multipoint shape not yet implemented");
            default:
                throw new IOException("Unknown shape type: " + fileShapeType);
        }
    }

    /**
     * Adds a record to the end of this file.  The record is written to
     * the file at the end of the last record.
     *
     * @param r the record to be added
     * @exception IOException if something goes wrong writing to the file
     */
    public void add(ESRIRecord r) throws IOException {
        if (r.getShapeType() == fileShapeType) {
            verifyRecordBuffer(r.getBinaryStoreSize());
            int nBytes = r.write(recBuf, 0);
            raf.seek(raf.length());
            raf.write(recBuf, 0, nBytes);
        } else {
            Debug.error("ShapeFile.add(): type=" + r.getShapeType() + " does not match file type=" + fileShapeType);
        }
    }

    /**
     * Closes the shape file and disposes of resources.
     *
     * @exception IOException if something goes wrong closing the file
     */
    public void close() throws IOException {
        raf.close();
        raf = null;
    }

    /**
     * Verifies the contents of a shape file.  The header is verified
     * for file length, bounding box, and shape type.  The records
     * are verified for shape type and record number.  The file is verified
     * for proper termination (EOF at the end of a record).
     *
     * @param repair NOT CURRENTLY USED - would signal that the file
     *               should be repaired if possible
     * @param verbose NOT CURRENTLY USED - would cause the verifier
     *                to display progress and status
     * @exception IOException if something goes wrong reading or writing
     *            the file
     */
    public void verify(boolean repair, boolean verbose) throws IOException {
        boolean headerChanged = false;
        boolean recordsChanged = false;
        long fLen = raf.length();
        if (verbose) {
            Debug.output("Checking file length...");
            System.out.flush();
        }
        if (fileLength == fLen) {
            if (verbose) {
                Debug.output("correct.");
            }
        } else {
            if (verbose) {
                Debug.output("incorrect (got " + fileLength + ", should be " + fLen + ")");
            }
            if (repair) {
                fileLength = fLen;
                writeBEInt(header, 24, ((int) fLen / 2));
                headerChanged = true;
                if (verbose) {
                    Debug.output("...repaired.");
                }
            }
        }
        raf.seek(100);
        ESRIRecord r;
        int nRecords = 0;
        Vector v = new Vector();
        ESRIBoundingBox bounds = new ESRIBoundingBox();
        long recStart = raf.getFilePointer();
        byte intBuf[] = new byte[4];
        while ((r = getNextRecord()) != null) {
            long recEnd = raf.getFilePointer();
            nRecords++;
            v.addElement(r);
            if (r.getRecordNumber() != nRecords) {
                writeBEInt(intBuf, 0, nRecords);
                raf.seek(recStart);
                raf.write(intBuf, 0, 4);
                raf.seek(recEnd);
            }
            if (fileShapeType == SHAPE_TYPE_NULL) {
                Debug.output("updating shape type in header.");
                fileShapeType = r.getShapeType();
                writeLEInt(header, 32, fileShapeType);
                headerChanged = true;
            }
            if (r.getShapeType() != fileShapeType) {
                Debug.output("invalid shape type " + r.getShapeType() + ", expecting " + fileShapeType);
            }
            bounds.addBounds(r.getBoundingBox());
            recStart = recEnd;
        }
        if (!fileBounds.equals(bounds)) {
            Debug.output("adjusting bounds");
            Debug.output("from min: " + fileBounds.min);
            Debug.output("to min: " + bounds.min);
            Debug.output("from max: " + fileBounds.max);
            Debug.output("to max: " + bounds.max);
            writeBox(header, 36, bounds);
            headerChanged = true;
            fileBounds = bounds;
        }
        if (headerChanged) {
            Debug.output("writing changed header");
            raf.seek(0);
            raf.write(header, 0, 100);
        }
    }

    /**
     * Verifies that the record buffer is big enough to hold the given
     * number of bytes.  If it is not big enough a new buffer is created
     * that can hold the given number of bytes.
     *
     * @param size the number of bytes the buffer needs to hold
     */
    protected void verifyRecordBuffer(int size) {
        if (recBuf.length < size) {
            recBuf = new byte[size];
        }
    }
}
