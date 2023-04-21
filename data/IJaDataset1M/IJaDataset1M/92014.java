package uk.org.ogsadai.dqp.lqp.operators;

import uk.org.ogsadai.dqp.execute.workflow.TupleSerialiser;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

/**
 * Exchanges data between OGSA-DAI services using a push mechanism (data sinks).
 * 
 * @author The OGSA-DAI Project Team.
 */
public class PushExchangeConsumerOperator extends UnaryOperator {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009";

    /** Data sink name for exchanging data. */
    private String mDataSink;

    /** Tuple serialiser. */
    private final TupleSerialiser mTupleSerialiser;

    /**
     * Constructor.
     * 
     * @param dataSinkID
     *            ID of data sink resource
     * @param tupleSerialiser
     *            tuple serialiser
     */
    public PushExchangeConsumerOperator(String dataSinkID, TupleSerialiser tupleSerialiser) {
        mID = OperatorID.PUSH_EXCHANGE_CONSUMER;
        mDataSink = dataSinkID;
        mTupleSerialiser = tupleSerialiser;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPartitionLeaf() {
        return true;
    }

    /**
     * Sets the data sink name.
     * 
     * @param id
     *            ID of the data sink
     */
    public void setDataSinkID(String id) {
        mDataSink = id;
    }

    /**
     * Returns the ID of the data sink.
     * 
     * @return data sink ID
     */
    public String getDataSinkID() {
        return mDataSink;
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException {
        super.update();
        mOperatorHeading = new HeadingImpl(mChildOperator.getHeading());
    }

    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Gets the tuple serialiser.
     * 
     * @return tuple serialiser
     */
    public TupleSerialiser getTupleSerialiser() {
        return mTupleSerialiser;
    }
}
