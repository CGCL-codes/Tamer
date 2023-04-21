package org.personalsmartspace.impl;

import java.util.Hashtable;
import junit.framework.TestSuite;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.personalsmartspace.rms010.AdminGUIFrameworkTest;
import org.personalsmartspace.rms010.CompositionMgmtIntegrationTest;
import org.personalsmartspace.rms010.ContextBrokerIntegrationTest;
import org.personalsmartspace.rms010.ContextChangeIntegrationTest;
import org.personalsmartspace.rms010.ONMandEMSIntegrationTest;
import org.personalsmartspace.rms010.PrivacyManagerTest;
import org.personalsmartspace.rms010.PssGroupManagementTest;
import org.personalsmartspace.rms010.ServiceDiscoveryIntegrationTest;
import org.personalsmartspace.rms010.ServiceLifecycleManagerTest;
import org.personalsmartspace.rms010.SessionMgmtIntegrationTest;
import org.personalsmartspace.rms020.CompositionMgmtIntegrationTest_020;
import org.personalsmartspace.rms020.PssManagerIntegrationTestPeer_1_020;
import org.personalsmartspace.rms020.PssManagerIntegrationTestPeer_2_020;
import org.personalsmartspace.rms020.ServiceDiscoveryIntegrationTest_020;
import org.personalsmartspace.rms020.ServiceLifecycleManagerTest_020;
import org.personalsmartspace.rms020.SessionMgmtIntegrationTest_020;
import org.personalsmartspace.rms030.PssGroupManagementTest_030;
import org.personalsmartspace.rms030.ServiceBrowserIntegrationTest_030;
import org.personalsmartspace.rms030.ServiceControlIntegrationTest_030;
import org.personalsmartspace.rms030.ServiceLifecycleManagerTest_030;
import org.personalsmartspace.rms031.AccessCtrlToPolicyMgrTest;
import org.personalsmartspace.rms031.ContextIdentity;
import org.personalsmartspace.rms031.PCMTest;
import org.personalsmartspace.rms031.ProactivityTest;
import org.personalsmartspace.rms040.ExternalPSStestsPssA;
import org.personalsmartspace.rms040.ExternalPSStestsPssB;
import org.personalsmartspace.rms040.InternalPSStestsPeerA;
import org.personalsmartspace.rms040.InternalPSStestsPeerB;
import org.personalsmartspace.rms040.SlmAfterInstallService;
import org.personalsmartspace.rms040.SlmInstallService;
import org.personalsmartspace.rms040.SlmShowGuiOnAllPeers;
import org.personalsmartspace.rms050.AddPreferenceTest;
import org.personalsmartspace.rms050.ConflictResolutionTest;
import org.personalsmartspace.rms050.DIANNELearningTest;
import org.personalsmartspace.rms050.FeedbackMgmtTest;
import org.personalsmartspace.rms050.ServiceRegistryTest;
import org.personalsmartspace.rms050.SetupInterCtx;
import org.personalsmartspace.rms050.SlmMoveGui;
import org.personalsmartspace.rms050.SlmStartUserSession;
import org.personalsmartspace.rms050.SlmStopUserSession;
import org.personalsmartspace.rms050.TestCtxInterPlatform;
import org.personalsmartspace.rms050.TestCtxInterPss;
import org.personalsmartspace.rms050.TestDPIContextEventing;
import org.personalsmartspace.rms050.TestRemoteNegotiationClient;
import org.personalsmartspace.rms050.TestRetrieveCtxModObj;
import org.personalsmartspace.rms050.TestStaticContext;
import org.personalsmartspace.rms050.TestTrustPssA;
import org.personalsmartspace.rms050.TestUpdateCtxModObj;
import org.personalsmartspace.rms060.TestBayesianInference;
import org.personalsmartspace.rms060.TestBayesianInferenceTask;
import org.personalsmartspace.rms060.TestContinuousInferenceRegistration;
import org.personalsmartspace.rms060.TestCtxAttributeSynchPerformance;
import org.personalsmartspace.rms060.TestCtxBrokerReasoning;
import org.personalsmartspace.rms060.TestCtxDBManagerPerformance;
import org.personalsmartspace.rms060.TestCtxSlaveStaticAttributeCRUD;
import org.personalsmartspace.rms060.TestCtxSlaveStaticAttributeCRUD3p;
import org.personalsmartspace.rms060.TestCtxSourceContinuousInference;
import org.personalsmartspace.rms060.TestInferSymLocFromGPSLocation;
import org.personalsmartspace.rms060.TestLocationSelection;
import org.personalsmartspace.rms060.TestUbisenseLocationSelection;
import org.personalsmartspace.rms060.TestPreferenceManagerEnhancements;
import org.personalsmartspace.rms060.TestResourceSharing;
import org.personalsmartspace.rms060.TestTILocationInference;

