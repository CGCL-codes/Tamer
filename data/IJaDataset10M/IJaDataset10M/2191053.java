package test.org.mbari.vcr;

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
import org.mbari.util.NumberUtil;
import org.mbari.vcr.rs422.VCR;
import org.mbari.vcr.rs422.VCRReply;

/**
 * Class declaration
 * 
 * 
 * @author
 * @version 1.8, 08/03/00
 */
public class SimpleReadRxtx implements Runnable, SerialPortEventListener {

    static CommPortIdentifier portId;

    static Enumeration portList;

    InputStream inputStream;

    OutputStream outputStream;

    SerialPort serialPort;

    Thread readThread;

    private static final Log log = LogFactory.getLog(SimpleReadRxtx.class);

    /**
	 * Constructor declaration
	 * 
	 * 
	 * @see
	 */
    public SimpleReadRxtx() {
        try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {
        }
        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
        }
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_ODD);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
            serialPort.enableReceiveTimeout((int) 400);
        } catch (UnsupportedCommOperationException e) {
        }
        try {
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
        }
        readThread = new Thread(this);
        readThread.start();
    }

    public void runTest() throws IOException, InterruptedException {
        send(VCR.GET_STATUS);
        send(VCR.GET_TIMECODE);
        send(VCR.PLAY_FWD);
        send(VCR.GET_STATUS);
        send(VCR.GET_TIMECODE);
        Thread.sleep(2000);
        send(VCR.STOP);
        send(VCR.GET_TIMECODE);
        Thread.sleep(2000);
        send(VCR.GET_TIMECODE1);
        Thread.sleep(2000);
        send(VCR.GET_TIMECODE2);
        Thread.sleep(2000);
        send(VCR.GET_STATUS);
        Thread.sleep(2000);
        send(VCR.GET_TIMECODE);
        send(VCR.PLAY_FWD);
        send(VCR.GET_STATUS);
        send(VCR.GET_TIMECODE);
        Thread.sleep(2000);
        send(VCR.STOP);
        send(VCR.GET_TIMECODE);
        Thread.sleep(2000);
        send(VCR.GET_TIMECODE1);
        Thread.sleep(2000);
        send(VCR.GET_TIMECODE2);
        Thread.sleep(2000);
        send(VCR.GET_STATUS);
        Thread.sleep(2000);
        System.exit(0);
    }

    /**
	 * Method declaration
	 * 
	 * 
	 * @see
	 */
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    public void send(byte[] data) throws IOException, InterruptedException {
        Thread.sleep(33);
        byte checksum = VCRReply.calculateChecksum(data);
        final byte[] c = new byte[data.length + 1];
        System.arraycopy(data, 0, c, 0, data.length);
        c[c.length - 1] = checksum;
        log.debug(">> " + NumberUtil.toHexString(c) + " ");
        outputStream.write(c);
    }

    /**
	 * Method declaration
	 * 
	 * 
	 * @param event
	 * 
	 * @see
	 */
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
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public byte[] receive() throws IOException, InterruptedException {
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
        int dataLength = data == null ? 0 : data.length;
        final byte[] c = new byte[cmd.length + dataLength + 1];
        System.arraycopy(cmd, 0, c, 0, cmd.length);
        if (data != null) {
            System.arraycopy(data, 0, c, cmd.length, data.length);
        }
        c[c.length - 1] = checksum[0];
        log.debug("<< " + NumberUtil.toHexString(c));
        return c;
    }

    /**
	 * Method declaration
	 * 
	 * 
	 * @param args
	 * 
	 * @see
	 */
    public static void main(String[] args) {
        boolean portFound = false;
        String defaultPort = "COM2";
        if (args.length > 0) {
            defaultPort = args[0];
        }
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(defaultPort)) {
                    System.out.println("Found port: " + defaultPort);
                    portFound = true;
                    SimpleReadRxtx reader = new SimpleReadRxtx();
                    try {
                        reader.runTest();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!portFound) {
            System.out.println("port " + defaultPort + " not found.");
        }
    }
}
