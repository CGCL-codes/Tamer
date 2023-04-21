package com.daffodilwoods.daffodildb.server.datasystem.persistentsystem;

import java.io.*;
import com.daffodilwoods.daffodildb.server.sql99.utils._Reference;
import java.sql.SQLException;
import com.daffodilwoods.database.resource.DException;

/**
 * To perform read and write operations on columns having blob clob Datatype. Data of blobClob columns can
 * be inserted and retrieved through it.
 */
public interface _LobUpdatable {

    boolean isStream();

    boolean isDBlob();

    byte[] getBytes();

    void setStartingClusterAddress(int startAddress);

    void setRecordNumber(short rec);

    int getStartingClusterAddress();

    short getRecordNumber();

    InputStream getStream();

    public int getLength();

    byte[] readBytes(long pos, int len) throws DException;
}
