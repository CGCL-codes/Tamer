package de.abg.jreichert.serviceqos;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class MeasurementStandaloneSetup extends MeasurementStandaloneSetupGenerated {

    public static void doSetup() {
        new MeasurementStandaloneSetup().createInjectorAndDoEMFRegistration();
    }
}
