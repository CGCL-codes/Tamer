package test;

import java.io.IOException;
import takatuka.drivers.*;
import takatuka.drivers.interfaces.*;
import takatuka.drivers.radio.Packet;

/**
 * 
 * Description:
 * <p>
 * </p> 
 * @author Faisal Aslam
 * @version 1.0
 */
public class SerialSendPackets {

    public static void main(String args[]) throws IOException {
        DriversFactory factory = DriversFactory.getInstanceOf();
        ISerial serial = factory.getSerialDriver();
        ILED led = factory.getLEDDriver();
        serial.start();
        Packet packet = serial.makePacket();
        packet.destination = 0;
        packet.data = new String("Hello").getBytes();
        packet.usedLength = (byte) packet.data.length;
        serial.send(packet);
    }
}
