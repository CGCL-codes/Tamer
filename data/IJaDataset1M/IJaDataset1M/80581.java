package net.sourceforge.binml.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Fredrik Hederstierna
 * @version $Id: $
 * 
 * Created: 2006
 * 
 * Copyright 2006 Purple Scout AB, Malmoe, Sweden
 * 
 * Class that implements the format source.
 * 
 */
public class BinaryFormatSource implements FormatSource {

    /**
	 * Stream to read format description from.
	 */
    private InputStream inStream;

    private long timestamp;

    /**
	 * Constructor
	 * 
	 * @param file
	 *            File to read format description from.
	 * @throws FileNotFoundException
	 */
    public BinaryFormatSource(File file) throws FileNotFoundException {
        if (file != null) {
            inStream = new FileInputStream(file);
            timestamp = file.lastModified();
        }
    }

    private static String removeExtension(String filename) {
        return filename.indexOf('.') != -1 ? filename.substring(0, filename.lastIndexOf('.')) : filename;
    }

    /**
	 * Constructor
	 * 
	 * @param inStream
	 *            Stream to read from
	 */
    public BinaryFormatSource(InputStream inStream, long timestamp) {
        this.inStream = inStream;
        this.timestamp = timestamp;
    }

    /**
	 * Get stream
	 * 
	 * @return stream
	 */
    public InputStream getInputStream() {
        return inStream;
    }

    /**
	 * Open if required
	 */
    public void open() throws IOException {
    }

    /**
	 * Read data from stream
	 */
    public byte read() throws IOException {
        return (byte) inStream.read();
    }

    /**
	 * Close stream if required
	 */
    public void close() throws IOException {
        if (inStream != null) {
            inStream.close();
            inStream = null;
        }
    }

    public long getTimestamp() {
        return timestamp;
    }
}
