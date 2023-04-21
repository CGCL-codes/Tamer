package org.personalsmartspace.rms050;

import java.util.HashMap;
import java.util.Iterator;
import junit.framework.TestCase;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.personalsmartspace.impl.Activator;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.onm.api.platform.IAdvertisementMgr;
import org.personalsmartspace.onm.api.pss3p.IMessageQueue;
import org.personalsmartspace.onm.api.pss3p.ONMException;
import org.personalsmartspace.onm.api.pss3p.PSSAdvertisement;
import org.personalsmartspace.onm.api.pss3p.ServiceMessage;
import org.personalsmartspace.onm.api.pss3p.XMLConverter;
import org.personalsmartspace.rms050.onm.IONMtest;
import org.personalsmartspace.rms050.onm.ServiceCallback;

public class ProxyPSSSyncEmbeddedInputReturn extends TestCase implements IONMtest {

    private PSSLog logger = new PSSLog(this);

    private BundleContext bc;

    private IAdvertisementMgr m_AdMgr;

    private boolean m_messageSent;

    private ServiceTracker messagequeueTracker;

    private IMessageQueue m_MessageQueue;

    private ServiceTracker adMgrTracker;

    private boolean m_answerReceived;

    private ServiceCallback m_Callback;

    public ProxyPSSSyncEmbeddedInputReturn() {
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
        m_messageSent = false;
        m_answerReceived = false;
        adMgrTracker = new ServiceTracker(bc, IAdvertisementMgr.class.getName(), null);
        adMgrTracker.open();
        m_AdMgr = (IAdvertisementMgr) adMgrTracker.getService();
        m_Callback = new ServiceCallback(this);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        messagequeueTracker.close();
        adMgrTracker.close();
    }

    public void testProxyPss() {
        try {
            Thread.sleep(4000);
            logger.info("Refreshing Groups!");
            RefreshGroups();
            logger.info("Waiting to see if message was sent!");
            Thread.sleep(MESSAGE_WAIT);
            logger.info("Time to check if message was sent!");
            assertTrue(m_messageSent);
            assertTrue(m_answerReceived);
            this.tearDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RefreshGroups() {
        HashMap<String, PSSAdvertisement> pssGroups;
        try {
            pssGroups = this.m_AdMgr.getCachedPSSAdvertisements();
            logger.info("Getting cached PSS Advertisements!");
            Iterator<String> it = pssGroups.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                PSSAdvertisement pssAd = pssGroups.get(key);
                sendMessageToOtherPss(pssAd);
            }
        } catch (ONMException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToOtherPss(PSSAdvertisement pssAd) {
        String targetID = pssAd.getDpi();
        boolean istargetOtherPSS = true;
        boolean isAsync = false;
        ServiceMessage message = new ServiceMessage("Proxy Service", "org.personalsmartspace.rms050.onm.ISyncService", targetID, istargetOtherPSS, "sendMessageReturnEmbeddedInput", isAsync, new Object[] { XMLConverter.objectToXml(m_AdMgr) }, new String[] { "org.personalsmartspace.onm.api.platform.IAdvertisementMgr" });
        message.setEmbeddedParaTypes(new String[] { "org.personalsmartspace.onm.api.platform.IAdvertisementMgr" });
        logger.info("Message ready: " + message);
        try {
            logger.info("Message Queue Service: " + m_MessageQueue);
            m_MessageQueue.addServiceMessage(message, m_Callback);
        } catch (ONMException e) {
            e.printStackTrace();
        }
        logger.info("Message sent to " + targetID);
        m_messageSent = true;
    }

    @Override
    public void messageArrived(boolean result) {
        logger.info("Message Received!");
        m_answerReceived = result;
    }
}
