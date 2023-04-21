package org.personalsmartspace.ext.rms050;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.personalsmartspace.cm.api.pss3p.ContextException;
import org.personalsmartspace.cm.broker.api.platform.ICtxBroker;
import org.personalsmartspace.cm.model.api.pss3p.CtxAttributeTypes;
import org.personalsmartspace.cm.model.api.pss3p.CtxModelType;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttribute;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntityIdentifier;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;
import org.personalsmartspace.ext.Activator;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.pm.prefmodel.api.pss3p.Action;
import org.personalsmartspace.spm.privacy.api.platform.IPrivacyMgmt;
import org.personalsmartspace.spm.privacy.api.platform.PrivacyManagementException;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.api.pss3p.PssServiceIdentifier;
import org.personalsmartspace.ui.uim.api.pss3p.IUserInteractionMonitor;

public class TestPreferenceMerging extends TestCase {

    private BundleContext bc;

    private ServiceTracker uimFinder;

    private IUserInteractionMonitor uim;

    private ServiceTracker privacyFinder;

    private IPrivacyMgmt privacy;

    private ServiceTracker ctxBrokerFinder;

    private ICtxBroker ctxBroker;

    Map<Action, List<String>> userDataset;

    IDigitalPersonalIdentifier consumerDPI;

    IServiceIdentifier serviceId;

    String serviceType = "Test";

    private PSSLog logging = new PSSLog(this);

    public TestPreferenceMerging() {
        this.bc = Activator.bundleContext;
        this.userDataset = getUserDataset();
    }

    /**
     * @throws Exception
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        uimFinder = new ServiceTracker(bc, IUserInteractionMonitor.class.getName(), null);
        uimFinder.open();
        uim = (IUserInteractionMonitor) uimFinder.getService();
        privacyFinder = new ServiceTracker(bc, IPrivacyMgmt.class.getName(), null);
        privacyFinder.open();
        privacy = (IPrivacyMgmt) privacyFinder.getService();
        ctxBrokerFinder = new ServiceTracker(bc, ICtxBroker.class.getName(), null);
        ctxBrokerFinder.open();
        ctxBroker = (ICtxBroker) ctxBrokerFinder.getService();
        this.consumerDPI = this.retrieveConsumerDPI();
        serviceId = new PssServiceIdentifier("C45LearningTest", "Sarah");
    }

    /**
     * @throws Exception
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        uim = null;
        uimFinder.close();
    }

    /**
     * Test Preference Merging
     */
    public void testC45Methods() {
        logging.info("Starting Preference Merging Test");
        loadUserHistory();
        assertTrue(true);
    }

