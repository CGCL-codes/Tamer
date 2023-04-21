package com.hmbnet.squelch;

import java.io.Serializable;

public class Diff implements Serializable {

    private Slice<CellDiff> mySlice;

    public Diff() {
        mySlice = new Slice<CellDiff>();
    }

    public Slice<CellDiff> getSlice() {
        return mySlice;
    }

    public void setSlice(Slice<CellDiff> slice) {
        mySlice = slice;
    }
}
