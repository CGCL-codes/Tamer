package org.personalsmartspace.pss_psm_pssmanager.impl;

import java.util.Properties;
import org.personalsmartspace.pss_sm_api.impl.PssService;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.api.pss3p.PssServiceIdentifier;

/**
 * @author awalsh
 *
 */
public class Utils {

    public static final String PSS_ID = "gbjugh7uy78hunb78y78KJNJ";

    public static final String PSS_ID_1 = "gbjugh7uy78hunb78y78KJNJ";

    public static final String PSS_ID_2 = "fsfbnsdjmnvbsd";

    public static final String PEER_ID_1 = "Peer1";

    public static final String PEER_ID_2 = "Peer2";

    public static final String PEER_ID_3 = "Peer3";

    public static final String SERVICE_NAME_1 = "eu.ict.persist.SampleService";

    public static final String SERVICE_NAME_2 = "eu.ict.persist.SimpleService";

    public static final String SERVICE_NAME_3 = "eu.ict.persist.OtherSampleService";

    public static final String SERVICE_NAME_4 = "eu.ict.persist.RottenSampleService";

    public static final String SERVICE_NAME_5 = "eu.ict.persist.GrotService";

    public static final String SERVICE_NAME_6 = "org.ict.persist.SampleService";

    public static final String SERVICE_NAME_7 = "org.ict.persist.SimpleService";

    public static final String SERVICE_NAME_8 = "org.ict.persist.OtherSampleService";

    public static final String SERVICE_NAME_9 = "org.ict.persist.RottenSampleService";

    public static final String SERVICE_NAME_10 = "org.ict.persist.GrotService";

    public static final String SERVICE_NAME_11 = "eeu.ict.persist.SampleService";

    public static final String SERVICE_NAME_12 = "eeu.ict.persist.SimpleService";

    public static final String SERVICE_NAME_13 = "eeu.ict.persist.OtherSampleService";

    public static final String SERVICE_NAME_14 = "eeu.ict.persist.RottenSampleService";

    public static final String SERVICE_NAME_15 = "eeu.ict.persist.GrotService";

    public static final String SERVICE_NAME_16 = "eorg.ict.persist.SampleService";

    public static final String SERVICE_NAME_17 = "eorg.ict.persist.SimpleService";

    public static final String SERVICE_NAME_18 = "eorg.ict.persist.OtherSampleService";

    public static final String SERVICE_NAME_19 = "eorg.ict.persist.RottenSampleService";

    public static final String SERVICE_NAME_20 = "eorg.ict.persist.GrotService";

    public static final String SERVICE_TYPE = "Printer";

    public static final String SERVICE_DESCRIPTION_1 = "A Really simple service";

    public static final String SERVICE_DESCRIPTION_2 = "A very dificult service";

    public static final String SERVICE_DESCRIPTION_3 = "Very recalcatrant service";

    public static final String SERVICE_DESCRIPTION_4 = "Rotten service";

    public static final String SERVICE_DESCRIPTION_5 = "Grotty service";

    public static final String SERVICE_DESCRIPTION_6 = "AAA Really simple service";

    public static final String SERVICE_DESCRIPTION_7 = "AAAA very dificult service";

    public static final String SERVICE_DESCRIPTION_8 = "AAAAAVery recalcatrant service";

    public static final String SERVICE_DESCRIPTION_9 = "AARotten service";

    public static final String SERVICE_DESCRIPTION_10 = "AGrotty service";

    public static final String SERVICE_DESCRIPTION_11 = "BA Really simple service";

    public static final String SERVICE_DESCRIPTION_12 = "BA very dificult service";

    public static final String SERVICE_DESCRIPTION_13 = "BVery recalcatrant service";

    public static final String SERVICE_DESCRIPTION_14 = "BRotten service";

    public static final String SERVICE_DESCRIPTION_15 = "BGrotty service";

    public static final String SERVICE_DESCRIPTION_16 = "BAAA Really simple service";

    public static final String SERVICE_DESCRIPTION_17 = "BAAAA very dificult service";

    public static final String SERVICE_DESCRIPTION_18 = "BAAAAAVery recalcatrant service";

    public static final String SERVICE_DESCRIPTION_19 = "BAARotten service";

    public static final String SERVICE_DESCRIPTION_20 = "BAGrotty service";

    public static final String SERVICE_TYPE_1 = "Printer";

    public static final String SERVICE_TYPE_2 = "Welder";

    public static final String ONTOLOGY_URI_1 = "file:///home/OWLS/SampleService.owl";

    public static final String ONTOLOGY_URI_2 = "file:///home/OWLS/SampleService.owl";

