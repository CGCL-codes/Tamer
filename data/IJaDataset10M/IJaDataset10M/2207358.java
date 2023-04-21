package ch.unizh.ini.jaer.projects.opticalflow.usbinterface;

import ch.unizh.ini.jaer.projects.opticalflow.Chip2DMotion;
import ch.unizh.ini.jaer.projects.opticalflow.MotionData;
import ch.unizh.ini.jaer.projects.opticalflow.mdc2d.MDC2D;
import ch.unizh.ini.jaer.projects.opticalflow.mdc2d.MotionDataMDC2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import net.sf.jaer.biasgen.Biasgen;
import net.sf.jaer.biasgen.IPot;
import net.sf.jaer.biasgen.VDAC.VPot;
import net.sf.jaer.hardwareinterface.HardwareInterfaceException;
import ch.unizh.ini.jaer.projects.dspic.serial.*;
import ch.unizh.ini.jaer.projects.opticalflow.mdc2d.GlobalOpticalFlowAnalyser;
import gnu.io.PortInUseException;
import java.util.Calendar;
import java.util.prefs.Preferences;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Communicates with a <code>dsPIC33F</code> over a serial interface.
 * <br /><br />
 *
 * This hardware interface can be used in conjunction with the <code>MDC2Dv2</code>
 * test-board that features a <code>MDC2D</code> chip that is controlled by a
 * <code>dsPIC33FJ128MC804</code> and communicates with the computer via a <code>FTDI FT232RL</code>
 * (UART over USB) interface
 * <br /><br />
 * 
 * this class depends on <code>ch.unizh.ini.jaer.projects.dspic.serial.StreamCommand</code>
 * that should be distributed with the jAER project
 * <br /><br />
 * 
 * use the bias file <code>MDC2D_dsPIC.xml</code> with this platform (can be found in
 * <code>jAER/biasgenSettings</code> subdirectory of the jAER subversion trunk)
 * <br /><br />
 * 
 * in case something is not working, please try to debug the hardware with
 * the class <code>ch.unizh.ini.projects.jaer.dspic.serial.StreamCommandTest</code>,
 * read the javadoc of the class <code>ch.unizh.ini.projects.jaer.dspic.serial.StreamCommand</code>
 * and read the documentation (particularly the "usage" section in the appendix) of 
 * the semester project for which this interface was written; the report for
 * that project can be downloaded at 
 * <a href="http://people.ee.ethz.ch/~andstein/mdc2d/">http://people.ee.ethz.ch/~andstein/mdc2d/</a> 
 * <br /><br />
 *
 * @see ch.unizh.ini.jaer.projects.dspic.serial.StreamCommand
 * @author andstein
 */
public class dsPIC33F_COM_OpticalFlowHardwareInterface implements MotionChipInterface, StreamCommandListener {

    static final Logger log = Logger.getLogger(dsPIC33F_COM_OpticalFlowHardwareInterface.class.getName());

    static Preferences prefs = Preferences.userNodeForPackage(dsPIC33F_COM_OpticalFlowHardwareInterface.class);

    /** set this boolean to true to enable some more diagnostic messages to be
        shown in the log; may be turned of to avoid too many noisy messages */
    public boolean debugging = prefs.getBoolean("debugging", true);

    private Chip2DMotion chip;

    public static final int PROTOCOL_MAJOR = 6;

    public static final int PROTOCOL_MINOR = 3;

    public static final int MESSAGE_RESET = 0x0000;

    public static final int MESSAGE_BYTES = 0x0001;

    public static final int MESSAGE_WORDS = 0x0003;

    public static final int MESSAGE_WORDS_DXDY = 0x0004;

    public static final int MESSAGE_DXDY = 0x0005;

    public static final int DSPIC_FCY = 39613750;

    public static final int DSPIC_BRGVAL = 3;

    public static final float DSPIC_DAC_SCALEFACTOR = 1.25f;

    static final int MAX_POTS = 16;

    private static final float VDD_SHOULD_BE = 3.33f;

    private static final float VDD_IS = 3.28f;

    protected String portName = prefs.get("port", "");

    private int delay1_us = prefs.getInt("delay1", 20000);

    private int delay2_us = prefs.getInt("delay2", 20000);

    protected int[] ipotValues = null;

    protected int[] vpotValues = null;

    protected int channel = prefs.getInt("channel", MotionDataMDC2D.LMC1);

    protected boolean onChipADC = prefs.getBoolean("onChipADC", false);

    protected boolean onChipBiases = prefs.getBoolean("onChipBiases", true);

    private boolean triedOpening = false;

    private boolean triedReset = false;

    private boolean verified = false;

