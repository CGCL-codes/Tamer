package org.jscsi.initiator.connection.state;

import java.nio.ByteBuffer;
import org.jscsi.initiator.connection.Connection;
import org.jscsi.parser.AbstractMessageParser;
import org.jscsi.parser.ProtocolDataUnit;
import org.jscsi.parser.data.DataInParser;
import org.jscsi.parser.datasegment.OperationalTextKey;
import org.jscsi.parser.exception.InternetSCSIException;
import org.jscsi.parser.exception.OperationalTextKeyException;
import org.jscsi.parser.scsi.SCSIResponseParser;
import org.jscsi.parser.scsi.SCSIStatus;

/**
 * <h1>ReadResponseState</h1>
 * <p/>
 * This state handles a Read Response.
 * 
 * @author Volker Wildi
 */
final class ReadResponseState extends AbstractState {

    /**
	 * This is the wrap around divisor (2**32) of the modulo operation used by
	 * incrementing the sequence numbers. See [RFC1982] for details.
	 */
    private static final int WRAP_AROUND_DIVISOR = (int) Math.pow(2, 32);

    /** The buffer to used for the message transfer. */
    private final ByteBuffer buffer;

    /** The start offset of the data to send. */
    private int bufferOffset;

    /** The expected data sequence number of the next response. */
    private int expectedDataSequenceNumber;

    /**
	 * Constructor to create a new, empty <code>ReadResponseState</code>.
	 * 
	 * @param initConnection
	 *            This is the connection, which is used for the network
	 *            transmission.
	 * @param initBuffer
	 *            The buffer, where the readed bytes are stored in.
	 * @param initBufferOffset
	 *            The start offset of the data to send.
	 * @param initExpectedDataSequenceNumber
	 *            The Expected Data Sequence Number of the next response
	 *            message.
	 */
    public ReadResponseState(final Connection initConnection, final ByteBuffer initBuffer, final int initBufferOffset, final int initExpectedDataSequenceNumber) {
        super(initConnection);
        buffer = initBuffer;
        bufferOffset = initBufferOffset;
        expectedDataSequenceNumber = initExpectedDataSequenceNumber;
    }

    /** {@inheritDoc} */
    public final void execute() throws InternetSCSIException {
        ProtocolDataUnit protocolDataUnit;
        do {
            protocolDataUnit = connection.receive();
            boolean dataWasRead = false;
            if (protocolDataUnit.getBasicHeaderSegment().getParser() instanceof DataInParser) {
                final DataInParser parser = (DataInParser) protocolDataUnit.getBasicHeaderSegment().getParser();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Remaining, DataSegmentLength: " + buffer.remaining() + ", " + protocolDataUnit.getBasicHeaderSegment().getDataSegmentLength());
                }
                final ByteBuffer dataSegment = protocolDataUnit.getDataSegment();
                while (buffer.hasRemaining() && dataSegment.hasRemaining()) {
                    buffer.put(dataSegment.get());
                }
                dataWasRead = true;
                if (parser.isStatusFlag() && parser.getStatus() == SCSIStatus.GOOD) {
                    return;
                } else if (connection.getSettingAsInt(OperationalTextKey.ERROR_RECOVERY_LEVEL) > 0 && parser.isAcknowledgeFlag()) {
                    connection.nextState(new SNACKRequestState(connection, this, parser.getTargetTaskTag()));
                    return;
                } else if (protocolDataUnit.getBasicHeaderSegment().getParser() instanceof SCSIResponseParser && !dataWasRead) {
                    readHandleImmediateData(protocolDataUnit);
                }
            }
        } while (!protocolDataUnit.getBasicHeaderSegment().isFinalFlag());
        if (connection.getSettingAsBoolean(OperationalTextKey.IMMEDIATE_DATA)) {
            return;
        } else {
            protocolDataUnit = connection.receive();
            if (protocolDataUnit.getBasicHeaderSegment().getParser() instanceof SCSIResponseParser) {
                readHandleImmediateData(protocolDataUnit);
            }
        }
    }

    private void readHandleImmediateData(final ProtocolDataUnit protocolDataUnit) throws InternetSCSIException {
        final SCSIResponseParser parser = (SCSIResponseParser) protocolDataUnit.getBasicHeaderSegment().getParser();
        final ByteBuffer dataSegment = protocolDataUnit.getDataSegment();
        while (buffer.hasRemaining() && dataSegment.hasRemaining()) {
            buffer.put(dataSegment.get());
        }
        if (parser.getStatus() == SCSIStatus.GOOD) {
            super.stateFollowing = false;
            return;
        } else {
            throw new InternetSCSIException();
        }
    }

    /** {@inheritDoc} */
    @Override
    public Exception isCorrect(final ProtocolDataUnit protocolDataUnit) {
        final AbstractMessageParser parser = protocolDataUnit.getBasicHeaderSegment().getParser();
        if (parser instanceof DataInParser) {
            final DataInParser dataParser = (DataInParser) parser;
            try {
                if (connection.getSettingAsBoolean(OperationalTextKey.DATA_PDU_IN_ORDER) && connection.getSettingAsBoolean(OperationalTextKey.DATA_SEQUENCE_IN_ORDER)) {
                    if (dataParser.getBufferOffset() < bufferOffset) {
                        return new IllegalStateException(new StringBuilder("This buffer offsets must be in increasing order and overlays are forbidden.").append(" The parserOffset here is ").append(dataParser.getBufferOffset()).append(" and the bufferOffset is ").append(bufferOffset).toString());
                    }
                    bufferOffset = dataParser.getBufferOffset();
                }
            } catch (OperationalTextKeyException e) {
                return e;
            }
            if (dataParser.getDataSequenceNumber() != expectedDataSequenceNumber) {
                return new IllegalStateException(new StringBuilder("Data Sequence Number Mismatch (received, expected): " + dataParser.getDataSequenceNumber() + ", " + expectedDataSequenceNumber).toString());
            }
            incrementExpectedDataSequenceNumber();
            if (dataParser.isStatusFlag()) {
                incrementExpectedDataSequenceNumber();
                return super.isCorrect(protocolDataUnit);
            } else if (dataParser.getStatusSequenceNumber() != 0) {
                return new IllegalStateException(new StringBuilder("Status Sequence Number must be zero.").toString());
            }
            return null;
        } else if (parser instanceof SCSIResponseParser) {
            try {
                if (connection.getSettingAsBoolean(OperationalTextKey.IMMEDIATE_DATA)) {
                    return new IllegalStateException(new StringBuilder("Parser ").append("should not be instance of SCSIResponseParser because of ImmendiateData-Flag \"no\" in config!").toString());
                }
            } catch (OperationalTextKeyException e) {
                return e;
            }
            return null;
        } else {
            return new IllegalStateException(new StringBuilder("Parser ").append(protocolDataUnit.getBasicHeaderSegment().getParser().toString()).append(" is instance of ").append(protocolDataUnit.getBasicHeaderSegment().getParser().getClass().toString()).append(" and not instance of either DataInParser or SCSIResponseParser!").toString());
        }
    }

    /**
	 * Increments the Expected Data Sequence Number counter.
	 */
    private void incrementExpectedDataSequenceNumber() {
        expectedDataSequenceNumber = (expectedDataSequenceNumber + 1) % WRAP_AROUND_DIVISOR;
    }
}
