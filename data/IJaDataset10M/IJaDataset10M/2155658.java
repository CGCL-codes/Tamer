package takatuka.drivers.hwImpl.sensor.msp430;

import takatuka.drivers.interfaces.sensors.IRead;

/**
 * <p>Title: </p>
 * <p>Description:
 *
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class MSP430TempRead implements IRead {

    private static final MSP430TempRead temp = new MSP430TempRead();

    private short data = 0;

    private MSP430TempRead() {
    }

    public static MSP430TempRead getInstanceOf() {
        return temp;
    }

    public native void readNative();

    public short read() {
        readNative();
        return data;
    }
}
