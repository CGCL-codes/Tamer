package org.xactor.test.ws.atomictx.internal.test;

import junit.framework.TestCase;
import org.xactor.RecoveryCoordinator;
import org.xactor.Resource;
import org.xactor.ws.atomictx.WSATStringRemoteRefConverter;
import org.xactor.ws.atomictx.wrapper.WSATRecoveryCoordinatorWrapper;
import org.xactor.ws.atomictx.wrapper.WSATResourceWrapper;

/**
 * Tests for our WS-AtomicTransation <code>StringRemoteRefConverter</code>.
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public class WSATStringRemoteRefConverterUnitTestCase extends TestCase {

    /** The WS-AtomicTransation <code>StringRemoteRefConverter</code>. */
    private static WSATStringRemoteRefConverter converter = new WSATStringRemoteRefConverter();

    /**
    * Tets the conversion from a String to a <code>Resource</code> and then from the
    * <code>Resource</code> back to a String.
    */
    public void testResourceToStringConversion() {
        String strResource = "<wscoor:ParticipantProtocolService " + "xmlns:wsa='http://schemas.xmlsoap.org/ws/2004/08/addressing' " + "xmlns:jbosswscoor='http://www.jboss.org/wscoor/extension' " + "xmlns:wscoor='http://schemas.xmlsoap.org/ws/2004/10/wscoor'>" + "<wsa:Address>http://192.168.1.2:8080/wsat/Participant</wsa:Address><wsa:ReferenceProperties>" + "<jbosswscoor:CoordinatedActivityID>158329674399844</jbosswscoor:CoordinatedActivityID>" + "</wsa:ReferenceProperties></wscoor:ParticipantProtocolService>2e46d654";
        long localId = 12345;
        Resource resource = converter.stringToResource(strResource, localId);
        WSATResourceWrapper wsatResource = (WSATResourceWrapper) resource;
        assertNotNull(wsatResource.getEndpointReference());
        assertNotNull(wsatResource.getParticipantId());
        assertEquals(localId, wsatResource.getLocalId());
        String restoredStrResource = converter.resourceToString(resource);
        assertEquals(strResource, restoredStrResource);
    }

    /**
    * Tets the conversion from a String to a <code>RecoveryCoordinator</code> and then from the
    * <code>RecoveryCoordinator</code> back to a String.
    */
    public void testRecoveryCoordinatorToStringConversion() {
        String strRecCoordinator = "<wscoor:CoordinatorProtocolService " + "xmlns:wsa='http://schemas.xmlsoap.org/ws/2004/08/addressing' " + "xmlns:jbosswscoor='http://www.jboss.org/wscoor/extension' " + "xmlns:wscoor='http://schemas.xmlsoap.org/ws/2004/10/wscoor'>" + "<wsa:Address>http://192.168.1.1:8080/wsat/Coordinator</wsa:Address><wsa:ReferenceProperties>" + "<jbosswscoor:CoordinatedActivityID>228698418577443</jbosswscoor:CoordinatedActivityID>" + "</wsa:ReferenceProperties></wscoor:CoordinatorProtocolService>";
        long localId = 12345;
        RecoveryCoordinator recCoordinator = converter.stringToRecoveryCoordinator(strRecCoordinator, localId);
        WSATRecoveryCoordinatorWrapper wsatRecoveryCoordinator = (WSATRecoveryCoordinatorWrapper) recCoordinator;
        assertNotNull(wsatRecoveryCoordinator.getEndpointReference());
        assertEquals(localId, wsatRecoveryCoordinator.getLocalId());
        String restoredStrRecCoordinator = converter.recoveryCoordinatorToString(recCoordinator);
        assertEquals(strRecCoordinator, restoredStrRecCoordinator);
    }
}
