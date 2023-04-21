package GPS;

import java.io.*;
import routing.flooding.*;
import takatuka.drivers.*;
import takatuka.drivers.interfaces.*;
import takatuka.drivers.interfaces.events.*;
import takatuka.drivers.radio.*;
import takatuka.routing.*;

/**
 * This Application forwards GPS-Coordinates received from the Serial Interface
 * to a desired Node specified by its ID (destID) using Flooding Routing.
 *
 * @author Alexander Schaetzle, Martin Przyjaciel-Zablocki
 * @version 1.0
 */
public class GPSFlooding implements IDataReceiver, ISerialCommEvent {

    private DriversFactory factory = DriversFactory.getInstanceOf();

    private IRadio radio = factory.getRadioDriver();

    private ISerial serial = factory.getSerialDriver();

    private ILED leds = factory.getLEDDriver();

    private Flooding routing;

    private static byte[] gpsData = new byte[11];

    private static int myID = 1;

    private static int destID = 5;

    /**
     * main-method to start the application.
     * @param args
     */
    public static void main(String args[]) {
        new GPSFlooding().execute();
    }

    /**
     * Initialize the application.
     */
    public void execute() {
        factory.getMoteInfoDriver().setMoteID(myID);
        leds.setOff((byte) 0);
        leds.setOn((byte) 1);
        radio.start();
        serial.start();
        routing = new Flooding(this);
        routing.setPacketLossModeOn();
        TTEventListener eventLst = TTEventListener.getInstanceOf();
        eventLst.registerSerialCommEventListner(this);
    }

    /**
     * Method specified by the ISerialCommEvent-Interface.
     * This method is called when the GPS-Module sends a packet with the
     * GPS-coordinates to the mote.
     *
     * @param packet    The packet received from the GPS-Module
     * @throws java.io.IOException
     */
    public void serialPacketReceiveEvent(Packet packet) throws IOException {
        if (packet.usedLength < 0) {
            return;
        }
        leds.setOn((byte) 1);
        for (byte i = 0; i < 11; i++) {
            gpsData[i] = packet.data[i];
        }
        routing.sendData(gpsData, (short) destID);
        leds.setOff((byte) 1);
    }

    /**
     * Method specified by the IDataReceiver-Interface of the Routing Protocol.
     * This method is called when a packet for this node is received.
     *
     * @param data
     * @param source
     */
    public void receiveData(byte[] data, short source) {
        leds.setOn((byte) 1);
        System.out.print((byte) 127);
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i]);
        }
        System.out.print((byte) -128);
        System.out.println();
        Thread.sleep(100);
        leds.setOffAll();
    }
}
