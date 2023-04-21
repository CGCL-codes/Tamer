package org.hswgt.teachingbox.crawler.env;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.datastructures.ActionFilter;
import org.hswgt.teachingbox.core.rl.datastructures.ActionSet;
import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.Environment;
import org.hswgt.teachingbox.core.rl.env.State;
import org.hswgt.teachingbox.gridworldeditor.gui.GridWorldGUI;
import org.hswgt.teachingbox.gridworldeditor.model.GridModel;

/**
 * The crawling robot environment
 * @author tokicm
 *
 */
public class CrawlerEnvironment implements Environment {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2243486529897811249L;

    private GridWorldGUI gridWorld;

    private static final Logger log4j = Logger.getLogger("CrawlerEnvironment");

    /**
	 * There are 4 possible actions: LEFT, RIGTH, UP, DOWN
	 */
    public static final Action LEFT = new Action(new double[] { -1, 0 });

    public static final Action RIGHT = new Action(new double[] { +1, 0 });

    public static final Action UP = new Action(new double[] { 0, +1 });

    public static final Action DOWN = new Action(new double[] { 0, -1 });

    /**
	 * The grid size of the robot
	 */
    public static final double maxX = 5;

    public static final double maxY = 5;

    private static final byte WALK_AND_RETURN = 0x1B;

    private static final byte GET_CURRENT_STATE = 0x1E;

    private static final byte SET_SERVO_POSITION = 0x20;

    private State currentState;

    /**
	 * The ActionSet contains all possible actions
	 */
    public static final ActionSet ACTION_SET = new ActionSet(new Action[] { LEFT, RIGHT, UP, DOWN });

    /**
	 * The agent is not allowed to cross the borders
	 */
    public static final ActionFilter FILTER = new ActionFilter() {

        private static final long serialVersionUID = -5057734785625735723L;

        public boolean isPermitted(final State s, final Action a) {
            if (s.get(0) <= 0 && a.equals(LEFT)) return false;
            if (s.get(0) >= maxX - 1 && a.equals(RIGHT)) return false;
            if (s.get(1) <= 0 && a.equals(UP)) return false;
            if (s.get(1) >= maxY - 1 && a.equals(DOWN)) return false;
            return true;
        }
    };

    static {
        ACTION_SET.addFilter(FILTER);
    }

    /**
	 * serial config
	 */
    private CommPortIdentifier serialPortId;

    private Enumeration enumComm;

    private SerialPort serialPort;

    private OutputStream outputStream;

    private InputStream inputStream;

    private Boolean serialPortOpen = false;

    private int baudrate = 19200;

    private int dataBits = SerialPort.DATABITS_8;

    private int stopBits = SerialPort.STOPBITS_1;

    private int parity = SerialPort.PARITY_NONE;

    private String portName = "/dev/ttyUSB0";

    /**
	 * Class for processing serial data messages
	 * @author tokicm
	 */
    private class CrawlerMessage {

        public byte type = 0x00;

        public byte length = 0x00;

        public byte data[] = new byte[255];

        private byte msg[] = new byte[260];

        /**
		 * Constructor for a message given CrawlerEnvironment.type and an action a
		 * @param type The type e.g. CrawlerEnvironment.WALK_AND_RETURN
		 * @param a The action to transmit
		 */
        CrawlerMessage(byte type, Action a) {
            this.type = type;
            this.length = 0x01;
            if (a.equals(UP)) {
                log4j.debug("UP");
                this.data[0] = 0x00;
            } else if (a.equals(DOWN)) {
                log4j.debug("DOWN");
                this.data[0] = 0x01;
            } else if (a.equals(LEFT)) {
                log4j.debug("LEFT");
                this.data[0] = 0x02;
            } else if (a.equals(RIGHT)) {
                log4j.debug("RIGHT");
                this.data[0] = 0x03;
            }
        }

