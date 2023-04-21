package net.sourceforge.x360mediaserve.plugins.taggers.impl.mp4;

import net.sourceforge.x360mediaserve.plugins.taggers.impl.CountedDataInput;

public class Box {

    int fourCCInt;

    private long start;

    private long size;

    Box(int fourCCInt, long start, long size, CountedDataInput input) {
        this.fourCCInt = fourCCInt;
        this.start = start;
        this.size = size;
    }
}
