package ds.nfcip;

import java.io.PrintStream;
import java.util.Vector;

/**
 * Abstract class that implements all functionality required by all
 * implementations of the NFCIPConnection class.
 * 
 * @author F. Kooman <F.Kooman@student.science.ru.nl>
 * 
 */
public abstract class NFCIPAbstract implements NFCIPInterface {

    private final byte[] END_BLOCK = { (byte) 0x04 };

    private final byte[] EMPTY_BLOCK = { (byte) 0x08 };

    private final byte[] DUMMY_BLOCK = { (byte) 0x88 };

    private final byte[] DUMMY_RESPONSE_BLOCK = { (byte) 0x89 };

    private static final int RECEIVE = 0;

    private static final int SEND = 1;

    /**
	 * temporary buffer for storing data from send() when in initiator mode.
	 */
    private byte[] receiveBuffer;

    /**
	 * backup for previously send data in case of connection problem where the
	 * previous data needs to be resent.
	 */
    private byte[] oldData;

    /**
	 * The log level.
	 */
    protected int logLevel;

    /**
	 * The stream to write log messages to.
	 */
    private PrintStream ps;

    /**
	 * The maximum block size in bytes to use for individual blocks.
	 * 
	 * The block size has to be at least 2 bytes (one byte for the chaining
	 * indicator and one for the actual data being sent or received).
	 */
    protected int blockSize;

    /**
	 * The block number currently expected
	 */
    private byte expectedBlockNumber;

    /**
	 * The mode of operation of the NFCIPConnection (either
	 * <code>INITIATOR</code>, <code>TARGET</code>, <code>FAKE_INITIATOR</code>
	 * or <code>FAKE_TARGET</code>).
	 */
    private int mode;

    /**
	 * The expected operation, either <code>SEND</code> or <code>RECEIVE</code>.
	 */
    private int transmissionMode;

    /**
	 * Counts the number of connection resets required to complete the
	 * transmission.
	 */
    private int numberOfResets;

    /**
	 * The number of bytes sent so far.
	 */
    private int noOfSentBytes;

    /**
	 * The number of bytes received so far.
	 */
    private int noOfReceivedBytes;

    /**
	 * The number of sent messages so far.
	 */
    private int noOfSentMessages;

    /**
	 * The number of received messages so far.
	 */
    private int noOfReceivedMessages;

    /**
	 * The number of sent blocks so far.
	 */
    private int noOfSentBlocks;

    /**
	 * The number of received blocks so far.
	 */
    private int noOfReceivedBlocks;

    protected NFCIPAbstract() {
        mode = -1;
        oldData = null;
        ps = null;
        blockSize = 100;
        expectedBlockNumber = 0;
        numberOfResets = 0;
        noOfSentBytes = 0;
        noOfReceivedBytes = 0;
        noOfSentMessages = 0;
        noOfReceivedMessages = 0;
        noOfSentBlocks = 0;
        noOfReceivedBlocks = 0;
    }

    /**
	 * Set the mode of operation (either <code>INITIATOR</code>,
	 * <code>TARGET</code>, <code>FAKE_INITIATOR</code> or
	 * <code>FAKE_TARGET</code>).
	 * 
	 * @param mode
	 *            the mode (<code>INITIATOR</code>, <code>TARGET</code>,
	 *            <code>FAKE_INITIATOR</code> or <code>FAKE_TARGET</code>)
	 * @throws NFCIPException
	 *             if the mode is invalid or setting the mode fails
	 */
    public void setMode(int mode) throws NFCIPException {
        this.mode = mode;
        logMessage(1, "Setting mode: " + NFCIPUtils.modeToString(mode));
        switch(mode) {
            case INITIATOR:
                setInitiatorMode();
                transmissionMode = SEND;
                break;
            case TARGET:
                setTargetMode();
                transmissionMode = RECEIVE;
                break;
            case FAKE_INITIATOR:
                setTargetMode();
                byte[] recv = receiveCommand();
                if (!NFCIPUtils.isDummyBlock(recv)) throw new NFCIPException("no dummy block received");
                transmissionMode = SEND;
                break;
            case FAKE_TARGET:
                setInitiatorMode();
                sendCommand(DUMMY_BLOCK);
                transmissionMode = RECEIVE;
                break;
            default:
                throw new NFCIPException("wrong mode specified");
        }
    }

    public int getMode() throws NFCIPException {
        if (mode < 0) throw new NFCIPException("no mode selected");
        return mode;
    }

    /**
	 * Set the log level.
	 * 
	 * @param ps
	 *            the stream to write the logging to (can be
	 *            <code>System.out</code>, or <code>null</code>)
	 * @param logLevel
	 *            the level on which log messages become visible in the log (0 =
	 *            nothing appears, 5 = maximum detail)
	 */
    public void setLogging(PrintStream ps, int logLevel) {
        this.logLevel = logLevel;
        this.ps = ps;
    }

