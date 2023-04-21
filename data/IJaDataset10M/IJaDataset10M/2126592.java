package com.jpeterson.x10.module;

import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.jpeterson.util.HexFormat;
import com.jpeterson.x10.InterruptedTransmissionException;
import com.jpeterson.x10.TooManyAttemptsException;
import com.jpeterson.x10.module.event.CM11AStatusEvent;
import com.jpeterson.x10.module.event.CM11AStatusListener;

/**
 * Create a status request. The CM11A performs monitoring on a certain
 * house code. The status request downloads the status of the monitored
 * house code.
 *
 * @author Jesse Peterson <jesse@jpeterson.com>
 */
public class CM11AStatusTransmission extends Object implements CM11ATransmissionEvent {

    private int attempts;

    private int maxAttempts;

    private CM11A cm11a;

    private CM11AStatusListener listener;

    private static final byte STATUS_REQ = (byte) 0x8b;

    private static final int STATUS_SIZE = 14;

    /**
     * Create a standard CM11 transmission event to request the status
     * of the monitoring performed by the CM11 interface
     *
     * @param parent The CM11A device to retrieve the status of.
     * @param listener CM11AStatusListener to notify when status retrieved.
     *        May be null.
     *
     * @author Jesse Peterson <jesse@jpeterson.com>
     */
    public CM11AStatusTransmission(CM11A parent, CM11AStatusListener listener) {
        attempts = 0;
        setMaxAttempts(3);
        cm11a = parent;
        this.listener = listener;
    }

    /**
     * Transmit a CM11 status command.
     *
     * @param in Input stream to read from
     * @param out Output stream to write to
     * @exception TooManyAttemptsException Too many retries have occurred
     * @exception InterruptedTransmissionException An unsolicited interrupt
     *            has been received during the transmission.
     * @exception IOException Some sort of I/O or I/O protocol error has
     *            occurred
     *
     * @author Jesse Peterson <jesse@jpeterson.com>
     */
    public void transmit(InputStream in, OutputStream out) throws TooManyAttemptsException, InterruptedTransmissionException, EOFException, IOException {
        byte[] buffer = new byte[STATUS_SIZE];
        int numBytesRead = 0;
        CM11AStatusEvent event;
        HexFormat hex = new HexFormat();
        ++attempts;
        if (attempts > maxAttempts) {
            throw new TooManyAttemptsException();
        }
        if (System.getProperty("DEBUG") != null) {
            System.out.println("Sending CM11AStatusTransmission");
            System.out.println("PC->CM11A: 0x" + hex.format(STATUS_REQ));
        }
        out.write(STATUS_REQ);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        if (System.getProperty("DEBUG") != null) {
            System.out.println("Bytes available: " + in.available());
        }
        if (in.available() < STATUS_SIZE) {
            if (System.getProperty("DEBUG") != null) {
                System.out.println("Not enough bytes for a status message yet");
            }
            int result;
            byte byteValue;
            result = in.read();
            if (result == -1) {
                throw new EOFException("Unexpected end of stream indicator received while retrieving status.");
            }
            byteValue = (byte) result;
            if (System.getProperty("DEBUG") != null) {
                System.out.println("byte read: 0x" + hex.format(byteValue));
                System.out.println("Second chance bytes available: " + in.available());
            }
            if ((byteValue == CM11A.CM11_RECEIVE_EVENT) || (byteValue == CM11A.CM11_POWER_FAILURE) || (byteValue == CM11A.CM11_MACRO_INITIATED)) {
                throw new InterruptedTransmissionException(byteValue);
            } else if (in.available() >= (STATUS_SIZE - 1)) {
                if (System.getProperty("DEBUG") != null) {
                    System.out.println("Now, enough bytes are available.");
                }
                buffer[0] = byteValue;
                numBytesRead = in.read(buffer, 1, buffer.length - 1) + 1;
            } else {
                System.err.println("Breakdown in protocol, consuming all bytes in CM11AStatusTransmission.");
                while (in.available() > 0) {
                    in.read(buffer);
                }
                transmit(in, out);
                return;
            }
        } else {
            if (System.getProperty("DEBUG") != null) {
                System.out.println("Reading...");
            }
            numBytesRead = in.read(buffer);
        }
        if (System.getProperty("DEBUG") != null) {
            System.out.println("Checking number of bytes read...");
        }
        if (numBytesRead != STATUS_SIZE) {
            System.err.println("Invalid status buffer size.  Received " + numBytesRead + " bytes out of " + STATUS_SIZE + " bytes.");
            while (in.available() > 0) {
                in.read(buffer);
            }
            transmit(in, out);
            return;
        }
        if (System.getProperty("DEBUG") != null) {
            System.out.println("Calling method to decode status...");
        }
        event = cm11a.decodeStatus(buffer, 0, buffer.length);
        if ((listener != null) && (event != null)) {
            listener.status(event);
        }
    }

    /**
     * Retrieve the number of transmission attempts.
     *
     * @return the number of transmission attempts
     *
     * @author Jesse Peterson <jesse@jpeterson.com>
     */
    public int getNumAttempts() {
        return (attempts);
    }

    /**
     * Set the number of transmission attempts
     *
     * @param maxAttempts the maximum number of transmission attempts
     *
     * @author Jesse Peterson <jesse@jpeterson.com>
     */
    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    /**
     * Create a string representation of the transmission.
     *
     * @return String representation of the transmission.
     *
     * @author Jesse Peterson <jesse@jpeterson.com>
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CM11AStatusTransmission");
        return (buffer.toString());
    }
}
