package hk.hku.cs.cpq.nn.device;

import hk.hku.cs.cpq.nn.geometry.Point;

public class MOMSG extends MSG {

    /**
	 * Message sent by MO
	 */
    private int moID;

    private Point point;

    public MOMSG(int moID, Point point, double currentTime) {
        setDeviceType(DeviceType.MO);
        this.moID = moID;
        this.point = point;
        setCurrentTime(currentTime);
    }

    public int getMoID() {
        return moID;
    }

    public Point getPoint() {
        return point;
    }

    public String toString() {
        return "[MOMSG\tcurrent time = " + getCurrentTime() + "\tmoID = " + moID + "\tcurrent position = " + point + "]";
    }
}
