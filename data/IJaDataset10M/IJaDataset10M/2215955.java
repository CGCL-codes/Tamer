package com.google.appengine.testing.cloudcover.harness.junit4;

import com.google.appengine.testing.cloudcover.spi.BaseTestHarnessConfig;
import com.google.appengine.testing.cloudcover.spi.TestHarness;

/**
 * Base config for JUnit 4.
 *
 * @author Max Ross <max.ross@gmail.com>
 */
public abstract class JUnit4Config extends BaseTestHarnessConfig {

    public TestHarness getTestHarness() {
        return new JUnit4TestHarness();
    }
}