    private boolean hardwareError = false;

    private StreamCommand serial;

    private dsPIC33F_COM_ConfigurationPanel configPanel;

    private DataConverter converter;

    private Thread converterThread;

    private MotionData currentBuffer;

    private int sequenceNumber;

    boolean doRemoveFPN = false;

    protected GlobalOpticalFlowAnalyser analyser = null;

    protected boolean analysing = false;

    /**
     * creates a new hardware interface instance; will <i>not yet connect</i> to
     * the device
     * @see #open
     */
    public dsPIC33F_COM_OpticalFlowHardwareInterface() {
        chip = new MDC2D();
        serial = new StreamCommand(this, DSPIC_FCY / (16 * (DSPIC_BRGVAL + 1)));
        configPanel = new dsPIC33F_COM_ConfigurationPanel(this);
        configPanel.setStatus("not connected");
        currentBuffer = chip.getEmptyMotionData();
        sequenceNumber = 0;
        converter = new DataConverter(new Exchanger<MotionData>(), chip.getEmptyMotionData());
        converterThread = null;
    }

    /**
     * sends a command to the device if it is connected and verified
     * @param cmd command to send
     */
    public void sendCommand(String cmd) {
        if (serial.isConnected()) if (cmd.equals("version") || cmd.equals("reset") || verified) {
            serial.sendCommand(cmd);
            if (debugging) System.err.format("%s : (%s)>\n", exactTimeString(), cmd);
        } else log.info("cannot send command " + cmd + " : not verified");
    }

    /**
     * is called upon completed opening and synchronizes device configuration
     * with class variables; also resets configPanel according to these values
     */
    public void initializeDevice() {
        if (!isOpen()) return;
        sendCommand("onchip " + (onChipADC ? "1" : "0"));
        sendCommand("pd " + (onChipBiases ? "0" : "1"));
        sendDelayCommands();
        sendCommand("set timer_cycles 0064");
        if (channel == MotionDataMDC2D.PHOTO) sendCommand("channel recep");
        if (channel == MotionDataMDC2D.LMC1) sendCommand("channel lmc1");
        if (channel == MotionDataMDC2D.LMC2) sendCommand("channel lmc2");
        ipotValues = null;
        vpotValues = null;
        try {
            sendConfiguration(chip.getBiasgen());
        } catch (HardwareInterfaceException ex) {
            log.warning("could not send configuration : " + ex);
        }
        if (isStreaming()) {
            stopStreaming();
            startStreaming();
        }
    }

    @Override
    public int getRawDataIndex(int bit) {
        if (bit != channel) log.warning("channel not currently selected");
        return 0;
    }

    @Override
    public void commandTimeout(String cmd) {
        if (cmd.equals("reset")) return;
        log.warning(exactTimeString() + " command time-out (" + cmd + ")");
        verified = false;
        if (cmd.equals("version")) updateStatus("no device attached");
    }

    @Override
    public void setPowerDown(boolean powerDown) throws HardwareInterfaceException {
        onChipBiases = !powerDown;
        if (!isOpen()) return;
        sendCommand("pd " + (onChipBiases ? "0" : "1"));
        log.info("set pd=" + (onChipBiases ? "0" : "1"));
    }

    /**
     * whether on-chip biases are used
     * 
     * @return <code>true</code> when <code>powerDown==0</code> (i.e. when
     *      the on-chip bias generator is <i>not powered down</i>)
     * @throws HardwareInterfaceException 
     */
    public boolean isOnChipBiases() {
        return onChipBiases;
    }

    @Override
    public void setChannel(int bit, boolean onChip) throws HardwareInterfaceException {
        channel = bit;
        onChipADC = onChip;
        prefs.putInt("channel", bit);
        prefs.putBoolean("onChipADC", onChip);
        if (isOpen()) {
            sendCommand("onchip " + (onChipADC ? "1" : "0"));
            if (channel == MotionDataMDC2D.PHOTO) sendCommand("channel recep");
            if (channel == MotionDataMDC2D.LMC1) sendCommand("channel lmc1");
            if (channel == MotionDataMDC2D.LMC2) sendCommand("channel lmc2");
        }
    }

    /**
     * get currently transmitted channel (only one channel transmitted at a time)
     * @return index {@link MotionDataMDC2D}<code>.PHOTO/LMC1/LMC2</code>
     * @throws HardwareInterfaceException 
     */
    public int getChannel() {
        return channel;
    }

    /**
     * whether on-chip ADC is used (rather than the <code>dsPIC</code>'s own ADC)
     * @return 
     */
    public boolean isOnChipADC() {
        return onChipADC;
    }

