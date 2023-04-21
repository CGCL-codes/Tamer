package eu.haslgruebler.darsens.service.sensors.pojo;

import java.io.IOException;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;
import com.sun.spot.util.IEEEAddress;
import eu.haslgruebler.darsens.service.Message;

/**
 * @author Michael Haslgrübler, uni-michael@haslgruebler.eu
 *
 */
public class SensorData extends Message {

    public SensorData() {
        super();
    }

    public SensorData(long localAddress) {
        super(localAddress);
    }

    private double xTilt;

    private double yTilt;

    private double zTilt;

    private double xAcc;

    private double yAcc;

    private double zAcc;

    private long time;

    /**
	 * value of message type sensor data
	 */
    public static final byte MSGTYPE = 3;

    /**
	 * @return the xTilt
	 */
    public double getXTilt() {
        return xTilt;
    }

    /**
	 * @param tilt the xTilt to set
	 */
    public void setXTilt(double tilt) {
        xTilt = tilt;
    }

    /**
	 * @return the yTilt
	 */
    public double getYTilt() {
        return yTilt;
    }

    /**
	 * @param tilt the yTilt to set
	 */
    public void setYTilt(double tilt) {
        yTilt = tilt;
    }

    /**
	 * @return the zTilt
	 */
    public double getZTilt() {
        return zTilt;
    }

    /**
	 * @param tilt the zTilt to set
	 */
    public void setZTilt(double tilt) {
        zTilt = tilt;
    }

    /**
	 * @return the xAcc
	 */
    public double getXAcc() {
        return xAcc;
    }

    /**
	 * @param acc the xAcc to set
	 */
    public void setXAcc(double acc) {
        xAcc = acc;
    }

    /**
	 * @return the yAcc
	 */
    public double getYAcc() {
        return yAcc;
    }

    /**
	 * @param acc the yAcc to set
	 */
    public void setYAcc(double acc) {
        yAcc = acc;
    }

    /**
	 * @return the zAcc
	 */
    public double getZAcc() {
        return zAcc;
    }

    /**
	 * @param acc the zAcc to set
	 */
    public void setZAcc(double acc) {
        zAcc = acc;
    }

    public byte getMessageType() {
        return SensorData.MSGTYPE;
    }

    public void fromDatagram(Datagram dg) throws IOException {
        setReceiver(new IEEEAddress(dg.readLong()));
        setSender(new IEEEAddress(dg.readLong()));
        xAcc = dg.readDouble();
        yAcc = dg.readDouble();
        zAcc = dg.readDouble();
        xTilt = dg.readDouble();
        yAcc = dg.readDouble();
        zAcc = dg.readDouble();
        time = dg.readLong();
    }

    public String toString() {
        return "RawData acc(" + xAcc + "," + yAcc + "," + zAcc + ")tilt(" + xTilt + "," + yTilt + "," + zTilt + ")";
    }

    public static final String logStringHead() {
        return "sensor;time;x-acc;y-acc;z-acc;x-tilt;y-tilt;z-tilt";
    }

    public static final String flashStringHead() {
        return "time;x-acc;y-acc;z-acc;x-tilt;y-tilt;z-tilt\n";
    }

    public String toLogString() {
        return getSender().asLong() + ";" + time + ";" + xAcc + ";" + yAcc + ";" + zAcc + ";" + xTilt + ";" + yTilt + ";" + zTilt;
    }

    public String toFlashString() {
        return time + ";" + xAcc + ";" + yAcc + ";" + zAcc + ";" + xTilt + ";" + yTilt + ";" + zTilt + "\n";
    }

    /**
	 * @param time the time to set
	 */
    public void setTime(long time) {
        this.time = time;
    }

    /**
	 * @return the time
	 */
    public long getTime() {
        return time;
    }

    public Datagram toDatagram(DatagramConnection c) throws IOException {
        Datagram d = c.newDatagram(c.getMaximumLength());
        d.write(getMessageType());
        d.writeLong(getReceiver().asLong());
        d.writeLong(getSender().asLong());
        d.writeDouble(xAcc);
        d.writeDouble(yAcc);
        d.writeDouble(zAcc);
        d.writeDouble(xTilt);
        d.writeDouble(yTilt);
        d.writeDouble(zTilt);
        d.writeLong(time);
        return d;
    }
}
