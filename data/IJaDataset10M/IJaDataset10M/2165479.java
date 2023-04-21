package net.frontlinesms.smsdevice;

import java.io.*;
import java.util.*;
import net.frontlinesms.CommUtils;
import serial.*;

/**
 * A commandline utility for detecting connected AT devices.
 * The detection workflow was taken from ComTest in SMSLib.
 * @author Alex Anderson alex@frontlinesms.com
 */
public class AllModemsDetector {

    public static void main(String[] args) {
        AllModemsDetector amd = new AllModemsDetector();
        ATDeviceDetector[] detectors = amd.detectBlocking();
        printReport(detectors);
    }

    private Logger log = new Logger(getClass());

    private ATDeviceDetector[] detectors;

    /** Trigger detection, and return the results when it is completed. */
    public ATDeviceDetector[] detectBlocking() {
        detect();
        waitUntilDetectionComplete(detectors);
        return getDetectors();
    }

    /** Trigger detection. */
    public void detect() {
        log.trace("Starting device detection...");
        Set<ATDeviceDetector> detectors = new HashSet<ATDeviceDetector>();
        Enumeration<CommPortIdentifier> ports = CommUtils.getPortIdentifiers();
        while (ports.hasMoreElements()) {
            CommPortIdentifier port = ports.nextElement();
            if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                ATDeviceDetector d = new ATDeviceDetector(port);
                detectors.add(d);
                d.start();
            } else {
                log.info("Ignoring non-serial port: " + port.getName());
            }
        }
        this.detectors = detectors.toArray(new ATDeviceDetector[0]);
        log.trace("All detectors started.");
    }

    /** Get the detectors. */
    public ATDeviceDetector[] getDetectors() {
        return detectors;
    }

    /** Blocks until all detectors have completed execution. */
    private static void waitUntilDetectionComplete(ATDeviceDetector[] detectors) {
        boolean completed;
        do {
            completed = true;
            for (ATDeviceDetector portDetector : detectors) {
                if (!portDetector.isFinished()) {
                    completed = false;
                }
            }
            Utils.sleep(500);
        } while (!completed);
    }

    /** Prints a report to {@link System#out} detailing the devices that were detected. */
    private static void printReport(ATDeviceDetector[] completedDetectors) {
        for (ATDeviceDetector d : completedDetectors) {
            System.out.println("---");
            System.out.println("PORT   : " + d.getPortIdentifier().getName());
            if (d.isDetected()) {
                System.out.println("SERIAL : " + d.getSerial());
                System.out.println("BAUD   : " + d.getMaxBaudRate());
            } else {
                System.out.println("DETECTION FAILED");
                System.out.println("> " + d.getExceptionMessage());
            }
        }
    }
}

class ATDeviceDetector extends Thread {

    /** Valid baud rates */
    private static final int[] BAUD_RATES = { 9600, 14400, 19200, 28800, 33600, 38400, 56000, 57600, 115200, 230400, 460800, 921600 };

    /** Logger */
    private final Logger log = new Logger(this.getClass());

    /** Port this is detecting on */
    private final CommPortIdentifier portIdentifier;

    /** The top speed the device was detected at. */
    private int maxBaudRate;

    /** The serial number of the detected device. */
    private String serial;

    /** <code>true</code> when the detection thread has finished. */
    private boolean finished;

    private String exceptionMessage;

    public ATDeviceDetector(CommPortIdentifier port) {
        super("ATDeviceDetector: " + port.getName());
        this.portIdentifier = port;
    }

