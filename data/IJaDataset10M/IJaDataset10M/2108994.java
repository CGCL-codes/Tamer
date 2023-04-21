package net.sourceforge.libairc;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Basically, this is all the stuff DCCSend and DCCReceive have in common
 * This is mainly here to reduce redundancy
 *
 * @author p-static
 */
public abstract class DCCTransfer extends Thread {

    /**
	 * The client this send is associated with
	 */
    protected Client client;

    /**
	 * Socket we're using
	 */
    protected Socket sock;

    /**
	 * User
	 */
    protected User user;

    /**
	 * Maximum Transfer Rate in bytes/second
	 */
    protected int maxTransferRate = 0;

    /**
	 * Actual Transfer Rate in bytes/second
	 */
    protected int actualTransferRate = 0;

    /**
	 * Length
	 */
    protected long length;

    /**
	 * Bytes Transferred
	 */
    protected int bytesTransferred;

    /**
	 * Place from which the transfer was resumed, or 0 if it was not
	 */
    protected long resumePosition;

    /**
	 * Start time
	 */
    protected long startTime;

    /**
	 * State of the transfer
	 */
    protected int state;

    /**
	 * Starting
	 */
    public static final int STATE_STARTING = 0;

    /**
	 * Waiting
	 */
    public static final int STATE_WAITING = 1;

    /**
	 * Active (transferring)
	 */
    public static final int STATE_ACTIVE = 2;

    /**
	 * Failed
	 */
    public static final int STATE_FAILED = 3;

    /**
	 * Timed out (failed?)
	 */
    public static final int STATE_TIMEOUT = 5;

    /**
	 * Completed
	 */
    public static final int STATE_COMPLETED = 8;

    /**
	 * Gets the status of the transfer
	 * 
	 * @return the status of the transfer
	 */
    public int getStatus() {
        return state;
    }

    /**
	 * Set the status
	 *
	 * @param status the new status
	 */
    public void setStatus(int status) {
        state = status;
    }

    /**
	 * Tells whether or not a transfer is still alive. For instance, STATE_WAITING or STATE_ACTIVE are alive, but STATE_COMPLETED isn't.
	 * 
	 * @return whether or not the send can still be considered active
	 */
    public boolean isActive() {
        return state == STATE_ACTIVE || state == STATE_WAITING || state == STATE_STARTING;
    }

    /**
	 * Gets the progress of the transfer.
	 * 
	 * @return A double, range 0-1, representing the fraction of the transfer completed
	 */
    public double getProgress() {
        return (double) (bytesTransferred + resumePosition) / length;
    }

    /**
	 * Gets the progress of the transfer.
	 * 
	 * @return the number of bytes that have so far been transferred(though not necessarily acknowledged)
	 */
    public long getBytesTransferred() {
        return bytesTransferred + resumePosition;
    }

    /**
	 * Gets the speed of the transfer
	 * 
	 * @return the transfer rate in bytes per second
	 */
    public double getTransferRate() {
        return ((double) bytesTransferred / ((System.currentTimeMillis() - startTime) / 1000));
    }

    /**
	 * Gets the maximum allowed speed of the transfer
	 * 
	 * @return the maximum allowed speed
	 */
    public int getMaxTransferRate() {
        return maxTransferRate;
    }

    /**
	 * Sets the maximum allowed speed of the transfer
	 * 
	 * @param speed the new maximum allowed speed, in bytes per second
	 */
    public void setMaxTransferRate(int speed) {
        maxTransferRate = speed;
    }

    /**
	 * Gets the user on the other end of the transfer
	 * 
	 * @return the user on the other end
	 */
    public User getUser() {
        return user;
    }

    /**
	 * Gets the client associated with this transfer
	 * 
	 * @return the client
	 */
    public Client getClient() {
        return client;
    }

    /**
	 * Sets the resume point of the transfer
	 *
	 * @param position the position from which to resume
	 */
    public void setResume(long position) {
        resumePosition = position;
    }

    /**
	 * Gets the resume point of the transfer
	 *
	 * @return position the position from which to resume
	 */
    public long getResume() {
        return resumePosition;
    }
}
