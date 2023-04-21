package org.timelord.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.timelord.FreezeTestHelper;

@RunWith(TimeLordRunner.class)
public class TimeLordRunnerTest {

    @Test
    public void testFrozen() throws Exception {
        FreezeTestHelper.testCalendarFrozen();
    }
}
