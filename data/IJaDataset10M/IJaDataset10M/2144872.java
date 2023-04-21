package net.grinder.communication;

import junit.framework.TestCase;
import junit.swingui.TestRunner;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *  Unit test case for <code>UnicastSender</code> and
 *  <code>MulticastReceiver</code>.
 *
 * @author Philip Aston
 * @version $Revision: 887 $
 **/
public class TestUnicastSenderAndReceiver extends AbstractSenderAndReceiverTests {

    public static void main(String[] args) {
        TestRunner.run(TestUnicastSenderAndReceiver.class);
    }

    public TestUnicastSenderAndReceiver(String name) {
        super(name);
    }

    private final String ADDRESS = "127.0.0.1";

    private final int PORT = 1234;

    protected Receiver createReceiver() throws Exception {
        return new UnicastReceiver(ADDRESS, PORT);
    }

    protected Sender createSender() throws Exception {
        return new UnicastSender("Test Sender", ADDRESS, PORT);
    }

    /**
     * Sigh, JUnit treats setUp and tearDown as non-virtual methods -
     * must define in concrete test case class.
     **/
    protected void setUp() throws Exception {
        Thread.sleep(50);
        m_receiver = createReceiver();
        m_sender = createSender();
    }

    protected void tearDown() throws Exception {
        m_receiver.shutdown();
        m_sender.shutdown();
    }
}
