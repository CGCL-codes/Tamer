package org.indi.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.indi.clientmessages.GetProperties;
import org.indi.nativelib.NativeLibraryLoader;
import org.indi.objects.Permission;
import org.indi.objects.Standard;
import org.indi.objects.State;
import org.indi.objects.Switch;
import org.indi.objects.SwitchRule;
import org.indi.objects.SwitchVector;
import org.indi.objects.Text;
import org.indi.objects.TextVector;
import org.indi.objects.TransferType;
import org.indi.server.BasicDevice;

/**
 * Listener to handle all serial port events.
 */
public class SerialDevice extends BasicDevice implements SerialPortEventListener {

    /**
     * the logger for errors and warnings.
     */
    private static final Log LOG = LogFactory.getLog(SerialDevice.class);

    /**
     * the switch to connect/disconnect the device
     */
    protected final Switch connectswitch;

    protected InputStream in;

    protected OutputStream out;

    /**
     * The Vector hosting the connect /disconnect switch
     */
    protected final SwitchVector powerswitch;

    protected final Text devicePort;

    protected final TextVector portvector;

    private CommPort commPort;

    /**
     * class constructor
     * 
     * @param server
     *                the server to host the device
     */
    public SerialDevice(String name) {
        super();
        this.name = name;
        NativeLibraryLoader.loadLibrary(Thread.currentThread().getContextClassLoader(), "rxtxSerial");
        this.powerswitch = new SwitchVector(this.name, Standard.Vector.CONNECTION, getMainControlGroup(), State.Idle, Permission.ReadWrite, SwitchRule.OneOfMany, 0);
        this.connectswitch = new Switch(Standard.Property.CONNECT, Switch.State.Off);
        this.powerswitch.add(this.connectswitch);
        this.powerswitch.add(new Switch(Standard.Property.DISCONNECT, Switch.State.On));
        this.devicePort = new Text(Standard.Property.DEVICE_PORT.id, Standard.Property.DEVICE_PORT.label, getDefaultPort());
        this.portvector = new TextVector(this.name, Standard.Vector.PORT.id, Standard.Vector.PORT.label, getMainControlGroup(), State.Idle, Permission.ReadWrite, 0d, Long.toString(System.currentTimeMillis()), null);
    }

    protected String getMainControlGroup() {
        return "Main Control";
    }

    private String getDefaultPort() {
        Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        if (portIdentifiers.hasMoreElements()) {
            return ((CommPortIdentifier) portIdentifiers.nextElement()).getName();
        } else {
            return "";
        }
    }

    public void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            LOG.error("Port is currently in use");
        } else {
            commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                setSerialPortParameters(serialPort);
                serialPort.addEventListener(this);
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                activateEventListener(serialPort);
            } else {
                LOG.error("Only serial ports are handled");
            }
        }
    }

    protected void setSerialPortParameters(SerialPort serialPort) throws UnsupportedCommOperationException {
        serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }

    protected void activateEventListener(SerialPort serialPort) {
        serialPort.notifyOnDataAvailable(true);
        serialPort.notifyOnOutputEmpty(true);
        serialPort.notifyOnBreakInterrupt(true);
        serialPort.notifyOnCarrierDetect(true);
        serialPort.notifyOnCTS(true);
        serialPort.notifyOnDSR(true);
        serialPort.notifyOnFramingError(true);
        serialPort.notifyOnOverrunError(true);
        serialPort.notifyOnParityError(true);
        serialPort.notifyOnRingIndicator(true);
    }

    @Override
    public void onGetProperties(GetProperties o) {
        def(this.powerswitch);
        def(this.portvector);
    }

    @Override
    public void onNew(SwitchVector vector) {
        if ((Standard.Vector.CONNECTION.id.equals(vector.getName())) && (this.name.equals(vector.getDevice()))) {
            this.powerswitch.update(vector);
        }
        switch(this.connectswitch.getState()) {
            case On:
                this.powerswitch.setState(State.Ok);
                set(this.powerswitch, "Connection to Simple Device is successful.");
                break;
            case Off:
                this.powerswitch.setState(State.Idle);
                set(this.powerswitch, "Simple Device has been disconneced.");
                break;
        }
    }

    @Override
    public void onNew(TextVector vector) {
        if ((Standard.Vector.PORT.id.equals(vector.getName())) && (this.name.equals(vector.getDevice()))) {
            this.portvector.update(vector);
        }
        if (this.connectswitch.getState() == Switch.State.On) {
            disconnectPort();
        }
        try {
            connect(devicePort.getValue(TransferType.Set));
        } catch (Exception e) {
            LOG.error("could not connect to device", e);
        }
    }

    private void disconnectPort() {
        if (commPort != null) {
            commPort.close();
            commPort = null;
        }
    }

    /**
     * Handle serial events. Dispatches the event to event-specific methods.
     * 
     * @param event
     *                The serial event
     */
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                outputBufferEmpty(event);
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                dataAvailable(event);
                break;
            case SerialPortEvent.BI:
                breakInterrupt(event);
                break;
            case SerialPortEvent.CD:
                carrierDetect(event);
                break;
            case SerialPortEvent.CTS:
                clearToSend(event);
                break;
            case SerialPortEvent.DSR:
                dataSetReady(event);
                break;
            case SerialPortEvent.FE:
                framingError(event);
                break;
            case SerialPortEvent.OE:
                overrunError(event);
                break;
            case SerialPortEvent.PE:
                parityError(event);
                break;
            case SerialPortEvent.RI:
                ringIndicator(event);
                break;
        }
    }

    /**
     * Handle data available events.
     * 
     * @param event
     *                The data available event
     */
    protected void dataAvailable(SerialPortEvent event) {
    }

    /**
     * Handle output buffer empty events. NOTE: The reception is of this event
     * is optional and not guaranteed by the API specification.
     * 
     * @param event
     *                The output buffer empty event
     */
    protected void outputBufferEmpty(SerialPortEvent event) {
    }

    private void breakInterrupt(SerialPortEvent event) {
    }

    private void carrierDetect(SerialPortEvent event) {
    }

    private void clearToSend(SerialPortEvent event) {
    }

    private void dataSetReady(SerialPortEvent event) {
    }

    private void framingError(SerialPortEvent event) {
    }

    private void overrunError(SerialPortEvent event) {
    }

    private void parityError(SerialPortEvent event) {
    }

    private void ringIndicator(SerialPortEvent event) {
    }
}
