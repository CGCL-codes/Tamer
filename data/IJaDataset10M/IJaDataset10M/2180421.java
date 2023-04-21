package net.sf.bacchus.record;

import java.math.BigDecimal;
import java.math.BigInteger;
import net.sf.bacchus.Record;
import net.sf.bacchus.Rtn;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TestDetailSupport {

    private DetailSupport detail;

    @Before
    public void setUp() throws Exception {
        this.detail = new DetailSupport();
    }

    @Test
    public void testRecordType() {
        assertEquals(Record.DETAIL_RECORD, this.detail.getType());
    }

    @Test
    public void testTransactionType() {
        assertEquals(0, this.detail.getTransaction());
        this.detail.setTransaction(1);
        assertEquals(1, this.detail.getTransaction());
    }

    @Test
    public void testOriginatingDfi() {
        assertNotNull(this.detail.getOdfi());
        assertEquals(0, this.detail.getOdfi().getRtn());
        this.detail.getOdfi().setRtn(12345678);
        assertEquals(12345678, this.detail.getOdfi().getRtn());
        final Rtn rtn = new Rtn(87654321);
        this.detail.setOdfi(rtn);
        assertEquals(rtn, this.detail.getOdfi());
    }

    @Test
    public void testReceivingDfi() {
        assertNotNull(this.detail.getRdfi());
        assertEquals(0, this.detail.getRdfi().getRtn());
        this.detail.getRdfi().setRtn(12345678);
        assertEquals(12345678, this.detail.getRdfi().getRtn());
        final Rtn rtn = new Rtn(87654321);
        this.detail.setRdfi(rtn);
        assertEquals(rtn, this.detail.getRdfi());
    }

    @Test
    public void testAccount() {
        assertNull(this.detail.getAccount());
        this.detail.setAccount("test");
        assertEquals("test", this.detail.getAccount());
    }

    @Test
    public void testAmount() {
        assertNotNull(this.detail.getAmount());
        assertEquals(0, BigDecimal.ZERO.compareTo(this.detail.getAmount()));
        this.detail.setAmount(BigDecimal.TEN);
        assertEquals(10.0, this.detail.getAmount().floatValue(), 0.0);
        this.detail.setAmount(new BigDecimal("12.5"));
        assertEquals(12.5, this.detail.getAmount().floatValue(), 0.0);
    }

    @Test
    public void testName() {
        assertNull(this.detail.getName());
        this.detail.setName("Jane");
        assertEquals("Jane", this.detail.getName());
        this.detail.setName(" SomeCo Inc. ");
        assertEquals(" SomeCo Inc. ", this.detail.getName());
    }

    @Test
    public void testDiscretionary() {
        assertNull(this.detail.getDiscretionary());
        this.detail.setDiscretionary("x");
        assertEquals("x", this.detail.getDiscretionary());
        this.detail.setDiscretionary("x ");
        assertEquals("x ", this.detail.getDiscretionary());
        this.detail.setDiscretionary(" x");
        assertEquals(" x", this.detail.getDiscretionary());
    }

    @Test
    public void testAddendaIndicator() {
        assertEquals(0, this.detail.getAddenda());
        assertEquals(0, this.detail.getIndicator());
        this.detail.setAddenda(0);
        assertEquals(0, this.detail.getAddenda());
        assertEquals(0, this.detail.getIndicator());
        this.detail.setAddenda(1);
        assertEquals(1, this.detail.getAddenda());
        assertEquals(1, this.detail.getIndicator());
        this.detail.setAddenda(2);
        assertEquals(2, this.detail.getAddenda());
        assertEquals(1, this.detail.getIndicator());
        this.detail.setIndicator(0);
        assertEquals(2, this.detail.getAddenda());
        assertEquals(0, this.detail.getIndicator());
        this.detail.setIndicator(1);
        assertEquals(2, this.detail.getAddenda());
        assertEquals(1, this.detail.getIndicator());
    }

    @Test
    public void testTraceNumber() {
        assertNotNull(this.detail.getTraceNumber());
        this.detail.setTraceNumber(BigInteger.TEN);
        assertEquals(0, BigInteger.TEN.compareTo(this.detail.getTraceNumber()));
    }

    @Test
    public void testSequence() {
        assertEquals(1, this.detail.getSequence());
        this.detail.setSequence(2);
        assertEquals(2, this.detail.getSequence());
    }
}
