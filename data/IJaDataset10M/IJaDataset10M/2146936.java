package org.mbari.vcr.rs422;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbari.comm.BadPortException;
import org.mbari.util.NumberUtil;
import org.mbari.vcr.IVCRReply;
import org.mbari.vcr.VCRAdapter;
import org.mbari.vcr.VCRException;
import org.mbari.vcr.VCRUtil;

/**
 * <p>Control software for Sony VCR's. This class connects to the VCR using the
 * RS-422 (Sony 9-pin) protocol through a serial port (RS-232). Note that the
 * pin setting of RS-232 and RS-422 are <u>NOT</u> the same. Connecting to the
 * VCR will require a pin converter.</p>
 * 
 * <p>When a command is sent to the VCR this class will get the VCR's reply and
 * update the various status objects(VCRReply, VCRTimecode, VCRError, and VCRState)
 * as needed. Access to the status objects can be obtained by using an IVCRManager
 * object. Get the IVCRManager object by calling VCR.getVcrManager();</p>
 * 
 * <p><font color="ff3333">Requires the java COMM package.</font></p></p>
 * 
 * @author : $Author: hohonuuli $
 * @version : $Revision: 286 $
 */
public class VCR2 extends VCRAdapter implements SerialPortEventListener, Runnable {

    private static final Log log = LogFactory.getLog(VCR2.class);

    /** Stop command */
    public static final byte[] STOP = { 0x20, 0x00 };

    /**
     * Seek time code command. Last four bytes need to be modified to
     * specify the timecode to seek to. Refer to the VCR documentation for the format of the timecode bytes.
     */
    public static final byte[] SONY_SEEK_TIMECODE = { 0x24, 0x31, 0x00, 0x00, 0x00, 0x00 };

    /**
     * Shuttle reverse command. Last byte should be modified to provide the
     * shuttle speed (byte value between 0 (stopped) and 255 (very fast))
     */
    public static final byte[] SHUTTLE_REV = { 0x21, 0x23, 0x00 };

    /**
     * Shuttle forward command. Last byte should be modified to provide the
     * shuttle speed (byte value between 0 (stopped) and 255 (very fast))
     */
    public static final byte[] SHUTTLE_FWD = { 0x21, 0x13, 0x00 };

    /** Rewind command */
    public static final byte[] REWIND = { 0x20, 0x20 };

    /** Release tape command */
    public static final byte[] RELEASE_TAPE = { 0x20, 0x04 };

    /** Record command */
    public static final byte[] RECORD = { 0x20, 0x02 };

    /** get cue up status command */
    public static final byte[] PRESET_USERBITS = { 0x44, 0x05, 0x00, 0x00, 0x00, 0x00 };

    /** preset time code command */
    public static final byte[] PRESET_TIMECODE = { 0x44, 0x04, 0x00, 0x00, 0x00, 0x00 };

    /** Play command */
    public static final byte[] PLAY_FWD = { 0x20, 0x01 };

    /**
     * Pause - this was passed to me from Danelle Cline for the JVC VCR. Ntt
     * sure if it works with Sony's. Not yet sure how to resume.
     */
    public static final byte[] PAUSE = { 0x21, 0x13, 0x00 };

    /** <!-- Field description --> */
    public static final byte[] LOCAL_ENABLE = { 0x00, 0x1d };

    /** <!-- Field description --> */
    public static final byte[] LOCAL_DISABLE = { 0x00, 0x0c };

    /** Get vertical Userbits timecode command */
    public static final byte[] GET_VUBTIMECODE = { 0x61, 0x0c, 0x10 };

    /** Get vertical timecode command */
    public static final byte[] GET_VTIMECODE = { 0x61, 0x0c, 0x02 };

    /** Get Timer-2 timecode command */
    public static final byte[] GET_TIMECODE2 = { 0x61, 0x0c, 0x08 };

    /** Get Timer-1 timecode command */
    public static final byte[] GET_TIMECODE1 = { 0x61, 0x0c, 0x04 };

    /**
     * This is a special command supplied by sony that returns the best source
     * of the timecode (VITC at slwo speeds, LTC at high speeds)
     */
    public static final byte[] GET_TIMECODE = { 0x61, 0x0c, 0x03 };

    /** Get status command */
    public static final byte[] GET_STATUS = { 0x61, 0x20, 0x03 };

    /** Get Longitudinal Userbits timecode command */
    public static final byte[] GET_LUBTIMECODE = { 0x61, 0x0c, 0x0F };

    /** Get longitudinal timecode command */
    public static final byte[] GET_LTIMECODE = { 0x61, 0x0c, 0x01 };

    /** get cue up status command */
    public static final byte[] GET_CUEUP_STATUS = { 0x61, 0x20, 0x21 };

    /** Fast-forward command */
    public static final byte[] FAST_FWD = { 0x20, 0x10 };

