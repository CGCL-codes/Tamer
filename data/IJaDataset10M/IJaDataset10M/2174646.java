package org.personalsmartspace.rms060;

import java.util.List;
import junit.framework.TestCase;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.personalsmartspace.cm.api.pss3p.ContextException;
import org.personalsmartspace.cm.broker.api.platform.ICtxBroker;
import org.personalsmartspace.cm.model.api.pss3p.CtxAttributeTypes;
import org.personalsmartspace.cm.model.api.pss3p.CtxModelType;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttribute;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttributeIdentifier;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;
import org.personalsmartspace.impl.Activator;
import org.personalsmartspace.pm.prefmgr.api.platform.IPreferenceHandler;
import org.personalsmartspace.pm.prefmodel.api.platform.ContextPreferenceCondition;
import org.personalsmartspace.pm.prefmodel.api.platform.IPreference;
import org.personalsmartspace.pm.prefmodel.api.platform.OperatorConstants;
import org.personalsmartspace.pm.prefmodel.api.platform.PreferenceDetails;
import org.personalsmartspace.pm.prefmodel.api.platform.PreferenceOutcome;
import org.personalsmartspace.pm.prefmodel.api.platform.PreferenceTreeNode;
import org.personalsmartspace.pm.prefmodel.api.pss3p.IAction;
import org.personalsmartspace.spm.identity.api.platform.IIdentityManagement;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.api.pss3p.PssServiceIdentifier;

public class TestPreferenceManagerEnhancements extends TestCase {

    private ICtxBroker broker;

    private IIdentityManagement IDM;

    private IPreferenceHandler prefMgr;

    private BundleContext myContext;

    private IDigitalPersonalIdentifier consumerDPI;

    private IDigitalPersonalIdentifier publicDPI;

    private IServiceIdentifier serviceID;

    private String serviceType;

    private ICtxAttribute symlocCtxAttr;

    private String preferenceName;

    private static final String home = "home";

    private static final String work = "work";

    public TestPreferenceManagerEnhancements() {
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.myContext = Activator.bundleContext;
        this.broker = (ICtxBroker) this.getService(ICtxBroker.class.getName());
        this.IDM = (IIdentityManagement) this.getService(IIdentityManagement.class.getName());
        this.prefMgr = (IPreferenceHandler) this.getService(IPreferenceHandler.class.getName());
        this.consumerDPI = this.IDM.getLocalDigitalPersonalIdentifier();
        this.publicDPI = this.IDM.getPublicDigitalPersonalIdentifier();
        this.setupServiceDetails();
        this.setupPreferences();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private void setupServiceDetails() {
        serviceType = "audio";
        String id = "pss://" + publicDPI.toUriString() + "@" + "026877415026291251";
        System.out.println("\n\nAttempting to create serviceID: " + id + "\n\n");
        serviceID = new PssServiceIdentifier(id);
        preferenceName = "volume";
    }

    private Object getService(String serviceClassName) {
        ServiceTracker servTracker = new ServiceTracker(this.myContext, serviceClassName, null);
        servTracker.open();
        Object[] services = servTracker.getServices();
        return services[0];
    }

    private void setupPreferences() {
        this.getCtxIdentifier(CtxAttributeTypes.SYMBOLIC_LOCATION);
        ICtxAttributeIdentifier symlocId = this.symlocCtxAttr.getCtxIdentifier();
        IPreference condition1 = new PreferenceTreeNode(new ContextPreferenceCondition(symlocId, OperatorConstants.EQUALS, home, CtxAttributeTypes.SYMBOLIC_LOCATION));
        IPreference condition2 = new PreferenceTreeNode(new ContextPreferenceCondition(symlocId, OperatorConstants.EQUALS, work, CtxAttributeTypes.SYMBOLIC_LOCATION));
        IPreference outcome1 = new PreferenceTreeNode(new PreferenceOutcome(preferenceName, "100"));
        IPreference outcome2 = new PreferenceTreeNode(new PreferenceOutcome(preferenceName, "0"));
        IPreference preference = new PreferenceTreeNode();
        preference.add(condition1);
        condition1.add(outcome1);
        preference.add(condition2);
        condition2.add(outcome2);
        System.out.println("Storing preference \n" + preference.toTreeString() + "for DPI: " + consumerDPI.toString());
        PreferenceDetails detail = new PreferenceDetails(serviceType, serviceID, preferenceName);
        prefMgr.updatePreference(consumerDPI, detail, preference);
    }

    private void getCtxIdentifier(String type) {
        ICtxIdentifier identifier = null;
        try {
            List<ICtxIdentifier> attrIds = broker.lookup(CtxModelType.ATTRIBUTE, type);
            if (attrIds.size() > 0) {
                identifier = attrIds.get(0);
                this.symlocCtxAttr = (ICtxAttribute) broker.retrieve(identifier);
            }
        } catch (ContextException e) {
            e.printStackTrace();
        }
    }

    private void changeValueofCtxAttribute(String value) {
        try {
            this.symlocCtxAttr.setStringValue(value);
            this.symlocCtxAttr = (ICtxAttribute) this.broker.update(this.symlocCtxAttr);
            System.out.println("Context value of symloc: " + symlocCtxAttr.getStringValue());
            Thread.sleep(1000);
        } catch (ContextException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testEvaluation1() {
        System.out.println("\nStarting 1st test with symloc = home");
        this.changeValueofCtxAttribute(home);
        IAction action = this.prefMgr.getPreference(this.publicDPI, this.consumerDPI, serviceType, serviceID, preferenceName);
        if (action == null) {
            fail("Action is null");
        } else {
            System.out.println("Received action: " + action.toString());
        }
    }

    public void testEvaluation2() {
        System.out.println("\nStarting 2nd test with symloc = work");
        this.changeValueofCtxAttribute(work);
        IAction action2 = this.prefMgr.getPreference(this.publicDPI, this.consumerDPI, serviceType, serviceID, preferenceName);
        if (action2 == null) {
            fail("Action is null");
        } else {
            System.out.println("Received action: " + action2.toString() + "\n");
        }
    }
}