        /**
		 * Constructor for a request message given CrawlerEnvironment.type
		 * @param type The type e.g. CrawlerEnvironment.WALK_AND_RETURN
		 */
        CrawlerMessage(byte type) {
            this.type = type;
            this.length = 0x00;
        }

        /**
		 * Constructor for a request message given a byte array received from the serial line
		 * @param bytes
		 */
        CrawlerMessage(byte[] bytes) {
            this.type = bytes[0];
            this.length = bytes[1];
            for (int i = 0; i < this.length; i++) {
                this.data[i] = bytes[i + 2];
            }
        }

        /**
		 * returns the message as a string
		 */
        public String getMessage() {
            msg[0] = type;
            msg[1] = length;
            for (int i = 0; i < length; i++) {
                msg[2 + i] = data[i];
            }
            return new String(msg);
        }

        /**
		 * Message data is interpreted as a state position and returned as a state
		 * @return
		 */
        public State getState() {
            return new State(new double[] { new Double(data[0]), new Double(data[1]) }).copy();
        }

        /**
		 * Message data[2] is interpreted as a reward and returned as a double
		 */
        public double getReward() {
            return new Double(data[2]);
        }

        /**
		 * prints out the current message
		 */
        public void debugMessage() {
            int tmp = this.type;
            String output = new String();
            output += "\n" + tmp;
            tmp = this.length;
            output += "," + tmp;
            for (int i = 0; i < this.length; i++) {
                tmp = this.data[i];
                output += "," + tmp;
            }
            log4j.debug(output);
        }
    }

    /**
	 * The constructor
	 * @param baudrate the baudrate eg. 19200 
	 * @param portname the portname eg. /dev/ttyUSB0
	 */
    public CrawlerEnvironment(int baudrate, String portname) {
        this.baudrate = baudrate;
        this.portName = portname;
        gridWorld = new GridWorldGUI("data/gridworld/gridworld.xml");
        this.currentState = requestCurrentState();
    }

    /**
	 * Method transmits action a to the robot and observes the reward+successor state.
	 * @return The reward observed by the robot 
	 */
    public synchronized double doAction(Action a) {
        double reward = 0;
        State s = this.getState();
        State newState;
        int gridAction = 0;
        if (a.equals(UP)) {
            gridAction = GridModel.UP;
        } else if (a.equals(DOWN)) {
            gridAction = GridModel.DOWN;
        } else if (a.equals(LEFT)) {
            gridAction = GridModel.LEFT;
        } else if (a.equals(RIGHT)) {
            gridAction = GridModel.RIGHT;
        }
        State oldState = this.getState();
        if (s.get(0) <= 0 && a.equals(LEFT) || s.get(0) >= GridModel.getInstance().getSize().getWidth() - 1 && a.equals(RIGHT) || s.get(1) <= 0 && a.equals(UP) || s.get(1) >= GridModel.getInstance().getSize().getHeight() - 1 && a.equals(DOWN)) {
            log4j.debug("performing virtual (border) action");
            GridModel.getInstance().getCell((int) oldState.get(0), (int) oldState.get(1)).setReward(-300, gridAction);
            reward = -300;
            log4j.debug(" reward=" + reward);
        } else {
            log4j.debug("performing Action (" + a.get(0) + "," + a.get(1) + ")  ... ");
            oldState = this.getState();
            if (!this.serialPortOpen && this.openSerialPort()) {
                this.clearBuffer();
                CrawlerMessage cm = new CrawlerMessage(WALK_AND_RETURN, a);
                gridAction = 0;
                if (a.equals(UP)) {
                    gridAction = GridModel.UP;
                } else if (a.equals(DOWN)) {
                    gridAction = GridModel.DOWN;
                } else if (a.equals(LEFT)) {
                    gridAction = GridModel.LEFT;
                } else if (a.equals(RIGHT)) {
                    gridAction = GridModel.RIGHT;
                }
                cm.debugMessage();
                this.transmitMessage(cm.getMessage().getBytes());
                byte rcvMsg[] = this.receiveMessage();
                reward = new CrawlerMessage(rcvMsg).getReward();
                log4j.debug("doAction() - successorState=(" + this.currentState.get(0) + "," + this.currentState.get(1) + ")");
                this.currentState = new CrawlerMessage(rcvMsg).getState();
                log4j.debug("doAction() - successorState=(" + this.currentState.get(0) + "," + this.currentState.get(1) + ")");
                GridModel.getInstance().getCell((int) oldState.get(0), (int) oldState.get(1)).setReward(reward, gridAction);
                synchronized (GridModel.class) {
                    GridModel.getInstance().setActCol((int) currentState.get(0));
                    GridModel.getInstance().setActRow((int) currentState.get(1));
                }
                log4j.debug(" reward=" + reward);
                this.closeSerialPort();
            }
            gridWorld.refreshTable();
            log4j.debug("doAction() finished");
        }
        return reward;
    }

