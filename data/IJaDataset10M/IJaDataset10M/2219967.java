package org.xactor.test.ws.usertx.test;

import java.rmi.RemoteException;
import java.util.Iterator;
import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPElement;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.test.JBossTestCase;
import org.jboss.test.JBossTestSetup;
import org.jboss.util.UnexpectedThrowable;
import org.xactor.test.ws.MultiServerTestUtil;
import org.xactor.test.ws.usertx.ejb.ClientHandler;
import org.xactor.test.ws.usertx.interfaces.SessionCounter;
import org.xactor.ws.Constants;
import org.xactor.ws.DOMUtils;
import org.xactor.ws.FaultInfo;
import org.xactor.ws.addressing.element.EndpointReference;
import org.xactor.ws.client.WSClientUserTransaction;
import org.xactor.ws.coordination.element.CoordinationContext;

/**
 * Invalid coordination context propagation tests.
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public class InvalidContextUnitTestCase extends JBossTestCase implements Constants {

    /** The SOAP UserTransaction. */
    private static WSClientUserTransaction userTx;

    /** The WS port used in the tests. */
    private static SessionCounter port;

    public InvalidContextUnitTestCase(String name) {
        super(name);
    }

    /**
    * Test a context with no identifier.
    */
    public void testContextWithNullIdentifier() throws Exception {
        try {
            userTx.begin();
            CoordinationContext ctx = userTx.getTransactionContext();
            ctx.setIdentifier(null);
            port.incCounter();
            getLog().debug("Should not get here!");
            fail("RemoteException should have been thrown");
        } catch (RemoteException e) {
            Throwable cause = e.detail;
            FaultInfo faultInfo = WSCoor.Faults.CONTEXT_REFUSED;
            String exception = IllegalArgumentException.class.getName();
            checkSOAPFaultException(cause, faultInfo, exception);
            String expectedExceptionMessage = "Missing coordination context element(s):";
            String exceptionMessage = getExceptionMessage(cause);
            assertTrue(exceptionMessage.startsWith(expectedExceptionMessage));
            assertTrue(exceptionMessage.indexOf(WSCoor.Elements.IDENTIFIER.getLocalPart()) != -1);
        } finally {
            rollbackTransactionIfStillActive();
        }
    }

    /**
    * Test a context with no coordination type.
    */
    public void testContextWithNullCoordinationType() throws Exception {
        try {
            userTx.begin();
            CoordinationContext ctx = userTx.getTransactionContext();
            ctx.setCoordinationType(null);
            port.incCounter();
            getLog().debug("Should not get here!");
            fail("RemoteException should have been thrown");
        } catch (RemoteException e) {
            Throwable cause = e.detail;
            FaultInfo faultInfo = WSCoor.Faults.CONTEXT_REFUSED;
            String exception = IllegalArgumentException.class.getName();
            checkSOAPFaultException(cause, faultInfo, exception);
            String expectedExceptionMessage = "Missing coordination context element(s):";
            String exceptionMessage = getExceptionMessage(cause);
            assertTrue(exceptionMessage.startsWith(expectedExceptionMessage));
            assertTrue(exceptionMessage.indexOf(WSCoor.Elements.COORDINATION_TYPE.getLocalPart()) != -1);
        } finally {
            rollbackTransactionIfStillActive();
        }
    }

    /**
    * Test a context with no registration service.
    */
    public void testContextWithNullRegistrationService() throws Exception {
        EndpointReference registrationService = null;
        try {
            userTx.begin();
            CoordinationContext ctx = userTx.getTransactionContext();
            registrationService = ctx.getRegistrationService();
            ctx.setRegistrationService(null);
            port.incCounter();
            getLog().debug("Should not get here!");
            fail("RemoteException should have been thrown");
        } catch (RemoteException e) {
            Throwable cause = e.detail;
            FaultInfo faultInfo = WSCoor.Faults.CONTEXT_REFUSED;
            String exception = IllegalArgumentException.class.getName();
            checkSOAPFaultException(cause, faultInfo, exception);
            String expectedExceptionMessage = "Missing coordination context element(s):";
            String exceptionMessage = getExceptionMessage(cause);
            assertTrue(exceptionMessage.startsWith(expectedExceptionMessage));
            assertTrue(exceptionMessage.indexOf(WSCoor.Elements.REGISTRATION_SERVICE.getLocalPart()) != -1);
        } finally {
            userTx.getTransactionContext().setRegistrationService(registrationService);
            rollbackTransactionIfStillActive();
        }
    }

    /**
    * Test a context with no identifier, no coordination type and no registration service.
    */
    public void testContextWithNullIdentifierCoordinationContextAndRegistrationService() throws Exception {
        EndpointReference registrationService = null;
        try {
            userTx.begin();
            CoordinationContext ctx = userTx.getTransactionContext();
            ctx.setIdentifier(null);
            ctx.setCoordinationType(null);
            registrationService = ctx.getRegistrationService();
            ctx.setRegistrationService(null);
            port.incCounter();
            getLog().debug("Should not get here!");
            fail("RemoteException should have been thrown");
        } catch (RemoteException e) {
            Throwable cause = e.detail;
            FaultInfo faultInfo = WSCoor.Faults.CONTEXT_REFUSED;
            String exception = IllegalArgumentException.class.getName();
            checkSOAPFaultException(cause, faultInfo, exception);
            String expectedExceptionMessage = "Missing coordination context element(s):";
            String exceptionMessage = getExceptionMessage(cause);
            assertTrue(exceptionMessage.startsWith(expectedExceptionMessage));
            assertTrue(exceptionMessage.indexOf(WSCoor.Elements.IDENTIFIER.getLocalPart()) != -1);
            assertTrue(exceptionMessage.indexOf(WSCoor.Elements.COORDINATION_TYPE.getLocalPart()) != -1);
            assertTrue(exceptionMessage.indexOf(WSCoor.Elements.REGISTRATION_SERVICE.getLocalPart()) != -1);
        } finally {
            userTx.getTransactionContext().setRegistrationService(registrationService);
            rollbackTransactionIfStillActive();
        }
    }

    /**
    * Rollback the current transaction, if it's still active.
    */
    private void rollbackTransactionIfStillActive() throws SystemException {
        int status = userTx.getStatus();
        if (status == Status.STATUS_ACTIVE || status == Status.STATUS_MARKED_ROLLBACK) userTx.rollback();
    }

    private void checkSOAPFaultException(Throwable t, FaultInfo expectedFaultInfo, String expectedExceptionName) {
        boolean throwException = true;
        if (t instanceof SOAPFaultException) {
            SOAPFaultException ex = (SOAPFaultException) t;
            QName expectedSubcode = expectedFaultInfo.getSubcode();
            String expectedReason = expectedFaultInfo.getReason();
            QName subCode = ex.getFaultCode();
            String reason = ex.getFaultString();
            String[] exceptionInfo = getExceptionInformation(ex);
            String exceptionName = exceptionInfo[0];
            boolean subcodeMatches = expectedSubcode.equals(subCode);
            boolean reasonMatches = expectedReason.equals(reason);
            boolean exceptionMatches = expectedExceptionName.equals(exceptionName);
            if (subcodeMatches && reasonMatches && exceptionMatches) throwException = false;
        }
        if (throwException) throw new UnexpectedThrowable(t);
    }

    /**
    * Return information about exceptions nested in a <code>SOAPFaultException</code> (exception
    * name and message).
    * 
    * @param cause
    *           the <code>SOAPFaultException</code>.
    * @return an array of <code>String</code>s containing the nested exception name in its first
    *         position and the exception message in its second position.
    */
    private String[] getExceptionInformation(SOAPFaultException cause) {
        String[] exceptionInfo = new String[2];
        Detail detail = cause.getDetail();
        if (detail != null) {
            Iterator it = detail.getDetailEntries();
            while (it.hasNext()) {
                SOAPElement el = (SOAPElement) it.next();
                QName qname = DOMUtils.getElementQName(el);
                if (WSCoor.Elements.EXCEPTION_NAME.equals(qname)) exceptionInfo[0] = el.getValue(); else if (WSCoor.Elements.EXCEPTION_MESSAGE.equals(qname)) exceptionInfo[1] = el.getValue();
            }
        }
        return exceptionInfo;
    }

    private String getExceptionMessage(Throwable cause) {
        String[] exceptionInfo = getExceptionInformation((SOAPFaultException) cause);
        assertNotNull(exceptionInfo);
        String exceptionMessage = exceptionInfo[1];
        assertNotNull(exceptionMessage);
        return exceptionMessage;
    }

    /**
    * Setup the test suite.
    */
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(InvalidContextUnitTestCase.class));
        TestSetup wrapper = new JBossTestSetup(suite) {

            protected void setUp() throws Exception {
                super.setUp();
                redeploy("sessioncounter.jar");
                redeploy("sessioncounter-client.jar");
                InitialContext ctx = MultiServerTestUtil.getInstance().getHost0Context();
                userTx = (WSClientUserTransaction) ctx.lookup(WSClientUserTransaction.USER_TRANSACTION);
                Service service = (Service) ctx.lookup("java:comp/env/service/sessioncounter");
                port = (SessionCounter) service.getPort(SessionCounter.class);
                ClientHandler.setClientId(new Integer(1));
                port.createCounter();
            }

            protected void tearDown() throws Exception {
                port.deleteCounter();
                undeploy("sessioncounter-client.jar");
                undeploy("sessioncounter.jar");
                userTx = null;
                port = null;
                ClientHandler.setClientId(null);
                super.tearDown();
            }
        };
        return wrapper;
    }
}