    public static final String ONTOLOGY_URI_3 = "file:///home/OWLS/AnOtherSampleService.owl";

    public static final String ONTOLOGY_URI_4 = "file:///home/OWLS/RottenService.owl";

    public static final String ONTOLOGY_URI_5 = "file:///home/OWLS/GrotService.owl";

    public static final String ONTOLOGY_URI_6 = "file:///home/OWLS/ASampleService.owl";

    public static final String ONTOLOGY_URI_7 = "file:///home/OWLS/AampleService.owl";

    public static final String ONTOLOGY_URI_8 = "file:///home/OWLS/AAnOtherSampleService.owl";

    public static final String ONTOLOGY_URI_9 = "file:///home/OWLS/ARottenService.owl";

    public static final String ONTOLOGY_URI_10 = "file:///home/OWLS/AGrotService.owl";

    public static final String ONTOLOGY_URI_11 = "file:///homey/OWLS/SampleService.owl";

    public static final String ONTOLOGY_URI_12 = "file:///homey/OWLS/SampleService.owl";

    public static final String ONTOLOGY_URI_13 = "file:///homey/OWLS/AnOtherSampleService.owl";

    public static final String ONTOLOGY_URI_14 = "file:///homey/OWLS/RottenService.owl";

    public static final String ONTOLOGY_URI_15 = "file:///homey/OWLS/GrotService.owl";

    public static final String ONTOLOGY_URI_16 = "file:///homey/OWLS/ASampleService.owl";

    public static final String ONTOLOGY_URI_17 = "file:///homey/OWLS/AampleService.owl";

    public static final String ONTOLOGY_URI_18 = "file:///homey/OWLS/AAnOtherSampleService.owl";

    public static final String ONTOLOGY_URI_19 = "file:///homey/OWLS/ARottenService.owl";

    public static final String ONTOLOGY_URI_20 = "file:///homey/OWLS/AGrotService.owl";

    public static final String SERVICE_URI_1 = "file:///home/services/SampleService.jar";

    public static final String SERVICE_URI_2 = "file:///home/services/SimpleService.jar";

    public static final String SERVICE_URI_3 = "file:///home/services/AnotherSampleService.jar";

    public static final String SERVICE_URI_4 = "file:///home/services/RottenService.jar";

    public static final String SERVICE_URI_5 = "file:///home/services/GrotService.jar";

    public static final String SERVICE_URI_6 = "file:///home/services/ASampleService.jar";

    public static final String SERVICE_URI_7 = "file:///home/services/ASimpleService.jar";

    public static final String SERVICE_URI_8 = "file:///home/services/AAnotherSampleService.jar";

    public static final String SERVICE_URI_9 = "file:///home/services/ARottenService.jar";

    public static final String SERVICE_URI_10 = "file:///home/services/AGrotService.jar";

    public static final String SERVICE_URI_11 = "file:///homey/services/SampleService.jar";

    public static final String SERVICE_URI_12 = "file:///homey/services/SimpleService.jar";

    public static final String SERVICE_URI_13 = "file:///homey/services/AnotherSampleService.jar";

    public static final String SERVICE_URI_14 = "file:///homey/services/RottenService.jar";

    public static final String SERVICE_URI_15 = "file:///homey/services/GrotService.jar";

    public static final String SERVICE_URI_16 = "file:///homey/services/ASampleService.jar";

    public static final String SERVICE_URI_17 = "file:///homey/services/ASimpleService.jar";

    public static final String SERVICE_URI_18 = "file:///homey/services/AAnotherSampleService.jar";

    public static final String SERVICE_URI_19 = "file:///homey/services/ARottenService.jar";

    public static final String SERVICE_URI_20 = "file:///homey/services/AGrotService.jar";

    public static final String VERSION_NO_1 = "90.545.33.3(a)";

    public static final String VERSION_NO_2 = "1.3.4";

    public static final String VERSION_NO_3 = "45/555";

    public static final String VERSION_NO_4 = "4.6565";

    public static final String VERSION_NO_5 = "44tg34t34";

    public static final String VERSION_NO_6 = "1.90.545.33.3(a)";

    public static final String VERSION_NO_7 = "11.3.4";

    public static final String VERSION_NO_8 = "1.45/555";

    public static final String VERSION_NO_9 = "1.4.6565";

    public static final String VERSION_NO_10 = "1/44tg34t34";

    public static final String VERSION_NO_11 = "1.90.545.33.3(a)";

    public static final String VERSION_NO_12 = "1.1.3.4";

    public static final String VERSION_NO_13 = "1.45/555";

    public static final String VERSION_NO_14 = "1.4.6565";

    public static final String VERSION_NO_15 = "1.44tg34t34";