    /**
	 * returns the current state
	 */
    public State getState() {
        return this.currentState.copy();
    }

    private State requestCurrentState() {
        log4j.debug("requestCurrentState() started");
        byte[] msg = new byte[260];
        State newState = new State(new double[] { -1, -1 });
        if (this.openSerialPort()) {
            this.clearBuffer();
            CrawlerMessage cm = new CrawlerMessage(GET_CURRENT_STATE);
            cm.debugMessage();
            this.transmitMessage(cm.getMessage().getBytes());
            msg = this.receiveMessage();
            newState = new CrawlerMessage(msg).getState();
            this.currentState = newState.copy();
            synchronized (GridModel.class) {
                GridModel.getInstance().setActCol((int) newState.get(0));
                GridModel.getInstance().setActRow((int) newState.get(1));
            }
            log4j.info("requestCurrentState() - Current State: x=" + newState.get(0) + ", y=" + newState.get(1));
            this.closeSerialPort();
        }
        return newState.copy();
    }

    public void init(State s) {
    }

    public void initRandom() {
    }

    public boolean isTerminalState() {
        return false;
    }

    private boolean openSerialPort() {
        Boolean foundPort = false;
        if (serialPortOpen != false) {
            System.out.println("Serialport is already open!");
            return false;
        }
        enumComm = CommPortIdentifier.getPortIdentifiers();
        while (enumComm.hasMoreElements()) {
            serialPortId = (CommPortIdentifier) enumComm.nextElement();
            if (portName.contentEquals(serialPortId.getName())) {
                foundPort = true;
                break;
            }
        }
        if (foundPort != true) {
            System.out.println("Serialport not found: " + portName);
            return false;
        }
        try {
            serialPort = (SerialPort) serialPortId.open("open and send", 500);
        } catch (PortInUseException e) {
            System.out.println("port is in use");
        }
        try {
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
            System.out.println("no access to OutputStream");
        }
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {
            System.out.println("Keinen Zugriff auf InputStream");
        }
        try {
            serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
        } catch (UnsupportedCommOperationException e) {
            System.out.println("could not set interface properties");
        }
        serialPortOpen = true;
        return true;
    }

    private void closeSerialPort() {
        if (serialPortOpen == true) {
            log4j.debug("closing Serialport");
            serialPort.close();
            serialPortOpen = false;
        } else {
            System.out.println("Serialport is already closed");
        }
    }

