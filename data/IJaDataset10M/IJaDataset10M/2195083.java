package org.mbari.vcr;

import org.mbari.util.IObservable;
import org.mbari.util.IObserver;
import org.mbari.util.NumberUtil;
import org.mbari.util.ObservableComponent;
import java.util.Arrays;

/**
 * <p>Use to store replys from a sony VCR. The proper method for using this class is:</p>
 *
 * <pre>
 * vcrReply.update(command, cmd, data, checksum);
 * </pre>
 *
 * <p>It will check the checksum value and set the appropriate VCRStatus flags and
 * reply errors. If the checksum is bad it will hrow a SonyVCRException</p>
 *
 * <p>A reply consists of 3 parts. The first part is the cmd block, which consists
 * of 2 bytes. This block contains both the type of reply the VCR is sending and
 * the number of additional blocks of data that will follow. This may be followed
 * by a data block which can contain some anxillary information such as
 * a problem with the response (if the reply is a Nack), timecode information or
 * the VCR status. Finally the VCR finished its reply with a checksum byte. After
 * setting the parts of the reply, using setCmd, setData and setChecksum, a call
 * to update caues the VCRReply to update he approriate state objects such as
 * VCRTimecode, VCRState, and VCRError.</p>
 *
 * @author  : $Author: hohonuuli $
 * @version : $Revision: 3 $
 */
public class VCRReply implements IObservable {

    /** <!-- Field description --> */
    public static final byte[] ACK = { 0x10, 0x01 };

    /** <!-- Field description --> */
    public static final byte[] NACK = { 0x11, 0x12 };

    /** <!-- Field description --> */
    public static final byte[] STATUS_REPLY = { 0x73, 0x20 };

    /** Corrected LTC time data */
    public static final byte[] OTHER_STATUS_REPLY = { 0x71, 0x20 };

    private byte[] checksum;

    private byte[] cmd;

    /** The command submited to the VCR */
    private byte[] command;

    private byte[] data;

    private ObservableComponent oc = new ObservableComponent();

    private final VCRState vcrStatus = new VCRState();

    private final VCRTimecode vcrTimecode = new VCRTimecode();

    private final VCRError vcrError = new VCRError();

    private final VCRUserbits vcrUserbits = new VCRUserbits();

    /** The checksum of the command submitted to the VCR */
    private byte[] commandChecksum = new byte[1];

    /**
     * Adds an IObserver
     * @param observer Some instance of IObserver to be added
     */
    public void addObserver(IObserver observer) {
        oc.add(observer);
    }

    /**
     * The last byte in a command block is the checksum, i.e. the lower eight
     * bits of the sum of the other bytes in the command block
     * @param command The byte array to to calculate a checksum
     * @return The checksum value of the command
     */
    public static final byte calculateChecksum(byte[] command) {
        int temp = 0;
        for (int i = 0; i < command.length; i++) {
            temp += command[i];
        }
        return ((byte) temp);
    }

    /** @return The checksum value stored in the VCRReply */
    public byte[] getChecksum() {
        return checksum;
    }

    /** @return The command block of the reply */
    public byte[] getCmd() {
        return cmd;
    }

    /** @return The Command that was sent to the VCR */
    public byte[] getCommand() {
        return this.command;
    }

    /** @return The checksum of the command that was sent to the VCR. */
    public byte[] getCommandChecksum() {
        return this.commandChecksum;
    }

    /** @return Get the data block of the VCR reply. */
    public byte[] getData() {
        return data;
    }

    /**
     * @return The error object associated with this reply
     * @see org.mbari.vcr.VCRError
     */
    public VCRError getVcrError() {
        return vcrError;
    }

    /**
     * @return The state object associated with this reply
     * @see org.mbari.vcr.VCRState
     */
    public VCRState getVcrState() {
        return vcrStatus;
    }

