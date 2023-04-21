package com.jme.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import com.jme.system.JmeException;

/**
 * <code>BinaryFileReader</code> provides a convienience class that wraps the
 * DataInputStream object for easier reading. Rather than having to deal with
 * a binary file one byte at a time, this class will handle reading: byte,
 * short, int, float and string. The file is read at the start, the file
 * stored as an array of bytes. An index into the array will keep track of
 * the reading point. The index can be manually adjusted via the
 * <code>setOffset</code> method.
 * @author Mark Powell
 * @version $Id: BinaryFileReader.java 4131 2009-03-19 20:15:28Z blaine.dev $
 */
public class BinaryFileReader {

    private static final Logger logger = Logger.getLogger(BinaryFileReader.class.getName());

    private byte[] fileContents;

    private int fileIndex = 0;

    private int markedPos = 0;

    /**
	 * Constructor creates a new <code>BinaryFileReader</code> class. This
	 * constructor takes the filename string as a parameter. This filename
	 * is converted into a URL and opened. If the filename is invalid, a
	 * <code>MalformedURLException</code> will be generated and logged.
	 * @param f the file to open.
	 */
    public BinaryFileReader(String f) {
        try {
            URL file = new URL(f);
            open(file);
        } catch (MalformedURLException e) {
            logger.warning("Could not open: " + f);
        }
    }

    /**
	 * Constructor instantiates a new <code>BinaryFileReader</code> object,
	 * loading the provided file and reading the data into a byte array.
	 * @param f the file to read.
	 */
    public BinaryFileReader(URL f) {
        open(f);
    }

    public BinaryFileReader(InputStream md2) {
        try {
            DataInputStream bis = new DataInputStream(md2);
            fileContents = new byte[bis.available()];
            bis.readFully(fileContents);
            bis.close();
        } catch (IOException e) {
            throw new JmeException("Could not read InputStream ");
        }
    }

    /**
	 *
	 * <code>open</code> opens a given URL stream. The data is read completely
	 * and the stream is then closed. This allows the stream to only be needed
	 * for the time it takes to read all the data, it is then closed.
	 *
	 * @param f the url pointing to the file to be read.
	 */
    public void open(URL f) {
        try {
            InputStream is = f.openStream();
            DataInputStream bis = new DataInputStream(is);
            fileContents = new byte[bis.available()];
            bis.readFully(fileContents);
            bis.close();
        } catch (IOException ioe) {
            throw new JmeException("Could not read: " + f);
        }
    }

    /**
	 *
	 * <code>readByte</code> reads a single byte from the array and
	 * returns this. The file index is then increased by one.
	 * @return the byte at the current index.
	 */
    public int readByte() {
        int b1 = (fileContents[fileIndex] & 0xFF);
        fileIndex += 1;
        return (b1);
    }

    /**
	 *
	 * <code>readShort</code> reads two bytes from the array, generating
	 * a short. The file index is then increased by two. The short is then
	 * inserted into an integer for convienience.
	 * @return the short at the current index.
	 */
    public int readShort() {
        int s1 = (fileContents[fileIndex] & 0xFF);
        int s2 = (fileContents[fileIndex + 1] & 0xFF) << 8;
        fileIndex += 2;
        return (s1 | s2);
    }

    public int readShort2() {
        int s1 = (fileContents[fileIndex + 1] & 0xFF);
        int s2 = (fileContents[fileIndex] & 0xFF) << 8;
        fileIndex += 2;
        return (s1 | s2);
    }

    /**
	 *
	 * <code>readInt</code> reads four bytes from the array, generating
	 * an int. The file index is then increased by four.
	 * @return the int at the currrent index.
	 */
    public int readInt() {
        int i1 = (fileContents[fileIndex] & 0xFF);
        int i2 = (fileContents[fileIndex + 1] & 0xFF) << 8;
        int i3 = (fileContents[fileIndex + 2] & 0xFF) << 16;
        int i4 = (fileContents[fileIndex + 3] & 0xFF) << 24;
        fileIndex += 4;
        return (i1 | i2 | i3 | i4);
    }

    /**
	 *
	 * <code>readFloat</code> reads four bytes from the array, generating
	 * a float. The file index is then increased by four.
	 * @return the float at the current index.
	 */
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    /**
	 *
	 * <code>readString</code> reads a specified number of bytes to
	 * form a string. The length of the string (number of characters)
	 * is required to notify when reading should stop. The index is
	 * increased the number of characters read.
	 * @param size the length of the string to read.
	 * @return the string read.
	 */
    public String readString(int size) {
        for (int i = fileIndex; i < fileIndex + size; i++) {
            if (fileContents[i] == (byte) 0) {
                String s = new String(fileContents, fileIndex, i - fileIndex);
                fileIndex += size;
                return s;
            }
        }
        String s = new String(fileContents, fileIndex, size);
        fileIndex += size;
        return s;
    }

    /**
	 *
	 * <code>setOffset</code> sets the index of the file data.
	 * @param offset the new index of the file pointer.
	 */
    public void setOffset(int offset) {
        if (offset < 0 || offset > fileContents.length) {
            throw new JmeException("Illegal offset value. " + offset);
        }
        fileIndex = offset;
    }

    /**
     * Sets a mark for a later seekMarkOffset call.
     */
    public void markPos() {
        markedPos = fileIndex;
    }

    /**
     * Seeks to the position of the last mark + offset.
     * @param offset The Offset relative to mark.
     */
    public void seekMarkOffset(int offset) {
        fileIndex = markedPos + offset;
        if (fileIndex < 0 || fileIndex > fileContents.length) {
            throw new JmeException("Illegal offset value. " + offset);
        }
    }

    /**
     * Reads a signed short value.
     * @return The signed short.
     */
    public short readSignedShort() {
        return (short) readShort();
    }
}
