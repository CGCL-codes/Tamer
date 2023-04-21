package ca.ubc.cs.wiimote;

import java.io.IOException;
import java.util.*;
import javax.bluetooth.*;
import javax.microedition.io.Connector;
import ca.ubc.cs.wiimote.event.*;
import java.nio.ByteBuffer;

public class Wiimote {

    String address;

    public static final double fov_y = 31.0;

    public static final double fov_x = 41.0;

    protected LinkedList<WiimoteListener> listeners;

    protected L2CAPConnection sendCon;

    protected L2CAPConnection receiveCon;

    boolean connectionOpen;

    protected int light;

    protected Wiimote wiimote;

    protected WiiButtonEvent lastButtonEvent;

    protected static final byte COMMAND_LIGHT = 0x11;

    protected static final byte COMMAND_IR = 0x13;

    protected static final byte COMMAND_IR_2 = 0x1a;

    protected static final byte COMMAND_REGISTER = 0x16;

    protected static final byte COMMAND_REPORTING = 0x12;

    protected static final byte COMMAND_READ_CALIBRATION = 0x17;

    double[] calibrationZero, calibrationOne;

    Thread commandListener;

    static Object gate = new Object();

    boolean inited = false;

    boolean button_A_state, button_B_state, button_1_state, button_2_state, button_Z_state, button_C_state;

    /**
	 * Creates a Wiimote instance given an address. Forms connections to the wiimote, and
	 * readies sending and receiving of data. You likely won't call this yourself. Instead,
	 * your Wiimote instances will be created by WiimoteDiscoverer
	 */
    public Wiimote(String a) throws Exception {
        button_A_state = button_B_state = button_1_state = button_2_state = button_Z_state = button_C_state = false;
        connectionOpen = false;
        calibrationZero = new double[3];
        calibrationOne = new double[3];
        listeners = new LinkedList<WiimoteListener>();
        light = -1;
        wiimote = this;
        address = "btl2cap://" + a;
        try {
            receiveCon = (L2CAPConnection) Connector.open(address + ":13", Connector.READ, true);
            sendCon = (L2CAPConnection) Connector.open(address + ":11", Connector.WRITE, true);
            connectionOpen = true;
        } catch (Exception e) {
            if (sendCon != null) sendCon.close();
            if (receiveCon != null) receiveCon.close();
            System.out.println("Could not open. Connections reset.");
            throw e;
        }
        commandListener = new CommandListener();
        commandListener.start();
        readCalibration();
    }

    /**
	 * Attaches a WiimoteListener to this wiimote. The wiimote will send events to the
	 * given listener whenever somethign happens (e.g. button is pressed, accelerometer event occurs)
	 */
    public void addListener(WiimoteListener l) {
        if (!listeners.contains(l)) listeners.add(l);
    }

    /**
	 * Detaches the given WiimoteListener.
	 */
    public void removeListener(WiimoteListener l) {
        listeners.remove(l);
    }

    /**
	 * Dispatches wiimote events to all the WiimoteListeners.
	 */
    protected void dispatchEvent(WiiEvent e) {
        for (ListIterator<WiimoteListener> it = listeners.listIterator(); it.hasNext(); ) {
            WiimoteListener listener = it.next();
            if (e instanceof WiiButtonEvent) {
                if (((WiiButtonEvent) e).getWasPress()) listener.wiiButtonPress((WiiButtonEvent) e); else listener.wiiButtonRelease((WiiButtonEvent) e);
            }
            if (e instanceof WiiIREvent) listener.wiiIRInput((WiiIREvent) e);
            if (e instanceof WiiAccelEvent) if (((WiiAccelEvent) e).source == WiiAccelEvent.Source.Wiimote) listener.wiiAccelInput((WiiAccelEvent) e); else if (((WiiAccelEvent) e).source == WiiAccelEvent.Source.Nunchuk) listener.wiiNunchukAccelInput((WiiAccelEvent) e);
            if (e instanceof WiiNunchukJoystickEvent) listener.wiiNunchukJoystickInput((WiiNunchukJoystickEvent) e);
        }
    }

    public void enableIREvents() {
        sendCommand(COMMAND_REPORTING, new byte[] { 0x04, 0x33 });
        initWiimote();
    }

    /**
	 * sends the necessary commands to the wiimote to enable Extension events
	 */
    public void enableExtensionEvents() {
        sendCommand(COMMAND_REPORTING, new byte[] { 0x04, 0x37 });
        initWiimote();
        writeToRegister(0xa40040, new byte[] { 0x00 });
    }

