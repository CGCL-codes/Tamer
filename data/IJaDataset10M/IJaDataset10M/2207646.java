package com.phloc.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link TimeValue}.
 * 
 * @author philip
 */
public final class TimeValueTest {

    @Test
    public void testAll() {
        final TimeValue t = new TimeValue(TimeUnit.SECONDS, 5);
        assertEquals(TimeUnit.SECONDS, t.getTimeUnit());
        assertEquals(5, t.getValue());
        PhlocTestUtils.testDefaultImplementationWithEqualContentObject(new TimeValue(TimeUnit.SECONDS, 5), new TimeValue(TimeUnit.SECONDS, 5));
        PhlocTestUtils.testDefaultImplementationWithDifferentContentObject(new TimeValue(TimeUnit.SECONDS, 5), new TimeValue(TimeUnit.SECONDS, 4));
        PhlocTestUtils.testDefaultImplementationWithDifferentContentObject(new TimeValue(TimeUnit.SECONDS, 5), new TimeValue(TimeUnit.NANOSECONDS, 5));
        try {
            new TimeValue(null, 5);
            fail();
        } catch (final NullPointerException ex) {
        }
    }
}
