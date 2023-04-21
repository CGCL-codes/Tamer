package net.sf.bacchus.record.generate;

import net.sf.bacchus.InvalidRecordException;
import net.sf.bacchus.record.CompanyBatchControl;
import net.sf.bacchus.record.CompanyBatchHeader;
import net.sf.bacchus.record.RecordSupport;
import net.sf.bacchus.record.process.RecordSequencerSupport;

/**
 * Applies batch sequence numbers to a series of {@link CompanyBatchHeader} and
 * {@link CompanyBatchControl} records.
 */
public class BatchSequenceGenerator extends RecordSequencerSupport<Object, RecordSupport, Object, Object, RecordSupport, Object, Object> {

    /** Creates a batch sequence generator in default state. */
    public BatchSequenceGenerator() {
        super(Object.class, RecordSupport.class, Object.class, Object.class, RecordSupport.class, Object.class, Object.class, false);
    }

    /**
     * Creates a batch sequence generator in a state partially through a
     * processing cycle. Use this constructor to restart a generator that has
     * been stopped in the middle of a file.
     * @param sequence the most recent entry detail sequence number encountered.
     */
    public BatchSequenceGenerator(final int sequence) {
        super(sequence, Object.class, RecordSupport.class, Object.class, Object.class, RecordSupport.class, Object.class, Object.class, false);
    }

    /**
     * Increments the sequence value and sets the
     * {@link RecordSupport#setSequence(int) batch number} to the incremented
     * value.
     * @param header {@inheritDoc}
     */
    @Override
    protected void handleCompanyBatchHeader(final RecordSupport header) throws InvalidRecordException {
        header.setSequence(incrementSequence());
    }

    /**
     * Sets the {@link RecordSupport#setSequence(int) batch number} on the
     * control record to the batch number of the most recent company/batch
     * header record.
     * @param control {@inheritDoc}
     */
    @Override
    protected void handleCompanyBatchControl(final RecordSupport control) throws InvalidRecordException {
        control.setSequence(getSequence());
    }
}
