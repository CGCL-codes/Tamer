package org.price.iso9660.descriptors.file.impl;

import java.io.IOException;
import org.price.iso9660.descriptors.file.api.IPathTableRecord;
import org.price.iso9660.utils.ISOUtils;

public class PathTableRecord implements IPathTableRecord {

    protected byte[] intBuff = null;

    protected short len = -1;

    protected int locationOfExtent = -1;

    protected short parentDirNr = -1;

    protected byte[] directoryId = null;

    protected ISOUtils isoUtils = new ISOUtils();

    public PathTableRecord(byte[] b, int index) throws IOException {
        len = (short) ((short) (b[index]) & 0xFF);
        if (len <= 0) {
            throw new IllegalArgumentException("Path table record size cannot be 0.");
        }
        int buffSize = 8 + len + ((len % 2 == 0) ? 0 : 1);
        intBuff = new byte[buffSize];
        directoryId = new byte[len + ((len % 2 == 0) ? 0 : 1)];
        System.arraycopy(b, index, intBuff, 0, intBuff.length);
        locationOfExtent = isoUtils.toInt(intBuff, 2);
        parentDirNr = isoUtils.toShort(intBuff, 6);
        System.arraycopy(intBuff, 8, directoryId, 0, directoryId.length);
    }

    public byte getDirIdLength() {
        return intBuff[0];
    }

    public byte[] getDirectoryId() {
        return directoryId;
    }

    public byte getExtendedAttributeRecordLength() {
        return intBuff[1];
    }

    public int getLocationOfExtent() {
        return locationOfExtent;
    }

    public short getParentDirectoryNr() {
        return parentDirNr;
    }

    public byte[] toByteArray() {
        return intBuff;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Location Of Extent: ");
        sb.append(String.valueOf(locationOfExtent));
        sb.append(" DirId: ");
        sb.append(new String(directoryId));
        return sb.toString();
    }
}
