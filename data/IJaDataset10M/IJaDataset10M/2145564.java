package de.maramuse.soundcomp.math;

import de.maramuse.soundcomp.util.NativeObjects;

public class log extends BasicMath2 {

    public log() {
        NativeObjects.registerNativeObject(this);
    }

    log(boolean s) {
    }

    @Override
    public double getValue(int index) {
        return Math.log(in2.getValue()) / Math.log(in1.getValue());
    }

    /**
	 * @see de.maramuse.soundcomp.process.ProcessElement#clone()
	 */
    @Override
    public log clone() {
        return new log();
    }
}