/**
 * Activator
 * @author fmahon
 *
 */
public class Activator implements BundleActivator {

    private static final String SERVICE_PID = "service.pid";

    public static BundleContext bundleContext;

    public void start(BundleContext context) throws Exception {
        bundleContext = context;
        this.registerRMS010Suites(context);
        this.registerRMS020Suites(context);
        this.registerRMS030Suites(context);
        this.registerRMS031Suites(context);
        this.registerRMS040Suites(context);
        this.registerRMS050Suites(context);
        this.registerRMS060Suites(context);
        this.registerStartChecks(context);
    }

    public void stop(BundleContext context) throws Exception {
    }

    /** This is a generic method for registering test suites
     * @param context The bundle context
     * @param suiteName The name of the test suite
     * @param clazzes The class objects of the testSuites in the UnitTest
     * */
    private void registerSuite(BundleContext context, String release, String suiteName, Class[] clazzes) {
        final TestSuite testSuite = new TestSuite("[" + release + "] " + suiteName);
        for (Class testSuiteClass : clazzes) {
            testSuite.addTestSuite(testSuiteClass);
        }
        Hashtable props = new Hashtable();
        props.put(SERVICE_PID, testSuite.getName());
        context.registerService(TestSuite.class.getName(), testSuite, props);
    }

    /** This is a generic method for registering test suites
     * @param context The bundle context
     * @param suiteName The name of the test suite
     * @param clazz The class object of the testSuite in the UnitTest
     * */
    private void registerSuite(BundleContext context, String release, String suiteName, Class clazz) {
        this.registerSuite(context, release, suiteName, new Class[] { clazz });
    }

    private void registerRMS010Suites(BundleContext context) {
        final String release = "010";
        final TestSuite adminGUIFrameworkSuite = new TestSuite("ServiceRuntimeEnv:TestAdminGUIFramework");
        adminGUIFrameworkSuite.addTest(new AdminGUIFrameworkTest(context));
        final Hashtable adminGUIFrameworkProps = new Hashtable();
        adminGUIFrameworkProps.put(SERVICE_PID, adminGUIFrameworkSuite.getName());
        context.registerService(TestSuite.class.getName(), adminGUIFrameworkSuite, adminGUIFrameworkProps);
        this.registerSuite(context, release, "ServiceRuntimeEnv:TestServiceLifecycleMgmt_010", ServiceLifecycleManagerTest.class);
        this.registerSuite(context, release, "ServiceMgmt:TestServiceDiscovery", new Class[] { ServiceDiscoveryIntegrationTest.class, CompositionMgmtIntegrationTest.class, SessionMgmtIntegrationTest.class });
        this.registerSuite(context, release, "ContextMgmt:TestContextBroker", ContextBrokerIntegrationTest.class);
        this.registerSuite(context, release, "ContextMgmt:TestContextChange", ContextChangeIntegrationTest.class);
        final TestSuite securityPrivacyMgmtSuite = new TestSuite("SecurityPrivacyManagement:TestPrivacyManager");
        securityPrivacyMgmtSuite.addTest(new PrivacyManagerTest(context));
        final Hashtable securityPrivacyMgmtProps = new Hashtable();
        securityPrivacyMgmtProps.put(SERVICE_PID, securityPrivacyMgmtSuite.getName());
        context.registerService(TestSuite.class.getName(), securityPrivacyMgmtSuite, securityPrivacyMgmtProps);
        this.registerSuite(context, release, "PssGroupManagement:Initial_Tests", PssGroupManagementTest.class);
        this.registerSuite(context, release, "ONMandEMS:ONMandEMSIntegrationTest", ONMandEMSIntegrationTest.class);
    }

