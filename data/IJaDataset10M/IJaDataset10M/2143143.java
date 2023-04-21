package org.personalsmartspace.rms050;

import java.util.Hashtable;
import junit.framework.TestCase;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.personalsmartspace.impl.Activator;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.onm.api.platform.IAdvertisementMgr;
import org.personalsmartspace.onm.api.platform.IPeerGroupMgr;
import org.personalsmartspace.onm.api.pss3p.IMessageQueue;
import org.personalsmartspace.rms050.onm.AsyncServiceImpl;
import org.personalsmartspace.rms050.onm.IAsyncService;
import org.personalsmartspace.rms050.onm.IONMtest;
import org.personalsmartspace.rms050.onm.ISyncService;
import org.personalsmartspace.rms050.onm.SyncServiceImpl;
import org.personalsmartspace.sre.ems.api.pss3p.IEventMgr;

public class ServicePSS extends TestCase implements IONMtest {

    private PSSLog logger = new PSSLog(this);

    private BundleContext bc;

    private ServiceTracker peerGroupMgrTracker;

    private ServiceTracker adMgrTracker;

    private ServiceTracker eventMgrTracker;

    private IMessageQueue msgQueueService;

    private IPeerGroupMgr m_PeerGrpMgr;

    private IAdvertisementMgr m_AdMgr;

    private IEventMgr m_EventMgr;

    private String m_thisPeername;

    private boolean m_messageReceived;

    private ServiceTracker messagequeueTracker;

    private IMessageQueue m_MessageQueue;

    private AsyncServiceImpl m_asynM;

    private SyncServiceImpl m_synM;

    public ServicePSS() {
        this.bc = Activator.bundleContext;
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        messagequeueTracker = new ServiceTracker(bc, IMessageQueue.class.getName(), null);
        messagequeueTracker.open();
        m_MessageQueue = (IMessageQueue) messagequeueTracker.getService();
        m_messageReceived = false;
        m_asynM = new AsyncServiceImpl(this);
        m_synM = new SyncServiceImpl(this);
        final Hashtable proptable = new Hashtable();
        bc.registerService(IAsyncService.class.getName(), m_asynM, proptable);
        bc.registerService(ISyncService.class.getName(), m_synM, proptable);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        messagequeueTracker.close();
    }

    public void testServicePss() {
        try {
            logger.info("Waiting for message!");
            Thread.sleep(MESSAGE_WAIT);
            logger.info("Time to check if the message was received!");
            assertTrue(m_messageReceived);
            this.tearDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RefreshGroups() {
    }

    @Override
    public void messageArrived(boolean result) {
        logger.info("Message Received!");
        m_messageReceived = true;
    }
}
