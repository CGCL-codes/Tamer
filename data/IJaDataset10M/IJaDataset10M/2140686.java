package org.tripcom.integration.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class ErrorTest {

    @Test
    public void testFromOrdinal() throws Exception {
        Error e = Error.fromOrdinal(0);
        assertEquals(Error.MM_mgmtAPI_wrongParameterCount, e);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromOrdinalException() throws Exception {
        Error e = Error.fromOrdinal(19837);
        fail("Exception expected");
    }
}
