package org.eclipse.mylyn.context.tests;

import org.eclipse.mylyn.monitor.core.StatusHandler;

/**
 * @author Mik Kersten
 */
public class ManualUiTest extends AbstractManualTest {

    public void testErrorDialog() {
        try {
            int i = 10 / 0;
            System.out.println(i);
        } catch (Throwable t) {
            StatusHandler.fail(t, "whoops", true);
        }
        StatusHandler.fail(null, "whoops", true);
        assertTrue(confirmWithUser("Did an error dialog show up correctly?"));
    }
}
