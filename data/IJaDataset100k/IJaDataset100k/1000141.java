package com.sun.spot.spotworld.usb.checkbasestation;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import com.sun.spot.peripheral.Spot;
import com.sun.spot.peripheral.ota.ISpotBootloaderConstants;

/**
 * NOT COMPLETELY IMPLEMENTED, AND NOT USED BY SPOTWORLD. THIS IS A PLACEHOLDER CLASS FOR NOW.
 *
 * The CheckBasestation class determines whether a device connected to a given
 * serial port is a base station. Due to limitations in the module used to do
 * the detection, CheckBaseStation must be executed as a separate process.
 *
 * @author Christian P�hringer
 *
 */
public class CheckBasestation {

    private static PrintStream infoOut = System.out;

    private String baseStationPort;

    private boolean isVerbose = Boolean.getBoolean("verbose");

    private InputStream inStream;

    private BufferedReader in;

    private OutputStream out;

    private SerialPort serialPort;

    private boolean portIsInUse;

    private CheckBasestation(String port) {
        super();
        this.baseStationPort = port;
    }

    public CheckBasestation() {
    }

    private boolean detectBootloader() {
        try {
            if (isVerbose) System.out.println("detect bootloader");
            try {
                initComms(baseStationPort);
                portIsInUse = false;
            } catch (PortInUseException e1) {
                if (isVerbose) e1.printStackTrace();
                portIsInUse = true;
                return false;
            }
            sendBootloaderCommand("X");
            boolean isOk = false;
            while (!isOk) {
                try {
                    System.out.println(".");
                    Thread.sleep(50);
                    isOk = true;
                } catch (InterruptedException e) {
                }
            }
            System.out.println(".");
            int bytesAvailable = inStream.available();
            byte[] buf = new byte[bytesAvailable];
            if (bytesAvailable > 0) {
                inStream.read(buf);
                String spotReply = new String(buf);
                if (isVerbose) System.out.println("spot:" + spotReply);
                if (spotReply.lastIndexOf(ISpotBootloaderConstants.BOOTLOADER_CMD_HEADER) != -1) {
                    if (isVerbose) System.out.println("Spot is in bootloader prompt, Not a basestation (at least not a running one)");
                    closeComms();
                    return false;
                }
            }
            closeComms();
        } catch (IOException e) {
            if (isVerbose) e.printStackTrace();
            return false;
        }
        if (isVerbose) System.out.println("OK for scan, not bootloader detectect.");
        return true;
    }

    public boolean checkIsBaseStation(String port) {
        baseStationPort = port;
        return checkIsBaseStation();
    }

    /**
     * Determines whether the device connected to the port is a base
     * station.
     *
     * @return returns true if the device connected to the serial port is a
     *         base station
     *
     */
    public boolean checkIsBaseStation() {
        System.setProperty("SERIAL_PORT", baseStationPort);
        try {
            if (detectBootloader() == false) return false;
            System.out.println("Testing if spot " + baseStationPort + " responds.");
            System.out.println("IEEE Address: " + Spot.getInstance().getRadioPolicyManager().getIEEEAddress());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace(infoOut);
        }
        return false;
    }

    /**
     * Initialize the comms connection
     *
     * @param portName
     *                Port to use
     * @throws IOException
     */
    private void initComms(String portName) throws PortInUseException, IOException {
        CommPortIdentifier portId = null;
        Enumeration portList;
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier nextPortId = (CommPortIdentifier) portList.nextElement();
            if (nextPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (nextPortId.getName().equalsIgnoreCase(portName)) {
                    portId = nextPortId;
                }
            }
        }
        if (portId == null) {
            throw new IOException(portName + " not found");
        }
        serialPort = (SerialPort) portId.open("Flasher", 2000);
        try {
            serialPort.disableReceiveThreshold();
            serialPort.enableReceiveTimeout(3000);
            serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
            throw new IOException(e.getMessage());
        }
        inStream = serialPort.getInputStream();
        in = new BufferedReader(new InputStreamReader(inStream));
        out = serialPort.getOutputStream();
    }

    /**
     * Close the comms connection
     *
     * @throws IOException
     */
    private void closeComms() throws IOException {
        inStream.close();
        in.close();
        out.close();
        serialPort.close();
    }

    private void sendBootloaderCommand(String string) throws IOException {
        out.write(string.getBytes());
    }

    /**
     * Checks whether the connected device is a base station.
     *
     * @param args
     *                The first command line argument is the serial port
     *                which will be checked whether a base station is
     *                connected.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            infoOut.println("Wrong argument format. \n\t usage: CheckBasestation SERIAL_PORT");
            System.exit(-1);
        }
        String port = args[0];
        CheckBasestation checkBasestation = new CheckBasestation(port);
        if (checkBasestation.checkIsBaseStation()) System.out.println("IS BASESTATION"); else {
            if (checkBasestation.portIsInUse) System.out.println("PORT IN USE"); else System.out.println("PORT NOT IN USE");
        }
    }
}