    /**
	 * Print a message in the log stream.
	 * 
	 * @param logThreshold
	 *            the minimum level of <code>logLevel</code> to display this
	 *            message
	 * @param message
	 *            the message to log
	 */
    protected void logMessage(int logThreshold, String message) {
        if (ps == null) return;
        if (logLevel >= logThreshold) {
            if (logLevel >= 3) ps.println("[DEBUG]" + message); else ps.println("[INFO] " + message);
        }
    }

    public void send(byte[] data) throws NFCIPException {
        if (transmissionMode != SEND) throw new NFCIPException("expected receive");
        noOfSentBytes += data != null ? data.length : 0;
        noOfSentMessages++;
        if (mode == INITIATOR || mode == FAKE_INITIATOR) sendInitiator(data); else sendTarget(data);
        transmissionMode = RECEIVE;
    }

    private void sendInitiator(byte[] data) {
        Vector v = NFCIPUtils.dataToBlockVector(data, blockSize);
        for (int i = 0; i < v.size(); i++) {
            sendBlock((byte[]) v.elementAt(i));
        }
    }

    private void sendTarget(byte[] data) {
        Vector v = NFCIPUtils.dataToBlockVector(data, blockSize);
        for (int i = 0; i < v.size(); i++) {
            sendBlock((byte[]) v.elementAt(i));
        }
        endBlockTarget();
    }

    private void sendBlock(byte[] data) {
        noOfSentBlocks++;
        if (mode == INITIATOR || mode == FAKE_INITIATOR) sendBlockInitiator(data); else sendBlockTarget(data);
    }

    private void sendBlockInitiator(byte[] data) {
        try {
            if (NFCIPUtils.isChained(data)) {
                sendCommand(data);
                receiveCommand();
            } else {
                sendCommand(data);
                receiveBuffer = receiveCommand();
            }
        } catch (NFCIPException e) {
            logMessage(1, e.getMessage());
            resetMode();
            sendBlockInitiator(data);
        }
    }

    private void sendBlockTarget(byte[] data) {
        sendBlockTarget(data, true);
    }

    private void sendBlockTarget(byte[] data, boolean backupData) {
        if (backupData) oldData = data;
        try {
            sendCommand(data);
            if (NFCIPUtils.isChained(data)) {
                byte[] checkForNullBlock = receiveCommand();
                if (NFCIPUtils.isNullBlock(checkForNullBlock)) throw new NFCIPException("empty block");
            }
        } catch (NFCIPException e) {
            logMessage(1, e.getMessage());
            resetMode();
            receiveBlockTarget();
            sendBlockTarget(data, false);
        }
    }

    public byte[] receive() throws NFCIPException {
        if (transmissionMode != RECEIVE) throw new NFCIPException("expected send");
        expectedBlockNumber = 0;
        byte[] res;
        if (mode == INITIATOR || mode == FAKE_INITIATOR) res = receiveInitiator(); else res = receiveTarget();
        transmissionMode = SEND;
        noOfReceivedMessages++;
        noOfReceivedBytes += res != null ? res.length : 0;
        return res;
    }

    private byte[] receiveInitiator() {
        Vector responses = new Vector();
        byte[] result = receiveBlock();
        responses.addElement(result);
        expectedBlockNumber = (byte) ((expectedBlockNumber + 1) % 2);
        while (NFCIPUtils.isChained(result)) {
            result = receiveBlock();
            if (NFCIPUtils.getBlockNumber(result) == expectedBlockNumber) {
                responses.addElement(result);
                expectedBlockNumber = (byte) ((expectedBlockNumber + 1) % 2);
            } else {
                logMessage(2, "unexpected block received");
            }
        }
        endBlockInitiator();
        return NFCIPUtils.blockVectorToData(responses);
    }

    private byte[] receiveTarget() {
        Vector responses = new Vector();
        byte[] result = receiveBlock();
        responses.addElement(result);
        while (NFCIPUtils.isChained(result)) {
            sendBlock(EMPTY_BLOCK);
            expectedBlockNumber = (byte) ((expectedBlockNumber + 1) % 2);
            result = receiveBlock();
            responses.addElement(result);
        }
        return NFCIPUtils.blockVectorToData(responses);
    }

    private byte[] receiveBlock() {
        byte[] res;
        if (mode == INITIATOR || mode == FAKE_INITIATOR) res = receiveBlockInitiator(); else res = receiveBlockTarget();
        logMessage(3, "receiveBlock: " + NFCIPUtils.byteArrayToString(res));
        noOfReceivedBlocks += 1;
        return res;
    }