    private void registerRMS020Suites(BundleContext context) {
        final String release = "020";
        this.registerSuite(context, release, "ServiceMgmt:TestServiceDiscovery-020", ServiceDiscoveryIntegrationTest_020.class);
        this.registerSuite(context, release, "PSSMgmt:TestPssManagerPeer1-020", PssManagerIntegrationTestPeer_1_020.class);
        this.registerSuite(context, release, "ServiceMgmt:TestSessionManager-020", SessionMgmtIntegrationTest_020.class);
        this.registerSuite(context, release, "ServiceMgmt:TestCompositionManager-020", CompositionMgmtIntegrationTest_020.class);
        this.registerSuite(context, release, "PSSMgmt:TestPssManagerPeer2-020", PssManagerIntegrationTestPeer_2_020.class);
        this.registerSuite(context, release, "ServiceRuntimeEnv:TestServiceLifecycleMgmt_020", ServiceLifecycleManagerTest_020.class);
    }

    private void registerRMS030Suites(BundleContext context) {
        final String release = "030";
        this.registerSuite(context, release, "ServiceMgmt:TestServiceControl-030", ServiceControlIntegrationTest_030.class);
        this.registerSuite(context, release, "ServiceMgmt:TestServiceBrowser-030", ServiceBrowserIntegrationTest_030.class);
        this.registerSuite(context, release, "ServiceRuntimeEnv:TestServiceLifecycleMgmt_030", ServiceLifecycleManagerTest_030.class);
        this.registerSuite(context, release, "PssGroupManagement:PssGroupManagementTest_030", PssGroupManagementTest_030.class);
    }

    private void registerRMS031Suites(BundleContext context) {
        final String release = "031";
        this.registerSuite(context, release, "ProactivityTest:ProactivityTest", ProactivityTest.class);
        this.registerSuite(context, release, "AccessCtrlToPolicyMgrTest:AccessCtrlToPolicyMgrTest", AccessCtrlToPolicyMgrTest.class);
        this.registerSuite(context, release, "PCMTest:PCMTest", PCMTest.class);
        this.registerSuite(context, release, "ContextMgmt:TestContextIdentity", ContextIdentity.class);
    }

    private void registerRMS040Suites(BundleContext context) {
        final String release = "040";
        this.registerSuite(context, release, "ONM:ONMPeerA", InternalPSStestsPeerA.class);
        this.registerSuite(context, release, "ONM:ONMPeerB", InternalPSStestsPeerB.class);
        this.registerSuite(context, release, "ONM:ONMPssA", ExternalPSStestsPssA.class);
        this.registerSuite(context, release, "ONM:ONMPssB", ExternalPSStestsPssB.class);
        this.registerSuite(context, release, "ServiceRuntimeEnv:SlmInstallService", SlmInstallService.class);
        this.registerSuite(context, release, "ServiceRuntimeEnv:SlmAfterInstallService", SlmAfterInstallService.class);
        this.registerSuite(context, release, "ServiceRuntimeEnv:SlmShowGuiOnAllPeers", SlmShowGuiOnAllPeers.class);
    }

