package org.smallmind.scribe.pen;

import java.io.File;

public class FileSizeRollover extends Rollover {

    private FileSizeQuantifier fileSizeQuantifier;

    private long maxSize;

    public FileSizeRollover(long maxSize, FileSizeQuantifier fileSizeQuantifier, char separator, Timestamp timestamp) {
        super(separator, timestamp);
        this.maxSize = maxSize;
        this.fileSizeQuantifier = fileSizeQuantifier;
    }

    public boolean willRollover(File logFile, long bytesToBeWritten) {
        return (logFile.length() + bytesToBeWritten) > (maxSize * fileSizeQuantifier.getMultiplier());
    }
}
