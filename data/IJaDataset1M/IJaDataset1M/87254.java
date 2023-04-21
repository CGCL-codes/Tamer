package org.hsqldb;

import java.sql.SQLException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import org.hsqldb.lib.UnifiedTable;

/**
 *  Wrapper for disk image of CathecRow and its CachedNodes used for direct
 *  file defragmentation and housekeeping tasks. Corresponds to the disk data
 *  for a database row, including Row and its Node objects.
 *
 *  Designed so that each object is reusable for many rows.
 *
 * @version    1.7.2
 * @author     frest@users
 */
class RawDiskRow {

    int storageSize;

    RawDiskNode[] diskNodes = new RawDiskNode[0];

    int indexCount = 0;

    byte[] rowData = new byte[0];

    int rowDataLength;

    long filePosition;

    void write(DatabaseFile file) throws IOException {
        writeNodes(file);
        file.write(rowData, 0, storageSize - 4 - indexCount * RawDiskNode.storageSize);
    }

    void write(RandomAccessFile file) throws IOException {
        writeNodes(file);
        file.write(rowData, 0, storageSize - 4 - indexCount * RawDiskNode.storageSize);
    }

    void writeNodes(DatabaseFile file) throws IOException {
        file.writeInteger(storageSize);
        for (int i = 0; i < indexCount; i++) {
            diskNodes[i].write(file);
        }
    }

    void writeNodes(RandomAccessFile file) throws IOException {
        file.writeInt(storageSize);
        for (int i = 0; i < indexCount; i++) {
            diskNodes[i].write(file);
        }
    }

    void read(DatabaseFile file, int indcount) throws IOException {
        readNodes(file, indcount);
        file.read(rowData, 0, storageSize - 4 - indcount * RawDiskNode.storageSize);
    }

    void readNodes(DatabaseFile file, int indcount) throws IOException {
        filePosition = file.pos;
        indexCount = indcount;
        if (indexCount > diskNodes.length) {
            diskNodes = new RawDiskNode[indexCount];
            for (int i = 0; i < indexCount; i++) {
                diskNodes[i] = new RawDiskNode();
            }
        }
        storageSize = file.readInteger();
        for (int i = 0; i < indexCount; i++) {
            diskNodes[i].read(file);
        }
        if (storageSize > rowData.length) {
            rowData = new byte[storageSize];
        }
    }

    void readNodes(RandomAccessFile file, int indcount) throws IOException {
        filePosition = file.getFilePointer();
        indexCount = indcount;
        if (indexCount > diskNodes.length) {
            diskNodes = new RawDiskNode[indexCount];
            for (int i = 0; i < indexCount; i++) {
                diskNodes[i] = new RawDiskNode();
            }
        }
        storageSize = file.readInt();
        for (int i = 0; i < indexCount; i++) {
            diskNodes[i].read(file);
        }
        if (storageSize > rowData.length) {
            rowData = new byte[storageSize];
        }
    }

    void replacePointers(UnifiedTable lookup) throws SQLException {
        for (int i = 0; i < indexCount; i++) {
            diskNodes[i].replacePointers(lookup);
        }
    }
}
