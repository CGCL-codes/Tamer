package org.fuin.axon.support.base;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.caucho.hessian.io.AbstractHessianOutput;

public class JodaDateTimeSerializerTest {

    private JodaDateTimeSerializer testee;

    @Before
    public final void setUp() {
        testee = new JodaDateTimeSerializer();
    }

    @After
    public final void tearDown() {
        testee = null;
    }

    @Test
    public final void testWriteValue() throws IOException {
        final AbstractHessianOutput out = mock(AbstractHessianOutput.class);
        final DateTime dateTime = new DateTime(0);
        final long millis = dateTime.toDateTime(DateTimeZone.UTC).getMillis();
        testee.writeValue(out, dateTime);
        verify(out).writeUTCDate(millis);
    }
}
