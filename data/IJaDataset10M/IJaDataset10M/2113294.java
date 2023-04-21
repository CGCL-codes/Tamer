package wizworld.io;

import java.io.*;
import java.util.Vector;

/** Generic serial comms
 * @author (c) Stephen Denham 2002
 * @version 0.1
 */
public interface Serial {

    /** 2400 baud */
    public static final int BAUDRATE_2400 = 2400;

    /** 4800 baud */
    public static final int BAUDRATE_4800 = 4800;

    /** 9600 baud */
    public static final int BAUDRATE_9600 = 9600;

    /** 19200 baud */
    public static final int BAUDRATE_19200 = 19200;

    /** 38400 baud */
    public static final int BAUDRATE_38400 = 38400;

    /** 57600 baud */
    public static final int BAUDRATE_57600 = 57600;

    /** 115200 baud */
    public static final int BAUDRATE_115200 = 115200;

    /** 5 data bits */
    public static final int DATABITS_5 = 5;

    /** 6 data bits */
    public static final int DATABITS_6 = 6;

    /** 7 data bits */
    public static final int DATABITS_7 = 7;

    /** 8 data bits */
    public static final int DATABITS_8 = 8;

    /** 1 stop bit **/
    public static final int STOPBITS_1 = 1;

    /** 2 stop bits **/
    public static final int STOPBITS_2 = 2;

    /** 1.5 stop bits **/
    public static final int STOPBITS_1_5 = 3;

    /** No parity */
    public static final int PARITY_NONE = 0;

    /** Odd parity */
    public static final int PARITY_ODD = 1;

    /** Even parity */
    public static final int PARITY_EVEN = 2;

    /** List available serial ports
   * @return	Vector (strings) of serial port names
   **/
    Vector getListOfPorts();

    /** Open port for read
   * @param	portName	Name of serial port to open
   * @param timeout Read timeout in millis
   * @return	Input stream
   * @exception	IOException	if cannot open port
   */
    InputStream openPortRead(String portName, int timeout) throws IOException;

    /** Open port for write
   * @param	portName	Name of serial port to open
   * @return	Output stream
   * @exception	IOException	if cannot open port
   */
    OutputStream openPortWrite(String portName) throws IOException;

    /** Set port characteristics
   * @param	baudRate	Baud rate bps
   * @param	stopBits	Number of stop bits
   * @param	dataBits	Number of data bits
   * @param	parity	Odd or even parity
   * @exception	IOException	if cannot set port
   */
    void setPort(int baudRate, int stopBits, int dataBits, int parity) throws IOException;

    /** Close port */
    void closePort();
}
