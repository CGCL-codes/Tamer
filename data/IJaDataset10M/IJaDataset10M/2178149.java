package org.personalsmartspace.rms040;

import java.util.ArrayList;
import junit.framework.TestCase;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.personalsmartspace.impl.Activator;
import org.personalsmartspace.onm.api.platform.IPeerGroupMgr;
import org.personalsmartspace.onm.api.platform.PeerGroupInfo;
import org.personalsmartspace.onm.api.platform.PeerInfo;
import org.personalsmartspace.onm.api.pss3p.ONMException;
import org.personalsmartspace.spm.identity.api.platform.IIdentityManagement;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.slm.api.platform.IServiceLifecycleManager;
import org.personalsmartspace.sre.slm.api.platform.ISlm2Slm;
import org.personalsmartspace.sre.slm.api.pss3p.ISlm3P;

public class SlmAfterInstallService extends TestCase {

    /**
     * Time to wait for other OSGi services to receive an event, etc.
     */
    private final int timeToWaitInMiliSec = 2000;

    private IServiceLifecycleManager slm;

    private BundleContext bundleContext;

    private String localPeerId;

    private IServiceIdentifier[] allServiceIds;

    private IServiceIdentifier firstServiceId;

    private IDigitalPersonalIdentifier localDpi;

    public SlmAfterInstallService() {
        this.bundleContext = Activator.bundleContext;
    }

    /**
     * @throws Exception
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        slm = (IServiceLifecycleManager) getOsgiService(IServiceLifecycleManager.class.getName());
        IIdentityManagement identityMgr = (IIdentityManagement) getOsgiService(IIdentityManagement.class.getName());
        assertNotNull(slm);
        assertNotNull(identityMgr);
        localDpi = identityMgr.getPublicDigitalPersonalIdentifier();
        localPeerId = getLocalPeerId();
        if (localPeerId == null) {
            System.out.println("Could not get local peer ID from ONM, " + "will use a hard-coded ID for this test.");
            localPeerId = "myPeerId";
        }
        allServiceIds = slm.listServices(true);
        if (allServiceIds == null || allServiceIds.length == 0) {
            fail("Before running this test, at least one 3rd party service " + "should be installed. Either on this peer, or some other " + "peer in same PSS (in this case, FIRST install the service " + "and THEN wait for both peers to discover each other)");
        }
        firstServiceId = allServiceIds[0];
    }

    /**
     * @throws Exception
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAllInCorrectOrder() throws InterruptedException {
        _testGetProxy();
        _testShowGui();
        _testStartUserSessionOnService();
        _testStopUserSessionOnService();
    }

    public void _testGetProxy() throws InterruptedException {
        SlmRpcListener listener = new SlmRpcListener();
        System.out.println("SLM test: Proxy will be installed for service " + firstServiceId);
        ((ISlm3P) slm).getProxy(firstServiceId, listener);
        Thread.sleep(timeToWaitInMiliSec);
        assertTrue(listener.hasBeenInvoked());
        assertTrue(listener.isSuccessful());
    }

    public void _testShowGui() throws InterruptedException {
        System.out.println("SLM test: " + firstServiceId + ". GUI will be shown.");
        SlmRpcListener listener = new SlmRpcListener();
        slm.showGui(firstServiceId.toString(), localPeerId, null, listener);
        Thread.sleep(timeToWaitInMiliSec);
        assertTrue(listener.hasBeenInvoked());
        assertTrue(listener.isSuccessful());
    }

    public void _testStartUserSessionOnService() throws InterruptedException {
        System.out.println("SLM test: " + firstServiceId + ". startUserSession.");
        SlmRpcListener listener = new SlmRpcListener();
        IDigitalPersonalIdentifier dpi = localDpi;
        System.out.println("Local public DPI = " + dpi);
        ((ISlm2Slm) slm).startUserSessionOnService(firstServiceId.toUriString(), "1", dpi.toUriString(), listener);
        Thread.sleep(timeToWaitInMiliSec);
        assertTrue(listener.hasBeenInvoked());
        assertTrue(listener.isSuccessful());
        assertEquals("##Service Identifier:" + firstServiceId.toUriString() + "##" + 0, listener.callbackObjects.lastElement());
    }

    public void _testStopUserSessionOnService() throws InterruptedException {
        System.out.println("SLM test: " + firstServiceId + ". stopUserSession.");
        SlmRpcListener listener = new SlmRpcListener();
        IDigitalPersonalIdentifier dpi = localDpi;
        System.out.println("Local public DPI = " + dpi);
        ((ISlm2Slm) slm).stopUserSessionOnService(firstServiceId.toUriString(), "1", dpi.toUriString(), listener);
        Thread.sleep(timeToWaitInMiliSec);
        assertTrue(listener.hasBeenInvoked());
        assertTrue(listener.isSuccessful());
    }

    public Object getOsgiService(String className) {
        ServiceReference[] sref = getOsgiServiceReferences(className);
        if (sref == null || sref.length == 0) {
            return null;
        }
        return bundleContext.getService(sref[0]);
    }

    public ServiceReference[] getOsgiServiceReferences(String className) {
        ServiceReference[] sref;
        try {
            sref = this.bundleContext.getServiceReferences(className, null);
        } catch (InvalidSyntaxException ex) {
            return null;
        }
        if (sref == null) {
            return null;
        }
        System.out.println("Found " + sref.length + " OSGi services for class " + className);
        return sref;
    }

    /**
     * @return Peer ID of local device
     */
    private String getLocalPeerId() {
        return getMyPeerInfo().getPeerID();
    }

    private String[] getOtherPeerIds() {
        ArrayList<PeerInfo> peerGroup = getPeerGroupInfo().getPeerList();
        String[] ids = new String[peerGroup.size() - 1];
        int j = 0;
        String localId = getLocalPeerId();
        String id;
        for (int i = 0; i < peerGroup.size(); i++) {
            id = peerGroup.get(i).getPeerID();
            if (!id.equals(localId)) {
                ids[j] = id;
                ++j;
            }
        }
        return ids;
    }

    private PeerInfo getMyPeerInfo() {
        IPeerGroupMgr onmPeerGroupMgr;
        onmPeerGroupMgr = (IPeerGroupMgr) getOsgiService(IPeerGroupMgr.class.getName());
        try {
            return onmPeerGroupMgr.getMyPeerInfo();
        } catch (ONMException ex) {
            System.out.println("Could not get my peer info. " + ex.getMessage());
            return null;
        }
    }

    private PeerGroupInfo getPeerGroupInfo() {
        IPeerGroupMgr onmPeerGroupMgr;
        onmPeerGroupMgr = (IPeerGroupMgr) getOsgiService(IPeerGroupMgr.class.getName());
        try {
            return onmPeerGroupMgr.getPeerGroupInfo();
        } catch (ONMException ex) {
            System.out.println("Could not get my peer info. " + ex.getMessage());
            return null;
        }
    }
}