    public void setServo(int xID, int xMin, int xMax, int yID, int yMin, int yMax) {
        log4j.debug("manipulate Servo ranges started");
        byte[] msg = new byte[28];
        msg[0] = SET_SERVO_POSITION;
        msg[1] = 0x18;
        msg[2] = 0x00;
        msg[3] = 0x00;
        msg[4] = 0x00;
        msg[5] = 0x02;
        msg[6] = 0x00;
        msg[7] = 0x00;
        msg[8] = 0x00;
        msg[9] = 0x64;
        msg[10] = 0x00;
        msg[11] = 0x00;
        msg[12] = 0x00;
        msg[13] = 0x74;
        msg[14] = 0x00;
        msg[15] = 0x00;
        msg[16] = 0x00;
        msg[17] = 0x06;
        msg[18] = 0x00;
        msg[19] = 0x00;
        msg[20] = 0x00;
        msg[21] = 0x64;
        msg[22] = 0x00;
        msg[23] = 0x00;
        msg[24] = 0x00;
        msg[25] = 0x74;
        CrawlerMessage cm = new CrawlerMessage(msg);
        cm.debugMessage();
        if (this.openSerialPort()) {
            this.clearBuffer();
            this.transmitMessage(cm.getMessage().getBytes());
        }
        this.closeSerialPort();
    }

    private void transmitMessage(byte[] msg) {
        int msgSize = msg[1] + 2;
        byte[] transmitMessage = new byte[msgSize];
        log4j.debug("transmitMessage() - " + transmitMessage);
        for (int i = 0; i < msgSize; i++) {
            transmitMessage[i] = msg[i];
            log4j.debug("Transmit-Byte[" + i + "]: " + new Double(transmitMessage[i]));
        }
        if (serialPortOpen != true) return;
        try {
            outputStream.write(transmitMessage);
        } catch (IOException e) {
            System.out.println("Transmit error!!");
        }
    }

    private byte[] receiveMessage() {
        byte[] retArray = new byte[255];
        byte[] rettmp = new byte[255];
        int tmp = 0;
        log4j.debug("receiving message ...");
        try {
            byte[] data = new byte[150];
            int id = 0;
            int dlc = 0;
            log4j.debug("waiting for data ... ");
            if (!this.waitForData(1000)) {
                return retArray;
            }
            inputStream.read(retArray, id, 1);
            for (int p = 0; retArray[id] < 0 && p < 10; p++) {
                inputStream.read(retArray, id, 1);
                log4j.debug("trying to get the command,... " + p);
            }
            log4j.debug("received command: " + new Double(retArray[id]));
            id++;
            log4j.debug("waiting for data ... ");
            if (!this.waitForData(1000)) {
                return retArray;
            }
            inputStream.read(retArray, id, 1);
            for (int p = 0; retArray[id] < 0 && p < 10; p++) {
                inputStream.read(retArray, id, 1);
            }
            log4j.debug("received length: " + new Double(retArray[id]));
            dlc = new Integer(retArray[id]);
            id++;
            for (int i = 0; i < dlc; i++) {
                log4j.debug("waiting for data ... ");
                if (!this.waitForData(1000)) {
                    return retArray;
                }
                inputStream.read(retArray, id, 1);
                log4j.debug("received data[" + i + "]: " + new Double(retArray[id]));
                id++;
            }
            log4j.debug("message reception finished.");
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen empfangener Daten");
        }
        return retArray;
    }

    private boolean waitForData(int millis) {
        int errorCounter = 0;
        int maxCounter = millis / 10;
        try {
            while (inputStream.available() == 0 && errorCounter < maxCounter) {
                try {
                    Thread.sleep((int) 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                errorCounter++;
                log4j.debug("waiting " + "10 milliseconds - errorCounter=" + errorCounter);
            }
            if (errorCounter == maxCounter) {
                System.out.println("ERROR: robot timeout - waiting 3 seconds ");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            }
            log4j.debug("waitForData() - message reception finished.");
        } catch (IOException e) {
            System.out.println("waitForData() - error during data reception");
        }
        return true;
    }

    private void clearBuffer() {
        log4j.debug("clearing buffer ...");
        try {
            byte[] data = new byte[150];
            int num;
            while (inputStream.available() > 0) {
                num = inputStream.read(data, 0, data.length);
                System.out.println("receiving: " + new String(data, 0, num));
            }
            log4j.debug("garbage received: " + data);
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen empfangener Daten");
        }
    }
}