    private byte[] receiveBlockInitiator() {
        byte[] returnBuffer = receiveBuffer;
        if (NFCIPUtils.isChained(returnBuffer)) {
            try {
                sendCommand(EMPTY_BLOCK);
                receiveBuffer = receiveCommand();
            } catch (NFCIPException e) {
                logMessage(1, e.getMessage());
                resetMode();
                return receiveBlockInitiator();
            }
        }
        return returnBuffer;
    }

    private byte[] receiveBlockTarget() {
        byte[] resultBuffer;
        try {
            resultBuffer = receiveCommand();
            if (NFCIPUtils.isNullBlock(resultBuffer)) throw new NFCIPException("empty block");
        } catch (NFCIPException e) {
            logMessage(1, e.getMessage());
            resetMode();
            return receiveBlockTarget();
        }
        if (NFCIPUtils.isEmptyBlock(resultBuffer)) {
            return EMPTY_BLOCK;
        } else if (NFCIPUtils.isEndBlock(resultBuffer)) {
            logMessage(3, "end block received");
            sendBlock(END_BLOCK);
            return receiveBlockTarget();
        } else if (NFCIPUtils.getBlockNumber(resultBuffer) == expectedBlockNumber) {
            return resultBuffer;
        } else if (resultBuffer != null && resultBuffer.length != 0) {
            logMessage(2, "unexpected block received");
            sendBlock(oldData);
            return receiveBlockTarget();
        } else {
            logMessage(0, "we received an empty message here, impossible");
            return null;
        }
    }

    private void endBlockInitiator() {
        try {
            logMessage(3, "sending end block");
            sendCommand(END_BLOCK);
            receiveCommand();
        } catch (NFCIPException e) {
            logMessage(1, e.getMessage());
            resetMode();
            endBlockInitiator();
        }
    }

    private void endBlockTarget() {
        byte[] data = null;
        try {
            data = receiveCommand();
            if (NFCIPUtils.isNullBlock(data)) throw new NFCIPException("empty block");
            if (NFCIPUtils.isEndBlock(data)) {
                sendCommand(data);
            } else {
                sendBlock(oldData);
                endBlockTarget();
            }
        } catch (NFCIPException e) {
            logMessage(1, e.getMessage());
            resetMode();
            endBlockTarget();
        }
    }

    private void resetMode() {
        logMessage(2, "Resetting connection...");
        numberOfResets++;
        try {
            releaseTargets();
            setMode(mode);
        } catch (NFCIPException e) {
            logMessage(1, e.getMessage());
        }
    }

    public void close() throws NFCIPException {
        logMessage(2, "Closing connection...");
        if (mode == FAKE_INITIATOR) sendCommand(DUMMY_RESPONSE_BLOCK);
        releaseTargets();
        rawClose();
    }

    public int getNumberOfResets() {
        return numberOfResets;
    }

    public int getNumberOfReceivedMessages() {
        return noOfReceivedMessages;
    }

    public int getNumberOfSentMessages() {
        return noOfSentMessages;
    }

    public int getNumberOfReceivedBlocks() {
        return noOfReceivedBlocks;
    }

    public int getNumberOfSentBlocks() {
        return noOfSentBlocks;
    }

    public int getNumberOfSentBytes() {
        return noOfSentBytes;
    }

    public int getNumberOfReceivedBytes() {
        return noOfReceivedBytes;
    }

    /**
	 * Low level send of a block of maximum size <code>blockSize</code>.
	 * 
	 * @param data
	 *            the block to send
	 * @throws NFCIPException
	 *             if sending failed
	 */
    protected abstract void sendCommand(byte[] data) throws NFCIPException;

    /**
	 * Low level receive of a block of maximum size <code>blockSize</code>.
	 * 
	 * @return the block data
	 * @throws NFCIPException
	 *             if receiving failed
	 */
    protected abstract byte[] receiveCommand() throws NFCIPException;

    /**
	 * Set the NFCIP device in <code>INITIATOR</code> mode.
	 * 
	 * @throws NFCIPException
	 *             if setting the mode fails
	 */
    protected abstract void setInitiatorMode() throws NFCIPException;

    /**
	 * Set the NFCIP device in <code>TARGET</code> mode.
	 * 
	 * @throws NFCIPException
	 *             if setting the mode fails
	 */
    protected abstract void setTargetMode() throws NFCIPException;

    /**
	 * Close the connection.
	 * 
	 * @throws NFCIPException
	 */
    protected abstract void rawClose() throws NFCIPException;

    /**
	 * Release the target(s) the initiator has an established connection with.
	 * 
	 * @throws NFCIPException
	 *             if releasing the target(s) fails
	 */
    protected abstract void releaseTargets() throws NFCIPException;
}
