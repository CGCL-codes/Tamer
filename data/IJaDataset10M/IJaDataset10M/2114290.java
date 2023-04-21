package uk.org.ogsadai.resource.datasource;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import uk.org.ogsadai.activity.io.BufferedPipe;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.config.KeyValueUnknownException;
import uk.org.ogsadai.exception.InvalidSequenceNumberException;
import uk.org.ogsadai.resource.Resource;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceLifetime;
import uk.org.ogsadai.resource.ResourceState;
import uk.org.ogsadai.resource.delivery.SequenceNumberStatus;

/**
 * Simple implementation of a data source.
 *
 * @author The OGSA-DAI Project Team.
 */
public class SimpleDataSourceResource implements DataSourceResource, ResourceAccessor {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2010.";

    /** For storing and retrieving the sequence number. */
    private static final Key SEQUENCE_NUMBER_KEY = new Key("uk.org.ogsadai.previous.sequence.number");

    /** For storing and retrieving a data blocks. */
    private static final Key PREVIOUS_SET_OF_BLOCKS_KEY = new Key("uk.org.ogsadai.previous.set.of.blocks");

    /** For storing and retrieving previous result. */
    private static final Key PREVIOUS_RESULT_KEY = new Key("uk.org.ogsadai.previous.result");

    /** State of the data source. */
    private DataSourceResourceState mState;

    /** The pipe used to store the data. */
    private BufferedPipe mPipe;

