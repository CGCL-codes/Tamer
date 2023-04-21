package de.bea.environment;

/**
 * Die Klasse AppInit ist f�r die Initialisierung der
 * verwendeten Produkte verantwortlich.
 * 
 * Diese Init-Klasse stellt sicher, dass jedes Feature nur 1 Mal
 * instanziert wird und gibt bei getFeature(featureClassname) stets 
 * die gleiche Instanz zur�ck.
 * 
 * Der eigentliche Initialisierungs-Code muss im Konstruktor
 * implementiert werden.
 */
public final class AppInit {

    private static Init appInstance;

    static {
        appInstance = Init.createInstance();
    }

    public static void setEnvironmentErrorHandler(EnvironmentErrorHandler environmentErrorHandler) {
        appInstance.setEnvironmentErrorHandler(environmentErrorHandler);
    }

    /**
     * Installs the feature if not available and returns always the
     * same instance.
    */
    public static Object getFeature(Class featureClassname) {
        return appInstance.getFeature(featureClassname);
    }

    public static Init.State clearFeatures() {
        return appInstance.clearFeatures();
    }

    public static void restoreFeatures(Init.State state) {
        appInstance.restoreFeatures(state);
    }

    /**
     * Constructor for AppInit.
     */
    private AppInit() {
    }
}
