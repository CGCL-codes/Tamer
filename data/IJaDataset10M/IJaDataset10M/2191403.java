package org.colombbus.tangara.util;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ThreadHelperTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    class NeverEndThread extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    class OneSecondThread extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    @Test
    public void testInterruptIfNecessaryNullParameter() {
        ThreadHelper.interruptIfNecessary(null);
    }

    @Test
    public void testInterruptIfNecessaryRunningThread() throws Exception {
        Thread thread = new NeverEndThread();
        thread.start();
        assertTrue(thread.isAlive());
        assertFalse(thread.isInterrupted());
        ThreadHelper.interruptIfNecessary(thread);
        Thread.sleep(100);
        assertFalse(thread.isAlive());
    }

    @Test
    public void testInterruptIfNecessaryJoinedThread() throws InterruptedException {
        Thread thread = new OneSecondThread();
        thread.start();
        thread.join();
        assertFalse(thread.isAlive());
        assertFalse(thread.isInterrupted());
        ThreadHelper.interruptIfNecessary(thread);
        assertFalse(thread.isInterrupted());
        assertFalse(thread.isAlive());
    }
}