    /**
     * sets the delay between frames according to this instances variables
     */
    protected void sendDelayCommands() {
        sendCommand("set main_us1 " + String.format("%04X", delay1_us) + " main_us2 " + String.format("%04X", delay2_us));
    }

    /**
     * call this to set the <code>dt</code> between two adjacent frames.
     * <ul>
     * <li>If the delay specified is longer than the time it takes to send
     *     a frame via the serial line, then the acquired frames will be
     *     taken at exactly the rate specified; this delay will be much more
     *     precise than the difference of the <code>getTimeCapturedMs()</code>
     *     because there is a variable delay in the emptying of the serial
     *     buffers...</li>
     * <li>In case the delay specified is shorter than the time necessary to
     *     transmit a frame, then the UART will dictate the delay between
     *     adjacent frame acquisitions</li>
     * </ul>
     * 
     * @param delayMs delay between frames ("<code>dt</code>") in milliseconds
     * @see ch.unizh.ini.jaer.projects.opticalflow.MotionData#getTimeCapturedMs
     */
    public void setDelayMs(int delayMs) {
        delay1_us = 1000 * delayMs;
        delay2_us = 1000 * delayMs;
        prefs.putInt("delay1", delay1_us);
        prefs.putInt("delay2", delay2_us);
        sendDelayCommands();
    }

    public int getDelay1Ms() {
        return delay1_us / 1000;
    }

    public int getDelay2Ms() {
        return delay2_us / 1000;
    }

    /**
     * all data gets piped through this class; coming in as <code>StreamCommandMessage</code>
     * on one side and getting out as <code>MotionData</code> on the other side
     * <br /><br />
     * 
     * the MotionData is <i>exchanged</i> in a blocking way so that the data converter
     * stops until its last unpacked frame is actually used by the motion viewer
     * and the motion viewer blocks in the exchange until the data converter
     * got a new message that could be converted into a frame
     * <br /><br />
     * 
     * this exchanging scheme ensures that if all the returned <code>MotionData</code>
     * are put into a queue, the last two of them are always references to
     * different objects (with different internal buffers) and can thereby
     * be used for motion calculation
     * <br /><br />
     * 
     * @see ch.unizh.ini.jaer.projects.dspic.serial.StreamCommandMessage
     * @see ch.unizh.ini.jaer.projects.opticalflow.MotionData
     */
    protected class DataConverter implements Runnable {

        private Exchanger<MotionData> dataOut;

        private LinkedList<StreamCommandMessage> messages;

        private MotionData frame;

        private boolean stopped;

        private int width;

        private int messagesPushed, messagesParsed;

        /** timeout for getting data from device -- <code>MotionViewer</code> calls close() when this occurs ! */
        private static final long DATA_TIMEOUT_MS = 20000;

        private static final long DATA_EXCHANGE_MS = 5;

        /** messages can be stored in a queue; normally, the <code>MotionViewer</code> is slower
         * than the message stream and therefore messages are lost; use the <code>ConfigurationPanel</code>
         * to set a different timeout that results in messages being sent more
         * slowly if it is important to get every message (e.g. for comparing
         * motion calculations on firmware/computer side)
         * @see dsPIC33F_COM_ConfigurationPanel
         */
        public static final int MAX_QUEUED_MESSAGES = 2;

        /**
         * creates a new instance of this class; the thread must later be
         * started via a call to <code>start()</code> of a <code>Thread</code>
         * instance based on this runnable
         * 
         * @param dst an <code>Exchanger</code> that will be used to swap the
         *      <code>MotionData</code> with the <code>MotionViewer</code>
         *      thread
         * @param motionBuffer an object that will be swapped with another
         *      MotionData in the .exchange() (and must therefore not be the
         *      same object that is used for the .exchange() call !)
         * @see java.lang.Thread
         */
        public DataConverter(Exchanger<MotionData> dst, MotionData motionBuffer) {
            dataOut = dst;
            messages = new LinkedList<StreamCommandMessage>();
            frame = motionBuffer;
            width = frame.chip.getSizeX();
            messagesPushed = messagesParsed = 0;
            stopped = false;
        }

        /**
         * call this method when the <code>MotionData</code> changes (could be the case
         * if that communicates with this hardware interface is changed...)
         * 
         * @param newFrame a new motion data to be used for swapping with 
         *      <code>MotionViewer</code> thread
         */
        public void setMotionData(MotionData newFrame) {
            frame = newFrame;
            width = frame.chip.getSizeX();
        }

        /**
         * indicates the thread to stop; <code>interrupt()</code> it after setting this flag
         * @see java.lang.Thread#interrupt
         */
        public void stop() {
            stopped = true;
        }