    /** Eject command */
    public static final byte[] EJECT_TAPE = { 0x20, 0x0F };

    /** Request the type of VCR (i.e. Device) */
    public static final byte[] DEVICE_TYPE_REQUEST = { 0x00, 0x11 };

    /**
	 * @uml.property  name="inputStream"
	 */
    private InputStream inputStream;

    /**
	 * @uml.property  name="commPortIdentifier"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private CommPortIdentifier commPortIdentifier;

    /**
	 * For storage of the mostRecently submitted command to the VCR. When a reply is returned by the vcr this command is updated in the vcrReplyBuffer
	 * @uml.property  name="mostRecentCommand" multiplicity="(0 -1)" dimension="1"
	 */
    private byte[] mostRecentCommand;

    /**
	 * Need to hang on to the check sum. the VCRReply object needs this.
	 * @uml.property  name="outputStream"
	 */
    private OutputStream outputStream;

    /**
	 * @uml.property  name="serialPort"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SerialPort serialPort;

    /**
	 * @uml.property  name="vcrReply"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private VCRReply vcrReply = new VCRReply();

    /**
	 * @uml.property  name="readThread"
	 */
    private Thread readThread;

    /**
     * @param portName
     * @throws BadPortException
     * @throws IOException
     */
    public VCR2(String portName) throws BadPortException, IOException {
        initComm(portName);
        readThread = new Thread(this, "VCR Response Thread");
        readThread.start();
        requestLTimeCode();
        requestStatus();
    }

    public IVCRReply getVcrReply() {
        return vcrReply;
    }