    private void initWiimote() {
        try {
            sendCommand(COMMAND_IR, new byte[] { 0x04 });
            Thread.sleep(100);
            sendCommand(COMMAND_IR_2, new byte[] { 0x04 });
            Thread.sleep(100);
            writeToRegister(0xb00030, new byte[] { 0x08 });
            Thread.sleep(100);
            writeToRegister(0xb00000, new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x90, 0x00, (byte) 0xC0 });
            Thread.sleep(100);
            writeToRegister(0xb0001a, new byte[] { 0x40, 0x00 });
            Thread.sleep(100);
            writeToRegister(0xb00033, new byte[] { (byte) 3 });
            Thread.sleep(100);
            writeToRegister(0xb00030, new byte[] { 0x08 });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Not yet implemented
	 */
    public void disableIREvents() {
    }

    /**
	 * Turns on the given light on the wiimote. Lights are indexed from 0.
	 */
    public void setLight(int i) {
        if (i < 0 || i > 3) return;
        light = i;
        sendCommand(COMMAND_LIGHT, new byte[] { (byte) Math.pow(2, 4 + i) });
    }

    /**
	 * Returns which light is turned on on this wiimote
	 */
    public int getLight() {
        return light;
    }

    /**
	 * Causes the wiimote to vibrate for the given number of milliseconds.
	 */
    public void vibrate(final int millis) {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                sendCommand(COMMAND_LIGHT, new byte[] { (byte) (0x01 | (byte) (Math.pow(2, 4 + light))) });
                try {
                    Thread.sleep(millis);
                } catch (Exception e) {
                    System.out.println("vibrate exception: " + e);
                }
                sendCommand(COMMAND_LIGHT, new byte[] { (byte) (Math.pow(2, 4 + light)) });
            }
        });
        thread.start();
    }

    /**
	 * Causes the given data to be written to the given register address on the wiimote.
	 */
    private void writeToRegister(int address, byte[] data) {
        byte[] message = new byte[21];
        message[0] = 0x04;
        message[1] = (byte) (address >> 16 & 0xff);
        message[2] = (byte) (address >> 8 & 0xff);
        message[3] = (byte) (address & 0xff);
        message[4] = (byte) data.length;
        for (int i = 0; i < data.length; i++) {
            message[5 + i] = data[i];
        }
        sendCommand(COMMAND_REGISTER, message);
    }

    /**
	 * Sends message to the wiimote asking for accelerometer calibration data.
	 */
    private synchronized void readCalibration() {
        try {
            byte[] message = new byte[] { 0x00, 0x00, 0x00, 0x16, 0x00, 0x08 };
            sendCommand(COMMAND_READ_CALIBRATION, message);
        } catch (Exception e) {
            System.out.println("readCalibration exception: ");
            e.printStackTrace();
        }
    }

    /**
	 * Sends a generic command to the wiimote.
	 */
    private synchronized void sendCommand(byte command, byte[] payload) {
        try {
            byte[] message = new byte[2 + payload.length];
            message[0] = 82;
            message[1] = command;
            System.arraycopy(payload, 0, message, 2, payload.length);
            sendCon.send(message);
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    /**
	 * Causes the wiimote connections to close.
	 */
    protected void finalize() throws Throwable {
        cleanup();
    }

    /**
	 * Closes any open wiimote connections.
	 */
    public void cleanup() {
        synchronized (receiveCon) {
            connectionOpen = false;
            try {
                if (sendCon != null) {
                    sendCon.close();
                }
                if (receiveCon != null) {
                    receiveCon.close();
                }
            } catch (Exception e) {
                System.out.println("cleanup exception " + e);
            }
        }
    }

    /**
	 * This loops infinitely, constantly listenign for data from the wiimote. When
	 * data is received it responds accordingly.
	 */
    protected class CommandListener extends Thread {

        public void run() {
            while (true) {
                byte[] bytes = new byte[32];
                if (connectionOpen == true) {
                    try {
                        receiveCon.receive(bytes);
                    } catch (IOException e) {
                    } catch (Exception e) {
                        System.out.println("wiimote " + receiveCon + " " + light + " exception: receive(bytes) " + e);
                    }
                    switch(bytes[1]) {
                        case 0x21:
                            parseCalibrationResponse(ByteBuffer.allocate(8).put(bytes, 7, 8));
                            break;
                        case 0x30:
                            createButtonEvent(ByteBuffer.allocate(2).put(bytes, 2, 2));
                            break;
                        case 0x33:
                            createButtonEvent(ByteBuffer.allocate(2).put(bytes, 2, 2));
                            createWiimoteAccelEvent(ByteBuffer.allocate(5).put(bytes, 2, 5));
                            createIREvent(ByteBuffer.allocate(12).put(bytes, 7, 12));
                            break;
                        case 0x37:
                            createButtonEvent(ByteBuffer.allocate(2).put(bytes, 2, 2));
                            createWiimoteAccelEvent(ByteBuffer.allocate(5).put(bytes, 2, 5));
                            createExtensionEvent(ByteBuffer.allocate(6).put(bytes, 17, 6));
                            break;
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
		 * When a response to an accelerometer calibration request is received this
		 * parses it.
		 */
        protected void parseCalibrationResponse(ByteBuffer b) {
            calibrationZero[0] = ((b.get(0) & 0xFF) << 2) + (b.get(3) & 3);
            calibrationZero[1] = ((b.get(1) & 0xFF) << 2) + ((b.get(3) & 0xC) >> 2);
            calibrationZero[2] = ((b.get(2) & 0xFF) << 2) + ((b.get(3) & 0x30) >> 4);
            calibrationOne[0] = ((b.get(4) & 0xFF) << 2) + (b.get(7) & 3);
            calibrationOne[1] = ((b.get(5) & 0xFF) << 2) + ((b.get(7) & 0xC) >> 2);
            calibrationOne[2] = ((b.get(6) & 0xFF) << 2) + ((b.get(7) & 0x30) >> 4);
        }

        /**
		 * Creates a WiiButtonEvent from raw data
		 */
        protected void createButtonEvent(ByteBuffer b) {
            int i = (b.get(0) << 8) | b.get(1);
            if (((i & 8) != 0) != button_A_state) {
                button_A_state = !button_A_state;
                WiiButtonEvent event = new WiiButtonEvent(wiimote, WiiButtonEvent.Button.B_A, button_A_state);
                wiimote.dispatchEvent(event);
            }
            if (((i & 4) != 0) != button_B_state) {
                button_B_state = !button_B_state;
                WiiButtonEvent event = new WiiButtonEvent(wiimote, WiiButtonEvent.Button.B_B, button_B_state);
                wiimote.dispatchEvent(event);
            }
            if (((i & 2) != 0) != button_1_state) {
                button_1_state = !button_1_state;
                WiiButtonEvent event = new WiiButtonEvent(wiimote, WiiButtonEvent.Button.B_1, button_1_state);
                wiimote.dispatchEvent(event);
            }
            if (((i & 1) != 0) != button_2_state) {
                button_2_state = !button_2_state;
                WiiButtonEvent event = new WiiButtonEvent(wiimote, WiiButtonEvent.Button.B_2, button_2_state);
                wiimote.dispatchEvent(event);
            }
        }

        /**
		 * Creates a WiiAccelEvent from raw data
		 */
        protected void createWiimoteAccelEvent(ByteBuffer b) {
            WiiAccelEvent event = createGenericAccelEvent(b);
            event.source = WiiAccelEvent.Source.Wiimote;
            wiimote.dispatchEvent(event);
        }

        protected WiiAccelEvent createGenericAccelEvent(ByteBuffer b) {
            int x = ((b.get(2) & 0xff) << 2) + ((b.get(0) & 0x60) >> 5);
            int y = ((b.get(3) & 0xff) << 2) + ((b.get(1) & 0x60) >> 5);
            int z = ((b.get(4) & 0xff) << 2) + ((b.get(1) & 0x80) >> 6);
            double xaccel = ((double) x - calibrationZero[0]) / (calibrationOne[0] - calibrationZero[0]);
            double yaccel = ((double) y - calibrationZero[1]) / (calibrationOne[1] - calibrationZero[1]);
            double zaccel = ((double) z - calibrationZero[2]) / (calibrationOne[2] - calibrationZero[2]);
            return new WiiAccelEvent(wiimote, xaccel, yaccel, zaccel);
        }

        /**
		 * Creates a WiiIREvent from raw data
		 */
        protected void createIREvent(ByteBuffer b) {
            for (int i = 0; i < b.limit(); i += 3) {
                int x = (b.get(i) & 0xFF) + ((b.get(i + 2) & 0x30) << 4);
                int y = (b.get(i + 1) & 0xFF) + ((b.get(i + 2) & 0xc0) << 2);
                int size = (b.get(i + 2) & 0xF);
                if (x != 1023 && y != 1023) {
                    WiiIREvent event = new WiiIREvent(wiimote, i, x, y, size);
                    wiimote.dispatchEvent(event);
                }
            }
        }

        protected void createExtensionEvent(ByteBuffer b) {
            for (int i = 0; i < b.capacity(); i++) {
                b.put(i, (byte) ((b.get(i) ^ 0x17) + 0x17));
            }
            createNunchukAccelEvent(b);
            WiiNunchukJoystickEvent je = new WiiNunchukJoystickEvent(wiimote, (b.get(0) & 0xFF) - 130, (b.get(1) & 0xFF) - 130);
            wiimote.dispatchEvent(je);
            if (((b.get(5) & 0x01) == 0) != button_Z_state) {
                button_Z_state = !button_Z_state;
                WiiButtonEvent event = new WiiButtonEvent(wiimote, WiiButtonEvent.Button.B_Z, button_Z_state);
                wiimote.dispatchEvent(event);
            }
            if (((b.get(5) & 0x02) == 0) != button_C_state) {
                button_C_state = !button_C_state;
                WiiButtonEvent event = new WiiButtonEvent(wiimote, WiiButtonEvent.Button.B_C, button_C_state);
                wiimote.dispatchEvent(event);
            }
        }

        protected void createNunchukAccelEvent(ByteBuffer b) {
            WiiAccelEvent event = createGenericAccelEvent(b);
            event.source = WiiAccelEvent.Source.Nunchuk;
            wiimote.dispatchEvent(event);
        }
    }
}
