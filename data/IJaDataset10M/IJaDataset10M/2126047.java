package fr.univartois.cril.xtext;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class AlsStandaloneSetup extends AlsStandaloneSetupGenerated {

    public static void doSetup() {
        new AlsStandaloneSetup().createInjectorAndDoEMFRegistration();
    }
}
