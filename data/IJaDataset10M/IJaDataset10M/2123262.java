package simpledb;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

public class TupleTest {

    /**
     * Unit test for Tuple.getField() and Tuple.setField()
     */
    @Test
    public void modifyFields() {
        TupleDesc td = Utility.getTupleDesc(2);
        Tuple tup = new Tuple(td);
        tup.setField(0, new IntField(-1));
        tup.setField(1, new IntField(0));
        assertEquals(new IntField(-1), tup.getField(0));
        assertEquals(new IntField(0), tup.getField(1));
        tup.setField(0, new IntField(1));
        tup.setField(1, new IntField(37));
        assertEquals(new IntField(1), tup.getField(0));
        assertEquals(new IntField(37), tup.getField(1));
    }

    /**
     * Unit test for Tuple.getTupleDesc()
     */
    @Test
    public void getTupleDesc() {
        TupleDesc td = Utility.getTupleDesc(5);
        Tuple tup = new Tuple(td);
        assertEquals(td, tup.getTupleDesc());
    }

    /**
     * Unit test for Tuple.getRecordID() and Tuple.setRecordID()
     */
    @Test
    public void modifyRecordID() {
        Tuple tup1 = new Tuple(Utility.getTupleDesc(1));
        HeapPageId pid1 = new HeapPageId(0, 0);
        RecordID rid1 = new RecordID(pid1, 0);
        tup1.setRecordID(rid1);
        assertEquals(rid1, tup1.getRecordID());
    }

    /**
     * JUnit suite target
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TupleTest.class);
    }
}
