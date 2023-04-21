package org.asmatron.messengine.testing.support;

import org.asmatron.messengine.engines.DefaultEventDelegate;

public class TestEventDelegate extends DefaultEventDelegate {

    public TestEventDelegate() {
        super(new TestingExecutorService());
    }
}
