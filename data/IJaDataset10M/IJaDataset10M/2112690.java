package com.apelon.apelonserver.util.socket;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *  TestManagedThreadPool is a junit test case
 *  for ManagedThreadPool
 *
 * @author Teep S. Socket
 * @version 2.0
 *
 * Copyright (c) 1997 - 2001 Apelon, Inc. All rights reserved.
 */
public class TestManagedThreadPool extends TestCase {

    ManagedThreadPool mtp = null;

    public TestManagedThreadPool(String s) {
        super(s);
    }

    /** called before each invocation of one of the test methods
   *  setUp creates a new ManagedThreadPool
   */
    protected void setUp() {
        System.out.println("calling setup");
        mtp = new ManagedThreadPool();
        mtp.start();
    }

    /** called after each running of a test method
   *  tearDown shutdowns the threadpool and frees
   *  the object for the garbage collector
   */
    protected void tearDown() {
        System.out.println("Asking Pool to Shutdown");
        mtp.shutdown();
        mtp = null;
    }

    public void testLongRunning() {
        System.out.println("Kicking off long running");
        for (int j = 0; j < 10; j++) {
            mtp.execute(new Runnable() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                            System.out.println("Hey I've been interrupted");
                        }
                        System.out.println("Hey I'm awake now -- " + i);
                    }
                }
            });
        }
        assertTrue(true);
    }

    public static Test suite() {
        return new TestSuite(TestManagedThreadPool.class);
    }

    /** JJ, this main allows the test program to run standalone
   *  I imagine what we'll do is take the code fragment
   *  "new TestSuite(ManagedThreadPoolTest.class)" and add
   *  it to some lager driver which will have already set up
   *  a "TestRunner" and passed it the appropriate streams
   *
   *  NOTE: I had to change the method "junit.textui.TestRunner.doRun"
   *  from protected to public in order to make this work,
   *  Bob
   */
    public static void main(String[] args) {
        try {
            long start = System.currentTimeMillis();
            String path = args[0];
            System.out.println("The total run time was : " + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