    /**
     * Constructs a new simple data source.
     */
    public SimpleDataSourceResource() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceState getState() {
        return mState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceID getResourceID() {
        return mState.getResourceID();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final ResourceState resourceState) {
        if (!(resourceState instanceof DataSourceResourceState)) {
            throw new IllegalArgumentException("Resource state must be of type " + DataSourceResourceState.class.getName());
        }
        mState = (DataSourceResourceState) resourceState;
        if ((mState.getResourceLifetime().getTerminationTime() == null) && (mState.getDefaultTerminationTime() > 0)) {
            Calendar terminationTime = Calendar.getInstance();
            terminationTime.add(Calendar.MINUTE, mState.getDefaultTerminationTime());
            mState.getResourceLifetime().setTerminationTime(terminationTime);
            mState.setDefaultTerminationTime(0);
        }
        mState.setStatus(DataSourceStatus.WAIT);
        mPipe = createPipe();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumBlocksReadable() {
        return mPipe.getNumBlocksReadable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        mPipe.closeForWritingDueToError();
        ResourceLifetime resourceLifetime = mState.getResourceLifetime();
        resourceLifetime.setTerminationTime(Calendar.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceAccessor createResourceAccessor(final SecurityContext securityContext) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource getResource() {
        return this;
    }

    /**
     * Creates a pipe.
     * 
     * @return newly created pipe.
     */
    private BufferedPipe createPipe() {
        return new BufferedPipe("PipeForDataSource", mState.getBufferSize());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return mPipe.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeForReading() {
        mPipe.closeForReading();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object peek() throws PipeTerminatedException, DataError {
        return mPipe.peek();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object read() throws PipeTerminatedException, DataError {
        Object block;
        try {
            block = mPipe.read();
        } catch (PipeTerminatedException e) {
            mState.setStatus(DataSourceStatus.ERROR);
            throw e;
        } catch (DataError e) {
            mState.setStatus(DataSourceStatus.ERROR);
            throw e;
        }
        if (block == ControlBlock.NO_MORE_DATA) {
            mState.setStatus(DataSourceStatus.COMPLETE);
        } else {
            mState.setStatus(DataSourceStatus.TRANSIT);
            mState.incNumBlocks();
        }
        return block;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeForWriting() {
        mPipe.closeForWriting();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeForWritingDueToError() {
        mPipe.closeForWritingDueToError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Object block) throws PipeClosedException, PipeIOException, PipeTerminatedException {
        mPipe.write(block);
        mState.setStatus(DataSourceStatus.TRANSIT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean couldWriteBlock() {
        return mPipe.couldWriteBlock();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNBlocks(int n, long sequenceNumber, List<Object> dataOut) throws InvalidSequenceNumberException {
        return getNBlocks(n, sequenceNumber, true, dataOut);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNBlocksNB(int n, long sequenceNumber, List<Object> dataOut) throws InvalidSequenceNumberException {
        return getNBlocks(n, sequenceNumber, false, dataOut);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFully(List<Object> dataOut) {
        int result = DataSourceResource.NO_MORE_DATA;
        while (true) {
            try {
                Object block = read();
                if (block != ControlBlock.NO_MORE_DATA) {
                    dataOut.add(block);
                } else {
                    break;
                }
            } catch (DataError e) {
                result = DataSourceResource.DATA_ERROR;
                break;
            } catch (PipeTerminatedException e) {
                result = DataSourceResource.DATA_ERROR;
                break;
            } catch (Throwable e) {
                result = DataSourceResource.DATA_ERROR;
                break;
            }
        }
        return result;
    }

    /**
     * Checks for the validity of the sequence number.
     * 
     * @param sequenceNumber
     * @return Validity of the sequence number.
     */
    private SequenceNumberStatus validateSequenceNumber(long sequenceNumber) throws InvalidSequenceNumberException {
        SequenceNumberStatus validityOfsequenceNumber = SequenceNumberStatus.ERROR;
        long previousSequenceNumber = -1L;
        KeyValueProperties properties = mState.getConfiguration();
        try {
            previousSequenceNumber = (Long) properties.get(SEQUENCE_NUMBER_KEY);
        } catch (KeyValueUnknownException e) {
        }
        long expectedSequenceNumber = previousSequenceNumber + 1L;
        if (sequenceNumber <= previousSequenceNumber) {
            validityOfsequenceNumber = SequenceNumberStatus.IGNORE;
        } else if (sequenceNumber == expectedSequenceNumber) {
            validityOfsequenceNumber = SequenceNumberStatus.OK;
            properties.put(SEQUENCE_NUMBER_KEY, new Long(sequenceNumber));
        } else {
            throw new InvalidSequenceNumberException(expectedSequenceNumber, sequenceNumber);
        }
        return validityOfsequenceNumber;
    }

    /**
     * Get N blocks.
     * 
     * @param numberOfBlocks
     *     Number of blocks to get.
     * @param sequenceNumber
     *     Sequence number from client.
     * @param isBlocking
     *     Is this a blocking read?
     * @param dataOut
     *     Placeholder for blocks read.
     * @return number of blocks.
     * @throws InvalidSequenceNumberException
     *     If sequence number from client is invalid.
     */
    private int getNBlocks(int numberOfBlocks, long sequenceNumber, boolean isBlocking, List<Object> dataOut) throws InvalidSequenceNumberException {
        if (sequenceNumber != -1) {
            if (validateSequenceNumber(sequenceNumber) == SequenceNumberStatus.IGNORE) {
                return getCachedResult(dataOut);
            }
        }
        mPipe.setBufferSize(numberOfBlocks);
        Object block = null;
        List<Object> blocks = new LinkedList<Object>();
        int result = DataSourceResource.MORE_DATA;
        try {
            while (blocks.size() < numberOfBlocks && block != ControlBlock.NO_MORE_DATA && (isBlocking || getNumBlocksReadable() > 0)) {
                block = read();
                if (block == ControlBlock.NO_MORE_DATA) {
                    result = DataSourceResource.NO_MORE_DATA;
                } else {
                    blocks.add(block);
                }
            }
        } catch (DataError e) {
            result = DataSourceResource.DATA_ERROR;
        } catch (PipeTerminatedException e) {
            result = DataSourceResource.DATA_ERROR;
        } catch (Throwable e) {
            result = DataSourceResource.DATA_ERROR;
        }
        if (sequenceNumber != -1) {
            cacheResult(result, blocks);
        }
        dataOut.addAll(blocks);
        return result;
    }

    /**
     * Cache the result in case the next request is with the same sequence
     * number.
     * 
     * @param result  
     *     Result to cache.
     * @param blocks   
     *     Blocks to cache.
     */
    private void cacheResult(int result, List<Object> blocks) {
        KeyValueProperties properties = mState.getConfiguration();
        properties.put(PREVIOUS_RESULT_KEY, new Integer(result));
        properties.put(PREVIOUS_SET_OF_BLOCKS_KEY, blocks);
    }

    /**
     * Gets the cached result.
     * 
     * @param blocksOut 
     *     Block list to populated with the cached blocks.
     * @return the cached result of the previous request.
     */
    private int getCachedResult(List<Object> blocksOut) {
        KeyValueProperties properties = mState.getConfiguration();
        Integer result = (Integer) properties.get(PREVIOUS_RESULT_KEY);
        List<Object> blocks = (List<Object>) properties.get(PREVIOUS_SET_OF_BLOCKS_KEY);
        blocksOut.addAll(blocks);
        return result.intValue();
    }
}