    public static final String VERSION_NO_16 = "1.1.90.545.33.3(a)";

    public static final String VERSION_NO_17 = "1.11.3.4";

    public static final String VERSION_NO_18 = "1.1.45/555";

    public static final String VERSION_NO_19 = "1.1.4.6565";

    public static final String VERSION_NO_20 = "1.1/44tg34t34";

    public static final String PROP_1_KEY_1 = "lang";

    public static final String PROP_1_KEY_2 = "Printer";

    public static final String PROP_1_KEY_3 = "Font";

    public static final String PROP_1_VALUE_1 = "EN";

    public static final String PROP_1_VALUE_2 = "HP laser";

    public static final String PROP_1_VALUE_3 = "Times New Roman";

    public static final String PROP_2_KEY_1 = "option";

    public static final String PROP_2_VALUE_1 = "1";

    private Utils() {
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_1() {
        Properties serviceProps;
        serviceProps = new Properties();
        serviceProps.setProperty(PROP_1_KEY_1, PROP_1_VALUE_1);
        serviceProps.setProperty(PROP_1_KEY_2, PROP_1_VALUE_2);
        serviceProps.setProperty(PROP_1_KEY_3, PROP_1_VALUE_3);
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_3, SERVICE_NAME_1);
        PssService service = new PssService(serviceId, SERVICE_TYPE_1, ONTOLOGY_URI_1, SERVICE_URI_1);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_1);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_1);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_2() {
        Properties serviceProps;
        serviceProps = new Properties();
        serviceProps.setProperty(PROP_2_KEY_1, PROP_2_VALUE_1);
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_2);
        PssService service = new PssService(serviceId, SERVICE_TYPE_1, ONTOLOGY_URI_2, SERVICE_URI_2);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_2);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_2);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_3() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_1, SERVICE_NAME_3);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_3, SERVICE_URI_3);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_3);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_3);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_4() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_3, SERVICE_NAME_4);
        PssService service = new PssService(serviceId, SERVICE_TYPE_1, ONTOLOGY_URI_4, SERVICE_URI_4);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_4);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_4);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_5() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_5);
        PssService service = new PssService(serviceId, SERVICE_TYPE_1, ONTOLOGY_URI_5, SERVICE_URI_5);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_5);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_5);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_6() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_6);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_6, SERVICE_URI_6);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_6);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_6);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_7() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_7);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_7, SERVICE_URI_7);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_7);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_7);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_8() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_8);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_8, SERVICE_URI_8);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_8);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_8);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_9() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_9);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_9, SERVICE_URI_9);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_9);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_9);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_10() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_10);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_10, SERVICE_URI_10);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_10);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_10);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_11() {
        Properties serviceProps;
        serviceProps = new Properties();
        serviceProps.setProperty(PROP_1_KEY_1, PROP_1_VALUE_1);
        serviceProps.setProperty(PROP_1_KEY_2, PROP_1_VALUE_2);
        serviceProps.setProperty(PROP_1_KEY_3, PROP_1_VALUE_3);
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_3, SERVICE_NAME_11);
        PssService service = new PssService(serviceId, SERVICE_TYPE_1, ONTOLOGY_URI_11, SERVICE_URI_11);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_11);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_11);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_12() {
        Properties serviceProps;
        serviceProps = new Properties();
        serviceProps.setProperty(PROP_2_KEY_1, PROP_2_VALUE_1);
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_12);
        PssService service = new PssService(serviceId, SERVICE_TYPE_1, ONTOLOGY_URI_12, SERVICE_URI_12);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_12);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_12);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_13() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_1, SERVICE_NAME_13);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_13, SERVICE_URI_13);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_13);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_13);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_14() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_3, SERVICE_NAME_14);
        PssService service = new PssService(serviceId, SERVICE_TYPE_1, ONTOLOGY_URI_14, SERVICE_URI_14);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_14);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_14);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_15() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_15);
        PssService service = new PssService(serviceId, SERVICE_TYPE_1, ONTOLOGY_URI_15, SERVICE_URI_15);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_15);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_15);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_16() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_16);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_16, SERVICE_URI_16);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_16);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_16);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_17() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_17);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_17, SERVICE_URI_17);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_17);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_17);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_18() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_18);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_18, SERVICE_URI_18);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_18);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_18);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_19() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_19);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_19, SERVICE_URI_19);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_19);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_19);
        return service;
    }

    /**
     * Create a PssService 
     * 
     * @return
     */
    public static PssService createService_20() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(PEER_ID_2, SERVICE_NAME_20);
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_20, SERVICE_URI_20);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_20);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_20);
        return service;
    }
}
