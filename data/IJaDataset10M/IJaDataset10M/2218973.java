package org.mobicents.servlet.sip.testsuite.annotations;

import java.util.EventObject;
import java.util.Hashtable;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import org.apache.log4j.Logger;
import org.mobicents.servlet.sip.SipServletTestCase;

public class AnnotationTest extends SipServletTestCase implements SipListener {

    private static transient Logger logger = Logger.getLogger(AnnotationTest.class);

    protected Hashtable providerTable = new Hashtable();

    private Tracker tracker;

    public AnnotationTest(String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        tracker = new Tracker();
        super.setUp();
    }

    public void testSecurity() {
        tracker.init();
        try {
            for (int q = 0; q < 10; q++) {
                if (tracker.receivedInvite) break;
                Thread.sleep(1111);
            }
            if (!tracker.receivedInvite) fail("Didnt receive notification that the scenario is complete.");
        } catch (InterruptedException e) {
            fail("Test failed: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void tearDown() throws Exception {
        tracker.destroy();
        super.tearDown();
    }

    @Override
    public void deployApplication() {
        assertTrue(tomcat.deployContext(projectHome + "/sip-servlets-test-suite/applications/annotated-servlet/src/main/sipapp", "sip-test-context", "sip-test"));
    }

    @Override
    protected String getDarConfigurationFile() {
        return "file:///" + projectHome + "/sip-servlets-test-suite/testsuite/src/test/resources/" + "org/mobicents/servlet/sip/testsuite/annotations/annotated-servlet-dar.properties";
    }

    public void init() {
    }

    private SipListener getSipListener(EventObject sipEvent) {
        SipProvider source = (SipProvider) sipEvent.getSource();
        SipListener listener = (SipListener) providerTable.get(source);
        if (listener == null) throw new RuntimeException("Unexpected null listener");
        return listener;
    }

    public void processRequest(RequestEvent requestEvent) {
        getSipListener(requestEvent).processRequest(requestEvent);
    }

    public void processResponse(ResponseEvent responseEvent) {
        getSipListener(responseEvent).processResponse(responseEvent);
    }

    public void processTimeout(TimeoutEvent timeoutEvent) {
        getSipListener(timeoutEvent).processTimeout(timeoutEvent);
    }

    public void processIOException(IOExceptionEvent exceptionEvent) {
        fail("unexpected exception");
    }

    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        getSipListener(transactionTerminatedEvent).processTransactionTerminated(transactionTerminatedEvent);
    }

    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        getSipListener(dialogTerminatedEvent).processDialogTerminated(dialogTerminatedEvent);
    }
}
