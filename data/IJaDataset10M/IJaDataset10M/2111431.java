package com.ibm.aglets.thread;

import junit.framework.TestCase;
import com.ibm.aglet.message.Message;
import com.ibm.aglets.MessageImpl;
import com.ibm.aglets.MessageManagerImpl;

/**
 * A test for the aglet thread classes.
 * 
 * @author Luca Ferrari - cat4hire@users.sourceforge.net
 * 
 *         24/ago/07
 */
public class TestAgletThread extends TestCase {

    AgletThread thread = null;

    MessageImpl message = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.thread = new AgletThread(new ThreadGroup("TestGroup"));
        this.message = new MessageImpl(new Message(), null, Message.ONEWAY, System.currentTimeMillis());
    }

    /**
     * Test method for {@link com.ibm.aglets.thread.AgletThread#run()}.
     */
    public void testRun() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.ibm.aglets.thread.AgletThread#AgletThread(java.lang.ThreadGroup)}
     * .
     */
    public void testAgletThreadThreadGroup() {
        ThreadGroup tg = new ThreadGroup("Test_Thread_Group");
        AgletThread t1 = new AgletThread(tg);
        assertEquals(tg, t1.getThreadGroup());
    }

    /**
     * Test method for
     * {@link com.ibm.aglets.thread.AgletThread#AgletThread(java.lang.ThreadGroup, com.ibm.aglet.message.MessageManager)}
     * .
     */
    public void testAgletThreadThreadGroupMessageManager() {
        ThreadGroup tg = new ThreadGroup("Test_Thread_Group");
        MessageManagerImpl mm = new MessageManagerImpl(null);
        AgletThread t1 = new AgletThread(tg, mm);
        assertEquals(tg, t1.getThreadGroup());
        assertEquals(mm, t1.getMessageManager());
    }

    /**
     * Test method for
     * {@link com.ibm.aglets.thread.AgletThread#getCurrentMessage()}.
     */
    public void testGetCurrentMessage() {
        this.thread.handleMessage(this.message);
        assertEquals(this.message, AgletThread.getCurrentMessage());
    }

    /**
     * Test method for
     * {@link com.ibm.aglets.thread.AgletThread#handleMessage(com.ibm.aglets.MessageImpl)}
     * .
     */
    public void testHandleMessage() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.ibm.aglets.thread.AgletThread#setReentrant(boolean)}.
     */
    public void testSetReentrant() {
        this.thread.setReentrant(true);
        assertEquals(true, this.thread.isReentrant());
    }
}
