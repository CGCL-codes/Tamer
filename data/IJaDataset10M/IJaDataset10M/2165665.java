package canonVC_C50i;

import java.io.*;
import java.util.*;
import javax.comm.*;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Redefinable;
import java.math.*;
import java.util.Arrays;

public class CanonVC implements Runnable, SerialPortEventListener {

    private Thread readThread;

    private Enumeration portList;

    private CommPortIdentifier portId;

    private SerialPort serialPort;

    private static OutputStream outputStream;

    boolean outputBufferEmptyFlag = false;

    private static InputStream inputStream;

    private String defaultPort;

    private static final int MAX_OPERATION_TIME = 2;

    private static byte[] camResponse;

    private static byte[] curComand;

    private static byte[] tempResponse;

    private static double panMax;

    private static double tiltMax;

    private double panMinSpeed;

    private double panMaxSpeed;

    private double tiltMinSpeed;

    private double tiltMaxSpeed;

    private double tiltSpeed;

    private double panSpeed;

    private double xPosition;

    private double yPosition;

    private double zoomPosition;

    private double focusPosition;

    private double panRatio;

    private double tiltRatio;

    private int focusMinRange;

    private int focusMaxRange;

    private int zoomMinRange;

    private int zoomMaxRange;

    private int panDirection;

    private int tiltDirection;

    private static final byte VC_CMD_END = (byte) 0xef;

    CanonVC() {
        defaultPort = "/dev/ttyS0";
        portList = CommPortIdentifier.getPortIdentifiers();
        portId = (CommPortIdentifier) portList.nextElement();
        try {
            serialPort = (SerialPort) portId.open("CanonVC", 2000);
        } catch (PortInUseException e) {
            System.out.println("Port in use.");
        }
        try {
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {
        }
        try {
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
        }
        tiltMax = 360.0;
        panMax = 360.0;
        panMinSpeed = 0.9;
        panMaxSpeed = 90.0;
        tiltMinSpeed = 0.9;
        tiltMaxSpeed = 69.975;
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e1) {
            e1.printStackTrace();
        }
        readThread = new Thread(this);
        readThread.start();
        try {
            VCgetPanRatio();
            VCgetTiltRatio();
            VChostControl();
            VCgetPanSpeed();
            VCgetTiltSpeed();
            VCgetFocusRange();
            VCgetZoomRange();
        } catch (CanonVC_Exception e) {
            System.out.println("Error: could not configure camera");
            System.exit(0);
        }
    }

    CanonVC(String port) {
        defaultPort = port;
        System.out.println("Using port: " + port);
        portList = CommPortIdentifier.getPortIdentifiers();
        portId = (CommPortIdentifier) portList.nextElement();
        try {
            serialPort = (SerialPort) portId.open("CanonVC", 2000);
        } catch (PortInUseException e) {
            System.out.println("Port in use.");
        }
        try {
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {
        }
        try {
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
        }
        tiltMax = 360.0;
        panMax = 360.0;
        panMinSpeed = 0.9;
        panMaxSpeed = 90.0;
        tiltMinSpeed = 0.9;
        tiltMaxSpeed = 69.975;
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e1) {
            e1.printStackTrace();
        }
        readThread = new Thread(this);
        readThread.start();
        try {
            VCgetPanRatio();
            VCgetTiltRatio();
            VChostControl();
            VCgetPanSpeed();
            VCgetTiltSpeed();
            VCgetFocusRange();
            VCgetZoomRange();
        } catch (CanonVC_Exception e) {
            System.out.println("Error: could not configure camera");
            System.exit(0);
        }
    }

