package com.jguild.jrpm.io.datatype;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.jguild.jrpm.io.IndexEntry;
import com.jguild.jrpm.io.RPMFile;
import com.jguild.jrpm.io.constant.RPMIndexType;

/**
 * A representation of a rpm string array data object.
 *
 * @author kuss
 * @version $Id: STRING_ARRAY.java,v 1.4 2005/11/11 08:27:40 mkuss Exp $
 */
public final class STRING_ARRAY implements DataTypeIf {

    private static final Logger logger = RPMFile.logger;

    private String[] data;

    private long size;

    STRING_ARRAY(final String[] data) {
        this.data = data;
        for (int pos = 0; pos < data.length; pos++) this.size += data[pos].length() + 1;
    }

    /**
     * Get the rpm string array as a java string array
     *
     * @return An array of strings
     */
    public String[] getData() {
        return this.data;
    }

    public Object getDataObject() {
        return this.data;
    }

    public RPMIndexType getType() {
        return RPMIndexType.STRING_ARRAY;
    }

    /**
     * Constructs a type froma stream
     *
     * @param inputStream An input stream
     * @param indexEntry  The index informations
     * @param length      the length of the data
     * @return The size of the read data
     * @throws IOException if an I/O error occurs.
     */
    public static STRING_ARRAY readFromStream(final DataInputStream inputStream, final IndexEntry indexEntry, final long length) throws IOException {
        if (indexEntry.getType() != RPMIndexType.STRING_ARRAY) {
            throw new IllegalArgumentException("Type <" + indexEntry.getType() + "> does not match <" + RPMIndexType.STRING_ARRAY + ">");
        }
        byte[] stringData = new byte[(int) length];
        inputStream.readFully(stringData);
        String data[] = new String[(int) indexEntry.getCount()];
        int off = 0;
        for (int pos = 0; pos < indexEntry.getCount(); pos++) {
            data[pos] = RPMUtil.cArrayToString(stringData, off);
            off += (data[pos].length() + 1);
            if (off > stringData.length) {
                throw new IllegalStateException("Index wrong; Strings doesn't fit into data area. [" + off + ", " + stringData.length + "]");
            }
        }
        STRING_ARRAY stringObject = new STRING_ARRAY(data);
        if (logger.isLoggable(Level.FINER)) {
            logger.finer(stringObject.toString());
            if (stringObject.size != stringData.length) {
                logger.warning("STRING_ARRAY size differs (is:" + stringData.length + ";should:" + stringObject.size + ")");
            }
        }
        stringObject.size = stringData.length;
        return stringObject;
    }

    public boolean isArray() {
        return true;
    }

    public long getElementCount() {
        return this.data.length;
    }

    public long getSize() {
        return this.size;
    }

    public Object get(final int i) {
        return this.data[i];
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (this.data.length > 1) {
            buf.append("[");
        }
        for (int pos = 0; pos < this.data.length; pos++) {
            buf.append(this.data[pos]);
            if (pos < (this.data.length - 1)) {
                buf.append(", ");
            }
        }
        if (this.data.length > 1) {
            buf.append("]");
        }
        return buf.toString();
    }
}
