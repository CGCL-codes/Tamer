package net.sf.bacchus.spring;

import net.sf.bacchus.StandardEntryClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestSpecializer {

    private Specializer specializer;

    @Before
    public void setUp() throws Exception {
        this.specializer = new Specializer();
        this.specializer.setName("test");
    }

    @Test
    public void testOpenExecutionContextEmpty() {
        final ExecutionContext context = new ExecutionContext();
        this.specializer.open(context);
        assertNull(this.specializer.getDelegate().getSec());
    }

    @Test
    public void testOpenExecutionContextSec() {
        final ExecutionContext context = new ExecutionContext();
        context.put("test.sec", "ACK");
        this.specializer.open(context);
        assertEquals(StandardEntryClass.ACK, this.specializer.getDelegate().getSec());
    }

    @Test
    public void testUpdateExecutionContextEmpty() {
        final ExecutionContext context = new ExecutionContext();
        this.specializer.update(context);
        assertFalse(context.containsKey("test.sec"));
    }

    @Test
    public void testUpdateExecutionContextSec() {
        this.specializer.getDelegate().setSec(StandardEntryClass.ACK);
        final ExecutionContext context = new ExecutionContext();
        this.specializer.update(context);
        assertTrue(context.containsKey("test.sec"));
        assertEquals("ACK", context.getString("test.sec"));
    }
}
