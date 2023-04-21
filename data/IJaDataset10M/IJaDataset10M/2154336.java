package com.aelitis.azureus.core.diskmanager.cache;

import org.gudy.azureus2.core3.torrent.TOTorrent;

public interface CacheFileManagerStats {

    public long getSize();

    public long getUsedSize();

    public long getBytesWrittenToCache();

    public long getBytesWrittenToFile();

    public long getBytesReadFromCache();

    public long getBytesReadFromFile();

    public long getAverageBytesWrittenToCache();

    public long getAverageBytesWrittenToFile();

    public long getAverageBytesReadFromCache();

    public long getAverageBytesReadFromFile();

    public long getCacheReadCount();

    public long getCacheWriteCount();

    public long getFileReadCount();

    public long getFileWriteCount();

    public boolean[] getBytesInCache(TOTorrent torrent, long[] absoluteOffsets, long[] lengths);
}
