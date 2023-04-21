package net.sf.bacchus.record.process;

import net.sf.bacchus.Record;

/**
 * Base class for verifying and generating record sequence numbers.
 * @param <FH> the type of file header record this supports.
 * @param <BH> the type of company/batch header record this supports.
 * @param <D> the type of detail record this supports.
 * @param <A> the type of addenda record this supports.
 * @param <BC> the type of company/bathch control record this supports.
 * @param <FC> the type of file control record this supports.
 * @param <F> the type of filler record this supports.
 */
public class RecordSequencerSupport<FH, BH, D, A, BC, FC, F> extends TypedRecordDispatcher<FH, BH, D, A, BC, FC, F> {

    /** the most recent sequence number encountered. */
    private int sequence;

    /**
     * Creates a sequence processor in default state.
     * @param fileHeader the expected type for a
     * {@link Record#FILE_HEADER_RECORD}.
     * @param batchHeader the expected type for a
     * {@link Record#COMPANY_BATCH_HEADER_RECORD}.
     * @param detail the expected type for a {@link Record#DETAIL_RECORD}.
     * @param addenda the expected type for an {@link Record#ADDENDA_RECORD}
     * @param batchControl the expected type for a
     * {@link Record#COMPANY_BATCH_CONTROL_RECORD}.
     * @param fileControl the expected type for a
     * {@link Record#FILE_CONTROL_RECORD}.
     * @param filler the expected type for a {@link Record#FILE_CONTROL_RECORD}
     * acting as a filler.
     * @param strict whether strict processing is enabled.
     */
    protected RecordSequencerSupport(final Class<FH> fileHeader, final Class<BH> batchHeader, final Class<D> detail, final Class<A> addenda, final Class<BC> batchControl, final Class<FC> fileControl, final Class<F> filler, final boolean strict) {
        this(0, fileHeader, batchHeader, detail, addenda, batchControl, fileControl, filler, strict);
    }

    /**
     * Creates a sequence processor in a state partially through a processing
     * cycle. Use this constructor to restart a processor that has been stopped
     * in the middle of a file.
     * @param sequence the most recent addenda sequence number encountered.
     * @param fileHeader the expected type for a
     * {@link Record#FILE_HEADER_RECORD}.
     * @param batchHeader the expected type for a
     * {@link Record#COMPANY_BATCH_HEADER_RECORD}.
     * @param detail the expected type for a {@link Record#DETAIL_RECORD}.
     * @param addenda the expected type for an {@link Record#ADDENDA_RECORD}
     * @param batchControl the expected type for a
     * {@link Record#COMPANY_BATCH_CONTROL_RECORD}.
     * @param fileControl the expected type for a
     * {@link Record#FILE_CONTROL_RECORD}.
     * @param filler the expected type for a {@link Record#FILE_CONTROL_RECORD}
     * acting as a filler.
     * @param strict whether strict processing is enabled.
     */
    protected RecordSequencerSupport(final int sequence, final Class<FH> fileHeader, final Class<BH> batchHeader, final Class<D> detail, final Class<A> addenda, final Class<BC> batchControl, final Class<FC> fileControl, final Class<F> filler, final boolean strict) {
        super(fileHeader, batchHeader, detail, addenda, batchControl, fileControl, filler, strict);
        this.sequence = sequence;
    }

    /**
     * Gets the most recent sequence number processed.
     * @return the most recent sequence number processed.
     */
    public int getSequence() {
        return this.sequence;
    }

    /**
     * Increments and returns the most recent sequence number processed.
     * @return the most recent sequence number processed.
     */
    public int incrementSequence() {
        return ++this.sequence;
    }

    /**
     * Sets the most recent sequence number encountered.
     * @param sequence the most recent sequence number encountered.
     */
    public void setSequence(final int sequence) {
        this.sequence = sequence;
    }
}
