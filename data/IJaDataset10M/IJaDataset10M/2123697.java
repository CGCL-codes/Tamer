package net.sf.doolin.app.sc.engine.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.Locale;
import net.sf.doolin.app.sc.engine.ClientConnectionResultType;
import net.sf.doolin.app.sc.engine.ClientID;
import net.sf.doolin.app.sc.engine.InstanceID;
import org.junit.Test;

public class TestDefaultClientConnectionResult {

    @Test
    public void testDefaultClientConnectionResultClientID() {
        DefaultClientConnectionResult r = new DefaultClientConnectionResult(new ClientID(1, "test", Locale.ENGLISH, new InstanceID(10)));
        assertEquals(ClientConnectionResultType.OK, r.getType());
        assertEquals(new ClientID(1, "test", Locale.ENGLISH, new InstanceID(10)), r.getClientID());
        assertNull(r.getErrorMessage());
    }

    @Test
    public void testDefaultClientConnectionResultString() {
        DefaultClientConnectionResult r = new DefaultClientConnectionResult("Message");
        assertEquals(ClientConnectionResultType.ERROR, r.getType());
        assertNull(r.getClientID());
        assertEquals("Message", r.getErrorMessage());
    }
}
