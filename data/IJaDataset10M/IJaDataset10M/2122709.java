package com.camera;

import java.io.*;
import java.net.*;
import java.util.*;
import gnu.io.*;

/** Code to control the PTU-D46 Pan Tilt Unit. */
public class CameraControl2 {

    protected boolean verbose = false;

    protected static final String DEF_WIN_PORTNAME = "COM3";

    protected static final String DEF_UNIX_PORTNAME = "/dev/ttyS2";

    protected static final long DEF_WAIT_FOR_PORT_TIMEOUT = 1000;

    protected static final int DEF_START_BAUD_RATE = 9600;

    protected static final int DEF_DESIRED_BAUD_RATE = 38400;

    protected static final int DEF_READ_BUF_SIZE = 2048;

    static CommPortIdentifier portId;

    static Enumeration portList;

    static SerialPort serialPort;

    private boolean portOpenOk = false;

    private OutputStream outputStream;

    private InputStream inputStream;

    private BufferedInputStream bufferedIS;

    private BufferedReader bufferedReader;

    private String portName = "";

    private int pan_curr = 150, tilt_curr = 90;

    private int curr_pan_speed = -1, curr_tilt_speed = -1, max_pan_speed = -1, max_tilt_speed = -1;

    public CameraControl2(String portname) {
        portName = portname;
        openPort(portName, DEF_START_BAUD_RATE);
        bufferedReader = new BufferedReader(new InputStreamReader(bufferedIS));
        byte[] commandData = ("FT ").getBytes();
        sendCommand(commandData);
        String response = readPacket();
        if (verbose) System.out.println(response);
        byte[] commandData2 = ("EE ").getBytes();
        sendCommand(commandData2);
        String response2 = readPacket();
        if (verbose) System.out.println(response2);
        byte[] commandData3 = ("PS2000 ").getBytes();
        sendCommand(commandData3);
        String response3 = readPacket();
        if (verbose) System.out.println(response3);
        init();
        bufferMaxPanSpeed();
        bufferMaxTiltSpeed();
        new Thread() {

            public void run() {
                while (true) {
                    bufferPan();
                    bufferTilt();
                    bufferPanSpeed();
                    bufferTiltSpeed();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ie) {
                    }
                }
            }
        }.start();
    }

    private String readPacket() {
        String response = "";
        if (verbose) System.out.println("reading in a packet");
        try {
            response = bufferedReader.readLine();
            if (verbose) System.out.println("response = " + response);
            if (response.indexOf('!') == -1) {
                if (response.substring(0, 3).equals("PP ")) {
                    pan_curr = unitsToDegreesPan(Integer.valueOf(response.substring(5)));
                    if (verbose) System.out.println("setting pan_curr to " + pan_curr);
                } else if (response.substring(0, 3).equals("TP ")) {
                    tilt_curr = unitsToDegreesTilt(Integer.valueOf(response.substring(5)));
                    if (verbose) System.out.println("setting tilt_curr to " + tilt_curr);
                } else if (response.substring(0, 3).equals("PS ")) {
                    curr_pan_speed = Integer.valueOf(response.substring(5));
                    if (verbose) System.out.println("setting curr_pan_speed to " + curr_pan_speed);
                } else if (response.substring(0, 3).equals("TS ")) {
                    curr_tilt_speed = Integer.valueOf(response.substring(5));
                    if (verbose) System.out.println("setting curr_tilt_speed to " + curr_tilt_speed);
                } else if (response.substring(0, 3).equals("PU ")) {
                    max_pan_speed = Integer.valueOf(response.substring(5));
                    if (verbose) System.out.println("setting max_pan_speed to " + max_pan_speed);
                } else if (response.substring(0, 3).equals("TU ")) {
                    max_tilt_speed = Integer.valueOf(response.substring(5));
                    if (verbose) System.out.println("setting max_tilt_speed to " + max_tilt_speed);
                }
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        } catch (IndexOutOfBoundsException ioob) {
        } catch (NumberFormatException nfe) {
            System.err.println(nfe);
        }
        return response;
    }

    public void closePort() {
        serialPort.close();
    }

    private void openPort(String pName, int baud) {
        if (verbose) System.out.println("Opening port " + pName);
        portList = CommPortIdentifier.getPortIdentifiers();
        if (verbose) System.out.println("Got port identifiers...");
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (verbose) System.out.println("Checking " + portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(pName)) {
                    if (verbose) System.out.println("Found serial port " + pName);
                    try {
                        serialPort = (SerialPort) portId.open("LaserApp", (int) DEF_WAIT_FOR_PORT_TIMEOUT);
                        if (verbose) System.out.println(pName + " opened successfully");
                        portOpenOk = true;
                    } catch (PortInUseException e) {
                        System.err.println("Port " + pName + " in use -- failure");
                    } catch (Exception e) {
                        System.err.println("Error opening port " + pName + ":");
                        System.err.println(e);
                    }
                    if (verbose) System.out.println("Setting port parameters...");
                    try {
                        serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                    } catch (UnsupportedCommOperationException e) {
                        System.err.println("Could not set port parameters -- failure");
                    }
                    if (verbose) System.out.println("Setting port streams...");
                    try {
                        serialPort.setInputBufferSize(DEF_READ_BUF_SIZE);
                        outputStream = serialPort.getOutputStream();
                        inputStream = serialPort.getInputStream();
                        bufferedIS = new BufferedInputStream(inputStream, DEF_READ_BUF_SIZE);
                        serialPort.notifyOnDataAvailable(true);
                    } catch (IOException e) {
                        System.err.println("Could not get port streams -- failure");
                    }
                }
            }
        }
    }

    private void sendCommand(byte[] commandData) {
        try {
            outputStream.write(commandData);
        } catch (IOException ie) {
            System.err.println(ie);
        }
    }

    /** Initializes the camera. */
    public void init() {
        if (verbose) System.out.println("initializing PTU");
        byte[] commandData = "R ".getBytes();
        sendCommand(commandData);
        String response = readPacket();
        if (verbose) System.out.println(response);
    }

    public void cancelLastCommand() {
    }

    public void fullStop() {
        byte[] commandData = "H ".getBytes();
        sendCommand(commandData);
        String response = readPacket();
        if (verbose) System.out.println(response);
    }

    public void pan(int horizontal) {
        byte[] commandData = ("PP" + degreesToUnitsPan(horizontal) + " ").getBytes();
        sendCommand(commandData);
        byte[] commandData2 = ("A ").getBytes();
        sendCommand(commandData2);
        String response = readPacket();
        if (verbose) System.out.println(response);
        String response2 = readPacket();
        if (verbose) System.out.println(response2);
    }

    public void panRelative(int deg) {
        byte[] commandData = ("PO" + -20.6 * deg + " ").getBytes();
        sendCommand(commandData);
        byte[] commandData2 = ("A ").getBytes();
        sendCommand(commandData2);
        String response = readPacket();
        if (verbose) System.out.println(response);
        String response2 = readPacket();
        if (verbose) System.out.println(response2);
    }

    public void tilt(int vertical) {
        byte[] commandData = ("TP" + degreesToUnitsTilt(vertical) + " ").getBytes();
        sendCommand(commandData);
        byte[] commandData2 = ("A ").getBytes();
        sendCommand(commandData2);
        String response = readPacket();
        if (verbose) System.out.println(response);
        String response2 = readPacket();
        if (verbose) System.out.println(response2);
    }

    public void tiltRelative(int deg) {
        byte[] commandData = ("TO" + -20.6 * deg + " ").getBytes();
        sendCommand(commandData);
        byte[] commandData2 = ("A ").getBytes();
        sendCommand(commandData2);
        String response = readPacket();
        if (verbose) System.out.println(response);
        String response2 = readPacket();
        if (verbose) System.out.println(response2);
    }

    public void panTilt(int pan, int tilt) {
        byte[] commandData = ("PP" + degreesToUnitsPan(pan) + " ").getBytes();
        sendCommand(commandData);
        byte[] commandData2 = ("TP" + degreesToUnitsTilt(tilt) + " ").getBytes();
        sendCommand(commandData2);
        byte[] commandData3 = ("A ").getBytes();
        sendCommand(commandData3);
        String response = readPacket();
        if (verbose) System.out.println(response);
        String response2 = readPacket();
        if (verbose) System.out.println(response2);
        String response3 = readPacket();
        if (verbose) System.out.println(response3);
    }

    public void panTiltRelative(int pan, int tilt) {
        byte[] commandData = ("PO" + -20.6 * pan + " ").getBytes();
        sendCommand(commandData);
        byte[] commandData2 = ("TO" + -20.6 * tilt + " ").getBytes();
        sendCommand(commandData2);
        byte[] commandData3 = ("A ").getBytes();
        sendCommand(commandData3);
        String response = readPacket();
        if (verbose) System.out.println(response);
        String response2 = readPacket();
        if (verbose) System.out.println(response2);
        String response3 = readPacket();
        if (verbose) System.out.println(response3);
    }

    public void zoom(int amount) {
        System.err.println("current camera not capable of zoom");
    }

    private void bufferPan() {
        byte[] commandData = ("PP ").getBytes();
        sendCommand(commandData);
        String response = readPacket();
        if (verbose) System.out.println(response);
    }

    public int getPan() {
        return pan_curr;
    }

    private void bufferTilt() {
        byte[] commandData = ("TP ").getBytes();
        sendCommand(commandData);
        String response = readPacket();
        if (verbose) System.out.println(response);
    }

    public int getTilt() {
        return tilt_curr;
    }

    private void bufferPanSpeed() {
        byte[] commandData = ("PS ").getBytes();
        sendCommand(commandData);
        String response = readPacket();
        if (verbose) System.out.println(response);
    }

    public int getPanSpeed() {
        return curr_pan_speed;
    }

    private void bufferTiltSpeed() {
        byte[] commandData = ("TS ").getBytes();
        sendCommand(commandData);
        String response = readPacket();
        if (verbose) System.out.println(response);
    }

    public int getTiltSpeed() {
        return curr_tilt_speed;
    }

    public int getZoom() {
        return 0;
    }

    private void bufferMaxPanSpeed() {
        byte[] commandData = ("PU ").getBytes();
        sendCommand(commandData);
        String response = readPacket();
        if (verbose) System.out.println(response);
    }

    public int getMaxPanSpeed() {
        return max_pan_speed;
    }

    private void bufferMaxTiltSpeed() {
        byte[] commandData = ("TU ").getBytes();
        sendCommand(commandData);
        String response = readPacket();
        if (verbose) System.out.println(response);
    }

    public int getMaxTiltSpeed() {
        return max_tilt_speed;
    }

    public static void main(String[] args) {
        CameraControl2 cc = new CameraControl2("/dev/ttyUSB0");
        cc.panRelative(50);
        cc.panRelative(-100);
        cc.panRelative(50);
    }

    private int degreesToUnitsPan(int degrees) {
        int units = 0;
        if (degrees == 150) {
            units = 0;
        } else if (degrees > 150) {
            units = (int) (-20.6 * (degrees - 150));
        } else if (degrees < 150) {
            units = (int) (20.6 * (150 - degrees));
        }
        return units;
    }

    private int unitsToDegreesPan(int units) {
        int degrees = 150;
        if (units == 0) {
            degrees = 150;
        } else if (units > 0) {
            degrees = (int) (150 - units / 20.6);
        } else if (units < 0) {
            degrees = (int) (150 + units / -20.6);
        }
        return degrees;
    }

    private int degreesToUnitsTilt(int degrees) {
        int units = 0;
        if (degrees == 90) {
            units = 0;
        } else if (degrees > 90) {
            units = (int) (-20.6 * (degrees - 90));
        } else if (degrees < 90) {
            units = (int) (20.6 * (90 - degrees));
        }
        return units;
    }

    private int unitsToDegreesTilt(int units) {
        int degrees = 90;
        if (units == 0) {
            degrees = 90;
        } else if (units > 0) {
            degrees = (int) (90 - units / 20.6);
        } else if (units < 0) {
            degrees = (int) (90 + units / -20.6);
        }
        return degrees;
    }

    public void setPanSpeed(int speed) {
        byte[] commandData = ("PS" + speed + " ").getBytes();
        sendCommand(commandData);
        String response = readPacket();
        if (verbose) System.out.println(response);
    }

    public void setTiltSpeed(int speed) {
        byte[] commandData = ("TS" + speed + " ").getBytes();
        sendCommand(commandData);
        String response = readPacket();
        if (verbose) System.out.println(response);
    }
}
