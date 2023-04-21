package org.callbackparams.junit4.integration;

import org.callbackparams.junit4.WrappedRunner;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * This sub-class test ensures backward compability by using the deprecated
 * annotation-class {@link WrappedRunner}
 *
 * @author Henrik Kaipe
 */
@WrappedRunner(PowerMockRunner.class)
public class TestLegacyPowermockIntegrationWithCallbackParamsRunner extends TestPowermockIntegrationWithCallbackParamsRunner {
}
