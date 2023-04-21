package com.ait.actors;

import junit.framework.TestCase;

public class LockableDequeTest extends AbstractStealableQueueTest {

    protected void setUp() throws Exception {
        super.setUp();
        deque = new LockableDeque<Integer>();
    }

    protected void tearDown() throws Exception {
        deque = null;
        super.tearDown();
    }
}