    public void run() {
        for (int baud : BAUD_RATES) {
            SerialPort serialPort = null;
            InputStream in = null;
            OutputStream out = null;
            try {
                serialPort = portIdentifier.open("ATDeviceDetector", 2000);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
                serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                serialPort.enableReceiveTimeout(1000);
                log.trace("LOOPING.");
                Utils.readAll(in);
                Utils.writeCommand(out, "AT");
                Utils.sleep(1000);
                String response = Utils.readAll(in);
                if (!Utils.isResponseOk(response)) {
                    throw new ATDeviceDetectionException("Bad response: " + response);
                } else {
                    Utils.writeCommand(out, "AT+CGSN");
                    response = Utils.readAll(in);
                    if (!Utils.isResponseOk(response)) {
                        throw new ATDeviceDetectionException("Bad response to request for serial number: " + response);
                    } else {
                        String serial = Utils.trimResponse("AT+CGSN", response);
                        log.debug("Found serial: " + serial);
                        if (this.serial != null) {
                            if (!this.serial.equals(serial)) {
                                log.info("New serial detected: '" + serial + "'.  Replacing previous: '" + this.serial + "'");
                            }
                        }
                        this.serial = serial;
                        maxBaudRate = Math.max(maxBaudRate, baud);
                    }
                }
            } catch (Exception ex) {
                log.info("Problem connecting to device.", ex);
                this.exceptionMessage = ex.getMessage();
            } finally {
                if (out != null) try {
                    out.close();
                } catch (Exception ex) {
                }
                if (in != null) try {
                    in.close();
                } catch (Exception ex) {
                }
                if (serialPort != null) try {
                    serialPort.close();
                } catch (Exception ex) {
                }
            }
        }
        finished = true;
        log.info("Detection completed on port: " + this.portIdentifier.getName());
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isDetected() {
        return this.maxBaudRate > 0;
    }

    public CommPortIdentifier getPortIdentifier() {
        return portIdentifier;
    }

    public int getMaxBaudRate() {
        return maxBaudRate;
    }

    public String getSerial() {
        assert (isDetected()) : "Cannot get serial if no device was detected.";
        return serial;
    }

    public String getExceptionMessage() {
        assert (!isDetected()) : "Cannot get Throwable clause if device was detected successfully.";
        return exceptionMessage;
    }
}

class Utils {

    /** Calls {@link Thread#sleep(long)} and ignores {@link InterruptedException}s thrown. */
    public static final void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }

    /**
	 * Read all bytes available on the input stream, and concatenates them into a string.
	 * N.B. This casts bytes read directly to characters.
	 */
    public static final String readAll(InputStream in) throws IOException {
        StringBuilder bob = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            bob.append((char) c);
        }
        return bob.toString();
    }

    /** Writes the supplied command to the output stream, followed by a \r character */
    public static final void writeCommand(OutputStream out, String command) throws IOException {
        for (char c : command.toCharArray()) out.write(c);
        out.write('\r');
    }

    /** @return <code>true</code> if the response contains "OK" */
    public static final boolean isResponseOk(String response) {
        return response.indexOf("OK") != -1;
    }

    /** @return the response with the original command, "OK" and all trailing and leading whitespace removed */
    public static final String trimResponse(String command, String response) {
        return response.replace("OK", "").replace(command, "").trim();
    }
}

/** Quick stub of a logger to send things to the command line.  You'll want to replace this with something proper. */
class Logger {

    /** Set this to <code>true</code> if you want logging to be printed. */
    private static final boolean DO_PRINTING = true;

    /** Description of this logger */
    private final String description;

    /** Create a new logger for the supplied class */
    public Logger(Class<?> clazz) {
        this.description = clazz.getSimpleName();
    }

    public void trace(String s) {
        out("TRACE", s);
    }

    public void debug(String s) {
        out("DEBUG", s);
    }

    public void info(String s) {
        out("INFO", s);
    }

    public void info(String message, Throwable t) {
        out("INFO", message, t);
    }

    private void out(String level, String message) {
        out(level, message, null);
    }

    private void out(String level, String message, Throwable t) {
        if (!DO_PRINTING) return;
        PrintStream out = t == null ? System.out : System.err;
        out.println("[" + Thread.currentThread().getName() + " : " + description + "] " + level + ": " + message);
        if (t != null) t.printStackTrace();
    }
}

/** Exception thrown when detecting an AT device. */
@SuppressWarnings("serial")
class ATDeviceDetectionException extends Exception {

    public ATDeviceDetectionException(String message) {
        super(message);
    }
}
