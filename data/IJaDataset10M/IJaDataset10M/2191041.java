package com.ibm.tuningfork.infra.util;

public class CacheArrayBlockSummary {

    public final long filePosition;

    public final int blockLength;

    public CacheArrayBlockSummary(long filePos, int len) {
        filePosition = filePos;
        blockLength = len;
    }

    public boolean isPartial() {
        return blockLength < 0;
    }
}
