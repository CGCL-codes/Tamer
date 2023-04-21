package net.sf.bacchus.record.validate;

import net.sf.bacchus.Addenda;
import net.sf.bacchus.Detail;
import net.sf.bacchus.InvalidRecordException;
import net.sf.bacchus.Record;
import net.sf.bacchus.record.process.SequenceException;
import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

public class TestAddendaSequenceValidator {

    private AddendaSequenceValidator test;

    @Before
    public void setUp() throws Exception {
        this.test = new AddendaSequenceValidator();
    }

    private void dispatchDetail(final int addenda) throws InvalidRecordException {
        final Detail detail = createMock(Detail.class);
        expect(detail.getType()).andReturn(Record.DETAIL_RECORD).anyTimes();
        expect(detail.getAddenda()).andReturn(addenda).atLeastOnce();
        replay(detail);
        this.test.dispatch(detail);
        verify(detail);
        assertEquals(addenda, this.test.getExpected());
    }

    private void dispatchAddenda(final int sequence) throws InvalidRecordException {
        final Addenda addenda = createMock(Addenda.class);
        expect(addenda.getType()).andReturn(Record.ADDENDA_RECORD).anyTimes();
        expect(addenda.getSequence()).andReturn(sequence).atLeastOnce();
        replay(addenda);
        this.test.dispatch(addenda);
        verify(addenda);
    }

    @Test
    public void testRestore() {
        final AddendaSequenceValidator restored = new AddendaSequenceValidator(7, 3);
        assertEquals(7, restored.getSequence());
        assertEquals(3, restored.getExpected());
    }

    @Test
    public void testExpectedDetail() throws InvalidRecordException {
        this.test.setExpected(0);
        dispatchDetail(3);
        assertEquals(3, this.test.getExpected());
    }

    @Test(expected = SequenceException.class)
    public void testUnexpectedDetail() throws InvalidRecordException {
        this.test.setExpected(1);
        dispatchDetail(1);
    }

    @Test
    public void testSequentialAddenda() throws InvalidRecordException {
        this.test.setExpected(2);
        dispatchAddenda(1);
        dispatchAddenda(2);
    }

    @Test(expected = SequenceException.class)
    public void testTooManyAddenda() throws InvalidRecordException {
        this.test.setExpected(1);
        dispatchAddenda(3);
        dispatchAddenda(4);
    }

    @Test(expected = SequenceException.class)
    public void testDuplicateAddendaSequence() throws InvalidRecordException {
        this.test.setExpected(2);
        dispatchAddenda(5);
        dispatchAddenda(5);
    }

    @Test(expected = SequenceException.class)
    public void testDescendingAddendaSequence() throws InvalidRecordException {
        this.test.setExpected(2);
        dispatchAddenda(5);
        dispatchAddenda(4);
    }

    @Test(expected = SequenceException.class)
    public void testSkippedAddendaSequence() throws InvalidRecordException {
        this.test.setExpected(2);
        dispatchAddenda(7);
        dispatchAddenda(9);
    }

    @Test(expected = SequenceException.class)
    public void testInvalidAddendaSequenceNumber() throws InvalidRecordException {
        this.test.setExpected(1);
        dispatchAddenda(0);
    }
}
