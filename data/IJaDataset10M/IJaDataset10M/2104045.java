package org.wtc.eclipse.platform.helpers;

import com.windowtester.runtime.IUIContext;

/**
 * Helper for injecting JUnit behavior into a test directly.
 * 
 * @since 3.8.0
 */
public interface ITestHelper {

    /**
     * Fail the test with the given message.
     *
     * @since 3.8.0
     * @param  ui       - Driver for UI-generated input
     * @param  message  - The message to tag the failure with
     */
    public void failTest(IUIContext ui, String message);
}