    public void run() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
        }
    }

    static long getTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
   * whenever there is a response from the 
   * camera read into tempResponse
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
                    Thread.sleep(10);
                    tempResponse = new byte[inputStream.available()];
                    while (inputStream.available() > 0) {
                        inputStream.read(tempResponse);
                    }
                    System.out.print("new response from cam ::");
                    VCprintCommand(tempResponse);
                } catch (IOException e) {
                } catch (InterruptedException e) {
                    System.out.println("Response interupted try again");
                    try {
                        CommandtoCam(curComand);
                    } catch (CanonVC_Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
   * Given an ascii string of hex numbers, convert it to a binary.
   *
   * @param raw Ascii string of hex numbers
   *
   * @param size Number of characters in raw.
   *
   * @return Int value representation of value.
   */
    double VCrawToInt(byte[] raw, int start, int size) {
        byte[] cr = new byte[size];
        int i;
        for (i = start; i < size + start; i++) {
            cr[i - start] = raw[i];
        }
        String temp = null;
        try {
            temp = new String(cr, "ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Double.valueOf(Integer.valueOf(temp, 16)).doubleValue();
    }

    /**
   * Given an ascii string of hex numbers, convert it to a real number.
   *
   * @param raw Ascii string of hex numbers
   *
   * @param size Number of characters in raw.
   *
   * @return floating point representation of value.
   */
    double VCrawToReal(byte[] raw, int start, int size) {
        double d = VCrawToInt(raw, start, size);
        return d;
    }

    /**
   * Convert up to four nibbles of an short to ascii.
   *
   * @param h Integer value.
   *
   * @param size Number of bytes in resulting ascii.
 * @return 
   */
    byte[] VChexToAscii(short h, byte[] ascii, int size) {
        byte[] ans;
        ans = ascii.clone();
        String myString = "0123456789ABCDEF";
        byte[] hex = null;
        try {
            hex = myString.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Encoding Exception");
        }
        int i;
        int offset = 4 - size;
        for (i = 0; i < size; i++) {
            ans[i] = (hex[(h >> (12 - (4 * (i + offset)))) & 0xf]);
        }
        return ans;
    }

    /**
 * @param BUF command to output
 */
    public static void VCprintCommand(byte[] BUF) {
        int i = 0;
        while (true) {
            System.out.format(" %02x", BUF[i]);
            if ((BUF[i] == (byte) 0xef) || (i > 30)) {
                break;
            }
            i++;
        }
        System.out.println();
    }

    /**
   * Convert pan degrees to raw camera native units.
   *
   *
   * @param degrees Degrees to pan (-360 to +360).
   *
   * @param ascii Put 4 ascii characters here.
   *
   * @param size Number of bytes in resulting ascii.
   *
   * @return Hex value of pan speed.
   */
    short VCpanDegreesToAscii(double degrees, byte[] ascii, int start, int size) {
        short p = (short) Math.round(degrees / panRatio);
        ascii = VChexToAscii((short) (p + 0x8000), ascii, size);
        return p;
    }

    /**
   * Convert tilt degrees to raw camera native units.
   *
   *
   * @param degrees Degrees to tilt (-360 to +360).
   *
   * @param ascii Put 4 ascii characters here.
   *
   * @param size Number of bytes in resulting ascii.
   *
   * @return Hex value of tilt speed.
   */
    short VCtiltDegreesToAscii(double degrees, byte[] ascii, int start, int size) {
        short t = (short) Math.round(degrees / tiltRatio);
        ascii = VChexToAscii((short) (t + 0x8000), ascii, size);
        return t;
    }

    /**
   * Given a response from the camera, decide whether it is a
   * good one or not.  If not, describe what the problem is.
   *
   *
   *
   * @param response Message from camera to computer.
   *
   * @return Zero if good message from camera, error code if
   *         there is a problem.
   */
    public static VCstatus VCcameraStatus() {
        byte[] good = { (byte) 0xfe, 0x30, 0x30, 0x30, 0x30, VC_CMD_END };
        byte[] busy = { (byte) 0xfe, 0x30, 0x30, 0x31, 0x30, VC_CMD_END };
        byte[] badParam = { (byte) 0xfe, 0x30, 0x30, 0x35, 0x30, VC_CMD_END };
        byte[] modeErr = { (byte) 0xfe, 0x30, 0x30, 0x39, 0x30, VC_CMD_END };
        if (Arrays.equals(good, tempResponse)) {
            return VCstatus.GOOD;
        } else if (Arrays.equals(busy, tempResponse)) {
            return VCstatus.BUSY;
        } else if (Arrays.equals(badParam, tempResponse)) {
            return VCstatus.BADPARAM;
        } else if (Arrays.equals(modeErr, tempResponse)) {
            return VCstatus.MODEERR;
        } else {
            return VCstatus.GOOD;
        }
    }

    /**
   * Send a message to the camera.  The command must be
   * terminated with VC_CMD_END, which will be used as the
   * cmomand terminator.  It is assumed that commands are short
   * (less than 100 bytes).
   *
   * After the command is sent, the response will be read from
   * the camera.
   *
   * If a good response is received, then a good status will be returned.
   *
   * If the camera does not respond or responds with an error,
   * then an exception will be thrown.
   *
   *
   *
   * @param command Command bytes to send to the camera,
   *        terminated with VC_CMD_END.
   *
   * @param resp If this is not null and the camera gives a
   * good response, then put the response here.  Caller must
   * ensure that the buffer is big enough.
   *
   * @return Zero on success, throws error on fail.
   * @throws CanonVC_Exception 
   */
    static int CommandtoCam(byte[] command) throws CanonVC_Exception {
        byte[] VC_start = { (byte) 0xff, (char) 0x30, (char) 0x30 };
        byte[] VC_BUF = new byte[command.length + 5];
        int i;
        for (i = 0; i < VC_start.length; i++) {
            VC_BUF[i] = VC_start[i];
        }
        for (int j = 0; j < command.length; j++) {
            VC_BUF[i] = command[j];
            i++;
        }
        curComand = VC_BUF;
        System.out.print("sending command :: ");
        VCprintCommand(VC_BUF);
        try {
            outputStream.write(VC_BUF);
        } catch (IOException e) {
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        camResponse = tempResponse;
        VCstatus temp = VCcameraStatus();
        if (temp != VCstatus.GOOD) {
            throw new CanonVC_Exception("VC status bad");
        }
        return 0;
    }

    /**
   * Send a command to the camera.  If the camera is busy, keep
   * retrying it until it either succeeds, fails for another
   * reason, or the operation times out
   *
   *
   * @param command Command to send to camera.
   *
   * @return Zero on success, throws error on failure.
   * @throws CanonVC_Exception 
   */
    public static int VCtoCameraRetryBusy(byte[] command) throws CanonVC_Exception {
        double timeout = getTime() + MAX_OPERATION_TIME;
        boolean ok = true;
        try {
            CommandtoCam(command);
        } catch (CanonVC_Exception e) {
            System.out.println("Command faild to exicute");
        }
        while (VCcameraStatus() == VCstatus.BUSY && (timeout > getTime())) {
            try {
                CommandtoCam(command);
            } catch (CanonVC_Exception e) {
                System.out.println("Command faild to exicute");
            }
            ok = false;
        }
        if ((!ok) && (VCcameraStatus() == VCstatus.GOOD)) {
            ok = true;
        }
        if ((!ok) && (timeout <= getTime())) {
            ok = true;
            throw new CanonVC_Exception("VCtoCameraRetryBusy failed. Time out");
        }
        if (!ok) {
            ok = true;
            throw new CanonVC_Exception("busy failed for some other reason");
        }
        return 0;
    }

    /**
   * convert an angle to ascii bits that the camera can handle
   *
   *
   * @param pan the pan or tilt angle
   *
   * @return an array on bytes with the ascii pan value
   */
    public static byte[] angle2ascii(double pan, int size) {
        byte[] ans = new byte[4];
        int bufangle = (int) (0x8000 + Math.round(pan / 0.1125));
        String myString = "0123456789ABCDEF";
        byte[] hex = null;
        try {
            hex = myString.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Encoding Exception");
        }
        int i;
        int offset = 4 - size;
        for (i = 0; i < size; i++) {
            ans[i] = (byte) (hex[(bufangle >> (12 - (4 * (i + offset)))) & 0xf]);
        }
        return ans;
    }

    /**
   * Assign an angle to the camera
   *
   *
   * @param x the pan degree
   * @param y the tilt degree
   * @throws CanonVC_Exception 
   *
   * 
   */
    public void AngleAssignment(double x, double y) throws CanonVC_Exception {
        if ((x > panMax) || (x < -panMax)) {
            throw (new CanonVC_Exception("pan out of bounds"));
        }
        if ((y > tiltMax) || (y < -(tiltMax))) {
            throw (new CanonVC_Exception("Tilt degrees out of bounds."));
        }
        byte[] Xarg = new byte[4];
        Xarg = angle2ascii(x, 4);
        byte[] Yarg = new byte[4];
        Yarg = angle2ascii(y, 4);
        byte[] com = { 0x00, 0x62, (byte) 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, VC_CMD_END };
        for (int i = 0; i < 4; i++) {
            com[2 + i] = Xarg[i];
        }
        for (int i = 0; i < 4; i++) {
            com[6 + i] = Yarg[i];
        }
        try {
            VCtoCameraRetryBusy(com);
        } catch (CanonVC_Exception e) {
            System.out.println("command fail");
            e.printStackTrace();
        }
    }

    /**
   * Put the camera in host control mode.  This must be done
   * before issuing any other commands.  This mode means that the
   * camera is controlled by the computer and not by the
   * infra-red remote.
   *
   *
   *
   * @throws CanonVC_Exception 
   */
    public void VChostControl() throws CanonVC_Exception {
        byte[] hostControl = { 0x00, (byte) 0x90, 0x30, VC_CMD_END };
        CommandtoCam(hostControl);
    }

    /**
   * Get the current pan and tilt angles of the camera.  The
   * results are put in the vc struct.
   *
   *
   *
   * @throws CanonVC_Exception
   */
    public void VCgetPosition() throws CanonVC_Exception {
        byte[] command = { 0x00, 0x63, VC_CMD_END };
        if (VCtoCameraRetryBusy(command) == 0) {
            xPosition = (VCrawToReal(camResponse, 5, 4) - 0x8000) * panRatio;
            yPosition = (VCrawToReal(camResponse, 9, 4) - 0x8000) * tiltRatio;
        }
    }

    /**
   * Get the pan ratio of the camera.
   *
   *
   *
   * @param ps Pan ratio (returned).
   * @throws CanonVC_Exception 
   *
   */
    public void VCgetPanRatio() throws CanonVC_Exception {
        byte[] command = { 0x00, 0x5b, 0x30, VC_CMD_END };
        CommandtoCam(command);
        panRatio = VCrawToReal(camResponse, 5, 4) / 100000.0;
    }

    /**
   * Get the tilt ratio of the camera.
   *
   *
   *
   * @param ps Tilt ratio (returned).
   *
   * @throws CanonVC_Exception 
   */
    public void VCgetTiltRatio() throws CanonVC_Exception {
        byte[] command = { 0x00, 0x5b, 0x31, VC_CMD_END };
        CommandtoCam(command);
        tiltRatio = VCrawToReal(camResponse, 5, 4) / 100000.0;
    }

    /**
   * Set the pan speed of the camera.  If the speed is too fast
   * or slow, then set it to the corresponding high or low speed limit.
   *
   *
   *
   * @param ps Pan speed in degrees/sec.
   * @throws CanonVC_Exception 
   *
   */
    public void VCsetPanSpeed(double ps) throws CanonVC_Exception {
        byte[] command = { 0x00, 0x50, 0x00, 0x00, 0x00, VC_CMD_END };
        double speed = ps;
        if (ps > panMaxSpeed) {
            speed = panMaxSpeed;
        }
        if (ps < panMinSpeed) {
            speed = panMinSpeed;
        }
        if (VCpanDegreesToAscii(speed, command, 2, 3) == 0) {
            command[4] = 0x31;
        }
        if (VCtoCameraRetryBusy(command) == 0) {
            panSpeed = VCrawToReal(command, 2, 3) * panRatio;
        }
    }

    /**
   * Set the tilt speed of the camera.  If the speed is too
   * fast or slow, then set it to the corresponding high or low
   * speed limit.
   *
   *
   *
   * @param ts Tilt speed in degrees/sec.
   *
   * @throws CanonVC_Exception 
   */
    public void VCsetTiltSpeed(double ts) throws CanonVC_Exception {
        byte[] command = { 0x00, 0x51, 0x00, 0x00, 0x00, VC_CMD_END };
        double speed = ts;
        if (ts > tiltMaxSpeed) {
            speed = tiltMaxSpeed;
        }
        if (ts < tiltMinSpeed) {
            speed = tiltMinSpeed;
        }
        if (VCtiltDegreesToAscii(speed, command, 2, 3) == 0) {
            command[4] = 0x31;
        }
        if (VCtoCameraRetryBusy(command) == 0) {
            tiltSpeed = ts;
        }
    }

    /**
   * Get the pan speed of the camera in degrees / sec.
   *
   *
   *
   *
   * @throws CanonVC_Exception 
   */
    public void VCgetPanSpeed() throws CanonVC_Exception {
        byte[] command = { 0x00, 0x52, 0x30, VC_CMD_END };
        CommandtoCam(command);
        panSpeed = VCrawToReal(tempResponse, 5, 3) * panRatio;
    }

    /**
   * Get the tilt speed of the camera in degrees / sec.
   *
   *
   *
   * @param ps Tilt speed (returned).
   *
   * @throws CanonVC_Exception 
   */
    public void VCgetTiltSpeed() throws CanonVC_Exception {
        byte[] command = { 0x00, 0x52, 0x31, VC_CMD_END };
        CommandtoCam(command);
        tiltSpeed = VCrawToReal(tempResponse, 5, 3) * tiltRatio;
    }

    /**
   * Convert the pan speed to the actual pan speed that the
   * camera will use.  This accounts for the fact that the speed
   * setting has a granularity.  For example, if the camera is
   * given a pan speed of 3.0000 degrees/sec, then it will
   * actually set it to 3.0375 degress/sec.
   *
   * Setting a value of 0 to stop panning will not work,
   * although the conversion does.
   *
   *
   *
   * @param ps Pan speed.
   *
   * @return Converted value.
   */
    double VCconvertPanSpeed(double ps) {
        byte[] buf = new byte[4];
        VCpanDegreesToAscii(ps, buf, 0, 3);
        double s = VCrawToReal(buf, 0, 3) * panRatio;
        return s;
    }

    /**
   * Convert the tilt speed to the actual tilt speed that the
   * camera will use.  This accounts for the fact that the speed
   * setting has a granularity.  For example, if the camera is
   * given a tilt speed of 24.0000 degrees/sec, then it will
   * actually set it to 23.96250 degress/sec.  Note that some
   * work out exactly, such as 9, 18, and 27 degrees.
   *
   * Setting a value of 0 to stop tilting will not work,
   * although the conversion does.
   *
   *
   *
   * @param ts Tilt speed.
   *
   * @return Converted value.
   */
    double VCconvertTiltSpeed(double ts) {
        byte[] buf = new byte[4];
        VCtiltDegreesToAscii(ts, buf, 0, 3);
        double s = VCrawToReal(buf, 0, 3) * tiltRatio;
        return s;
    }

    /**
   * Reset the camera.
   *
   *
   *
   * @throws CanonVC_Exception 
   */
    public void VCreset() throws CanonVC_Exception {
        byte[] command = { 0x00, (byte) 0xa1, (byte) 0xaa, VC_CMD_END };
        CommandtoCam(command);
    }

    /**
   * Set the focus mode.
   *
   *
   *
   * @param mode Focus mode.
   *
   * @throws CanonVC_Exception 
   */
    public void VCsetFocusMode(int mode) throws CanonVC_Exception {
        byte[] command = { 0x00, (byte) 0xa1, 0x00, VC_CMD_END };
        command[2] = (byte) mode;
        CommandtoCam(command);
    }

    /**
   * Set the focus position.
   *
   *
   *
   * @param pos Position of focus.
   *
   * @throws CanonVC_Exception 
   */
    public void VCsetFocusPosition(int pos) throws CanonVC_Exception {
        byte[] command = { 0x00, (byte) 0xb0, 0x00, 0x00, 0x00, 0x00, VC_CMD_END };
        short p = (short) pos;
        command = VChexToAscii(p, command, 4);
        CommandtoCam(command);
    }

    /**
   * Get the focal range.
   *
   *
   *
   * @param minFocus Minimum focus range.
   *
   * @param maxFocus Maximum focus range.
   *
   * @throws CanonVC_Exception 
   */
    public void VCgetFocusRange() throws CanonVC_Exception {
        byte[] command = { 0x00, (byte) 0xb1, 0x32, VC_CMD_END };
        CommandtoCam(command);
        focusMinRange = (char) VCrawToInt(camResponse, 5, 4);
        focusMaxRange = (char) VCrawToInt(camResponse, 9, 4);
    }

    /**
   * Get the current focus position.
   *
   *
   *
   * @param pos Focus position (returned).
   *
   * @throws CanonVC_Exception 
   */
    public void VCgetFocusPosition() throws CanonVC_Exception {
        byte[] command = { 0x00, (byte) 0xb1, 0x30, VC_CMD_END };
        CommandtoCam(command);
        focusPosition = VCrawToInt(camResponse, 5, 4);
    }

    /**
   * Get the zoom range.
   *
   *
   *
   * @param minFocus Minimum focus range.
   *
   * @param maxFocus Maximum focus range.
   *
   * @throws CanonVC_Exception 
   */
    public void VCgetZoomRange() throws CanonVC_Exception {
        byte[] command = { 0x00, (byte) 0xb4, 0x33, VC_CMD_END };
        CommandtoCam(command);
        zoomMinRange = 0;
        zoomMaxRange = (int) VCrawToInt(camResponse, 5, 4);
    }

    /**
   * Set the zoom.  This uses zoom position 2.
   *
   *
   * @param zoom Amount to zoom.  The larger the value, the
   *        more the image is magnified.
   *
   * @throws CanonVC_Exception 
   */
    public void VCsetZoom(int zoom) throws CanonVC_Exception {
        byte[] command = { 0x00, (byte) 0xb3, 0x00, 0x00, 0x00, 0x00, VC_CMD_END };
        short z = (short) zoom;
        if ((zoom < zoomMinRange) || (zoom > zoomMaxRange)) {
            throw new CanonVC_Exception("Zoom value out of range");
        }
        command = VChexToAscii(z, command, 4);
        VCtoCameraRetryBusy(command);
        zoomPosition = zoom;
    }

    /**
   * Get the current zoom position.  Use zoom position 2.
   *
   *
   *
   * @param pos Zoom position (returned).
   *
   * @throws CanonVC_Exception 
   */
    public void VCgetZoomPosition() throws CanonVC_Exception {
        byte[] command = { 0x00, (byte) 0xb4, 0x30, VC_CMD_END };
        VCtoCameraRetryBusy(command);
        zoomPosition = VCrawToInt(camResponse, 5, 4);
    }

    /**
   * Point the camera to the straight ahead position.
   *
   *
   * @throws CanonVC_Exception 
   */
    public void VChome() throws CanonVC_Exception {
        byte[] home = { 0x00, 0x57, VC_CMD_END };
        VCtoCameraRetryBusy(home);
    }

    /**
   * Close the serial port connection. 
   * Should only be called at the very end of the program.
   * 
   */
    public void close() {
        try {
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
   * Point the camera to the straight ahead position. 
   * also initialize (1) the pedestal
   *
   * @throws CanonVC_Exception 
   */
    public void InitializePedestal() throws CanonVC_Exception {
        byte[] Initalize = { 0x00, 0x58, 0x30, VC_CMD_END };
        VCtoCameraRetryBusy(Initalize);
    }

    /**
   * return the information about the VC that you want.
   * options for falg:
   * camResponse, curComand, tempResponse, panMax, tiltMax, panMinSpeed, panMaxSpeed,
   * tiltMinSpeed, tiltMaxSpeed, tiltSpeed, panSpeed, xPosition, Yposition,
   * zoomPosition, flcusPosition, panRatio, tiltRatio, focusMinRange, focusMaxRange,
   * zoomMinRange, zoomMaxRange, panDirection, tiltDirection, defaultPort
   * 
   * default: Arguament not valid
   * 
 * @param flag
 * @return string of the requested info
 */
    public String getinfo(String flag) {
        if ((flag == "camResponse")) return (new String(camResponse));
        if (flag == "curComand") return (new String(curComand));
        if (flag == "tempResponse") return (new String(tempResponse));
        if (flag == "panMax") return Double.toString(panMax);
        if (flag == "tiltMax") return Double.toString(tiltMax);
        if (flag == "panMinSpeed") return Double.toString(panMinSpeed);
        if (flag == "panMaxSpeed") return Double.toString(panMaxSpeed);
        if (flag == "tiltMinSpeed") return Double.toString(tiltMinSpeed);
        if (flag == "tiltMaxSpeed") return Double.toString(tiltMaxSpeed);
        if (flag == "tiltSpeed") return Double.toString(tiltSpeed);
        if (flag == "panSpeed") return Double.toString(panSpeed);
        if (flag == "xPosition") return Double.toString(xPosition);
        if (flag == "yPosition") return Double.toString(yPosition);
        if (flag == "zoomPosition") return Double.toString(zoomPosition);
        if (flag == "focusPosition") return Double.toString(focusPosition);
        if (flag == "panRatio") return Double.toString(panRatio);
        if (flag == "tiltRatio") return Double.toString(tiltRatio);
        if (flag == "focusMinRange") return Integer.toString(focusMinRange);
        if (flag == "focusMaxRange") return Integer.toString(focusMaxRange);
        if (flag == "zoomMinRange") return Integer.toString(zoomMinRange);
        if (flag == "zoomMaxRange") return Integer.toString(zoomMaxRange);
        if (flag == "panDirection") return Integer.toString(panDirection);
        if (flag == "tiltDirection") return Integer.toString(tiltDirection);
        if (flag == "defaultPort") return defaultPort;
        return ("Arguament not valid");
    }

    public void panRangeAssign(double range) {
    }
}
