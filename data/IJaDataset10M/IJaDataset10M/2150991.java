package org.personalsmartspace.pss_stub_sm_composition.impl;

import java.util.ArrayList;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.personalsmartspace.pss_sm_composition.api.platform.ICompositionManager;
import org.personalsmartspace.pss_sm_composition.api.platform.IServiceBlackList;
import org.personalsmartspace.pss_sm_composition.api.platform.CompositionID;
import org.personalsmartspace.pss_sm_composition.api.platform.CompositionPlan;
import org.personalsmartspace.pss_sm_api.impl.PssService;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtException;
import org.personalsmartspace.pss_sm_dbc.impl.Dbc;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.api.pss3p.PssServiceIdentifier;
import static org.junit.Assert.*;

public class TestStubComposition {

    public static final String OPERATOR_ID_1 = "DPIOne";

    public static final String OPERATOR_ID_2 = "DPITwo";

    public static final String OPERATOR_ID_3 = "DPIThree";

    public static final String SERVICE_NAME_1 = "SampleService";

    public static final String SERVICE_NAME_2 = "SimpleService";

    public static final String SERVICE_NAME_3 = "OtherSampleService";

    public static final String SERVICE_NAME_4 = "RottenSampleService";

    public static final String SERVICE_NAME_5 = "GrotService";

    public static final String SERVICE_NAME_6 = "SampleService1";

    public static final String SERVICE_NAME_7 = "SimpleService1";

    public static final String SERVICE_NAME_8 = "OtherSampleService1";

    public static final String SERVICE_NAME_9 = "RottenSampleService1";

    public static final String SERVICE_NAME_10 = "GrotService1";

    private static final String SERVICE_TYPE = "Printer";

    private static final String ONTOLOGY_URI_1 = "file:///home/OWLS/SampleService.owl";

    private static final String ONTOLOGY_URI_2 = "file:///home/OWLS/SampleService.owl";

    private static final String ONTOLOGY_URI_3 = "file:///home/OWLS/AnOtherSampleService.owl";

    private static final String SERVICE_URI_1 = "file:///home/services/SampleService.jar";

    private static final String SERVICE_URI_2 = "file:///home/services/SimpleService.jar";

    private static final String SERVICE_URI_3 = "file:///home/services/AnotherSampleService.jar";

    private static final String PARAMETER_URI_1 = "file:///home/parameters/SampleService.par";

    private static final String PARAMETER_URI_2 = "file:///home/parameters/SimpleService.par";

    private static final String PARAMETER_URI_3 = "file:///home/parameters/AnotherSampleService.par";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testStubComposition() throws Exception {
        ICompositionManager manager = new CompositionManager(null);
        CompositionPlan plan = null;
        assertNotNull(manager);
        IServiceIdentifier serviceId = new PssServiceIdentifier(SERVICE_NAME_1, OPERATOR_ID_1);
        Dbc.ensure(null != serviceId.getOperatorId());
        Dbc.ensure(null != serviceId.getLocalServiceId());
        PssService service = new PssService(serviceId, SERVICE_TYPE, ONTOLOGY_URI_1, SERVICE_URI_1, PARAMETER_URI_1);
        assertNotNull(serviceId);
        assertNotNull(service);
        assertTrue(manager.composePlan(service, new Properties()) instanceof CompositionPlan);
        plan = manager.composePlan(service, new Properties());
        assertNotNull(plan);
        assertNotNull(manager.reComposePlan(new ArrayList<PssService>(), plan.getCompositionId()));
    }

    @Test(expected = ServiceMgmtException.class)
    public void testRecomposition() throws Exception {
        ICompositionManager manager = new CompositionManager(null);
        CompositionPlan plan = null;
        manager.reComposePlan(new ArrayList<PssService>(), new CompositionID());
    }

    @Test
    public void testStubServiceBlackList() throws Exception {
        IServiceBlackList blackList = new ServiceBlackList();
        CompositionID compID = new CompositionID();
        assertNotNull(blackList);
        assertFalse(blackList.remove(compID, new PssServiceIdentifier(SERVICE_NAME_1, OPERATOR_ID_1)));
        assertTrue(blackList.add(compID, new PssServiceIdentifier(SERVICE_NAME_1, OPERATOR_ID_1)));
        assertFalse(blackList.add(compID, new PssServiceIdentifier(SERVICE_NAME_1, OPERATOR_ID_1)));
        assertTrue(blackList.remove(compID, new PssServiceIdentifier(SERVICE_NAME_1, OPERATOR_ID_1)));
    }
}
