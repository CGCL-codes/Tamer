package org.shestkov.timeseriestorage.io;

import java.io.RandomAccessFile;
import java.io.IOException;

/**
 * NumiumTrade project
 * Time: 23.01.2010 12:49:09
 *
 * @author Vasily Shestkov
 */
public interface OffsetCalculator {

    public void setRandomAccessFile(RandomAccessFile randomAccessFile);

    public long seek(long time) throws IOException;
}