    /**
     * @return The timecode object associated with this reply
     * @see org.mbari.vcr.VCRTimecode
     */
    public VCRTimecode getVcrTimecode() {
        return vcrTimecode;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public VCRUserbits getVcrUserbits() {
        return vcrUserbits;
    }

    /**
     * Checks to see if the reply is an ack (acknowledgement)
     * @return True if the reply is ACK
     */
    public boolean isAck() {
        return (Arrays.equals(cmd, ACK));
    }

    /**
     * Checks the checksum in the reply with the calculated checksum. Sets
     * the appropriate error status in the vCRStatus object.
     *
     * @return
     */
    private boolean isChecksumOK() {
        boolean OK = true;
        if (cmd == null) {
            OK = false;
        } else {
            byte[] cmdBlock;
            if ((data == null) || (data.length == 0)) {
                cmdBlock = new byte[cmd.length];
            } else {
                cmdBlock = new byte[cmd.length + data.length];
            }
            for (int i = 0; i < cmd.length; i++) {
                cmdBlock[i] = cmd[i];
            }
            if ((data != null) && (data.length > 0)) {
                for (int i = 2; i < cmdBlock.length; i++) {
                    cmdBlock[i] = data[i - 2];
                }
            }
            byte checksum2 = calculateChecksum(cmdBlock);
            if (checksum[0] != checksum2) {
                vcrError.setError(VCRError.ES_CHECKSUM);
                OK = false;
            } else {
                vcrError.setError(VCRError.ES_NOERROR);
                OK = true;
            }
        }
        return OK;
    }

    /**
     * Checks to see if the reply is a nack (i.e error)
     * @return True if the reply is NACK
     */
    public boolean isNack() {
        return (Arrays.equals(cmd, NACK));
    }

    /**
     * Checks to see if the reply is a reply to a Status sense (also called GET_STATUS
     * @return True if he reply contains a status update
     */
    public boolean isStatusReply() {
        return (Arrays.equals(cmd, STATUS_REPLY));
    }

    /**
     * Checks to see if he reply is a response to a timecode command
     * @return True if the reply is for a timecode
     */
    public boolean isTimecodeReply() {
        return VCRTimecode.isTimecodeReply(cmd);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public boolean isUserbitsReply() {
        return VCRUserbits.isUserbitsReply(cmd);
    }

    /** Notifies registered observers when the this objects state has changed. */
    public void notifyObservers() {
        oc.notify(this, "newVcrReply");
    }

    /** Remove all observers */
    public void removeAllObservers() {
        oc.clear();
    }

    /**
     * Remove an observer
     * @param observer The observer to be removed
     */
    public void removeObserver(IObserver observer) {
        oc.remove(observer);
    }

    /**
     * Update the ste objects associated with this VCRReply
     * @param command The command that was sent to the VCR
     * @param cmd The command portion of the VCR's reply
     * @param data The data portion of the VCR's reply
     * @param checksum The checksum of the VCR's reply
     *
     * @throws VCRException Thrown if the checksum supplied in the reply
     * does not match the checksum calculated from the cmd and data blocks.
     */
    public synchronized void update(byte[] command, byte[] cmd, byte[] data, byte[] checksum) throws VCRException {
        this.command = command;
        this.commandChecksum[0] = calculateChecksum(command);
        this.cmd = cmd;
        this.data = data;
        this.checksum = checksum;
        if (!isChecksumOK()) {
            throw new VCRException("invalid checksum");
        }
        if (isAck()) {
            vcrError.setError(VCRError.ES_NOERROR);
        } else if (isTimecodeReply()) {
            vcrTimecode.setTimecode(data);
        } else if (isUserbitsReply()) {
            String bs = "";
            for (int i = 0; i < cmd.length; i++) {
                bs = bs + cmd[i] + " ";
            }
            for (int i = 0; i < data.length; i++) {
                bs = bs + data[i] + " ";
            }
            for (int i = 0; i < checksum.length; i++) {
                bs = bs + checksum[i] + " ";
            }
            vcrUserbits.setUserbits(data);
        } else if (isStatusReply()) {
            vcrStatus.setStatus(NumberUtil.toLong(data));
            if (vcrStatus.isHardwareError()) {
                vcrError.setError(VCRError.ES_HW_ERROR);
            } else if (vcrStatus.isBadCommunication()) {
                vcrError.setError(VCRError.ES_BAD_COMM);
            }
        } else if (isNack()) {
            int nackData = (int) data[0];
            if ((nackData & 0x01) > 0) {
                vcrError.setError(VCRError.ES_INVALID);
            } else if ((nackData & 0x04) > 0) {
                vcrError.setError(VCRError.ES_CHECKSUM);
            } else if ((nackData & 0x10) > 0) {
                vcrError.setError(VCRError.ES_PARITY);
            } else if ((nackData & 0x20) > 0) {
                vcrError.setError(VCRError.ES_OVERRUN);
            } else if ((nackData & 0x40) > 0) {
                vcrError.setError(VCRError.ES_CHECKSUM);
            } else if ((nackData & 0x80) > 0) {
                vcrError.setError(VCRError.ES_SONY_TIMEOUT);
            }
        }
        notifyObservers();
    }
}