        /**
         * converts an unsigned 10bit value as returned by the microcontroller's
         * ADC to a float as used inside jAER
         */
        protected float convertPixelValue(int i) {
            return ((float) i) / 1024.f;
        }

        /**
         * converts a 16bit encoded motion value as calculated by the firmware
         * into a float in units of pixels/dt
         */
        protected float convertMotionValue(int i) {
            if ((i & 0x8000) == 0) {
                return ((float) i) / 32768f;
            } else {
                i ^= 0xFFFF;
                i += 1;
                return -((float) i) / 32768f;
            }
        }

        /**
         * extracts the pixel value (as digitized by the microcontroller's
         * ADC) at a specific position form a message, depending on the messages
         * <code>type</code> field.
         * 
         * @see #convertPixelValue
         */
        protected int getFramePixel(StreamCommandMessage message, int x, int y) {
            if (message.getType() == dsPIC33F_COM_OpticalFlowHardwareInterface.MESSAGE_WORDS || message.getType() == dsPIC33F_COM_OpticalFlowHardwareInterface.MESSAGE_WORDS_DXDY) return message.getUnsignedWordAt(y * width + x); else return 1023 * ((y + x) % 2);
        }

        /**
         * @return true if this message contains a global motion vector
         */
        protected boolean hasGlobalMotion(StreamCommandMessage msg) {
            return msg.getType() == MESSAGE_DXDY || msg.getType() == MESSAGE_WORDS_DXDY;
        }

        /**
         * @return true if this message contains a global motion vector
         */
        protected boolean hasPixelData(StreamCommandMessage msg) {
            switch(msg.getType()) {
                case MESSAGE_WORDS:
                case MESSAGE_WORDS_DXDY:
                case MESSAGE_BYTES:
                    return true;
            }
            return false;
        }

        /**
         * @return true if this message should contain a global motion vector
         *      but some error occurred while calculating the motion vector value
         */
        protected boolean globalMotionError(StreamCommandMessage msg) {
            if (!hasGlobalMotion(msg)) return false;
            if (msg.getType() == MESSAGE_DXDY) return msg.getUnsignedWordAt(0) == 0xFFFF; else return msg.getUnsignedWordAt(400) == 0xFFFF;
        }

        /**
         * @return the error-code; 0x01 and 0x02 indicate <code>dx</code> respectively
         *      <code>dy</code> overflow (see firmware source code for signification 
         *      of the other errors that are much rarer)
         */
        protected int globalMotionErrorCode(StreamCommandMessage msg) {
            if (msg.getType() == MESSAGE_DXDY) return msg.getUnsignedWordAt(1); else return msg.getUnsignedWordAt(401);
        }

        /**
         * @return the x component of the global motion vector contained in
         *      the specified message (in units of pixels/dt)
         */
        protected float globalMotionX(StreamCommandMessage msg) {
            if (msg.getType() == MESSAGE_DXDY) return msg.getSignedFloatAt(0) * 2; else return msg.getSignedFloatAt(400) * 2;
        }

        /**
         * @return the y component of the global motion vector contained in
         *      the specified message (in units of pixels/dt)
         */
        protected float globalMotionY(StreamCommandMessage msg) {
            if (msg.getType() == MESSAGE_DXDY) return msg.getSignedFloatAt(1) * 2; else return msg.getSignedFloatAt(401) * 2;
        }

        /**
         * @return sequence number of the frame contained in the message;
         *      -1 if the message contains to sequence number
         */
        protected int getSequenceNumber(StreamCommandMessage msg) {
            if (msg.getType() == MESSAGE_WORDS_DXDY) return msg.getUnsignedWordAt(402);
            if (msg.getType() == MESSAGE_DXDY) return msg.getUnsignedWordAt(2);
            return -1;
        }

        /**
         * blocks execution until a new message is pushed
         * 
         * @throws InterruptedException 
         */
        protected synchronized void waitForNextMessage() throws InterruptedException {
            while (messages.size() == 0) wait();
        }

