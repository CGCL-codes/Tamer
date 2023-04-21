package org.personalsmartspace.pss_sm_synchroniser.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.personalsmartspace.pss_sm_api.impl.PssService;
import org.personalsmartspace.pss_sm_api.impl.PssServiceVisibility;
import org.personalsmartspace.pss_sm_dbc.impl.Dbc;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.api.pss3p.PssServiceIdentifier;

public class Utils {

    public static final String PUBLIC_PSS_DPI = "DPIOne";

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
     * Serialise an object
     *
     * @param outputFile
     * @param object
     * @throws Exception
     */
    public static void serialiseObject(String outputFile, Object object) throws Exception {
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
        os.writeObject(object);
        os.close();
    }

    /**
     * Deserialise a serialised object
     *
     * @param serialFile
     * @return
     * @throws Exception
     */
    public static Object deserialiseObject(String serialFile) throws Exception {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(serialFile));
        Object obj = is.readObject();
        is.close();
        return obj;
    }

    public static boolean fileExists(File file) {
        return file.exists();
    }

    public static boolean deleteFile(File file) {
        boolean retValue = false;
        if (fileExists(file)) {
            retValue = file.delete();
        }
        return retValue;
    }

    public static long fileSize(File file) {
        long retValue = 0;
        if (fileExists(file)) {
            retValue = file.length();
        }
        return retValue;
    }

    /**
     * Create an internal service
     * 
     * @return
     */
    public static PssService createService_1() {
        Properties serviceProps;
        serviceProps = new Properties();
        serviceProps.setProperty(PROP_1_KEY_1, PROP_1_VALUE_1);
        serviceProps.setProperty(PROP_1_KEY_2, PROP_1_VALUE_2);
        serviceProps.setProperty(PROP_1_KEY_3, PROP_1_VALUE_3);
        IServiceIdentifier serviceId = new PssServiceIdentifier(SERVICE_NAME_1, PUBLIC_PSS_DPI);
        Dbc.ensure(null != serviceId.getOperatorId());
        Dbc.ensure(null != serviceId.getLocalServiceId());
        PssService service = new PssService(serviceId, SERVICE_TYPE_1, ONTOLOGY_URI_1, SERVICE_URI_1);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_1);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_1);
        return service;
    }

    /**
     * Create an internal service 
     * 
     * @return
     */
    public static PssService createService_2() {
        Properties serviceProps;
        serviceProps = new Properties();
        serviceProps.setProperty(PROP_2_KEY_1, PROP_2_VALUE_1);
        IServiceIdentifier serviceId = new PssServiceIdentifier(SERVICE_NAME_2, PUBLIC_PSS_DPI);
        Dbc.ensure(null != serviceId.getOperatorId());
        Dbc.ensure(null != serviceId.getLocalServiceId());
        PssService service = new PssService(serviceId, SERVICE_TYPE_1, ONTOLOGY_URI_2, SERVICE_URI_2);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_2);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_2);
        return service;
    }

    /**
     * Create an internal service 
     * 
     * @return
     */
    public static PssService createService_3() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(SERVICE_NAME_3, PUBLIC_PSS_DPI);
        Dbc.ensure(null != serviceId.getOperatorId());
        Dbc.ensure(null != serviceId.getLocalServiceId());
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_3, SERVICE_URI_3);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_3);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_3);
        return service;
    }

    /**
     * Create an internal service 
     * 
     * @return
     */
    public static PssService createService_4() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(SERVICE_NAME_4, PUBLIC_PSS_DPI);
        Dbc.ensure(null != serviceId.getOperatorId());
        Dbc.ensure(null != serviceId.getLocalServiceId());
        PssService service = new PssService(serviceId, SERVICE_TYPE_1, ONTOLOGY_URI_4, SERVICE_URI_4);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_4);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_4);
        return service;
    }

    /**
     * Create an internal service  
     * 
     * @return
     */
    public static PssService createService_5() {
        Properties serviceProps;
        serviceProps = new Properties();
        IServiceIdentifier serviceId = new PssServiceIdentifier(SERVICE_NAME_5, PUBLIC_PSS_DPI);
        Dbc.ensure(null != serviceId.getOperatorId());
        Dbc.ensure(null != serviceId.getLocalServiceId());
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
        IServiceIdentifier serviceId = new PssServiceIdentifier(SERVICE_NAME_6, OPERATOR_ID_2);
        Dbc.ensure(null != serviceId.getOperatorId());
        Dbc.ensure(null != serviceId.getLocalServiceId());
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
        IServiceIdentifier serviceId = new PssServiceIdentifier(SERVICE_NAME_7, OPERATOR_ID_3);
        Dbc.ensure(null != serviceId.getOperatorId());
        Dbc.ensure(null != serviceId.getLocalServiceId());
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
        IServiceIdentifier serviceId = new PssServiceIdentifier(SERVICE_NAME_8, OPERATOR_ID_3);
        Dbc.ensure(null != serviceId.getOperatorId());
        Dbc.ensure(null != serviceId.getLocalServiceId());
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
        IServiceIdentifier serviceId = new PssServiceIdentifier(SERVICE_NAME_9, OPERATOR_ID_3);
        Dbc.ensure(null != serviceId.getOperatorId());
        Dbc.ensure(null != serviceId.getLocalServiceId());
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
        IServiceIdentifier serviceId = new PssServiceIdentifier(SERVICE_NAME_10, OPERATOR_ID_3);
        Dbc.ensure(null != serviceId.getOperatorId());
        Dbc.ensure(null != serviceId.getLocalServiceId());
        PssService service = new PssService(serviceId, SERVICE_TYPE_2, ONTOLOGY_URI_10, SERVICE_URI_10);
        service.setAtomicService(true);
        service.setServiceDescription(SERVICE_DESCRIPTION_10);
        service.setServiceQualifiers(serviceProps);
        service.setVersionNumber(VERSION_NO_10);
        return service;
    }
}