    public void registerRMS050Suites(BundleContext context) {
        final String release = "050";
        this.registerSuite(context, release, "UserInteraction:FeedbackMgmtTest", FeedbackMgmtTest.class);
        this.registerSuite(context, release, "ContextMgmt:SetupInterCtx", SetupInterCtx.class);
        this.registerSuite(context, release, "ContextMgmt:TestInterPlatform", TestCtxInterPlatform.class);
        this.registerSuite(context, release, "ContextMgmt:TestInterPss", TestCtxInterPss.class);
        this.registerSuite(context, release, "ContextMgmt:TestStaticContext", TestStaticContext.class);
        this.registerSuite(context, release, "RemoteNegotiation:RemoteNegotiationClientTest", TestRemoteNegotiationClient.class);
        this.registerSuite(context, release, "ContextMgmt:TestDPIContextEventing", TestDPIContextEventing.class);
        this.registerSuite(context, release, "ServiceMgmt:TestServiceRegistry", ServiceRegistryTest.class);
        this.registerSuite(context, release, "SPM:TestTrustPssA", TestTrustPssA.class);
        this.registerSuite(context, release, "Proactivity:ConflictResolutionTest", ConflictResolutionTest.class);
        this.registerSuite(context, release, "ServiceRuntimeEnv:SlmMoveGui", SlmMoveGui.class);
        this.registerSuite(context, release, "LearningMgmt:TestDianneLearning", DIANNELearningTest.class);
        this.registerSuite(context, release, "ContextMgmt:TestCreateUpdateCtxMO", TestUpdateCtxModObj.class);
        this.registerSuite(context, release, "ContextMgmt:TestRetrieveCtxMO", TestRetrieveCtxModObj.class);
        this.registerSuite(context, release, "PreferencesMgmt:TestAddPreference", AddPreferenceTest.class);
        this.registerSuite(context, release, "ServiceRuntimeEnv:SlmStartUserSession", SlmStartUserSession.class);
        this.registerSuite(context, release, "ServiceRuntimeEnv:SlmStopUserSession", SlmStopUserSession.class);
    }

    public void registerRMS060Suites(BundleContext context) {
        final String release = "060";
        this.registerSuite(context, release, "Personalisation:PrefMgrEnhancements", TestPreferenceManagerEnhancements.class);
        this.registerSuite(context, release, "ReasoningManager:ContinuousInferenceRegistration", TestContinuousInferenceRegistration.class);
        this.registerSuite(context, release, "ReasoningManager:CtxSourceContinuousInference", TestCtxSourceContinuousInference.class);
        this.registerSuite(context, release, "ReasoningManager:BayesianInferenceStatus", TestBayesianInference.class);
        this.registerSuite(context, release, "ContextMgmt:CtxAttributeSynchPerformance", TestCtxAttributeSynchPerformance.class);
        this.registerSuite(context, release, "ContextMgmt:CtxSlaveStaticAttributeCRUD", TestCtxSlaveStaticAttributeCRUD.class);
        this.registerSuite(context, release, "ContextMgmt:CtxSlaveStaticAttributeCRUD3p", TestCtxSlaveStaticAttributeCRUD3p.class);
        this.registerSuite(context, release, "ContextMgmt:CtxBrokerReasoning", TestCtxBrokerReasoning.class);
        this.registerSuite(context, release, "ReasoningManager:LocationTagging", TestTILocationInference.class);
        this.registerSuite(context, release, "ReasoningManager:LocationSelection", TestLocationSelection.class);
        this.registerSuite(context, release, "ReasoningManager:UbisenseLocationSelection", TestUbisenseLocationSelection.class);
        this.registerSuite(context, release, "ContextMgmt:ContextDBMgmtPerformance", TestCtxDBManagerPerformance.class);
        this.registerSuite(context, release, "ReasoningManager:BayesianInferenceTask", TestBayesianInferenceTask.class);
        this.registerSuite(context, release, "ReasoningManager:InferSymLocFromGPSLocation", TestInferSymLocFromGPSLocation.class);
        this.registerSuite(context, release, "PSSMgmt:ResourceSharingManager", TestResourceSharing.class);
    }

    private void registerStartChecks(BundleContext context) {
        final TestSuite testSuite = new TestSuite("CheckStart");
        testSuite.addTestSuite(TestPssStandardStarted.class);
        Hashtable props = new Hashtable();
        props.put(SERVICE_PID, testSuite.getName());
        context.registerService(TestSuite.class.getName(), testSuite, props);
    }
}
