package hu.cubussapiens.modembed.notation.highlevelprogram;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class HighLevelProgramNotationStandaloneSetup extends HighLevelProgramNotationStandaloneSetupGenerated {

    public static void doSetup() {
        new HighLevelProgramNotationStandaloneSetup().createInjectorAndDoEMFRegistration();
    }
}
