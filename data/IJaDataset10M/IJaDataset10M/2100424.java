package com.google.code.appengine.awt;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import com.google.code.appengine.awt.EventQueue;
import junit.framework.TestCase;

public class EventDispatchThreadRTest extends TestCase {

    /**
     * Regression test for JIRA issue HARMONY-2818
     */
    public final void testHARMONY2818() throws Throwable {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                throw new RuntimeException("expected from EDT");
            }
        });
        EventQueue.invokeAndWait(new Runnable() {

            public void run() {
            }
        });
    }
}
