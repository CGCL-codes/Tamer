package de.bea.environment;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Die Klasse Init ist f�r die Initialisierung der
 * verwendeten Produkte verantwortlich.
 * 
 * Diese Init-Klasse stellt sicher, dass jedes Feature nur 1 Mal
 * instanziert wird und gibt bei getFeature(featureClassname) stets 
 * die gleiche Instanz zur�ck.
 * 
 * Der eigentliche Initialisierungs-Code muss im Konstruktor
 * implementiert werden.
 */
public final class Init {

    private Map featureMap;

    private final Set inProgress;

    private EnvironmentErrorHandler environmentErrorHandler;

    /**
     * @return a new instance
    */
    public static Init createInstance() {
        return new Init();
    }

    /**
     * Installs the feature if not available and returns always the
     * same instance.
    */
    public Object getFeature(Class featureClassname) {
        Object feature = featureMap.get(featureClassname);
        if (feature == null) {
            synchronized (inProgress) {
                if (inProgress.contains(featureClassname)) {
                    environmentErrorHandler.cyclicInitialization(featureClassname);
                    System.err.println("-E- Cylic initialization of feature " + featureClassname);
                    System.err.println("-I- Application cannot be configured.");
                    System.exit(1);
                }
                inProgress.add(featureClassname);
            }
            try {
                Object instance;
                if (featureClassname == EnvInit.class) {
                    Constructor constructor = featureClassname.getConstructor(new Class[] { Init.class });
                    instance = constructor.newInstance(new Object[] { this });
                } else {
                    try {
                        Constructor constructor = featureClassname.getConstructor(new Class[] { EnvInit.class });
                        EnvInit envInit = (EnvInit) getFeature(EnvInit.class);
                        instance = constructor.newInstance(new Object[] { envInit });
                    } catch (NoSuchMethodException e) {
                        instance = featureClassname.newInstance();
                    }
                }
                synchronized (this) {
                    featureMap.put(featureClassname, instance);
                }
                synchronized (inProgress) {
                    inProgress.remove(featureClassname);
                }
                return instance;
            } catch (Throwable e) {
                environmentErrorHandler.exceptionOccured(featureClassname, e);
                System.err.println("-E- Error while initializing feature: " + featureClassname);
                System.err.println("-I- Application cannot be configured.");
                System.exit(1);
            }
            return null;
        } else {
            return feature;
        }
    }

    public void setEnvironmentErrorHandler(EnvironmentErrorHandler environmentErrorHandler) {
        if (environmentErrorHandler == null) {
            System.out.println("-E- setEnvironmentErrorHandler called with null argument");
        } else {
            this.environmentErrorHandler = environmentErrorHandler;
        }
    }

    public synchronized State clearFeatures() {
        State state = new State(featureMap);
        featureMap = new HashMap();
        return state;
    }

    public synchronized void restoreFeatures(State state) {
        featureMap = state.featureMap;
    }

    /**
     * Constructor for Init.
     */
    private Init() {
        featureMap = new HashMap();
        inProgress = new HashSet();
        environmentErrorHandler = new StdOutEnvironmentErrorHandler();
    }

    public static final class State {

        private final Map featureMap;

        private State(Map featureMap) {
            this.featureMap = featureMap;
        }
    }
}
