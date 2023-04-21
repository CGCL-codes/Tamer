package takatuka.drivers.hwImpl.sensor;

import takatuka.drivers.interfaces.sensors.*;

/**
 * <p>Title: </p>
 * <p>Description:
 *
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class AccelYRead implements IRead {

    private static final AccelYRead AccelY = new AccelYRead();

    private short data = 0;

    private AccelYRead() {
    }

    public static AccelYRead getInstanceOf() {
        return AccelY;
    }

    public native void readNative();

    public short read() {
        readNative();
        return data;
    }
}
