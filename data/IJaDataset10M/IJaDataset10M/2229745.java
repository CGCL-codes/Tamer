package de.abg.jreichert.serviceqos.model.wsdl.xtext;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class ServiceQosWsdlStandaloneSetup extends ServiceQosWsdlStandaloneSetupGenerated {

    public static void doSetup() {
        new ServiceQosWsdlStandaloneSetup().createInjectorAndDoEMFRegistration();
    }
}
