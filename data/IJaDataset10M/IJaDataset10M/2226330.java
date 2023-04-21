package net.sf.bacchus.record.process;

import net.sf.bacchus.Addenda;
import net.sf.bacchus.Detail;

/**
 * Base class for verifying and generating addenda sequence numbers.
 * @param <A> the type of addenda record this supports.
 * @param <D> the type of detail record this supports.
 */
public class AddendaSequencerSupport<D, A> extends RecordSequencerSupport<Object, Object, D, A, Object, Object, Object> {

    /** the number of addenda records expected before the next detail or reset. */
    private int expected;

    /**
     * Creates an addenda sequence processor in default state.
     * @param detail the detail class.
     * @param addenda the addenda class.
     * @param strict whether exceptions should be thrown for invalid records.
     */
    public AddendaSequencerSupport(final Class<D> detail, final Class<A> addenda, final boolean strict) {
        this(0, 0, detail, addenda, strict);
    }

    /**
     * Creates an addenda sequence processor in a state partially through a
     * processing cycle. Use this constructor to restart a processor that has
     * been stopped in the middle of a file.
     * @param last the most recent addenda sequence number encountered.
     * @param expected the number of addenda records expected before the next
     * detail or reset.
     * @param detail the detail class.
     * @param addenda the addenda class.
     * @param strict whether exceptions should be thrown for invalid records and
     * {@link Addenda} records that are not expected based on the most recent
     * {@link Detail#getAddenda() addenda count}.
     */
    public AddendaSequencerSupport(final int last, final int expected, final Class<D> detail, final Class<A> addenda, final boolean strict) {
        super(last, Object.class, Object.class, detail, addenda, Object.class, Object.class, Object.class, strict);
        this.expected = expected;
    }

    /**
     * Gets the number of addenda records expected before the next detail or
     * reset.
     * @return the number of addenda records expected before the next detail or
     * reset.
     */
    public int getExpected() {
        return this.expected;
    }

    /**
     * Decrements and returns the number of addenda records expected.
     * @return the number of addenda records expected.
     */
    public int decrementExpected() {
        return --this.expected;
    }

    /**
     * Sets the number of addenda records expected before the next detail or
     * reset.
     * @param expected the number of addenda records expected before the next
     * detail or reset.
     */
    public void setExpected(final int expected) {
        this.expected = expected;
    }

    /**
     * Resets and checks that an addenda was not expected here.
     * @param record {@inheritDoc}
     * @throws SequenceException if an addenda was expected.
     */
    @Override
    protected void handleDefault(final Object record) throws SequenceException {
        setSequence(0);
        final int addendaExpected = getExpected();
        if (addendaExpected == 0) return;
        setExpected(0);
        if (isStrict() && addendaExpected > 0) throw new SequenceException("Expected addenda record");
    }
}
