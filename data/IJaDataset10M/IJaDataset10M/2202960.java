package com.ibm.tuningfork.infra.units;

import com.ibm.tuningfork.infra.sharing.ISharingConvertibleCallback;

/**
 * The unit of measure of a sample, stream, or axis
 * 
 * 
 */
public class Bel extends SimpleUnit {

    public static final int SCALE_BELS = 0;

    public static final int SCALE_DECIBELS = -1;

    public Bel(int scaling) {
        super("Volume", "bels", "b", scaling, false, false);
    }

    @Override
    public void collectReconstructionArguments(ISharingConvertibleCallback cb) throws Exception {
        cb.convert(scaling);
    }
}
