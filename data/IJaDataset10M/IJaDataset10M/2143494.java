package white440.truck;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * A class for controlling a PSC servo control board.
 * Based heavily on the one developed by Oranuj
 * @author evan, Oranuj Janrathitikarn
 *
 */
public class ServoControlBoard {

    private SerialPort port = null;

    private OutputStream outStream = null;

    private InputStream inStream = null;

    /**
	 * Connects to the PSC servo control board at commPort and prepares and prepares 
	 * it to receive commands.
	 * @param CommPort identifier in the form "COMM#" where # is a number
	 */
    public ServoControlBoard(int commPortNumber) {
        String commPort = "COM" + commPortNumber;
        try {
            port = (SerialPort) CommPortIdentifier.getPortIdentifier(commPort).open("PSCBoard", 2000);
            port.setSerialPortParams(2400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            outStream = port.getOutputStream();
            inStream = port.getInputStream();
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Set one servo to the specified angle.
	 * @param servoNum the channel the servo is on: [0,31]
	 * @param angle the position to set the servo to [-90,90]. 
	 * -90 is far left, 0 is centered and 90 is far right.
	 * @throws ServoNotSetException if the servo could not be set
	 */
    public void setServoPosition(int servoNum, int angle) throws ServoNotSetException {
        if (angle < -90 || angle > 90) {
            throw new ServoNotSetException(new IllegalArgumentException("the servo angle must be in the range [-90,90]"));
        }
        short pos = (short) (750 + angle * (500. / 90));
        try {
            setServoParameters((short) servoNum, (short) 0, pos);
        } catch (IOException e) {
            throw new ServoNotSetException("servo " + servoNum + "was not set to the position" + angle, e);
        }
    }

    /**
	 * writes four bytes of data to the board using the correct 
	 * formatting required by serial port.
	 * NOTE: use this method instead of writing directly to the outputStream
	 * @param one data byte
	 * @param two data byte
	 * @param three data byte
	 * @param four data byte
	 * @throws IOException  if the method cannot communicate with the board
	 */
    private synchronized void writeOut(byte one, byte two, byte three, byte four) throws IOException {
        System.out.println("writing to board: " + one + two + three + four);
        outStream.write('!');
        outStream.write('S');
        outStream.write('C');
        byte[] bytes = { one, two, three, four };
        outStream.write(bytes);
        outStream.write(0x0D);
    }

    /**
	 * Sets the servo on channel servoNumber to a position using a 
	 * ramp factor to control speed.
	 * 
	 * <p>The sevoNumber parameter is a number 0-31 corresponding to the servo channel
	 *  number. The servo channel number should be 0-15 with no jumper present on the PSC, 
	 *  or 16-31 with the jumper present. With a jumper present on the PSC, servo channel 0 
	 *  become channel 16, channel 1 becomes 17, etc.</p>
	 *  <p>The ramp parameter is a number number 0 � 63 that controls the ramp function for 
	 *  each channel. If the ramp parameter is set to 0, ramping is disabled and the pulse 
	 *  width will be set to the P parameter sent immediately. Ramp values of 1-63 correspond 
	 *  to speeds from 3/4 of a second up to 60 seconds for a full 500 micros to 2.50 ms excursion 
	 *  for standard servos. This correlation is rather linear though no equation presently 
	 *  exists.</p>
	 *  <p>The position parameter number that corresponds to the desired servo position. 
	 *  The range, (250-1250), corresponds to 0 to 180 degrees of servo rotation with each 
	 *  step equaling 2 micros.</p>
	 * @param servoNumber the channel that the servo is connected to
	 * @param ramp controls how fast the servo moves to the desired position
	 * @param position the position to set the servo to
	 * @throws IOException if the board is out of communication
	 */
    private void setServoParameters(short servoNumber, short ramp, short position) throws IOException {
        if (servoNumber < 0 || servoNumber > 31) {
            throw new IllegalArgumentException("The servo number must be between 0 and 31");
        }
        if (ramp < 0 || ramp > 63) {
            throw new IllegalArgumentException("the ramp value must be between 0 and 63");
        }
        if (position < 250 || position > 1250) {
            throw new IllegalArgumentException("the position must be between 250 and 1250");
        }
        writeOut((new Short(servoNumber)).byteValue(), (new Short(ramp)).byteValue(), lowByte(position), highByte(position));
    }

    /**
	 * Separate the 4th byte of an int for communicating with PSCBoard
	 * @author Oranuj Janrathitikarn
	 * @param myInt
	 * @return the lowbyte of an int
	 */
    private byte lowByte(int myInt) {
        myInt = myInt << 24;
        myInt = myInt >> 24;
        return (byte) myInt;
    }

    /**
	 * Separate the 3rd byte of an int for communicating with PSCBoard
	 * @author Oranuj Janrathitikarn
	 * @param myInt
	 * @return the high byte of an int
	 */
    private byte highByte(int myInt) {
        myInt = myInt >>> 8;
        return (byte) myInt;
    }

    public void finalize() {
        try {
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        port.close();
    }
}