        /**
         * this method should be implemented reasonably fast in order not to
         * loose any more message that are already lost by the relatively
         * infrequent polling by the <code>MotionViewer</code>
         * 
         * @return true if frame could be unpacked, false if an error occurred
         */
        protected boolean unpackFrame() {
            try {
                StreamCommandMessage message = messages.poll();
                frame.setSequenceNumber(sequenceNumber++);
                if (getSequenceNumber(message) > 0) frame.setSequenceNumber(getSequenceNumber(message));
                frame.setTimeCapturedMs(System.currentTimeMillis());
                if (hasPixelData(message)) {
                    frame.setContents(MotionData.PHOTO | MotionDataMDC2D.LMC1 | MotionDataMDC2D.LMC2 | MotionDataMDC2D.ON_CHIP_ADC);
                    float[][][] rawData = frame.getRawDataPixel();
                    for (int y = 0; y < chip.getSizeY(); y++) for (int x = 0; x < chip.getSizeX(); x++) {
                        int pixelValue = getFramePixel(message, x, y);
                        if (pixelValue > 1024) return false;
                        rawData[0][y][x] = convertPixelValue(pixelValue);
                    }
                    for (int channel = 1; channel < rawData.length; channel++) for (int y = 0; y < chip.getSizeY(); y++) System.arraycopy(rawData[0][y], 0, rawData[channel][y], 0, chip.getSizeX());
                }
                if (frame.getPastMotionData() != null && frame.getPastMotionData().length > 0) {
                    long dt = frame.getTimeCapturedMs() - frame.getPastMotionData()[0].getTimeCapturedMs();
                    if (message.getType() != MESSAGE_DXDY) frame.collectMotionInfo();
                    if (hasGlobalMotion(message)) {
                        if (globalMotionError(message)) {
                            log.warning("firmware could not calculate Srinivasan : code=0x" + Integer.toHexString(globalMotionErrorCode(message)));
                            if (isAnalysing()) analyser.addErroneousCalculations(globalMotionErrorCode(message), frame);
                            frame.setGlobalX2(0);
                            frame.setGlobalY2(0);
                        } else {
                            float dx = globalMotionX(message);
                            float dy = globalMotionY(message);
                            frame.setGlobalX2(dx / dt * MotionDataMDC2D.globalScaleFactor);
                            frame.setGlobalY2(dy / dt * MotionDataMDC2D.globalScaleFactor);
                        }
                        if (isAnalysing()) analyser.analyseMotionData(frame);
                        if (hasGlobalMotion(message)) chip.integrator.addFrame(frame);
                    } else {
                        frame.setGlobalX2(0);
                        frame.setGlobalY2(0);
                    }
                }
            } catch (NoSuchElementException ex) {
                log.warning("messages.poll() : " + ex.getMessage());
                messages.clear();
                return false;
            } catch (NullPointerException ex) {
                log.warning("caught null pointer exception");
                ex.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        public void run() {
            if (debugging) System.err.println("converter started");
            while (!stopped) {
                try {
                    waitForNextMessage();
                    if (unpackFrame()) {
                        messagesParsed++;
                        MotionData last = frame.clone();
                        frame = dataOut.exchange(frame);
                        frame.setLastMotionData(last);
                    } else {
                        log.warning("discarded bogus frame");
                        if (isAnalysing()) analyser.addBogusFrame(frame);
                    }
                } catch (InterruptedException e) {
                }
            }
            if (debugging) System.err.println("converter stopped");
            stopped = false;
        }

        /**
         * push a new message to be converted; returns immediately
         * 
         * @param msg the next message to convert (will be copied)
         */
        public synchronized void pushMessage(StreamCommandMessage msg) {
            if (messages.size() > MAX_QUEUED_MESSAGES) {
                if (isAnalysing()) analyser.addLostMessage(getSequenceNumber(msg));
                return;
            }
            StreamCommandMessage message = new StreamCommandMessage();
            message.copy(msg);
            messages.add(message);
            messagesPushed++;
            notify();
        }

        /**
         * use this method to exchange a <code>MotionData</code> object from the motion
         * viewer loop
         * <br /><br />
         * 
         * it will block until new data arrived and is unpacked into the
         * provided <code>MotionData</code> object
         * 
         * @param data to exchange, must be a different object from the one
         *      exchanged last time
         * @return the object that was exchanged in the last call to this
         *      method
         * @throws InterruptedException
         * @throws TimeoutException indicates that the streaming has stopped
         */
        public MotionData exchangeMotionData(MotionData data) throws InterruptedException, TimeoutException {
            return dataOut.exchange(data, DATA_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * starts a recording using the class <code>GlobalOpticalFlowAnalyser</code>
     * 
     * @param name of the directory where to save the 
     * @param saveRate 2 out of <code>saveRate</code> frames will be stored with
     *      all their pixel values
     * @see GlobalOpticalFlowAnalyser
     */
    public void startAnalysis(String name, int saveRate) {
        analyser = new GlobalOpticalFlowAnalyser(name, saveRate);
        analysing = true;
    }

    /**
     * @return if <code>startAnalysis()</code> has already been called but
     *      not yet <code>stopAnalysis()</code>
     * @see #stopAnalysis
     * @see #startAnalysis
     */
    public boolean isAnalysing() {
        return analysing;
    }

    /**
     * stops a recording previously started via <code>startAnalysis()</code>
     * and writes data to disk -- depending on the amount of data recorded,
     * it might take some time before this method returns
     * 
     * @see #startAnalysis
     */
    public void stopAnalysis() {
        analyser.finish();
        analyser = null;
        analysing = false;
    }

    /**
     * <b>NOT WORKING YET</b>
     */
    public void setRemoveFPN(boolean removeFPN) {
        doRemoveFPN = removeFPN;
    }

    /**
     * tells device to use current frame as a reference image to remove fixed
     * pattern noise
     */
    public void setFPN() {
        sendCommand("FPN zero");
        sendCommand("FPN set");
    }

    @Override
    public MotionData getData() throws TimeoutException {
        try {
            currentBuffer = converter.exchangeMotionData(currentBuffer);
            return currentBuffer;
        } catch (InterruptedException e) {
            return null;
        } catch (java.util.concurrent.TimeoutException to) {
            throw new TimeoutException("timeout when exchanging MotionBuffer with ConverterThread");
        }
    }

    /**
     * list the system's serial ports to which the device might be connected
     * 
     * @return a system dependent list of names of serial ports available
     */
    public String[] getAvailablePortNames() {
        return serial.getPortNames();
    }

    /**
     * sets the serial communication port to use
     * <br /><br />
     *
     * because the different serial ports look the same to the hardware
     * interface (since it does not use a OS specific driver), the port to
     * which the device is attached needs be set manually; for this purpose,
     * the <code>dsPIC33F_COM_ConfigurationPanel</code> can be used
     * <br /><br />
     * 
     * if the port is changed and the hardware interface is currently connected
     * to a serial port, this connection is closed. a connection will be re-established
     * on the new port; this method should therefore not be called from within
     * a serial i/o thread.
     *
     * @param portName OS specific (<code>null</code> means not connected)
     * @see dsPIC33F_COM_ConfigurationPanel
     * @see #open
     */
    public void setPortName(String portName) throws HardwareInterfaceException {
        if (portName == null) {
            if (this.portName == null) return;
        } else {
            if (portName.equals(this.portName)) return;
        }
        prefs.put("port", portName == null ? "" : portName);
        updateStatus("");
        if (serial.isConnected()) close();
        this.portName = portName;
        this.triedOpening = false;
        this.triedReset = false;
        this.hardwareError = false;
        log.info("portName set to " + portName);
        if (portName != null) open();
    }

    public String getPortName() {
        return portName;
    }

    /**
     * this method is called by the Biasgen whenever something in the observables
     * (mainly IPots and VPots) has changed -- this class keeps track of
     * values that were already set and only transmits the delta of the
     * configuration
     * 
     * @param biasgen
     * @throws HardwareInterfaceException 
     * @see net.sf.jaer.biasgen.Biasgen
     */
    @Override
    public void sendConfiguration(Biasgen biasgen) throws HardwareInterfaceException {
        if (vpotValues == null) {
            vpotValues = new int[MAX_POTS];
            for (int i = 0; i < vpotValues.length; i++) {
                vpotValues[i] = -1;
            }
        }
        if (!onChipBiases) for (int i = 0; i < biasgen.getNumPots(); i++) {
            VPot vpot = (VPot) biasgen.getPotArray().getPotByNumber(i);
            int chan = vpot.getChannel();
            int mv = (int) (1000 * vpot.getVoltage());
            if (mv > 2500) mv += (int) (1000 * (VDD_IS - VDD_SHOULD_BE));
            if (vpotValues[chan] != mv) {
                vpotValues[chan] = mv;
                if (debugging) System.err.println("setting DAC channel " + i + " (" + vpot.getName() + ") to " + mv + " mV");
                sendCommand("DAC " + Integer.toHexString(i) + " " + Integer.toHexString((int) (mv * DSPIC_DAC_SCALEFACTOR)));
            }
        }
        if (ipotValues == null) {
            ipotValues = new int[12];
            for (int i = 0; i < ipotValues.length; i++) {
                ipotValues[i] = -1;
            }
        }
        Iterator<IPot> ipotsIt = (Iterator<IPot>) ((MDC2D.MDC2DBiasgen) biasgen).getShiftRegisterIterator();
        boolean allEqual = true;
        byte[] allbin = new byte[36];
        for (int i = 0; ipotsIt.hasNext(); i++) {
            IPot ipot = ipotsIt.next();
            int sr = ipot.getShiftRegisterNumber();
            if (ipotValues[sr] != ipot.getBitValue()) {
                ipotValues[sr] = ipot.getBitValue();
                allEqual = false;
            }
            byte[] bin = ipot.getBinaryRepresentation();
            allbin[sr * 3 + 0] = bin[0];
            allbin[sr * 3 + 1] = bin[1];
            allbin[sr * 3 + 2] = bin[2];
        }
        if (!allEqual && onChipBiases) {
            StringBuilder allValues = new StringBuilder();
            for (int i = 0; i < allbin.length; i++) allValues.append(String.format("%02X", allbin[i]));
            sendCommand("biases " + allValues.toString());
        }
    }

    @Override
    public void flashConfiguration(Biasgen biasgen) throws HardwareInterfaceException {
        log.warning("cannot flash configuration on this hardware");
    }

    @Override
    public byte[] formatConfigurationBytes(Biasgen biasgen) {
        return null;
    }

    @Override
    public String getTypeName() {
        return "dsPIC33F_COMx";
    }

    /**
     * call this to issue before closing the device; wait for commands to
     * finish before closing using <code>waitForCommands</code>
     * @see #close
     * @see ch.unizh.ini.jaer.projects.dspic.serial.StreamCommand#waitForCommands
     */
    protected void shutdownDevice() {
        sendCommand("stop");
    }

    /**
     * closes the serial port; <b>do not call from within serial i/o thread</b>
     */
    @Override
    public void close() {
        if (isOpen()) {
            shutdownDevice();
            try {
                serial.waitForCommands();
            } catch (InterruptedException ex) {
            }
        }
        if (!serial.isConnected()) {
            log.info("close : no connection !");
            return;
        }
        serial.close();
        updateStatus("not connected");
        log.info("interface closed");
    }

    /**
     * sends a reset command to the device if it is already opened; else,
     * tries to open the device and eventually sends a reset command once
     * its opened
     * 
     * @throws HardwareInterfaceException 
     * @see #open
     */
    public void doReset() throws HardwareInterfaceException {
        if (isOpen()) {
            sendCommand("reset");
        } else {
            triedReset = false;
            open();
        }
    }

    /**
     * tries to open the hardware interface; opening the interface consists
     * in two distinct steps : first, a serial connection over which commands
     * can be sent must be established; second, a <code>"version"</code> command
     * is issued to make sure that a valid device with a valid firmware is connected
     * to that serial port; if this second step does not succeed, this class also
     * tries to reset the device via a <code>"reset"</code> command; if a valid
     * answer to the <code>"version"</code> is recieved, the hardware interface
     * is considered open.
     * 
     * @throws HardwareInterfaceException 
     * @see #setPortName
     * @see #isOpen
     * @see #doReset
     */
    @Override
    public void open() throws HardwareInterfaceException {
        if (isOpen()) return;
        if (portName == null || triedReset || hardwareError) return;
        if (!serial.isConnected()) try {
            serial.connect(portName);
        } catch (PortInUseException ex) {
            updateStatus("port in use");
            log.warning("port " + portName + " is already in use by another application");
            hardwareError = true;
            return;
        } catch (Exception ex) {
            updateStatus("I/O error");
            log.warning("I/O error while opening port " + portName + " : " + ex);
            hardwareError = true;
            return;
        }
        verified = false;
        if (!triedOpening) {
            sendCommand("version");
            updateStatus("verifying...");
            triedOpening = true;
        } else {
            sendCommand("reset");
            updateStatus("tried reset...");
            triedReset = true;
        }
    }

    /**
     * checks whether frames are currently streamed by this interface
     * @return whether frames are currently being streamed
     */
    public boolean isStreaming() {
        return converterThread != null && converterThread.isAlive();
    }

    /**
     * starts streaming via of frames via the interface
     * <br /><br />
     * this is automatically called at the device's initialization and also
     * sets the corresponding GUI element
     */
    public void startStreaming() {
        if (!isOpen()) try {
            open();
        } catch (HardwareInterfaceException ex) {
            log.warning("could not start streaming because could not open interface");
            return;
        }
        configPanel.setStreaming(true);
        if (isStreaming()) return;
        converterThread = new Thread(converter, "converter");
        converterThread.start();
        sendCommand("stream frames srinivasan");
        sendCommand("start");
    }

    /**
     * stops streaming without closing the connection
     */
    public void stopStreaming() {
        configPanel.setStreaming(false);
        if (isOpen()) sendCommand("stop");
        if (!isStreaming()) return;
        try {
            converter.stop();
            converterThread.interrupt();
            converterThread.join();
            converterThread = null;
        } catch (InterruptedException ex) {
            log.warning("interrupted while waiting for converterThread");
        }
    }

    /**
     * this method indicates, whether the device is open <b>and ready</b> for use
     * (i.e. a valid device with a valid firmware ready to stream data and
     * receive commands)
     * 
     * @return whether a connection to the device is established;
     *      can return false even when a connection to the serial port is
     *      established, but no device could be found or an error has occured
     */
    @Override
    public boolean isOpen() {
        return serial.isConnected() && verified;
    }

    @Override
    public void setChip(Chip2DMotion chip) {
        this.chip = chip;
        converter.setMotionData(chip.getEmptyMotionData());
    }

    @Override
    public JPanel getConfigPanel() {
        return configPanel;
    }

    public Chip2DMotion getChip() {
        return chip;
    }

    @Override
    public void setCaptureMode(int mask) throws HardwareInterfaceException {
        converter.setMotionData(chip.getEmptyMotionData());
    }

    /**
     * for debugging where exact timing is relevant
     * @return human readable string of 'now' down to a ms resolution
     */
    private String exactTimeString() {
        Calendar x = Calendar.getInstance();
        return String.format("%02d:%02d:%02d.%03d", x.get(Calendar.HOUR_OF_DAY), x.get(Calendar.MINUTE), x.get(Calendar.SECOND), x.get(Calendar.MILLISECOND));
    }

    /**
     * @return -1 in case of an error; the major part of the version number
     *      (that determines backward-compatability) in all other cases
     */
    protected int getMajorVersion(String answer) {
        if (answer.charAt(0) == '!' || !answer.contains("version")) return -1;
        try {
            String version = answer.substring(answer.lastIndexOf(' ') + 1);
            String major = version.substring(0, version.indexOf('.'));
            return Integer.parseInt(major);
        } catch (IndexOutOfBoundsException ex) {
            return -1;
        }
    }

    /**
     * @return -1 in case of an error; the minor part of the version number
     *      (that keeps track of new features) in all other cases
     */
    protected int getMinorVersion(String answer) {
        if (answer.charAt(0) == '!' || !answer.contains("version")) return -1;
        try {
            String version = answer.substring(answer.lastIndexOf(' ') + 1);
            String minor = version.substring(version.indexOf('.') + 1, version.length() - 1);
            return Integer.parseInt(minor);
        } catch (IndexOutOfBoundsException ex) {
            return -1;
        }
    }

    @Override
    public void answerReceived(String cmd, String answer) {
        if (debugging) System.err.format("%s : (%s)<%s\n", exactTimeString(), cmd, answer);
        configPanel.answerReceived(cmd, answer);
        if (cmd.equals("version")) {
            if (getMajorVersion(answer) == PROTOCOL_MAJOR && getMinorVersion(answer) >= PROTOCOL_MINOR) {
                verified = true;
                updateStatus("connected");
                initializeDevice();
            } else {
                updateStatus("version conflict : " + answer);
                log.warning("version conflict : " + answer);
            }
        }
        if (answer.charAt(0) == '!') {
            updateStatus("error : " + cmd);
            log.warning("command error (" + cmd + ") : " + answer);
        }
    }

    @Override
    public void unsyncedChar(char c) {
        return;
    }

    /**
     * update the status text in configPanel from within any thread
     * @param msg 
     */
    protected void updateStatus(final String msg) {
        Runnable doUpdate = new Runnable() {

            @Override
            public void run() {
                if (configPanel.isValid()) configPanel.setStatus(msg);
            }
        };
        SwingUtilities.invokeLater(doUpdate);
    }

    @Override
    public void messageReceived(StreamCommandMessage message) {
        if (message.getType() == MESSAGE_RESET) {
            log.info("device reset message received");
            if (getMajorVersion(message.getAsString()) == PROTOCOL_MAJOR) {
                verified = true;
                updateStatus("connected");
                initializeDevice();
            } else {
                updateStatus("version conflict : " + message.getAsString());
                log.warning("version conflict : " + message.getAsString());
            }
            initializeDevice();
            return;
        }
        if (!isStreaming()) return;
        if (message.getType() == MESSAGE_WORDS || message.getType() == MESSAGE_WORDS_DXDY || message.getType() == MESSAGE_DXDY) converter.pushMessage(message); else log.warning("unknown message type : " + message.getType());
    }

    @Override
    public void streamingError(int error, String msg) {
        if (isAnalysing()) analyser.incrementStreamingErrors();
        if (error == StreamCommandListener.STREAMING_OUT_OF_SYNC) {
            if (debugging) log.info("lost sync : " + msg);
        } else log.warning("streaming error : " + msg);
    }

    public boolean isDebugging() {
        return debugging;
    }

    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
        prefs.putBoolean("debugging", debugging);
    }
}
