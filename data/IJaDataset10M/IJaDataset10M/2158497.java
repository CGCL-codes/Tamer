package upm.fi.oeg.sparql.lqp.activities;

import java.util.Iterator;
import java.util.List;
import uk.org.ogsadai.activity.ActivityBase;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityInputsException;
import uk.org.ogsadai.activity.UnmatchedActivityInputsException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.expression.IncomparableTypesException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;
import uk.org.ogsadai.tuple.join.ConfigurationException;

public class TupleSparqlUnionActivity extends ActivityBase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) Universidad Politecnica de Madrid, 2011.";

    /** Logger object for logging in this class. */
    private static final DAILogger LOG = DAILogger.getLogger(TupleSparqlUnionActivity.class);

    /**
     * Activity input name (<code>data</code>) - Data sets. (OGSA-DAI tuple
     * list).
     */
    public static final String INPUT_DATA = "data";

    /**
     * Activity output name (<code>data</code>) - Data set after union.
     * (OGSA-DAI tuple list).
     */
    public static final String OUTPUT_DATA = "data";

    /** All inputs. */
    private BlockReader[] mInputs;

    /** Activity output. */
    private BlockWriter mOutput;

    protected SparqlUnion mUnion;

    protected TupleListActivityInput mInputDataLeft;

    protected TupleListActivityInput mInputDataRight;

    /**
     * Sets up inputs and outputs.
     * 
     * @throws ActivityUserException
     *             if there is a user-related exception.
     * @throws ActivityProcessingException
     *             if there is an activity processing exception.
     * @throws ActivityTerminatedException
     *             if the activity terminated.
     */
    private void preprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        mUnion = new SparqlUnion();
        List inputs = getInputs(INPUT_DATA);
        if (inputs.size() < 1) {
            throw new InvalidActivityInputsException(inputs.size(), INPUT_DATA);
        }
        mInputs = (BlockReader[]) inputs.toArray(new BlockReader[inputs.size()]);
        validateOutput(OUTPUT_DATA);
        mOutput = getOutput();
    }

    /**
     * {@inheritDoc}
     */
    public void process() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        preprocess();
        while (hasNextList()) {
            LOG.debug("SPARQL union activity started");
            mInputDataLeft = new TupleListActivityInput("data");
            mInputDataLeft.setBlockReader(mInputs[0]);
            mInputDataRight = new TupleListActivityInput("data");
            mInputDataRight.setBlockReader(mInputs[1]);
            TupleListIterator streamedTuples;
            TupleListIterator storedTuples;
            TupleMetadata leftMetadata;
            TupleMetadata rightMetadata;
            streamedTuples = (TupleListIterator) mInputDataLeft.read();
            leftMetadata = (TupleMetadata) streamedTuples.getMetadataWrapper().getMetadata();
            storedTuples = (TupleListIterator) mInputDataRight.read();
            rightMetadata = (TupleMetadata) storedTuples.getMetadataWrapper().getMetadata();
            try {
                TupleMetadata unionMetadata = null;
                unionMetadata = mUnion.configure(leftMetadata, rightMetadata);
                LOG.debug("Reading and storing RIGHT input data stream.");
                mUnion.storeTuples(storedTuples);
                union(streamedTuples, new MetadataWrapper(unionMetadata));
            } catch (PipeClosedException e) {
                e.printStackTrace();
            } catch (ColumnNotFoundException e) {
                e.printStackTrace();
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            } catch (IncomparableTypesException e) {
                e.printStackTrace();
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
            LOG.debug("SPARQL union activity completed");
        }
    }

    /**
     * Checks that all inputs are either closed or open.
     * 
     * @return <code>true</code> if another tuple list is available,
     *         <code>false</code> otherwise.
     * 
     * @throws ActivityUserException
     *             if there is a user-related exception.
     * @throws ActivityProcessingException
     *             if there is an activity processing exception.
     * @throws ActivityTerminatedException
     *             if the activity terminated.
     */
    private boolean hasNextList() throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException {
        int closed = 0;
        Object[] blocks = new Object[mInputs.length];
        String[] names = new String[mInputs.length];
        for (int i = 0; i < mInputs.length; i++) {
            try {
                Object block = mInputs[i].peek();
                if (block == ControlBlock.NO_MORE_DATA) {
                    closed++;
                }
                blocks[i] = block;
                names[i] = INPUT_DATA;
            } catch (PipeIOException e) {
                throw new ActivityProcessingException(e);
            } catch (PipeTerminatedException e) {
                throw new ActivityTerminatedException();
            }
        }
        if (closed > 0 && closed < mInputs.length) {
            throw new UnmatchedActivityInputsException(names, blocks);
        } else {
            return (closed == 0);
        }
    }

    /**
     * Writes a block to the output and throws appropriate exceptions if an
     * error occurs.
     * 
     * @param block
     *            block to write.
     * @throws PipeClosedException
     *             if the pipe is closed.
     * @throws ActivityProcessingException
     *             if there is an activity processing exception.
     * @throws ActivityTerminatedException
     *             if the activity terminated.
     */
    protected synchronized void writeBlock(Object block) throws ActivityTerminatedException, ActivityProcessingException, PipeClosedException {
        if (Thread.interrupted()) {
            return;
        }
        try {
            mOutput.write(block);
        } catch (PipeIOException e) {
            throw new ActivityProcessingException(e);
        } catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        }
    }

    private void union(TupleListIterator streamedTuples, MetadataWrapper unionMetadata) throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException, PipeClosedException {
        LOG.debug("Starting join.");
        writeBlock(ControlBlock.LIST_BEGIN);
        writeBlock(unionMetadata);
        Tuple tuple;
        LOG.debug("Streaming input tuples.");
        while ((tuple = (Tuple) streamedTuples.nextValue()) != null) {
            boolean noCandidates = true;
            Iterable<Tuple> unionIterator = mUnion.union(tuple);
            for (Tuple unioned : unionIterator) {
                LOG.debug(unioned.toString());
                writeBlock(unioned);
                noCandidates = false;
            }
            if (noCandidates) {
                writeBlock(mUnion.createUnionTuple(tuple, true));
            }
            for (Object key : mUnion.getRestKey()) {
                Iterator restIt = mUnion.getNotMatchedTuples(key);
                while (restIt.hasNext()) {
                    writeBlock(mUnion.createUnionTuple((Tuple) restIt.next(), false));
                }
            }
        }
        writeBlock(ControlBlock.LIST_END);
        LOG.debug("Completed union.");
    }
}