    private IDigitalPersonalIdentifier retrieveConsumerDPI() {
        IDigitalPersonalIdentifier dpi = null;
        final List<IDigitalPersonalIdentifier> sharedDpis = new ArrayList<IDigitalPersonalIdentifier>();
        try {
            final IDigitalPersonalIdentifier[] dpis = this.privacy.getAllDigitalPersonalIdentifiers();
            for (int i = 0; i < dpis.length; ++i) {
                if ((!dpis[i].equals(this.privacy.getPrivateDigitalPersonalIdentifier())) && !dpis[i].equals(this.privacy.getPublicDigitalPersonalIdentifier())) sharedDpis.add(dpis[i]);
            }
            if (sharedDpis.isEmpty()) {
                System.out.println("Could not get consumerDPI");
                return null;
            } else {
                return sharedDpis.get(0);
            }
        } catch (PrivacyManagementException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadUserHistory() {
        initialiseCtxAttr(CtxAttributeTypes.SYMBOLIC_LOCATION);
        initialiseCtxAttr(CtxAttributeTypes.STATUS);
        logging.info("USER ACTION dataset has size = " + userDataset.size());
        Iterator<Action> userDataset_it = userDataset.keySet().iterator();
        while (userDataset_it.hasNext()) {
            logging.info("Loading another context and USER action");
            Action action = (Action) userDataset_it.next();
            List<String> context = userDataset.get(action);
            String symLoc = context.get(0);
            String status = context.get(1);
            setContext(symLoc, status);
            logging.info("consumerDPI = " + consumerDPI.toString());
            uim.monitor(serviceId, serviceType, consumerDPI, action);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialiseCtxAttr(String attrName) {
        try {
            List<ICtxIdentifier> attrIds = ctxBroker.lookup(consumerDPI, CtxModelType.ATTRIBUTE, attrName);
            if (attrIds.size() <= 0) {
                logging.info("No " + attrName + " attribute found - creating");
                ICtxEntityIdentifier entity = ctxBroker.retrieveOperator().getCtxIdentifier();
                entity.setOperatorId(consumerDPI.toUriString());
                ICtxAttribute newAttr = ctxBroker.createAttribute(entity, attrName);
                logging.info("Created attribute: " + newAttr.toString());
            }
        } catch (ContextException e) {
            e.printStackTrace();
        }
    }

    private void setContext(String symLoc, String status) {
        setAttr(CtxAttributeTypes.SYMBOLIC_LOCATION, symLoc);
        setAttr(CtxAttributeTypes.STATUS, status);
    }

    private void setAttr(String attrName, String value) {
        try {
            List<ICtxIdentifier> attrIds = ctxBroker.lookup(consumerDPI, CtxModelType.ATTRIBUTE, attrName);
            if (attrIds.size() > 0) {
                ICtxIdentifier id = attrIds.get(0);
                System.out.println("EntityID: " + id.toString());
                ICtxAttribute attribute = (ICtxAttribute) ctxBroker.retrieve(id);
                attribute.setBlobValue(value);
                ctxBroker.update(attribute);
            }
        } catch (ContextException e) {
            e.printStackTrace();
        }
    }

    private Map<Action, List<String>> getUserDataset() {
        Map<Action, List<String>> dataset = new LinkedHashMap<Action, List<String>>();
        ArrayList<String> context1 = new ArrayList<String>();
        context1.add("home");
        context1.add("free");
        dataset.put(new Action("volume", "unmute"), context1);
        ArrayList<String> context2 = new ArrayList<String>();
        context2.add("home");
        context2.add("free");
        dataset.put(new Action("volume", "unmute"), context2);
        ArrayList<String> context3 = new ArrayList<String>();
        context3.add("gym");
        context3.add("free");
        dataset.put(new Action("volume", "mute"), context3);
        ArrayList<String> context4 = new ArrayList<String>();
        context4.add("work");
        context4.add("free");
        dataset.put(new Action("volume", "mute"), context4);
        ArrayList<String> context5 = new ArrayList<String>();
        context5.add("work");
        context5.add("free");
        dataset.put(new Action("volume", "mute"), context5);
        ArrayList<String> context6 = new ArrayList<String>();
        context6.add("work");
        context6.add("busy");
        dataset.put(new Action("volume", "unmute"), context6);
        ArrayList<String> context7 = new ArrayList<String>();
        context7.add("gym");
        context7.add("busy");
        dataset.put(new Action("volume", "mute"), context7);
        ArrayList<String> context8 = new ArrayList<String>();
        context8.add("home");
        context8.add("free");
        dataset.put(new Action("volume", "unmute"), context8);
        ArrayList<String> context9 = new ArrayList<String>();
        context9.add("home");
        context9.add("busy");
        dataset.put(new Action("volume", "mute"), context9);
        ArrayList<String> context10 = new ArrayList<String>();
        context10.add("work");
        context10.add("free");
        dataset.put(new Action("volume", "mute"), context10);
        ArrayList<String> context11 = new ArrayList<String>();
        context11.add("home");
        context11.add("busy");
        dataset.put(new Action("volume", "mute"), context11);
        ArrayList<String> context12 = new ArrayList<String>();
        context12.add("gym");
        context12.add("free");
        dataset.put(new Action("volume", "mute"), context12);
        ArrayList<String> context13 = new ArrayList<String>();
        context13.add("gym");
        context13.add("busy");
        dataset.put(new Action("sound", "mute"), context13);
        ArrayList<String> context14 = new ArrayList<String>();
        context14.add("work");
        context14.add("free");
        dataset.put(new Action("sound", "unmute"), context14);
        return dataset;
    }
}