    /**
     * Query the VCR for its type. Return codes are MODEL        Data-1      Data-2<br> BVH-2000(00) 00  11<br>
     * BVH-2000(02) 00  10<br>
     */
    public void deviceTypeRequest() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Device Type Request [" + NumberUtil.toHexString(DEVICE_TYPE_REQUEST) + "]");
        }
        sendCommand(DEVICE_TYPE_REQUEST);
    }

    /** Disconnect serial port communications */
    public void disconnect() {
        try {
            removeAllObservers();
            outputStream.close();
            inputStream.close();
            serialPort.close();
            getVcrTimecode().getTimecode().setTimecode(0, 0, 0, 0);
            ((VCRState) getVcrReply().getVcrState()).setStatus(0);
        } catch (Exception e) {
            if (log.isErrorEnabled() && serialPort != null) {
                log.error("Problem occured when closing serial port communications on " + serialPort.getName());
            }
        }
    }

    /** Eject a tape, updates status. */
    public void eject() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Eject [" + NumberUtil.toHexString(EJECT_TAPE) + "]");
        }
        sendCommand(EJECT_TAPE);
        requestStatus();
    }

    /** Fast forward the tape, updates status. */
    public void fastForward() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Fast Forward [" + NumberUtil.toHexString(FAST_FWD) + "]");
        }
        sendCommand(FAST_FWD);
        requestStatus();
    }

    /**
	 * @return  the commPortIdentifier
	 * @uml.property  name="commPortIdentifier"
	 */
    public CommPortIdentifier getCommPortIdentifier() {
        return commPortIdentifier;
    }

    /**
     * sets up communications with a Sony VTR (i.e VCR)
     *
     * @throws BadPortException
     */
    private void initComm(String serialPortName) throws BadPortException {
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            final CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(serialPortName)) {
                    commPortIdentifier = portId;
                    break;
                }
            }
        }
        if (commPortIdentifier != null) {
            try {
                serialPort = (SerialPort) commPortIdentifier.open(getClass().getName(), 2000);
            } catch (PortInUseException e1) {
                throw new BadPortException("The serial port, " + serialPortName + ", is already in use.");
            }
            try {
                inputStream = serialPort.getInputStream();
                outputStream = serialPort.getOutputStream();
            } catch (IOException e) {
                throw new BadPortException("Unable to establish I/O to " + serialPortName);
            }
            try {
                serialPort.addEventListener(this);
            } catch (TooManyListenersException e) {
                throw new BadPortException("Too many EventListners on " + serialPortName + ". This Exception should never occur!!");
            }
            serialPort.notifyOnDataAvailable(true);
            try {
                serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_ODD);
                serialPort.enableReceiveTimeout((int) 40);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
            } catch (UnsupportedCommOperationException e) {
                throw new BadPortException("The serial port, " + serialPortName + " does not apper to support 38400 8O1");
            }
        }
    }

    /** Play the tape, updates status. */
    public void play() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Play [" + NumberUtil.toHexString(PLAY_FWD) + "]");
        }
        sendCommand(PLAY_FWD);
        requestStatus();
    }

    /**
     * Set the timecode on the VCR.
     * @param timecode a byte array representing the time code value
     */
    public void presetTimecode(byte[] timecode) {
        byte[] command = PRESET_TIMECODE;
        for (int i = 0; i < timecode.length; i++) {
            command[2 + i] = timecode[i];
        }
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Preset Timecode [" + NumberUtil.toHexString(command) + "]");
        }
        sendCommand(command);
        requestStatus();
    }

    /**
     * Set the Userbits on the VCR
     *
     * @param userbits
     */
    public void presetUserbits(byte[] userbits) {
        byte[] command = PRESET_USERBITS;
        for (int i = 0; i < userbits.length; i++) {
            command[2 + i] = userbits[i];
        }
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Preset Userbits [" + NumberUtil.toHexString(command) + "]");
        }
        sendCommand(command);
        requestStatus();
    }

    /** Start recording, updates status. */
    public void record() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Record [" + NumberUtil.toHexString(RECORD) + "]");
        }
        sendCommand(RECORD);
        requestStatus();
    }

    /** Release the tape, updates status. */
    public void releaseTape() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Release Tape [" + NumberUtil.toHexString(RELEASE_TAPE) + "]");
        }
        sendCommand(RELEASE_TAPE);
        requestStatus();
    }

    /**
     * Get longitudial timecode, which is stored on the audio track. Not as
     * accurate as getVTimeCode, but this method will get timecodes during fast-forwards, rewinds and shuttiling
     */
    public void requestLTimeCode() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Request Longitudinal Timecode [" + NumberUtil.toHexString(GET_LTIMECODE) + "]");
        }
        sendCommand(GET_LTIMECODE);
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    public void requestLUserbits() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Request Longitudinal Userbits [" + NumberUtil.toHexString(GET_LUBTIMECODE) + "]");
        }
        sendCommand(GET_LUBTIMECODE);
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    public void requestLocalDisable() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Request Local Disable [" + NumberUtil.toHexString(LOCAL_DISABLE) + "]");
        }
        sendCommand(LOCAL_DISABLE);
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    public void requestLocalEnable() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Request Local Enable [" + NumberUtil.toHexString(LOCAL_ENABLE) + "]");
        }
        sendCommand(LOCAL_ENABLE);
    }

    /** Send a "get status" command to the VCR */
    public void requestStatus() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Request Status [" + NumberUtil.toHexString(GET_STATUS) + "]");
        }
        sendCommand(GET_STATUS);
    }

    /**
     * When requesting a timecode, the results depend on the tape speed because
     * at very low speeds (> 0.25 play speed) it may not be possible to recover
     * the timecode. However, if VITC is present it may be used instead. To
     * automate the decision process, Sony has provided a special command which
     * will return the best source of timecode. Note that when LTC and VITC are
     * both garbage you get back the 74.14 corrected LTC data. In this case, the
     * time is actually the last good LTC time corrected by the tape timer.
     *
     */
    public void requestTimeCode() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Request Timecode [" + NumberUtil.toHexString(GET_TIMECODE) + "]");
        }
        sendCommand(GET_TIMECODE);
    }

    /**
     * Tries to request the best userbits. This method has not been tested.
     *
     */
    public void requestUserbits() {
        if (getVcrState().isPlaying()) {
            requestVUserbits();
        } else {
            requestLUserbits();
        }
    }

    /**
     * Get vertical timecode, which is stored between video frames. These
     * timecodes can not be accessed in any mode accept play mode. So use
     * getLTimeCode when fast-forwarding, rewinding, or shuttling
     */
    public void requestVTimeCode() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Request Vertical Timecode [" + NumberUtil.toHexString(GET_VTIMECODE) + "]");
        }
        sendCommand(GET_VTIMECODE);
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    public void requestVUserbits() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Request Vertical Userbits [" + NumberUtil.toHexString(GET_VUBTIMECODE) + "]");
        }
        sendCommand(GET_VUBTIMECODE);
    }

    /** Resumes the last command that was paused. This is not yet implemented */
    public void resume() {
    }

    /** Rewind the tape, updates status. */
    public void rewind() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Rewind [" + NumberUtil.toHexString(REWIND) + "]");
        }
        sendCommand(REWIND);
        requestStatus();
    }

    /**
     * Fast forward to the specified timecode, updates status.
     * @param timecode a byte array representing the time code value
     * @see VCRUtil
     */
    public void seekTimecode(byte[] timecode) {
        byte[] command = SONY_SEEK_TIMECODE;
        for (int i = 0; i < timecode.length; i++) {
            command[2 + i] = timecode[i];
        }
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Seek Timecode [" + NumberUtil.toHexString(command) + "]");
        }
        sendCommand(command);
        requestStatus();
    }

    /**
     * Fast forward to the specified timecode, updates status.
     * @param timecode
     */
    public void seekTimecode(int timecode) {
        byte[] command = SONY_SEEK_TIMECODE;
        byte[] byteTimecode = NumberUtil.toByteArray(timecode);
        for (int i = 0; i < byteTimecode.length; i++) {
            command[2 + i] = byteTimecode[i];
        }
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Seek Timecode [" + NumberUtil.toHexString(command) + "]");
        }
        sendCommand(command);
        requestStatus();
    }

    /**
     * Sends a command, in the format of a byte[], to the VCR. This class will
     * re-attempt to send the command up to 5 times if a recoverable error occurs.
     * @param command The command to send to the VCR
     */
    protected synchronized void sendCommand(byte[] command) {
        if (commPortIdentifier == null || serialPort == null) {
            return;
        }
        try {
            Thread.sleep(33);
        } catch (InterruptedException e) {
            log.error("The thread, " + Thread.currentThread().getName() + ", was interrrupted", e);
            Thread.currentThread().interrupt();
        }
        this.mostRecentCommand = command;
        byte checksum = VCRReply.calculateChecksum(command);
        try {
            final byte[] c = new byte[command.length + 1];
            System.arraycopy(command, 0, c, 0, command.length);
            c[c.length - 1] = checksum;
            if (log.isDebugEnabled()) {
                log.debug("Sending bytes '" + NumberUtil.toHexString(c) + "' to " + commPortIdentifier.getName());
            }
            outputStream.write(c);
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to send a command to the VCR", e);
            }
        }
    }

    /**
     * shuttle foward using the giving speed, updates status.
     * @param speed value between 0 (slow) and 255 (fast)
     */
    public void shuttleForward(int speed) {
        byte[] command = SHUTTLE_FWD;
        byte[] byteSpeed = NumberUtil.toByteArray(speed);
        command[2] = byteSpeed[3];
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Shuttle Forward [" + NumberUtil.toHexString(command) + "]");
        }
        sendCommand(command);
        requestStatus();
    }

    /**
     * shuttle backwards using the giving speed, updates status.
     * @param speed value between 0 (slow) and 255 (fast)
     */
    public void shuttleReverse(int speed) {
        byte[] command = SHUTTLE_REV;
        byte[] byteSpeed = NumberUtil.toByteArray(speed);
        command[2] = byteSpeed[3];
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Shuttle Reverse [" + NumberUtil.toHexString(command) + "]");
        }
        sendCommand(command);
        requestStatus();
    }

    /** Stop the tape, updates status. */
    public void stop() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> Stop [" + NumberUtil.toHexString(STOP) + "]");
        }
        sendCommand(STOP);
        requestStatus();
    }

    /**
     * Returns a name for this connection
     */
    public String getConnectionName() {
        return (commPortIdentifier == null) ? "Not Connected" : commPortIdentifier.getName();
    }

    private synchronized void receive() throws IOException, InterruptedException, VCRException {
        final byte[] cmd = new byte[2];
        if (inputStream.available() > 0) {
            inputStream.read(cmd);
        }
        Thread.sleep(33);
        final int numDataBytes = (int) (cmd[0] & 0x0F);
        byte[] data = null;
        if (numDataBytes > 0) {
            data = new byte[numDataBytes];
            if (inputStream.available() > 0) {
                inputStream.read(data);
            } else {
                throw new IOException("Incoming data is missing . byte[] = " + NumberUtil.toHexString(cmd));
            }
        }
        Thread.sleep(33);
        final byte[] checksum = new byte[1];
        if (inputStream.available() > 0) {
            inputStream.read(checksum);
        } else {
            throw new IOException("Incoming checksum is missing. cmd[] =  " + NumberUtil.toHexString(cmd) + " data[] = " + NumberUtil.toHexString(data));
        }
        vcrReply.update(mostRecentCommand, cmd, data, checksum);
        if (log.isDebugEnabled()) {
            int dataLength = data == null ? 0 : data.length;
            final byte[] c = new byte[cmd.length + dataLength + 1];
            System.arraycopy(cmd, 0, c, 0, cmd.length);
            if (data != null) {
                System.arraycopy(data, 0, c, cmd.length, data.length);
            }
            c[c.length - 1] = checksum[0];
            log.debug("<< " + NumberUtil.toHexString(c));
        }
    }

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                try {
                    while (inputStream.available() > 0) {
                        receive();
                    }
                } catch (IOException e) {
                    log.error("Problem occured while reading from " + commPortIdentifier.getName(), e);
                } catch (InterruptedException e) {
                    log.error("The thread, " + Thread.currentThread().getName() + ", was interrrupted", e);
                    Thread.currentThread().interrupt();
                } catch (VCRException e) {
                    log.error("An error occured while handling the response from " + commPortIdentifier.getName(), e);
                }
                break;
        }
    }

    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            log.error("The thread, " + Thread.currentThread().getName() + ", was interrrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
